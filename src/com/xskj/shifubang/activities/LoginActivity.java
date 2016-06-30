package com.xskj.shifubang.activities;

import java.util.HashMap;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zshifu.R;
import com.example.jpushdemo.ExampleUtil;
import com.google.gson.Gson;
import com.xskj.shifubang.been.NewLoginBeen;
import com.xskj.shifubang.db.MySharedPerferences;
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.IsPhoneEmail;
import com.xskj.shifubang.tools.Utils;
import com.xskj.shifubang.view.CheckSwitchButton;
import com.xskj.shifubang.view.ProgersssDialogTools;

public class LoginActivity extends InstrumentedActivity implements
		OnCheckedChangeListener, OnClickListener {
	private static final String TAG = "JPush";
	private CheckSwitchButton mCheckSwithcButton; // ��������ѡ��ť
	private Button accountTopBtn; // �˺ŵ�¼ѡ�ť
	private Button pwdTopBtn; // ��̬�����¼ѡ�ť
	private Button loginBtn; // ��¼��ť
	private Button dynamicVerificationBtn; // ��̬��֤�밴ť
	private Button registerBtn; // ע�ᰴť
	private Button verificationPwdBtn; // �������밴ť
	private EditText accountEdit; // �༭�˺�
	private EditText pwdEdit; // �༭����
	private EditText verificationPwdEdit; // �༭��֤��
	private String name; // ��¼�˺�
	private String pwd; // ��¼����
	private boolean flag = false; // �ж���֤
	public TimeCount time; // ��֤�뵹��ʱ
	private boolean verificationFlag = false; // false��ʾ���˺ŵ�¼;true��ʾ�ö�̬���뷽ʽ��¼;
	private boolean rememberPwd = false; // ��ס���밴ť���ж�����
	private NewLoginBeen loginBeen; // ���·���״̬����
	private String msg; // ����������Ϣ
	private String status; // ��������״̬
	private String data; // ������������
	private String userId; // ���������û�ID
	private String workerId; // ���������˻�Id
	private String isAccreditation; // ���״̬
	public ProgersssDialogTools progressDialog; // ����Ȧ
	private RequestQueue requestQueue; // �������
	private HashMap<String, String> mapParameter; // ��Ӳ���
	public MySharedPerferences mySharedPerferences;// ����

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		init();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		mCheckSwithcButton = (CheckSwitchButton) findViewById(R.id.mCheckSwithcButton);
		accountTopBtn = (Button) findViewById(R.id.btn_login_account);
		pwdTopBtn = (Button) findViewById(R.id.btn_login_pwd);
		loginBtn = (Button) findViewById(R.id.btn_login);
		registerBtn = (Button) findViewById(R.id.btn_login_register);
		verificationPwdBtn = (Button) findViewById(R.id.btn_login_verification_pwd);
		dynamicVerificationBtn = (Button) findViewById(R.id.btn_dynamic);
		accountEdit = (EditText) findViewById(R.id.et_login_account);
		pwdEdit = (EditText) findViewById(R.id.et_login_pwd);
		verificationPwdEdit = (EditText) findViewById(R.id.et_login_dynamic_pwd);
		accountTopBtn.setOnClickListener(this);
		dynamicVerificationBtn.setOnClickListener(this);
		pwdTopBtn.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		verificationPwdBtn.setOnClickListener(this);
		mCheckSwithcButton.setOnCheckedChangeListener(this);
		requestQueue = Volley.newRequestQueue(this);
		loginBeen = new NewLoginBeen();
		mapParameter = new HashMap<String, String>();
		mySharedPerferences = new MySharedPerferences();
		mySharedPerferences.saveSharedPreferences(LoginActivity.this);
		time = new TimeCount(60000, 1000);

	}

	@Override
	protected void onResume() {
		super.onResume();
		String isChecked = MySharedPerferences.sharedPreferences.getString("isPwdChecked", ""); // ѡ��״̬
		if (isChecked.equals("true")){			
			mCheckSwithcButton.setChecked(true);
			accountEdit.setText(MySharedPerferences.sharedPreferences.getString("loginName", "")); // Ĭ�ϼ�ס�˺�
			pwdEdit.setText(MySharedPerferences.sharedPreferences.getString("passWord", "")); // Ĭ�ϼ�ס����
		}
		if (isChecked.equals("false")) {			
			mCheckSwithcButton.setChecked(false);
			accountEdit.setText(""); // Ĭ�ϼ�ס�˺�
			pwdEdit.setText(""); // Ĭ�ϼ�ס����
		}

		Log.e("loginName:", MySharedPerferences.sharedPreferences.getString("loginName", ""));
		Log.e("passWord:",MySharedPerferences.sharedPreferences.getString("passWord", ""));
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			rememberPwd = true;
		} else {
			rememberPwd = false;
			Toast.makeText(LoginActivity.this, "ȡ��", Toast.LENGTH_LONG).show();
		}
	}


	@SuppressLint({ "NewApi", "ShowToast" })
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login_account:
			verificationFlag = false;
			accountTopBtn.setBackground(getResources().getDrawable(
					R.drawable.login_shape_btn_left));
			pwdTopBtn.setBackground(getResources().getDrawable(
					R.color.transparent));
			findViewById(R.id.llyt_2).setVisibility(View.GONE);
			findViewById(R.id.llyt_11).setVisibility(View.VISIBLE);
			findViewById(R.id.llyt_1).setVisibility(View.VISIBLE);
			break;
		case R.id.btn_login_pwd:
			verificationFlag = true;
			pwdTopBtn.setBackground(getResources().getDrawable(
					R.drawable.login_shape_btn_right));
			accountTopBtn.setBackground(getResources().getDrawable(
					R.color.transparent));
			findViewById(R.id.llyt_2).setVisibility(View.VISIBLE);
			findViewById(R.id.llyt_11).setVisibility(View.GONE);
			findViewById(R.id.llyt_1).setVisibility(View.INVISIBLE);
			break;
		case R.id.btn_dynamic:
			// ��ȡ��̬��֤��
			if (isDynamicVerification() == true) {
				time.start(); // ��ʼ����ʱ60s
				mapParameter.clear();				
				mapParameter.put("phoneNum", name);
				Log.e("checkLoginUrl:", LinkUrlStatic.checkLoginUrl);
				Log.e("name:", name);
				Log.e("mapParameter.size():", mapParameter.size()+"");
				progressDialog = new ProgersssDialogTools(LoginActivity.this);
				getDate(LinkUrlStatic.checkLoginUrl, mapParameter);
			}
			break;
		case R.id.btn_login_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

			break;
		case R.id.btn_login_verification_pwd:
			startActivity(new Intent(LoginActivity.this,
					ForgetPasswordActivity.class));
			break;
		case R.id.btn_login:
			if (verificationFlag == true) {
				if (verificationPwdEdit.getText().toString().equals(data))	{					
					if(isAccreditation==null){
						Toast.makeText(LoginActivity.this, "���˺�δע�ᣬ����ע����ٵ�½��", 0).show();
					}
					else if(isAccreditation.equals("1")) {
						MySharedPerferences.editor.putString("loginName", name);
						MySharedPerferences.editor.putString("userId", userId);
						MySharedPerferences.editor.putString("workerId", workerId);
						Log.e("userId", userId);
						Log.e("workerId", workerId);
						finish();
						startActivity(new Intent(LoginActivity.this,MainActivity.class));
					}else if(isAccreditation.equals("0"))
					startActivity(new Intent(LoginActivity.this,AuditActivity.class));
					else if(isAccreditation.equals("-1")){
						finish();
						startActivity(new Intent(LoginActivity.this,AuthenticationActivity.class));
					}
				}
				else
					Toast.makeText(LoginActivity.this, "��֤�벻ƥ�䣡", 0).show();

			} else {
				if (isVerification() == true) {
					progressDialog = new ProgersssDialogTools(LoginActivity.this);
					mapParameter.clear();
					mapParameter.put("name", name);
					mapParameter.put("pwd", pwd);
					Log.e("name", name);
					Log.e("pwd", pwd);
					getDate(LinkUrlStatic.loginUrl, mapParameter);
				}
			}

			break;
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
			dynamicVerificationBtn.setText("���»�ȡ");
			dynamicVerificationBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) { // ��ʱ����
			dynamicVerificationBtn.setClickable(false);// ��ֹ�ظ����
			dynamicVerificationBtn.setText(millisUntilFinished / 1000 + "s");
		}

	}
	
	
	/**
	 * ��̬�����¼��֤
	 */
	@SuppressLint("ShowToast")
	private boolean isDynamicVerification() {
		flag = false;
		if (accountEdit.getText().toString().equals(""))
			Toast.makeText(LoginActivity.this, "�������˺ţ�", 0).show();
		else if (IsPhoneEmail.isMobile(accountEdit.getText().toString()) == false)
			Toast.makeText(LoginActivity.this, "�˺Ÿ�ʽ����ȷ��", 0).show();
		else if (!Utils.isNetworkAvailable(this))
			Toast.makeText(LoginActivity.this, "�޿��õ����磡", 0).show();
		else {
			name = accountEdit.getText().toString();
			flag = true;
		}
		return flag;
	}

	/**
	 * �˺ŵ�¼��֤
	 */
	@SuppressLint("ShowToast")
	private boolean isVerification() {
		flag = false;
		if (accountEdit.getText().toString().equals(""))
			Toast.makeText(LoginActivity.this, "�������˺ţ�", 0).show();
		else if (pwdEdit.getText().toString().equals(""))
			Toast.makeText(LoginActivity.this, "���������룡", 0).show();
		else if (IsPhoneEmail.isMobile(accountEdit.getText().toString()) == false)
			Toast.makeText(LoginActivity.this, "�˺Ÿ�ʽ����ȷ��", 0).show();
		else if (!Utils.isNetworkAvailable(this))
			Toast.makeText(LoginActivity.this, "�޿��õ����磡", 0).show();
		else {
			name = accountEdit.getText().toString();
			pwd = pwdEdit.getText().toString();
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
    Log.e("������", "������");
    Log.e("map", map.size()+"");
    Log.e("path", path+"");
    
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				path, new Listener<String>() {
					@Override
					public void onResponse(String result) {
						Log.e("resule:", result + "");
						Gson gson = new Gson();
						loginBeen = gson.fromJson(result, NewLoginBeen.class);
						volleyGetData();
						isLogin();
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {	
						
						Toast.makeText(LoginActivity.this, "�����������쳣��", 0).show();
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
		status = loginBeen.getStatus();
		msg = loginBeen.getMsg();
		userId = loginBeen.getUserId();
		workerId = loginBeen.getWorkerId();
		Log.e("11111", "111111");
		isAccreditation = loginBeen.getIsAccreditation();
		Log.e("222", "222222222");
		this.data = loginBeen.getData();
		//verificationPwdEdit.setText(data+"");
		Log.e("status:", "" + status);
	}

	/**
	 * ����״̬��֤�ɷ������¼
	 */
	private void isLogin() {
	
		if (status.equals("0") && data == null) {
			MySharedPerferences.editor.putString("loginName", name);
			MySharedPerferences.editor.putString("userId", userId);
			MySharedPerferences.editor.putString("workerId", workerId);
			Log.e("userId", userId);
			Log.e("workerId", workerId);
			if (rememberPwd == true) { // �ж��ǲ��ǵ����ס���밴ť
				MySharedPerferences.editor.putString("passWord", pwd);
				MySharedPerferences.editor.putString("isPwdChecked", "true");
			} else {
				MySharedPerferences.editor.putString("passWord", "");
				MySharedPerferences.editor.putString("isPwdChecked", "false");
			}
			MySharedPerferences.editor.commit();
			initJpush();
			sendAlias(); // used for receive msg
			setStyleCustom();
			
			if(isAccreditation.equals("1")) {
				finish();
				startActivity(new Intent(LoginActivity.this,MainActivity.class));
			}else if(isAccreditation.equals("0")){
				finish();				
				startActivity(new Intent(LoginActivity.this,AuditActivity.class));
			}
				
			else if(isAccreditation.equals("-1")){
				finish();
				startActivity(new Intent(LoginActivity.this,AuthenticationActivity.class));
			}
		} else
			Toast.makeText(LoginActivity.this, msg + "", 0).show();
	}

	// ��ʼ�� JPush������Ѿ���ʼ������û�е�¼�ɹ�����ִ�����µ�¼��
	private void initJpush() {
		JPushInterface.init(getApplicationContext());
	}

	/**
	 * ���ͱ��������������
	 */
	public void sendAlias() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				setAlias(name);
			}
		}).start();
	}
	
	/**
	 *����֪ͨ����ʽ - ����֪ͨ��Layout
	 */
	private void setStyleCustom(){
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(LoginActivity.this,
			                              R.layout.notitfication_layout, 
			                              R.id.icon, 
			                              R.id.title, 
			                              R.id.text); 
			                             // ָ�����Ƶ� Notification Layout
		
		builder.layoutIconDrawable = R.drawable.head;

			 //   builder.statusBarDrawable = R.drawable.logo;      
			    // ָ�����״̬��Сͼ��
			   // builder.layoutIconDrawable = R.drawable.top_bg;   
			    // ָ������״̬��ʱ��ʾ��֪ͨͼ��
			    JPushInterface.setPushNotificationBuilder(1, builder);
	}

	/**
	 * 
	 * @param name ���ֻ��������ֻ���
	 * 
	 */
	private void setAlias(String name) {
	    
	    if (TextUtils.isEmpty(name)) {
	        return;
	    }
	    if (!ExampleUtil.isValidTagAndAlias(name)) {
	        return;
	    }

	    // ���� Handler ���첽���ñ���
	    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, name));
	}

	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			String logs;
			switch (code) {
			case 0:
				logs = "Set tag and alias success";
				MySharedPerferences.editor.putString("logs", logs);
				MySharedPerferences.editor.commit();
				Log.i(TAG, logs);
				
				// ���������� SharePreference ��дһ���ɹ����õ�״̬���ɹ�����һ�κ��Ժ󲻱��ٴ������ˡ�
				break;
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				// �ӳ� 60 �������� Handler ���ñ���
				mHandler.sendMessageDelayed(
						mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
				break;
			default:
				logs = "Failed with errorCode = " + code;
				Log.e(TAG, logs);
			}
		//	ExampleUtil.showToast(logs, getApplicationContext());
		}
	};
	private static final int MSG_SET_ALIAS = 1001;
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SET_ALIAS:
				Log.d(TAG, "Set alias in handler.");
				// ���� JPush �ӿ������ñ�����
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;
			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};
	
	
	// ������ؼ������˳�����
	private long exitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			// System.exit(0)�������˳�����
		}
	}

}
