package com.xskj.shifubang.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.xskj.shifubang.cropimage.CropHelper;
import com.xskj.shifubang.cropimage.OSUtils;
import com.xskj.shifubang.fragment.FirstOrders;
import com.xskj.shifubang.fragment.Personal;
import com.zshifu.R;

/**
 * 功能：师傅帮首页
 * @author 姓名：aimin 
 * 时间：2016-5-28
 * 你好的
 */
public class MainActivity extends _FragmentActivity implements OnClickListener {

	private RelativeLayout myOrderBtn; // 我的订单按钮
	private RelativeLayout personalBtn; // 个人中心按钮
	Fragment firstOrders_frag; //我的订单碎片
	Fragment firstPersonal_frag; //个人中心碎片
	private  ImageView myOrderImgBtn;
	private  ImageView personalImgBtn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		init();
		initFragment();
		startFragment(firstOrders_frag);
		
	}
	


	/**
	 * 初始化控件
	 */
	private void init() {
		myOrderBtn = $(R.id.llytBtn_myorder);
		personalBtn = $(R.id.llytBtn_personal);
		myOrderImgBtn = $(R.id.imgBtn_myorder);
		personalImgBtn = $(R.id.imgBtn_personal);
		
		myOrderImgBtn.setImageResource(R.drawable.ic_tab_home_show);
		personalImgBtn.setImageResource(R.drawable.ic_tab_me);
		
		myOrderBtn.setOnClickListener(this);
		personalBtn.setOnClickListener(this);
		
	}

	/**
	 * 初始化fragment
	 */
	private void initFragment() {
		firstOrders_frag = new FirstOrders();
		firstPersonal_frag = new Personal();
	}

	private void startFragment(Fragment ff) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.faty_first, ff);
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llytBtn_myorder:
			// 我的订单按钮点击事件
			myOrderImgBtn.setImageResource(R.drawable.ic_tab_home_show);
			personalImgBtn.setImageResource(R.drawable.ic_tab_me);
			startFragment(firstOrders_frag);
			

			break;
		case R.id.llytBtn_personal:
			// 个人中心按钮点击事件
			myOrderImgBtn.setImageResource(R.drawable.ic_tab_home);
			personalImgBtn.setImageResource(R.drawable.ic_tab_me_highlight);
			startFragment(firstPersonal_frag);
			
			break;

		}

	}
	
	
	/**
	 * 从本地相册获取图片级路径
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		Log.e("onActivityResult", requestCode + "**" + resultCode);

		if (requestCode == RESULT_CANCELED) {
			return;
		} else {
			switch (requestCode) {
			case CropHelper.HEAD_FROM_ALBUM:
				Personal.mCropHelper.getDataFromAlbum(data);
				Log.e("onActivityResult", "接收到图库图片");
				break;
			case CropHelper.HEAD_FROM_CAMERA:
				Personal.mCropHelper.getDataFromCamera(data);
				Log.e("onActivityResult", "接收到拍照图片");
				break;
			case CropHelper.HEAD_SAVE_PHOTO:
				if (data != null && data.getParcelableExtra("data") != null) {		
					Personal.circleImageView.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
					Personal.mCropHelper.savePhoto(data,OSUtils.getSdCardDirectory() + "/sfb_head.png");
					Log.e("头像保存的路径：", OSUtils.getSdCardDirectory() + "/sfb_head.png");
				}
				break;

			}
		}

	}
	
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
