package com.dfire.retail.app.manage.adapter;

import android.graphics.drawable.Drawable;

public class EmployeePFMItem {
    private Drawable mBitmap;
    // 职称，店员，店长等
    private String professional;
    private String name;
    private String emp_no;

    public EmployeePFMItem(Drawable mBitmap, String professional, String name,
            String emp_no) {
        this.mBitmap = mBitmap;
        this.emp_no = emp_no;
        this.professional = professional;
        this.name = name;
    }

    public Drawable getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Drawable mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmp_no() {
        return emp_no;
    }

    public void setEmp_no(String emp_no) {
        this.emp_no = emp_no;
    }

}
