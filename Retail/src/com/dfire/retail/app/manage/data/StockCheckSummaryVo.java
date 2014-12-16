/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2DFIRE Co., Ltd. 2014 
 *
 * 工程名称： Retail
 * 创建者： chengzi  
 * 创建日期： 2014年11月18日
 * 创建记录： 创建类结构。
 *
 * ************************* 变更记录 ********************************
 * 修改者： 
 * 修改日期：
 * 修改记录：
 *
 **/

package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**     
 * 项目名称：Retail  
 * 类名称：StockCheckSummaryVo  
 * 类描述：   盘点结果汇总
 * 创建时间：2014年11月18日 上午10:10:03  
 * @author chengzi  
 * @version 1.0
 */
public class StockCheckSummaryVo implements Serializable {

    private static final long serialVersionUID = 6154840273908770266L;

    /**原库存总数*/
    private Integer totalCount;
    /**盘点库存总数*/
    private Integer totalCheckCount;
    /**总库存盈亏数*/
    private Integer totalResultCount;
    /**总库存金额*/
    private BigDecimal totalRetailPrice;
    /**总盈亏金额*/
    private BigDecimal totalResultPrice;
    private List<StockGoodsCheckVo> stockGoodsCheckVoList;
    public Integer getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    public Integer getTotalCheckCount() {
        return totalCheckCount;
    }
    public void setTotalCheckCount(Integer totalCheckCount) {
        this.totalCheckCount = totalCheckCount;
    }
    public Integer getTotalResultCount() {
        return totalResultCount;
    }
    public void setTotalResultCount(Integer totalResultCount) {
        this.totalResultCount = totalResultCount;
    }
    public BigDecimal getTotalRetailPrice() {
        return totalRetailPrice;
    }
    public void setTotalRetailPrice(BigDecimal totalRetailPrice) {
        this.totalRetailPrice = totalRetailPrice;
    }
    public BigDecimal getTotalResultPrice() {
        return totalResultPrice;
    }
    public void setTotalResultPrice(BigDecimal totalResultPrice) {
        this.totalResultPrice = totalResultPrice;
    }
    public List<StockGoodsCheckVo> getStockGoodsCheckVoList() {
        return stockGoodsCheckVoList;
    }
    public void setStockGoodsCheckVoList(List<StockGoodsCheckVo> stockGoodsCheckVoList) {
        this.stockGoodsCheckVoList = stockGoodsCheckVoList;
    }
    
}
