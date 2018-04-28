package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.Notification;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public Notification saveNotification(ScoreCardGroupUser entity) {

		if (entity == null) {
			throw new VedantaWebException("ScoreCardGroupUser object cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_NOTIFICATION, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while saving Notification details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Notification>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responded while save Notification data.", 401);
			}

			log.error("API not responded while save Notification data. ");
			throw new VedantaWebException("API not responding while saving Notification details.");
		}
	}

	public List<Notification> getAllNotification() {

		int status;
		try {
			String url = String.format(URLConstants.GET_NOTIFICATION);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while fetching notification");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Notification>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error occurred while fetching notification.", 401);
			}
			log.error("Error occurred while fetching notification");
			throw new VedantaWebException("Error occurred while fetching notification");
		}
	}

	public void submitNotification(ScoreCard entity) {

		if (entity == null) {
			throw new VedantaWebException("ScoreCard object cannot be null");
		}
		int status;

		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SUBMIT_NOTIFICATION, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while saving Notification details.");
			}

		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responding while saving Notification details.", 401);
			}

			log.error("API not responding while saving Notification details.");
			throw new VedantaWebException("API not responding while saving Notification details.");
		}
	}

	public List<Notification> getNotificationById() {

		int status;
		try {
			String url = String.format(URLConstants.GET_NOTIFICATIONBYID);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while fetching notification details");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Notification>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responding while saving Notification details.", 401);
			}

			log.error("Error occurred while fetching the notification details");
			throw new VedantaWebException("Error occurred while fetching the notification deatils");
		}
	}

	public Long getNotificationDetails(long id) {

		int status;
		try {
			String url = String.format(URLConstants.GET_NOTIFICATION_BY_CONTRACT_ID, id);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while fetching notification");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Long>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responding while saving Notification details.", 401);
			}

			log.error("Error occurred while fetching notification details");
			throw new VedantaWebException("Error occurred while fetching notification details");
		}
	}

	public List<Notification> getAllnotification() {

		int status;
		try {
			String url = String.format(URLConstants.GET_NOTIFICATION_ALL);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while fetching notification");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Notification>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responding while saving Notification details.", 401);
			}

			log.error("Error occurred while fetching notification details");
			throw new VedantaWebException("Error occurred while fetching notification details");
		}
	}

	public void changeStatus() {

		int status;
		try {
			String url = String.format(URLConstants.CHANGE_STATUS);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while fetching notification details");
			}

		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responding while saving Notification details.", 401);
			}

			log.error("Error occurred while fetching notification");
			throw new VedantaWebException("Error occurred while fetching notification details");
		}
	}

	public void checked(String id) {

		int status;
		try {
			String url = String.format(URLConstants.CHANGE_STATUS_NOTIFICATION, id);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responding while fetching notification details");
			}

		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responding while saving Notification details.", 401);
			}

			log.error("Error occurred while fetching notification");
			throw new VedantaWebException("Error occurred while fetching notification details");
		}
	}
}
