package com.vedanta.vpmt.web.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.cache.CacheService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestServiceUtil {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final HttpUtil httpUtil;

	private final AuthorizationUtil authorizationUtil;

	private final CacheService<String, Object> cacheService;

	private static int retryCount = 0;

	@Autowired
	public RestServiceUtil(HttpUtil httpUtil, AuthorizationUtil authorizationUtil,
			@Qualifier("memcacheService") CacheService<String, Object> cacheService) {
		this.httpUtil = httpUtil;
		this.authorizationUtil = authorizationUtil;
		this.cacheService = cacheService;
	}

	public JsonNode makeRequest(String url, Object payload, Map<String, Object> parameters, HttpMethod method) {

		if (StringUtils.isEmpty(url)) {
			log.info("Url cannot be null/empty");
			throw new IllegalArgumentException("Url cannot be null/empty");
		}

		if ((HttpMethod.POST == method || HttpMethod.PUT == method) && ObjectUtils.isEmpty(payload)) {
			log.info("Cannot make post or put request with empty/null payload");
			throw new IllegalArgumentException("Cannot make post or put request with empty/null payload");
		}

		Map<String, String> headers = createHeaders();

		String response = null;

		try {
			if (HttpMethod.GET == method) {
				response = httpUtil.get(url, headers, method);
			} else if (HttpMethod.POST == method || HttpMethod.PUT == method) {
				response = httpUtil.postOrPut(url, payload, headers, method);
			}
		} catch (VedantaWebException | IOException e) {
			log.error("Error while making request to rest-api url");
			e.printStackTrace();
			throw new VedantaWebException("Error while making request to rest-api url");
		}

		JsonNode responseJson;
		try {
			responseJson = parseResponse(response);
		} catch (VedantaWebException | IOException e) {
			log.error("Error while parsing response for url");
			e.printStackTrace();
			throw new VedantaWebException("Error while parsing response for url");
		}

		int status = responseJson.get(VedantaConstant.STATUS_CODE).asInt();
		if (status == 401) {
			log.info("The authorization token has expired, hence generating new one.");
			if (++retryCount > 2) {
				generateAuthToken();
				retryCount = 0;
				makeRequest(url, payload, parameters, method);
			} else {
				log.error("The rest service seems to be down, aborting process.");
				throw new VedantaWebException("401", 401);
			}
		}
		if (status == 403) {
			log.info("The authorization token has expired, hence generating new one.");

			log.error("The rest service seems to be down, aborting process.");
			throw new VedantaWebException("403", 403);
		}
		return responseJson;
	}

	public void writeToStream(String url, OutputStream outputStream) {

		Map<String, String> headers = createHeaders();
		headers.remove(VedantaConstant.CONTENT_TYPE);
		httpUtil.fetchDataAndWriteToStream(url, headers, outputStream);

	}

	private JsonNode parseResponse(String responseAsString) throws JsonProcessingException, IOException {
		return OBJECT_MAPPER.readTree(responseAsString);
	}

	private Map<String, String> createHeaders() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(VedantaConstant.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		Object authenticationToken = session.getAttribute(VedantaConstant.AUTH_HEADER);

		if (ObjectUtils.isEmpty(authenticationToken)) {
			log.info("Authentication token is not generate hence generating it.");
			generateAuthToken();
		}
		headerMap.put(VedantaConstant.AUTH_HEADER, session.getAttribute(VedantaConstant.AUTH_HEADER).toString());
		headerMap.put(Constants.ADMIN_REQUEST, "TRUE");
		return headerMap;
	}

	private void generateAuthToken() {
		try {

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// //(auth.getName());
			//// (auth.getCredentials().toString());
			/* String buId = (String)cacheService.get(Constants.USER_BU_ID); */
			String buId = session.getAttribute(Constants.USER_BU_ID).toString();

			JsonNode responseData = authorizationUtil.generateAuthToken(auth.getName(),
					auth.getCredentials().toString(), buId);
			JsonNode dataNode = responseData.get(VedantaConstant.DATA);
			String authorizationToken = dataNode.get(Constants.AUTH_TOKEN).asText();
			// cacheService.put(VedantaConstant.AUTH_HEADER, authorizationToken);

			session.setAttribute(VedantaConstant.AUTH_HEADER, authorizationToken);

		} catch (VedantaWebException | NullPointerException e) {
			log.info("Exception on generate AuthToken.");
			throw new VedantaWebException("401", 401);
		}
	}

	public JsonNode sendMailRequest(String url, Map<String, String> payload, Map<String, Object> parameters,
			HttpMethod method) {

		if (StringUtils.isEmpty(url)) {
			log.info("Url cannot be null/empty");
			throw new IllegalArgumentException("Url cannot be null/empty");
		}

		if ((HttpMethod.POST == method || HttpMethod.PUT == method) && ObjectUtils.isEmpty(payload)) {
			log.info("Cannot make post or put request with empty/null payload");
			throw new IllegalArgumentException("Cannot make post or put request with empty/null payload");
		}

		Map<String, String> headers = createHeaders();

		String response = null;

		try {
			if (HttpMethod.GET == method) {
				response = httpUtil.get(url, headers, method);
			} else if (HttpMethod.POST == method || HttpMethod.PUT == method) {
				response = httpUtil.postOrPut(url, payload, headers, method);
			}
		} catch (VedantaWebException | IOException e) {
			log.error("Error while making request to rest-api url");
			e.printStackTrace();
			throw new VedantaWebException("Error while making request to rest-api url");
		}

		JsonNode responseJson;
		try {
			responseJson = parseResponse(response);
		} catch (VedantaWebException | IOException e) {
			log.error("Error while parsing response for url");
			throw new VedantaWebException("Error while parsing response for url");
		}

		int status = responseJson.get(VedantaConstant.STATUS_CODE).asInt();
		if (status == 401) {
			log.info("The authorization token has expired, hence generating new one.");
			if (++retryCount > 2) {
				generateAuthToken();
				retryCount = 0;
				makeRequest(url, payload, parameters, method);
			} else {
				log.error("The rest service seems to be down, aborting process.");
				throw new VedantaWebException("The rest service seems to be down, aborting process.", 401);
			}
		}
		return responseJson;
	}

}
