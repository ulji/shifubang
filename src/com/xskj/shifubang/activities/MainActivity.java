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
 * ���ܣ�ʦ������ҳ
 * @author ������aimin 
 * ʱ�䣺2016-5-28
 * ��õ�
 */
public class MainActivity extends _FragmentActivity implements OnClickListener {

	private RelativeLayout myOrderBtn; // �ҵĶ�����ť
	private RelativeLayout personalBtn; // �������İ�ť
	Fragment firstOrders_frag; //�ҵĶ�����Ƭ
	Fragment firstPersonal_frag; //����������Ƭ
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
	 * ��ʼ���ؼ�
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
	 * ��ʼ��fragment
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
			// �ҵĶ�����ť����¼�
			myOrderImgBtn.setImageResource(R.drawable.ic_tab_home_show);
			personalImgBtn.setImageResource(R.drawable.ic_tab_me);
			startFragment(firstOrders_frag);
			

			break;
		case R.id.llytBtn_personal:
			// �������İ�ť����¼�
			myOrderImgBtn.setImageResource(R.drawable.ic_tab_home);
			personalImgBtn.setImageResource(R.drawable.ic_tab_me_highlight);
			startFragment(firstPersonal_frag);
			
			break;

		}

	}
	
	
	/**
	 * �ӱ�������ȡͼƬ��·��
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
				Log.e("onActivityResult", "���յ�ͼ��ͼƬ");
				break;
			case CropHelper.HEAD_FROM_CAMERA:
				Personal.mCropHelper.getDataFromCamera(data);
				Log.e("onActivityResult", "���յ�����ͼƬ");
				break;
			case CropHelper.HEAD_SAVE_PHOTO:
				if (data != null && data.getParcelableExtra("data") != null) {		
					Personal.circleImageView.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
					Personal.mCropHelper.savePhoto(data,OSUtils.getSdCardDirectory() + "/sfb_head.png");
					Log.e("ͷ�񱣴��·����", OSUtils.getSdCardDirectory() + "/sfb_head.png");
				}
				break;

			}
		}

	}
	
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
