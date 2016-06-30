/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.photo.choosephotos.photoviewer.photoviewerinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.zshifu.R;
import com.photo.choosephotos.photo.Item;
import com.photo.choosephotos.photoviewer.photoviewlibs.HackyViewPager;
import com.photo.choosephotos.photoviewer.photoviewlibs.PhotoView;
import com.photo.choosephotos.util.PictureManageUtil;

public class ViewPagerDeleteActivity extends Activity {

	public static final String FILES = "files";
	public static final String CURRENT_INDEX = "currentIndex";
	
	public ArrayList<Item> files = new ArrayList<Item>();
	public int index;
	private Button btnDelete;
	private ViewPager mViewPager;
	private ArrayList<Integer> deleteIndexs = new ArrayList<Integer>();
	private ArrayList<Integer> remainIndexs = new ArrayList<Integer>();
	private static int iniSize = 0;
	
	private Map<String, BitmapDrawable> cacheBitmap = new HashMap<String, BitmapDrawable>();
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_delete);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		
//		this.files = this.getIntent().getStringArrayExtra(FILES);
		this.files = this.getIntent().getParcelableArrayListExtra(FILES);
		this.index = this.getIntent().getIntExtra(CURRENT_INDEX, 0);
		iniSize = files.size();
		for (int i = 0; i < iniSize; i++) {
			remainIndexs.add(i);
		}
		
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				remainToDelete();
				intent.putIntegerArrayListExtra("deleteIndexs", deleteIndexs);
				intent.putParcelableArrayListExtra("files", files);
				setResult(RESULT_OK, intent);
				ViewPagerDeleteActivity.this.finish();
			}
		});
		btnDelete = (Button)findViewById(R.id.btnCheck);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = mViewPager.getCurrentItem();
				if(files.size()>1){
					files.remove(position);
					remainIndexs.remove(position);
				}else{
					files.remove(0);
					remainIndexs.remove(0);
				}
				mViewPager.setAdapter(new SamplePagerAdapter());
				if(files.size()>0){
					if(position == files.size()){
						mViewPager.setCurrentItem(position-1);
					}else{
						mViewPager.setCurrentItem(position);
					}
				}else{
					Intent intent = new Intent();
					remainToDelete();
					intent.putIntegerArrayListExtra("deleteIndexs", deleteIndexs);
					setResult(RESULT_OK, intent);
					ViewPagerDeleteActivity.this.finish();
				}
			}
		});
		SamplePagerAdapter spAdapter = new SamplePagerAdapter();
		mViewPager.setAdapter(spAdapter);
		mViewPager.setCurrentItem(index);
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return files.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			BitmapDrawable bmDrawable;
			if(cacheBitmap.get(files.get(position).getPhotoPath())==null){
				//����·�����õ�һ��ѹ������Bitmap����߽ϴ�ı��500��������ѹ����
				Bitmap bitmap = PictureManageUtil.getCompressBm(files.get(position).getPhotoPath());
				//��ȡ��ת����
				int rotate = PictureManageUtil.getCameraPhotoOrientation(files.get(position).getPhotoPath());
				//��ѹ����ͼƬ������ת
				bitmap = PictureManageUtil.rotateBitmap(bitmap, rotate);
				bmDrawable = new BitmapDrawable(ViewPagerDeleteActivity.this.getResources(), bitmap);
				cacheBitmap.put(files.get(position).getPhotoPath(), bmDrawable);
				photoView.setImageDrawable(bmDrawable);
			}else{
				photoView.setImageDrawable(cacheBitmap.get(files.get(position).getPhotoPath()));
			}

			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	public void remainToDelete(){
		for(int i=0;i<iniSize;i++){
			boolean flag = false;
			if(remainIndexs.size()>0){
				for(int j=0;j<remainIndexs.size();j++){
					if(i==remainIndexs.get(j)){
						flag=true;
					}
				}
				if(flag==false){
					deleteIndexs.add(i);
				}
			}else{
				deleteIndexs.add(i);
			}
		}
	}
}
