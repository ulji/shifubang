package com.xskj.shifubang.activities;

import android.support.v4.app.FragmentActivity;
import android.view.View;

public class _FragmentActivity extends FragmentActivity{

		 @SuppressWarnings("unchecked")
		 public <T extends View> T $(int id) {  
		     return (T) findViewById(id);  
		  } 

}
