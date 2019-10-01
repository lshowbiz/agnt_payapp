package com.jm.application.utils.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayConstants {
	public static Map<String,String> payCompany = new HashMap<String,String>();
	public static List<String> companyList = new ArrayList<String>();
	static{
		payCompany.put("1", "快钱");
		payCompany.put("2", "畅捷通");
		payCompany.put("3", "盛付通");
		payCompany.put("4", "宝易互通");
		payCompany.put("5", "易宝");
		payCompany.put("6", "汇付天下");
		payCompany.put("7", "顺手付");
		payCompany.put("8", "银盛");
	}
}
