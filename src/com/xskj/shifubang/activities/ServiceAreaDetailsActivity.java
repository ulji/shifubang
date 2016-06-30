package com.xskj.shifubang.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zshifu.R;
import com.xskj.shifubang.cropimage.ChooseLocationDialog;
import com.xskj.shifubang.view.WordWrapView;

public class ServiceAreaDetailsActivity extends Activity implements
		OnClickListener {
	private Button addBtn; // ��Ӱ�ť
	private Button sendBtn; // �ύ��ť
	private ImageView returnBtn; //���ذ�ť
	private TextView textview;

	ChooseLocationDialog locationDialog;
	public static int i = 0;
	public static WordWrapView wordWrapView;

	private String erviceAreaDB;
	public String[] AreaIdStr=null; //����id�ļ���
	public String[] AreanameStr=null; //�������ļ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_area_details);
		init();

	}

	private void init() {
		addBtn = (Button) findViewById(R.id.btn_settled_service_area);
		sendBtn = (Button) findViewById(R.id.btn_information_ok);		
		returnBtn = (ImageView) findViewById(R.id.information_return);		
		textview = new TextView(this);
		wordWrapView = (WordWrapView) this.findViewById(R.id.view_wordwrap);
		locationDialog = new ChooseLocationDialog(ServiceAreaDetailsActivity.this, textview, erviceAreaDB);
		addBtn.setOnClickListener(this);
		wordWrapView.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		returnBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btn_settled_service_area:
			locationDialog.popSelectDialog();
			break;
		case R.id.btn_information_ok:
			getCityData();
			finish();
			break;
		case R.id.information_return:
			finish();
			break;
		default:
			wordWrapView.removeView(wordWrapView.findViewById(v.getId()));
			i = wordWrapView.getChildCount();
			break;

		}
	}

	/**
	 * ��ȡ��������
	 */
	private void getCityData() {
		AreaIdStr = new String[wordWrapView.getChildCount()]; //Ҫ��ӵĵ��������ܳ���
		AreanameStr = new String[wordWrapView.getChildCount()]; //Ҫ��ӵĵ���������
        for(int i = 0;i<wordWrapView.getChildCount();i++) {
        	TextView tv = (TextView) wordWrapView.getChildAt(i);   		
        		AreaIdStr[i] = tv.getId()+""; 
        		AreanameStr[i] = tv.getText()+" "; 
        	    Log.e("aaa:", tv.getText()+" "+tv.getId()+"\n");
        }
        if(wordWrapView.getChildCount()>0){        	
        	sendBrod();
        }
	}

	/**
	 * ʹ�ù㲥�������ݷ��͵�ʦ����ס����
	 */
	private void sendBrod() {
		Intent intent = new Intent("Service");
		intent.putExtra("AreaIdStr", AreaIdStr);
		intent.putExtra("AreanameStr", AreanameStr);
		sendBroadcast(intent);
	}
	
}
