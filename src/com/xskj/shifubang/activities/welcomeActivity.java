package com.xskj.shifubang.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

import com.zshifu.R;

public class welcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		new MyThread().start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();

			}
			startActivity(new Intent(welcomeActivity.this, LoginActivity.class));
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			finish();
		}
	}

}
