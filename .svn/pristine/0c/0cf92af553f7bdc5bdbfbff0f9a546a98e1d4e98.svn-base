package com.dfire.retail.app.manage.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 用户信息
 * 
 * @author kyolee
 * 
 */
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;
    //默认用户数据结构
    private String userId;// 用户ID
    private String userName;// 登陆名称
    private String name;// 姓名
    private String staffId;// 工号
    private Date inDate;// 入职时间
    private String inDateStr;
    private String mobile;// 手机号
    private Integer sex;// 性别
    private Date birthday;// 生日
    private String birthdayStr;
    private String identityNo;// 证件号码
    private Integer identityTypeId;// 证件类型
    private String address;// 住址

    private SimpleDateFormat mSimpleDateFormat;
    
    //添加额外关联的数据
    private String shopId; //店家ID
    private String shopName; //店家名称
    private String roleId; //角色ID
    private String roleName; //角色名称
    private int logoId;
    
    public UserVo() {
    }

    public UserVo(String userId, String userName, String name, String staffId,
            String inDate, String mobile, Integer sex, String birthday,
            String identityNo, Integer identityTypeId, String address) {
        
        this.userId = userId;
        this.userName = userName;
        this.name = name;
        this.staffId = staffId;
        this.mobile = mobile;
        this.sex = sex;
        this.identityNo = identityNo;
        this.identityTypeId = identityTypeId;
        this.address = address;
        
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        
        try {
            this.inDate = mSimpleDateFormat.parse(inDate);
            inDateStr = inDate;
        } catch (Exception e) {
            this.inDate = null;
            inDateStr = null;
        }
        try {
            this.birthday = mSimpleDateFormat.parse(birthday);
            birthdayStr = birthday;
        } catch (Exception e) {
            this.birthday = null;
            birthdayStr = null;
        }

    }
    
    public UserVo (JSONObject jobj) throws JSONException {

        this.userId = jobj.getString("userId");
        this.userName = jobj.getString("userName");
        this.name = jobj.getString("name");
        this.staffId = jobj.getString("staffId");
        this.mobile = jobj.getString("mobile");
        this.sex = Integer.valueOf(jobj.getString("sex"));
        this.identityNo = jobj.getString("identityNo");
        this.identityTypeId = Integer.valueOf(jobj.getString("identityTypeId"));
        this.address = jobj.getString("address");
        
        if (mSimpleDateFormat == null) {
            mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        
        try {
            if ((!jobj.getString("inDate").equals("null"))
                    && (jobj.getString("inDate") != null)) {
                this.inDate = mSimpleDateFormat.parse(jobj.getString("inDate"));
                inDateStr = jobj.getString("inDate");
            }
        } catch (Exception e) {
            this.inDate = null;
            inDateStr = null;
        }

        try {
            if ((!jobj.getString("birthday").equals("null"))
                    && (jobj.getString("birthday") != null)) {
                this.birthday = mSimpleDateFormat.parse(jobj.getString("birthday"));
                birthdayStr = jobj.getString("birthday");
            }
        } catch (Exception e) {
            this.birthday = null;
            birthdayStr = null;
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

    public String getInDateStr() {
        return inDateStr;
    }
    
    public Date getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        try {
            this.inDate = mSimpleDateFormat.parse(inDate);
            inDateStr = inDate;
        } catch (Exception e) {
            this.inDate = null;
            inDateStr = null;
        }
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
    
    public void setBirthday(String birthday) {
        try {
            this.birthday = mSimpleDateFormat.parse(birthday);
            birthdayStr = birthday;
        } catch (Exception e) {
            this.birthday = null;
            birthdayStr = null;
        }
    }
    
    public String getBirthdayStr() {
        return birthdayStr;
    }
    
    public Date getBirthday() {
        return birthday;
    }
    
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopId() {
        return shopId;
    }
    
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    
    public String getShopName() {
        return shopName;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleId() {
        return roleId;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setLogId(int logoId) {
        this.logoId = logoId;
    }
    
    public int getLogoId() {
        return logoId;
    }
}
