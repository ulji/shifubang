package com.xskj.shifubang.db;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPerferences {

	public static SharedPreferences sharedPreferences; // ���ػ������ݿ�
	public static Editor editor; // ��ȡsharedPreferences�ı༭��
	
	/**
	 * ���ػ����ʦ����Ϣ
	 */
	
	@SuppressLint("CommitPrefEdits")
	public void saveSharedPreferences(Activity aty) {
		sharedPreferences = aty.getSharedPreferences("workerPersonalData", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();// ��ȡ�༭��		
		
	}
}
