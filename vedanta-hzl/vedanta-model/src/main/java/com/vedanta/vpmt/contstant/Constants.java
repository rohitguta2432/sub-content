package com.vedanta.vpmt.contstant;

public class Constants {
    public static final String VENDOR = "VENDOR";
    public static final String CONTRACT = "CONTRACT";
    public static final String TEMPLATE= "TEMPLATE";
    public static final String TEMPLATE_GROUPS= "TEMPLATE_GROUPS";
    public static final String SCORECARD_DETAIL = "SCORECARD_DETAIL";
    public static final String USER_DETAIL = "USER_DETAIL";
    public static final String PLANT_DETAIL = "PLANT_DETAIL";
    public static final String TEMPLATE_FROMS = "TEMPLATE_FROMS";

    public static final String ALL_CATEGORIES= "CATEGORIES";
    public static final String ALL_PLANTS= "ALL_PLANTS";
    public static final String ALL_DEPARTMENT= "ALL_DEPARTMENT";

    public static final String FORM = "FORM";
    public static final String FORM_SAVED_DETAIL = "FORM_SAVED_DETAIL";
    public static final String FORM_GROUPS= "FORM_GROUPS";


    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String IMAGE = "IMAGE";
    public static final String PDF = "PDF";
    public static final String RESOURCE_TYPE = "RESOURCE";
    public static final String RESOURCE_DATA = "DATA";


    // Scheduler Constants
    public final static String SCHEDULER_NAME_EIC_PRODUCT = "EIC_PRODUCT_SCHEDULER";

    public final static String SCB_CREDENTIALS_TEMPLATE = "scb_credentials";
    public final static String REQUIRE_DOCUMENTS_TEMPLATE = "Required-documents";
    public final static String EXISTING_REQUIRE_DOCUMENTS_TEMPLATE = "ExistedRequired-documents";

    // Start - Performance
    public static final String YEARLY = "YEARLY";
    public static final String MONTHLY = "MONTHLY";
    public static final String WEEKLY = "WEEKLY";
    public static final String DAILY = "DAILY";

    public static final String AUTH_TOKEN = "token";
    public static final String USER = "user";
    public static final String USER_BU_ID = "businessUnitId";
    public static final String AUTHORITIES = "authorities";
    public static final String AUTHORITY = "authority";
    public static final String SAML_RESPONSE = "samlResponse";

    public static final String ROLE_SRM = "ROLE_SRM";
    public static final String ROLE_SEGMENT_HEAD = "ROLE_SEGMENT_HEAD";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER_MANAGER = "ROLE_USER_MANAGER";
    public static final String ROLE_DATA_UPLOAD = "ROLE_DATA_UPLOAD";
    public static final String ROLE_PLANT_HEAD = "ROLE_PLANT_HEAD";
    public static final String ROLE_BUSINESS_UNIT_HEAD = "ROLE_BUSINESS_UNIT_HEAD";
    public static final String ROLE_CONTRACT_MANAGER = "ROLE_CONTRACT_MANAGER";
    public static final String ROLE_HR = "ROLE_HR";

    
    // business unit Ids 
    
  /*  public static final Long HZL_BUSINESS_UNIT = 1L;
    public static final Long SESA_BUSINESS_UNIT = 2L;
    public static final Long STERLITE_BUSINESS_UNIT = 3L;
    public static final Long VAL_BUSINESS_UNIT = 4L;
    */
    
    public static final Long HZL_BUSINESS_UNIT = 1L;
    public static final Long STERLITE_BUSINESS_UNIT = 2L;
    public static final Long CAIRN_BUSINESS_UNIT = 3L;
    public static final Long SESA_BUSINESS_UNIT = 4L;
    public static final Long VGCB_BUSINESS_UNIT = 5L;
    public static final Long FUJAIRAH_BUSINESS_UNIT = 6L;
    public static final Long BALCO_BUSINESS_UNIT = 7L;
    public static final Long JHARSUGUDA_BUSINESS_UNIT = 8L;
    public static final Long VALLANJIGARH_BUSINESS_UNIT = 9L;
    public static final Long TSPL_BUSINESS_UNIT = 10L;
    public static final Long KCM_BUSINESS_UNIT = 11L;
    public static final Long ZI_BUSINESS_UNIT = 12L;
    
    
    // Keystores name

    public static final String KEY_STORE_NAME_EIC = "keystore.jks";
    public static final String KEY_STORE_NAME_ADFS = "adfs_keystore.jks";

    public static final String P_STRING = "a3345swer1oip34677ksla";


    public static final String PROP_LOCATION_FROMAT = "file:%s%s";
    public static final  String CONFIG_LOCATION = "config.location";

    public final static String ADMIN_REQUEST = "adminRequest";

    public final static int STATUS_NOT_FILLED = 0;
    public final static int STATUS_ACTIVE = 1;
    public final static int STATUS_INACTIVE = 2;
    public final static int STATUS_DRAFT = 3;
    public final static int STATUS_DELETE = 4;
    public final static int STATUS_SUBMITTED = 5;
    public final static int STATUS_REJECTED = 6;
    public final static int STATUS_APPROVED = 7;
    public final static int STATUS_OPENED = 8;
    public final static int STATUS_PROGRESS = 9;
    public final static int STATUS_CLOSED = 10;
    

    public final static Long DELETE = 1l;

    public final static Long DEFAULT_CATEGORY = 0L;
    public final static Long DEFAULT_SUB_CATEGORY = 0L;

    public final static int ALL_SCORECARD = 777;
    public final static int ALL_SAVEFORM = 777;
    
    public final static String NEW_DASHBOARD_DETAILS = "newDashboardDetails";
    
    public final static String MONTH_LIST = "monthList";
    public final static String ALL_SUBCATEGORIES = "ALL_SUBCATEGORIES";
    public final static String ALL_CONTRACTS = "ALL_CONTRACTS";


    public static final String SCORECARD_GROUP_USERS = "SCORECARD_GROUP_USERS";
    public static final String FORM_GROUP_USERS = "FORM_GROUP_USERS";
    
    /*for compliance-form*/
    public final static String COMPLIANCE_FORM_SUBMIT = "Submit";
    public final static String COMPLIANCE_FORM_APPROVE ="Approve";
    public final static String COMPLIANCE_FORM_REJECT = "Reject";
    
    public final static Long DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_SUBMIT = 11L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_APPROVE = 12L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_REJECT = 13L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_ASSIGN = 14L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_SCORECARD_ASSIGN = 1L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_SUPPORT = 15L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_SCHEDULER_LOG = 16L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_CREATEDON_SCORECARD = 8L;
    
    /*for scorecard-email-notification*/
    public final static String SCORECARD_SUBMIT = "Submit";
    public final static String SCORECARD_APPROVE ="Approve";
    public final static String SCORECARD_REJECT = "Reject";
    
    public final static Long DEFAULT_EMAIL_TEMPLATE_SCORECARD_SUBMIT = 2L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_SCORECARD_APPROVE = 9L;
    public final static Long DEFAULT_EMAIL_TEMPLATE_SCORECARD_REJECT = 10L;
    
   
	/* Constants for SESA EmailNotification */
	public final static int PENDING_SCORECARD_STATUS = 0;
	public final static Long DEFAULT_EMAIL_TEMPLATE_CURRENT_DATE_CREATED_ON_SCORECARD = 8L;
	public final static Long DEFAULT_EMAIL_TEMPLATE_THREE_DAYS_BFORE_DUE_DATE_SCORECARD = 7L;
	

	 /*Constants for HZL Compliance-Form*/ 
    public final static Long HZL_DEFAULT_CATEGORY_ID = 6L;
    public final static Long HZL_DEFAULT_SUB_CATEGORY_ID = 36L;
    public final static Long HZL_DEFAULT_FORM_ID = 20L;
    

    public final static int SCORECARD_PENDING_STATUS = 0;

    
    /* Constants for STERLITE  Compliance-Form */
	public final static Long STERLITE_DEFAULT_CATEGORY_ID = 12L;
	public final static Long STERLITE_DEFAULT_SUB_CATEGORY_ID = 54L;
	public final static Long STERLITE_DEFAULT_FORM_ID = 30L;
	
	/* Constants for CAIRN Compliance-Form */
	public final static Long CAIRN_DEFAULT_CATEGORY_ID = 18L;
	public final static Long CAIRN_DEFAULT_SUB_CATEGORY_ID = 217L;
	public final static Long CAIRN_DEFAULT_FORM_ID = 93L;
    
    
    /* Constants for Sesa Compliance-Form */
	public final static Long SESA_DEFAULT_CATEGORY_ID = 24L;
	public final static Long SESA_DEFAULT_SUB_CATEGORY_ID = 177L;
	public final static Long SESA_DEFAULT_FORM_ID = 36L;
	
	/* Constants for VGCB Compliance-Form */
	public final static Long VGCB_DEFAULT_CATEGORY_ID = 30L;
	public final static Long VGCB_DEFAULT_SUB_CATEGORY_ID = 157L;
	public final static Long VGCB_DEFAULT_FORM_ID = 45L;
	
	
	/* Constants for FUJAIRAH Compliance-Form */
	public final static Long FUJAIRAH_DEFAULT_CATEGORY_ID = 36L;
	public final static Long FUJAIRAH_DEFAULT_SUB_CATEGORY_ID = 195L;
	public final static Long FUJAIRAH_DEFAULT_FORM_ID = 55L;
	
	/* Constants for BALCO Compliance-Form */
	public final static Long BALCO_DEFAULT_CATEGORY_ID = 42L;
	public final static Long BALCO_DEFAULT_SUB_CATEGORY_ID = 83L;
	public final static Long BALCO_DEFAULT_FORM_ID = 65L;
	
	/* Constants for JHARSUGDA Compliance-Form */
	public final static Long JHARSUGDA_DEFAULT_CATEGORY_ID = 48L;
	public final static Long JHARSUGDA_DEFAULT_SUB_CATEGORY_ID = 110L;
	public final static Long JHARSUGDA_DEFAULT_FORM_ID = 66L;
	
	/* Constants for VAL Compliance-Form */
	public final static Long VAL_DEFAULT_CATEGORY_ID = 54L;
	public final static Long VAL_DEFAULT_SUB_CATEGORY_ID = 139L;
	public final static Long VAL_DEFAULT_FORM_ID = 85L;
	

}
