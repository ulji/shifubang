package com.xskj.shifubang.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.zshifu.R;

public class LocationDetailsActivity extends Activity implements OnClickListener,OnItemSelectedListener{

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private Spinner spinner;//位置下拉框
	private ArrayList<String> data_list;//下拉框的列表集合
	private ArrayAdapter<String> arr_adapter; 
	private EditText locationEt;
	private Button locationBtn;
	public String locationStr="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_details);
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		spinner = (Spinner) findViewById(R.id.spinner);		
		locationEt = (EditText) findViewById(R.id.et_location_area);
		locationBtn = (Button) findViewById(R.id.btn_location_area);
		mLocationClient = new LocationClient(this.getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		data_list = new ArrayList<String>();
		locationBtn.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
		initLocation();
	}
	
	/**
	 * 初始化地图
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 0;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}
	
	
	public class MyLocationListener implements BDLocationListener {

		@SuppressLint("ShowToast")
		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);		
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果			
				locationStr = location.getAddrStr();

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				locationStr = location.getAddrStr();
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			
			} else {
				Toast.makeText(LocationDetailsActivity.this, "无法定位到您的位置！", 0).show();
			}
			
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				for (Poi p : list) {				
					data_list.add(p.getName());
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}				
		        arr_adapter= new ArrayAdapter<String>(LocationDetailsActivity.this, android.R.layout.simple_spinner_item, data_list);//适配器		        
		        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置样式		      
		        spinner.setAdapter(arr_adapter);  //加载适配器
			}
			Log.i("BaiduLocationApiDem", sb.toString());
			mLocationClient.stop();
			if(!locationStr.equals(""))
			     locationEt.setText(locationStr);
			else
				Toast.makeText(LocationDetailsActivity.this, "无法定位到您的位置！", 0).show();
		}
	}
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_location_area:
			mLocationClient.start();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		//locationEt.setText(data_list.get(position));
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
