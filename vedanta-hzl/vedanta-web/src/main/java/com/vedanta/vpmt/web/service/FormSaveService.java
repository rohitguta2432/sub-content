package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 23/09/17.
 */
@Component
@Slf4j
public class FormSaveService {
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public Map<String, Object> getFormSavedFormByFormSavedId(long formSavedId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_FORM_SAVED_FORM_BY_FORM_SAVED_ID, formSavedId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching formSaved form.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching formSaved form");
			throw new VedantaWebException("Error while fetching formSaved form");
		}
	}

	public Map<String, Object> getComplainceFormSavedFormByFormSavedId(long formSavedId, long scoreCardId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_FORM_SAVED_COMPLIANCE_FORM_BY_FORM_SAVED_ID, formSavedId,
					scoreCardId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching formSaved form.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching formSaved form");
			throw new VedantaWebException("Error while fetching formSaved form");
		}
	}

	public Map<String, Object> getFormSavedBaseData() {
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_SCORECARD_BASE_DATA, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard base data.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching scorecard template");
			throw new VedantaWebException("Error while fetching scorecard template");
		}
	}

	public FormSaved save(FormSaved entity) {
		if (entity == null) {
			throw new VedantaWebException("FormSaved cannot be null");
		}

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_FORM_SAVED, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save FormSaved.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormSaved>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("API not responded while save FormSaved. ");
			throw new VedantaWebException("API not responded while save FormSaved.");
		}
	}

	public FormGroupUser saveFormGroupUser(FormGroupUser entity) {

		if (entity == null) {
			throw new VedantaWebException("FormGroupUser cannot be null");
		}
		int status;

		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_FORM_GROUP_USER, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save FormGroupUser.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormGroupUser>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("API not responded while save FormGroupUser. ");
			throw new VedantaWebException("API not responded while save FormGroupUser.");
		}
	}

	public List<FormSaved> getAllFormSaved() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_FORM_SAVED, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all formSaved details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<FormSaved>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching all FormSaved details");
			throw new VedantaWebException("Error while fetching all FormSaved details");
		}
	}

	public List<FormSaved> getAllFormSavedByStatus(int statusValue) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_FORM_SAVED_STATUS, statusValue);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all FormSaved details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<FormSaved>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching all FormSaved details");
			throw new VedantaWebException("Error while fetching all FormSaved details");
		}
	}

	public FormSaved populate(FormSaved entity) {
		if (entity == null) {
			throw new VedantaWebException("FormSaved cannot be null");
		}

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.POPULATE_FORM_SAVE, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save FormSaved.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormSaved>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("API not responded while save FormSaved. ");
			throw new VedantaWebException("API not responded while save FormSaved.");
		}
	}

	public List<FormSaved> getAllFormSavedByPlantCodeAndBuIdAndStatus(int statusValue, Long bussinessId,
			String PlantCode) {

		int status;
		try {
			String url = String.format(URLConstants.FORM_SAVE_BY_BUID_PLANTCODE_STATUS, statusValue, bussinessId,
					PlantCode);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all FormSaved details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<FormSaved>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching all FormSaved details");
			throw new VedantaWebException("Error while fetching all FormSaved details");
		}
	}
	
	
	public List<User> getAllUserNotAdminAndHrByLimit(Long buId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_NOT_ADMIN_AND_HR_BY_LIMIT, buId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all User not admin and hr .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all User not admin and hr", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching all User not admin and hr");
			throw new VedantaWebException("Error while fetching all User not admin and hr");
		}
	}
	
	
	public List<User> getAllHrUserByLimit(Long buId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_HR_USER_BY_LIMIT, buId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all hr user .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all hr user", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching all User not admin and hr");
			throw new VedantaWebException("Error while fetching all hr user");
		}
	}
	
	public List<User> getAllUserNotAdminAndHrByName(String name, Long buId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_NOT_ADMIN_AND_HR_BY_NAME, name, buId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user not admin and hr by name.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all user not admin and hr by name", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching all user not admin and hr by name");
			throw new VedantaWebException("Error while fetching all user not admin and hr by name");
		}
	}
	
	
	public List<User> getAllHrUserByName(String name, Long buId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_HR_USER_BY_NAME, name, buId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user not admin and hr by name.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all user not admin and hr by name", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching all user not admin and hr by name");
			throw new VedantaWebException("Error while fetching all user not admin and hr by name");
		}
	}
	
	
}
