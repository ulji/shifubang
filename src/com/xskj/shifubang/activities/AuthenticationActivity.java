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
	
	private Map fileMap = new HashMap<String,File>(); //ͼƬ·��
	private int num=0;//���ͼƬ��id
	/* ������ʶ�������๦�ܵ�activity */
	private final int CAMERA_WITH_DATA = 3023;
	/* ������ʶ����gallery��activity */
	private final int PHOTO_PICKED_WITH_DATA = 3021;
	private final int PIC_VIEW_CODE = 2016; // GridViewԤ��ɾ��ҳ�����
	/* ���յ���Ƭ�洢λ�� */
	private final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()
					+ "/Android/data/com.photo.choosephotos");
	private File mCurrentPhotoFile;// ��������յõ���ͼƬ
	private ArrayList<Bitmap> microBmList = new ArrayList<Bitmap>(); // ������ʾԤ��ͼ
	private ArrayList<Item> photoList = new ArrayList<Item>(); // ��ѡͼ����Ϣ(��Ҫ��·��)
	private AddImageGridAdapter imgAdapter;
	private Bitmap addNewPic;
	private GridView gridView;//��ʾ�����ϴ�ͼƬ
	private SelectPicPopupWindow menuWindow;
	public ProgersssDialogTools progressDialog; // ����Ȧ
	private EditText NameEt; //�����༭
	private EditText idNameEt; //���֤�༭
	private Button authenticationBtn; //�˹���˰�ť
	private ImageView returnBtn; //�˹���˰�ť
	HashMap<String,String> mapAdd; //��Ӳ�����map
	
	
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
   	 * �������֮���ȡ��������
   	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){   		
   		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
   			progressDialog._dismiss();
   			Log.e("�յ�������:",msg.obj+"");
   			if(msg.obj.equals("0")){   				
   				finish();
   				startActivity(new Intent(AuthenticationActivity.this,LoginActivity.class));
   			}
   			else if(msg.obj.equals("2")){
   				Toast.makeText(AuthenticationActivity.this,"�������쳣��", Toast.LENGTH_LONG).show();
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
	 * ��ʼ����ȡ��Ƭ
	 */
	private void initPhoto() {
		if(!(PHOTO_DIR.exists() && PHOTO_DIR.isDirectory())){
			PHOTO_DIR.mkdirs();
		}	
	    gridView=(GridView)findViewById(R.id.allPic); //���ͼƬ
        addNewPic = BitmapFactory.decodeResource(this.getResources(), R.drawable.add_new_pic); //�Ӻ�ͼƬ
        //addNewPic = PictureManageUtil.resizeBitmap(addNewPic, 180, 180);
        microBmList.add(addNewPic);
        imgAdapter = new AddImageGridAdapter(this,microBmList);
        gridView.setAdapter(imgAdapter);
        //�¼����������GridView���ͼƬʱ����ImageView����ʾ����
      	gridView.setOnItemClickListener(new OnItemClickListener() {
      		public void onItemClick(AdapterView<?> parent, View v, int position, long id)
      		{
      			if(position==(photoList.size())){ //�����ǼӺŰ�ť���¼�
      				if(photoList.size()>2){      					
      					Toast.makeText(AuthenticationActivity.this, "���ֻ��ѡ��3�ţ�", Toast.LENGTH_LONG).show();
      				}else{      					
      					Log.e("�����", "���");
      					menuWindow = new SelectPicPopupWindow(AuthenticationActivity.this, itemsOnClick);
      					menuWindow.showAtLocation(AuthenticationActivity.this.findViewById(R.id.uploadPictureLayout), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //����layout��PopupWindow����ʾ��λ��
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
	
	
	
	//Ϊ��������ʵ�ּ�����
    private View.OnClickListener  itemsOnClick = new View.OnClickListener(){
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:{
				String status=Environment.getExternalStorageState();
				if(status.equals(Environment.MEDIA_MOUNTED)){
					//�ж��Ƿ���SD��
					doTakePhoto();// �û�����˴��������ȡ 
				}
				else{
					Toast.makeText(AuthenticationActivity.this, "û��SD��", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case R.id.btn_pick_photo:{
				//��ѡ��ͼƬ����
				doPickPhotoFromGallery();
				break;
			}
			}						
		}  	
    };
    
    
    
    /**
   	 * ���ջ�ȡͼƬ
   	 * 
   	 */
   	protected void doTakePhoto() {
   		try {
   			// ������Ƭ�Ĵ洢Ŀ¼
   			mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// �����յ���Ƭ�ļ�����
   			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
   			startActivityForResult(intent, CAMERA_WITH_DATA);
   		} catch (ActivityNotFoundException e) {
   			Toast.makeText(this, "�Ҳ������", Toast.LENGTH_LONG).show();
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
   	// ����Gallery����
   	protected void doPickPhotoFromGallery() {
   		try {
   			final ProgressDialog dialog;
   			dialog=new ProgressDialog(this);
   			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //����ΪԲ��
   			dialog.setMessage("���ݼ�����...");
   			dialog.setIndeterminate(false);//
   			dialog.show();
   			Window window = dialog.getWindow();     
   			View view = window.getDecorView();     
   			new Handler().postDelayed(new Runnable() {
   				public void run() {
   					//��ʼ����ʾ��
   					dialog.dismiss();
   				}

   			}, 2000);	    
   			final Intent intent = new Intent(AuthenticationActivity.this,PhotoAlbumActivity.class);
   			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
   		} catch (ActivityNotFoundException e) {
   			Toast.makeText(this, "ͼ�����Ҳ�����Ƭ", Toast.LENGTH_LONG).show();
   		}
   	}
   	
   	

   	/**
   	 * ��������ҳ�淵������
   	 */
   	@SuppressWarnings("unused")
	@Override
   	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   		if (resultCode != RESULT_OK)
   			return;
   		switch (requestCode) {
   		case PHOTO_PICKED_WITH_DATA: {
   			// ����Gallery���ص�
   			ArrayList<Item> tempFiles = new ArrayList<Item>();
   			if(data==null)
   				return;
   			tempFiles = data.getParcelableArrayListExtra("fileNames");
   			Log.e("test","��ѡ�е���Ƭ"+tempFiles.toString());
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
   					// ��������򷵻ص�,�ٴε���ͼƬ��������ȥ�޼�ͼƬ
   					//ȥ��GridView��ļӺ�
   					microBmList.remove(addNewPic);
   					Item item = new Item();
   					item.setPhotoPath(mCurrentPhotoFile.getAbsolutePath());
   					photoList.add(item);
   					//����·�����õ�һ��ѹ������Bitmap����߽ϴ�ı��500��������ѹ����
   					Bitmap bitmap = PictureManageUtil.getCompressBm(mCurrentPhotoFile.getAbsolutePath());
   					//��ȡ��ת����
   					int rotate = PictureManageUtil.getCameraPhotoOrientation(mCurrentPhotoFile.getAbsolutePath());
   					//��ѹ����ͼƬ������ת
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
			Log.e("��֤ͨ��:","��֤ͨ��"+"");
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
	 * ���֮ǰ��������֤
	 * @return �����true��֤ͨ��  �� �����false��֤δͨ�� 
	 */
	@SuppressLint("ShowToast")
	@SuppressWarnings("unchecked")
	private boolean isVerification() {		
		for(int i =0;i<photoList.size();i++){ //ͼƬ·������
			Log.e("photoList "+i+":",photoList.get(i).getPhotoPath());	
			fileMap.put("file"+i, photoList.get(i).getPhotoPath());
		}
		
		if(NameEt.getText().toString().equals("")) {			
			Toast.makeText(AuthenticationActivity.this, "����д��ʵ����ѡ�", 0).show();
			return false;
		}
		else if(idNameEt.getText().toString().equals("")) {			
			Toast.makeText(AuthenticationActivity.this, "���֤�Ų���Ϊ�գ�", 0).show();
			return false;
		}
		else if(!Utils.personIdValidation(idNameEt.getText().toString())){			
			Toast.makeText(AuthenticationActivity.this, "���֤�Ÿ�ʽ����ȷ��", 0).show();	
			return false;
		}
		else if(photoList.size()!=3){			
			Toast.makeText(AuthenticationActivity.this, "��ѡ��3��ͼƬ��", 0).show();
			return false;
		}else{			
			return true;
		}
		
	}


}
