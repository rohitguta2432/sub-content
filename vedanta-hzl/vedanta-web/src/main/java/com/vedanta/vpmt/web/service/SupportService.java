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
import com.vedanta.vpmt.model.Support;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SupportService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public Support saveSupport(Support support) {
		if (ObjectUtils.isEmpty(support)) {
			throw new VedantaWebException("form info cannot be empty");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_SUPPORT_DETAIL, support, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving Support form.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Support>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching Support form listing", 401);
			}
			log.error("Error while saving form information");
			throw new VedantaWebException("Error while saving Support form information");
		}
	}

	public List<Support> getSupportList() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_SUPPORT_LIST, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching support listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Support>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching support listing", 401);
			}
			log.error("Error while fetching support listing");
			throw new VedantaWebException("Error while fetching support listing");
		}
	}

	public Support updateSupport(Support support) {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.UPDATE_SUPPORT, support, null, HttpMethod.PUT);

			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while updating support listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Support>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while updating support listing", 401);
			}
			log.error("Error while updating support listing");
			throw new VedantaWebException("Error while updating support listing");
		}
	}

}
