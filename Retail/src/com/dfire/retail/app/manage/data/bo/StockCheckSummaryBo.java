package com.dfire.retail.app.manage.data.bo;

import com.dfire.retail.app.manage.data.StockCheckSummaryVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckSummaryBo  
 * 类描述：   盘点汇总结果
 * 创建时间：2014年11月20日 下午6:29:40  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckSummaryBo extends BaseRemoteBo {

    private static final long serialVersionUID = -6802657762998271782L;

    private StockCheckSummaryVo stockCheckSummaryVo;

    public StockCheckSummaryVo getStockCheckSummaryVo() {
        return stockCheckSummaryVo;
    }

    public void setStockCheckSummaryVo(StockCheckSummaryVo stockCheckSummaryVo) {
        this.stockCheckSummaryVo = stockCheckSummaryVo;
    }
    
}
