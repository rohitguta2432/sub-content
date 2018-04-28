package com.vedanta.vpmt.web.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.web.VedantaWebException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorizationUtil {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final HttpUtil httpUtil;

	@Autowired
	public AuthorizationUtil(HttpUtil httpUtil) {
		this.httpUtil = httpUtil;
	}

	public JsonNode generateAuthToken(String userName, String password, String buId) {

		Map<String, String> headers = new HashMap<>();
		headers.put(VedantaConstant.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(Constants.ADMIN_REQUEST, "TRUE");
		String response = httpUtil.postOrPut(URLConstants.AUTHORIZATION_URL, getAuthPayload(userName, password, buId),
				headers, HttpMethod.POST);
		JsonNode responseData;
		try {
			responseData = parseResponse(response);

		} catch (IOException e) {
			e.printStackTrace();
			log.error("Error while parsing authorization token response");
			throw new VedantaWebException("Error while parsing authorization token response");
		}

		if (responseData == null) {
			log.error("No response obtained from Authorization api");
			throw new VedantaWebException("No response obtained from Authorization api");
		}
		return responseData;
	}

	private JsonNode parseResponse(String response) throws JsonParseException, JsonMappingException, IOException {
		return OBJECT_MAPPER.readTree(response);
	}

	private Map<String, String> getAuthPayload(String userName, String password, String buId) {
		Map<String, String> dataMap = new HashMap<>();
		dataMap.put(VedantaConstant.U_KEY, userName);
		dataMap.put(VedantaConstant.P_KEY, password);
		dataMap.put(VedantaConstant.B_KEY, buId);
		return dataMap;
	}
}
