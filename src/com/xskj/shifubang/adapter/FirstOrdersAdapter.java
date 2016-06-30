package com.xskj.shifubang.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 功能：接单界面viewPager
 * @author aimin
 * 时间:2016-5-28
 */

public class FirstOrdersAdapter extends FragmentPagerAdapter {
	
	ArrayList<Fragment> list;
	public FirstOrdersAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		this.list=list;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}
		
}
