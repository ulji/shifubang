package com.xskj.shifubang.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jpushdemo.MyReceiver;
import com.zshifu.R;
import com.xskj.shifubang.activities.SettledActivity.ServiceBroad;
import com.xskj.shifubang.adapter.OrdersDetailsAdapter;
import com.xskj.shifubang.been.DetailOrdersBeen;
import com.xskj.shifubang.been.DetailOrdersBeen.ListEntity;
import com.xskj.shifubang.db.MySharedPerferences;
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.Utils;
import com.xskj.shifubang.view.ProgersssDialogTools;

@SuppressLint("ResourceAsColor")
public class OrderDetailsActivity extends Activity implements OnClickListener,View.OnTouchListener {

	private RequestQueue requestQueue; // 请求队列
	public ProgersssDialogTools progressDialog; // 加载圈

	private String loginName; // 师傅账号
	private List<ListEntity> listImg; // 图片的集合
	private List<DetailOrdersBeen.T> list; // 实体类中的list集合
	TextView descriptionTv; // 描述

	
	private TextView buyerNameTv; // 买家姓名
	private TextView buyerPhoneTv; // 买家电话
	private TextView elevatorTv; // 电梯
	private TextView column4Tv; // 详细地址	
	private TextView servicePriceTv; // 价格
	private ListView listView; // 动态添加部分
	private ArrayList<HashMap<String, String>> list1; //适配器用到的集合
	private OrdersDetailsAdapter adaper; //适配器
	
	Button statusBtn; // 状态
	 
	ImageButton returnBtn; // 返回键
	TextView releaseTimeTv; // 时间	
	private String status = ""; // 接单状态值
	
	
	String result; // 返回值
	ArrayList<String> arrayList;// 图片地址集合
	HashMap<String, String> mapAdd;

	LinearLayout ll_hide_logisitics; // 隐藏的布局收货地址电话
	LinearLayout ll_hide; // 隐藏的布局
	EditText et_nowTime; // 编辑框
	TextView tv_NowTime; //显示框
	TextView tv_logisiticsPhone; //显示框取货电话
	TextView tv_title; //标题
	private TextView tv_logisiticsAddress; //显示框取货地址
	//TextView tv_logisiticsNotes; //显示框收货备注
	
	private String sendAddress ;
	private String orderCreateTime ;
	private String orderId; 
	private String column3 ;
	private String column1 ;
	private String orderState ;
	private String servicePrice ;
	private String serviceTime ;
	private String column4 ;
	private String buyerName ;
	private String buyerPhone ;
	private String elevator ;


	boolean onStatue = false;
	

	// for receive customer msg from jpush server
	// private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";	
	public HashMap<String,String> mapParameter = new HashMap<String, String>();
	private Object noList; //实体类中的list集合
	private ServiceBroad broad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		init();
		initAssignment();
		
	}
	
	/**
	 * 初始化控件
	 */
	private void init() {
		descriptionTv = (TextView) findViewById(R.id.order_details_describe);
		tv_title = (TextView) findViewById(R.id.tv_settings_title);
	
		tv_NowTime = (TextView) findViewById(R.id.tv_details_now_time);
		buyerNameTv = (TextView) findViewById(R.id.details_name);
		buyerPhoneTv = (TextView) findViewById(R.id.details_phone);
		elevatorTv = (TextView) findViewById(R.id.details_elevator);
		tv_logisiticsPhone = (TextView) findViewById(R.id.orders_details_logisiticsPhone);
		tv_logisiticsAddress = (TextView) findViewById(R.id.orders_details_logisiticsAddress);
	//	tv_logisiticsNotes = (TextView) findViewById(R.id.details_tv_logisiticsNotes);
		servicePriceTv = (TextView) findViewById(R.id.details_orderPrice);
		column4Tv = (TextView) findViewById(R.id.orders_details_column4);
		statusBtn = (Button) findViewById(R.id.btn_status);
		returnBtn = (ImageButton) findViewById(R.id.Imagebtn_returnbtn);
		releaseTimeTv = (TextView) findViewById(R.id.details_time);
		ll_hide = (LinearLayout) findViewById(R.id.ll_hide);
		ll_hide_logisitics = (LinearLayout) findViewById(R.id.ll_hide_logisitics);
		listView = (ListView) findViewById(R.id.lv_details_goods);

		
		et_nowTime = (EditText) findViewById(R.id.et_details_now_time);
		
		mapAdd = new HashMap<String, String>();
		statusBtn.setOnClickListener(this);
		returnBtn.setOnClickListener(this);
		et_nowTime.setOnTouchListener(this);
		list1 = new ArrayList<HashMap<String,String>>();
		list = new ArrayList<DetailOrdersBeen.T>();
		listImg = new ArrayList<ListEntity>();
		requestQueue = Volley.newRequestQueue(this);
		noList = new Object();
	
	}
	
	
	/**
	 * 给参数初始化赋值
	 */
	private void initAssignment() { //从全部订单获取到的数据
		if(!"".equals(MySharedPerferences.sharedPreferences.getString("orderId", ""))){			
			orderId = MySharedPerferences.sharedPreferences.getString("orderId", "");
			orderState = MySharedPerferences.sharedPreferences.getString("orderState", "");
			Log.e("orderState:", orderState);
			initStatus(orderState);
			mapAdd.clear();
			mapAdd.put("orderId", orderId);
			getDateInit(LinkUrlStatic.orderDetailsUrl, mapAdd);
			MySharedPerferences.editor.putString("orderId",  "");
			MySharedPerferences.editor.putString("orderState","");
			MySharedPerferences.editor.commit();	
		}

		else if(!"".equals(MyReceiver.getMessage)) { //极光推送获取到的数据
			Log.e("message:", MyReceiver.getMessage);
			
			mapParameter.clear();
			mapParameter.put("orderId", MyReceiver.getMessage);					
			getDateInit(LinkUrlStatic.orderDetailsUrl, mapParameter);
			MyReceiver.getMessage ="";
		}
		
	}
	
	
	@SuppressLint("SimpleDateFormat")
	public static String GetStringFromLong(long millis) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		java.util.Date dt = new Date(millis);
		return sdf.format(dt);
	}

	private void initStatus(String status) {
		
		switch (Integer.valueOf(status).intValue()) {
		case 32:
			ll_hide.setVisibility(View.VISIBLE);
			statusBtn.setText("预约完成");
			et_nowTime.setVisibility(View.GONE);
			Log.e("32-tv_NowTime:", tv_NowTime.getText().toString());
			tv_NowTime.setVisibility(View.VISIBLE);
			//statusBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));			
			break;
		case 33:
			tv_title.setText("完成订单");
			et_nowTime.setVisibility(View.GONE);			
			tv_NowTime.setVisibility(View.VISIBLE);
			ll_hide.setVisibility(View.VISIBLE);
			statusBtn.setVisibility(View.GONE);
			break;
		case 29:
			statusBtn.setText("接单");
			ll_hide.setVisibility(View.GONE);
			//statusBtn.setBackgroundColor(getResources().getColor(R.color.theme_color1));
			break;
			
		}
	}
	
	
	@SuppressLint("ShowToast")
	private void judgeStatus(String status) {

		switch (Integer.valueOf(status).intValue()) {
		case 32:			
			/*	statusBtn.setText("预约完成");
				//statusBtn.setBackgroundColor(getResources().getColor(R.color.dark));
			if (et_nowTime.getText().toString().equals("")) {
				Toast.makeText(this, "请输入您的预约时间", 0).show();
				et_nowTime.setHintTextColor(0xffdddddd);
			} else {
				mapAdd.clear();
				mapAdd.put("orderId", orderId);
				mapAdd.put("serviceTime", et_nowTime.getText().toString());				
				getDate0(LinkUrlStatic.saveReserveUrl, mapAdd);
			}*/
			break;
		case 33:
			break;
		case 29:
			ll_hide.setVisibility(View.VISIBLE);
			if (Utils.isNetworkAvailable(this)) { // 有网络时
				
					et_nowTime.setTextColor(0xffdddddd);
					//statusBtn.setBackgroundColor(getResources().getColor(R.color.theme_color));
				
					statusBtn.setText("预约完成");
					//statusBtn.setBackgroundColor(getResources().getColor(R.color.dark));
				if (et_nowTime.getText().toString().equals("")) {
					Toast.makeText(this, "请输入您的预约时间", Toast.LENGTH_LONG).show();
					et_nowTime.setHintTextColor(0xffdddddd);
				} else {
					mapAdd.clear();
					mapAdd.put("workerId",MySharedPerferences.sharedPreferences.getString("workerId", ""));
					mapAdd.put("orderId", orderId);
					mapAdd.put("serviceTime", et_nowTime.getText().toString());				
					getDate0(LinkUrlStatic.saveReserveUrl, mapAdd);
					
				}
			}else{
				Toast.makeText(this, "无网路设备！", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	/**
	 * Volley 和 Gson第三方获取数据并解析
	 * 
	 * @param path
	 *            请求地址
	 */
	public void getDate0(String path, final HashMap<String, String> map) {
		Log.e("-------:", path);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				path, new Listener<String>() {
					@Override
					public void onResponse(String result) {
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getString("status");
							if (status.equals("0")) {
								Intent intent = new Intent(OrderDetailsActivity.this,SaveReserveOrderActivity.class);								
								intent.putExtra("orderId", orderId + ""); // 唯一Id					
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(OrderDetailsActivity.this,
										"服务器异常！", Toast.LENGTH_LONG).show();
							}

							Log.e("status:", status + "");
						} catch (JSONException e) {
							Toast.makeText(OrderDetailsActivity.this, "服务器异常！",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onErrorResponse(VolleyError err) {
						Toast.makeText(OrderDetailsActivity.this, "服务器请求异常！",
								Toast.LENGTH_LONG);
						Log.e("volley错误。", err.getMessage(), err);
					}
				}) {
			@Override
			protected HashMap<String, String> getParams() {
				// 在这里设置需要post的参数
				return map;
			}
		};
		requestQueue.add(stringRequest);
	}
	
	/**
	 * 初始化的时候的Volley 和 Gson第三方获取数据并解析
	 * 
	 * @param path
	 *            请求地址
	 */
	public void getDateInit(String path, final HashMap<String, String> map) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				path, new Listener<String>() {
			@SuppressLint("NewApi")
			@Override
			public void onResponse(String result) {
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getString("status");
					JSONObject object0 = object.getJSONObject("object");					
					String orderCreateTime = object0.getString("orderCreateTime");
					String column3 = object0.getString("column3");
					String orderId = object0.getString("orderId");
					String column1 = object0.getString("column1");
					String orderState = object0.getString("orderState");
					String servicePrice = object0.getString("servicePrice");
					String serviceTime = object0.getString("serviceTime");
					String column4 = object0.getString("column4");
					String buyerName = object0.getString("buyerName");
					String buyerPhone = object0.getString("buyerPhone");
					String elevator = object0.getString("elevator");
			
					if("".equals(servicePrice)||servicePrice==null)
						servicePriceTv.setText("¥0");
					else
						servicePriceTv.setText("¥"+servicePrice);
					
					String a = GetStringFromLong(Long.valueOf(orderCreateTime));
					tv_NowTime.setText(serviceTime);
					descriptionTv.setText(column1 );				
					releaseTimeTv.setText(a);
					buyerNameTv.setText(buyerName);
					buyerPhoneTv.setText(buyerPhone);
					elevatorTv.setText(elevator);
					if(column4.equals("null"))
						column4Tv.setText("无");
					else
						column4Tv.setText(column4);
					
					
					Log.e("getDateInit-----list:", status+"");
					JSONArray goods = object.getJSONArray("goods");
					JSONArray logistics = object.getJSONArray("logistics");
					if(logistics.length()>0){
								JSONObject object3 = logistics.getJSONObject(0);
								String logisiticsPhone = object3.getString("logisiticsPhone");
								String logisiticsAddress = object3.getString("logisiticsAddress");
							//	String logisiticsNotes = object3.getString("logisiticsNotes");						
								tv_logisiticsPhone.setText(logisiticsPhone+"");
								tv_logisiticsAddress.setText(logisiticsAddress+"");
							//	tv_logisiticsNotes.setText(logisiticsNotes+"");
						 //	    Log.e("logisiticsPhone:", logisiticsNotes + "");
								Log.e("logisiticsAddress:", logisiticsAddress+ "");
					}
					HashMap<String,String> map;
					String goodsName="";
					for (int i = 0; i < goods.length(); i++) {
						map = new HashMap<String,String>();
						JSONObject object2 = goods.getJSONObject(i);
						goodsName = object2.getString("goodsName");
						map.put("goodsName", goodsName);						
						list1.add(map);
					}
					if(goods.length()>0){	
						Log.e("goods.length():", goods.length()+"");
						adapterOk();
					}
					
				
				} catch (JSONException e) {
					Log.e("么么么么吗:", "么"+"");
					Toast.makeText(OrderDetailsActivity.this, "服务器异常！",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
			}

		
		}, new ErrorListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onErrorResponse(VolleyError err) {
				Toast.makeText(OrderDetailsActivity.this, "服务器请求异常！",
						Toast.LENGTH_LONG);
				Log.e("volley错误。", err.getMessage(), err);
			}
		}) {
			@Override
			protected HashMap<String, String> getParams() {
				// 在这里设置需要post的参数
				
				return map;
			}
		};
		requestQueue.add(stringRequest);
	}
	
	/**
	 * 适配器
	 */
	private void adapterOk() {
		Log.e("适配器中的 adapterOk  list1:", list1.size()+"");
		adaper = new OrdersDetailsAdapter(list1, OrderDetailsActivity.this);
	//	listView.setVisibility(View.VISIBLE);
		listView.setAdapter(adaper);
	}

	/**
	 * Volley 和 Gson第三方获取数据并解析
	 * 
	 * @param path
	 *            请求地址
	 */
	public void getDate(String path, final HashMap<String, String> map) {
		Log.e("-------:", path);
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				path, new Listener<String>() {
					@Override
					public void onResponse(String result) {
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getString("status");
							if (status.equals("0")) {
								statusBtn.setText("预约完成");
							} else {
								Toast.makeText(OrderDetailsActivity.this,
										"服务器异常！", Toast.LENGTH_LONG).show();
							}

							Log.e("status:", status + "");
						} catch (JSONException e) {
							Toast.makeText(OrderDetailsActivity.this, "服务器异常！",
									Toast.LENGTH_LONG).show();
							e.printStackTrace();
						}

					}
				}, new ErrorListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onErrorResponse(VolleyError err) {
						Toast.makeText(OrderDetailsActivity.this, "服务器请求异常！",
								Toast.LENGTH_LONG);
						Log.e("volley错误。", err.getMessage(), err);
					}
				}) {
			@Override
			protected HashMap<String, String> getParams() {
				// 在这里设置需要post的参数

				return map;
			}
		};
		requestQueue.add(stringRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Imagebtn_returnbtn:
			finish();
			break;
		case R.id.btn_status:
			Log.e("status:", orderState + "");
			judgeStatus(orderState);
			break;
	
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.date_time_picker, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

			timePicker.setIs24HourView(true);
			timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(Calendar.MINUTE);
			final int inType = et_nowTime.getInputType();
			et_nowTime.setInputType(InputType.TYPE_NULL);
			et_nowTime.onTouchEvent(event);
			et_nowTime.setInputType(inType);
			et_nowTime.setSelection(et_nowTime.getText().length());

			builder.setTitle("请选择日期与时间");
	
			builder.setPositiveButton("确  定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							StringBuffer sb = new StringBuffer();
							sb.append(String.format("%d-%02d-%02d",
									datePicker.getYear(),
									datePicker.getMonth() + 1,
									datePicker.getDayOfMonth()));
							
							sb.append("  ");
							sb.append(timePicker.getCurrentHour()).append(":")
									.append(timePicker.getCurrentMinute());

							et_nowTime.setText(sb);

							dialog.cancel();
						}
					});

			Dialog dialog = builder.create();
			dialog.show();
        }

        return true;
    }
	

}
