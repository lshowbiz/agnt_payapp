package com.jm.application.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

import com.jm.application.dao.HibernateDAO;

/**
 * 描述: <类功能描述>. <br>
 * <p>
 * 数据库操作类,包括条件组合查询
 * </p>
 */
@SuppressWarnings("unchecked")
public class HibernateDAOImpl implements HibernateDAO {

	private Logger logger = Logger.getLogger(HibernateDAOImpl.class);

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private void lazyInitialize(Class<?> entityClazz, List<?> l, String[] fields) {
		if (fields != null) {
			for (String field : fields) {

				String targetMethod = "get" + upperFirstWord(field);

				Method method;
				try {
					method = entityClazz.getDeclaredMethod(targetMethod);
					for (Object o : l) {
						/*
						 * 在Session关闭之前不调用Obj.getXxx()方法而关闭Session之后又要用，
						 * 此时只要在Session关闭之前调用Hibernate.initialize(Obj)
						 * 或者Hibernate.initialize(Obj.getXxx())即可
						 */
						Hibernate.initialize(method.invoke(o));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private void lazyInitialize(Class<?> entityClazz, Object obj, String[] fields) {
		if (fields != null) {
			for (String field : fields) {

				String targetMethod = "get" + upperFirstWord(field);

				Method method;
				try {
					method = entityClazz.getDeclaredMethod(targetMethod);
					Hibernate.initialize(method.invoke(obj));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private String upperFirstWord(String str) {
		StringBuffer sb = new StringBuffer(str);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	private Session getHibernateSession() {
		Session session = sessionFactory.getCurrentSession();
		return session;
	}

	@Override
	public Object get(Class<?> entityClazz, Serializable id, String... str) {
		Object obj = getHibernateSession().get(entityClazz, id);
		lazyInitialize(entityClazz, obj, str);
		return obj;
	}

	@Override
	public Object load(Class<?> entityClazz, Serializable id, String... str) {
		Object obj = getHibernateSession().load(entityClazz, id);
		lazyInitialize(entityClazz, obj, str);
		return obj;
	}

	@Override
	public Object findById(Class<?> entityClazz, Serializable id, String... str) {
		Object obj = getHibernateSession().get(entityClazz, id);
		lazyInitialize(entityClazz, obj, str);
		return obj;
	}

	@Override
	public List<?> loadAll(Class<?> entityClazz, String... str) {
		return findAll(entityClazz, str);
	}

	@Override
	public List<?> findAll(Class<?> entityClazz, String... str) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClazz);
		List<?> list = findAllByCriteria(dc);
		lazyInitialize(entityClazz, list, str);
		return list;
	}

	@Override
	public void update(Object entity) {
		getHibernateSession().update(entity);
	}

	@Override
	public void save(Object entity) {
		getHibernateSession().save(entity);
	}

	@Override
	public void saveOrUpdate(Object entity) {
		getHibernateSession().saveOrUpdate(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<?> entities) {
		logger.info(".......saveOrUpdateAll...");
		for (Object entity : entities) {
			getHibernateSession().saveOrUpdate(entity);
		}
	}

	@Override
	public void delete(Object entity) {
		getHibernateSession().delete(entity);
	}

	@Override
	public void deleteByKey(Serializable id, Class<?> entityClazz) {
		getHibernateSession().delete(load(entityClazz, id));
	}

	@Override
	public void deleteAll(Collection<?> entities) {
		for (Object entity : entities) {
			getHibernateSession().delete(entity);
		}
	}

	@Override
	public int bulkUpdate(String hql) {
		Query queryObject = getHibernateSession().createQuery(hql);
		return queryObject.executeUpdate();
	}

	@Override
	public int bulkUpdate(String hql, Object... values) {
		Query queryObject = getHibernateSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.executeUpdate();
	}

	@Override
	public List<?> findAllByHQL(String hql) {
		Query queryObject = getHibernateSession().createQuery(hql);
		return queryObject.list();
	}

	@Override
	public List<?> find(String hql, Object... values) {
		Query queryObject = getHibernateSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}

	@Override
	public Criteria createCriteria(Class<?> entityClazz) {
		return this.createDetachedCriteria(entityClazz).getExecutableCriteria(getHibernateSession());
	}

	@Override
	public DetachedCriteria createDetachedCriteria(Class<?> entityClazz) {
		return DetachedCriteria.forClass(entityClazz);
	}

	@Override
	public List<?> findByCriteria(DetachedCriteria detachedCriteria) {
		Criteria executableCriteria = detachedCriteria.getExecutableCriteria(getHibernateSession());
		return executableCriteria.list();
	}

	@Override
	public List<?> findByCriteria(final DetachedCriteria detachedCriteria, final int startIndex, final int pageSize) {

		Criteria criteria = detachedCriteria.getExecutableCriteria(getHibernateSession()).setFirstResult(startIndex).setMaxResults(pageSize);
		return criteria.list();

	}

	@Override
	public Integer getTotalCountByCriteria(final DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getHibernateSession()).setProjection(Projections.rowCount());
		Number count = (Number) criteria.uniqueResult();

		return count.intValue();

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<?> findEqualByEntity(Object entity, String[] propertyNames, Class<?> entityClazz, String... str) {
		Criteria criteria = this.createCriteria(entityClazz);
		Example exam = Example.create(entity);
		// 如果int类型的成员变量为零的话，则排除在sql语句之外
		exam.excludeZeroes();
		String[] defPropertys = getSessionFactory().getClassMetadata(entityClazz).getPropertyNames();
		for (String defProperty : defPropertys) {
			int ii = 0;
			for (ii = 0; ii < propertyNames.length; ++ii) {
				if (defProperty.equals(propertyNames[ii])) {
					criteria.addOrder(Order.asc(defProperty));
					break;
				}
			}
			if (ii == propertyNames.length) {
				exam.excludeProperty(defProperty);
			}
		}
		criteria.add(exam);
		List list = criteria.list();
		lazyInitialize(entityClazz, list, str);
		return list;
	};

	@Override
	public Object merge(Object entity) {
		return getHibernateSession().merge(entity);
	}

	@Override
	public List<?> findByNamedParam(String queryString, String[] paramNames, Object[] values) {
		Query queryObject = getHibernateSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		return queryObject.list();
	}

	@SuppressWarnings("rawtypes")
	protected void applyNamedParameterToQuery(Query queryObject, String paramName, Object value) throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	@Override
	public List<?> findByNamedQuery(String queryName) {
		Query queryObject = getHibernateSession().createQuery(queryName);

		return queryObject.list();
	}

	@Override
	public List<?> findByNamedQuery(String queryName, Object[] values) {
		Query queryObject = getHibernateSession().getNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject.list();
	}

	@Override
	public List<?> findByNamedQueryAndNamedParam(String queryName, String[] paramNames, Object[] values) {
		Query queryObject = getHibernateSession().getNamedQuery(queryName);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				applyNamedParameterToQuery(queryObject, paramNames[i], values[i]);
			}
		}
		return queryObject.list();
	}

	@Override
	public void flush() {
		getHibernateSession().flush();
	}

	@Override
	public void initialize(Object proxy) {
		Hibernate.initialize(proxy);
	}

	@Override
	public List<?> findAllByCriteria(DetachedCriteria detachedCriteria) {
		// TODO Auto-generated method stub
		Criteria criteria = detachedCriteria.getExecutableCriteria(getHibernateSession());
		return criteria.list();
	}

	@Override
	public List<?> getListBySql(String sql) {
		// TODO Auto-generated method stub
		logger.info("getListBySql=" + sql);
		final String tempsql = sql;
		List<?> list = getHibernateSession().createSQLQuery(tempsql).list();
		return list;
	}

	@Override
	public List<Map<String, Object>> getListMapBySql(String sql) {
		// TODO Auto-generated method stub
		logger.info("getListMapBySql=" + sql);
		final String tempsql = sql;
		Query query = getHibernateSession().createSQLQuery(tempsql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.list();
	}

	public int updateBySQL(String sql) {
		logger.info("updateBySQL=" + sql);
		final String tempsql = sql;
		SQLQuery sqlQuery = getHibernateSession().createSQLQuery(tempsql);
		return sqlQuery.executeUpdate();

	}

}
