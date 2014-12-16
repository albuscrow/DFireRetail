/**
 * 
 */
package com.dfire.retail.app.manage.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ItemBase;
import com.dfire.retail.app.common.item.listener.IItemIsChangeListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 头部栏
 * 
 * @author qiuch
 * 
 */
public class TitleActivity extends BaseActivity {
	protected ImageButton mBack, mLeft, mRight;

	protected  TextView titleTxt;
	private IItemIsChangeListener itemChangeListener;
	private Map<View,Short> itemEditMap = new HashMap<View,Short>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.title_layout);
		FrameLayout body = (FrameLayout) findViewById(R.id.body);
		body.requestFocus();
		getLayoutInflater().inflate(layoutResID, body, true);
		
		titleTxt = (TextView) findViewById(R.id.title_text);
		mBack = (ImageButton) findViewById(R.id.title_back);
		mLeft = (ImageButton) findViewById(R.id.title_left);
		mRight = (ImageButton) findViewById(R.id.title_right);
		OnClickListener cancelListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                TitleActivity.this.finish();
            }
        };
		mBack.setOnClickListener(cancelListener);
		mLeft.setOnClickListener(cancelListener);
	}

	/**
	 * 设置头部标题内容
	 * 
	 * @param 标题字符串
	 */
	public void setTitleText(String title) {
		//((TextView) findViewById(R.id.title_text)).setText(title);
		
		titleTxt.setText(title);
	}

	/**
	 * 设置头部标题内容
	 * 
	 * @param 标题资源ID
	 */
	public void setTitleRes(int resId) {
		//((TextView) findViewById(R.id.title_text)).setText(resId);
		titleTxt.setText(resId);
	}

	/**
	 * 显示返回按钮
	 */
	public void showBackbtn() {
		//mBack = (ImageButton) findViewById(R.id.title_back);
		mBack.setVisibility(View.VISIBLE);
		if(mRight != null) {
		    mRight.setVisibility(View.GONE);
		}
		if(mLeft != null) {
		    mLeft.setVisibility(View.GONE);
        }
		/*mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TitleActivity.this.finish();
			}
		});*/
	}

	public ImageButton setRightBtn(int ResId) {
		//mRight = (ImageButton) findViewById(R.id.title_right);
		//mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(ResId);
		mRight.setVisibility(View.VISIBLE);
		return mRight;
	}

	public ImageButton change2saveFinishMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.VISIBLE);
		//mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.GONE);
		/*mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TitleActivity.this.finish();
			}
		});*/

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.GONE);
		mRight.setImageResource(R.drawable.save);
		return mRight;
	}
	
	/**
	 * 添加信息的保存模式
	 * @return
	 */
	public ImageButton change2AddSaveMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.GONE);
		//mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.VISIBLE);

		//mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(R.drawable.save);
		//setRightBtn(R.drawable.save);
		return mRight;
	}
	
	public ImageButton change2saveMode() {
		if (mBack == null) {
			mBack = (ImageButton) findViewById(R.id.title_back);
		}
		mBack.setVisibility(View.GONE);
		//mLeft = (ImageButton) findViewById(R.id.title_left);
		mLeft.setImageResource(R.drawable.cancel);
		mLeft.setVisibility(View.VISIBLE);
		

		mRight = (ImageButton) findViewById(R.id.title_right);
		mRight.setVisibility(View.VISIBLE);
		mRight.setImageResource(R.drawable.save);
		//setRightBtn(R.drawable.save);
		return mRight;
	}
	
	/**
	 * 将byte类型的图片数据、转换成bitmap类型
	 * @param bytes 图片数据信息
	 * @param opts 转换选项
	 * @return
	 */
	public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) { 
	    if (bytes != null) 
	        if (opts != null) 
	            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts); 
	        else 
	            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
	    return null; 
	} 

	/**
	 * 从输入流中读取数据、并转换成byte[]类型
	 * @param inStream 相册图片的输入流
	 * @return 返回byte[]类型的数据流
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception { 
	    byte[] buffer = new byte[1024]; 
	    int len = -1; 
	    ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
	    while ((len = inStream.read(buffer)) != -1) { 
	             outStream.write(buffer, 0, len); 
	    } 
	    byte[] data = outStream.toByteArray(); 
	    outStream.close(); 
	    inStream.close(); 
	    return data; 

	} 
	
	/**
	 * 检测当前页面信息是否有被修改
	 * @param change 记录当前页面子项的修改记录
	 * @return
	 */
	/*public boolean isChange(HashMap<String, Boolean> change){
		boolean ret = false;
		
		for(int i = 0;i < change.size() ;i++){
			if(change.get(Integer.toString(i))){
				ret = true;
			}
		}
		return ret;
	}*/
	

	/**
	 * 根据输入的角色roleId获取对应的名字
	 * @param roleId 角色ID
	 * @return 角色名称
	 */
	public String  getRoleName(String roleId){
		String ret = "";
		
		for(int i = 0;i < RetailApplication.getRoleList().size() ;i++){
			if(RetailApplication.getRoleList().get(i).getRoleId().equals(roleId)){
				
				ret = RetailApplication.getRoleList().get(i).getRoleName();
				break;
			}
		}
		return ret;
	}
	

	public String  getShopName(String shopId){
		String ret = "";
		
		for(int i = 0;i < RetailApplication.getShopList().size() ;i++){
			if(getMyApp().getShopList().get(i).getShopId().equals(shopId)){
				
				ret = RetailApplication.getShopList().get(i).getShopName();
				
				break;
			}
		}
		return ret;
	}
	
	

	public String  getIndentityName(String identityId){
		String ret = "";
		
		for(int i = 0;i < RetailApplication.getIdentityTypeList().size() ;i++){
			if(RetailApplication.getIdentityTypeList().get(i).getVal().equals(Integer.valueOf(identityId))){
				
				ret = RetailApplication.getIdentityTypeList().get(i).getName();
				
				break;
			}
		}
		return ret;
	}
	
	/**
	 * 根据输入的角色sxiId获取对应的名字
	 * @param sxiId 角色ID
	 * @return 性别名称
	 */
	public String  getSexName(String sexId){
		String ret = "";
		
		for(int i = 0;i < RetailApplication.getSexList().size() ;i++){
			if(RetailApplication.getSexList().get(i).getVal().equals(Integer.valueOf(sexId))){
				
				ret = RetailApplication.getSexList().get(i).getName();
				break;
			}
		}
		return ret;
	}
	
    //统计当前页面的修改记录，返回时候有修改记录未保存
    public boolean isHaveChange(boolean [] change){
    	boolean ret = false;
    	for(int i=0;i < change.length;i++){
    		if(change[i]){
    			ret = true;
    			break;
    		}
    	}
    	return ret;
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
/*		if(getProgressDialog().isShowing()){
			getProgressDialog().dismiss();
		}*/
		//}else{
			TitleActivity.this.finish();
		//}
	}

    public IItemIsChangeListener getItemChangeListener() {
        if(itemChangeListener == null) {
            itemChangeListener = new IItemIsChangeListener(){

                @Override
                public void onItemIsChangeListener(View v) {
                    ItemBase item = (ItemBase)v;
                    if(StringUtils.isEquals(item.getCurrVal(),item.getOldVal())) {
                        itemEditMap.put(v, (short)0);
                    }else {
                        itemEditMap.put(v, (short)1);
                        
                    }
                    Collection<Short> collection = itemEditMap.values();
                    if(collection.contains((short)1) && mBack.getVisibility() == View.VISIBLE) {
                        change2saveMode();
                    }else if(!collection.contains((short)1) && mRight.getVisibility() == View.VISIBLE) {
                        showBackbtn();
                    }
                    
                }};
        }
        return itemChangeListener;
    }
}
