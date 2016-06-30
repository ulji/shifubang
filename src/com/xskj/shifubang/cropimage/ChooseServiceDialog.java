package com.xskj.shifubang.cropimage;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.zshifu.R;
import com.xskj.shifubang.activities.ServiceAreaDetailsActivity;
import com.xskj.shifubang.activities.ServiceTypeDetailsActivity;

/**
 * 
 * 服务类型弹出框
 * @author 参数1：Activity act--MyPageRegisterActivity界面
 * @author 参数2：TextView oneCheckBoxStr--参数
 *
 */
public class ChooseServiceDialog implements OnClickListener,OnCheckedChangeListener {

	private Dialog mDialog = null;
	private Activity mActivity = null;
    private CheckBox oneCheck;
    private CheckBox twoCheck;
    private CheckBox threeCheck;
    private CheckBox foreCheck;
    private TextView oneTv;
    private TextView twoTv;
    private TextView threeTv;
    private TextView foreTv;    
    public static HashMap<String,String> map;
    public ChooseServiceFurnitureDialog furnitureDialog;//维修保养服务类型
  private String[] sendCode = new String[3];
  
  public String[]  sendCode1 = new String[7]; //维修保养服务类型选项编号
  public String[]  sendStr0 = new String[7]; //维修保养服务类型选项键
  public String[]  sendStr1; //维修保养服务类型选项文字
	

	public ChooseServiceDialog(Activity act) {
		mActivity = act;	
		
	}

	public void popSelectDialog(String[] sendStr,String[] sendCode) {
		   this.sendStr0 = sendStr;
		    this.sendCode = sendCode;
			setDialog(sendStr);
			mDialog.show();
	}


	private void setDialog(String[] sendStr) {
		if (mDialog == null) {
			mDialog = new Dialog(mActivity, R.style.MyDialog);
			mDialog.setContentView(R.layout.service_furniture_pop);
			mDialog.setCanceledOnTouchOutside(true);
			oneCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_one);
			twoCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_two);
			threeCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_three);
			foreCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_fore);
			oneTv = (TextView) mDialog.findViewById(R.id.tv_service_one);
			twoTv = (TextView) mDialog.findViewById(R.id.tv_service_two);
			threeTv = (TextView) mDialog.findViewById(R.id.tv_service_three);
			foreTv = (TextView) mDialog.findViewById(R.id.tv_service_fore);		
			oneTv.setText(sendStr[0]);
			twoTv.setText(sendStr[1]);
			threeTv.setText(sendStr[2]);
			foreTv.setText(sendStr[3]);		
			map = new HashMap<String,String>();
			
			furnitureDialog = new ChooseServiceFurnitureDialog(mActivity);
			oneCheck.setOnCheckedChangeListener(this);
			twoCheck.setOnCheckedChangeListener(this);
			threeCheck.setOnCheckedChangeListener(this);
			foreCheck.setOnCheckedChangeListener(this);
			foreTv.setOnClickListener(this);
		}
	}


	@Override
	public void onClick(View v) {		
		switch (v.getId()) {		
		case R.id.tv_service_fore:
			mDialog.dismiss();
			sendStr1 = new String[]{"板式家具","实木家具","办公家具","大理石   ","皮革修复","沙发换皮","家具美容","磕碰修复"};
			sendCode1 = new String[]{"7","8","9","10","11","12","13","14"};
			furnitureDialog.popSelectDialog(sendStr1, sendCode1);
			break;

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {
		case R.id.checkbox_service_one:
			if (isChecked) {
				map.put(sendStr0[0], sendCode[0]) ;
			} else {
				map.remove(sendStr0[0]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[0]);
			}
			break;
		case R.id.checkbox_service_two:
			if (isChecked) {
				map.put(sendStr0[1], sendCode[1]) ;
			}else {
				map.remove(sendStr0[1]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[1]);
			}
			break;
		case R.id.checkbox_service_three:
			if (isChecked) {
				map.put(sendStr0[2], sendCode[2]) ;
			
			}else {
				map.remove(sendStr0[2]);		
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[2]);
			}
			break;
		case R.id.checkbox_service_fore:
			if (isChecked) {
				map.put(sendStr0[3], sendCode[3]) ;
				
			
			}else {
				map.remove(sendStr0[3]);	
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[3]);
			}
			break;
		}

		ServiceTypeDetailsActivity.mapAll.putAll(map);
			Log.e("sendCode:", map.size()+"");
	
			

	}

}
