package com.xskj.shifubang.activities;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zshifu.R;
import com.google.gson.Gson;
import com.xskj.shifubang.been.LoginBeen;
import com.xskj.shifubang.db.MySharedPerferences;
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.Utils;
import com.xskj.shifubang.view.ProgersssDialogTools;

@SuppressLint("ShowToast")
public class SettledActivity extends Activity implements OnClickListener{
    private EditText nameEt; //师傅姓名
    private EditText idEt; //身份证号码
    private EditText workAddressEt; //工作地址
    private EditText serviceNumEt; //服务人数
    private EditText automobileTypeEt; //汽车类型
    private RelativeLayout erviceAreaRlyt; //服务区域按钮
   
    private RelativeLayout serviceTypeRlyt; //服务类型按钮
    private LinearLayout settledAgreementLlyt; //协议同意按钮
    public TextView erviceAreaTv; //服务区域显示框
    public String serviceArea[] =null; //服务区域	
    public String serviceNameArea[] =null; //服务区域名字（不保存到数据库）	
    private String serviceType[] =null; //服务类型	
    private String serviceNameType[] ; //服务类型名字（不保存到数据库）	
    private TextView serviceTypeTv; //服务类型显示框
    private CheckBox checkbox; //协议显示框
    private Button settledOkBtn; //立即入驻按钮
    private ImageView returnBtn; //返回按钮
    private boolean flag = false; //判断可否入驻
    private ServiceBroad broad;
    private ServiceBroad1 broad1;
    
	private RequestQueue requestQueue; // 请求队列
	public ProgersssDialogTools progressDialog ; // 加载圈
	private HashMap<String, String> mapParameter; //添加参数
	private LoginBeen loginBeen; //验证码的实体类
	private String areaStr=""; //服务区域，最终保存到数据库
	private String typeStr=""; //服务类型，最终保存到数据库
	
    Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settled);
		init();
	}
	
	/**
	 * 初始化控件
	 */
	private void init() {
		
		        nameEt = (EditText) findViewById(R.id.et_settled_name);
        		idEt = (EditText) findViewById(R.id.et_settled_id);
        		serviceNumEt = (EditText) findViewById(R.id.et_settled_service_num);
        		automobileTypeEt = (EditText) findViewById(R.id.et_settled_automobile_type);
        		workAddressEt = (EditText) findViewById(R.id.et_settled_work_address);
        		erviceAreaRlyt = (RelativeLayout) findViewById(R.id.rlyt_settled_service_area);
        		erviceAreaTv = (TextView) findViewById(R.id.tv_settled_service_area);
        		serviceTypeRlyt = (RelativeLayout) findViewById(R.id.rlyt_settled_service_type);
        		serviceTypeTv = (TextView) findViewById(R.id.tv_settled_service_type);
        		settledAgreementLlyt = (LinearLayout) findViewById(R.id.llyt_checkbox_settled_agreement);
        		checkbox = (CheckBox) findViewById(R.id.checkbox_settled_agreement);
        		settledOkBtn = (Button) findViewById(R.id.btn_settled_ok);
        		returnBtn = (ImageView) findViewById(R.id.register_return);
        		 
        		erviceAreaRlyt.setOnClickListener(this);
        		serviceTypeRlyt.setOnClickListener(this);
        		settledAgreementLlyt.setOnClickListener(this);
        		settledOkBtn.setOnClickListener(this);
        		returnBtn.setOnClickListener(this);
        		
        		intent = new Intent();
        		
        		serviceArea = new String[60];
        		serviceNameArea = new String[60];
        		serviceType = new String[60];
        		serviceNameType = new String[60];
        		requestQueue = Volley.newRequestQueue(this);
        		mapParameter = new HashMap<String, String>();
        		loginBeen = new LoginBeen();
        		
		       IntentFilter intentFilter = new IntentFilter("Service");
		       broad = new ServiceBroad();
		       registerReceiver(broad, intentFilter);
		       IntentFilter intentFilter1 = new IntentFilter("ServiceType");
		       broad1 = new ServiceBroad1();
		       registerReceiver(broad1, intentFilter1);
        	   
	}

	/**
	 * 广播接收器
	 */
    class ServiceBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        	if(intent.getStringArrayExtra("AreaIdStr")!=null||!intent.getStringArrayExtra("AreaIdStr").equals("null")){        		
        		serviceArea = intent.getStringArrayExtra("AreaIdStr");
        	}
        	if(intent.getStringArrayExtra("AreanameStr")!=null||!intent.getStringArrayExtra("AreanameStr").equals("null"))
        		serviceNameArea = intent.getStringArrayExtra("AreanameStr");
        		showArea();
        
        }

    }     
    /**
     * 广播接收器
     */
    class ServiceBroad1 extends BroadcastReceiver {
    	
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		serviceType = intent.getStringArrayExtra("ServiceTypeData");
    		serviceNameType = intent.getStringArrayExtra("serviceNameTypeStr");
    		showType();          		
    	}
    	
    }     
    
    /**
     * 显示区域的数据
     */
    private void showArea() {    	
    	String numStr="";
    	for(int i=0;i<serviceNameArea.length;i++){
    		numStr+=serviceNameArea[i];
    	}    	    
    	erviceAreaTv.setText(numStr+"");
    
    	//把String数组用逗号隔开用字符串保存
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < serviceArea.length; i++) {
			sb.append(serviceArea[i].trim() + ",");
		}
		areaStr = sb.toString();
		areaStr = areaStr.substring(0, areaStr.length() - 1);
		Log.e("serviceArea---str:", areaStr.substring(0, areaStr.length()));
    	
    }
    /**
     * 显示类型的数据
     */
    private void showType() {    	
    	String numStr="";
    	if(ServiceTypeDetailsActivity.mapAll.size()>0){    		
    		for(int i=0;i<serviceNameType.length;i++){	
    			if(serviceNameType[i]!=null)
    			numStr+=serviceNameType[i];
    		}
    		serviceTypeTv.setText(numStr+"");
    		
    	 	//把String数组用逗号隔开用字符串保存
    		StringBuffer sb = new StringBuffer();
    		for (int i = 0; i < ServiceTypeDetailsActivity.mapAll.size(); i++) {
    			sb.append(serviceType[i].trim() + ",");
    		}
    		typeStr = sb.toString();
    		typeStr = typeStr.substring(0, typeStr.length());
    		Log.e("serviceType---str:", typeStr.substring(0, typeStr.length() - 1));
    	}
    }
    
    

    
  
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rlyt_settled_service_area:
			// 选择区域事件
			startActivity(new Intent (SettledActivity.this,ServiceAreaDetailsActivity.class));
			break;
		case R.id.rlyt_settled_service_type:
			// 选择类型事件
			startActivity(new Intent (SettledActivity.this,ServiceTypeDetailsActivity.class));
			
			break;
		case R.id.llyt_checkbox_settled_agreement:
			// 协议按钮事件

			break;
		case R.id.btn_settled_ok:// 立即入驻按钮事件			
			if(isSettled()==true) {
				progressDialog = new ProgersssDialogTools(SettledActivity.this);
				mapParameter.put("loginName", MySharedPerferences.sharedPreferences.getString("loginName", ""));
				mapParameter.put("workerName", nameEt.getText().toString());
				mapParameter.put("idCard", idEt.getText().toString());
				mapParameter.put("workerAddress", workAddressEt.getText().toString());
				mapParameter.put("column3", typeStr);
				mapParameter.put("column4", areaStr);	
				Log.e("loginName------:", MySharedPerferences.sharedPreferences.getString("loginName", ""));
				getDate(LinkUrlStatic.upgradeRegiaterUrl,mapParameter);	
			}	
			break;
		case R.id.register_return:// 返回按钮事件
			finish();
			break;
		}
	}
	
	/**
	 * 验证可否入驻
	 */
	@SuppressLint("ShowToast")
	private boolean isSettled() {
		flag = false;
		if(nameEt.getText().toString().equals(""))
			Toast.makeText(SettledActivity.this, "师傅姓名不能为空！", 0).show();		
		else if(idEt.getText().toString().equals(""))
			Toast.makeText(SettledActivity.this, "身份证号不能为空！", 0).show();		
		else if(!Utils.personIdValidation(idEt.getText().toString()))
			Toast.makeText(SettledActivity.this, "身份证号格式不正确！", 0).show();		
		else if(workAddressEt.getText().toString().equals(""))
			Toast.makeText(SettledActivity.this, "工作地址不能为空！", 0).show();		
		else if("".equals(serviceArea[0])||null==serviceArea[0])
			Toast.makeText(SettledActivity.this, "请选择服务区域！", 0).show();		
		else if("".equals(serviceType[0])||null==serviceType[0])
			Toast.makeText(SettledActivity.this, "请选择服务类型！", 0).show();		
		else if(checkbox.isChecked()==false)
			Toast.makeText(SettledActivity.this, "您同意遵守师傅帮协议才可以入驻！", 0).show();
		else {			
			Log.e("serviceArea", serviceArea.length+"");
			Log.e("serviceType", serviceType.length+"");
			flag = true;
		}
		return flag;
	}
	
	/**
	 * Volley 和 Gson第三方获取数据并解析
	 * 
	 * @param path
	 *            请求地址
	 */
	public void getDate(String path, final HashMap<String, String> map) {
		
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				path, new Listener<String>() {
					@Override
					public void onResponse(String result) {
						Log.e("resule:", result + "");						
						Gson gson = new Gson();
						loginBeen = gson.fromJson(result, LoginBeen.class);		
						volleyGetData();
						
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {
						progressDialog._dismiss();
						Toast.makeText(SettledActivity.this, "服务器请求超时！", 0).show();
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
	 * volley 解析得到的数据
	 */
	private void volleyGetData() {
		progressDialog._dismiss();
		String status = loginBeen.getStatus();
		String msg = loginBeen.getMsg();
		//String data = loginBeen.getData();
		if (msg.equals("注册成功") && status.equals("0")) {
			unregisterReceiver(broad); //注销服务区域的广播			
			unregisterReceiver(broad1);//注销服务类型的广播
			finish();
			startActivity(new Intent(SettledActivity.this,LoginActivity.class));
		}
	}

}
