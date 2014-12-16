package com.dfire.retail.app.manage.data;

import java.util.Map;

/*
 * （会员汇总信息）
 * @author chenxx
 * 
 */
public class CardSummarizingVo {
	private Integer memberSum;//会员总数
	private Map<String, Integer> cardTypeSumMap;//按会员卡类型统计会员数
	private String sumPerMonth;//本月新增会员数

	public Integer getMemberSum() {
		return memberSum;
	}

	public void setMemberSum(Integer memberSum) {
		this.memberSum = memberSum;
	}

	public Map<String, Integer> getCardTypeSumMap() {
		return cardTypeSumMap;
	}

	public String getSumPerMonth() {
		return sumPerMonth;
	}

	public void setSumPerMonth(String sumPerMonth) {
		this.sumPerMonth = sumPerMonth;
	}
}
