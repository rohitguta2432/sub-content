package com.vedanta.vpmt.web.util;

public class URLConstants {

	public final static String AUTHORIZATION_URL = "auth";

	public final static String GET_ALL_CATEGORIES = "category/categories";
	public final static String GET_CATEGORY_DETAIL = "category/%d";
	public final static String GET_CATEGORIES_BY_BUSINESS_UNIT_ID = "category/business-unit-id/%d";

	public final static String GET_ALL_DATA_UNITS = "admin/template/data-unit/all";

	public final static String GET_ALL_FIELDS = "admin/template/field/all";
	public final static String GET_FIELD_DETAIL = "admin/template/field/%d";
	public final static String SAVE_FIELD_DETAIL = "admin/template/field/save";

	public final static String GET_ALL_GROUPS = "admin/template/group/all";
	public final static String GET_GROUP_DETAIL = "admin/template/group/%d";
	public final static String SAVE_GROUP_DETAIL = "admin/template/group/save";

	public final static String GET_ALL_TEMPLATES = "admin/template/all";
	public final static String GET_TEMPLATE_DETAIL = "admin/template/%d";
	public final static String SAVE_TEMPLATE_DETAIL = "admin/template/save";

	public final static String GET_ALL_GROUP_FIELD = "admin/template/group/fields/all";
	public final static String GET_GROUP_FIELDS_DETAIL = "admin/template/group/fields/%d";
	public final static String GET_GROUP_FIELDS_BY_GROUP_ID = "admin/template/group/fields/by-group/%d";
	public final static String SAVE_GROUP_FIELD = "admin/template/group/fields/save";
	public final static String SAVE_GROUP_FIELD_LIST = "admin/template/group/fields/save-list/%d";
	public final static String DELETE_GROUP_FIELD_LIST = "admin/template/group/fields/delete/%d";

	public final static String GET_ALL_TEMPLATE_GROUPS = "admin/template/get/groups/all";
	public final static String GET_TEMPLATE_GROUPS_BY_TEMPLATE_ID = "admin/template/get/groups/by-template/%d";
	public final static String GET_TEMPLATE_GROUP_BY_TEMPLATE_GROUP_ID = "admin/template/get/groups/%d";
	public final static String SAVE_TEMPLATE_GROUPS = "admin/template/post/groups/save";
	public final static String SAVE_TEMPLATE_GROUP_LIST = "admin/template/post/groups/save-list/%d";
	public final static String DELETE_TEMPLATE_GROUPS_BY_TEMPLATE_ID = "admin/template/delete/groups/by-template/%d";
	public final static String GET_ALL_DISTINCT_TEMPLATE_TARGET = "admin/template/get/distinct/target";

	public final static String GET_ALL_TEMPLATE_GROUPS_BY_BUSINESS_UNIT_ID = "admin/template/get/groups/getAllTemplateGroupsByBusinessUnit/%d";

	public final static String GET_SCORECARD_TEMPLATE_BY_SCORECARD_ID = "scorecard/user/%d";
	public final static String GET_SCORECARD_TEMPLATE_BY_CONTRACT = "scorecard/template/%s";
	public final static String GET_SCORECARD_TEMPLATE_BY_SUB_CATEGORY = "template/get/sub-category/%d";
	public final static String GET_SCORECARD_BASE_DATA = "scorecard/template/get/base-data";
	public final static String SAVE_SCORECARD = "scorecard/save";
	public final static String SAVE_SCORECARD_GROUP_USER = "scorecard/group/user/save";
	public final static String GET_ALL_SCORECARD = "scorecard/all-scorecard";
	public final static String GET_ALL_SCORECARD_STATUS = "scorecard/get-user-scorecard/%d";
	public final static String GET_ALL_SCORECARD_STATUS_BUSINESSUNITID_PLANTCODE = "scorecard/get-user-scorecard/%d/%d/%s";

	// for admin and business unit head
	public final static String GET_ALL_SCORECARD_STATUS_BUSINESS_UNIT_ID = "scorecard/get-user-scorecard/%d/%d";

	public final static String GET_ALL_SCORECARD_GROUP_USER_ID = "scorecard/get/scorecard-group-user/%d";
	public final static String GET_ALL_SCORECARD_DATE_RANGE = "scorecard/get-user-scorecard/date-range?";
	public final static String GET_ALL_SCORECARD_DATE_RANGE_BUSINESSUNITID_PLANTCODE = "scorecard/get-user-scorecard/date-range-businessUnitId-plantCode?";

	public final static String GET_ALL_SCORECARD_DATE_RANGE_BUSINESSUNITID = "scorecard/get-user-scorecard/date-range-businessUnitId?";

	public final static String SCORECARD_ASSIGN = "scorecard/scorecard-assign";

	public final static String GET_SCORECARD__BY_SUBCATEGORY_ID = "scorecard/getScorecardBySubcategoryId/%d";

	public final static String GET_ALL_USER_NOT_ASSIGNED = "scorecard/getAllUserNotAssigned/%d";
	public final static String GET_ALL_USER_NOT_ASSIGNED_BY_NAME = "scorecard/getAllUserNotAssignedByName/%d/%s";

	public final static String GET_ALL_USER_NOT_ADMIN_BY_LIMIT = "scorecard/getAllUserNotAdminByLimit/%d";

	public final static String GET_ALL_USER_BY_NAME = "scorecard/getAllUserByName/%s";

	public final static String GET_ALL_USER_BY_NAME_AND_BUID = "scorecard/getAllUserByName/%s/bu-id/%d";

	public final static String GET_FORM_SAVED_FORM_BY_FORM_SAVED_ID = "form-save/user/%d";
	public final static String GET_FORM_SAVED_COMPLIANCE_FORM_BY_FORM_SAVED_ID = "form-save/user/compliance/%d/scorecard/%d";
	public final static String SAVE_FORM_SAVED = "form-save/save";
	public final static String SAVE_FORM_GROUP_USER = "form-save/group/user/save";
	public final static String GET_ALL_FORM_SAVED = "form-save/all-form-saved";
	public final static String GET_ALL_FORM_SAVED_STATUS = "form-save/get-user-scorecard/%d";

	public final static String GET_USER_BY_LOGINID = "user/userValidation/%s";
	public final static String GET_ALL_USER_DETAILS = "user/all-users";
	public final static String GET_USER_BY_USERID = "user/byUserId/%d";
	public final static String UPDATE_USER_BY_USERID = "user/save";
	public final static String GET_CURRENT_USER = "user/currentUser";
	public final static String LOGOUT = "auth/logout/token";

	public final static String NEW_DASHBOARD_DATA = "scorecard/dashboard-data?";

	public final static String UPDATE_VENDOR_BY_VENDOR_ID = "vendor/save";
	public final static String GET_ALL_VENDORS_DETAILS = "vendor/vendors";
	public final static String GET_VENDOR_BY_VENDORID = "vendor/%d";
	public final static String GET_VENDOR_BY_BUSINESS_UNIT_ID = "vendor/getAllVendorsByBusinessUnit/%d";

	public final static String GET_ALL_FORM_DATA_UNITS = "admin/form/data-unit/all";

	public final static String GET_ALL_FORM_FIELDS = "admin/form/field/all";
	public final static String GET_FORM_FIELD_DETAIL = "admin/form/field/%d";
	public final static String SAVE_FORM_FIELD_DETAIL = "admin/form/field/save";

	public final static String GET_ALL_FORM_GROUPS_DETAIL = "admin/form/group-detail/all";
	public final static String GET_FORM_GROUP_DETAIL = "admin/form/group-detail/%d";
	public final static String SAVE_FORM_GROUP_DETAIL = "admin/form/group-detail/save";

	public final static String GET_ALL_FORMS = "admin/form/all";
	public final static String GET_FORM_DETAIL = "admin/form/%d";
	public final static String SAVE_FORM_DETAIL = "admin/form/save";

	public final static String GET_ALL_FORM_GROUP_FIELD = "admin/form/group/fields/all";
	public final static String GET_FORM_GROUP_FIELDS_DETAIL = "admin/form/group/fields/%d";
	public final static String GET_FORM_GROUP_FIELDS_BY_GROUP_ID = "admin/form/group/fields/by-group/%d";
	public final static String SAVE_FORM_GROUP_FIELD_LIST = "admin/form/group/fields/save-list/%d";
	public final static String DELETE_FORM_GROUP_FIELD_LIST = "admin/form/group/fields/delete/%d";

	public final static String GET_ALL_FORM_GROUPS = "admin/form/get/groups/all";
	public final static String GET_FORM_GROUPS_BY_FORM_ID = "admin/form/get/groups/by-from/%d";
	public final static String GET_FORM_GROUP_BY_FORM_GROUP_ID = "admin/form/get/groups/%d";
	public final static String SAVE_FORM_GROUP_LIST = "admin/form/post/groups/save-list/%d";
	public final static String DELETE_FORM_GROUPS_BY_FORM_ID = "admin/form/delete/groups/by-form/%d";
	public final static String GET_ALL_DISTINCT_FORM_TARGET = "admin/form/get/distinct/target";

	public final static String SAVE_CONTRACT_DETAIL = "contract/save";
	public final static String GET_CONTRAT_DETAIL_BY_CONTRACT_ID = "contract/%d";
	public final static String GET_CONTRAT_DETAIL_BY_VENDOR_ID = "contract/vendorId/%d";
	public final static String GET_CONTRACT_DETAILS = "contract/contracts";

	public final static String SAVE_USER_FORM_GROUP = "userFormGroup/save";
	public final static String UPDATE_USER_FORM_GROUP_BY_ID = "userFormGroup/update";
	public final static String GET_ALL_USER_FORM_GROUP_DETAILS = "userFormGroup/all-userFormGroups";
	public final static String GET_USER_FORM_GROUP_DETAILS_BY_USERID_AND_VENDORID_AND_CONTRACTID_AND_FORMID = "userFormGroup/view-user-form-groupdata/user/%d/vendor/%d/contract/%d/form/%d";
	public final static String GET_USER_FORM_GROUP_BY_ID = "userFormGroup/%d";

	public final static String SAVE_TARGET_VALUE = "admin/template/targetvalue/save";
	public final static String UPDATE_TARGET_VALUE_BY_ID = "admin/template";
	public final static String GET_ALL_TARGET_VALUE = "admin/template/targetvalue/all";
	public final static String GET_TARGET_VALUE_BY_ID = "admin/template/get/targetvalue/%d";

	// For Email Template
	public final static String GET_EMAILTEMPLATE_DETAIL = "emailTemplate/getAll";
	public final static String SAVE_EMAIL_TEMPLATE = "emailTemplate/save";
	public final static String GET_EMAILTEMPLATE_DETAILBYID = "emailTemplate/%d";
	public final static String UPDATE_EMAIL_TEMPLATE = "emailTemplate/save";
	public final static String GET_SCORECARD_SCORECARD_ID = "scorecard/%d";
	public final static String SAVE_EMAIL_DETAILS = "email/save";

	public final static String SAVE_EMAIL_LOG_DETAILS = "emailTemplate/Survey/template/save";
	public final static String SEND_EMAIL = "emailTemplate/send/mail";
	public final static String GET_USER_BY_EMPLOYEEID = "emailTemplate/getUser/%s";
	public final static String GET_ALL_PLANTS = "plants/all";
	public final static String GET_PLANT_BY_ID = "plants/%d";
	public final static String GET_ALL_CURRENT_BUSINESS_UNIT_PLANTS = "plants/current-business-unit-id";
	public final static String GET_ALL_PLANTS_CURRENT_USER = "plants/current-user";
	public final static String ALL_USER_NOT_ADMIN = "emailTemplate/getAllUsersNotAdmin";
	public final static String ALL_CONTRACTS_BY_PLANTID = "emailTemplate/getAllContract/%d";
	public final static String ALL_CONTRACTS_BY_VENDOR_ID = "emailTemplate/getAllContractByVendorId/%d";
	public final static String GET_ALL_PLANT_BY_PLANTID = "plants/%d";
	public final static String GET_ALL_FORMS_CATEGORYID = "emailTemplate/getAllforms/%d/%d";

	// get All bussiness Unit

	public final static String GET_ALL_BUSINESS_UNIT = "business-unit/businessUnits";
	public final static String UPDATE_ALL_APPROVER_FOR_SCORECARD = "scorecard/update-approver";

	public final static String GET_PLANT_BY_BUSINESS_ID = "business-unit/getAllplant/%d";
	public final static String GET_PLANT_BY_PLANT_NAME = "business-unit/getAllplant/plant-name/%s";

	public final static String GET_ALL_BUSINESS_UNIT_BY_ROLE = "business-unit/businessUnitsByRole";
	
	public final static String GET_ALL_BUSINESS_UNITS = "business-unit/all";

	// get user by Plant head and business Id
	public final static String GET_USER_BY_PLANT_HEAD_AND_BUSINESS_ID = "user/getAllUserByPlantHeadAndBusinessUnitId/%d";

	// get all plant head

	public final static String GET_ALL_PLANT_HEAD = "plantHead/getAllPlantHead";

	public final static String SAVE_PLANT_HEAD = "plantHead/save";

	public final static String GET_PLANT_HEAD_DETAIL = "plantHead/getById/%d";

	public final static String GET_PLANT_HEAD_BY_EMPLOYEEID = "plantHead/getByEmployeeId/%s";

	public final static String GET_ALL_USER_DEPARTMENT = "userDepartment/user-department/%d";

	public final static String GET_ALL_USER_OFFICE = "userDepartment/user-office/%d";

	public final static String CHECK_EXISTING_PLANT_HEAD = "plantHead/checkExistingPlantHead";

	// notification

	public final static String SAVE_NOTIFICATION = "notification/save";
	public final static String GET_NOTIFICATION = "notification/getAll";
	public final static String SUBMIT_NOTIFICATION = "notification/submitNotification";
	public final static String GET_NOTIFICATIONBYID = "notification/getById";
	public final static String GET_NOTIFICATION_BY_CONTRACT_ID = "notification/getScorecadId/%d/%s";
	public final static String GET_NOTIFICATION_ALL = "notification/getAllNotification";
	public final static String CHANGE_STATUS = "notification/changeStatus";
	public final static String CHANGE_STATUS_NOTIFICATION = "notification/checked/%s";

	// for All subcategory
	public final static String GET_ALL_SUBCATEGORIES = "scorecard/allSubCategory";

	public final static String NEW_DASHBOARD_DATA_BU_ID = "dashboard/dashboard-data?";
	public final static String GET_ALL_SUBCATEGORIES_BY_BU_ID = "dashboard/subCategoryByBuId/%d";

	public final static String GET_PLANT_BY_PLANT_CODE = "dashboard/getPlantByPlantCode/%s";

	public final static String GET_PLANTHEAD_BY_EMPLOYEEID = "dashboard/getPlantHeadByEmployeeId/%s";

	// get user by bussiness unitId not Admin
	public final static String GET_USER_BY_BUSSINESSID = "user/getAllUserByBussinessId/%d";

	// for populate form saved
	public final static String POPULATE_FORM_SAVE = "form-save/populateFormSaved";

	// for get form saved by buId,status,plantCode
	public final static String FORM_SAVE_BY_BUID_PLANTCODE_STATUS = "form-save/getFormSaved/%d/%d/%s";

	// for plant head by plantCode
	public final static String GET_PLANT_HEAD_BY_PLANTCODE = "plantHead/getByPlantCode/%s";

	// support form
	public final static String SAVE_SUPPORT_DETAIL = "support/save";
	public final static String GET_SUPPORT_LIST = "support/support-list";
	public final static String UPDATE_SUPPORT = "support/update";

	public final static String GET_SCORECARD_BY_ID = "scorecard/%d";

	public final static String GET_ALL_SCORECARD_ROLE_BASED = "scorecard/getAllScorecardRoleBased";

	public final static String GET_ALL_USER_DEPARTMENT_BY_BUID = "userDepartment/getAllUserDepartmentByBuId/%d";

	public final static String GET_ALL_PLANT_BY_PLANTCODE_AND_BUID = "plants/getAllPlantByPlantCodeAndBusinessUnitId/%s/%d";
	
	public final static String GET_ALL_USER_NOT_ADMIN_BY_LIMIT_AND_NOT_APPROVER = "scorecard/getAllUserNotAdminByLimitAndNotApprover/%d/%d";
	
	public final static String GET_ALL_USER_BY_NAME_AND_BUID_AND_NOT_APPROVER = "scorecard/getAllUserByNameAndBuIdAndNotApprover/%s/%d/%d";

	// human resource	
	public final static String SAVE_HUMAN_RESOURCE = "humanResource/save";
	
	public final static String CHECK_EXISTING_HUMAN_RESOURCE = "humanResource/checkExistingHumanResource";
	
	public final static String GET_USER_BY_HUMAN_RESOURCE_AND_BUSINESS_ID = "user/getAllUserByHumanResourceAndBusinessUnitId/%d";
	
	public final static String GET_ALL_HUMAN_RESOURCE = "humanResource/getHumanResource";
	
	public final static String GET_HUMAN_RESOURCE_DETAIL = "humanResource/getHumanResourceById/%d";
	//---
	
	public final static String GET_ALL_USER_NOT_ADMIN_AND_HR_BY_LIMIT = "form-save/getAllUserNotAdminAndHrByLimit/%d";
	
	public final static String GET_ALL_HR_USER_BY_LIMIT = "form-save/getAllHrUserByLimit/%d";
	
	public final static String GET_ALL_USER_NOT_ADMIN_AND_HR_BY_NAME = "form-save/getAllUserNotAdminAndHrByName/%s/%d";
	
	public final static String GET_ALL_HR_USER_BY_NAME = "form-save/getAllHrUserByName/%s/%d";
	
	public final static String GET_ALL_USER_DEPARTMENT_BY_LIMIT = "userDepartment/getUserDepartmentByLimit/%d";
	
	public final static String GET_ALL_USER_DEPARTMENT_BY_NAME = "userDepartment/getUserDepartmentByName/%s/%d";
}
