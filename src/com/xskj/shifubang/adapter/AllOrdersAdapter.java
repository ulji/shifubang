package com.xskj.shifubang.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zshifu.R;
import com.xskj.shifubang.been.AllOrdersBeen;

@SuppressLint("SimpleDateFormat")
public class AllOrdersAdapter extends BaseAdapter {

	private List<AllOrdersBeen.T> list;
	private Context context;
	

	public AllOrdersAdapter(List<AllOrdersBeen.T> list, Context context) {
		this.list = list;
		this.context = context;

	}

	@SuppressWarnings("rawtypes")
	public List getlist() {
		return list;
	}

	public void setData(List<AllOrdersBeen.T> list) {
		this.list = list;
		notifyDataSetChanged();

	}

	@SuppressWarnings("unchecked")
	public void addData(List<AllOrdersBeen.T> list) {
		
		@SuppressWarnings("rawtypes")
		Set set = new LinkedHashSet<String>(); //List中有重复的数据
        set.addAll(list);
        this.list.clear();
        this.list.addAll(set);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// viewHolder优化模式
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
			viewHolder.sendAddress = (TextView) convertView.findViewById(R.id.order_location);
			viewHolder.column1 = (TextView) convertView.findViewById(R.id.order_destination);
			viewHolder.status = (TextView) convertView.findViewById(R.id.order_status);
			viewHolder.orderCreateTime = (TextView) convertView.findViewById(R.id.order_time);
			viewHolder.servicePrice = (TextView) convertView.findViewById(R.id.tv_orderPrice);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// ImageLoder.getImageByloader(list.get(position).getImgsrc(),viewHolder.image);
		viewHolder.sendAddress.setText(list.get(position).getSendAddress());
		String str = list.get(position).getOrderCreateTime();
		
		String a =GetStringFromLong(Long.valueOf(str));
		viewHolder.orderCreateTime.setText(a);
	//	viewHolder.status.setText(list.get(position).getColumn3());
		viewHolder.status.setText("新单");
		viewHolder.column1.setText(list.get(position).getColumn1());		
		String servicePrice = list.get(position).getServicePrice();
		if("".equals(servicePrice)||servicePrice==null)
			viewHolder.servicePrice.setText("¥0");
		else
			viewHolder.servicePrice.setText("¥"+servicePrice);
		
		return convertView;
	}
	
	public static String GetStringFromLong(long millis)
	{
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
	java.util.Date dt = new Date(millis); 
	return sdf.format(dt); 
	}
	
	class ViewHolder {		
		TextView sendAddress; //地址
		TextView column1; //描述
		TextView status;  //状态
		TextView orderCreateTime; //下单时间
		TextView servicePrice; //价格
	}
	

	

}
