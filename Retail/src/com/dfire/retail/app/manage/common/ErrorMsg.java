package com.dfire.retail.app.manage.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

public  class ErrorMsg {
	
	/*##用户相关
	UM_MSG_000001=编号不能为空，请输入!
	UM_MSG_000002=编号输入错误，请重新输入!
	UM_MSG_000003=账号或密码错误，请重新输入!
	UM_MSG_000004=账号不能为空，请输入!
	UM_MSG_000005=密码不能为空，请输入!
	UM_MSG_000006=新密码不能为空，请输入!
	UM_MSG_000007=原密码不正确，请重新输入!
	UM_MSG_000008=两次输入的密码不一致，请重新输入!
	UM_MSG_000009=此编号未注册，请注册之后再登录!
	UM_MSG_000010=次账号未注册，请注册之后再登录!
	UM_MSG_000011=用户名已存在，请重新设置!
	UM_MSG_000012=登陆者不能删除自己！
	UM_MSG_000013=有营业数据的员工不允许删除！


	##商品相关
	GM_MSG_000001=商品名不能为空，请输入!
	GM_MSG_000002=商品条码未输入，确定要输入无码商品吗？
	GM_MSG_000003=商品零售价不能为空，请输入!
	GM_MSG_000004=商品库存数不能为空，请输入!
	GM_MSG_000005=商品入库门店不能为空，请选择!
	GM_MSG_000006=商品内容更新失败!
	GM_MSG_000007=商品内容添加失败!
	GM_MSG_000008=输入的商品不存在，是否需要添加此商品信息？
	GM_MSG_000009=确定要删除商品信息吗？
	GM_MSG_000010=该商品信息已被使用到库存和交易信息中，不能被删除!
	GM_MSG_000011=输入的商品条码已经存在，是否继续添加一码多品的商品？ 
	GM_MSG_000012=分类名称不能为空，请选择!
	GM_MSG_000013=分类名称已经存在，请使用其他名称。
	GM_MSG_000014=拥有子分类的分类，不能被删除。
	GM_MSG_000015=分类名称已被商品使用，删除分类，将使部分商品丢失分类信息。确定要删除吗？
	GM_MSG_000016=加工商品的规则数量必须为大于0的整数，请重新输入!
	GM_MSG_000017=本次加工数量必须为规则数量的整数倍，请重新输入!
	GM_MSG_000018=商品数量必须为大于0的整数，请重新输入!
	GM_MSG_000019=零售价必须大于0，请重新输入!
	GM_MSG_000020=零售价小于进货价，确定要保存吗？
	GM_MSG_000021=提成比率不能为负数，请重新输入!
	GM_MSG_000022=折扣率不能为负数，请重新输入!
	GM_MSG_000023=商品简码已存在，请重新输入!
	GM_MSG_000024=该商品已被删除!
	GM_MSG_000025=分类编码已经存在，请使用其他编码。

	##物流相关
	LM_MSG_000001=订货单添加失败!
	LM_MSG_000002=订货单更新失败!
	LM_MSG_000003=入库单更新失败!
	LM_MSG_000004=出库单更新失败!
	LM_MSG_000005=删除后不可恢复!是否要删除此订单？
	LM_MSG_000006=请选择供应商!
	LM_MSG_000007=请选择调入门店!
	LM_MSG_000008=请选择退货原因
	LM_MSG_000009=调拨数不能大于库存数，请重新输入!
	LM_MSG_000010=订货数量必须大于0，请重新输入!
	LM_MSG_000011=到货日不得小于今日，请重新输入!
	LM_MSG_000012=进货数量必须大于0，请重新输入!
	LM_MSG_000013=生产日期不能大于今日，请重新输入!
	LM_MSG_000014=确定要收货吗？
	LM_MSG_000015=确定要拒绝收货吗？
	LM_MSG_000016=请选择退货原因。
	LM_MSG_000017=退货数量不能大于库存数量，请重新输入!
	LM_MSG_000018=您添加的退货原因已经存在，请修改。
	LM_MSG_000019=调出门店和调入门店相同，请重新选择。
	LM_MSG_000020=该订货单已确认，不可操作！
	LM_MSG_000021=该进货单已确认收货或拒绝收货，不可操作！
	LM_MSG_000022=该退货单已确认收货或拒绝收货，不可操作！
	LM_MSG_000023=该调拨单已确认收货或拒绝收货，不可操作！


	##库存相关
	SM_MSG_000001=库存调整失败!
	SM_MSG_000002=盘点已经开始，不能重复开始盘点!
	SM_MSG_000003=无法结束盘点!
	SM_MSG_000004=清除盘点失败!
	SM_MSG_000005=您提交的商品在本区域内已盘点，是否覆盖之前的盘点记录？


	##门店连锁相关
	SC_MSG_000001=商户名称不能为空，请输入!
	SC_MSG_000002=店家代码不能为空，请输入!
	SC_MSG_000003=店家代码已存在，请重新设置!
	SC_MSG_000004=上级店家不能为空，请输入!
	SC_MSG_000005=店家类型不能为空，请选择!
	SC_MSG_000006=所在地区不能为空，请输入!
	SC_MSG_000007=请选择需要复制的门店!
	SC_MSG_000008=员工工号不能为空，请输入!
	SC_MSG_000009=员工工号已存在，请重新设置!
	SC_MSG_000010=请选择员工所属门店!
	SC_MSG_000011=请选择员工所属角色!
	SC_MSG_000012=确定要删除店家吗？
	SC_MSG_000013=此店家已有营业数据，确定要删除吗？
	SC_MSG_000014=使用期已过或被停用，请与系统服务商联系！
	SC_MSG_000015=商户名称已经存在，请使用其他名称。
	SC_MSG_000016=被复制商户商品数据不存在，请重新选择。
	SC_MSG_000017=该商户存在库存信息，不能删除!
	SC_MSG_000018=该商户存在订单信息，不能删除!
	SC_MSG_000019=商户代码不正确，找不到该商户!
	SC_MSG_000020=该商户尚未初始化,请与软件提供商联系!
	SC_MSG_000021=该商户的初始密码不正确，请与软件提供商联系!
	SC_MSG_000022=您的机器硬件发生变动，需要重置绑定信息，请与软件提供商联系!
	SC_MSG_000023=该商户尚未绑定，请与软件提供商联系!
	SC_MSG_000024=短信数量不足！
	SC_MSG_000025=该门店存在下属门店，不能删除！
	SC_MSG_000026=该门店存在员工，不能删除！


	##消息相关
	MC_MSG_000001=消息标题不能为空，请输入!
	MC_MSG_000002=消息内容不能为空，请输入!
	MC_MSG_000003=该消息已发布，不可删除！
	MC_MSG_000004=该消息已发布，不可修改！

	##会员相关
	MM_MSG_000001=请选择性别!
	MM_MSG_000002=会员名不能为空，请输入!
	MM_MSG_000003=手机号码不能为空，请输入!
	MM_MSG_000004=请选择卡类型!
	MM_MSG_000005=短信发送失败!
	MM_MSG_000006=短信发送成功!
	MM_MSG_000007=充值金额不能有小数，请重新输入!
	MM_MSG_000008=单次兑换的数量不能超过可兑换的总量，请重新输入!
	MM_MSG_000009=积分不足，请重新选择!
	MM_MSG_000010=此手机号码已经被使用，请重新输入！
	MM_MSG_000011=当前卡类型下有会员，无法删除！
	MM_MSG_000012=此卡类型名称已经存在，请重新输入！
	MM_MSG_000013=充值优惠赠送金额不匹配！

	##营销相关
	SG_MSG_000001=活动名称不能为空，请输入!
	SG_MSG_000002=开始时间不能为空，请输入!
	SG_MSG_000003=结束时间不能为空，请输入!
	SG_MSG_000004=特价不能为空，请输入!
	SG_MSG_000005=数量不能为空且必须大于0，请输入！
	SG_MSG_000006=满足金额不能为空且必须大于0，请输入!
	SG_MSG_000007=换购价不能为空且必须大于0，请输入!
	SG_MSG_000008=该活动正在进行中，无法删除!
	SG_MSG_000009=充值金额不能为空，且大于0，请输入!
	SG_MSG_000010=扣减金额不能为空，且大于0，请输入！
	SG_MSG_000011=扣减金额不能大于满足金额，请重新输入！
	SG_MSG_000012=折扣率必须大于0，且不能为空，请输入！
	SG_MSG_000013=优惠券名称不能为空，请输入!
	SG_MSG_000014=优惠券数量不能小于0，请输入！
	SG_MSG_000015=出券条件不能为空，且要大于0的整数，请输入！
	SG_MSG_000016=使用条件不能为空，且要大于0的整数，请输入！
	SG_MSG_000017=面额不能为空，且要大于0的整数，请输入！
	SG_MSG_000018=赠送金额不能为空，且大于0，请输入!
	SG_MSG_000019=赠送积分不能为空，且大于0，请输入!


	##系统设置
	SS_MSG_000001=今日的营业数据不能删除!
	SS_MSG_000002=今日操作记录不能删除!
	SS_MSG_000003=所有商品库存预警数量设置将被更新为{1}，是否继续执行更新？  //1代表数值
	SS_MSG_000004=所有商品过期提醒天数设置将被更新为{1}，是否继续执行更新？  //1代表数值
	SS_MSG_000005=角色名称不能为空，请选择!
	SS_MSG_000006=角色名称已存在，请重新输入!
	SS_MSG_000007=清理之后，数据将不能恢复，确定清理吗？
	SS_MSG_000008=门店数超过1家时不能设置为单店模式!
	SS_MSG_000009=小票设置信息获取失败，请联系系统管理员!
	SS_MSG_000010=没有可用的小票模板，请联系系统管理员!
	SS_MSG_000011={1}将于{2}到期，请尽快处理！                              //1代表商品名称，2代表商品到期日期
	SS_MSG_000012={1}剩余库存数量为{2}，请尽快补货！                        //1代表商品名称，2代表数值
	SS_MSG_000013=该角色权限信息不存在！
	SS_MSG_000014=系统权限信息不存在！
	SS_MSG_000015=该角色存在员工，不能删除！
*/


	


	
		
		private static HashMap<String, String> errorMsg = new HashMap<String, String>();
		
		private static void initErrorMsg(){
			errorMsg.put("UM_MSG_000001", "编号不能为空，请输入!");
			errorMsg.put("UM_MSG_000002", "编号输入错误，请重新输入!");
			errorMsg.put("UM_MSG_000003", "账号或密码错误，请重新输入!");
			errorMsg.put("UM_MSG_000004", "账号不能为空，请输入!");
			errorMsg.put("UM_MSG_000005", "密码不能为空，请输入!");
			errorMsg.put("UM_MSG_000006", "新密码不能为空，请输入!");
			errorMsg.put("UM_MSG_000007", "原密码不正确，请重新输入!");
			errorMsg.put("UM_MSG_000008", "两次输入的密码不一致，请重新输入!");
			errorMsg.put("UM_MSG_000009", "此编号未注册，请注册之后再登录!");
			errorMsg.put("UM_MSG_000010", "次账号未注册，请注册之后再登录!");
			errorMsg.put("UM_MSG_000011", "用户名已存在，请重新设置!");
			errorMsg.put("UM_MSG_000012", "登陆者不能删除自己！");
			errorMsg.put("UM_MSG_000013", "有营业数据的员工不允许删除！");			
			/*##共通消息
			CS_MSG_000001=查询结果为空!
			CS_MSG_000002=开始时间必须小于结束时间，且结束时间大于本日，请重新输入!
			CS_MSG_000003=该商品库存为0，不能添加!
			CS_MSG_000004=无此功能修改权限!
			CS_MSG_000005=无此功能查看权限!
			CS_MSG_000006=充值金额必须为大于或50元的整数!
			CS_MSG_000007=折扣率不能大于100!
			CS_MSG_000008=折扣不能大于100，请重新输入!
			CS_MSG_000009=折扣不能为负，请重新输入!
			CS_MSG_000010=折扣不能为0，请重新输入!
			CS_MSG_000011=该数据已被别人修改或删除!
			CS_MSG_000012=输入参数为空或格式不正确，请重试!
			CS_MSG_000013=调用服务时的参数不正确!
			CS_MSG_000014=服务器中断，请联系客服!
			CS_MSG_000015=开始日期不能为空，请重新输入!
			CS_MSG_000016=结束日期不能为空，请重新输入!
			CS_MSG_000017=开始日期必须小于结束日期，且结束日期大于本日，请重新输入!
			CS_MSG_000018=上传的文件类型不支持!
			CS_MSG_000019=会话状态失效，请重新登录!*/
			errorMsg.put("java.lang.NullPointerException", "服务器空指针异常！");
			errorMsg.put("CS_MSG_000001", "查询结果为空!");
			errorMsg.put("CS_MSG_000002", "开始时间必须小于结束时间，且结束时间大于本日，请重新输入!");
			errorMsg.put("CS_MSG_000003", "该商品库存为0，不能添加!");
			errorMsg.put("CS_MSG_000004", "无此功能修改权限!");
			errorMsg.put("CS_MSG_000005", "无此功能查看权限!");
			errorMsg.put("CS_MSG_000006", "充值金额必须为大于或50元的整数!");
			errorMsg.put("CS_MSG_000007", "折扣率不能大于100!");
			errorMsg.put("CS_MSG_000008", "折扣不能为负，请重新输入!");
			errorMsg.put("CS_MSG_000010", "折扣不能为0，请重新输入!");
			errorMsg.put("CS_MSG_000011", "该数据已被别人修改或删除!");
			errorMsg.put("CS_MSG_000012", "输入参数为空或格式不正确，请重试!");
			errorMsg.put("CS_MSG_000013", "调用服务时的参数不正确!");
			errorMsg.put("CS_MSG_000014", "服务器中断，请联系客服!");
			errorMsg.put("CS_MSG_000015", "开始日期不能为空，请重新输入!");
			errorMsg.put("CS_MSG_000016", "结束日期不能为空，请重新输入!");
			errorMsg.put("CS_MSG_000017", "开始日期必须小于结束日期，且结束日期大于本日，请重新输入!");
			errorMsg.put("CS_MSG_000018", "上传的文件类型不支持!");
			errorMsg.put("CS_MSG_000019", "会话状态失效，请重新登录!");	
			
			/*
			 * 	##收银台
				CD_MSG_000001=请输入需要退货的单号!
				CD_MSG_000002=退货单号不存在，请重新输入!
				CD_MSG_000003=是否要从云端继续下载数据同步到此设备？
				CD_MSG_000004=确定要上传营业数据到云端吗？
				CD_MSG_000005=今日的营业数据不能删除!
				CD_MSG_000006=确定要删除 时间1-时间2的营业数据吗？
				CD_MSG_000007=实退金额不能大于应退金额!
				CD_MSG_000008=获取数据失败，请检查网络!
				CD_MSG_000009=编码格式不正确，请重新输入!
				CD_MSG_000010=邮箱格式不正确，请重新输入!
				CD_MSG_000011=手机号格式不正确，请重新输入!
				CD_MSG_000012=验证码输入不正确，请重新输入!
				CD_MSG_000013=开始日期必须小于等于结束日期，请重新输入!
				CD_MSG_000014=使用条件不能为空，且必须大于0，请重新输入！
				CD_MSG_000015=出券条件不能为空，且必须大于0，请重新输入！
				CD_MSG_000016=该表信息无效！
				CD_MSG_000017=该活动状态无效！
				CD_MSG_000018=该活动已过期！
				CD_MSG_000019=该活动会员专享！
				CD_MSG_000020=使用条件不满足，无法使用优惠券！
				CD_MSG_000021=该优惠券已使用或者无效！
				CD_MSG_000022=优惠券番号表插入失败！
				CD_MSG_000023=优惠券出券条件不满足，无法出券！
			 * 
			 * 
			 */
			
			errorMsg.put("CD_MSG_000001", "请输入需要退货的单号!");
			errorMsg.put("CD_MSG_000002", "退货单号不存在，请重新输入!");
			errorMsg.put("CD_MSG_000003", "是否要从云端继续下载数据同步到此设备？");
			errorMsg.put("CD_MSG_000004", "确定要上传营业数据到云端吗？");
			errorMsg.put("CD_MSG_000005", "今日的营业数据不能删除!");
			errorMsg.put("CD_MSG_000006", "确定要删除 时间1-时间2的营业数据吗？");
			errorMsg.put("CD_MSG_000007", "实退金额不能大于应退金额!");
			errorMsg.put("CD_MSG_000008", "获取数据失败，请检查网络!");
			errorMsg.put("CD_MSG_000009", "编码格式不正确，请重新输入!");
			errorMsg.put("CD_MSG_000010", "邮箱格式不正确，请重新输入!");
			errorMsg.put("CD_MSG_000011", "手机号格式不正确，请重新输入!");
			errorMsg.put("CD_MSG_000012", "验证码输入不正确，请重新输入!");
			errorMsg.put("CD_MSG_000013", "开始日期必须小于等于结束日期，请重新输入!");			
			errorMsg.put("CD_MSG_000014", "使用条件不能为空，且必须大于0，请重新输入！");
			errorMsg.put("CD_MSG_000015", "出券条件不能为空，且必须大于0，请重新输入！");
			errorMsg.put("CD_MSG_000016", "该表信息无效！");
			errorMsg.put("CD_MSG_000017", "该活动状态无效！");
			errorMsg.put("CD_MSG_000018", "该活动已过期！");
			errorMsg.put("CD_MSG_000019", "该活动会员专享！");
			errorMsg.put("CD_MSG_000020", "使用条件不满足，无法使用优惠券！");
			errorMsg.put("CD_MSG_000021", "该优惠券已使用或者无效！");
			errorMsg.put("CD_MSG_000022", "优惠券番号表插入失败！");
			errorMsg.put("CD_MSG_000023", "优惠券出券条件不满足，无法出券！");

			/**
			 * 
				##门店连锁相关
				SC_MSG_000001=商户名称不能为空，请输入!
				SC_MSG_000002=店家代码不能为空，请输入!
				SC_MSG_000003=店家代码已存在，请重新设置!
				SC_MSG_000004=上级店家不能为空，请输入!
				SC_MSG_000005=店家类型不能为空，请选择!
				SC_MSG_000006=所在地区不能为空，请输入!
				SC_MSG_000007=请选择需要复制的门店!
				SC_MSG_000008=员工工号不能为空，请输入!
				SC_MSG_000009=员工工号已存在，请重新设置!
				SC_MSG_000010=请选择员工所属门店!
				SC_MSG_000011=请选择员工所属角色!
				SC_MSG_000012=确定要删除店家吗？
				SC_MSG_000013=此店家已有营业数据，确定要删除吗？
				SC_MSG_000014=使用期已过或被停用，请与系统服务商联系！
				SC_MSG_000015=商户名称已经存在，请使用其他名称。
				SC_MSG_000016=被复制商户商品数据不存在，请重新选择。
				SC_MSG_000017=该商户存在库存信息，不能删除!
				SC_MSG_000018=该商户存在订单信息，不能删除!
				SC_MSG_000019=商户代码不正确，找不到该商户!
				SC_MSG_000020=该商户尚未初始化,请与软件提供商联系!
				SC_MSG_000021=该商户的初始密码不正确，请与软件提供商联系!
				SC_MSG_000022=您的机器硬件发生变动，需要重置绑定信息，请与软件提供商联系!
				SC_MSG_000023=该商户尚未绑定，请与软件提供商联系!
				SC_MSG_000024=短信数量不足！
				SC_MSG_000025=该门店存在下属门店，不能删除！
				SC_MSG_000026=该门店存在员工，不能删除！
			 * 
			 */
			
			errorMsg.put("SC_MSG_000001", "商户名称不能为空，请输入!");
			errorMsg.put("SC_MSG_000002", "店家代码不能为空，请输入!");
			errorMsg.put("SC_MSG_000003", "店家代码已存在，请重新设置!");
			errorMsg.put("SC_MSG_000004", "上级店家不能为空，请输入!");
			errorMsg.put("SC_MSG_000005", "今店家类型不能为空，请选择!");
			errorMsg.put("SC_MSG_000006", "所在地区不能为空，请输入!");
			errorMsg.put("SC_MSG_000007", "实请选择需要复制的门店!");
			errorMsg.put("SC_MSG_000008", "员工工号不能为空，请输入!");
			errorMsg.put("SC_MSG_000009", "员工工号已存在，请重新设置!");
			errorMsg.put("SC_MSG_000010", "请选择员工所属门店!");
			errorMsg.put("SC_MSG_000011", "请选择员工所属角色!");
			errorMsg.put("SC_MSG_000012", "确定要删除店家吗？");
			errorMsg.put("SC_MSG_000013", "此店家已有营业数据，确定要删除吗？");			
			errorMsg.put("SC_MSG_000014", "使用期已过或被停用，请与系统服务商联系！");
			errorMsg.put("SC_MSG_000015", "商户名称已经存在，请使用其他名称。");
			errorMsg.put("SC_MSG_000016", "被复制商户商品数据不存在，请重新选择。");
			errorMsg.put("SC_MSG_000017", "该商户存在库存信息，不能删除!");
			errorMsg.put("SC_MSG_000018", "该商户存在订单信息，不能删除!");
			errorMsg.put("SC_MSG_000019", "商户代码不正确，找不到该商户!");
			errorMsg.put("SC_MSG_000020", "该商户尚未初始化,请与软件提供商联系!");
			
			errorMsg.put("SC_MSG_000021", "该商户的初始密码不正确，请与软件提供商联系!");
			errorMsg.put("SC_MSG_000022", "您的机器硬件发生变动，需要重置绑定信息，请与软件提供商联系!");
			errorMsg.put("SC_MSG_000023", "该商户尚未绑定，请与软件提供商联系!");
			errorMsg.put("SC_MSG_000024", "短信数量不足！");
			errorMsg.put("SC_MSG_000025", "该门店存在下属门店，不能删除！");
			errorMsg.put("SC_MSG_000026", "该门店存在员工，不能删除！");					
					
			
		}
		
		
		
		public static final String  UM_MSG_000001="UM_MSG_000001";
		public static final String	UM_MSG_000002="UM_MSG_000002";
		public static final String	UM_MSG_000003="UM_MSG_000003";
		public static final String	UM_MSG_000004="UM_MSG_000004";
		public static final String	UM_MSG_000005="UM_MSG_000005";
		public static final String	UM_MSG_000006="UM_MSG_000006";
		public static final String	UM_MSG_000007="UM_MSG_000007";
		public static final String	UM_MSG_000008="UM_MSG_000008";
		public static final String	UM_MSG_000009="UM_MSG_000009";
		public static final String	UM_MSG_000010="UM_MSG_0000010";
		public static final String	UM_MSG_000011="UM_MSG_0000011";
		public static final String	UM_MSG_000012="UM_MSG_0000012";
		public static final String	UM_MSG_000013="UM_MSG_0000013";
		public static final String	UM_MSG_000014="UM_MSG_0000014";
		
		public static int getEorMsgId(String str){
			int ret =0;
			
			if(str.equalsIgnoreCase(UM_MSG_000001)){
				ret =1;
			}else if(str.equalsIgnoreCase(UM_MSG_000002)){
				ret =2;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000003)){
				ret =3;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000004)){
				ret =4;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000005)){
				ret =5;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000006)){
				ret =6;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000007)){
				ret =7;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000008)){
				ret =8;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000009)){
				ret =9;
			}
		else if(str.equalsIgnoreCase(UM_MSG_000010)){
				ret =10;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000011)){
				ret =11;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000012)){
				ret =12;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000013)){
				ret =13;
			}
			else if(str.equalsIgnoreCase(UM_MSG_000014)){
				ret =14;
			}			
			return ret;
		}
		
		public static String getErrorMsg(String str){
			String ret = "未知错误";
			initErrorMsg();
			Iterator iter = errorMsg.entrySet().iterator();
			
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				//Log.i("ErrorMsg","str = "+str +" entry = "+entry.getKey().toString());
				if(str.equalsIgnoreCase(entry.getKey().toString())){
					ret = entry.getValue().toString();
					break;
				}
			
				
			}
			
			
			return  ret;
		}
		

}
