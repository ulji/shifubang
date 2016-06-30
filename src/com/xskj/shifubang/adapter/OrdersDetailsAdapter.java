package com.xskj.shifubang.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zshifu.R;

public class OrdersDetailsAdapter extends BaseAdapter {

	public ArrayList<HashMap<String,String>> list;

	public Context context;

	public OrdersDetailsAdapter(ArrayList<HashMap<String,String>> list, Context context) {
		Log.e("1", "1");
		this.list = list;
		this.context = context;
		
	}
	@Override
	public int getCount() {
		Log.e("2", "2"+list.size());
		return list.size();
	}


	@Override
	public Object getItem(int position) {
		Log.e("3", "3"+list.get(position));
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		Log.e("4", "4"+position);
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("5", "5"+position);
		ViewHolder viewHolder = null;
		Log.e("5:", list.size()+",");
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			
			convertView = LayoutInflater.from(context).inflate(R.layout.details_goods, null);
			viewHolder.goods = (TextView) convertView.findViewById(R.id.tv_details_goods);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.goods.setText(list.get(position).get("goodsName"));
		Log.e(position+"", list.get(position).get("goodsName")+","+position);
		
		return convertView;
	}
	
	class ViewHolder {		
		TextView goods; //地址
	}
	

}
