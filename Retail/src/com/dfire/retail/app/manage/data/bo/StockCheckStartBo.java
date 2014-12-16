package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckStartBo  
 * 类描述：   盘点开始
 * 创建时间：2014年11月20日 上午11:07:12  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckStartBo extends BaseRemoteBo {

    private static final long serialVersionUID = 1654820542574596965L;

    private String message;
    private String limitsCount;
    private String stockCheckId;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getLimitsCount() {
        return limitsCount;
    }
    public void setLimitsCount(String limitsCount) {
        this.limitsCount = limitsCount;
    }
    public String getStockCheckId() {
        return stockCheckId;
    }
    public void setStockCheckId(String stockCheckId) {
        this.stockCheckId = stockCheckId;
    }
    
}
