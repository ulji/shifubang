package com.xskj.shifubang.db;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharedPerferences {

	public static SharedPreferences sharedPreferences; // 本地缓存数据库
	public static Editor editor; // 获取sharedPreferences的编辑器
	
	/**
	 * 本地缓存的师傅信息
	 */
	
	@SuppressLint("CommitPrefEdits")
	public void saveSharedPreferences(Activity aty) {
		sharedPreferences = aty.getSharedPreferences("workerPersonalData", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();// 获取编辑器		
		
	}
}
