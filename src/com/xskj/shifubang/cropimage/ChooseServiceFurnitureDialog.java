package com.xskj.shifubang.cropimage;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.zshifu.R;
import com.xskj.shifubang.activities.ServiceTypeDetailsActivity;

/**
 * 
 * 维修保养->服务类型弹出框
 * @author 参数1：Activity act--MyPageRegisterActivity界面
 * @author 参数2：TextView oneCheckBoxStr--参数
 *
 */
public class ChooseServiceFurnitureDialog implements OnCheckedChangeListener {

	private Dialog mDialog = null;
	private Activity mActivity = null;
    private CheckBox oneCheck;
    private CheckBox twoCheck;
    private CheckBox threeCheck;
    private CheckBox foreCheck;
    private CheckBox fiveCheck;
    private CheckBox sixCheck;
    private CheckBox sevenCheck;
    private CheckBox eightCheck;
  
    private TextView oneTv;
    private TextView twoTv;
    private TextView threeTv;
    private TextView foreTv;    
    private TextView fiveTv;
    private TextView sixTv;
    private TextView sevenTv;
    private TextView eightTv;    
    public static HashMap<String,String> map;
    public String[] sendCode = new String[7];;
    public String[] sendStr0 = new String[7];;
	

	public ChooseServiceFurnitureDialog(Activity act) {
		mActivity = act;	
		
	}

	public void popSelectDialog(String[] sendStr,String[] sendCode) {
		    this.sendCode = sendCode;
		    this.sendStr0 = sendStr;
			setDialog(sendStr);
			mDialog.show();
	}


	private void setDialog(String[] sendStr) {
		if (mDialog == null) {
			mDialog = new Dialog(mActivity, R.style.MyDialog);
			mDialog.setContentView(R.layout.service_furniture_maintenance_pop);
			mDialog.setCanceledOnTouchOutside(true);
			oneCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_one);
			twoCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_two);
			threeCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_three);
			foreCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_fore);			
			fiveCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_five);
			sixCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_six);
			sevenCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_seven);
			eightCheck = (CheckBox) mDialog.findViewById(R.id.checkbox_service_details_eight);
			
			oneTv = (TextView) mDialog.findViewById(R.id.tv_service_details_one);
			twoTv = (TextView) mDialog.findViewById(R.id.tv_service_details_two);
			threeTv = (TextView) mDialog.findViewById(R.id.tv_service_details_three);
			foreTv = (TextView) mDialog.findViewById(R.id.tv_service_details_fore);	
			fiveTv = (TextView) mDialog.findViewById(R.id.tv_service_details_five);
			sixTv = (TextView) mDialog.findViewById(R.id.tv_service_details_six);
			sevenTv = (TextView) mDialog.findViewById(R.id.tv_service_details_seven);
			eightTv = (TextView) mDialog.findViewById(R.id.tv_service_details_eight);	
			
			oneTv.setText(sendStr[0]);
			twoTv.setText(sendStr[1]);
			threeTv.setText(sendStr[2]);
			foreTv.setText(sendStr[3]);
			fiveTv.setText(sendStr[4]);
			sixTv.setText(sendStr[5]);
			sevenTv.setText(sendStr[6]);
			eightTv.setText(sendStr[7]);
			map = new HashMap<String,String>(8);

			oneCheck.setOnCheckedChangeListener(this);
			twoCheck.setOnCheckedChangeListener(this);
			threeCheck.setOnCheckedChangeListener(this);
			foreCheck.setOnCheckedChangeListener(this);
			fiveCheck.setOnCheckedChangeListener(this);
			sixCheck.setOnCheckedChangeListener(this);
			sevenCheck.setOnCheckedChangeListener(this);
			eightCheck.setOnCheckedChangeListener(this);
			
		}
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		switch (buttonView.getId()) {
		case R.id.checkbox_service_details_one:
			if (isChecked) {
				map.put(sendStr0[0], sendCode[0]);
			} else {
				map.remove(sendStr0[0]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[0]);
			}
			break;
		case R.id.checkbox_service_details_two:
			if (isChecked) {
				map.put(sendStr0[1], sendCode[1]);
			} else {
				map.remove(sendStr0[1]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[1]);
			}
			break;
		case R.id.checkbox_service_details_three:
			if (isChecked) {
				map.put(sendStr0[2], sendCode[2]);

			} else {
				map.remove(sendStr0[2]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[2]);
			}
			break;
		case R.id.checkbox_service_details_fore:
			if (isChecked) {
				map.put(sendStr0[3], sendCode[3]);

			} else {
				map.remove(sendStr0[3]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[3]);
			}
			break;
		case R.id.checkbox_service_details_five:
			if (isChecked) {
				map.put(sendStr0[4], sendCode[4]);
			} else {
				map.remove(sendStr0[4]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[4]);
			}
			break;
		case R.id.checkbox_service_details_six:
			if (isChecked) {
				map.put(sendStr0[5], sendCode[5]);

			} else {
				map.remove(sendStr0[5]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[5]);
			}
			break;
		case R.id.checkbox_service_details_seven:
			if (isChecked) {
				map.put(sendStr0[6], sendCode[6]);

			} else {
				map.remove(sendStr0[6]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[6]);
			}
			break;
		case R.id.checkbox_service_details_eight:
			if (isChecked) {
				map.put(sendStr0[7], sendCode[7]);

			} else {
				map.remove(sendStr0[7]);
				ServiceTypeDetailsActivity.mapAll.remove(sendStr0[7]);
			}
			break;
		}
		ServiceTypeDetailsActivity.mapAll.putAll(map);
		Log.e("sendCode:", map.size() + "");

	}

}
