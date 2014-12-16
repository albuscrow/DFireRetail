package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.ListAddFooterItem;
import com.dfire.retail.app.common.view.FootScanView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.CodeSearchCheckGoodsVo;
import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckReviewActivity  
 * 类描述：   库存盘点复核
 * 创建时间：2014年11月18日 下午2:50:44  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckReviewActivity extends TitleActivity implements View.OnClickListener {

    private static final int REQUEST_GOODS_INFO = 1;
    private View saveAndContinueBtn;
    private View printBtn;
    private TextView regionTxt;
    private ArrayList<StockGoodsCheckVo> goodsList = new ArrayList<StockGoodsCheckVo>();
    private StockCheckReviewAdapter adapter;
    private ListView listView;
    private Integer location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check_goods_review);
        setTitleText("库存盘点");
        showBackbtn();
        findView();
    }
    public void findView(){
        Intent intent=getIntent();
        ArrayList<StockGoodsCheckVo> list = (ArrayList<StockGoodsCheckVo>) intent.getSerializableExtra("goods");
        if(list != null) {
            this.goodsList  = list;
        }
        String region = intent.getStringExtra("region");
        //regionTxt = (TextView)findViewById(R.id.txt_region);
        TextView listTitleTxt = (TextView)findViewById(R.id.txt_list_title);
        if(region != null) {
            //regionTxt.setText(region);
            listTitleTxt.setText("盘点商品（" + region +   "）");
        }
        printBtn = findViewById(R.id.print);
        saveAndContinueBtn = findViewById(R.id.saveAndContinue);
        printBtn.setOnClickListener(this);
        saveAndContinueBtn.setOnClickListener(this);
        
        adapter = new StockCheckReviewAdapter(StockCheckReviewActivity.this, goodsList);
        listView = (ListView)findViewById(R.id.lv_check_goods);
        new ListAddFooterItem(this, listView);
        listView.setAdapter(adapter);
        /*listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                 
                location = position;
                Intent intent = new Intent(StockCheckReviewActivity.this,StockCheckGoodsInfoDetailActivity.class);  
                intent.putExtra("goods", goodsList.get(position));
                startActivity(intent);
            }
        });*/
    }
    @Override
    public void onClick(View v) {
          
        switch(v.getId()) {
        case R.id.print:
            this.print();
            break;
        case R.id.saveAndContinue:
            setResult(Activity.RESULT_OK);
            finish();
            break;
        default:break;
        }
        
    }
    private void print() {
          
        // TODO Auto-generated method stub  
        
    }
    
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
          
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_GOODS_INFO == requestCode && StockCheckGoodsInfoDetailActivity.RESULT_SAVE == resultCode) {
            int size = this.goodsList.size();
            if(this.location != null && size > this.location) {
                StockGoodsCheckVo vo = this.goodsList.get(location);
                int newStore = data.getIntExtra("newStore", 0);
                int exhibitCount = data.getIntExtra("exhibitCount", 0);
                vo.setCheckCount(newStore);
                vo.setGetLossNumber(exhibitCount);
                adapter.notifyDataSetChanged();
            }
        }else if(REQUEST_GOODS_INFO == requestCode && StockCheckGoodsInfoDetailActivity.RESULT_DELETE == resultCode) {
            int size = this.goodsList.size();
            if(this.location != null && size > this.location) {
                this.goodsList.remove(location.intValue());
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class StockCheckReviewAdapter extends BaseAdapter{

        private ArrayList<StockGoodsCheckVo> goodsList;
        private List<StockGoodsCheckVo> checkGoodsList;
        private LayoutInflater mLayoutInflater;
        
        public StockCheckReviewAdapter(Context context, ArrayList<StockGoodsCheckVo> goodsList) {
            super();
            this.goodsList = goodsList;
            mLayoutInflater =  LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return goodsList.size();
            
        }

        @Override
        public Object getItem(int position) {
            return goodsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.stock_check_goods_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.goodsName = (TextView) convertView.findViewById(R.id.txt_goods_name);
                viewHolder.barcode = (TextView) convertView.findViewById(R.id.txt_barcode);
                viewHolder.nowStore = (TextView) convertView.findViewById(R.id.txt_now_store);
                viewHolder.realStore = (TextView) convertView.findViewById(R.id.txt_real_store);
                viewHolder.exhibitCount = (TextView) convertView.findViewById(R.id.txt_exhibit_count);
                
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            StockGoodsCheckVo goods = (StockGoodsCheckVo)this.getItem(position);    
            viewHolder.goodsName.setText(goods.getGoodsName());
            viewHolder.barcode.setText(goods.getBarCode());
            if(goods.getCount() == null) {
                viewHolder.nowStore.setText("无");
            }else {
                viewHolder.nowStore.setText(String.valueOf(goods.getCount()));
            }
            
            viewHolder.realStore.setText(String.valueOf(goods.getCheckCount()));
            viewHolder.exhibitCount.setText(String.valueOf(goods.getGetLossNumber()));
                        
            return convertView;
        }
        
         private class ViewHolder {
                TextView goodsName;
                TextView barcode;
                TextView nowStore;
                TextView realStore;
                TextView exhibitCount;
            }

    }
}
