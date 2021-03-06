package com.dfire.retail.app.manage.activity.login;

import java.io.IOException;

import android.app.Activity;
import android.app.WallpaperManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.adapter.ImageAdapter;

public class ChangeBackgroundActivity extends Activity {
	 Gallery mGallery;
	 RelativeLayout mGalleryRela;
	 int[] Resources=new int[]{
			 R.drawable.bg_01,
			 R.drawable.bg_02,
			 R.drawable.bg_03,
			 R.drawable.bg_04,
			   R.drawable.bg_05,
			   R.drawable.bg_06,
			   R.drawable.bg_07,
			   R.drawable.bg_08,
			   R.drawable.bg_09};
	 
	 int[] ResourcesIcon=new int[]{
			 R.drawable.bg_01_min,
			 R.drawable.bg_02_min,
			 R.drawable.bg_03_min,
			 R.drawable.bg_04_min,
			   R.drawable.bg_05_min,
			   R.drawable.bg_06_min,
			   R.drawable.bg_07_min,
			   R.drawable.bg_08_min,
			   R.drawable.bg_09_min};
	 
	 int index;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  //不显示标题栏
	  requestWindowFeature(Window.FEATURE_NO_TITLE); 
	  setContentView(R.layout.change_background);
	  mGallery=(Gallery)findViewById(R.id.Gallery);
	  
	  //mSwitcher=(ImageSwitcher)findViewById(R.id.ImageSwitcher);
	  mGalleryRela = (RelativeLayout)findViewById(R.id.my_gallery);
	  
	  //实现ImageSwitcher的工厂接口


	     mGalleryRela.setBackgroundResource(Resources[0]);
	     //设置动画
	  ImageAdapter mAdapter=new ImageAdapter(this,ResourcesIcon);
	  mGallery.setAdapter(mAdapter);
	  mGallery.setOnItemSelectedListener(new OnItemSelectedListener()
	  {
		  
	   public void onItemSelected(AdapterView<?> Adapter, View view,int position, long id)
	   {
	    //设置图片
	    mGalleryRela.setBackgroundResource(Resources[position]);
	    //获取当前图片索引
	    index=position;
	   }
	   
	   public void onNothingSelected(AdapterView<?> arg0) 
	   {

	   }

	  });

	       
	 }
	 //设置壁纸
	    public void SetWallPaper()
	    {
	     WallpaperManager mWallManager=WallpaperManager.getInstance(this);
	     try 
	     {
	    	 mWallManager.setResource(Resources[index]);
	     } 
	     catch (IOException e) 
	     {
	   e.printStackTrace();
	  }
	    }

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) 
	 {
	  return true;
	 }
	}