package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.UserDepartment;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public List<User> getAllUsers() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_USER_DETAILS, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all users details", 401);
			}
			log.error("Error while fetching all users details");
			throw new VedantaWebException("Error while fetching all users details");
		}
	}

	public User getUser(long id) {

		int status;
		String url = String.format(URLConstants.GET_USER_BY_USERID, id);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user details");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<User>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all users details", 401);
			}

			log.error("Error while fetching all users details");
			throw new VedantaWebException("Error while fetching all users details");
		}
	}

	public User Update(User user) {

		if (user == null) {
			throw new VedantaWebException("User cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.UPDATE_USER_BY_USERID, user, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while update user.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<User>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while updating user by user id", 401);
			}

			log.error("Error while updating user by user id");
			throw new VedantaWebException("Error while updating user by user id");
		}
	}

	public Map<String, String> userValidation(User user) {
		Map<String, String> map = new HashMap<String, String>();
		String aulphaNumeric = "^[a-zA-Z0-9]+$";
		String aulphaNumericWithSpaceUnderScore = "^[a-zA-Z0-9 _-]+$";
		String numeric = "^[0-9]+$";
		String alphanumericWithUnderscoreAndDot = "^[a-zA-Z0-9._]+$";

		if (!user.getLoginId().matches(alphanumericWithUnderscoreAndDot)) {
			map.put("fieldName", "Login Id");
		} else if (!user.getName().matches(aulphaNumericWithSpaceUnderScore)) {
			map.put("fieldName", "User Name");
		} else if (!user.getEmployeeId().matches(aulphaNumeric)) {
			map.put("fieldName", "User Employee Id");
		} else if (user.getParentId() != null && !user.getParentId().isEmpty()) {
			if (!user.getParentId().matches(aulphaNumeric)) {
				map.put("fieldName", "User Parent Id");
			} else {
				map.put("fieldName", "validationSuccessfully");
			}
		} else {
			map.put("fieldName", "validationSuccessfully");
		}

		return map;
	}

	public User getCurrentUser() {

		int status;

		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_CURRENT_USER, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user details");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<User>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all users details", 401);
			}
			log.error("Error while fetching all users details");
			throw new VedantaWebException("Error while fetching all users details");
		}
	}

	public Boolean logout() {

		int status;

		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.LOGOUT, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while logout");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while logout", 401);
			}

			log.error("Error while logout");
			throw new VedantaWebException("Error while logout");
		}
	}

	public User getUserDetailByEmployeeId(String employeeId) {

		int status;
		String url = String.format(URLConstants.GET_USER_BY_EMPLOYEEID, employeeId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching User information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<User>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching User information");
			throw new VedantaWebException("Error while fetching User information");
		}
	}

	public User getUserByloginId(String loginId) {

		int status;
		String url = String.format(URLConstants.GET_USER_BY_LOGINID, loginId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user details");
			}

			if (response.get(VedantaConstant.DATA.toString()) != null) {
				String data = response.get(VedantaConstant.DATA).toString();
				return OBJECT_MAPPER.readValue(data, new TypeReference<User>() {
				});
			}
			return null;

		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all users details", 401);
			}

			log.error("Error while fetching all users details");
			throw new VedantaWebException("Error while fetching all users details");
		}
	}

	// get user by business id
	public List<User> userByPlantHeadAndBusinessUnitId(Long businessId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_USER_BY_PLANT_HEAD_AND_BUSINESS_ID, businessId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while user details by business id.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching user details by business id.");
		}
	}

	public HashSet<String> getAllUserDepartment(Long businessUnitId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_DEPARTMENT, businessUnitId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching user department.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<HashSet<String>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching  user department list", 401);
			}
			log.error("Error while fetching user department list");
			throw new VedantaWebException("Error while fetching user department  list");
		}
	}

	public HashSet<String> getAllUserOffice() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_USER_OFFICE, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching user office.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<HashSet<String>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching  user office list", 401);
			}
			log.error("Error while fetching user department list");
			throw new VedantaWebException("Error while fetching user office  list");
		}
	}

	public List<User> userByBusinessUnitId(Long businessId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_USER_BY_BUSSINESSID, businessId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while user");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching user", 401);
			}
			log.error("Error while fetching user");
			throw new VedantaWebException("Error while fetching user");
		}
	}

	public HashSet<UserDepartment> userDepartmentByBuId(Long businessId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_DEPARTMENT_BY_BUID, businessId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException(
						"API not responded while fetching all user-department by businessunitid.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<HashSet<UserDepartment>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while user-department details by business id.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching user-department details by business id.");
		}
	}
	
	
	
	
	public List<User> userByHumanResourceAndBusinessUnitId(Long businessId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_USER_BY_HUMAN_RESOURCE_AND_BUSINESS_ID, businessId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching user.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching user", 401);
			}
			log.error("Error while fetching user");
			throw new VedantaWebException("Error while fetching user");
		}
	}
	
	public List<UserDepartment> getUserDepartmentByLimit(Long businessUnitId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_DEPARTMENT_BY_LIMIT, businessUnitId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException(
						"API not responded while fetching all user-department by business unit id .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<UserDepartment>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while user-department details by business id.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching user-department details by business id.");
		}
	}
	
	public List<UserDepartment> getUserDepartmentByName(String name, Long buId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_DEPARTMENT_BY_NAME, name, buId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user department by name.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<UserDepartment>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all user department by name", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching all user department by name");
			throw new VedantaWebException("Error while fetching all user department by name");
		}
	}
	

}
