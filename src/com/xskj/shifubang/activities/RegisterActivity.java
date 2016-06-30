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
import com.xskj.shifubang.db.MySharedPerferences;
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.IsPhoneEmail;
import com.xskj.shifubang.tools.Utils;
import com.xskj.shifubang.view.ProgersssDialogTools;

@SuppressLint("ShowToast")
public class RegisterActivity extends Activity implements OnClickListener {
	private Button nextBtn; // ��һ���İ�ť
	private Button dynamicBtn; // ��ȡ��֤�밴ť
	private ImageView returnBtn; // ���ذ�ť
	public TimeCount time; // ��֤�뵹��ʱ
	
	private EditText accountEt; // �˻��༭
	private EditText verificationCodeEt; // ��֤��༭
	private EditText pwdEt; // ����༭
	private EditText aginPwdEt; // �ٴ���������༭	
	private RequestQueue requestQueue; // �������
	public ProgersssDialogTools progressDialog ; // ����Ȧ
	private String name; //�˻�
	private boolean flag = false; //�ж���֤
	private boolean flagNext = false; //��һ����ť���ж�
	private HashMap<String, String> mapParameter; //��Ӳ���
	
	private LoginBeen loginBeen; //��֤���ʵ����
	private String msg; //����������Ϣ
	private String status; //��������״̬
	private String data; //������������
	private String getData; //������


	public MySharedPerferences mySharedPerferences ;//����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		nextBtn = (Button) findViewById(R.id.btn_register_next);
		dynamicBtn = (Button) findViewById(R.id.btn_register_dynamic);
		returnBtn = (ImageView) findViewById(R.id.register_return);
		accountEt = (EditText) findViewById(R.id.et_register_account);
		verificationCodeEt = (EditText) findViewById(R.id.et_register_verification);
		pwdEt = (EditText) findViewById(R.id.et_register_pwd);
		aginPwdEt = (EditText) findViewById(R.id.et_register_agin_pwd);
		nextBtn.setOnClickListener(this);
		dynamicBtn.setOnClickListener(this);
		returnBtn.setOnClickListener(this);
		mapParameter = new HashMap<String, String>();		
		requestQueue = Volley.newRequestQueue(this);
		loginBeen = new LoginBeen();
		mySharedPerferences = new MySharedPerferences();
		mySharedPerferences.saveSharedPreferences(RegisterActivity.this);
		
		time = new TimeCount(60000, 1000);
	}

	@Override
	public void onClick(View v) {
		mapParameter.clear();
		switch (v.getId()) {
		case R.id.btn_register_next:		
			//����
			if(isNext() == true) {		
				progressDialog = new ProgersssDialogTools(RegisterActivity.this);
				mapParameter.clear();
				mapParameter.put("loginName", name);
				mapParameter.put("pwd", pwdEt.getText().toString());
				getDate(LinkUrlStatic.registerUrl,mapParameter);	
			}		
			break;
		case R.id.btn_register_dynamic://��ȡ��̬��֤��	
			if(isDynamicVerification() == true) {	
				time.start();
				mapParameter.put("phoneNum", name);
				getDate(LinkUrlStatic.genCodeUrl,mapParameter);	
			}
			break;
		case R.id.register_return://���ذ�ť		
			finish();
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		}
	}
	
	/**
	 * ��һ����ť֮ǰ��һЩ��֤
	 */
	@SuppressLint("ShowToast")
	private boolean isNext() {
		getData = data;
		flagNext = false;	
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "�������˺ţ�", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(RegisterActivity.this, "�˺Ÿ�ʽ����ȷ��", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(RegisterActivity.this, "�޿��õ����磡", 0).show();
		else if(verificationCodeEt.getText().toString().equals("")) {			
			Toast.makeText(RegisterActivity.this, "��֤�벻��Ϊ�գ�", 0).show();		
		}
		else if(!verificationCodeEt.getText().toString().equals(getData)) {			
			Toast.makeText(RegisterActivity.this, "��֤�벻ƥ�䣬�����»�ȡ��", 0).show();		
		}
		else if(pwdEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "���벻��Ϊ�գ�", 0).show();		
		else if(aginPwdEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "���ٴ��������룡", 0).show();
		else if(!aginPwdEt.getText().toString().equals(pwdEt.getText().toString()))
			Toast.makeText(RegisterActivity.this, "���벻ƥ�䣬���������룡", 0).show();		
		else if(msg.equals("�绰����ע��")) {			
			Toast.makeText(RegisterActivity.this, "���û���ע�ᣡ", 0).show();	
			data=null;
		}
		else{
			Log.e("data1111111:", data);
			
			MySharedPerferences.editor.putString("loginName", name);								
			MySharedPerferences.editor.putString("passWord", pwdEt.getText().toString());		
			MySharedPerferences.editor.commit();
			flagNext = true;			
		}
		return flagNext;
	
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
						Log.e("resule1111:", result + "");						
						Gson gson = new Gson();
						loginBeen = gson.fromJson(result, LoginBeen.class);		
						volleyGetData();
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {
						Toast.makeText(RegisterActivity.this, "�����������쳣��", 0).show();
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
	 * ��̬�����¼��֤
	 */
	@SuppressLint("ShowToast")
	private boolean isDynamicVerification() {
		
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "�������˺ţ�", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(RegisterActivity.this, "�˺Ÿ�ʽ����ȷ��", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(RegisterActivity.this, "�޿��õ����磡", 0).show();
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
		status = loginBeen.getStatus();
		msg = loginBeen.getMsg();
		this.data = loginBeen.getData();	
		//if(data!=null)
		//verificationCodeEt.setText(data+"");
		if(msg.equals("�绰����ע��"))
			Toast.makeText(RegisterActivity.this, msg, 0).show();
		
		if (msg.equals("ע��ɹ�") && status.equals("0")){
			progressDialog._dismiss();
			Log.e("getData:", getData);
			finish();
			startActivity(new Intent(RegisterActivity.this,SettledActivity.class));
		
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
