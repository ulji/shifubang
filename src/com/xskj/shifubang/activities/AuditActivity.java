package com.xskj.shifubang.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zshifu.R;

public class AuditActivity extends Activity implements OnClickListener{
    ImageView returnBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audit);
		init();
	}
	
	/**
	 * ³õÊ¼»¯¿Ø¼þ
	 */
	private void init() {
		
		returnBtn = (ImageView) findViewById(R.id.audit_return);
		returnBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
		startActivity(new Intent(AuditActivity.this,LoginActivity.class));
	}


}
