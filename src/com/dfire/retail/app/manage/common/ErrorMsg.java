package com.dfire.retail.app.manage.common;

public  class ErrorMsg {
		
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

}