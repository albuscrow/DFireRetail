package com.dfire.retail.app.manage.netData;

public class CardTranRecordsSearchParam extends BaseRequestData {
	private String cardId;//会员卡ID
	private Integer currentPage;//当前页

	public String getCardId() {
		return cardId;

	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Integer getCurrentPage() {
		return currentPage;

	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
}
