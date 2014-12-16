package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.activity.setting.RolePermissionActivity;
import com.dfire.retail.app.manage.activity.setting.RolePermissionSettingActivity;
import com.dfire.retail.app.manage.adapter.RolePermissionSettingAdapter;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.RoleVo;
import com.dfire.retail.app.manage.data.StockCheckArea;
import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
import com.dfire.retail.app.manage.data.bo.RoleListBo;
import com.dfire.retail.app.manage.data.bo.StockCheckBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.util.DBHelper;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckRegionActivity  
 * 类描述：   盘点区域
 * 创建时间：2014年11月28日 下午1:11:22  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckRegionActivity extends TitleActivity implements OnClickListener {
    protected static final int REQUEST_STOCK_CHECK = 0;
    private List<StockCheckArea> regions = new ArrayList<StockCheckArea>();
    private StockCheckRegionAdapter adapter;
    private Integer location;
    private String selectShopId,stockCheckId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check_region);
        setTitleText("盘点区域");
        findViewById(R.id.minus).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.help).setOnClickListener(this);
        //mData = getResources().getStringArray(R.array.setting_role);
        ListView list = (ListView) findViewById(R.id.stock_check_region_list);
        new ListAddFooterItem(this,list);
        adapter = new StockCheckRegionAdapter(this, regions);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                  
                String region = regions.get(position).getRegion();
                Intent intent = new Intent(StockCheckRegionActivity.this,StockCheckGoodsListActivity.class);
                intent.putExtra("selectShopId", selectShopId);
                intent.putExtra("stockCheckId", stockCheckId);
                intent.putExtra("region", region);
                
                startActivityForResult(intent, REQUEST_STOCK_CHECK);
            }
        });
        this.showBackbtn();
        initData();
    }

    private void initData() {
        Intent intent = this.getIntent();
        selectShopId= intent.getStringExtra("selectShopId");
        stockCheckId= intent.getStringExtra("stockCheckId");
        this.initRegionList(); 
        
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.help:
            break;
        case R.id.submit:
            this.submitConfirm();
            break;
        case R.id.minus:
            this.add();
            break;
        }
    }
    private void submitConfirm() {
        final AlertDialog alertDialog = new AlertDialog(this);
        alertDialog.setMessage("确定要提交区域盘点数据吗？");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                submit();
            }
        });
        alertDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    private void submit() {
        DBHelper dbHepler = new DBHelper(this);
        String sql = "select * from stockcheck where stockcheckid=? and region=?";
        String[] param = new String[2];
        param[0] = this.stockCheckId;
        List<StockGoodsCheckVo> checkGoodsList = new ArrayList<StockGoodsCheckVo>();
        try {
            final SQLiteDatabase db = dbHepler.getReadableDatabase();
            for(StockCheckArea area : this.regions) {
                param[1] = area.getRegion();
                Cursor cursor = db.rawQuery(sql,param);
                if(cursor.getCount() > 0) {
                    while(!cursor.isLast()) {
                        cursor.moveToNext();
                        StockGoodsCheckVo vo = new StockGoodsCheckVo();
                        vo.doInit(cursor);
                        vo.setRegion(area.getRegion());
                        checkGoodsList .add(vo);
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();  
            
        }finally {
            dbHepler.close();
        } 
        if(checkGoodsList.size() == 0) {
            new ErrDialog(this, "没有未提交的盘点数据").show();
            return;
        }
        RequestParameter params = new RequestParameter(true);
        params.setUrl(Constants.STOCK_CHECK_SUBMIT_STORE);
        params.setParam("shopId", this.selectShopId);
        params.setParam("operateType", "do");
        
        
        try {
            params.setParam("stockGoodsCheckVoList", new JSONArray(new Gson().toJson(checkGoodsList)));
        } catch (JSONException e1) {
            e1.printStackTrace();  
            
        }
        new AsyncHttpPost(this, params, StockCheckBo.class, new RequestCallback(){
            @Override
            public void onSuccess(Object bo) {
                StockCheckBo stockCheckStart = (StockCheckBo)bo;
                
                //清除本地数据
                DBHelper dbHepler = new DBHelper(StockCheckRegionActivity.this);
                try {
                    final SQLiteDatabase db = dbHepler.getWritableDatabase();
                    db.execSQL("delete from stockcheck where stockcheckid=?",new String[]{stockCheckId});
                } catch (SQLException e) {
                    e.printStackTrace();  
                    
                }finally {
                    dbHepler.close();
                }
                for(StockCheckArea area : regions) {
                    area.setCheckedCount(0);
                }
                adapter.notifyDataSetChanged();
                new ErrDialog(StockCheckRegionActivity.this, "盘点提交成功").show();
            }

            @Override
            public void onFail(Exception e) {
                new ErrDialog(StockCheckRegionActivity.this, "盘点提交失败").show();
            }}).execute();
        
    }

    private void add() {
          
        Intent intent = new Intent(this,StockCheckRegionAddActivity.class);
        intent.putExtra("selectShopId", this.selectShopId);
        startActivityForResult(intent, RESULT_SAVE);
        
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == RESULT_SAVE) {
            StockCheckArea area = (StockCheckArea)data.getSerializableExtra("area");
            if(area != null) {
                this.regions.add(area);
                Collections.sort(this.regions);
                adapter.notifyDataSetChanged();
            }
        }else if(requestCode == REQUEST_STOCK_CHECK) {
            this.initRegionList();
        }
    }

    private void initRegionList() {
        DBHelper dbHepler = new DBHelper(this);
        
        try {
            this.regions.clear();
            final SQLiteDatabase db = dbHepler.getReadableDatabase();
            Cursor cursor = db.rawQuery("select area.region as region,case when s.checkedcount is null then 0 else s.checkedcount end as checkedcount from stockcheckarea area "
                    + "left join (select region ,count(1) as checkedcount from stockcheck where stockcheckid=? group by region) s on s.region=area.region "
                    + "where area.shopid=?", new String[]{stockCheckId,selectShopId});
            if(cursor.getCount() > 0) {
                while(!cursor.isLast()) {
                    cursor.moveToNext();
                    StockCheckArea area = new StockCheckArea();
                    area.setRegion(cursor.getString(cursor.getColumnIndex("region")));
                    area.setCheckedCount(cursor.getInt(cursor.getColumnIndex("checkedcount")));
                    this.regions.add(area);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();  
            
        }finally {
            dbHepler.close();
        } 
        
    }
    private class StockCheckRegionAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<StockCheckArea> mData;
        private int mResId;
        
        public StockCheckRegionAdapter(Context context, List<StockCheckArea> data) {
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
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.stock_check_region_item, null);
                holder.txt = (TextView) convertView.findViewById(R.id.text);
                holder.saveTag =  convertView.findViewById(R.id.saveTag);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            StockCheckArea region = (StockCheckArea)this.getItem(position);
            holder.txt.setText(region.getRegion());
            if(region.getCheckedCount() > 0) {
                holder.saveTag.setVisibility(View.VISIBLE);
            }else {
                holder.saveTag.setVisibility(View.GONE);
            }
            
            return convertView;
        }
        private class ViewHolder {
            TextView txt;
            View saveTag;
        }
    }
}
