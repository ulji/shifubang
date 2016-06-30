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
	private Button nextBtn; // 下一个的按钮
	private Button dynamicBtn; // 获取验证码按钮
	private ImageView returnBtn; // 返回按钮
	public TimeCount time; // 验证码倒计时
	
	private EditText accountEt; // 账户编辑
	private EditText verificationCodeEt; // 验证码编辑
	private EditText pwdEt; // 密码编辑
	private EditText aginPwdEt; // 再次输入密码编辑	
	private RequestQueue requestQueue; // 请求队列
	public ProgersssDialogTools progressDialog ; // 加载圈
	private String name; //账户
	private boolean flag = false; //判断验证
	private boolean flagNext = false; //下一步按钮的判断
	private HashMap<String, String> mapParameter; //添加参数
	
	private LoginBeen loginBeen; //验证码的实体类
	private String msg; //请求结果，消息
	private String status; //请求结果，状态
	private String data; //请求结果，数据
	private String getData; //请求结果


	public MySharedPerferences mySharedPerferences ;//缓存
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();
	}

	/**
	 * 初始化控件
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
			//测试
			if(isNext() == true) {		
				progressDialog = new ProgersssDialogTools(RegisterActivity.this);
				mapParameter.clear();
				mapParameter.put("loginName", name);
				mapParameter.put("pwd", pwdEt.getText().toString());
				getDate(LinkUrlStatic.registerUrl,mapParameter);	
			}		
			break;
		case R.id.btn_register_dynamic://获取动态验证码	
			if(isDynamicVerification() == true) {	
				time.start();
				mapParameter.put("phoneNum", name);
				getDate(LinkUrlStatic.genCodeUrl,mapParameter);	
			}
			break;
		case R.id.register_return://返回按钮		
			finish();
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		}
	}
	
	/**
	 * 下一步按钮之前的一些验证
	 */
	@SuppressLint("ShowToast")
	private boolean isNext() {
		getData = data;
		flagNext = false;	
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "请输入账号！", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(RegisterActivity.this, "账号格式不正确！", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(RegisterActivity.this, "无可用的网络！", 0).show();
		else if(verificationCodeEt.getText().toString().equals("")) {			
			Toast.makeText(RegisterActivity.this, "验证码不能为空！", 0).show();		
		}
		else if(!verificationCodeEt.getText().toString().equals(getData)) {			
			Toast.makeText(RegisterActivity.this, "验证码不匹配，请重新获取！", 0).show();		
		}
		else if(pwdEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "密码不能为空！", 0).show();		
		else if(aginPwdEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "请再次输入密码！", 0).show();
		else if(!aginPwdEt.getText().toString().equals(pwdEt.getText().toString()))
			Toast.makeText(RegisterActivity.this, "密码不匹配，请重新输入！", 0).show();		
		else if(msg.equals("电话号已注册")) {			
			Toast.makeText(RegisterActivity.this, "此用户已注册！", 0).show();	
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
						Log.e("resule1111:", result + "");						
						Gson gson = new Gson();
						loginBeen = gson.fromJson(result, LoginBeen.class);		
						volleyGetData();
					}

				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {
						Toast.makeText(RegisterActivity.this, "服务器出现异常！", 0).show();
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
	 * 动态密码登录验证
	 */
	@SuppressLint("ShowToast")
	private boolean isDynamicVerification() {
		
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(RegisterActivity.this, "请输入账号！", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(RegisterActivity.this, "账号格式不正确！", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(RegisterActivity.this, "无可用的网络！", 0).show();
		else{
			name = accountEt.getText().toString();
			flag = true;			
		}
		return flag;
	}
	
	/**
	 * volley 解析得到的数据
	 */
	private void volleyGetData() {	
		status = loginBeen.getStatus();
		msg = loginBeen.getMsg();
		this.data = loginBeen.getData();	
		//if(data!=null)
		//verificationCodeEt.setText(data+"");
		if(msg.equals("电话号已注册"))
			Toast.makeText(RegisterActivity.this, msg, 0).show();
		
		if (msg.equals("注册成功") && status.equals("0")){
			progressDialog._dismiss();
			Log.e("getData:", getData);
			finish();
			startActivity(new Intent(RegisterActivity.this,SettledActivity.class));
		
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
			dynamicBtn.setText("重新获取");
			dynamicBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) { // 计时过程
			dynamicBtn.setClickable(false);// 防止重复点击
			dynamicBtn.setText(millisUntilFinished / 1000 + "s");
		}

	}
	

}
