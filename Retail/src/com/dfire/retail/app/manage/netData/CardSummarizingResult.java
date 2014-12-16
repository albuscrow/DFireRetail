package com.dfire.retail.app.manage.netData;

import com.dfire.retail.app.manage.data.CardSummarizingVo;

public class CardSummarizingResult extends BaseResult {
	private CardSummarizingVo cardSummarizing;// 会员汇总信息

	public CardSummarizingVo getCardSummarizing() {
		return cardSummarizing;
	}

	public void setCardSummarizing(CardSummarizingVo cardSummarizing) {
		this.cardSummarizing = cardSummarizing;
	}
}
