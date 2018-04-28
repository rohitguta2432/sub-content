package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.HumanResource;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HumanResourceService {
	
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;
	
	public HumanResource save(HumanResource entity) {
		if (entity == null) {
			throw new VedantaWebException("Human resource cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_HUMAN_RESOURCE, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save human resource.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<HumanResource>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while save human resource", 401);
			}

			log.error("API not responded while save human resource. ");
			throw new VedantaWebException("API not responded while save human resource.");
		}
	}
	
	
	public HumanResource checkExistingHumanResource(HumanResource humanResource) {

		int status;
		String url = String.format(URLConstants.CHECK_EXISTING_HUMAN_RESOURCE);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, humanResource, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching human resource.");
			}
			if (response.get(VedantaConstant.DATA.toString()) != null) {
				String data = response.get(VedantaConstant.DATA).toString();
				return OBJECT_MAPPER.readValue(data, new TypeReference<HumanResource>() {
				});
			}
			return null;
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching human resource ", 401);
			}
			log.error("Error while fetching human resource information");
			throw new VedantaWebException("Error while fetching human resource");
		}
	}
	
	
	public List<PlantHead> getAllHumanResource() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_HUMAN_RESOURCE, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all human resource");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<PlantHead>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all human resource", 401);
			}
			log.error("Error while fetching all human resource");
			throw new VedantaWebException("Error while fetching all human resource");
		}
	}
	
	
	
	public HumanResource getHumanResourceById(long humanResourceId) {

		int status;
		String url = String.format(URLConstants.GET_HUMAN_RESOURCE_DETAIL, humanResourceId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching human resource by id.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<HumanResource>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching human resource by id", 401);
			}
			log.error("Error while fetching human resource by id");
			throw new VedantaWebException("Error while fetching human resource by id");
		}
	}

	public HumanResource deleteHumanResource(long humanResourceId) {
		if (humanResourceId <= 0) {
			log.info("Invalid human resource id.");
			throw new VedantaWebException("Invalid human resource id.");
		}
		HumanResource humanResource = this.getHumanResourceById(humanResourceId);
		if (ObjectUtils.isEmpty(humanResource)) {
			throw new VedantaWebException("Invalid human resource id, no human resource found by id provided.");
		}
		try {
			if (humanResource.getStatus() != Constants.STATUS_DELETE) {
				humanResource.setStatus(Constants.STATUS_DELETE);
			}
			HumanResource deleteHumanResource = this.save(humanResource);
			return deleteHumanResource;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while deleting human resource  information", 401);
			}
			log.error("Error while deleting human resource information");
			throw new VedantaWebException("Error while deleting human resource information");
		}
	}

}
