package com.xskj.shifubang.cropimage;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zshifu.R;
import com.xskj.shifubang.activities.ServiceAreaDetailsActivity;
import com.xskj.shifubang.city.view.CityPicker;
import com.xskj.shifubang.city.view.CityPicker.OnSelectingListener;


/**
 * 
 * 选择城市选择器弹出框
 * @author 参数1：Activity act--MyPageRegisterActivity界面
 * @author 参数2：TextView location--选择的城市
 * @author 参数3：TextView location--选择的城市保存到数据库
 *
 */
public class ChooseLocationDialog {

	private Dialog mDialog = null;
	private Activity mActivity = null;
    private CityPicker cityPicker;	
    private TextView location;	
   

	private String locationDB;
    public static String cityId = "110228"; //城市编号
    private boolean flag = true; //首次弹出时候避免没有数据
    HashMap<String,String> map = new HashMap<String, String>();
    
	public String getLocationDB() {
		return locationDB;
	
	}

	public void setLocationDB(String locationDB) {
		this.locationDB = locationDB;
		
	}

	public ChooseLocationDialog(Activity act,TextView location,String locationDB) {
		this.mActivity = act;	
		this.location=location;	
		this.locationDB=locationDB;
	
	}

	public void popSelectDialog() {
			setDialog();
			mDialog.show();
	}
	
	
	private void setDialog() {
		if (mDialog == null) {
			
			mDialog = new Dialog(mActivity, R.style.MyDialog);
			mDialog.setContentView(R.layout.activity_register_main_pop_city);		
			mDialog.setCanceledOnTouchOutside(true);
			cityPicker = (CityPicker) mDialog.findViewById(R.id.citypicker);
			cityPicker.setOnSelectingListener(new OnSelectingListener() {

				@Override
				public void selected(boolean selected) {
				
					location.setText(cityPicker.getCity_string());
					location.setId(Integer.valueOf(cityId));
					cityId = cityPicker.getCity_code_string();					
					locationDB = cityPicker.getCity_string();
					
				}
			});
			if(flag==true) {
				if (ServiceAreaDetailsActivity.i < 12) {
					location = new TextView(mActivity);
					location.setId(Integer.parseInt(cityId));		
					location.setOnClickListener((OnClickListener) mActivity);
					location.setText(cityPicker.getCity_string());
					location.setTextColor(0Xffffffff);
					ServiceAreaDetailsActivity.wordWrapView.addView(location);
					ServiceAreaDetailsActivity.i++;
				} else {
					Toast.makeText(mActivity, "只能添加12个区域。", 0).show();
				}
				flag = false;
			}
			
		}else{
			if (ServiceAreaDetailsActivity.i < 12) {
				location = new TextView(mActivity);			
				location.setId(Integer.parseInt(cityId));
				location.setOnClickListener((OnClickListener) mActivity);
				location.setText(cityPicker.getCity_string());
				location.setTextColor(0Xffffffff);
				ServiceAreaDetailsActivity.wordWrapView.addView(location);
				ServiceAreaDetailsActivity.i++;
			} else {
				Toast.makeText(mActivity, "只能添加12个区域。", 0).show();
			}
		}
	}



}
