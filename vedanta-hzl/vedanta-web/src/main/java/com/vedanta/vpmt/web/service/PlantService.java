package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlantService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private RestServiceUtil restServiceUtil;

	public List<Plant> getAllPlants() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_PLANTS, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plants listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Plant>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plants listing", 401);
			}
			log.error("Error while fetching plants listing");
			throw new VedantaWebException("Error while fetching plants listing");
		}
	}

	public Plant getPlantById(Long plantId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_PLANT_BY_ID, plantId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Plant>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plant.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching plant.");
		}
	}

	public List<Plant> getAllCurrentBusinessUnitPlants() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_CURRENT_BUSINESS_UNIT_PLANTS, null,
					null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plants listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Plant>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching current business unit plants listing", 401);
			}
			log.error("Error while fetching current business unit plants listing");
			throw new VedantaWebException("Error while fetching current business unit plants listing");
		}
	}

	public List<Plant> getAllPlantsCurrentUser() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_PLANTS_CURRENT_USER, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plants listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Plant>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching current business unit plants listing", 401);
			}
			log.error("Error while fetching current business unit plants listing");
			throw new VedantaWebException("Error while fetching current business unit plants listing");
		}
	}

	public List<BusinessUnit> getAllBusinessUnits() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_BUSINESS_UNIT, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching Business Unit.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<BusinessUnit>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching  business unit list", 401);
			}
			log.error("Error while fetching  business unit list");
			throw new VedantaWebException("Error while fetching business unit  list");
		}
	}

	public List<Plant> getPlantByBusinessId(Long businessId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_PLANT_BY_BUSINESS_ID, businessId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Plant>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plant.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching plant.");
		}
	}

	public List<Plant> getPlantByPlantName(String plantName) {

		int status;
		try {
			String url = String.format(URLConstants.GET_PLANT_BY_PLANT_NAME, plantName);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Plant>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plant.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching plant.");
		}
	}

	public List<BusinessUnit> getAllBusinessUnitsByRole() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_BUSINESS_UNIT_BY_ROLE, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching Business Unit.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<BusinessUnit>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching  business unit list", 401);
			}
			log.error("Error while fetching  business unit list");
			throw new VedantaWebException("Error while fetching business unit  list");
		}
	}

	public List<Plant> getPlantByPlantCodeAndBuId(String plantCode, Long businessUnitId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_PLANT_BY_PLANTCODE_AND_BUID, plantCode, businessUnitId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Plant>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plant.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching plant.");
		}
	}

}
