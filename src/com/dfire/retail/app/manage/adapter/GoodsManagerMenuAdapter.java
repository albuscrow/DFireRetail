package com.dfire.retail.app.manage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;



public class GoodsManagerMenuAdapter extends BaseAdapter {


    
    private int[][] mData;
    private LayoutInflater mLayoutInflater;
    
    public GoodsManagerMenuAdapter(Context context) {
        mData = new int[][]{
                /*setting中位置,  图片, 主标题, 副标题, 小图标 */
                {R.drawable.setting_account_info, R.string.goods_sort, R.string.goods_sort_sub, },/* 账号信息 */
                {R.drawable.setting_params, R.string.goods_inf_manager, R.string.goods_inf_manager_sub, },/* 参数设置 */
                {R.drawable.setting_receipt, R.string.goods_import, R.string.goods_import_sub,},/* 小票设置 */
                {R.drawable.setting_role_permission, R.string.goods_split,R.string.goods_split_sub,},/* 角色管理与权限设置 */
                {R.drawable.setting_pay_way, R.string.goods_assemb, R.string.goods_assemb_sub},/* 付款方式设置 */
                {R.drawable.setting_business_model, R.string.goods_process_manager, R.string.goods_process_manager_sub},/* 商业模式设置 */
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
            convertView = mLayoutInflater.inflate(R.layout.goods_manager_menu_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.setting_item_img);
            viewHolder.mainText = (TextView) convertView.findViewById(R.id.setting_item_main_title);
            viewHolder.subheadText = (TextView) convertView.findViewById(R.id.setting_item_subhead_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.imageView.setImageResource(mData[position][0]);
        viewHolder.mainText.setText(mData[position][1]);
        viewHolder.subheadText.setText(mData[position][2]);
       
        return convertView;
    }
    
    private class ViewHolder {
        ImageView imageView;
        TextView mainText;
        TextView subheadText;
    }
}
