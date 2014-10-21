/**
 * 
 */
package com.dfire.retail.app.manage.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户信息
 * 
 * @author qiuch
 * 
 */
public class UserInfo {
    private String userId;
    private String userName;
    private String name;
    private String staffId;
    private Date inDate;
    private String mobile;
    private Integer sex;
    private Date birthday;
    private String identityNo;
    private Integer identityTypeId;
    private String address;

    public UserInfo(String res) {
        JSONObject jobj;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
        try {
            jobj = new JSONObject(res);
            this.userId = jobj.getString("userId");
            this.userName = jobj.getString("userName");
            this.name = jobj.getString("name");
            this.staffId = jobj.getString("staffId");
            try {
                if ((!jobj.getString("inDate").equals("null"))
                        && (jobj.getString("inDate") != null))
                    this.inDate = sdf.parse(jobj.getString("inDate"));

                if ((!jobj.getString("birthday").equals("null"))
                        && (jobj.getString("birthday") != null))
                    this.birthday = sdf.parse(jobj.getString("birthday"));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            this.mobile = jobj.getString("mobile");
            this.sex = Integer.valueOf(jobj.getString("sex"));
            this.identityNo = jobj.getString("identityNo");
            this.identityTypeId = Integer.valueOf(jobj
                    .getString("identityTypeId"));
            this.address = jobj.getString("address");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Date getInDate() {
        return inDate;
    }

    public void setInDate(Date inDate) {
        this.inDate = inDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public Integer getIdentityTypeId() {
        return identityTypeId;
    }

    public void setIdentityTypeId(Integer identityTypeId) {
        this.identityTypeId = identityTypeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}