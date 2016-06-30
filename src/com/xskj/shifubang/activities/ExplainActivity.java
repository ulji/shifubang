package com.xskj.shifubang.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.zshifu.R;

public class ExplainActivity extends Activity {
	private TextView explainTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_explain);
		init();
	}

	/**
	 * ³õÊ¼»¯¿Ø¼þ
	 */
	private void init() {
		explainTv = (TextView) findViewById(R.id.tv_explain);
	}

}
