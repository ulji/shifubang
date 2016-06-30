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
	private CheckSwitchButton mCheckSwithcButton; // 忘记密码选择按钮
	private Button accountTopBtn; // 账号登录选项按钮
	private Button pwdTopBtn; // 动态密码登录选项按钮
	private Button loginBtn; // 登录按钮
	private Button dynamicVerificationBtn; // 动态验证码按钮
	private Button registerBtn; // 注册按钮
	private Button verificationPwdBtn; // 忘记密码按钮
	private EditText accountEdit; // 编辑账号
	private EditText pwdEdit; // 编辑密码
	private EditText verificationPwdEdit; // 编辑验证码
	private String name; // 登录账号
	private String pwd; // 登录密码
	private boolean flag = false; // 判断验证
	public TimeCount time; // 验证码倒计时
	private boolean verificationFlag = false; // false表示用账号登录;true表示用动态密码方式登录;
	private boolean rememberPwd = false; // 记住密码按钮的判断条件
	private NewLoginBeen loginBeen; // 最新返回状态的类
	private String msg; // 请求结果，消息
	private String status; // 请求结果，状态
	private String data; // 请求结果，数据
	private String userId; // 请求结果，用户ID
	private String workerId; // 请求结果，账户Id
	private String isAccreditation; // 审核状态
	public ProgersssDialogTools progressDialog; // 加载圈
	private RequestQueue requestQueue; // 请求队列
	private HashMap<String, String> mapParameter; // 添加参数
	public MySharedPerferences mySharedPerferences;// 缓存

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		init();
	}

	/**
	 * 初始化控件
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
		String isChecked = MySharedPerferences.sharedPreferences.getString("isPwdChecked", ""); // 选择状态
		if (isChecked.equals("true")){			
			mCheckSwithcButton.setChecked(true);
			accountEdit.setText(MySharedPerferences.sharedPreferences.getString("loginName", "")); // 默认记住账号
			pwdEdit.setText(MySharedPerferences.sharedPreferences.getString("passWord", "")); // 默认记住密码
		}
		if (isChecked.equals("false")) {			
			mCheckSwithcButton.setChecked(false);
			accountEdit.setText(""); // 默认记住账号
			pwdEdit.setText(""); // 默认记住密码
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
			Toast.makeText(LoginActivity.this, "取消", Toast.LENGTH_LONG).show();
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
			// 获取动态验证码
			if (isDynamicVerification() == true) {
				time.start(); // 开始倒计时60s
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
						Toast.makeText(LoginActivity.this, "此账号未注册，请先注册后再登陆！", 0).show();
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
					Toast.makeText(LoginActivity.this, "验证码不匹配！", 0).show();

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
	 * 功能：验证码倒计时60s
	 * 
	 * @author aimin 2016-5-7
	 */
	public class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() { // 计时完毕
			dynamicVerificationBtn.setText("重新获取");
			dynamicVerificationBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) { // 计时过程
			dynamicVerificationBtn.setClickable(false);// 防止重复点击
			dynamicVerificationBtn.setText(millisUntilFinished / 1000 + "s");
		}

	}
	
	
	/**
	 * 动态密码登录验证
	 */
	@SuppressLint("ShowToast")
	private boolean isDynamicVerification() {
		flag = false;
		if (accountEdit.getText().toString().equals(""))
			Toast.makeText(LoginActivity.this, "请输入账号！", 0).show();
		else if (IsPhoneEmail.isMobile(accountEdit.getText().toString()) == false)
			Toast.makeText(LoginActivity.this, "账号格式不正确！", 0).show();
		else if (!Utils.isNetworkAvailable(this))
			Toast.makeText(LoginActivity.this, "无可用的网络！", 0).show();
		else {
			name = accountEdit.getText().toString();
			flag = true;
		}
		return flag;
	}

	/**
	 * 账号登录验证
	 */
	@SuppressLint("ShowToast")
	private boolean isVerification() {
		flag = false;
		if (accountEdit.getText().toString().equals(""))
			Toast.makeText(LoginActivity.this, "请输入账号！", 0).show();
		else if (pwdEdit.getText().toString().equals(""))
			Toast.makeText(LoginActivity.this, "请输入密码！", 0).show();
		else if (IsPhoneEmail.isMobile(accountEdit.getText().toString()) == false)
			Toast.makeText(LoginActivity.this, "账号格式不正确！", 0).show();
		else if (!Utils.isNetworkAvailable(this))
			Toast.makeText(LoginActivity.this, "无可用的网络！", 0).show();
		else {
			name = accountEdit.getText().toString();
			pwd = pwdEdit.getText().toString();
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
    Log.e("进来了", "进来了");
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
						
						Toast.makeText(LoginActivity.this, "服务器出现异常！", 0).show();
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
	 * 按照状态验证可否继续登录
	 */
	private void isLogin() {
	
		if (status.equals("0") && data == null) {
			MySharedPerferences.editor.putString("loginName", name);
			MySharedPerferences.editor.putString("userId", userId);
			MySharedPerferences.editor.putString("workerId", workerId);
			Log.e("userId", userId);
			Log.e("workerId", workerId);
			if (rememberPwd == true) { // 判断是不是点击记住密码按钮
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

	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void initJpush() {
		JPushInterface.init(getApplicationContext());
	}

	/**
	 * 发送别名到极光服务器
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
	 *设置通知栏样式 - 定义通知栏Layout
	 */
	private void setStyleCustom(){
		CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(LoginActivity.this,
			                              R.layout.notitfication_layout, 
			                              R.id.icon, 
			                              R.id.title, 
			                              R.id.text); 
			                             // 指定定制的 Notification Layout
		
		builder.layoutIconDrawable = R.drawable.head;

			 //   builder.statusBarDrawable = R.drawable.logo;      
			    // 指定最顶层状态栏小图标
			   // builder.layoutIconDrawable = R.drawable.top_bg;   
			    // 指定下拉状态栏时显示的通知图标
			    JPushInterface.setPushNotificationBuilder(1, builder);
	}

	/**
	 * 
	 * @param name 用手机号来榜定手机号
	 * 
	 */
	private void setAlias(String name) {
	    
	    if (TextUtils.isEmpty(name)) {
	        return;
	    }
	    if (!ExampleUtil.isValidTagAndAlias(name)) {
	        return;
	    }

	    // 调用 Handler 来异步设置别名
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
				
				// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
				break;
			case 6002:
				logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
				Log.i(TAG, logs);
				// 延迟 60 秒来调用 Handler 设置别名
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
				// 调用 JPush 接口来设置别名。
				JPushInterface.setAliasAndTags(getApplicationContext(),
						(String) msg.obj, null, mAliasCallback);
				break;
			default:
				Log.i(TAG, "Unhandled msg - " + msg.what);
			}
		}
	};
	
	
	// 点击返回键两次退出设置
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
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			// System.exit(0)是正常退出程序
		}
	}

}
