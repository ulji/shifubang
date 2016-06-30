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
	private PullToRefreshView pull; // ����
	private RequestQueue requestQueue; // �������
	private String sNum = "0"; //��ʼ����ҳ��
	private int eNum = 10;//��������ҳ��
	private ListView listView;
	public AllSQLite sqlite;
	private List<AllOrdersBeen.T> list; //ʵ�����е�list����
	private List<AllOrdersBeen.T> dblist; // ��SQLite��ȡ������
	private AllOrdersAdapter timelineAdapter; //������
	private HashMap<String, String> mapParameter; //��Ӳ���
	private int type = 0; //ˢ��״̬
	
	LinearLayout noDatalylt ;//--û�����ݵĲ���
	TextView noDatatv ;//--û�����ݵĲ���
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.first_orders_items, null);
		init(v);
		return v;
	}


	/**
	 * ��ʼ���ؼ�
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
		if (Utils.isNetworkAvailable(getActivity())) { // ������ʱ
			Log.e("ȫ������:", "ȫ������");
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
	 * Volley �� Gson��������ȡ���ݲ�����
	 * 
	 * @param path
	 *            �����ַ
	 */
	public void getDate(final HashMap<String, String> map) {
		Log.e("ȫ��������·��:", LinkUrlStatic.allOrdersUrl);
		Log.e("eNum:", eNum+"");
		StringRequest stringRequest = new StringRequest(Request.Method.POST,LinkUrlStatic.allOrdersUrl, new Listener<String>() {
					@SuppressWarnings("unchecked")
					@Override
					public void onResponse(String result) {
						Log.e("ȫ������---:", result+ "");
						sqlite.deleteAll();
						Gson gson = new Gson();
						AllOrdersBeen data = gson.fromJson(result, AllOrdersBeen.class);
						list.clear();
						if(data.getStatus().equals("1")&&data.getMsg().equals("���޶���")){//�ж����������û������
							Log.e("ȫ������----���޶���:", result + "");
							noDatalylt.setVisibility(View.GONE);
							noDatatv.setVisibility(View.VISIBLE);					
						}else {//������������
							
							noDatalylt.setVisibility(View.VISIBLE);
							noDatatv.setVisibility(View.GONE);
							
							list = data.getObject();
							Log.e("ȫ�������������أ�", LinkUrlStatic.allOrdersUrl);
						
							if (type == 0) {
								// ��һ�ν���
								timelineAdapter.setData(list);
							} else if (type == 1) {
								// ˢ��
								timelineAdapter.setData(list);
								pull.onHeaderRefreshFinish(); // �ر�ˢ����
							} else if (type == 2) {
								
								timelineAdapter.addData(list); // ����
								pull.onFooterLoadFinish(); // �رռ�����
							}
							list = timelineAdapter.getlist();
							addSqlite(list);
						}
										
					}
				}, new ErrorListener() {

					@SuppressLint("ShowToast")
					@Override
					public void onErrorResponse(VolleyError err) {
						Toast.makeText(getActivity(), "�����������쳣��", Toast.LENGTH_LONG);
						Log.e("volley����", err.getMessage(), err);
					}
				})  {
			@Override
			protected HashMap<String, String> getParams() {
				// ������������Ҫpost�Ĳ���
			
				return map;
			}
		};
		requestQueue.add(stringRequest);
	}
	
	/**
	 * getSendAddress ��ַ 
	 * getColumn1 �������� 
	 * getColumn3; ״̬ 
	 * getOrderCreateTime; �µ�ʱ��
	 * getOrderId; Ψһ��ʾ
	 * 
	 * @param list  ���浽SQLite���ݿ�
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
	 * û�������ʱ��ӱ���SQLite���ݿ⻺���ȡ����
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
		/*intent.putExtra("orderId",list.get(position).getOrderId() + ""); //ΨһId
		intent.putExtra("orderState",list.get(position).getOrderState() + ""); //����״̬
*/		startActivity(intent);
	}

	/**
	 * ��������
	 */
	@Override
	public void onFooterLoad(PullToRefreshView view) {
		if (Utils.isNetworkAvailable(getActivity())) // ������ʱ
		{
			type = 2;
			eNum += 10;		
			mapParameter.put("eNum", String.valueOf(eNum));
			getDate(mapParameter);
		} else {
			Toast.makeText(getActivity(), "��������", Toast.LENGTH_LONG).show();
			pull.onFooterLoadFinish();
		}		
	}
	
	/**
	 * ����ˢ��
	 */
	@Override
	public void onHeaderRefresh(PullToRefreshView view) {

		if (Utils.isNetworkAvailable(getActivity())) // ������ʱ
		{
			type = 1;
			eNum = 10;
			mapParameter.put("eNum", String.valueOf(eNum));
			getDate(mapParameter);
		} else {
			Toast.makeText(getActivity(), "��������", Toast.LENGTH_LONG).show();
			pull.onHeaderRefreshFinish();
		}
	}
}
