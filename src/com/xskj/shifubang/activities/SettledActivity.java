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
    private EditText nameEt; //ʦ������
    private EditText idEt; //���֤����
    private EditText workAddressEt; //������ַ
    private EditText serviceNumEt; //��������
    private EditText automobileTypeEt; //��������
    private RelativeLayout erviceAreaRlyt; //��������ť
   
    private RelativeLayout serviceTypeRlyt; //�������Ͱ�ť
    private LinearLayout settledAgreementLlyt; //Э��ͬ�ⰴť
    public TextView erviceAreaTv; //����������ʾ��
    public String serviceArea[] =null; //��������	
    public String serviceNameArea[] =null; //�����������֣������浽���ݿ⣩	
    private String serviceType[] =null; //��������	
    private String serviceNameType[] ; //�����������֣������浽���ݿ⣩	
    private TextView serviceTypeTv; //����������ʾ��
    private CheckBox checkbox; //Э����ʾ��
    private Button settledOkBtn; //������פ��ť
    private ImageView returnBtn; //���ذ�ť
    private boolean flag = false; //�жϿɷ���פ
    private ServiceBroad broad;
    private ServiceBroad1 broad1;
    
	private RequestQueue requestQueue; // �������
	public ProgersssDialogTools progressDialog ; // ����Ȧ
	private HashMap<String, String> mapParameter; //��Ӳ���
	private LoginBeen loginBeen; //��֤���ʵ����
	private String areaStr=""; //�����������ձ��浽���ݿ�
	private String typeStr=""; //�������ͣ����ձ��浽���ݿ�
	
    Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settled);
		init();
	}
	
	/**
	 * ��ʼ���ؼ�
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
	 * �㲥������
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
     * �㲥������
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
     * ��ʾ���������
     */
    private void showArea() {    	
    	String numStr="";
    	for(int i=0;i<serviceNameArea.length;i++){
    		numStr+=serviceNameArea[i];
    	}    	    
    	erviceAreaTv.setText(numStr+"");
    
    	//��String�����ö��Ÿ������ַ�������
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < serviceArea.length; i++) {
			sb.append(serviceArea[i].trim() + ",");
		}
		areaStr = sb.toString();
		areaStr = areaStr.substring(0, areaStr.length() - 1);
		Log.e("serviceArea---str:", areaStr.substring(0, areaStr.length()));
    	
    }
    /**
     * ��ʾ���͵�����
     */
    private void showType() {    	
    	String numStr="";
    	if(ServiceTypeDetailsActivity.mapAll.size()>0){    		
    		for(int i=0;i<serviceNameType.length;i++){	
    			if(serviceNameType[i]!=null)
    			numStr+=serviceNameType[i];
    		}
    		serviceTypeTv.setText(numStr+"");
    		
    	 	//��String�����ö��Ÿ������ַ�������
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
			// ѡ�������¼�
			startActivity(new Intent (SettledActivity.this,ServiceAreaDetailsActivity.class));
			break;
		case R.id.rlyt_settled_service_type:
			// ѡ�������¼�
			startActivity(new Intent (SettledActivity.this,ServiceTypeDetailsActivity.class));
			
			break;
		case R.id.llyt_checkbox_settled_agreement:
			// Э�鰴ť�¼�

			break;
		case R.id.btn_settled_ok:// ������פ��ť�¼�			
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
		case R.id.register_return:// ���ذ�ť�¼�
			finish();
			break;
		}
	}
	
	/**
	 * ��֤�ɷ���פ
	 */
	@SuppressLint("ShowToast")
	private boolean isSettled() {
		flag = false;
		if(nameEt.getText().toString().equals(""))
			Toast.makeText(SettledActivity.this, "ʦ����������Ϊ�գ�", 0).show();		
		else if(idEt.getText().toString().equals(""))
			Toast.makeText(SettledActivity.this, "���֤�Ų���Ϊ�գ�", 0).show();		
		else if(!Utils.personIdValidation(idEt.getText().toString()))
			Toast.makeText(SettledActivity.this, "���֤�Ÿ�ʽ����ȷ��", 0).show();		
		else if(workAddressEt.getText().toString().equals(""))
			Toast.makeText(SettledActivity.this, "������ַ����Ϊ�գ�", 0).show();		
		else if("".equals(serviceArea[0])||null==serviceArea[0])
			Toast.makeText(SettledActivity.this, "��ѡ���������", 0).show();		
		else if("".equals(serviceType[0])||null==serviceType[0])
			Toast.makeText(SettledActivity.this, "��ѡ��������ͣ�", 0).show();		
		else if(checkbox.isChecked()==false)
			Toast.makeText(SettledActivity.this, "��ͬ������ʦ����Э��ſ�����פ��", 0).show();
		else {			
			Log.e("serviceArea", serviceArea.length+"");
			Log.e("serviceType", serviceType.length+"");
			flag = true;
		}
		return flag;
	}
	
	/**
	 * Volley �� Gson��������ȡ���ݲ�����
	 * 
	 * @param path
	 *            �����ַ
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
						Toast.makeText(SettledActivity.this, "����������ʱ��", 0).show();
						Log.e("volley����", err.getMessage(), err);
					}
				}) {
			@Override
			protected HashMap<String, String> getParams() {
				// ������������Ҫpost�Ĳ���
			
				return map;
			}
		};
		requestQueue.add(stringRequest);
	}

	/**
	 * volley �����õ�������
	 */
	private void volleyGetData() {
		progressDialog._dismiss();
		String status = loginBeen.getStatus();
		String msg = loginBeen.getMsg();
		//String data = loginBeen.getData();
		if (msg.equals("ע��ɹ�") && status.equals("0")) {
			unregisterReceiver(broad); //ע����������Ĺ㲥			
			unregisterReceiver(broad1);//ע���������͵Ĺ㲥
			finish();
			startActivity(new Intent(SettledActivity.this,LoginActivity.class));
		}
	}

}
