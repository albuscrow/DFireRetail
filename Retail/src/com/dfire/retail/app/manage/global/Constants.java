/**
 * 
 */
package com.dfire.retail.app.manage.global;


/**
 * @author qiuch
 * 
 */
public class Constants {
//	public static final String BASE_URL = "http://10.1.4.254:8080/retail/serviceCenter/api/";
	public static final String BASE_URL = "http://114.215.178.171:8080/retail/serviceCenter/api/";
//	public static final String BASE_URL = "http://10.1.6.131/serviceCenter/api/";
//    public static final String BASE_URL = "http://10.1.2.36:8080/retail/serviceCenter/api/";

//	public static final String BASE_URL = "http://10.1.4.240:8080/serviceCenter/api/";

	
	
	public static final String SESSIONID = "1000";
	public static final String VERSION = "level_r_1";

	/**
	 * 标记是否打印log信息
	 */
	public static final boolean DEBUG = true;
	/**
	 * 标记是否使用测试数据
	 */
	public static final boolean TEST = false;

	// 登陆
	public static final String LOGIN = BASE_URL + "login?";
	// 用户初始化
	public static final String EMPLOYEE_INFO_INIT = BASE_URL + "user/init?";
	// 用户一览查询
	public static final String EMPLOYEE_INFO_LIST = BASE_URL + "user/list?";
	
	//用户信息保存
	public static final String EMPLOYEE_INFO_SAVE = BASE_URL + "user/save";
	//用户信息保存
	public static final String EMPLOYEE_INFO_DETAIL = BASE_URL + "user/detail";
	//用户删除
	public static final String EMPLOYEE_INFO_DELETE = BASE_URL + "user/delete";
	

	// 修改密码
	public static final String CHANGE_PASSWORD = BASE_URL + "password/change";

	// 修改店铺商业模式
	public static final String CHANGE_SHOPTYPE = BASE_URL + "entityMode/change";

	// 后台主页面
	public static final String MAINPAGE = BASE_URL + "income/msg";

	// 根据shopId获取商店名称
	public static final String SHOPDETAILBYCODE = BASE_URL + "shop/detailByCode";

	// 根据shop init
	public static final String SHOPINIT = BASE_URL + "shop/init";

	// shop list
	public static final String SHOPLIST = BASE_URL + "shop/list";
	// shop delete
	public static final String SHOPDELETE = BASE_URL + "shop/delete";

	//公司 下面的所有门店
	public static final String SHOPALLSHOPLIST = BASE_URL + "shop/allshoplist";
	
	//所有门店 包含公司
	public static final String  ALL_SHOPLIST_INCLUDECOMPANY = BASE_URL +"shop/allshoplistincludecompany";
	
	// 保存门店信息接口
	public static final String SHOPSAVE = BASE_URL +"shop/save";
	//重置密码
	public static final String RESTPASSWORD = BASE_URL +"user/changePwd";
	
	// 会员信息汇总
	public static final String SUMMARIZING = BASE_URL + "customer!summarizing.action";
	// 会员信息查询
	public static final String MEMBER_INFO_SEARCH = BASE_URL + "customer!list.action";
	// 会员信息详情
	public static final String MEMBER_INFO_DETAIL = BASE_URL + "customer!findById.action";
	// 保存会员信息
	public static final String SAVE_MEMBER_INFO = BASE_URL + "customer!save.action";
	// 会员卡交易查询
	public static final String CARD_TRAN_SEARCH = BASE_URL + "customerDeal!card.action";
	// 会员卡交易明细
	public static final String CARD_TRAN_DETAIL = BASE_URL + "customerDeal!cardDetail.action";
	// 会员交易查询
	public static final String MEMBER_TRAN_SEARCH = BASE_URL + "customerDeal!list.action";
	// 会员信息管理
	public static final String VEASION = "5";// 版本
	public static final String INTENT_EXTRA_KEYWORDS = "INTENT_EXTRA_KEYWORDS";// INTENT传值，会员卡/姓名/手机号/卡类型
	public static final String INTENT_EXTRA_CARD_TYPE = "INTENT_EXTRA_CARD_TYPE";// INTENT传值，会员卡类型
	// 优惠券列表
	public static final String COUPON_LIST = BASE_URL + "coupon!list.action";
	// 优惠券详情
	public static final String COUPON_DETAIL = BASE_URL + "coupon!detail.action";
	// 换购促销
	public static final String SALESSWAP_LIST = BASE_URL + "salesSwap!list.action";
	// 换购促销详情
	public static final String SALESSWAP_DETAIL = BASE_URL + "salesSwap!detail.action";
	// 充值促销
	public static final String RECHARGERULE_LIST = BASE_URL + "rechargeRule!list.action";
	// 充值促销详情
	public static final String RECHARGERULE_DETAIL = BASE_URL + "rechargeRule!detail.action";
	// 促销套餐
	public static final String SALES_COMBO_LIST = BASE_URL + "salesCombo!list.action";
	// 促销套餐详情
	public static final String SALES_COMBO_DETAIL = BASE_URL + "salesCombo!detail.action";
	// 满减满送
	public static final String SALES_MATCH_LIST = BASE_URL + "salesMatch!list.action";
	// 满减满送详情
	public static final String SALES_MATCH_DETAIL = BASE_URL + "salesMatch!detail.action";

	// 物流管理-门店退货

	public static final String RETURN_GOODS_LIST = BASE_URL + "returnGoods/list";
	
	
	// 物流管理-门店调拨

	public static final String ALLOCATE_LIST = BASE_URL + "allocate/list";
	
	// 物流管理-门店调拨状态一览

	public static final String ALLOCATE_STATUSLIST = BASE_URL + "allocate/statusList";
	
	// 物流管理-门店退货状态一览

	public static final String RETURNGOODS_STATUSLIST = BASE_URL + "returnGoods/statusList";
	
	// 供应商

	public static final String SUPPLY_INFO_MANAGE_LIST = BASE_URL + "supplyinfoManage/list";
	
	public static final String SUPPLY_INFO = BASE_URL+"supplyinfoManage/supplyDetail";

	public static final String SUPPLY_INFO_MANAGE_ADD = BASE_URL + "supplyinfoManage/addSaveSupply";
	
	public static final String SUPPLY_INFO_MANAGE_UPDATE = BASE_URL + "supplyinfoManage/updateSaveSupply";
	
	public static final String SUPPLY_INFO_MANAGE_GET_TYPE = BASE_URL + "supplyinfoManage/getSupplyType";
	
	public static final String ADD_SUPPLY_TYPE = BASE_URL + "supplyinfoManage/addSupplyType";
	
	public static final String SUPPLY_INFO_MANAGE_DELETE = BASE_URL + "supplyinfoManage/delSupply";


	// 门店退货保存
	public static final String PURCHASE_SAVE = BASE_URL + "returnGoods/save";

	// 门店退货理由
	public static final String RETURNGOODS_REASONLIST = BASE_URL + "returnGoods/resonList";

	// 门店退货详细
	public static final String RETURNGOODS_DETAIL = BASE_URL + "returnGoods/detail";

	
	// 门店调拨详细
	public static final String ALLOCATE_DETAIL = BASE_URL + "allocate/detail";
	
	// 门店理由添加

	public static final String RETURNGOODS_REASON_ADD = BASE_URL + "returnGoods/resonAdd";
	
	// 物流查询一览
	public static final String LOGISTICS_LIST = BASE_URL + "logistics/list";
	// 物流查询详细
	public static final String LOGISTICS_DETAIL = BASE_URL + "logistics/detail";

	// 门店退货保存
	public static final String RETURN_GOODS_SAVE = BASE_URL + "returnGoods/save";
	// 门店调拨保存
	public static final String ALLOCATE_SAVE = BASE_URL + "allocate/save";
	
	
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

	// 定义网络连接的时间
	public static final int CONNECT_TIMEOUT = 1000 * 10;
	public static final int READ_TIMEOUT = 1000 * 20;

	// 连接默认参数
	public static final String result_success = "0";

	public static final String SHARED_PRENFENCE_NAME = "retail";
	public static final String SHARED_PRENFENCE_ISFIRST = "retail_isfirst_use";
	public static final String SP_LOGIN_SHOPID = "login_shopid";
	public static final String SP_LOGIN_USERNAME = "login_username";
	public static final String SP_LOGIN_PASSWORD = "login_password";
	public static final String SP_LOGIN_SAVE_USERNAME = "login_save_username";
	public static final String SP_LOGIN_AUTO_LOGIN = "login_auto_login";

	// 网络请求成功
	public static final int HANDLER_SUCESS = 1;
	// 网络请求失败
	public static final int HANDLER_FAIL = 2;

	// 网络请求出错
	public static final int HANDLER_ERROR = 3;
	// 网络请求出错
	public static final int HANDLER_SEARCH = 4;

	// 请求返回Code值
	public static final String REPONSE_FAIL = "fail";
	public static final String SUCCESS = "success";

	// add by luzheqi

	// url
	
	//物流操作标示 ORDER_ADD 新生订单 ,UNRECOGNIZED 未确定订单,CONFIRMATION 总部登陆  确认订单,CONFIRMATION_AFTER 确认后订单查看详情 ,
	public static final String ORDER_ADD = "1",UNRECOGNIZED = "2",CONFIRMATION = "3",CONFIRMATION_AFTER = "4"; 
	/**
	 * COLLECT_ADD 新生进货单 ，COLLECT_DISTRIBUTION  连锁配送中，STORE_COLLECT_DISTRIBUTION 单店配送中 ， COLLECT_RECEIVING 连锁已收货和拒收查看，STORE_COLLECT_RECEIVING 单店已收货和拒绝查看
	 */
	public static final String COLLECT_ADD = "0",COLLECT_DISTRIBUTION = "1",STORE_COLLECT_DISTRIBUTION = "2",COLLECT_RECEIVING = "3",STORE_COLLECT_RECEIVING = "4";	
	//add by luzheqi
	
	//url
	public static final String CATEGORY_SAVE_URL = BASE_URL + "category/save";
	public static final String CATEGORY_DELETE_URL = BASE_URL + "category/delete";
	public static final String CATEGORY_LIST_URL = Constants.BASE_URL + "category/list";
	public static final String GOODS_COUNT_URL = Constants.BASE_URL + "goods/categoryCount";
	public static final String SHOP_LIST_URL = Constants.BASE_URL + "shop/allshoplistincludecompany";
	public static final String GOODS_LIST_URL = Constants.BASE_URL + "goods/list";
	public static final String SAVE_CONFIG_URL = Constants.BASE_URL + "config/setting";
	public static final String GET_CONFIG_DETAIL = Constants.BASE_URL + "config/detail";
	public static final String DELETE_URL = Constants.BASE_URL + "goods/delete";
	public static final String GOODS_SAVE_URL = BASE_URL + "goods/save";
	public static final String SAVE_SETTING_BATCH_URL = BASE_URL + "salesset/set";
	public static final String GET_RECEIPT = BASE_URL + "receipt/selectData";
	public static final String SAVE_RECEIPT_URL = BASE_URL + "receipt/setting";

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

	public static final String GOODS_PROCESSING_URL = BASE_URL + "processing/list";
	public static final String GOODS_PROCESSING_SAVE_URL = BASE_URL + "processing/save";
	public static final String GOODS_PROCESSING_DELETE_URL = BASE_URL + "processing/delete";
	public static final String GOODS_PROCESSING_DETAIL_URL = BASE_URL + "processing/detail";
	public static final String GOODS_PROCESSING_CHOICE_URL = BASE_URL + "processing/choice";
	public static final String GOODS_CHECK_URL = BASE_URL + "goods/checkGoods";
	public static final String GOODS_DETAIL_URL = BASE_URL + "goods/findById";
	public static final String STOCK_QUERY_LIST = BASE_URL + "stockInfo/list";
	
	
	//盘点
	public static final String STOCK_CHECK_STATUS_URL = BASE_URL + "checkStock/searchShopCheckStockStatus";
    public static final String STOCK_CHECK_START_URL = BASE_URL + "checkStock/start";
    public static final String STOCK_CHECK_DOWNLOAD_GOODS_URL = BASE_URL + "checkStock/downlodeCheckGoods";//下载盘点商品
	public static final String STOCK_CHECK_UPDATE_STORE = BASE_URL + "checkStock/updateShopSingleGoodsCheck";//更新单个盘点商品
	public static final String STOCK_CHECK_CLEAR_STORE = BASE_URL + "checkStock/clearShopSingleGoodsCheck";//清除单个盘点商品
	public static final String STOCK_CHECK_CLEAR_REGION_STORE = BASE_URL + "checkStock/clearRegionGoodsCheck";//清除区域盘点
	public static final String STOCK_CHECK_SUBMIT_STORE = BASE_URL + "checkStock/updateGoodsCheck";//提交盘点信息
	public static final String STOCK_CHECK_SEARCH_GOODS = BASE_URL + "checkStock/searchGoodsCheck";//查询盘点商品
	public static final String STOCK_CHECK_SEARCH_NUCHECKED_GOODS = BASE_URL + "checkStock/searchNotGoodsCheck";//查询 未盘点商品
	public static final String STOCK_CHECK_FINISH = BASE_URL + "checkStock/finishShopGoodsCheck";//完成盘点
	public static final String STOCK_CHECK_CANCEL = BASE_URL + "checkStock/abandonShopGoodsCheck";//取消盘点
	public static final String STOCK_CHECK_GET_REGION_GOODS = BASE_URL + "checkStock/getRegionGoodsCheck";//获取区域盘点商品
	public static final String STOCK_CHECK_SUMMARY = BASE_URL + "checkStock/summaryShopGoodsCheck";//汇总盘点商品
	
	//角色权限
	public static final String ROLE_PERMISSION_ROLE_LIST = BASE_URL + "roleAction/list";//角色一览
	public static final String ROLE_PERMISSION_PERMISSION_LIST = BASE_URL + "roleAction/detail";//权限详情
	public static final String ROLE_PERMISSION_PERMISSION_INIT = BASE_URL + "roleAction/init";//权限初始化
	public static final String ROLE_PERMISSION_ROLE_DELETE = BASE_URL + "roleAction/delete";//角色删除
	public static final String ROLE_PERMISSION_ROLE_SAVEORUPDATE = BASE_URL + "roleAction/save";//角色添加修改
	// parameters name
	public static final String OPT_TYPE = "operateType";
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
	public static final String OPT_OLD_GOODS_ID = "oldGoodsId";
	public static final String STOCKQUERYPARAM = "findParameter";
	

	// parameters
	public static final String ADD = "add";
	public static final String EDIT = "edit";
	public static final String SAVE = "save";

	// return
	public static final String RETURN_CODE = "returnCode";
	public static final String LSUCCESS = "success";
	public static final String EXCEPTAION_CODE = "exceptionCode";
	public static final String CATEGORY_LIST = "categoryList";
	public static final String COUNT = "count";
	public static final String PAGE_SIZE = "pageSize";
	public static final String SHOP_LIST = "shopList";
	public static final String GOODS_LIST = "goodsList";
	public static final String GOODS_HANDLERS_LIST = "goodsHandleList";
	public static final String ORDER_GOODS_LIST = "orderGoodsList";
	public static final String GOODS_HANDLE = "goodsHandle";
	public static final String ORDER_GOODS_DETAIL_LIST = "orderGoodsDetailList";
	public static final String STOCK_IN_LIST = "stockInList";
	public static final String ALL_SHOP_LIST = "allShopList";
	public static final String STATUSLIST = "statusList";
	public static final String SUPPLY_MANAGER_LIST = "supplyManageList";
	public static final String STOCK_INDETAIL_LIST = "stockInDetailList";
	public static final String SUPPLY_MANAGE_LIST = "supplyManageList";
	public static final String STOCK_ADJUST_LIST = "stockAdjustList";
	public static final String STOCK_CHECK_RECORD_LIST = "stockCheckRecordList";
	public static final String STOCK_INFO_lIST = "stockInfoList";
	public static final String STOCKADJUSTVO = "stockAdjustVo";
	public static final String STOCKINFOALERTVO = "stockInfoAlertVo";
	


	//exception code
	public static final String GOODS_ERROR_13="GM_MSG_000013";
	public static final String GOODS_ERROR_14="GM_MSG_000014";
	public static final String GOODS_ERROR_15="GM_MSG_000015";
	public static final String GOODS_ERROR_25="GM_MSG_000025";
	public static final String COMMON_ERROR_12="CS_MSG_000012";
	public static final String COMMON_ERROR_11="CS_MSG_000011";
	public static final String LM_MSG_000011="LM_MSG_000011";
	public static final String SM_MSG_000013="SM_MSG_000013";
	public static final String LM_MSG_000006="LM_MSG_000006";
	public static final String LM_MSG_000021="LM_MSG_000021";
	public static final String LM_MSG_000013="LM_MSG_000013";
	public static final String CS_MSG_000020="CS_MSG_000020";
	public static final String SS_MSG_000009="SS_MSG_000009";
	public static final String GM_MSG_000010="GM_MSG_000010";
	public static final String GM_MSG_000023="GM_MSG_000023";
	public static final String GM_MSG_000027="GM_MSG_000027";
	public static final String GM_MSG_000028="GM_MSG_000028";
	public static final String GM_MSG_000029="GM_MSG_000029";
	public static final String CS_MSG_000011="CS_MSG_000011";
	public static final String LM_MSG_000019="LM_MSG_000019";
	
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

	// value category list
	public static final int CATEGORY_DEPTH = 3;
	public static final String CATEGORY = "category";
	public static final String MODE = "mode";
	public static final String PARENT_CATEGORY = "parentCategory";
	public static final String WAIT_GET_CATEGORY = "正在获取分类...";

	// value goods search acitvity
	public static final String GOODS_TITLE = "商品";
	public static final String CONNECTOR = "-";
	public static final String WAIT_SEARCH_GOODS = "正在搜索商品...";
	public static final String DEVICE_CODE = "deviceCode";
	public static final String NO_GOODS = "该分类下暂无商品";
	public static final int PAGE_SIZE_OFFSET = 1;
	public static final String SHOP = "shop";
	public static final String GOODS = "goods";

	// value goods detail
	public static final String PNG = ".png";
	public static final String SEARCH_STATUS = "searchStatus";
	public static final String BAR_CODE = "商品条码";
	public static final String TOTAL_PAGE = "totalPage";
	public static final String GOODS_CODE = "商品代码";
	public static final String GOODS_NAME = "商品名称";
	public static final String GOODS_JINHUOJIA = "进货价(元)";
	public static final String GOODS_JINHUOJIA_ERROR = "进货价";
	public static final String GOODS_LINGSHOUJIA = "零售价(元)";
	public static final String GOODS_LINGSHOUJIA_ERROR = "零售价";
	public static final String GOODS_KUCUN = "库存数";
	public static final String GOODS_TONGBU = "商品同步";
	public static final String GOODS_JIANMA = "商品简码";
	public static final String GOODS_PINYIN = "商品拼音码";
	public static final String GOODS_CATEGORY = "商品分类";
	public static final String GOODS_GUIGE = "商品规格";
	public static final String GOODS_PINGPAI = "商品品牌";
	public static final String GOODS_CHANDI = "商品产地";
	public static final String GOODS_BAOZHIQI = "保质期(天)";
	public static final String GOODS_IMAGE = "商品图片";
	public static final String GOODS_TICHENG = "销售提成比例(%)";
	public static final String GOODS_YOUHUI = "参与任何优惠活动";
	public static final String GOODS_TICHENG_FOR_ERROR = "销售提成比例";
	public static final String GOODS_JIFEN = "参与积分";
	public static final int INVALID_INT = -1;
	public static final CharSequence DELETE = "删除";
	public static final CharSequence CONTINUS_ADD = "保存并继续添加";
	public static final String DAY = "天";
	public static final String INPUT_GOODS_NAME = "商品名称不能为空，请输入！";
	public static final String INPUT_GOODS_LINGSHOUJIA = "商品零售价不能为空，请输入！";
	public static final String INPUT_RIGHT_LINGSHOUJI = "请输入正确的商品零售价";
	public static final String INPUT_RIGHT_JINHUO = "请输入正确的进货价";
	public static final String INPUT_RIGHT_SHOUJIA = "请输入正确零售价";
	public static final String INPUT_RIGHT_TIAOMA = "请输入正确的13位商品条码";
	public static final String INPUT_TIAOMA = "商品条码不能为空，请输入！";
	public static final String INPUT_RIGHT_DAIMA = "请输入正确的13位商品代码";
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

	// setting
	public static final CharSequence WAIT_SETTING = "正在获取系统参数...";
	public static final String UNKNOW_ERROR = "未知错误";
	public static final String SAVE_SETTING_SUCCESS = "保存系统设置成功";
	public static final String REMIND_SETTING = "提醒设置";

	// batch
	public static final String CHOOSE = "请先选择商品";
	public static final String WAIT_DELETE_BATCH = "正在删除...";
	public static final String IS_OPEN = "是否启用";
	public static final String ZHEKOU = "折扣(%)";
	public static final String ZHEKOU_FOR_ERROR = "折扣";
	public static final String BEGIN_TIME = "开始时间";
	public static final String CLICK_CHOOSE = "点击选择";
	public static final String END_TIME = "结束时间";
	public static final String HUIYUAN = "会员专享";
	public static final String INPUT_TICHENG = "销售提成比例不能为空，请输入！";
	public static final String INPUT_RIGHT_TICHENG = "销售提成比例输入格式错误，请重新输入！";
	public static final String WAIT_SAVE_SET = "正在保存销售设置...";
	public static final String INPUT_ZHEKOU = "折扣不能为空，请输入！";
	public static final String INPUT_RIGHT_ZHEKOU = "商品折扣输入格式错误，请重新输入！";
	public static final String INPUT_RIGHT_TIME = "时间输入错误，请重新输入！";

	// goods process
	public static final String INPUT_SEARCH_CODE = "请输入搜索条件！";
	public static final String SHOW_LOGO = "店家logo";
	public static final String RECEIPT_TITLE = "小票标题";
	public static final String BOTTOM_CONTENT = "尾注";
	public static final String RECEIPT_SPECIFIC = "小票规格";
	public static final String CHOOSE_TEMPLATE = "选择模板";
	public static final String INPUT_RECEIPT_TITLE = "小票标题不可为空，请输入！";
	public static final String INPUT_RECEIPT_BOTTTOM = "尾注不可未空，请输入！";
	public static final String WAIT_SAVE_RECEIPT = "正在保存小票设置...";
	public static final String WAIT_SAVE_GOODS = "正在保存商品信息...";
	public static final String SAVE_RECEIPT_SUCCESS = "保存小票设置成功";
	public static final String SALES_SAVE_SUCCESS = "保存销售设置成功";
	public static final String GOODS_SAVE_SUCCESS = "保存商品信息成功";

	// split assemble precess
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
	public static final String WAIT_OPT_DETAIL = "正在获取规则详情...";
	public static final String WAIT_DELETE_OPT = "正在删除规则...";
	public static final String CHOOSE_GOODS = "选择商品";
	public static final String WAIT_SAVE_RULE = "正在保存规则...";
	public static final String SAVE_RULE_SUCCESS = "保存规则成功";
	public static final String INPUT_RIGHT_NUM = "请输入正确的数目";
	public static final String INPUT = "请选择";
	public static final String DELETEP = "是否删除该商品";
	public static final String DELETE_P = "确定删除吗？";
	public static final String YES = "是";
	public static final String NO = "否";
	public static final String ONE = "1";
	public static final String ZERO = "0";

	public static final String OPUSERID = "opUserId";
	public static final String STOCKCHECKID = "stockCheckId";

	// 物流-退货

	//
	public static final String COMPANY_ALL_SHOP_LIST_URL = Constants.BASE_URL + "shop/allshoplistincludecompany";																

	public static final String ALL_SHOP_LIST_URL = Constants.BASE_URL + "shop/allshoplist";
	//根据实体id查询门店一览
	public static final String SHOPLIST_BY_ENTITYID = Constants.BASE_URL + "shop/shoplistbyentityid";

	public static final String SHUT_DOWN = "关闭";
	public static final String SAVE_TEXT = "保存";
	public static final String BACK_TEXT = "返回";
	public static final String CANCEL_TEXT = "取消";
	public static final String HAS_PARENT = "此分类有上级";
	public static final String PARENT = "上级分类";
	public static final String ADD_CATEGORY = "添加分类";
	public static final String CATEGORY_TEXT = "分类";
	public static final String GOODS_DETAIL = "商品详情";
	public static final CharSequence SCAN_SUCCESS = "扫码成功!";
	public static final CharSequence SCAN_SHIBAI = "扫码失败!";
	public static final String NO_GOODS_SCAN = "未查找到商品!";
	public static final String FILENAME_SUFFIX_BIG = "@1e_580w_385h_1c_0i_1o_90Q_1x.jpg";
	public static final String FILENAME_SUFFIX_SMALL = "@1e_144w_144h_1c_0i_1o_90Q_1x.jpg";
	public static final String NO_CATEGORY = "暂无分类";

	public static final String TITLE_ADD = "添加"; 
	public static final String TITLE_SPLIT_RULE = "商品拆分规则";
	public static final String TITLE_ASSEMBLE_RULE = "商品组装规则";
	public static final String TITLE_PROCESS_RULE = "商品加工规则";

	
	public static final String TITLE_OPT_SEARCH_SPLIT = "商品拆分";
	public static final String TITLE_OPT_SEARCH_ASSEMBLE = "商品组装";
	public static final String TITLE_OPT_SEARCH_PROCESS = "商品加工管理";

	public static final String NO_GOODS_CHOOSE = "未搜索到相关商品";

	public static final int FOR_SEARCH = 10088;
	public static final int FOR_GET = 10087;
	
	public static final String OPT = "操作";
	public static final String INF_NO_GOODS = "您查询的商品不存在，是否添加该商品？";
	public static final String CONFIRM = "确定";
	public static final String CANCEL = "取消";
	public static final int ZHONGDIANYOU = 2;
	public static final int BENDIANYOU = 1;
	public static final String I_KNOW = "我知道了";
	public static final String CAN_NOT_DELETE_SPLIT = "无法删除小件商品";
	public static final String CAN_NOT_DELETE_PROCESS = "无法删除加工商品";
	public static final String CAN_NOT_MORE_ASSEMBLE = "小件商品不能超过十个";
	public static final String CAN_NOT_MORE_PROCESS = "原料商品不能超过是个";
	public static final String IDS = "ids";
	public static final String SALE_SETTING = "销售设置";
	public static final String NO_NET = "当前网络不稳定，请稍后再试！";
	public static final String VIEW_IMAGE = "image/*";
	public static final String DELETEP_FOR_RULE = "确定删除该规则吗？";
	public static final String TEMP_PNG_NAME = "temp.png";
	public static final int SEARCH_STATUS_NO_GOODS = 4;
	public static final String PRESS_CHOOSE = "请选择";
	public static final String NO_RULES = "未找到相关规则";
 
	public static String getErrorInf(String input, String defaultMessage){
		if(input == null) {
			if (defaultMessage == null) {
				return NO_NET;
			} else {
				return defaultMessage;
			}
		} else if (input.equals(Constants.GOODS_ERROR_14)) {
			return "删除商品分类失败,请先删除其子分类";
		} else if (input.equals(Constants.GOODS_ERROR_15)) {
			return "删除商品分类失败,请先删除该分类下的商品";
		} else if (input.equals(Constants.GOODS_ERROR_13)) {
			return "商品分类名称已经存在，请重新输入！";
		} else if (input.equals(Constants.GOODS_ERROR_25)) {
			return "商品分类编码已经存在，请重新输入！";
//		} else if (input.equals(Constants.COMMON_ERROR_14)) {
//			return "请输入正确的数据";
		}else if (input.equals(Constants.COMMON_ERROR_12)) {
			return "输入参数为空或格式不正确,请重试!";
		}else if (input.equals(Constants.COMMON_ERROR_11)) {
			return "该数据已被别人修改或删除!";
		}else if (input.equals(Constants.LM_MSG_000011)) {
			return "到货日不得小于今日，请重新输入!";
		} else if (input.equals(Constants.SM_MSG_000013)) {
			return "库存正在盘点中，无法更新、插入库存信息表！";
		} else if (input.equals(Constants.LM_MSG_000021)) {
			return "该进货单已确认收货或拒绝收货，不可操作！";
		} else if (input.equals(Constants.LM_MSG_000006)) {
			return "请选择供应商！";
		} else if (input.equals(Constants.LM_MSG_000013)) {
			return "生产日期不能大于今日，请重新输入!";
		} else if (input.equals(Constants.CS_MSG_000020)) {
			return "此调整原因已存在!";
		} else if (input.equals(Constants.SS_MSG_000009)) {
			return "小票设置信息获取失败，请联系系统管理员！";
		}else if (input.equals(GM_MSG_000010)) {
			return "该商品信息已经被使用到库存和交易信息中，不能被删除！";
		}else if (input.equals(GM_MSG_000023)){
			return "商品简码已存在，请重新输入！";
		}else if (input.equals(CS_MSG_000011)){
			return "该数据已被别人修改或删除!！";
		}else if(input.equals(LM_MSG_000019)){
			return "调出门店和调入门店相同，请重新选择!";
		}else if(input.equals(GM_MSG_000027)){
			return "该商品加工规则已存在！";
		}else if(input.equals(GM_MSG_000028)){
			return "该商品拆分规则已存在！";
		}else if(input.equals(GM_MSG_000029)){
			return "该商品组装规则已存在！";
		}else {
			if (defaultMessage == null) {
				return NO_NET;
			} else {
				return defaultMessage;
			}
		}
	}
	
	//门店解析list
	public static final String ADDRESS_LIST = "addressList";
	public static final String SHOPTYPE_LIST = "shopTypeList";
	public static final String SEARCHSHOPCODE = "shopCode";
	public static final String SHOPARENTNAME = "parentName";
	public static final String SHOPTYPE = "shopType";
	public static final String DATAFROMSHOPID = "dataFromShopId";
	public static final String SHOPKEYWORD = "keyWord";
	public static final Integer ONE_INT = 1;


	
	public static final String USER_LIST = "userList";
	public static final String USER = "user";
	public static final int USERSTAFFIDMAXLENGTH = 20;
	public static final int USERNAMEMAXLENGTH= 50;
	public static final int USERACCOUNTMAXLENGTH = 50;
	public static final int USERPASSWORDMAXLENGTH = 10;
	public static final int USERADDRESSMAXLENGTH = 100;
	public static final int USERIDENTITYNOMAXLENGTH = 18;
	public static final int USERMOBILELENGTH = 11;
	//public static final String ROLE_LIST = "roleList";

	public static final String ROLE_LIST = "roleList";
	public static final String SEX_LIST = "sexList";
	public static final String IDENTITYTYPE_LIST = "identityTypeList";	
	
	//收入信息查询
	public static final String TODAYINCOME = "inComeMsgDay";
	public static final String YESTERDAYINCOME = "inComeMsgYDay";
	public static final String MONTHINCOME = "inComeMsgMonth";
	public static final String ENTITYMODE = "entityModel";
	public static final String JSESSIONID = "jsessionId";
	public static final String TIMERANGE = "timeRange";
	public static final String USERID = "userId";
	public static final String SHOPCOPNAME = "shopname";
	public static final String ROLEID = "roleId";

	public static final String STOCKINFOLIST = "stockInfoList";

	public static final String UNEDITABLE = "不可编辑";
	public static final String UNWRITE = "未填写";
	public static final String All_SHOP = "allShopList";

	public static final String PAGECOUNT = "pageCount";
	
	public static final String SUMMONEY = "sumMoney";
	
	public static final String NOWSTORE = "nowStore";

	public static final String CHOOSE_SHOP = "选择商店";

	public static final String INPUT_JIANMA = "请输入简码";

	public static final String WAIT_RECEIPT_SETTING = "正在获取销票原始设置";

	public static final int MAX_RECEIPT = 50;

	public static final String JSON = "json";

	public static final String SYNC_SHOP = "同步门店";

	public static final String LENGTOU = "零头处理";

	public static final String IS_ALREADY_HAVE = "alreadyHave";


	public static final String GOODS_ALREADY_HAVE = "商品已存在，请修改商品明后添加。";


	public static final int RECEIPT_TEXT_MAX_LENGTH = 50;


	public static final String ZERO_PERCENT = "0.00";


	public static final CharSequence ONE_HUNDRED = "100";

	public static final int SINGLEMODE = 1;
	public static final int THIRTEEN_LENGTH = 13;
	public static final int SIX_LENGTH = 6;
	public static final int FIFTH_LENGTH = 50;
	public static final int ONE_HUNDRED_LENGTH = 100;
	public static final int EIGHT_LENGTH = 8;
	public static final String MAX_PRICE = "999999";
	public static final String MAX_PRICE_WITH_POTIN = "999999.99";
	public static final String DELETEP_FOR_MANY_GOODS = "确定删除所有选中商品吗?";
	public static final String INPUT_CATEGORY_NAME = "商品分类名称不能未空，请输入！";
	public static final String ERRORCSMGS = "CS_MSG_000019";
	public static final String MESSAGE = "message";

	public static final String DELETE_CATEGORY_P = "分类名称已被商品使用，删除分类，将使部分商品丢失分类信息。确定要删除吗？";
	
	public static final String IMGENDWITH144 = "@1e_144w_144h_1c_0i_1o_90Q_1x.jpg"	;
	public static final String IMGENDWITH580 = "@1e_580w_385h_1c_0i_1o_90Q_1x.jpg"	;
	
	public static final String SHOPNAME ="shopName";
	public static final String PARENTID = "parentId"; 
	public static final String SHOPCODE = "shopCode";
	public static final String PASSWORD = "pwd";

	public static final String INPUT_RIGHT_TIME_S = "结束时间要大于等于开始时间。"; 
	public static final String INPUT_RIGHT_TIME_E = "结束时间要大于等于当前时间。";

	public static final String RESULT = "result"; 
	public static final String ALL_CHIRLDREN_SHOP = "所有下属门店";


	public static final String PLEASE_CHOOSE = "请选择";


	public static final String BAR_CODE_FOR_REQUEST = "barCode"; 
	public static final String GOODS_NAME_FOR_REQUEST = "name";


	public static final String ADD_YIMADUOPING = "输入的商品条码已经存在，是否继续添加一码多品的商品？";


	public static final String GOODS_NAME_EXISTS = "商品名称已经存在，请重新输入！";
	public static final String PULL = "下拉即可刷新数据";
	public static final String RELEASE = "放开即可刷新数据";
	public static final String REFRESHING = "数据读取中...";
	public static final String DPULL = "上拉即可加载数据";
	public static final String DRELEASE = "放开即可加载数据";
	public static final String DREFRESHING = "数据加载中...";

	public static final String ENTITY_CODE = "entityCode";
	public static final String USER_NAME = "username";
	public static final String LOGIN_PASSWORD = "password";

	public static final int NINE_LENGTH = 9;

	public static final int FIVE_LENTH = 5;

	public static final String XIAOSHU_ERROR_TOO_LONG = "小数位不得超过两位，请重新输入！";
	public static final String XIAOSHU_ERROR_TOO_SHORT = "输入格式错误，请重新输入！";
	public static final String ZHENGSHU_ERROR_TOO_LONG_6 = "整数部分不得超过6位，请重新输入！";
	public static final String ZHENGSHU_ERROR_TOO_LONG_3 = "不得超过100，请重新输入！";
	public static final String XIAOSHU_NEGTIVE = "不能小于零，请重新输入！";

	public static final String ASYNC_ALL = "同步所有";

	public static final String ALL_SHOP_ID = "all_shop_id";

	public static final String NEED_ASK_TO_ADD = "need_ask_to_add";

	public static final int FOR_ADD = 10089;



	public static final String IS_BIG = "isBig";
}
