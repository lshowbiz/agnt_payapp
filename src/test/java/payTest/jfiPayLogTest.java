package payTest;

/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.function.controller.DemoControllerWebAppContextSetupTest.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年9月1日 上午10:34:31 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年9月1日    |    Administrator     |     Created 
 */

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;

import web.core.AbsWebAppContextSetupTest;

import com.jm.application.entity.JfiPayLog;
import com.jm.application.service.JfiPayLogService;

/**
 * Description: <类功能描述>. <br>
 * <p>
 * <使用说明>
 * </p>
 * Makedate:2014年9月1日 上午10:34:31
 * 
 * @author Administrator
 * @version V1.0
 */
public class jfiPayLogTest extends AbsWebAppContextSetupTest {
	//MockHttpServletRequest request = new MockHttpServletRequest();
	//MockHttpServletResponse response = new MockHttpServletResponse();
	// @Test
	// @Rollback(false)
	public void add() {
		JfiPayLogService s = this.wac.getBean(JfiPayLogService.class);
		JfiPayLog entity = new JfiPayLog();
		entity.setInc("2");
		entity.setUserCode("9---7536270");
		entity.setLogId(100L);
		s.saveEntity(entity);
		System.out.println(s.getEntity(100L).getUserCode());
	}

	@Test
	@Rollback(false)
	public void jfipayReceiveController() throws Exception {

		String url = "/payReceive?zmType=";
		url+="hfpay&CmdId=Buy&MerId=873983&RespCode=000000&TrxId=2018043050628993&OrdAmt=4180.00&CurCode=RMB&Pid=1183000&OrdId=904499306";
		// url+="kqpay&merchantAcctId=1002438213401&version=v2.0&language=1&signType=4&payType=10&bankId=CMB&orderId=CN20160224000030&orderAmount=3200&orderTime=20160224134504&ext1=&ext2=CN18756134%2C0%2C0.00&payAmount=3200&dealId=2155680248&bankDealId=8401512083&dealTime=20160224134632&payResult=10&errCode=&fee=10&signMsg=yR9TkDUMXYV6g3Itbk2ty6ZrQmXRO3pLeVbUtEmJ67o4NfUB3BuxLUdahhlfSN1j0dCyus00cnHj%2FIPs9KLTGEhyjf71pDcE%2FLrFox7ZUvAVJ4H4caeMy9Xz%2BJ0liIrJSPW4KYMEiLTpdMtFU9Y%2Bf5icawbf4TVKyVbSOYZIQrvyA%2ByNaQw6n8VmRIPGWG91tH4%2B2oNQsgPgzD89HslqNvL5Ecy0z39khxcVjlfPvM8nBA52gLsrYDnS3tDY3jeUNmCqYc0lA4SyTLnR3hjoov7LycNG0rTb24Smi3SlIZvHyvrkQSi5QkwazL9Q1FQPO%2FAkCB%2BDdfosieRzfNyy%2Bw%3D%3D";
		// url+="yspay&src=ysepay&msgCode=R3501&msgId=20151126105935-234              &check=s3VCyPq07PHHB5c5e+13WYCerApT/JoQclFRQoLGhM+1qgU3+4I6ykV4GpujEAZvRb++84WStri0FgoG3Q5kCjxITYcF6h8jijOAsdWySEnCaUrxt5zUdefgHcE1u/ahK6hoGB55Ly69stqV07OQ+yfT9mv0b8vCXA04pyzn/pE=                                                                                    &msg=PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iR0JLIj8+Cjx5c3BheT48aGVhZD48VmVyPjEuMDwvVmVyPjxTcmM+eXNlcGF5PC9TcmM+PE1zZ0NvZGU+UjM1MDE8L01zZ0NvZGU+PFRpbWU+MjAxNTExMjYxMDU5MzU8L1RpbWU+PC9oZWFkPjxib2R5PjxPcmRlcj48T3JkZXJJZD4xNzM2MzM0MTwvT3JkZXJJZD48U2hvcERhdGU+MjAxNTExMjY8L1Nob3BEYXRlPjxDdXI+Q05ZPC9DdXI+PEFtb3VudD4xPC9BbW91bnQ+PFJlbWFyaz5bQ045MzEyODIyNiwxLGxoenJteV08L1JlbWFyaz48L09yZGVyPjxSZXN1bHQ+PEJ1c2lTdGF0ZT4wMDwvQnVzaVN0YXRlPjxDb2RlPjAwMDA8L0NvZGU+PE5vdGU+vLTKsbW91cvWp7i2s8m5pjwvTm90ZT48VHJhZGVTTj4zMTExNTExMjYzMjIzOTkxODc8L1RyYWRlU04+PEZlZT4wPC9GZWU+PC9SZXN1bHQ+PC9ib2R5PjwveXNwYXk+&";
		//url+="ybpay&p1_MerId=10012562324&r0_Cmd=Buy&r1_Code=1&r2_TrxId=314720117078124I&r3_Amt=0.01&r4_Cur=RMB&r5_Pid=&r6_Order=17375520&r7_Uid=&r8_MP=CN83735733%2C1&r9_BType=2&ru_Trxtime=20160107102401&ro_BankOrderId=29097923681601&rb_BankId=ICBC-NET&rp_PayDate=20160107102331&rq_CardNo=&rq_SourceFee=0.0&rq_TargetFee=0.0&hmac=4191630643cbab9c5e219e15baede164";
		//url = URLDecoder.decode(url, "utf-8");
		MvcResult result = mockMvc.perform(post(url)).andReturn();
		result.getResponse().setCharacterEncoding("UTF-8");
		byte[] b = result.getResponse().getContentAsString().getBytes("utf-8");
		System.out.println(new String(b, "ISO-8859-1"));/**/
	}

	//@Test
	public void select() {
		JfiPayLogService s = this.wac.getBean(JfiPayLogService.class);
		List<Map<String, Object>> list = s.getJpoMemberOrder("17352795");
		System.out.println(list.size());
		//workByKeySet(list.get(0));
	}

	public void workByKeySet(Map<String, Object> map) {
		for (String key : map.keySet()) {
			System.out.println("key= " + key + "  and  value= " + map.get(key));
		}
	}
}
