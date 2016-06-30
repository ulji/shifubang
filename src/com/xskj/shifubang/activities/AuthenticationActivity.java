package com.xskj.shifubang.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.zshifu.R;
import com.photo.choosephotos.adapter.AddImageGridAdapter;
import com.photo.choosephotos.controller.SelectPicPopupWindow;
import com.photo.choosephotos.photo.Item;
import com.photo.choosephotos.photo.PhotoAlbumActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerActivity;
import com.photo.choosephotos.photoviewer.photoviewerinterface.ViewPagerDeleteActivity;
import com.photo.choosephotos.util.PictureManageUtil;
import com.xskj.shifubang.db.MySharedPerferences;
import com.xskj.shifubang.statics.LinkUrlStatic;
import com.xskj.shifubang.tools.HttpPostUploadUtil;
import com.xskj.shifubang.tools.Utils;
import com.xskj.shifubang.view.ProgersssDialogTools;

public class AuthenticationActivity extends Activity implements OnClickListener{
	
	private Map fileMap = new HashMap<String,File>(); //图片路径
	private int num=0;//添加图片的id
	/* 用来标识请求照相功能的activity */
	private final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private final int PHOTO_PICKED_WITH_DATA = 3021;
	private final int PIC_VIEW_CODE = 2016; // GridView预览删除页面过来
	/* 拍照的照片存储位置 */
	private final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()
					+ "/Android/data/com.photo.choosephotos");
	private File mCurrentPhotoFile;// 照相机拍照得到的图片
	private ArrayList<Bitmap> microBmList = new ArrayList<Bitmap>(); // 用来显示预览图
	private ArrayList<Item> photoList = new ArrayList<Item>(); // 所选图的信息(主要是路径)
	private AddImageGridAdapter imgAdapter;
	private Bitmap addNewPic;
	private GridView gridView;//显示所有上传图片
	private SelectPicPopupWindow menuWindow;
	public ProgersssDialogTools progressDialog; // 加载圈
	private EditText NameEt; //姓名编辑
	private EditText idNameEt; //身份证编辑
	private Button authenticationBtn; //人工审核按钮
	private ImageView returnBtn; //人工审核按钮
	HashMap<String,String> mapAdd; //添加参数的map
	
	
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
   	 * 请求审核之后获取到的数据
   	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){   		
   		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
   			progressDialog._dismiss();
   			Log.e("收到的数据:",msg.obj+"");
   			if(msg.obj.equals("0")){   				
   				finish();
   				startActivity(new Intent(AuthenticationActivity.this,LoginActivity.class));
   			}
   			else if(msg.obj.equals("2")){
   				Toast.makeText(AuthenticationActivity.this,"服务器异常！", Toast.LENGTH_LONG).show();
   			}
   		};
   	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);
		init();		
		initPhoto();
	}
	
	private void init() {
		NameEt = (EditText) findViewById(R.id.et_authentication_name);		
		idNameEt = (EditText) findViewById(R.id.et_id_name);
		authenticationBtn = (Button) findViewById(R.id.btn_authentication);
		returnBtn = (ImageView) findViewById(R.id.authentication_return);
		
		mapAdd = new HashMap<String, String>();
		authenticationBtn.setOnClickListener(this);
		returnBtn.setOnClickListener(this);
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
      					Toast.makeText(AuthenticationActivity.this, "最多只能选择3张！", Toast.LENGTH_LONG).show();
      				}else{      					
      					Log.e("点击：", "点击");
      					menuWindow = new SelectPicPopupWindow(AuthenticationActivity.this, itemsOnClick);
      					menuWindow.showAtLocation(AuthenticationActivity.this.findViewById(R.id.uploadPictureLayout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
      				}
      			}
      			else{
      				Intent intent = new Intent(AuthenticationActivity.this, ViewPagerDeleteActivity.class);
      				intent.putParcelableArrayListExtra("files", photoList);
      				intent.putExtra(ViewPagerActivity.CURRENT_INDEX, position);      			
      				startActivityForResult(intent, PIC_VIEW_CODE);
      			}
      		}
      	});
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
					Toast.makeText(AuthenticationActivity.this, "没有SD卡", Toast.LENGTH_LONG).show();
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
   			final ProgressDialog dialog;
   			dialog=new ProgressDialog(this);
   			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //设置为圆形
   			dialog.setMessage("数据加载中...");
   			dialog.setIndeterminate(false);//
   			dialog.show();
   			Window window = dialog.getWindow();     
   			View view = window.getDecorView();     
   			new Handler().postDelayed(new Runnable() {
   				public void run() {
   					//初始化提示框
   					dialog.dismiss();
   				}

   			}, 2000);	    
   			final Intent intent = new Intent(AuthenticationActivity.this,PhotoAlbumActivity.class);
   			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
   		} catch (ActivityNotFoundException e) {
   			Toast.makeText(this, "图库中找不到照片", Toast.LENGTH_LONG).show();
   		}
   	}
   	
   	

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

	@Override
	public void onClick(View v) {
       switch (v.getId()) {
	case R.id.btn_authentication:
	
		if (isVerification()==true){
			Log.e("验证通过:","验证通过"+"");
			progressDialog = new ProgersssDialogTools(AuthenticationActivity.this);
			authenticationBtn.setClickable(false);
		mapAdd.clear();
		mapAdd.put("realName", NameEt.getText().toString());	
		mapAdd.put("idCard", idNameEt.getText().toString());
		mapAdd.put("workerId", MySharedPerferences.sharedPreferences.getString("workerId", ""));
		Log.e("id:", NameEt.getText().toString()+" , "+idNameEt.getText().toString()+" , "+ MySharedPerferences.sharedPreferences.getString("workerId", ""));
		new Thread(new Runnable() {
			@Override
			public void run() {
				@SuppressWarnings("unchecked")
				String m = HttpPostUploadUtil.formUpload(LinkUrlStatic.workerAccreditaion, mapAdd, fileMap);
				try {
					JSONObject object = new JSONObject(m);
					String status = object.getString("status");
					Message message=new Message();
					message.obj = status;	
					Log.e("status:", status);
					handler.sendMessage(message);
				} catch (JSONException e) {
					progressDialog._dismiss();
					e.printStackTrace();
				}
			}
		}).start();
		}
		break;
	case R.id.authentication_return:
		finish();
		break;

	  }		
	}
	
	/**
	 * 审核之前的数据验证
	 * @return 如果是true验证通过  ， 如果是false验证未通过 
	 */
	@SuppressLint("ShowToast")
	@SuppressWarnings("unchecked")
	private boolean isVerification() {		
		for(int i =0;i<photoList.size();i++){ //图片路径集合
			Log.e("photoList "+i+":",photoList.get(i).getPhotoPath());	
			fileMap.put("file"+i, photoList.get(i).getPhotoPath());
		}
		
		if(NameEt.getText().toString().equals("")) {			
			Toast.makeText(AuthenticationActivity.this, "请填写真实姓名选项！", 0).show();
			return false;
		}
		else if(idNameEt.getText().toString().equals("")) {			
			Toast.makeText(AuthenticationActivity.this, "身份证号不能为空！", 0).show();
			return false;
		}
		else if(!Utils.personIdValidation(idNameEt.getText().toString())){			
			Toast.makeText(AuthenticationActivity.this, "身份证号格式不正确！", 0).show();	
			return false;
		}
		else if(photoList.size()!=3){			
			Toast.makeText(AuthenticationActivity.this, "请选择3张图片！", 0).show();
			return false;
		}else{			
			return true;
		}
		
	}


}
