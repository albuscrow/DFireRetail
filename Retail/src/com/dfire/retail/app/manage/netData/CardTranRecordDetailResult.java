package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.CardOrderGoodsVo;
/*
 * 会员卡交易明细输出参数
 * 
 */
public class CardTranRecordDetailResult extends BaseResult {
	private List<CardOrderGoodsVo> cardOrderGoodsList;//会员消费商品列表

	public List<CardOrderGoodsVo> getCardOrderGoodsList() {
		return cardOrderGoodsList;
	}

	public void setCardOrderGoodsList(List<CardOrderGoodsVo> cardOrderGoodsList) {
		this.cardOrderGoodsList = cardOrderGoodsList;
	}
}
