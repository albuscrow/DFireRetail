package com.dfire.retail.app.manage.data;

/*
 * 会员卡订单商品详情
 * 
 */
public class CardOrderGoodsVo {
	private String goodsId;
	private long price;
	private Integer number;
	private String name;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
