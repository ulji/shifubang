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
	private Spinner spinner;//λ��������
	private ArrayList<String> data_list;//��������б���
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
	 * ��ʼ���ؼ�
	 */
	private void init() {
		spinner = (Spinner) findViewById(R.id.spinner);		
		locationEt = (EditText) findViewById(R.id.et_location_area);
		locationBtn = (Button) findViewById(R.id.btn_location_area);
		mLocationClient = new LocationClient(this.getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������
		data_list = new ArrayList<String>();
		locationBtn.setOnClickListener(this);
		spinner.setOnItemSelectedListener(this);
		initLocation();
	}
	
	/**
	 * ��ʼ����ͼ
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 0;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}
	
	
	public class MyLocationListener implements BDLocationListener {

		@SuppressLint("ShowToast")
		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);		
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS��λ���			
				locationStr = location.getAddrStr();

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ���綨λ���
				locationStr = location.getAddrStr();
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
			
			} else {
				Toast.makeText(LocationDetailsActivity.this, "�޷���λ������λ�ã�", 0).show();
			}
			
			List<Poi> list = location.getPoiList();// POI����
			if (list != null) {
				for (Poi p : list) {				
					data_list.add(p.getName());
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}				
		        arr_adapter= new ArrayAdapter<String>(LocationDetailsActivity.this, android.R.layout.simple_spinner_item, data_list);//������		        
		        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//������ʽ		      
		        spinner.setAdapter(arr_adapter);  //����������
			}
			Log.i("BaiduLocationApiDem", sb.toString());
			mLocationClient.stop();
			if(!locationStr.equals(""))
			     locationEt.setText(locationStr);
			else
				Toast.makeText(LocationDetailsActivity.this, "�޷���λ������λ�ã�", 0).show();
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
