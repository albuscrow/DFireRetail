/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2DFIRE Co., Ltd. 2014 
 *
 * 工程名称： Retail
 * 创建者： chengzi  
 * 创建日期： 2014年11月20日
 * 创建记录： 创建类结构。
 *
 * ************************* 变更记录 ********************************
 * 修改者： 
 * 修改日期：
 * 修改记录：
 *
 **/

package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;

/**     
 * 项目名称：Retail  
 * 类名称：StockCheckStatusBo  
 * 类描述：   盘点状态
 * 创建时间：2014年11月20日 上午10:53:53  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckStatusBo extends BaseRemoteBo {

    private static final long serialVersionUID = -6704029844348002445L;

    private String opUserId;
    private String message;
    private String stockCheckId;
    private Short checkType;
    public String getOpUserId() {
        return opUserId;
    }
    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStockCheckId() {
        return stockCheckId;
    }
    public void setStockCheckId(String stockCheckId) {
        this.stockCheckId = stockCheckId;
    }
    public Short getCheckType() {
        return checkType;
    }
    public void setCheckType(Short checkType) {
        this.checkType = checkType;
    }
    
}
