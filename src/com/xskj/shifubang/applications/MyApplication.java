package com.xskj.shifubang.applications;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application{

	  @Override
	    public void onCreate() {    	     
	         super.onCreate();
	         JPushInterface.setDebugMode(true); 	// ���ÿ�����־,����ʱ��ر���־
	         JPushInterface.init(this);     		// ��ʼ�� JPush
	    }
}
