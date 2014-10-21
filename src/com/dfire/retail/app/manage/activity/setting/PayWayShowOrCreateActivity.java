package com.dfire.retail.app.manage.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.PayWayElement;


/**
 * 显示、创建或修改已有付款方式
 * @author 刘思海
 */
public class PayWayShowOrCreateActivity extends TitleActivity implements OnClickListener {
    
    /**
     * 用于标记是添加一种支付方式，还是修改已有支付方式.
     * true:标记是Change 显示; false:标记是Add button;
     */
    public static final String CHANGE_OR_ADD = "changeOrAdd";
    /** 标记修改时，付款方式数据 */
    public static final String PAYWAY_DATA = "paywayData";
    /** 标记删除操作 */
    public static final int PAYWAY_DELETE = 0x11;
    
    private ViewSwitcher mViewSwitcher;
    private Button mType;
    private EditText mName;
    private CheckBox mChoiceSale;
    private CheckBox mChoiceDiscount;
    private EditText mEditDiscount;
    
    /** 
     * true:标记来源于显示
     * false:来源于PayWaySetting Add button
     */
    private boolean mChangeOrAddState;
    private PayWayElement mElement;
    private String[] mPayType;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_payway_show_or_create);
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.setting_payway_viewswitcher);
        
        //解析Intent数据,并设置标题
        Intent intent = getIntent();
        mChangeOrAddState = intent.getBooleanExtra(CHANGE_OR_ADD, false);
        if (mChangeOrAddState) {//展示付款方式信息
            mElement = (PayWayElement) getIntent().getSerializableExtra(PAYWAY_DATA);
            setTitle(mElement.mName);
            
            findViewById(R.id.payway_show_edit).setOnClickListener(this);
            findViewById(R.id.payway_show_delete).setOnClickListener(this);
            
            initUnEditView();
        } else {//添加
            mPayType = getResources().getStringArray(R.array.setting_payway_type_array);
            mElement = new PayWayElement(mPayType[0], mPayType[0]);
            initEditableView(R.string.setting_payway_add);
        }
    }
    
    private void initUnEditView() {
        View parent = findViewById(R.id.setting_payway_show);
        //支付类型
        Button type = (Button) parent.findViewById(R.id.payway_create_type);
        type.setText(mElement.mNameType);
        type.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        type.setClickable(false);
        
        //付款名称
        EditText name = (EditText) parent.findViewById(R.id.payway_create_name);
        name.setText(mElement.mName);
        name.setEnabled(false);
        
        //是否计入销售额
        CheckBox sale = (CheckBox) parent.findViewById(R.id.payway_create_sale);
        sale.setChecked(mElement.mChoiceSale);
        sale.setClickable(false);
        
        //是否享受折扣
        CheckBox discount = (CheckBox) parent.findViewById(R.id.payway_create_discount);
        discount.setChecked(mElement.mChoiceDiscount);
        discount.setClickable(false);
        
        //折扣率
        EditText editDiscount = (EditText) parent.findViewById(R.id.payway_edit_discount);
        editDiscount.setText(String.valueOf(mElement.mDiscount));
        editDiscount.setEnabled(false);
    }

    private void initEditableView(int titleResId) {
        mViewSwitcher.showNext();
        View parent = findViewById(R.id.setting_payway_create);
        
        //支付类型
        mType = (Button) parent.findViewById(R.id.payway_create_type);
        mType.setText(mElement.mNameType);
        mType.setOnClickListener(this);
        
        //付款名称
        mName = (EditText) parent.findViewById(R.id.payway_create_name);
        mName.setText(mElement.mName);
        
        //是否计入销售额
        mChoiceSale = (CheckBox) parent.findViewById(R.id.payway_create_sale);
        mChoiceSale.setChecked(mElement.mChoiceSale);
        
        //是否享受折扣
        mChoiceDiscount = (CheckBox) parent.findViewById(R.id.payway_create_discount);
        mChoiceDiscount.setChecked(mElement.mChoiceDiscount);
        
        //折扣率
        mEditDiscount = (EditText) parent.findViewById(R.id.payway_edit_discount);
        mEditDiscount.setText(String.valueOf(mElement.mDiscount));
        
        //标题
        TextView title = (TextView) parent.findViewById(R.id.titleText);
        title.setText(titleResId);
        
        //标题按键
        parent.findViewById(R.id.title_left_btn).setOnClickListener(this);
        parent.findViewById(R.id.title_right_btn).setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.payway_show_edit:
            initEditableView(R.string.setting_payway_change);
            break;
            
        case R.id.payway_show_delete:
            setResult(PAYWAY_DELETE, getIntent());
            finish();
            break;
            
        case R.id.title_left_btn:
            setResult(RESULT_CANCELED);
            finish();
            break;
            
        case R.id.payway_create_type:
            Intent type = new Intent();
            type.setClass(this, PayWayTypeActivity.class);
            startActivityForResult(type, 1);
            break;
            
        case R.id.title_right_btn:
            //读取值
            mElement.mNameType = mType.getText().toString();
            mElement.mName = mName.getText().toString();
            if (TextUtils.isEmpty(mElement.mName)) {
                mElement.mName = mElement.mNameType;
            }
            mElement.mChoiceSale = mChoiceSale.isChecked();
            mElement.mChoiceDiscount = mChoiceDiscount.isChecked();
            String discountStr = mEditDiscount.getText().toString();
            if (TextUtils.isEmpty(discountStr)) {
                mElement.mDiscount = 100;/*默认折扣*/
            } else {
                int discount = Integer.valueOf(discountStr);
                if (discount > 100) {
                    mElement.mDiscount = 100;/*默认折扣*/
                } else if (discount < 0) {
                    mElement.mDiscount = 0;/*默认折扣*/
                } else {
                    mElement.mDiscount = discount;
                }
            }

            //保存值
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(PAYWAY_DATA, mElement);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
            break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            mType.setText(data.getStringExtra("result"));
        }
    }
}
