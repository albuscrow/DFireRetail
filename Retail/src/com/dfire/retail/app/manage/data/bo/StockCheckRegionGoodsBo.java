package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.StockGoodsCheckVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckRegionGoodsBo  
 * 类描述：   区域盘点商品
 * 创建时间：2014年11月20日 下午7:18:12  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckRegionGoodsBo extends BaseRemoteBo {

    private static final long serialVersionUID = 6867449189492181717L;

    private List<StockGoodsCheckVo> stockGoodsCheckVoList;

    public List<StockGoodsCheckVo> getStockGoodsCheckVoList() {
        return stockGoodsCheckVoList;
    }

    public void setStockGoodsCheckVoList(List<StockGoodsCheckVo> stockGoodsCheckVoList) {
        this.stockGoodsCheckVoList = stockGoodsCheckVoList;
    }
    
}
