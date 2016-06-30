package com.xskj.shifubang.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * ���ܣ�
 * 1.��֤�Ƿ�����
 * 2.�ֻ�����
 * 3.������֤
 * @author ����
 *  ʱ�䣺2016-5-9
 */
public class IsPhoneEmail {

	/**
	 * ��֤�Ƿ����ֻ�����
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		/* 
	    �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188 
	    ��ͨ��130��131��132��152��155��156��185��186 
	    ���ţ�133��153��180��189����1349��ͨ�� 
	    �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��  
	    if (TextUtils.isEmpty(mobiles)) {	    	
	    	return false;  
	    }
	    else {	    	
	    	return mobiles.matches(telRegex);  
	    } 
	}
	
	/**
	* ��֤���������Ƿ�Ϸ�
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
