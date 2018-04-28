package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.Vendor;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VendorService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public List<Vendor> getAllVendors() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_VENDORS_DETAILS, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all vendor details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Vendor>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all vendors details", 401);
			}

			log.error("Error while fetching all vendors details");
			throw new VedantaWebException("Error while fetching all vendors details");
		}
	}

	public Vendor getVendor(long id) {

		int status;
		String url = String.format(URLConstants.GET_VENDOR_BY_VENDORID, id);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all vendor details");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Vendor>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all vendors details", 401);
			}

			log.error("Error while fetching all vendors details");
			throw new VedantaWebException("Error while fetching all vendors details");
		}
	}

	public Vendor Update(Vendor vendor) {

		if (vendor == null) {
			throw new VedantaWebException("Vendor cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.UPDATE_VENDOR_BY_VENDOR_ID, vendor, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while update vendor.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Vendor>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while updating vendor by vendor id", 401);
			}

			log.error("Error while updating vendor by vendor id");
			throw new VedantaWebException("Error while updating vendor by vendor id");
		}
	}

	public Map<String, String> vendorValidation(Vendor vendor) {
		Map<String, String> map = new HashMap<String, String>();
		String aulphaNumeric = "^[a-zA-Z0-9]+$";
		String aulphaNumericWithSpaceUnderScore = "^[a-zA-Z0-9 _-]+$";
		String numeric = "^[0-9]+$";
		/*
		 * if(!vendor.getLoginId().matches(aulphaNumeric)){ map.put("fieldName"
		 * ,"Login Id"); }else
		 * if(!vendor.getName().matches(aulphaNumericWithSpaceUnderScore)){
		 * map.put("fieldName" ,"Vendor Name"); }else
		 * if(!vendor.getEmployeeId().matches(numeric)){ map.put("fieldName"
		 * ,"Vendor Employee Id"); }else if(vendor.getParentId()!=null &&
		 * !vendor.getParentId().isEmpty()){
		 * if(!vendor.getParentId().matches(aulphaNumeric)){ map.put("fieldName"
		 * ,"Vendor Parent Id"); }else{ map.put("fieldName" ,"validationSuccessfully");
		 * } }else{ map.put("fieldName" ,"validationSuccessfully"); }
		 */

		return map;
	}

	public List<Vendor> getAllVendorsByBusinessUnitId(Long buId) {

		int status;
		String url = String.format(URLConstants.GET_VENDOR_BY_BUSINESS_UNIT_ID, buId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all vendors by businessunit id");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Vendor>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all vendors by businessunit id", 401);
			}

			log.error("Error while fetching all vendors by businessunit id");
			throw new VedantaWebException("Error while fetching all vendors by businessunit id");
		}
	}

}
