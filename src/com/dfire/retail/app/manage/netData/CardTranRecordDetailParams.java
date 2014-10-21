package com.dfire.retail.app.manage.netData;

public class CardTranRecordDetailParams extends BaseRequestData {
	private String orderId;//订单ID

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
