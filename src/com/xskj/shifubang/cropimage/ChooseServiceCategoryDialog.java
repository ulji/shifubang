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
import android.widget.Toast;

import com.zshifu.R;
import com.xskj.shifubang.activities.ServiceTypeDetailsActivity;

/**
 * 
 * 服务类型弹出框
 * @author 参数1：Activity act--MyPageRegisterActivity界面
 * @author 参数2：TextView oneCheckBoxStr--参数
 *
 */
public class ChooseServiceCategoryDialog implements OnCheckedChangeListener {

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
  private String[] sendCode = new String[3];;
  private String[] sendStr0 = new String[3];;
	

	public ChooseServiceCategoryDialog(Activity act) {
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
			oneCheck.setOnCheckedChangeListener(this);
			twoCheck.setOnCheckedChangeListener(this);
			threeCheck.setOnCheckedChangeListener(this);
			foreCheck.setOnCheckedChangeListener(this);
			
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
