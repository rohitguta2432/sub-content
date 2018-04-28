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
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlantHeadService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public List<PlantHead> getAllPlantHead() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_PLANT_HEAD, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<PlantHead>>() {
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

	public PlantHead save(PlantHead entity) {
		if (entity == null) {
			throw new VedantaWebException("PlantHead cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_PLANT_HEAD, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save plant head.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<PlantHead>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while save plant head", 401);
			}

			log.error("API not responded while save FormSaved. ");
			throw new VedantaWebException("API not responded while save plant head.");
		}
	}

	public PlantHead getPlantHeadById(long plantHeadId) {

		int status;
		String url = String.format(URLConstants.GET_PLANT_HEAD_DETAIL, plantHeadId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant head by id.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<PlantHead>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plant head by id", 401);
			}
			log.error("Error while fetching plant head information");
			throw new VedantaWebException("Error while fetching plant head by id");
		}
	}

	public PlantHead deletePlantHead(long id) {
		if (id <= 0) {
			log.info("Invalid template id.");
			throw new VedantaWebException("Invalid  plant id.");
		}
		PlantHead plantHead = this.getPlantHeadById(id);
		if (ObjectUtils.isEmpty(plantHead)) {
			throw new VedantaWebException("Invalid plant id, no plant plant found by id provided.");
		}
		try {
			if (plantHead.getStatus() != Constants.STATUS_DELETE) {
				plantHead.setStatus(Constants.STATUS_DELETE);
			}
			PlantHead deletePlantHead = this.save(plantHead);
			return deletePlantHead;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while deleting plant head information", 401);
			}
			log.error("Error while deleting plant head information");
			throw new VedantaWebException("Error while deleting plant head information");
		}
	}

	public PlantHead checkExistingPlantHead(PlantHead plantHead) {

		int status;
		String url = String.format(URLConstants.CHECK_EXISTING_PLANT_HEAD);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, plantHead, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant head.");
			}
			if (response.get(VedantaConstant.DATA.toString()) != null) {
				String data = response.get(VedantaConstant.DATA).toString();
				return OBJECT_MAPPER.readValue(data, new TypeReference<PlantHead>() {
				});
			}
			return null;
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plant head ", 401);
			}
			log.error("Error while fetching plant head  information");
			throw new VedantaWebException("Error while fetching plant head");
		}
	}

	public List<PlantHead> getAllPlantHeadByEmployeeId(String employeeId) {

		int status;
		String url = String.format(URLConstants.GET_PLANT_HEAD_BY_EMPLOYEEID, employeeId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<PlantHead>>() {
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

	public List<PlantHead> getAllPlantHeadByPlantCode(String plantCode) {

		int status;
		String url = String.format(URLConstants.GET_PLANT_HEAD_BY_PLANTCODE, plantCode);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all user details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<PlantHead>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all Plant Heads", 401);
			}
			log.error("Error while fetching all Plant Heads details");
			throw new VedantaWebException("Error while fetching all Plant Heads details");
		}
	}

}
