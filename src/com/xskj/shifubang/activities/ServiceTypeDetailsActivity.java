package com.xskj.shifubang.activities;

import java.util.HashMap;
import java.util.Iterator;

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
import com.xskj.shifubang.cropimage.ChooseServiceCategoryDialog;
import com.xskj.shifubang.cropimage.ChooseServiceDialog;
import com.xskj.shifubang.cropimage.ChooseServiceLampsDialog;
import com.xskj.shifubang.cropimage.ChooseServiceTransportDialog;

public class ServiceTypeDetailsActivity extends Activity implements
		OnClickListener {
	private TextView furnitureTv; // �Ҿ߷���
	private TextView lampTv; // �ƾ߷���
	private TextView categoryTv; // �������
	private TextView transportTv; // �������
	private ImageView returnBtn; // ���ذ�ť
	public String[] sendCode; // ����ѡ����
	public String[] sendStr; // ����ѡ������
	public ChooseServiceDialog serviceDialog; // ������
	public ChooseServiceLampsDialog serviceLampsDialog; // �ƾߵ����򣬵���һ��
	public ChooseServiceCategoryDialog serviceCategoryDialog; // ���൯����
	public ChooseServiceTransportDialog serviceTransportDialog; // ���䵯����
	public Button informationBtn;
	public String[] serviceTypeStr = null; // ѡ�����ͼ���
	public String[] serviceNameTypeStr = null; // ѡ������������
	public static HashMap<String, String> mapAll = null; // map����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_type_details);
		init();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		furnitureTv = (TextView) findViewById(R.id.tv_service_furniture);
		returnBtn = (ImageView) findViewById(R.id.information_return);
		lampTv = (TextView) findViewById(R.id.tv_service_lamp);
		categoryTv = (TextView) findViewById(R.id.tv_service_category);
		transportTv = (TextView) findViewById(R.id.tv_service_transport);
		informationBtn = (Button) findViewById(R.id.btn_information_ok);
		serviceDialog = new ChooseServiceDialog(ServiceTypeDetailsActivity.this);
		serviceLampsDialog = new ChooseServiceLampsDialog(
				ServiceTypeDetailsActivity.this);
		serviceCategoryDialog = new ChooseServiceCategoryDialog(
				ServiceTypeDetailsActivity.this);
		serviceTransportDialog = new ChooseServiceTransportDialog(
				ServiceTypeDetailsActivity.this);
		furnitureTv.setOnClickListener(this);
		lampTv.setOnClickListener(this);
		categoryTv.setOnClickListener(this);
		informationBtn.setOnClickListener(this);
		transportTv.setOnClickListener(this);
		returnBtn.setOnClickListener(this);
		mapAll = new HashMap<String, String>();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_service_furniture:
			sendStr = new String[] { "���Ͱ�װ", "��װ����", "���ͷ���", "ά�ޱ���" };
			sendCode = new String[] { "3", "4", "2", "6" };
			serviceDialog.popSelectDialog(sendStr, sendCode);
			break;
		case R.id.tv_service_lamp:
			sendStr = new String[] { "�ƾ߰�װ" };
			sendCode = new String[] { "22" };
			serviceLampsDialog.popSelectDialog(sendStr, sendCode);

			break;
		case R.id.tv_service_transport:
			sendStr = new String[] { "�������" };
			sendCode = new String[] { "16" };
			serviceTransportDialog.popSelectDialog(sendStr, sendCode);

			break;
		case R.id.tv_service_category:
			sendStr = new String[] { "�������Ͱ�װ", "���లװ����", "�������ͷ���", "����ά�ޱ���" };
			sendCode = new String[] { "18", "19", "21", "20" };
			serviceCategoryDialog.popSelectDialog(sendStr, sendCode);
			break;
		case R.id.btn_information_ok:
			getServiceTypeData();
			finish();
			break;
		case R.id.information_return:		
			finish();
			break;
		}
	}

	

	/**
	 * ��ȡ�������͵�idֵ�����浽 serviceTypeStr String������
	 */
	private void getServiceTypeData() {
		Log.e("All():", mapAll.size() + "");
		serviceTypeStr = new String[60];
		serviceNameTypeStr = new String[60];
		@SuppressWarnings("rawtypes")
		Iterator iter = mapAll.entrySet().iterator();
		int i = 0;

		while (iter.hasNext()) {
			@SuppressWarnings("rawtypes")
			HashMap.Entry entry = (HashMap.Entry) iter.next();
			Object _id = entry.getKey();
			Object val = entry.getValue();
			serviceTypeStr[i] = val.toString();
			serviceNameTypeStr[i] = _id.toString() + " ";
			i++;
		}
		if(mapAll.size()>0) {			
			sendBrod();
		}
	
	}
	
	/**
	 * ʹ�ù㲥�������ݷ��͵�ʦ����ס����
	 */
	private void sendBrod() {
		Intent intent = new Intent("ServiceType");
		intent.putExtra("ServiceTypeData", serviceTypeStr);
		intent.putExtra("serviceNameTypeStr", serviceNameTypeStr);
		sendBroadcast(intent);
	}

}
