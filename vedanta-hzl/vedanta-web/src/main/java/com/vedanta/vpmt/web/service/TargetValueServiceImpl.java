package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.TargetValue;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Service("TargetValueService")
@Slf4j
public class TargetValueServiceImpl implements TargetValueService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	RestServiceUtil restServiceUtil;

	@Override
	public TargetValue get(long id) {
		int status;
		String url = String.format(URLConstants.GET_TARGET_VALUE_BY_ID, id);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form fields details");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<TargetValue>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form details", 401);
			}
			log.error("Error while fetching form details");
			throw new VedantaWebException("Error while fetching form fields details");
		}
	}

	@Override
	public TargetValue save(TargetValue entity) {
		if (entity == null) {
			throw new VedantaWebException("Form fields cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_TARGET_VALUE, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save form fields.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<TargetValue>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form details", 401);
			}

			log.error(" ");
			throw new VedantaWebException("");
		}
	}

	@Override
	public TargetValue update(long id, TargetValue entity) {
		if (entity == null && id <= 0) {
			throw new VedantaWebException("Form fields cannot be null");
		}
		entity.setId(id);
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_TARGET_VALUE, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save form fields.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<TargetValue>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form details", 401);
			}
			log.error(" ");
			throw new VedantaWebException("");
		}

	}

	@Override
	public List<TargetValue> getTargetValues() {
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_TARGET_VALUE, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all form fields details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<TargetValue>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all form fields details", 401);
			}
			log.error("Error while fetching all form fields details");
			throw new VedantaWebException("Error while fetching all form fields details");
		}
	}

	@Override
	public TargetValue deleteTargetValue(long targetvalueId) {
		if (targetvalueId <= 0) {
			log.info("Invalid TargetValue id.");
			throw new VedantaWebException("Invalid TargetValue id.");
		}
		TargetValue targetvalue = this.get(targetvalueId);
		if (ObjectUtils.isEmpty(targetvalue)) {
			throw new VedantaWebException("Invalid TargetValue found by id provided.");
		}
		return targetvalue;
	}
}
