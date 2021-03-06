package com.dfire.retail.app.manage.adapter;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class AttendanceItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Drawable mBitmap;
    // 职称，店员，店长等
    private String professional;
    private String name;
    private String emp_no;
    private String startdate;
    private String enddate;
    private String starthour;
    private String endhour;
    private String startTitle;
    private String endTitle;

    public AttendanceItem(Drawable mBitmap, String professional, String name,
            String emp_no, String startdate, String enddate, String starthour,
            String endhour, String startTitle, String endTitle) {
        this.mBitmap = mBitmap;
        this.emp_no = emp_no;
        this.professional = professional;
        this.name = name;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starthour = starthour;
        this.endhour = endhour;
        this.startTitle = startTitle;
        this.endTitle = endTitle;

    }

    public String getStartTitle() {
        return startTitle;
    }

    public void setStartTitle(String startTitle) {
        this.startTitle = startTitle;
    }

    public String getEndTitle() {
        return endTitle;
    }

    public void setEndTitle(String endTitle) {
        this.endTitle = endTitle;
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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStarthour() {
        return starthour;
    }

    public void setStarthour(String starthour) {
        this.starthour = starthour;
    }

    public String getEndhour() {
        return endhour;
    }

    public void setEndhour(String endhour) {
        this.endhour = endhour;
    }

}
