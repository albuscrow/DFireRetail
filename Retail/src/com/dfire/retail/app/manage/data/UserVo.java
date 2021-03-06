package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.List;

import com.dfire.retail.app.manage.global.Constants;

/**
 * 用户信息
 * 
 * @author kyolee
 * 
 */
public class UserVo implements Serializable {

	/*
	 * 参数名称 类型 长度范围 说明 userId String 32 用户ID userName String 50 登录名 name String
	 * 50 姓名 staffId String 50 工号 inDate Date 入职时间 mobile String 50 手机号 sex
	 * Integer 性别 birthday Date 生日 identityNo String 20 证件号码 identityTypeId
	 * Integer 证件类型 address String 100 住址 roleId String 角色ID file byte[] 文件内容
	 * fileName String 文件名(包括后缀) lastVer Long 版本号 userHandoverVo UserHandoverVo
	 * 交接班信息 roleName String 角色名 handoverPayTypeList List<HandoverPayTypeVo>
	 * 支付方式一览 fileOperate Short "文件操作 (null:不操作,1:上传,0:删除)"
	 * 
	 * 网络提交数据时，更新用户资料
	 * {"userId":"e55ecbcca8a24112b0bd561b706e0e43","userName":"xiangfei1QQ11",
	 * "pwd":"123456","name":"香榧","staffId":"717899",
	 * "roleId":"170ff6426b544730aaf7512d5ed50515"
	 * ,"shopId":"00000000000000000000000000000001",
	 * "inDate":"2014-09-18","mobile":"12345679876",
	 * "sex":"1","birthday":"1989-05-26"
	 * ,"identityTypeId":"1","identityNo":"256812198905263587",
	 * "address":"教工路","lastVer":"3","fileName":"","file":
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -7030356890222216243L;
	// 默认用户数据结构
	private String userId;// 用户ID
	private String userName;// 登陆名称
	private String name;// 姓名
	private String pwd;// 密码
	private String staffId;// 工号
	private String inDate;// 入职时间
	private String mobile;// 手机号
	private Integer sex;// 性别
	private String birthday;// 生日
	private String identityNo;// 证件号码
	private Integer identityTypeId;// 证件类型
	private String identityTypeName;// 证件类型
	private String address;// 住址
	private String shopId; // 店家ID
	private String shopName; // 店家名称
	private String roleId; // 角色ID
	private String roleName; // 角色ID
	private byte[] file;// 头像文件名称
	private String fileName;// 头像文件信息
	private Long lastVer;// 版本号
	private UserHandoverVo userHandoverVo; // 交接班信息
	private List<HandoverPayTypeVo> handoverPayTypeList;// List<HandoverPayTypeVo>
	private Short fileOperate;
	private boolean isHasDone = false;

	public UserHandoverVo getUserHandoverVo() {
		return userHandoverVo;
	}

	public void setUserHandoverVo(UserHandoverVo userHandoverVo) {
		this.userHandoverVo = userHandoverVo;
	}

	public List<HandoverPayTypeVo> getHandoverPayTypeList() {
		return handoverPayTypeList;
	}

	public void setHandoverPayTypeList(
			List<HandoverPayTypeVo> handoverPayTypeList) {
		this.handoverPayTypeList = handoverPayTypeList;
	}

	public Short getFileOperate() {
		return fileOperate;
	}

	public void setFileOperate(Short fileOperate) {
		this.fileOperate = fileOperate;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getIdentityTypeName() {
		return identityTypeName;
	}

	public void setIdentityTypeName(String string) {
		this.identityTypeName = string;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
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

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getLastVer() {
		return lastVer;
	}

	public void setLastVer(Long lastVer) {
		this.lastVer = lastVer;
	}

	public boolean isHasDone() {
		return isHasDone;
	}

	public void setHasDone(boolean isHasDone) {
		this.isHasDone = isHasDone;
	}
	/**
	 * @return the fileName
	 */
	public String getFileNameSmall() {
		if (fileName == null || fileName.length() == 0) {
			return "";
		}else{
			return fileName+Constants.FILENAME_SUFFIX_SMALL;
		}
	}
}
