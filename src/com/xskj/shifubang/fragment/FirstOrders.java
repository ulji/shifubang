package com.xskj.shifubang.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.zshifu.R;
import com.xskj.shifubang.adapter.FirstOrdersAdapter;

/**
 * 功能：我的订单界面
 * 
 * @author aimin time:2016-5-28
 */

@SuppressLint("ResourceAsColor")
public class FirstOrders extends Fragment implements OnClickListener,
		OnPageChangeListener {
	private Button allOrders_btn;// 全部接单
	private Button alreadyOrders_btn;// 已接单
	private Button completeOrders_btn;// 完成接单
	private ImageView imageView;// 随界面滑动的图片
	private ViewPager viewPager;
	private ArrayList<Fragment> list;

	private int screen_width;// 屏幕宽度
	private int table_width;// 每个新闻标题所占屏宽
	FirstOrdersAdapter adapter; // 接单界面的ViewPager

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.first_orders, null);
		init(v);
		initViewPager(v);
		ButtonClick(0);
		return v;
	}

	/**
	 * 初始化控件
	 * 
	 * @param v
	 */
	private void init(View v) {

		allOrders_btn = (Button) v.findViewById(R.id.btn_first_allOrders);
		alreadyOrders_btn = (Button) v
				.findViewById(R.id.btn_first_alreadyOrders);
		completeOrders_btn = (Button) v
				.findViewById(R.id.btn_first_completeOrders);
		imageView = (ImageView) v.findViewById(R.id.cursor);
		allOrders_btn.setTextColor(0xff2C3E54);
		alreadyOrders_btn.setTextColor(0xff009A9A);
		completeOrders_btn.setTextColor(0xff009A9A);

		allOrders_btn.setOnClickListener(this);
		alreadyOrders_btn.setOnClickListener(this);
		completeOrders_btn.setOnClickListener(this);

		viewPager = (ViewPager) v.findViewById(R.id.viewpager_first_orders);
		viewPager.setOnPageChangeListener(this);

		// 获取屏幕的宽度和高度
		Display display = getActivity().getWindow().getWindowManager()
				.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		screen_width = metrics.widthPixels;
		table_width = (screen_width) / 3;
		// 设置下划线即imageView的宽度
		LinearLayout.LayoutParams lp = (LayoutParams) imageView
				.getLayoutParams();
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		allOrders_btn.measure(w, h);
		int width = allOrders_btn.getMeasuredWidth();
		lp.width = width;
		imageView.setLayoutParams(lp);
	}

	/**
	 * 初始化ViewPager控件
	 */
	private void initViewPager(View v) {

		// 关闭预加载，默认一次只加载一个Fragment
		viewPager.setOffscreenPageLimit(1);
		// 添加Fragment
		list = new ArrayList<Fragment>();
		list.add(new AllOrders());
		list.add(new AlreadyOrders());
		list.add(new CompleteOrders());

		// 适配器
		adapter = new FirstOrdersAdapter(getChildFragmentManager(), list);
		viewPager.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_first_allOrders:
			viewPager.setCurrentItem(0);
			allOrders_btn.setTextColor(0xff2C3E54);
			alreadyOrders_btn.setTextColor(0xff009A9A);
			completeOrders_btn.setTextColor(0xff009A9A);

			break;
		case R.id.btn_first_alreadyOrders:
			viewPager.setCurrentItem(1);
			allOrders_btn.setTextColor(0xff009A9A);
			alreadyOrders_btn.setTextColor(0xff2C3E54);
			completeOrders_btn.setTextColor(0xff009A9A);
			break;
		case R.id.btn_first_completeOrders:
			viewPager.setCurrentItem(2);
			allOrders_btn.setTextColor(0xff009A9A);
			alreadyOrders_btn.setTextColor(0xff009A9A);
			completeOrders_btn.setTextColor(0xff2C3E54);
			break;

		}
	}

	/**
	 * 此方法为修复Fragment嵌套ViewPager时的Bug
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");

			childFragmentManager.setAccessible(true);

			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {

			throw new RuntimeException(e);

		} catch (IllegalAccessException e) {

			throw new RuntimeException(e);

		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	/**
	 * 设置动画
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		lp.leftMargin = (int) ((arg0 + arg1) * table_width);

		imageView.setLayoutParams(lp);
	}

	@Override
	public void onPageSelected(int arg0) {
		ButtonClick(arg0);
	}

	/**
	 * 根据viewpager变化，标签颜色字体改变
	 * 
	 * @param pos
	 *            的值有三个 1 2 3
	 */
	private void ButtonClick(int pos) {
		Log.e("pos:", pos + "");
		switch (pos) {
		case 0:
			allOrders_btn.setTextColor(0xff2C3E54);
			alreadyOrders_btn.setTextColor(0xff009A9A);
			completeOrders_btn.setTextColor(0xff009A9A);
			break;
		case 1:
			allOrders_btn.setTextColor(0xff009A9A);
			alreadyOrders_btn.setTextColor(0xff2C3E54);
			completeOrders_btn.setTextColor(0xff009A9A);
			break;
		case 2:
			allOrders_btn.setTextColor(0xff009A9A);
			alreadyOrders_btn.setTextColor(0xff009A9A);
			completeOrders_btn.setTextColor(0xff2C3E54);
			break;
		}

	}

}
