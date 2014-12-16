package com.dfire.retail.app.manage.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.data.UserVo;
import com.dfire.retail.app.manage.util.ImageUtil;
import com.dfire.retail.app.manage.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * EmployeeInfoActivity中用于部分展示员工信息的List Adapter
 * @author 刘思海
 */
@SuppressLint("InflateParams")
public class EmployeeInfoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<UserVo> mData;
    public EmployeeInfoAdapter (Context context, List<UserVo> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
    }
    
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EmployeeViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.shiftwork_item, null);
            viewHolder = new EmployeeViewHolder();
            viewHolder.portrait = (ImageView) convertView.findViewById(R.id.userPortrait);
            viewHolder.profession = (TextView) convertView.findViewById(R.id.profession);
            viewHolder.empname = (TextView) convertView.findViewById(R.id.emp_name);
            viewHolder.empno = (TextView) convertView.findViewById(R.id.emp_number);
            viewHolder.starttitle = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.endtitle = (TextView) convertView.findViewById(R.id.end_time);
            viewHolder.startdate = (TextView) convertView.findViewById(R.id.start_date);
            viewHolder.starthour = (TextView) convertView.findViewById(R.id.start_hour);
            viewHolder.enddate = (TextView) convertView.findViewById(R.id.end_date);
            viewHolder.endhour = (TextView) convertView.findViewById(R.id.end_hour);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (EmployeeViewHolder) convertView.getTag();
        }
        viewHolder.profession.setText(getRoleName(mData.get(position).getRoleId()));     
        viewHolder.empname.setText(mData.get(position).getName());
        TextPaint tp = viewHolder.empname.getPaint(); //加粗
		tp.setFakeBoldText(true); 
        viewHolder.empno.setText("工号 ：" + mData.get(position).getStaffId());
        viewHolder.starttitle.setText(mData.get(position).getShopName());
        viewHolder.startdate.setVisibility(View.INVISIBLE);
        viewHolder.starthour.setVisibility(View.INVISIBLE);
        if(mData.get(position).getMobile() != null)
        	viewHolder.endtitle.setText("手机 ：" + mData.get(position).getMobile());
        else{
        	viewHolder.endtitle.setText("手机 ：" + "无");
        }
        viewHolder.enddate.setVisibility(View.INVISIBLE);
        viewHolder.endhour.setVisibility(View.INVISIBLE);
        
        final ImageView imageView = viewHolder.portrait;
        
        String imageUrl = mData.get(position).getFileNameSmall();
    	/**图片*/
		if (!StringUtils.isEmpty(imageUrl)&&!StringUtils.isEquals(imageUrl, "")) {
			ImageLoader.getInstance().loadImage(imageUrl, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
				}
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					imageView.setImageResource(R.drawable.pic_none);
				}
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					if (loadedImage == null) {
						imageView.setImageResource(R.drawable.pic_none);
					}else{
						imageView.setImageBitmap(ImageUtil.getRoundedCornerBitmap(loadedImage, 5));/**处理圆角*/
					}
				}
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
				}
			});
		}else {
			viewHolder.portrait.setImageResource(R.drawable.pic_none);
		}
        return convertView;
    }
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
}
