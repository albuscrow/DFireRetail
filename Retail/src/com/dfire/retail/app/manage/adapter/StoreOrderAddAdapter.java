package com.dfire.retail.app.manage.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.StockInDetailVo;

/**
 * 物流管理-添加门店订货
 * @author ys
 *
 */
@SuppressLint("InflateParams")
public class StoreOrderAddAdapter extends BaseAdapter{

	private Context context;
	private List<StockInDetailVo> list=new ArrayList<StockInDetailVo>();
	private LayoutInflater mLayoutInflater;
	HolderView lHolderView = null;
	public StoreOrderAddAdapter(Context context, List<StockInDetailVo> list) {
		super();
		this.context = context;
		this.list = list;
		mLayoutInflater =  LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public List<StockInDetailVo> getList() {
		return list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		StockInDetailVo detailVo = list.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stock_order_add_item, null);
            lHolderView = new HolderView();
            lHolderView.setGoods_name((TextView) convertView.findViewById(R.id.goods_name));
            lHolderView.setGoods_price((TextView) convertView.findViewById(R.id.goods_price));
            lHolderView.setReduce_img((ImageView) convertView.findViewById(R.id.reduce_img)); //減
            lHolderView.setPlus_img((ImageView) convertView.findViewById(R.id.plus_img));//加
            lHolderView.setGoods_num((EditText) convertView.findViewById(R.id.goods_num));
            convertView.setTag(lHolderView);
        } else {
        	lHolderView = (HolderView) convertView.getTag();
        }
        if (detailVo!=null) {
        	 lHolderView.getGoods_name().setText(""+detailVo.getGoodsName()+"");
             lHolderView.getGoods_price().setText("￥:"+detailVo.getGoodsPrice()+"");
             lHolderView.getGoods_num().setText(1+"");
             lHolderView.getReduce_img().setOnClickListener(new OnClickListener() {
     				@Override
     				public void onClick(View arg0) {
     					String number = lHolderView.getGoods_num().getText().toString();
     					lHolderView.getGoods_num().setText((Integer.parseInt(number)-1)+"");
     				}
     			});
             lHolderView.getPlus_img().setOnClickListener(new OnClickListener() {
     				@Override
     				public void onClick(View arg0) {
     					String number = lHolderView.getGoods_num().getText().toString();
     					lHolderView.getGoods_num().setText((Integer.parseInt(number)+1)+"");
     				}
     			});
		}
        return convertView;
	}
	class HolderView implements Serializable{
		private static final long serialVersionUID = 8202294968497901240L;
		TextView goods_name;
	   	TextView goods_price;
		ImageView reduce_img;
		ImageView plus_img;
		EditText goods_num;
		 public TextView getGoods_name() {
				return goods_name;
			}
			public void setGoods_name(TextView goods_name) {
				this.goods_name = goods_name;
			}
			public TextView getGoods_price() {
				return goods_price;
			}
			public void setGoods_price(TextView goods_price) {
				this.goods_price = goods_price;
			}
			public ImageView getReduce_img() {
				return reduce_img;
			}
			public void setReduce_img(ImageView reduce_img) {
				this.reduce_img = reduce_img;
			}
			public ImageView getPlus_img() {
				return plus_img;
			}
			public void setPlus_img(ImageView plus_img) {
				this.plus_img = plus_img;
			}
			public EditText getGoods_num() {
				return goods_num;
			}
			public void setGoods_num(EditText goods_num) {
				this.goods_num = goods_num;
			}	
	}
}
