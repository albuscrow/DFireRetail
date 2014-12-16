package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckBo  
 * 类描述：   盘点共通
 * 创建时间：2014年11月20日 下午1:38:14  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckBo extends BaseRemoteBo {

    private static final long serialVersionUID = 7806672336806730946L;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
