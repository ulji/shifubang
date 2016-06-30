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
	private TextView furnitureTv; // 家具服务
	private TextView lampTv; // 灯具服务
	private TextView categoryTv; // 门类服务
	private TextView transportTv; // 运输服务
	private ImageView returnBtn; // 返回按钮
	public String[] sendCode; // 服务选项编号
	public String[] sendStr; // 服务选项文字
	public ChooseServiceDialog serviceDialog; // 弹出框
	public ChooseServiceLampsDialog serviceLampsDialog; // 灯具弹出框，弹出一个
	public ChooseServiceCategoryDialog serviceCategoryDialog; // 门类弹出框
	public ChooseServiceTransportDialog serviceTransportDialog; // 运输弹出框
	public Button informationBtn;
	public String[] serviceTypeStr = null; // 选择类型集合
	public String[] serviceNameTypeStr = null; // 选择类型名集合
	public static HashMap<String, String> mapAll = null; // map集合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_type_details);
		init();
	}

	/**
	 * 初始化控件
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
			sendStr = new String[] { "配送安装", "安装服务", "运送服务", "维修保养" };
			sendCode = new String[] { "3", "4", "2", "6" };
			serviceDialog.popSelectDialog(sendStr, sendCode);
			break;
		case R.id.tv_service_lamp:
			sendStr = new String[] { "灯具安装" };
			sendCode = new String[] { "22" };
			serviceLampsDialog.popSelectDialog(sendStr, sendCode);

			break;
		case R.id.tv_service_transport:
			sendStr = new String[] { "运输服务" };
			sendCode = new String[] { "16" };
			serviceTransportDialog.popSelectDialog(sendStr, sendCode);

			break;
		case R.id.tv_service_category:
			sendStr = new String[] { "门类配送安装", "门类安装服务", "门类运送服务", "门类维修保养" };
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
	 * 获取服务类型的id值，保存到 serviceTypeStr String数组中
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
	 * 使用广播来把数据发送到师傅入住界面
	 */
	private void sendBrod() {
		Intent intent = new Intent("ServiceType");
		intent.putExtra("ServiceTypeData", serviceTypeStr);
		intent.putExtra("serviceNameTypeStr", serviceNameTypeStr);
		sendBroadcast(intent);
	}

}
