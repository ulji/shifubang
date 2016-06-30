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

	private EditText accountEt; //账号编辑
	private EditText verificationEt; //验证码编辑
	private EditText pwdEt; //密码编辑
	private EditText pwdAginEt; //再次输入密码编辑
	private Button dynamicBtn; //获取验证码按钮
	private Button nextBtn; //提交按钮
	private ImageView returnBtn; //返回
	public TimeCount time; // 验证码倒计时
	private RequestQueue requestQueue; // 请求队列
	public ProgersssDialogTools progressDialog ; // 加载圈
	private HashMap<String, String> mapParameter; //添加参数
	private String name; //账户
	
	private boolean flag = false; //判断验证
	private boolean flagNext = false; //下一步按钮的判断
	private LoginBeen loginBeen; //验证码的实体类
	private String msg; //请求结果，消息
	private String status; //请求结果，状态
	private String data; //请求结果，数据
	private String isData; //请求结果，匹配判断
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		init();
		
	}
	
	/**
	 * 初始化控件
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
            //获取动态验证码按钮事件
			if(isDynamicVerification()==true) {
				progressDialog = new ProgersssDialogTools(ForgetPasswordActivity.this);
				time.start();
				mapParameter.put("phoneNum", name);
				getDate(LinkUrlStatic.checkLoginUrl,mapParameter);
			}
			
			break;
		case R.id.btn_rorget_next:
			//提交按钮事件
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
			
		case R.id.pwd_rorget_return://返回按钮		
			finish();
			
			break;
		}

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

					@SuppressLint("ShowToast")
					@Override
					public void onErrorResponse(VolleyError err) {	
						Toast.makeText(ForgetPasswordActivity.this, "服务器出现异常！", 0).show();
						progressDialog._dismiss();
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
	 * 下一步按钮之前的一些验证
	 */
	@SuppressLint("ShowToast")
	private boolean isNext() {
		flagNext = false;
		isData = data;
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(this, "请输入账号！", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(this, "账号格式不正确！", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(this, "无可用的网络！", 0).show();
		else if(verificationEt.getText().toString().equals("")) {			
			Toast.makeText(this, "验证码不能为空！", 0).show();		
		}
		else if(!verificationEt.getText().toString().equals(isData)) {			
			Toast.makeText(this, "验证码不匹配，请重新获取！", 0).show();		
		}
		else if(pwdEt.getText().toString().equals(""))
			Toast.makeText(this, "密码不能为空！", 0).show();		
		else if(pwdAginEt.getText().toString().equals(""))
			Toast.makeText(this, "请再次输入密码！", 0).show();
		else if(!pwdAginEt.getText().toString().equals(pwdEt.getText().toString()))
			Toast.makeText(this, "密码不匹配，请重新输入！", 0).show();		
	
		else{		
			flagNext = true;			
		}
		return flagNext;
	
	}
	
	
	/**
	 * 动态密码登录验证
	 */
	@SuppressLint("ShowToast")
	private boolean isDynamicVerification() {
		
		if(accountEt.getText().toString().equals(""))
			Toast.makeText(this, "请输入账号！", 0).show();
		else if(IsPhoneEmail.isMobile(accountEt.getText().toString())==false)
			Toast.makeText(this, "账号格式不正确！", 0).show();
		else if(!Utils.isNetworkAvailable(this))
			Toast.makeText(this, "无可用的网络！", 0).show();
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
		progressDialog._dismiss();
		status = loginBeen.getStatus();
		msg = loginBeen.getMsg();
		data = loginBeen.getData();	
		//if(data!=null)
			//verificationEt.setText(data+"");
		if (msg.equals("修改成功") && status.equals("0")){			
			Log.e("isData:", isData);
			if (verificationEt.getText().toString().equals(isData)){				
				finish();			
				startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
			}else {
				Toast.makeText(ForgetPasswordActivity.this,"验证码不匹配！" , 0).show();
			}
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
