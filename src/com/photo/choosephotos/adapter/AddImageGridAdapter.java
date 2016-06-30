package com.photo.choosephotos.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zshifu.R;

public class AddImageGridAdapter extends BaseAdapter
{
	// ����Context
	private Context context;
	//ͼƬ��ַ
	private List<Bitmap> imageList = new ArrayList<Bitmap>();

	public AddImageGridAdapter(Context context)
	{
		this.context = context;
	}

	public AddImageGridAdapter(Context context,List<Bitmap> imageList)
	{
		this.context = context;
		this.imageList=imageList;
	}
	
	// ��ȡͼƬ�ĸ���
	public int getCount()
	{
		return imageList.size();
	}

	// ��ȡͼƬ�ڿ��е�λ��
	public Object getItem(int position)
	{
		return position;
	}

	// ��ȡͼƬID
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.image_add_grid_item, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.img_view);
        imageView.setImageBitmap(imageList.get(position));
        return view;
	}

	public List<Bitmap> getImageList(){
		return imageList;
	}
	
}