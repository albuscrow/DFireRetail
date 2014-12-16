package com.dfire.retail.app.manage.activity.stockmanager;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;

import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.common.ErrDialog;
import com.dfire.retail.app.manage.data.StockCheckArea;
import com.dfire.retail.app.manage.util.DBHelper;
import com.dfire.retail.app.manage.util.StringUtils;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckRegionAddActivity  
 * 类描述：   盘点区域添加
 * 创建时间：2014年11月28日 下午3:06:12  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckRegionAddActivity extends TitleActivity implements OnClickListener {

    private ItemEditText regionTxt;
    private String selectShopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_check_region_add);
        setTitleText("盘点区域");
        showBackbtn();
        super.mRight.setOnClickListener(this);
        regionTxt = (ItemEditText)this.findViewById(R.id.region);
        regionTxt.initLabel("盘点区域名称", "", true, InputType.TYPE_CLASS_TEXT);
        regionTxt.setMaxLength(20);
        regionTxt.setIsChangeListener(super.getItemChangeListener());
        
        Intent intent = this.getIntent();
        selectShopId = intent.getStringExtra("selectShopId");
    }

    @Override
    public void onClick(View v) {
          
        switch(v.getId()) {
        case R.id.title_right:
            this.save();
            break;
        }  
        
    }

    private void save() {
        if(StringUtils.isEmpty(regionTxt.getCurrVal())) {
            new ErrDialog(this, "请输入盘点区域名称").show();
            return;
        }  
        StockCheckArea area = new StockCheckArea();
        area.setRegion(regionTxt.getCurrVal());
        area.setShopId(selectShopId);
        DBHelper dbHepler = new DBHelper(this);
        try {
            final SQLiteDatabase db = dbHepler.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from stockcheckarea where shopid=? and region=?", new String[]{selectShopId,regionTxt.getCurrVal()});
            if(cursor.getCount() > 0) {
                new ErrDialog(this, regionTxt.getCurrVal() + "已经存在，请输入其他名字").show();
                return;
            }
            db.insert("stockcheckarea", null, area.getContentValues());
        } catch (SQLException e) {
            e.printStackTrace();  
            
        }finally {
            dbHepler.close();
        }
        
        Intent intent = new Intent();
        intent.putExtra("area", area);
        setResult(RESULT_SAVE, intent);
        finish();
    }
    
}
