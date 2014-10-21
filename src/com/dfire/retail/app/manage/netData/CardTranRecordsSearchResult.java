package com.dfire.retail.app.manage.netData;

import java.util.List;

import com.dfire.retail.app.manage.data.CardOrderVo;

public class CardTranRecordsSearchResult extends BaseResult {
	private List<CardOrderVo> dateFrom;//会员卡消费列表
	private long balance;//会员卡消费列表
	private Integer pageSize;//会员卡消费列表

	public List<CardOrderVo> getDateFrom() {
		return dateFrom;

	}

	public void setDateFrom(List<CardOrderVo> dateFrom) {
		this.dateFrom = dateFrom;
	}

	public long getBalance() {
		return balance;

	}

	public void setBalance(long balance) {
		this.balance = balance;
	}
	public Integer getPageSize(){
		return pageSize;
		
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
