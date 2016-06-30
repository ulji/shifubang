package com.xskj.shifubang.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 功能：
 * 1.验证是否邮箱
 * 2.手机号码
 * 3.密码验证
 * @author 爱民
 *  时间：2016-5-9
 */
public class IsPhoneEmail {

	/**
	 * 验证是否是手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		/* 
	    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
	    联通：130、131、132、152、155、156、185、186 
	    电信：133、153、180、189、（1349卫通） 
	    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) {	    	
	    	return false;  
	    }
	    else {	    	
	    	return mobiles.matches(telRegex);  
	    } 
	}
	
	/**
	* 验证邮箱输入是否合法
	*
	* @param strEmail
	* @return
	*/
	public static boolean isEmail(String strEmail) {
	// String strPattern =
	// "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";


	Pattern p = Pattern.compile(strPattern);
	Matcher m = p.matcher(strEmail);
	return m.matches();
	}
	
}
