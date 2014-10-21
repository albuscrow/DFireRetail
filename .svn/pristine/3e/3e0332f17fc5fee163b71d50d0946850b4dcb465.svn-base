package com.dfire.retail.app.manage.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dfire.retail.app.manage.R;
public class ImageAdapter extends BaseAdapter{
 //类成员myContext为context父类
 private Context myContext;
 private int[] myImages;
 private LayoutInflater mLayoutInflater;
 
 private class ViewHolder {
	 ImageView image;
 }
 //构造函数，有两个参数，即要存储的Context和Images数组
 public ImageAdapter(Context c,int[] Images) 
 {
  // TODO Auto-generated constructor stub
  this.myContext=c;
  this.myImages=Images;
	mLayoutInflater =  LayoutInflater.from(c);
 }
 //返回所有的图片总数量
 public int getCount() 
 {
  return this.myImages.length;
 }
 //利用getItem方法，取得目前容器中图像的数组ID
 public Object getItem(int position)
 {
  return position;
 }

 public long getItemId(int position)
 {
  return position;
 }
 //取得目前欲显示的图像的VIEW，传入数组ID值使之读取与成像
 public View getView(int position, View convertView, ViewGroup parent) 
 {
	 ViewHolder viewHolder = null;
	 if (convertView == null) {
         convertView = mLayoutInflater.inflate(R.layout.gallery_item, null);
         viewHolder = new ViewHolder();
         viewHolder.image = (ImageView)convertView.findViewById(R.id.gallery_image);
	 }
	 else
	 {
		 viewHolder = (ViewHolder) convertView.getTag();
	 }
	 viewHolder.image.setImageResource(this.myImages[position]);
//  ImageView image=new ImageView(this.myContext);
	 viewHolder.image.setPadding(20, 5, 20, 5);
//  image.setImageResource(this.myImages[position]);
//	 viewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
//	 viewHolder.image.setAdjustViewBounds(true);
  return convertView;
 }

}