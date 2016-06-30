package com.xskj.shifubang.view;

import com.zshifu.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 功能：保存图片，动态添加图片
 * @author 赵爱民
 * 时间：2016-5-31
 */


public class SaveImageWordWrapView  extends ViewGroup {
	  private static final int PADDING_HOR = 10;//水平方向padding
	  private static final int PADDING_VERTICAL = 5;//垂直方向padding
	  private static final int SIDE_MARGIN = 10;//左右间距
	  private static final int TEXT_MARGIN = 10;
	  /**
	   * @param context
	   */
	  public SaveImageWordWrapView(Context context) {
	    super(context);
	  }
	  
	  /**
	   * @param context
	   * @param attrs
	   * @param defStyle
	   */
	  public SaveImageWordWrapView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	  }



	  /**
	   * @param context
	   * @param attrs
	   */
	  public SaveImageWordWrapView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	  }



	  @Override
	  protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    int childCount = getChildCount();
	    int x = SIDE_MARGIN;// 横坐标开始
	    int y = 0;//纵坐标开始
	    for(int i=0;i<childCount;i++){
	      View view = getChildAt(i);
	      view.setBackgroundResource(R.color.transparent);
	      int width = 100;
	      int height = 120;
/*	      int width = view.getMeasuredWidth();
	      int height = view.getMeasuredHeight();
*/	      x += width+TEXT_MARGIN;
	//      if(i==0){
	//        view.layout(x-width-TEXT_MARGIN, y-height, x-TEXT_MARGIN, y);
	  //    }else{
	        view.layout(width, height, x, y);
	 //     }
	    }
	  };

	  

	@Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	//    int x = 0;//横坐标
	//    int y = 0;//纵坐标
	  //  int rows = 1;//总行数
	  //  int specWidth = MeasureSpec.getSize(widthMeasureSpec);
	//    int actualWidth = 100 - SIDE_MARGIN * 2;//实际宽度
	 //   int childCount = getChildCount();
	  /*  for(int index = 0;index<childCount;index++){
	      View child = getChildAt(index);
	      child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR, PADDING_VERTICAL);
	      child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	      int width = child.getMeasuredWidth();
	      int height = child.getMeasuredHeight();*/
	  //    x += 100+TEXT_MARGIN;
	  //    y = 120+TEXT_MARGIN;
	  //  }
	    setMeasuredDimension(100, 120);
	  }

	}
