package com.dfire.retail.app.manage.adapter;

import java.io.Serializable;

/**
 * 存储付款方式的相关设置,相关数据的有效性在设置时保证
 * 
 * @author 刘思海
 * 
 */
public class PayWayElement implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    public String mNameType;
    public String mName;
    public boolean mChoiceSale = true; /* 默认都是计入销售额的 */
    public boolean mChoiceDiscount = true; /* 默认都是打折扣的 */
    public int mDiscount = 100; /* 默认折扣为100 */

    public int mPosition; /* 用于标记该元素处于List的位置, 注意仅在Adapter中更改 */
    
    public PayWayElement(String nameType, String name) {
        this(nameType, name, true, true, 100);
    }

    public PayWayElement(String nameType, String name, boolean choiceSale,
            boolean choiceDiscount, int discount) {
        mNameType = nameType;
        mName = name;
        mChoiceSale = choiceSale;
        mChoiceDiscount = choiceDiscount;
        mDiscount = discount;
    }
}
