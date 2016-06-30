package com.photo.choosephotos.photo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.zshifu.R;
import com.photo.choosephotos.ActivityManager;

public class PhotoAlbumActivity extends Activity{
	private GridView albumGV;
	private List<Album> albumList;

	// ���û�ȡͼƬ���ֶ�
	private static final String[] STORE_IMAGES = {
			MediaStore.Images.Media.DISPLAY_NAME, // ��ʾ����
			MediaStore.Images.Media.LATITUDE, // ά��
			MediaStore.Images.Media.LONGITUDE, // ����
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id Ŀ¼
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // dir name Ŀ¼����
			MediaStore.Images.Media.DATA, //·��
			MediaStore.Images.Media.DATE_TAKEN  //
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_album);
		
		//ActivityManager���Լ�д����������Activity���࣬��Ҫ������ҳ��finish
		ActivityManager.addActivity(this, "PhotoAlbumActivity");
		
		albumGV = (GridView) findViewById(R.id.album_gridview);
		albumList = getPhotoAlbum();
		albumGV.setAdapter(new AlbumAdapter(albumList, this));
		albumGV.setOnItemClickListener(albumClickListener);
	}

	/**
	 * ������¼�
	 */
	OnItemClickListener albumClickListener =  new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Intent intent = new Intent(PhotoAlbumActivity.this,
					PhotoActivity.class);
			intent.putExtra("album", albumList.get(position));
			startActivity(intent);
		}
	};
	/**
	 * ����������������ȡͼƬ��Ϣ
	 */
	private List<Album> getPhotoAlbum() {
		List<Album> albumList = new ArrayList<Album>();
		Cursor cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES,
				null, null, MediaStore.Images.Media.DATE_TAKEN + " desc");
		Map<String, Album> countMap = new HashMap<String, Album>();
		Album pa = null;
		while (cursor.moveToNext()) {
			String id = cursor.getString(3);
			String dir_id = cursor.getString(4);
			String dir = cursor.getString(5);
			String path = cursor.getString(6);
			String dateTaken = cursor.getString(7);
			if (!countMap.containsKey(dir_id)) {
				pa = new Album();
				pa.setName(dir);
				pa.setBitmap(Integer.parseInt(id));
				pa.setCount("1");
				pa.getBitList().add(new Item(Integer.valueOf(id), path, dateTaken));
				countMap.put(dir_id, pa);
			} else {
				pa = countMap.get(dir_id);
				pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
				pa.getBitList().add(new Item(Integer.valueOf(id), path, dateTaken));
			}
		}
		cursor.close();
		Iterable<String> it = countMap.keySet();
		for (String key : it) {
			albumList.add(countMap.get(key));
		}
		return albumList;
	}
	
	public void cancel(View v){
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}
}
