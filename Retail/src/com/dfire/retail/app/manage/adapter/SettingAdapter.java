package com.dfire.retail.app.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;



public class SettingAdapter extends BaseAdapter {

    /** 标示数据无效.
     *  当是副标题无效时，尝试从通过登录者获取副标题信息，没获取到则为null.
     *  当是小图标无效时，则什么也不做.
     */
    private static final int INVALID = 0;
    /** 账号信息, 处于Setting List的 0 位置 */
    public static final int ACCOUNT_INFO = 0;
    /** 参数设置, 处于Setting List的 1 位置*/
    public static final int PARAM_SETTING = 1;
    /** 小票设置, 处于Setting List的 2 位置 */
    public static final int RECEIPT_SETTING = 2;
    /** 角色管理与权限设置, 处于Setting List的 3 位置 */
    public static final int ROLE_PERMISSION_SETTING = 3;
    /** 硬件设置, 处于Setting List的 4 位置 */
    //public static final int HARDWARE_SETTING = 4;
    /** 付款方式设置, 处于Setting List的 5 位置 */
    public static final int PAY_SETTING = 4;
    /** 商业模式设置, 处于Setting List的 6 位置 */
    public static final int BUSINESS_SETTING = 5;
    /** 查看操作日志, 处于Setting List的 7 位置 */
    public static final int OPERATE_LOG = 6;
    /** 数据清理, 处于Setting List的 8 位置 */
    public static final int DATA_CLEAR = 7;
    
    private int[][] mData;
    private LayoutInflater mLayoutInflater;
    
    public SettingAdapter(Context context) {
        mData = new int[][]{
                /*setting中位置,  图片, 主标题, 副标题, 小图标 */
                {ACCOUNT_INFO, R.drawable.setting_account_info, R.string.account_info, R.string.account_info, INVALID},/* 账号信息 */
                {PARAM_SETTING, R.drawable.setting_params, R.string.param_setting, R.string.param_setting, INVALID},/* 参数设置 */
                {RECEIPT_SETTING, R.drawable.setting_receipt, R.string.receipt_setting, R.string.receipt_setting, INVALID},/* 小票设置 */
                {ROLE_PERMISSION_SETTING, R.drawable.setting_role_permission, R.string.role_permission_setting, INVALID, R.drawable.ico_pw},/* 角色管理与权限设置 */
                /*{HARDWARE_SETTING, R.drawable.setting_hardware, R.string.hardware_setting, R.string.hardware_setting, 0},*//* 硬件设置 */
                {PAY_SETTING, R.drawable.setting_pay_way, R.string.pay_setting, INVALID, 0},/* 付款方式设置 */
                {BUSINESS_SETTING, R.drawable.setting_business_model, R.string.business_setting, R.string.business_setting_subhead, 0},/* 商业模式设置 */
                {OPERATE_LOG, R.drawable.setting_operate_log, R.string.operate_log, R.string.operate_log, 0},/* 查看操作日志 */
                {DATA_CLEAR, R.drawable.setting_data_clear, R.string.data_clear, R.string.data_clear_subhead, 0} /* 数据清理 */
        };
        
        mLayoutInflater =  LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.setting_main_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.setting_item_img);
            viewHolder.mainText = (TextView) convertView.findViewById(R.id.setting_item_main_title);
            viewHolder.subheadText = (TextView) convertView.findViewById(R.id.setting_item_subhead_title);
            viewHolder.lockImage = (ImageView) convertView.findViewById(R.id.setting_item_lock);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.imageView.setImageResource(mData[position][1]);
        viewHolder.mainText.setText(mData[position][2]);
        if (mData[position][3] != INVALID) {
            viewHolder.subheadText.setText(mData[position][3]);
        } else {
            //从登录模块拿数据
            viewHolder.subheadText.setText(null);
        }
        if (mData[position][4] != INVALID) {
            viewHolder.lockImage.setImageResource(mData[position][4]);
            viewHolder.lockImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lockImage.setVisibility(View.GONE);
        }
        
        return convertView;
    }
    
    private class ViewHolder {
        ImageView imageView;
        TextView mainText;
        TextView subheadText;
        ImageView lockImage;
    }
}
