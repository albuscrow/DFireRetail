package com.dfire.retail.app.manage.data.bo;

import java.util.List;

import com.dfire.retail.app.manage.data.CodeSearchCheckGoodsVo;
import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 项目名称：Retail  
 * 类名称：StockCheckSearchGoodsBo  
 * 类描述：   盘点查询商品
 * 创建时间：2014年11月20日 下午7:27:26  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckSearchGoodsBo extends BaseRemoteBo {

    private static final long serialVersionUID = 1409222908002612119L;

    private Integer pageSize;
    private List<CodeSearchCheckGoodsVo> GoodsList;
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public List<CodeSearchCheckGoodsVo> getGoodsList() {
        return GoodsList;
    }
    public void setGoodsList(List<CodeSearchCheckGoodsVo> goodsList) {
        GoodsList = goodsList;
    }
    
}
