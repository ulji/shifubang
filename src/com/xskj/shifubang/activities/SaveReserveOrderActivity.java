package com.xskj.shifubang.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
import com.photo.choosephotos.adapter.AddImageGridAdapter;
import com.photo.choosephotos.controller.SelectPicPopupWindow;
import com.photo.choosephotos.photo.Item;
import com.photo.choosephotos.photo.PhotoAlbumActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerDeleteActivity;
import com.photo.choosephotos.util.PictureManageUtil;
import com.xskj.shifubang.adapter.OrdersDetailsAdapter;
import com.xskj.shifubang.been.DetailOrdersBeen;
import com.xskj.shifubang.been.DetailOrdersBeen.ListEntity;
import com.xskj.shifubang.db.AllSQLite;
import com.xskj.shifubang.db.MySharedPerferences;
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.HttpPostUploadUtil;
import com.xskj.shifubang.tools.MyDialog;
import com.xskj.shifubang.tools.MyDialog.DialogClickListener;
import com.xskj.shifubang.view.ProgersssDialogTools;


public class SaveReserveOrderActivity extends Activity implements
		OnClickListener,DialogClickListener {

	private String loginName; // 师傅账号
	private List<ListEntity> listImg; // 图片的集合
	private List<DetailOrdersBeen.T> list; // 实体类中的list集合
	public AllSQLite sqlite;
	private RequestQueue requestQueue; // 请求队列
	public ProgersssDialogTools progressDialog; // 加载圈
	
	TextView descriptionTv; // 描述

	Button statusBtn; // 状态
	Button saveOkBtn; // 完成工作按钮
	ImageButton returnBtn; // 返回键
	TextView releaseTimeTv; // 时间
	private TextView buyerNameTv; // 买家姓名
	private TextView buyerPhoneTv; // 买家电话
	private TextView elevatorTv; // 电梯
	private TextView column4Tv; // 详细地址	
	private TextView tv_NowTime; //显示框
	private TextView tv_servicePrice; //价格
	private TextView tv_logisiticsAddress; //取货地址
	private TextView tv_logisiticsPhone; //取货电话
	
	private ListView listView;
	private ArrayList<HashMap<String, String>> list1; //适配器用到的集合
	private OrdersDetailsAdapter adaper; //适配器

	String orderEndTime = ""; // 结束时间
	String status = ""; // 接单状态值
	String orderId; //
	String result; // 返回值
	HashMap<String, String> mapAdd;
	EditText et_nowTime; // 编辑框
	boolean isFlagNum=false; //判断第几次请求数据
	private Map fileMap = new HashMap<String,File>(); //图片路径
	private int num=0;//添加图片的id
	/* 用来标识请求照相功能的activity */
	private final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private final int PHOTO_PICKED_WITH_DATA = 3021;
	private final int PIC_VIEW_CODE = 2016; // GridView预览删除页面过来
	/* 拍照的照片存储位置 */
	private final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()+ "/Android/data/com.photo.choosephotos");
	private File mCurrentPhotoFile;// 照相机拍照得到的图片
	private ArrayList<Bitmap> microBmList = new ArrayList<Bitmap>(); // 用来显示预览图
	private ArrayList<Item> photoList = new ArrayList<Item>(); // 所选图的信息(主要是路径)
	private AddImageGridAdapter imgAdapter;
	private Bitmap addNewPic;
	private GridView gridView;//显示所有上传图片
	private SelectPicPopupWindow menuWindow;
	public MyDialog dialogStart;//开始工作弹出框
	public MyDialog dialogEnd; //结束工作弹出框
    private boolean workerBtnFlag=true; //判断开始、结束 工作	
    private boolean flag = true; //弹出框的顺序
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_reserve_order);
		init();
		initAssignment();
		initPhoto();
	}

	/**
	 * 初始化获取照片
	 */
	private void initPhoto() {
		if(!(PHOTO_DIR.exists() && PHOTO_DIR.isDirectory())){
			PHOTO_DIR.mkdirs();
		}	
	    gridView=(GridView)findViewById(R.id.allPic); //添加图片
        addNewPic = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_pic); //加号图片
        //addNewPic = PictureManageUtil.resizeBitmap(addNewPic, 180, 180);
        
        microBmList.add(addNewPic);
        imgAdapter = new AddImageGridAdapter(this,microBmList);
        gridView.setAdapter(imgAdapter);
        //事件监听，点击GridView里的图片时，在ImageView里显示出来
      	gridView.setOnItemClickListener(new OnItemClickListener() {
      		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
      		{
      			if(position==(photoList.size())){ //这里是加号按钮的事件
      				if(photoList.size()>2){      					
      					Toast.makeText(SaveReserveOrderActivity.this, "最多只能选择3张！", Toast.LENGTH_LONG).show();
      				}else{      					
      					Log.e("点击：", "点击");
      					menuWindow = new SelectPicPopupWindow(SaveReserveOrderActivity.this, itemsOnClick);
      					menuWindow.showAtLocation(SaveReserveOrderActivity.this.findViewById(R.id.uploadPictureLayout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
      				}
      			}
      			else{
      				Intent intent = new Intent(SaveReserveOrderActivity.this, ViewPagerDeleteActivity.class);
      				intent.putParcelableArrayListExtra("files", photoList);
      				intent.putExtra(ViewPagerActivity.CURRENT_INDEX, position);      			
      				startActivityForResult(intent, PIC_VIEW_CODE);
      			}
      		}
      	});
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		descriptionTv = (TextView) findViewById(R.id.details_describe);
		statusBtn = (Button) findViewById(R.id.btn_status);
		returnBtn = (ImageButton) findViewById(R.id.Imagebtn_returnbtn);
		tv_NowTime = (TextView) findViewById(R.id.tv_details_now_time);
		buyerNameTv = (TextView) findViewById(R.id.details_name);
		buyerPhoneTv = (TextView) findViewById(R.id.details_phone);
		elevatorTv = (TextView) findViewById(R.id.details_elevator);
		column4Tv = (TextView) findViewById(R.id.save_details_column4);
		tv_servicePrice = (TextView) findViewById(R.id.tv_save_orderPrice);
		tv_logisiticsAddress = (TextView) findViewById(R.id.save_details_logisiticsAddress);
		tv_logisiticsPhone = (TextView) findViewById(R.id.save_details_logisiticsPhone);
		releaseTimeTv = (TextView) findViewById(R.id.details_time);
		et_nowTime = (EditText) findViewById(R.id.et_details_now_time);
		saveOkBtn = (Button) findViewById(R.id.btn_save_ok);	
		list1 = new ArrayList<HashMap<String,String>>();
		listView = (ListView) findViewById(R.id.lv_save_details_goods);

		mapAdd = new HashMap<String, String>();
		statusBtn.setOnClickListener(this);
		returnBtn.setOnClickListener(this);
		saveOkBtn.setOnClickListener(this);	
		list = new ArrayList<DetailOrdersBeen.T>();
		listImg = new ArrayList<ListEntity>();
		requestQueue = Volley.newRequestQueue(this);
		sqlite = new AllSQLite(this);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static String GetStringFromLong(long millis)
	{
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
	java.util.Date dt = new Date(millis); 
	return sdf.format(dt); 
	}

	/**
	 * 给参数初始化赋值
	 */
	private void initAssignment() {
		progressDialog = new ProgersssDialogTools(SaveReserveOrderActivity.this);
		Intent intent = getIntent();		
		orderId = intent.getStringExtra("orderId");		
		mapAdd.clear();
		mapAdd.put("orderId", orderId);
		getDateInit(LinkUrlStatic.orderDetailsUrl, mapAdd);
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.Imagebtn_returnbtn:
			finish();
			break;
		case R.id.btn_status:
			if(workerBtnFlag == true){
				dialogStart = new MyDialog(SaveReserveOrderActivity.this, R.style.MyDialog, "确定开始工作吗？", "确定", "取消",
						SaveReserveOrderActivity.this);
				dialogStart.show();
				workerBtnFlag = false;
			}else {		
				dialogStart = new MyDialog(SaveReserveOrderActivity.this, R.style.MyDialog, "确定结束工作吗？", "确定", "取消",
						SaveReserveOrderActivity.this);
				dialogStart.show();
				
				workerBtnFlag = true;
			}
			break;
			
		case R.id.btn_save_ok: //提货问题按钮
			
			for(int i =0;i<photoList.size();i++){ //图片路径集合
				fileMap.put("file"+i, photoList.get(i).getPhotoPath());
			}
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日  HH:mm:ss");       
			Date curDate = new  Date(System.currentTimeMillis());//获取当前时间       
			String str = formatter.format(curDate); 
			mapAdd.clear();
			mapAdd.put("orderId", orderId);
			mapAdd.put("workerId", MySharedPerferences.sharedPreferences.getString("workerId", ""));
			mapAdd.put("column2", str);	
			mapAdd.put("imgDecribtion", "");
			progressDialog = new ProgersssDialogTools(SaveReserveOrderActivity.this);
	
			new Thread(new Runnable() {
				@Override
				public void run() {
					String m = HttpPostUploadUtil.formUpload(LinkUrlStatic.saveMentioningUrl, mapAdd, fileMap);
					try {
						JSONObject object = new JSONObject(m);
						String status = object.getString("status");
						Message message=new Message();
						message.obj = status;
						Log.e("22222222222222222222222222:", status);
						handler.sendMessage(message);						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
	
		}
	}

	/**
	 * 开始工资的 Volley 和 Gson第三方获取数据并解析
	 * 
	 * @param path
	 *            请求地址
	 */
	public void getDateStart(String path, final HashMap<String, String> map) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				path, new Listener<String>() {
			@Override
			public void onResponse(String result) {
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getString("status");
					if (status.equals("0")) {
						progressDialog._dismiss();
						statusBtn.setText("结束工作");	
						gridView.setVisibility(View.VISIBLE);
						saveOkBtn.setVisibility(View.INVISIBLE);
						statusBtn.setBackgroundResource(R.color.theme_color);
						statusBtn.setClickable(true);
					} else {
						Toast.makeText(SaveReserveOrderActivity.this,
								"服务器异常！", Toast.LENGTH_LONG).show();
					}
					
					Log.e("status:", status + "");
				} catch (JSONException e) {
					Toast.makeText(SaveReserveOrderActivity.this,
							"服务器异常！", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
			}
			
		}, new ErrorListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onErrorResponse(VolleyError err) {
				Toast.makeText(SaveReserveOrderActivity.this,
						"服务器请求异常！", Toast.LENGTH_LONG);
				Log.e("volley错误。", err.getMessage(), err);
			}
		}) {
			@Override
			protected HashMap<String, String> getParams() {
				// 在这里设置需要post的参数
				
				return map;
			}
		};
		requestQueue.add(stringRequest);
	}
	
	//为弹出窗口实现监听类
    private View.OnClickListener  itemsOnClick = new View.OnClickListener(){
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:{
				String status=Environment.getExternalStorageState();
				if(status.equals(Environment.MEDIA_MOUNTED)){
					//判断是否有SD卡
					doTakePhoto();// 用户点击了从照相机获取 
				}
				else{
					Toast.makeText(SaveReserveOrderActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case R.id.btn_pick_photo:{
				//打开选择图片界面
				doPickPhotoFromGallery();
				break;
			}
			}						
		}  	
    };
    
    
    /**
   	 * 拍照获取图片
   	 * 
   	 */
   	protected void doTakePhoto() {
   		try {
   			// 创建照片的存储目录
   			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
   			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
//   			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
   			startActivityForResult(intent, CAMERA_WITH_DATA);
   		} catch (ActivityNotFoundException e) {
   			Toast.makeText(this, "找不到相机", Toast.LENGTH_LONG).show();
   		}
   	}
   	public String getPhotoFileName() {		
   		UUID uuid = UUID.randomUUID();		
   		return uuid + ".jpg";
   	}
   	public static Intent getTakePickIntent(File f) {
   		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
   		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
   		return intent;
   	}
   	// 请求Gallery程序
   	protected void doPickPhotoFromGallery() {
   		try {
   			progressDialog = new ProgersssDialogTools(SaveReserveOrderActivity.this);
   			progressDialog.setCancelable(true);
   			new Handler().postDelayed(new Runnable() {
   				public void run() {
   					//初始化提示框
   					progressDialog._dismiss();
   				}

   			}, 1000);	    
   			final Intent intent = new Intent(SaveReserveOrderActivity.this,PhotoAlbumActivity.class);
   			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
   		} catch (ActivityNotFoundException e) {
   			Toast.makeText(this, "图库中找不到照片", Toast.LENGTH_LONG).show();
   		}
   	}
   	
   	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){   		
   		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
   			progressDialog._dismiss();
   			Log.e("返回值", msg.obj+"");
   			if(msg.obj.equals("0")){
   				saveOkBtn.setVisibility(View.INVISIBLE);
   				gridView.setVisibility(View.INVISIBLE);   				
   			}   		
   			else if(msg.obj.equals("3")){   				
   				finish();
   				startActivity(new Intent(SaveReserveOrderActivity.this,MainActivity.class));
   			}
   			else if(msg.obj.equals("2")){
   				Toast.makeText(SaveReserveOrderActivity.this,"服务器异常！", Toast.LENGTH_LONG).show();
   			}
  
   		};
   	};
   	@SuppressLint("HandlerLeak")
	Handler handler1 = new Handler(){   		
   		@SuppressLint("ShowToast")
   		public void handleMessage(Message msg) {
  
   			switch(msg.what){
   			case 1 :
   				imgAdapter.notifyDataSetChanged();
   				break;
   			}
   		};
   	};
   	
   	/**
   	 * 处理其他页面返回数据
   	 */
   	@SuppressWarnings("unused")
	@Override
   	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   		if (resultCode != RESULT_OK)
   			return;
   		switch (requestCode) {
   		case PHOTO_PICKED_WITH_DATA: {
   			// 调用Gallery返回的
   			ArrayList<Item> tempFiles = new ArrayList<Item>();
   			if(data==null)
   				return;
   		
   			tempFiles = data.getParcelableArrayListExtra("fileNames");
   			Log.e("test","被选中的照片"+tempFiles.toString());
   			Log.e("tempFiles:",tempFiles.size()+"");
   			if(tempFiles == null){
   				return;
   			}
   			microBmList.remove(addNewPic);
   			Bitmap bitmap;
   			for (int i = 0; i < tempFiles.size(); i++) {
   				bitmap = MediaStore.Images.Thumbnails.getThumbnail(this.getContentResolver(),  tempFiles.get(i).getPhotoID(), Thumbnails.MICRO_KIND, null);
   				int rotate = PictureManageUtil.getCameraPhotoOrientation(tempFiles.get(i).getPhotoPath());
   				bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
   				microBmList.add(bitmap);
   				
   				photoList.add(tempFiles.get(i));
   			}
   			microBmList.add(addNewPic);
   			imgAdapter.notifyDataSetChanged();
   			break;
   		}
   		case CAMERA_WITH_DATA: {
   			Long delayMillis = 0L;
   			if(mCurrentPhotoFile==null){
   				delayMillis = 500L;
   			}
   			handler1.postDelayed(new Runnable(){
   				@Override
   				public void run() {
   					// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
   					//去掉GridView里的加号
   					microBmList.remove(addNewPic);
   					Item item = new Item();
   					
   					item.setPhotoPath(mCurrentPhotoFile.getAbsolutePath());
   					
   					photoList.add(item);
   					//根据路径，得到一个压缩过的Bitmap（宽高较大的变成500，按比例压缩）
   					Bitmap bitmap = PictureManageUtil.getCompressBm(mCurrentPhotoFile.getAbsolutePath());
   					//获取旋转参数
   					int rotate = PictureManageUtil.getCameraPhotoOrientation(mCurrentPhotoFile.getAbsolutePath());
   					//把压缩的图片进行旋转
   					bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
   					microBmList.add(bitmap);
   					microBmList.add(addNewPic);
   					Message msg = handler1.obtainMessage(1);
   					msg.sendToTarget();
   				}
   			}, delayMillis);
   			break;
   		}
   		case PIC_VIEW_CODE: {
   			ArrayList<Integer> deleteIndex = data.getIntegerArrayListExtra("deleteIndexs");
   			if(deleteIndex.size()>0){
   				for(int i=deleteIndex.size()-1;i>=0;i--){
   					microBmList.remove((int)deleteIndex.get(i));
   					photoList.remove((int)deleteIndex.get(i));
   				}
   			}
   			imgAdapter.notifyDataSetChanged();
   			break;
   		}
   		}
   	}

   	/**
   	 * 开始工作
   	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onLeftBtnClick(Dialog dialog) {
		
		Log.e("1212121:",dialog+"");
		if(flag == true){		
			flag = false;
			SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日  HH:mm:ss");       
			Date curDate = new  Date(System.currentTimeMillis());//获取当前时间       
			String str = formatter.format(curDate);
			progressDialog = new ProgersssDialogTools(SaveReserveOrderActivity.this);
			mapAdd.clear();
			mapAdd.put("orderId", orderId);
			mapAdd.put("column2", str);		
			getDateStart(LinkUrlStatic.saveWorkTimeUrl, mapAdd);
			statusBtn.setClickable(false);
			dialog.dismiss();
		}else if(flag == false){
			dialog.dismiss();
			Log.e("结束工作：", "结束工作：");
			saveEndWork();
		}
	}

	@Override
	public void onRightBtnClick(Dialog dialog) {
		dialog.dismiss();
	}
	
   /**
    * 结束工作
    */
	@SuppressWarnings("unchecked")
	@SuppressLint("SimpleDateFormat")
	private void saveEndWork() {	
		for(int i =0;i<photoList.size();i++){ //图片路径集合
			Log.e("photoList "+i+":",photoList.get(i).getPhotoPath());	
			
			fileMap.put("file"+i, photoList.get(i).getPhotoPath());
		}
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日  HH:mm:ss");       
		Date curDate = new  Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate); 
		mapAdd.clear();
		mapAdd.put("orderId", orderId);
		mapAdd.put("workerId", MySharedPerferences.sharedPreferences.getString("workerId", ""));
		mapAdd.put("orderEndTime", str);	
		progressDialog = new ProgersssDialogTools(SaveReserveOrderActivity.this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String m = HttpPostUploadUtil.formUpload(LinkUrlStatic.saveEndWorkTimeUrl, mapAdd, fileMap);
				try {
					JSONObject object = new JSONObject(m);
					String status = object.getString("status");
					if(status.equals("0")){
						status = "3";
					}
					Log.e("status:", status);
					Message message=new Message();
					message.obj = status;					
					handler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	/**
	 * 初始化的时候的Volley 和 Gson第三方获取数据并解析
	 * 
	 * @param path
	 *            请求地址
	 */
	public void getDateInit(String path, final HashMap<String, String> map) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST,
				path, new Listener<String>() {
			@SuppressLint("NewApi")
			@Override
			public void onResponse(String result) {
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getString("status");
					JSONObject object0 = object.getJSONObject("object");					
					String orderCreateTime = object0.getString("orderCreateTime");
					String column3 = object0.getString("column3");
					String orderId = object0.getString("orderId");
					String column1 = object0.getString("column1");
					String column2 = object0.getString("column2");
					String orderState = object0.getString("orderState");
					String servicePrice = object0.getString("servicePrice");
					String serviceTime = object0.getString("serviceTime");
					String column4 = object0.getString("column4");
					orderEndTime = object0.getString("orderEndTime");
					String buyerName = object0.getString("buyerName");
					String buyerPhone = object0.getString("buyerPhone");
					String elevator = object0.getString("elevator");
					Log.e("column2column2column2:", column2+"");
					if(!column2.equals("")){
						flag = false;					
						statusBtn.setText("结束工作");	
						saveOkBtn.setVisibility(View.GONE);
						statusBtn.setBackgroundResource(R.color.theme_color);
					}
			
					if("".equals(servicePrice)||servicePrice==null)
						tv_servicePrice.setText("¥0");
					else
						tv_servicePrice.setText("¥"+servicePrice);
					
					String a = GetStringFromLong(Long.valueOf(orderCreateTime));
					tv_NowTime.setText(serviceTime);
					descriptionTv.setText(column1 );				
					releaseTimeTv.setText(a);
					buyerNameTv.setText(buyerName);
					buyerPhoneTv.setText(buyerPhone);
					elevatorTv.setText(elevator);
					if(column4.equals("null"))
						column4Tv.setText("无");
					else
						column4Tv.setText(column4);
					
					Log.e("getDateInit-----list:", status+"");
					JSONArray goods = object.getJSONArray("goods");
					JSONArray logistics = object.getJSONArray("logistics");
					if(logistics.length()>0){
								JSONObject object3 = logistics.getJSONObject(0);
								String logisiticsPhone = object3.getString("logisiticsPhone");
								String logisiticsAddress = object3.getString("logisiticsAddress");
								tv_logisiticsPhone.setText(logisiticsPhone+"");
								tv_logisiticsAddress.setText(logisiticsAddress+"");
					}
					HashMap<String,String> map;
					String goodsName="";
					for (int i = 0; i < goods.length(); i++) {
						map = new HashMap<String,String>();
						JSONObject object2 = goods.getJSONObject(i);
						goodsName = object2.getString("goodsName");
						map.put("goodsName", goodsName);						
						list1.add(map);
					}
					if(goods.length()>0){	
						Log.e("goods.length():", goods.length()+"");
						adapterOk();
					}
					progressDialog._dismiss();
				
				} catch (JSONException e) {
					Toast.makeText(SaveReserveOrderActivity.this, "服务器异常！",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
			}

		
		}, new ErrorListener() {
			
			@SuppressLint("ShowToast")
			@Override
			public void onErrorResponse(VolleyError err) {
				Toast.makeText(SaveReserveOrderActivity.this, "服务器请求异常！",
						Toast.LENGTH_LONG);
				Log.e("volley错误。", err.getMessage(), err);
			}
		}) {
			@Override
			protected HashMap<String, String> getParams() {
				// 在这里设置需要post的参数
				
				return map;
			}
		};
		requestQueue.add(stringRequest);
	}
	
	/**
	 * 适配器
	 */
	private void adapterOk() {
		Log.e("适配器中的 adapterOk  list1:", list1.size()+"");
		adaper = new OrdersDetailsAdapter(list1, SaveReserveOrderActivity.this);
	//	listView.setVisibility(View.VISIBLE);
		listView.setAdapter(adaper);
	}

}
