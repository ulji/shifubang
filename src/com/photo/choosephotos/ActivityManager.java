package com.photo.choosephotos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
/**
 * Activity������
 * @author Lih
 *
 */
public class ActivityManager {
	/**
	 * ���Activity��map
	 */
	private static Map<String, Activity> activitys = new HashMap<String, Activity>();

	/**
	 * ��ȡ��������ע�������Activity��map
	 * @return
	 */
	public static Map<String, Activity> getActivitys()
	{
		return activitys;
	}

	/**
	 * ���ݼ�ֵȡ��Ӧ��Activity
	 * @param key ��ֵ
	 * @return ��ֵ��Ӧ��Activity
	 */
	public static Activity getActivity(String key)
	{
		return activitys.get(key);
	}
	
	/**
	 * ע��Activity
	 * @param value
	 * @param key
	 */
	public static void addActivity(Activity value,String key)
	{
		activitys.put(key, value);
	}
	
	/**
	 * ��key��Ӧ��Activity�Ƴ���
	 * @param key
	 */
	public static void removeActivity(String key)
	{
		activitys.remove(key);
	}
	
	/**
	 * finish�����е�Activity�Ƴ����е�Activity
	 */
	public static void removeAllActivity()
	{
		Iterator<Activity> iterActivity = activitys.values().iterator();
		while(iterActivity.hasNext()){
			iterActivity.next().finish();
		}
		activitys.clear();
	}
}
