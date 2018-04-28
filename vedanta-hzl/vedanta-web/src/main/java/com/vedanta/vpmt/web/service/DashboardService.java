package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DashboardService {

	@Autowired
	private RestServiceUtil restServiceUtil;
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public Map<String, Object> getNewDashBoardDataByBuId(Long bussinessId) {

		int status;
		try {
			String url = null;
			url = URLConstants.NEW_DASHBOARD_DATA_BU_ID + "bussinessId=" + bussinessId;
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException(" " + status);
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			if (errorCode.equals("403")) {
				throw new VedantaWebException("API not responded while fetching new dashboard data.", 403);
			}

			log.error("Error while fetching new dashboard data");
			throw new VedantaWebException("Error while fetching new dashboard data");
		}
	}

	public List<SubCategory> getSubcategoryByBuId(Long bussinessId) {

		int status;
		String url = String.format(URLConstants.GET_ALL_SUBCATEGORIES_BY_BU_ID, bussinessId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<SubCategory>>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching SubCategory listing", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}
			log.error("Error while fetching SubCategory information");
			throw new VedantaWebException("Error while fetching SubCategory information");
		}
	}

	public Map<String, Object> getNewDashBoardDataBySearchKey(Long bussinessId, Long plantId, Long categoryId,
			Long subCategoryId, Long contractId, String fromDate, String toDate) {

		int status;
		try {
			String url = null;
			if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
				url = URLConstants.NEW_DASHBOARD_DATA_BU_ID + "bussinessId=" + bussinessId + "&plantId=" + plantId
						+ "&categoryId=" + categoryId + "&subCategoryId=" + subCategoryId + "&contractId=" + contractId
						+ "&fromDate=" + URLEncoder.encode(fromDate, "UTF-8") + "&toDate="
						+ URLEncoder.encode(toDate, "UTF-8");
			} else {
				url = URLConstants.NEW_DASHBOARD_DATA_BU_ID + "bussinessId=" + bussinessId + "&plantId=" + plantId
						+ "&categoryId=" + categoryId + "&subCategoryId=" + subCategoryId + "&contractId=" + contractId;

			}
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching new _new dashboard data.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching _new dashBoard data", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}
			log.error("Error while fetching new dashboard data");
			throw new VedantaWebException("Error while fetching _new dashBoard data data");
		}
	}

	public List<Plant> getPlantByPlantCode(String plantCode) {

		int status;
		try {
			String url = String.format(URLConstants.GET_PLANT_BY_PLANT_CODE, plantCode);
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

	public List<PlantHead> getPlantHeadByEmployeeId(String employeeId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_PLANTHEAD_BY_EMPLOYEEID, employeeId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching plant.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<PlantHead>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plantHead.", 401);
			}
			log.error("Error while fetching plant.");
			throw new VedantaWebException("Error while fetching plantHead.");
		}
	}
}
