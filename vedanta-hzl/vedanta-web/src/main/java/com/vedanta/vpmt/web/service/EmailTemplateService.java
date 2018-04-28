package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

@Service
public class EmailTemplateService {

	public static final Logger logger = LoggerFactory.getLogger(EmailTemplateService.class);
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private RestServiceUtil restServiceUtil;

	public EmailTemplate save(EmailTemplate entity) {
		if (entity == null) {
			throw new VedantaWebException("EmailTemplate cannot be null");
		}
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_EMAIL_TEMPLATE, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save EmailTemplate.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<EmailTemplate>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				logger.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			logger.error("API not responded while save Email Template. ");
			throw new VedantaWebException("API not responded while save Email Template.");
		}
	}

	public List<EmailTemplate> getAllTemplate() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_EMAILTEMPLATE_DETAIL, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<EmailTemplate>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				logger.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			logger.error("Error while fetching form listing");
			throw new VedantaWebException("Error while fetching form listing");
		}
	}

	public EmailTemplate getEmailTemplateDetail(long Id) {

		int status;
		String url = String.format(URLConstants.GET_EMAILTEMPLATE_DETAILBYID, Id);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching Email Template information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<EmailTemplate>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				logger.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			logger.error("Error while fetching Email Template information");
			throw new VedantaWebException("Error while fetching Email Template information");
		}
	}

	public EmailTemplate update(long id, EmailTemplate entity) {
		if (entity == null && id <= 0) {
			throw new VedantaWebException("Email Template cannot be null");
		}
		entity.setId(id);
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.UPDATE_EMAIL_TEMPLATE, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while Updating Email Template.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<EmailTemplate>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				logger.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			logger.error("Error while Updating Email Template ");
			throw new VedantaWebException("Error while updating Email Template");
		}
	}

	public EmailTemplate deleteEmailTemplate(long EmailTemplateId) {
		if (EmailTemplateId <= 0) {
			logger.info("Invalid field id.");
			throw new VedantaWebException("Invalid field id.");
		}
		EmailTemplate emailTemplate = this.getEmailTemplateDetail(EmailTemplateId);

		if (ObjectUtils.isEmpty(emailTemplate)) {
			throw new VedantaWebException("Invalid EmailTemplate id, no field found by id provided.");
		}
		try {
			if (emailTemplate.getStatus() != Constants.STATUS_DELETE) {
				emailTemplate.setStatus(Constants.STATUS_DELETE);
			}
			EmailTemplate deletedemailTemplate = this.save(emailTemplate);
			return deletedemailTemplate;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				logger.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			logger.error("Error while deleting email template information");
			throw new VedantaWebException("Error while deleting email template information");
		}
	}

	public User getUserDetailByEmployeeId(String employeeId) {

		int status;
		String url = String.format(URLConstants.GET_USER_BY_EMPLOYEEID, employeeId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching User information.");
			}

			if (response.get(VedantaConstant.DATA.toString()) != null) {
				String data = response.get(VedantaConstant.DATA).toString();
				return OBJECT_MAPPER.readValue(data, new TypeReference<User>() {
				});
			}
			return null;

		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				logger.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			logger.error("Error while fetching User information");
			throw new VedantaWebException("Error while fetching User information");
		}
	}

}
