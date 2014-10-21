/**
 * 
 */
package com.dfire.retail.app.manage.global;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author qiuch
 * 
 */
public class Constants {
	public static final String BASE_URL = "http://114.215.178.171:8080/retail/serviceCenter/api/";

	public static final String SESSIONID = "1000";
	public static final String VERSION = "level_r_1";

	/**
	 * 标记是否打印log信息
	 */
	public static final boolean DEBUG = true;
	/**
	 * 标记是否使用测试数据
	 */
	public static final boolean TEST = true;
	
	// 登陆
	public static final String LOGIN = BASE_URL +"login?";
	//用户初始化
	public static final String EMPLOYEE_INFO_INIT = BASE_URL + "user/init?";
	//用户一览查询
	public static final String EMPLOYEE_INFO_LIST = BASE_URL + "user/list?";
	
	//修改密码
	public static final String CHANGE_PASSWORD = BASE_URL +"password/change";
	
	//修改店铺商业模式
	public static final String CHANGE_SHOPTYPE = BASE_URL +"entityMode/change";
	
	// 后台主页面
	public static final String MAINPAGE = BASE_URL +"income/msg";
	
	// 根据shopId获取商店名称
	public static final String SHOPDETAILBYCODE = BASE_URL +"shop/detailByCode";

	// 根据shop init
	public static final String SHOPINIT = BASE_URL +"shop/init";
	
	// shop list
	public static final String SHOPLIST = BASE_URL +"shop/list";
	
	// 会员信息汇总
	public static final String SUMMARIZING = BASE_URL
			+ "customer!summarizing.action";
	// 会员信息查询
	public static final String MEMBER_INFO_SEARCH = BASE_URL
			+ "customer!list.action";
	// 会员信息详情
	public static final String MEMBER_INFO_DETAIL = BASE_URL
			+ "customer!findById.action";
	// 保存会员信息
	public static final String SAVE_MEMBER_INFO = BASE_URL
			+ "customer!save.action";
	// 会员卡交易查询
	public static final String CARD_TRAN_SEARCH = BASE_URL
			+ "customerDeal!card.action";
	// 会员卡交易明细
	public static final String CARD_TRAN_DETAIL = BASE_URL
			+ "customerDeal!cardDetail.action";
	// 会员交易查询
	public static final String MEMBER_TRAN_SEARCH = BASE_URL
			+ "customerDeal!list.action";
	// 会员信息管理
	public static final String VEASION = "5";// 版本
	public static final String INTENT_EXTRA_KEYWORDS = "INTENT_EXTRA_KEYWORDS";// INTENT传值，会员卡/姓名/手机号/卡类型
	public static final String INTENT_EXTRA_CARD_TYPE = "INTENT_EXTRA_CARD_TYPE";// INTENT传值，会员卡类型
	// 优惠券列表
	public static final String COUPON_LIST = BASE_URL + "coupon!list.action";
	// 优惠券详情
	public static final String COUPON_DETAIL = BASE_URL
			+ "coupon!detail.action";
	// 换购促销
	public static final String SALESSWAP_LIST = BASE_URL
			+ "salesSwap!list.action";
	// 换购促销详情
	public static final String SALESSWAP_DETAIL = BASE_URL
			+ "salesSwap!detail.action";
	// 充值促销
	public static final String RECHARGERULE_LIST = BASE_URL
			+ "rechargeRule!list.action";
	// 充值促销详情
	public static final String RECHARGERULE_DETAIL = BASE_URL
			+ "rechargeRule!detail.action";
	// 促销套餐
	public static final String SALES_COMBO_LIST = BASE_URL
			+ "salesCombo!list.action";
	// 促销套餐详情
	public static final String SALES_COMBO_DETAIL = BASE_URL
			+ "salesCombo!detail.action";
	// 满减满送
	public static final String SALES_MATCH_LIST = BASE_URL
			+ "salesMatch!list.action";
	// 满减满送详情
	public static final String SALES_MATCH_DETAIL = BASE_URL
			+ "salesMatch!detail.action";
	// 参数传递
	public static final String INTENT_MENBER_NAME = "intet_menber_name";
	public static final String INTENT_CUSTOMERID = "intet_customerid";
	public static final String INTENT_CUSTOMER_NAME = "INTENT_CUSTOMER_NAME";// 会员姓名
	public static final String INTENT_CARD_ID = "INTENT_CARD_ID";// 会员卡号
	public static final String INTENT_RECHARGERULE_NAME = "intet_rechargerule_name";
	public static final String INTENT_RECHARGERULE_ID = "intet_rechargerule_id";
	public static final String INTENT_COUPON_NAME = "intet_coupon_name";
	public static final String INTENT_COUPONID = "intet_couponid";
	public static final String INTENT_EVENT_NAME = "intet_event_name";
	public static final String INTENT_SALESCOMBO_NAME = "intet_salescombo_name";
	public static final String INTENT_SALESCOMBO_ID = "intet_salescombo_id";
	public static final String INTENT_SALESSWAP_NAME = "intet_salesswap_name";
	public static final String INTENT_SALESSWAP_ID = "intet_salesswap_id";
	public static final String INTENT_SALESMATCH_NAME = "intet_salesmatch_name";
	public static final String INTENT_SALESMATCH_ID = "intet_salesmatch_id";
	public static final String INTENT_SALESMATCH_LABLE = "intet_salesmatch_lable";// 满减满送区分lable
	
	public static final String INTENT_ORDER_ID = "INTENT_ORDER_ID";// 订单id
	public static final String INTENT_RECORD_NUM = "INTENT_RECORD_NUM";// 第几个记录
	public static final String INTENT_ORDER_CODE = "INTENT_ORDER_CODE";// 订单流水号
	public static final String INTENT_ORDER_DATE = "INTENT_ORDER_DATE";// 订单交易日期
	public static final String INTENT_ORDER_CONSUME = "INTENT_ORDER_CONSUME";// 订单交易金额
	public static final String INTENT_CARD_BALANCE = "INTENT_CARD_BALANCE";// 卡内余额
	public static final String INTENT_GET_POINT = "INTENT_GET_POINT";// 获得积分
	
    //定义网络连接的时间
    public static final int CONNECT_TIMEOUT = 1000 * 10;
    public static final int READ_TIMEOUT = 1000 * 20;
    
    //连接默认参数
	public static final String result_success = "0";

	public static final String SHARED_PRENFENCE_NAME = "retail";
	public static final String SHARED_PRENFENCE_ISFIRST = "retail_isfirst_use";
	public static final String SP_LOGIN_SHOPID = "login_shopid";
	public static final String SP_LOGIN_USERNAME = "login_username";
	public static final String SP_LOGIN_PASSWORD = "login_password";
	public static final String SP_LOGIN_SAVE_USERNAME = "login_save_username";
	public static final String SP_LOGIN_AUTO_LOGIN = "login_auto_login";
	
	//网络请求成功 
	public static final int HANDLER_SUCESS = 1;
	//网络请求失败
	public static final int HANDLER_FAIL = 2;
	
	//网络请求出错
	public static final int HANDLER_ERROR = 3;
	
	//请求返回Code值
	public static final String REPONSE_FAIL="fail";
	public static final String SUCCESS="scuess";
	
	//add by luzheqi
	
	//url
	public static final String CATEGORY_SAVE_URL = BASE_URL + "category/save";
	public static final String CATEGORY_DELETE_URL = BASE_URL + "category/delete";
	public static final String CATEGORY_LIST_URL = Constants.BASE_URL+"category/list";
	public static final String GOODS_COUNT_URL = Constants.BASE_URL + "goods/categoryCount";
	public static final String SHOP_LIST_URL = Constants.BASE_URL + "shop/list";
	public static final String GOODS_LIST_URL = Constants.BASE_URL + "goods/list";
	public static final String SAVE_CONFIG_URL = Constants.BASE_URL + "config/setting";
	public static final String GET_CONFIG_DETAIL = Constants.BASE_URL + "config/detail";
	public static final String DELETE_URL = Constants.BASE_URL + "goods/delete";
	public static final String GOODS_SAVE_URL = BASE_URL + "goods/save";
	public static final String SAVE_SETTING_BATCH_URL = BASE_URL + "salesset/set";
	public static final String GET_RECEIPT = BASE_URL  + "receipt/selectData";
	public static final String SAVE_RECEIPT_URL = BASE_URL  + "receipt/setting";
	
	public static final String GOODS_ASSEMBLE_URL = BASE_URL + "assemble/list";
	public static final String GOODS_ASSEMBLE_SAVE_URL = BASE_URL + "assemble/save";
	public static final String GOODS_ASSEMBLE_DELETE_URL = BASE_URL + "assemble/delete";
	public static final String GOODS_ASSEMBLE_DETAIL_URL = BASE_URL + "assemble/detail";
	public static final String GOODS_ASSEMBLE_CHOICE_URL = BASE_URL + "assemble/choice";
	
	public static final String GOODS_SPLIT_URL = BASE_URL + "split/list";
	public static final String GOODS_SPLIT_SAVE_URL = BASE_URL + "split/save";
	public static final String GOODS_SPLIT_DELETE_URL = BASE_URL + "split/delete";
	public static final String GOODS_SPLIT_DETAIL_URL = BASE_URL + "split/detail";
	public static final String GOODS_SPLIT_CHOICE_URL = BASE_URL + "split/choice";
	
	public static final String GOODS_PROCESSING_URL = BASE_URL +        "processing/list";
	public static final String GOODS_PROCESSING_SAVE_URL =   BASE_URL + "processing/save";
	public static final String GOODS_PROCESSING_DELETE_URL = BASE_URL + "processing/delete";
	public static final String GOODS_PROCESSING_DETAIL_URL = BASE_URL + "processing/detail";
	public static final String GOODS_PROCESSING_CHOICE_URL = BASE_URL + "processing/choice";
	
	//parameters name
	public static final String OPT_TYPE= "operateType";
	public static final String SHOP_ID = "shopId";
	public static final String PAGE = "currentPage";
	public static final String CATEGORY_ID = "categoryId";
	public static final String SEARCH_CODE = "searchCode";
	public static final String NEGATIVE_STORE_STATUS = "negativestoreStatus";
	public static final String REMNANE_MODEL = "remnantModel";
	public static final String GOODS_ID = "goodsId";
	public static final String GOODSIDS = "goodsIdArray";
	public static final String SALES_SETTING = "salesSet";
	public static final String RECEIPT = "receiptStyle";
	
	//parameters
	public static final String ADD = "add";
	public static final String EDIT = "edit";
	public static final String SAVE = "save";
	
	
	//return
	public static final String RETURN_CODE = "returnCode";
	public static final String LSUCCESS = "success";
	public static final String EXCEPTAION_CODE = "exceptionCode";
	public static final String CATEGORY_LIST = "categoryList";
	public static final String COUNT = "count";
	public static final String PAGE_SIZE = "pageSize";
	public static final String SHOP_LIST = "shopList";
	public static final String GOODS_LIST = "goodsList";
	public static final String GOODS_HANDLERS_LIST = "goodsHandleList";
	public static final String GOODS_HANDLE = "goodsHandle";
	
	

	//exception code
	public static final String GOODS_ERROR_13="GM_MSG_000013";
	public static final String GOODS_ERROR_14="GM_MSG_000014";
	public static final String GOODS_ERROR_15="GM_MSG_000015";
	public static final String GOODS_ERROR_25="GM_MSG_000025";
	public static final String COMMON_ERROR_14="CS_MSG_000012";
	
	
	//value category detail
	public static final String EMPTY_STRING = "";
	public static final String NECESSARY = "必填";
	public static final String NOT_NECESSARY = "可不填";
	public static final String CATEGORY_NAME = "商品分类名称";
	public static final String CATEGORY_CODE = "商品分类编码";
	public static final String EDIT_SUCCESS = "修改成功";
	public static final String ADD_SUCCESS = "添加成功";
	public static final String EDIT_FAIL = "修改失败";
	public static final String ADD_FAIL = "添加失败";
	public static final String GOODS_SORT = "商品分类";
	public static final String WAIT_SAVE_CATEGORY = "正在保存分类...";
	public static final String WAIT_DELETE_CATEGORY = "正在删除分类...";
	
	//value category list
	public static final int CATEGORY_DEPTH = 3;
	public static final String CATEGORY = "category";
	public static final String MODE = "mode";
	public static final String PARENT_CATEGORY = "parentCategory";
	public static final String WAIT_GET_CATEGORY = "正在获取分类...";
	

	//value goods search acitvity
	public static final String GOODS_TITLE = "商品";
	public static final String CONNECTOR = "-";
	public static final String WAIT_SEARCH_GOODS = "正在搜索商品...";
	public static final String DEVICE_CODE = "deviceCode";
	public static final String NO_GOODS = "该分类下暂无商品";
	public static final int PAGE_SIZE_OFFSET = 1;
	public static final String SHOP = "shop";
	public static final String GOODS = "goods";
	
	//value goods detail
	public static final String PNG = ".png";
	public static final String SEARCH_STATUS = "searchStatus";
	public static final String BAR_CODE = "商品条码";
	public static final String TOTAL_PAGE = "totalPage";
	public static final String GOODS_CODE = "商品代码";
	public static final String GOODS_NAME = "商品名称";
	public static final String GOODS_JINHUOJIA = "进货价（元）";
	public static final String GOODS_LINGSHOUJIA = "零售价（元）";
	public static final String GOODS_KUCUN = "库存数";
	public static final String GOODS_TONGBU = "商品同步";
	public static final String GOODS_JIANMA = "简码";
	public static final String GOODS_PINYIN = "拼音码";
	public static final String GOODS_CATEGORY = "商品分类";
	public static final String GOODS_GUIGE = "规格";
	public static final String GOODS_PINGPAI = "品牌";
	public static final String GOODS_CHANDI = "产地";
	public static final String GOODS_BAOZHIQI = "保质期(天)";
	public static final String GOODS_IMAGE = "商品图片";
	public static final String GOODS_TICHENG = "销售提成比例(%)";
	public static final String GOODS_YOUHUI = "不参任何活动";
	public static final String GOODS_JIFEN = "不参与积分";
	public static final int INVALID_INT = -1;
	public static final CharSequence DELETE = "删除";
	public static final CharSequence CONTINUS_ADD = "继续添加";
	public static final String DAY = "天";
	public static final String INPUT_GOODS_NAME = "请输入商品名称";
	public static final String INPUT_GOODS_LINGSHOUJIA = "请输入商品零售价";
	public static final String INPUT_RIGHT_LINGSHOUJI = "请输入正确的商品零售价";
	public static final String INPUT_RIGHT_JINHUO = "请输入正确的进货价";
	public static final String INPUT_RIGHT_SHOUJIA = "请输入正确零售价";
	public static final String INPUT_RIGHT_TIAOMA = "请输入正确的16位条码";
	public static final String INPUT_RIGHT_DAIMA = "请输入正确的16位代码";
	public static final String GOODS_PERIOD = "请输入正确的保质期";
	public static final String INPUT_RIGHT_KUCUN = "请输入正确的库存";
	public static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";
	public static final String NO_TONGBU = "不同步";
	public static final String TONGBU = "同步所有";
	public static final String TEMPFILE = "fileNeedToUpload";
	public static final String DELETE_SUCCESS = "删除成功";
	public static final String DELETE_FAIL = "删除失败";
	public static final CharSequence WHERE_IMAGE = "从哪里获取图片";
	public static final CharSequence[] GET_IMAGE_METHOD = new CharSequence[] { "相册", "相机" };
	public static final String IMAGE_INTENT = "image/*";
	public static final String OUTPUT_FORMAT = "outputFormat";
	public static final String CHOOSE_SOMETHING = "请先选择商品";

	//setting
	public static final CharSequence WAIT_SETTING = "正在获取系统参数...";
	public static final String UNKNOW_ERROR = "未知错误";
	public static final String SAVE_SETTING_SUCCESS = "保存系统设置成功";

	//batch
	public static final String CHOOSE = "请先选择商品";
	public static final String WAIT_DELETE_BATCH = "正在删除...";
	public static final String IS_OPEN = "是否启用";
	public static final String ZHEKOU = "折扣(%)";
	public static final String BEGIN_TIME = "开始时间";
	public static final String CLICK_CHOOSE = "点击选择";
	public static final String END_TIME = "开始时间";
	public static final String HUIYUAN = "会员专享";
	public static final String INPUT_TICHENG = "请输入提成百分比";
	public static final String INPUT_RIGHT_TICHENG = "请输入正确的提成百分比";
	public static final String WAIT_SAVE_SET = "正在保存销售设置...";
	public static final String INPUT_ZHEKOU = "请输入折扣百分比";
	public static final String INPUT_RIGHT_ZHEKOU = "请输入正确的折扣百分比";
	public static final String INPUT_RIGHT_TIME = "请输入正确的启止时间";


	//goods process
	public static final String INPUT_SEARCH_CODE = "请输入搜索条件";
	public static final String SHOW_LOGO = "店家logo";
	public static final String RECEIPT_TITLE = "小票标题";
	public static final String BOTTOM_CONTENT = "尾注";
	public static final String RECEIPT_SPECIFIC = "小票规格";
	public static final String CHOOSE_TEMPLATE = "选择模板";
	public static final String INPUT_RECEIPT_TITLE = "请输入小票标题";
	public static final String INPUT_RECEIPT_BOTTTOM = "请输入尾注";
	public static final CharSequence WAIT_SAVE_RECEIPT = "正在保存小票设置...";
	public static final String SAVE_RECEIPT_SUCCESS = "保存小票设置成功";
	public static final String SALES_SAVE_SUCCESS = "保存销售设置成功";
	public static final String GOODS_SAVE_SUCCESS = "保存商品信息成功";

	//split assemble precess
	public static final String WAIT_SEARCH_RULE = "正在获取规则...";
	public static final String BIG_GOODS = "大件商品";
	public static final String CHOOSE_BIG_GOODS = "选择大件商品...";
	
	public static final String SMALL_GOODS = "小件商品";
	public static final String CHOOSE_SMALL_GOODS = "添加小件商品...";
	
	
	public static final String BEFORE_GOODS = "原料商品";
	public static final String CHOOSE_BEFORE_GOODS = "添加原料商品...";
	public static final String AFTER_GOODS = "加工商品";
	public static final String CHOOSE_AFTER_GOODS = "选择加工商品...";
	public static final int REQUEST_CODE_FOR_SCAN = 10086;
	public static final String OPT_GOODS_ID = "newGoodsId";
	public static final CharSequence WAIT_OPT_DETAIL = "正在获取规则详情...";
	public static final CharSequence WAIT_DELETE_OPT = "正在删除规则...";
	public static final CharSequence CHOOSE_GOODS = "选择商品";
	public static final CharSequence WAIT_SAVE_RULE = "正在保存规则...";
	public static final String SAVE_RULE_SUCCESS = "保存规则成功";
	public static final String INPUT_RIGHT_NUM = "请输入正确的数目";
	public static final String INPUT = "请输入";
	public static final CharSequence DELETEP = "是否删除该商品";
	public static final CharSequence YES = "是";
	public static final CharSequence NO = "否";
	public static final CharSequence ONE = "1";
	public static final Object ZERO = "0";






	public static String getErrorInf(String input, String defaultMessage){
		if(input == null) {
			if (defaultMessage == null) {
				return "未知错误";
			}else{
				return defaultMessage;
			}
		}else if (input.equals(Constants.GOODS_ERROR_14)) {
			return "删除商品分类失败,请先删除其子分类";
		}else if(input.equals(Constants.GOODS_ERROR_15)){
			return "删除商品分类失败,请先删除该分类下的商品";
		}else if (input.equals(Constants.GOODS_ERROR_13)) {
			return "保存商品分类失败，商品分类名称已存在";
		}else if (input.equals(Constants.GOODS_ERROR_25)) {
			return "保存商品分类失败，分类编码已存在";
		}else if (input.equals(Constants.COMMON_ERROR_14)) {
			return "请输入正确的数据";
		}else{
			if (defaultMessage == null) {
				return "未知错误";
			}else{
				return defaultMessage;
			}
		}
	}

}