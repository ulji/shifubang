package com.xskj.shifubang.activities;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.IsPhoneEmail;
import com.xskj.shifubang.tools.Utils;
import com.xskj.shifubang.view.ProgersssDialogTools;

public class ForgetPasswordActivity extends Activity implements OnClickListener{

	private EditText accountEt; //�˺ű༭
	private EditText verificationEt; //��֤��༭
	private EditText pwdEt; //����༭
	private EditText pwdAginEt; //�ٴ���������༭
	private Button dynamicBtn; //��ȡ��֤�밴ť
	private Button nextBtn; //�ύ��ť
	private ImageView returnBtn; //����
	public TimeCount time; // ��֤�뵹��ʱ
	private RequestQueue requestQueue; // �������
	public ProgersssDialogTools progressDialog ; // ����Ȧ
	private HashMap<String, String> mapParameter; //��Ӳ���
	private String name; //�˻�
	
	private boolean flag = false; //�ж���֤
	private boolean flagNext = false; //��һ����ť���ж�
	private LoginBeen loginBeen; //��֤���ʵ����
	private String msg; //����������Ϣ
	private String status; //��������״̬
	private String data; //������������
	private String isData; //��������ƥ���ж�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		init();
		
	}
	
	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
        accountEt = (EditText) findViewById(R.id.et_rorget_account);
        verificationEt = (EditText) findViewById(R.id.et_rorget_verification);
        pwdEt = (EditText) findViewById(R.id.et_rorget_pwd);
        pwdAginEt = (EditText) findViewById(R.id.et_rorget_agin_pwd);
        dynamicBtn = (Button) findViewById(R.id.btn_rorget_dynamic);
        nextBtn = (Button) findViewById(R.id.btn_rorget_next);
        returnBtn = (ImageView) findViewById(R.id.pwd_rorget_return);
        dynamicBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);
    	loginBeen = new LoginBeen();
        mapParameter = new HashMap<String, String>();			
		requestQueue = Volley.newRequestQueue(this);
		time = new TimeCount(60000, 1000);
	}

	@Override
	public void onClick(View v) {
		mapParameter.clear();
		switch (v.getId()) {
		case R.id.btn_rorget_dynamic:
            //��ȡ��̬��֤�밴ť�¼�
			if(isDynamicVerification()==true) {
				progressDialog = new ProgersssDialogTools(ForgetPasswordActivity.this);
				time.start();
				mapParameter.put("phoneNum", name);
				getDate(LinkUrlStatic.checkLoginUrl,mapParameter);
			}
			
			break;
		case R.id.btn_rorget_next:
			//�ύ��ť�¼�
			if(isNext()==true) {				
				progressDialog = new ProgersssDialogTools(ForgetPasswordActivity.this);
				mapParameter.put("loginName", accountEt.getText().toString()+"");
				mapParameter.put("npwd", pwdEt.getText().toString()+"");
				Log.e("mapParameter:",mapParameter.size()+"");
				Log.e("loginName:",accountEt.getText().toString());
				Log.e("pwd:",pwdEt.getText().toString());                
				getDate(LinkUrlStatic.changepwdUrl,mapParameter);
			}
			break;
			
		case R.id.pwd_rorget_return://���ذ�ť		
			finish();
			
			break;
		}

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

					@SuppressLint("ShowToast")
					@Override
					public void onErrorResponse(VolleyError err) {	
						Toast.makeText(ForgetPasswordActivity.this, "�����������쳣��", 0).show();
						progressDialog._dismiss();
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
	 * ��һ����ť֮ǰ��һЩ��֤
	 */
	@SuppressLint("ShowToast")
	private boolean isNext() {
		flagNext = false;
		isData = data;
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(this, "�������˺ţ�", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(this, "�˺Ÿ�ʽ����ȷ��", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(this, "�޿��õ����磡", 0).show();
		else if(verificationEt.getText().toString().equals("")) {			
			Toast.makeText(this, "��֤�벻��Ϊ�գ�", 0).show();		
		}
		else if(!verificationEt.getText().toString().equals(isData)) {			
			Toast.makeText(this, "��֤�벻ƥ�䣬�����»�ȡ��", 0).show();		
		}
		else if(pwdEt.getText().toString().equals(""))
			Toast.makeText(this, "���벻��Ϊ�գ�", 0).show();		
		else if(pwdAginEt.getText().toString().equals(""))
			Toast.makeText(this, "���ٴ��������룡", 0).show();
		else if(!pwdAginEt.getText().toString().equals(pwdEt.getText().toString()))
			Toast.makeText(this, "���벻ƥ�䣬���������룡", 0).show();		
	
		else{		
			flagNext = true;			
		}
		return flagNext;
	
	}
	
	
	/**
	 * ��̬�����¼��֤
	 */
	@SuppressLint("ShowToast")
	private boolean isDynamicVerification() {
		
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(this, "�������˺ţ�", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(this, "�˺Ÿ�ʽ����ȷ��", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(this, "�޿��õ����磡", 0).show();
		else{
			name = accountEt.getText().toString();
			flag = true;			
		}
		return flag;
	}
	
	/**
	 * volley �����õ�������
	 */
	private void volleyGetData() {	
		progressDialog._dismiss();
		status = loginBeen.getStatus();
		msg = loginBeen.getMsg();
		data = loginBeen.getData();	
		//if(data!=null)
			//verificationEt.setText(data+"");
		if (msg.equals("�޸ĳɹ�") && status.equals("0")){			
			Log.e("isData:", isData);
			if (verificationEt.getText().toString().equals(isData)){				
				finish();			
				startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
			}else {
				Toast.makeText(ForgetPasswordActivity.this,"��֤�벻ƥ�䣡" , 0).show();
			}
		}
	}
	
	


	/**
	 * ���ܣ���֤�뵹��ʱ60s
	 * 
	 * @author aimin 2016-5-7
	 */
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() { // ��ʱ���
			dynamicBtn.setText("���»�ȡ");
			dynamicBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) { // ��ʱ����
			dynamicBtn.setClickable(false);// ��ֹ�ظ����
			dynamicBtn.setText(millisUntilFinished / 1000 + "s");
		}

	}
	


}
