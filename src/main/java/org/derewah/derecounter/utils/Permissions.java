package org.derewah.derecounter.utils;

public class Permissions {


	public static String ADMIN_PERMISSION = "derecounter.admin";

	private static final String USE_PERMISSION = "derecounter.use.%company%";
	private static final String SELL_PERMISSION = USE_PERMISSION+".sell";
	private static final String DEPOSIT_PERMISSION = USE_PERMISSION+".deposit";
	private static final String WITHDRAW_PERMISSION = USE_PERMISSION+".withdraw";


	public static String getDepositPermission(String companyName){
		return DEPOSIT_PERMISSION.replace("%company%", companyName);
	}

	public static String getWithdrawPermission(String companyName){
		return WITHDRAW_PERMISSION.replace("%company%", companyName);
	}

	public static String getSellPermission(String companyName){
		return SELL_PERMISSION.replace("%company%", companyName);
	}

	public static String getUsePermission(String companyName){
		return USE_PERMISSION.replace("%company%", companyName);
	}


}
