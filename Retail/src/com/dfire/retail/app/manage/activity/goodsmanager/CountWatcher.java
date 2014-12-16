package com.dfire.retail.app.manage.activity.goodsmanager;

public class CountWatcher {
	public GoodsManagerBaseActivity activity;
	public int changeCount = 0;

	public CountWatcher(GoodsManagerBaseActivity activity) {
		this.activity = activity;
	}
	
	public void add(){
		++ changeCount;
		check();
	}

	private void check() {
		if (changeCount == 0) {
			activity.switchToBackMode();
		}else{
			activity.switchToEditMode();
		}
	}
	
	public void minus(){
		-- changeCount;
		if (changeCount < 0) {
			changeCount = 0;
		}
		check();
	}
	
}
