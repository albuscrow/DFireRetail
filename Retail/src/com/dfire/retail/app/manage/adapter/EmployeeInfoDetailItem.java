package com.dfire.retail.app.manage.adapter;


public class EmployeeInfoDetailItem {
    private String title;
    private String value;
    private boolean isChange = false;
    private int _iTag;//标记位置
    
    public EmployeeInfoDetailItem(String title, String value, boolean isChange) {
        this(title, value, isChange, 0);
    }
    
    public EmployeeInfoDetailItem(String title, String value, boolean isChange, int tag) {
        this.title = title;
        this.value = value;
        this.isChange = isChange;
        _iTag = tag;
    }

    public int getTag() {
        return _iTag;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

}
