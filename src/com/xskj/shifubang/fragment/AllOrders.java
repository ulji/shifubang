package com.xskj.shifubang.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zshifu.R;
import com.google.gson.Gson;
import com.xskj.shifubang.activities.OrderDetailsActivity;
import com.xskj.shifubang.adapter.AllOrdersAdapter;
import com.xskj.shifubang.been.AllOrdersBeen;
import com.xskj.shifubang.db.AllSQLite;
import com.xskj.shifubang.db.MySharedPerferences;
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.PullToRefreshView;
import com.xskj.shifubang.tools.PullToRefreshView.OnFooterLoadListener;
import com.xskj.shifubang.tools.PullToRefreshView.OnHeaderRefreshListener;
import com.xskj.shifubang.tools.Utils;

public class AllOrders extends Fragment implements OnHeaderRefreshListener,OnFooterLoadListener , OnItemClickListener {
	private PullToRefreshView pull; // 下拉
	private RequestQueue requestQueue; // 请求队列
	private String sNum = "0"; //开始计数页码
	private int eNum = 10;//结束计数页码
	private ListView listView;
	public AllSQLite sqlite;
	private List<AllOrdersBeen.T> list; //实体类中的list集合
	private List<AllOrdersBeen.T> dblist; // 从SQLite获取的数据
	private AllOrdersAdapter timelineAdapter; //适配器
	private HashMap<String, String> mapParameter; //添加参数
	private int type = 0; //刷新状态
	
	LinearLayout noDatalylt ;//--没有数据的布局
	TextView noDatatv ;//--没有数据的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.first_orders_items, null);
		init(v);
		return v;
	}


	/**
	 * 初始化控件
	 * 
	 * @param v
	 */
	private void init(View v) {
		mapParameter = new HashMap<String, String>();
		pull = (PullToRefreshView) v.findViewById(R.id.pull);
		listView = (ListView) v.findViewById(R.id.lv_ordersAll);
		listView.setOnItemClickListener(this);
		noDatalylt = (LinearLayout) v.findViewById(R.id.no_llyt);
		noDatatv = (TextView) v.findViewById(R.id.tv_no_data);
		
		pull.setOnHeaderRefreshListener(this);
		pull.setOnFooterLoadListener(this);
		
		requestQueue = Volley.newRequestQueue(getActivity());
		sqlite = new AllSQLite(getActivity());
		list = new ArrayList<AllOrdersBeen.T>();
		dblist = new ArrayList<AllOrdersBeen.T>();
			timelineAdapter = new AllOrdersAdapter(list, getActivity());
			listView.setAdapter(timelineAdapter);
			loadData();
		
	}
	
	
	private void loadData() {
		if (Utils.isNetworkAvailable(getActivity())) { // 有网络时
			Log.e("全部订单:", "全部订单");
			mapParameter.put("workerId", MySharedPerferences.sharedPreferences.getString("workerId", ""));
			mapParameter.put("sNum", sNum);
			mapParameter.put("eNum", String.valueOf(eNum));
			getDate(mapParameter);
		} else {
			getSqliteData();
			timelineAdapter.setData(dblist);
		}		
	}


	/**
	 * Volley 和 Gson第三方获取数据并解析
	 * 
	 * @param path
	 *            请求地址
	 */
	public void getDate(final HashMap<String, String> map) {
		Log.e("全部订单的路径:", LinkUrlStatic.allOrdersUrl);
		Log.e("eNum:", eNum+"");
		StringRequest stringRequest = new StringRequest(Request.Method.POST,LinkUrlStatic.allOrdersUrl, new Listener<String>() {
					@SuppressWarnings("unchecked")
					@Override
					public void onResponse(String result) {
						Log.e("全部订单---:", result+ "");
						sqlite.deleteAll();
						Gson gson = new Gson();
						AllOrdersBeen data = gson.fromJson(result, AllOrdersBeen.class);
						list.clear();
						if(data.getStatus().equals("1")&&data.getMsg().equals("暂无订单")){//判断如果集合中没有数据
							Log.e("全部订单----暂无订单:", result + "");
							noDatalylt.setVisibility(View.GONE);
							noDatatv.setVisibility(View.VISIBLE);					
						}else {//集合中有数据
							
							noDatalylt.setVisibility(View.VISIBLE);
							noDatatv.setVisibility(View.GONE);
							
							list = data.getObject();
							Log.e("全部订单上拉加载：", LinkUrlStatic.allOrdersUrl);
						
							if (type == 0) {
								// 第一次进入
								timelineAdapter.setData(list);
							} else if (type == 1) {
								// 刷新
								timelineAdapter.setData(list);
								pull.onHeaderRefreshFinish(); // 关闭刷新栏
							} else if (type == 2) {
								
								timelineAdapter.addData(list); // 加载
								pull.onFooterLoadFinish(); // 关闭加载栏
							}
							list = timelineAdapter.getlist();
							addSqlite(list);
						}
										
					}
				}, new ErrorListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onErrorResponse(VolleyError err) {
						Toast.makeText(getActivity(), "服务器请求异常！", Toast.LENGTH_LONG);
						Log.e("volley错误。", err.getMessage(), err);
					}
				})  {
			@Override
			protected HashMap<String, String> getParams() {
				// 在这里设置需要post的参数
			
				return map;
			}
		};
		requestQueue.add(stringRequest);
	}
	
	/**
	 * getSendAddress 地址 
	 * getColumn1 订单描述 
	 * getColumn3; 状态 
	 * getOrderCreateTime; 下单时间
	 * getOrderId; 唯一标示
	 * 
	 * @param list  保存到SQLite数据库
	 */
	public void addSqlite(List<AllOrdersBeen.T> list) {
		for (int i = 0; i < list.size(); i++) {
			sqlite.insert(list.get(i).getSendAddress(),
					list.get(i).getOrderCreateTime(),
					list.get(i).getColumn1(),
					list.get(i).getColumn3(),
					list.get(i).getOrderId());
		}
	}
	
	/**
	 * 没有网络的时候从本地SQLite数据库缓存获取数据
	 */
	public void getSqliteData() {	
		
		Cursor c = sqlite.query();		
		if (c.moveToFirst()) {
			do {
				AllOrdersBeen.T data = new AllOrdersBeen.T();
				data.setSendAddress(c.getString(c.getColumnIndex("sendAddress")));
				data.setOrderCreateTime(c.getString(c.getColumnIndex("orderCreateTime")));
				data.setColumn1(c.getString(c.getColumnIndex("column1")));
				data.setColumn3(c.getString(c.getColumnIndex("column3")));
				data.setOrderId(c.getString(c.getColumnIndex("orderId")));				
				dblist.add(data);
			} while (c.moveToNext());
		}
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
		MySharedPerferences.editor.putString("orderId", list.get(position).getOrderId() + "");
		MySharedPerferences.editor.putString("orderState",list.get(position).getOrderState() + "");
		MySharedPerferences.editor.commit();
		/*intent.putExtra("orderId",list.get(position).getOrderId() + ""); //唯一Id
		intent.putExtra("orderState",list.get(position).getOrderState() + ""); //服务状态
*/		startActivity(intent);
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onFooterLoad(PullToRefreshView view) {
		if (Utils.isNetworkAvailable(getActivity())) // 有网络时
		{
			type = 2;
			eNum += 10;		
			mapParameter.put("eNum", String.valueOf(eNum));
			getDate(mapParameter);
		} else {
			Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_LONG).show();
			pull.onFooterLoadFinish();
		}		
	}
	
	/**
	 * 下拉刷新
	 */
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {

		if (Utils.isNetworkAvailable(getActivity())) // 有网络时
		{
			type = 1;
			eNum = 10;
			mapParameter.put("eNum", String.valueOf(eNum));
			getDate(mapParameter);
		} else {
			Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_LONG).show();
			pull.onHeaderRefreshFinish();
		}
	}
}
