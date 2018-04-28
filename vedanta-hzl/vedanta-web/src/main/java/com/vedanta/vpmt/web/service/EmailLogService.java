package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.EmailLog;
import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.Vendor;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailLogService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private RestServiceUtil restServiceUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailTemplateService emailTemplateservice;

	@Autowired
	private VendorService vendorService;
	
	@Value("${scorecard.email.user.url}")
	private String scorecardUrl;
	
	@Value("${form.email.user.url}")
	private String formUrl;

	public EmailLog save(EmailLog entity) {	
		if (entity == null) {
			throw new VedantaWebException("Email Log cannot be null");
		}
		int status;
		try {

			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_EMAIL_LOG_DETAILS, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save Email Log Details.");
			}

			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<EmailLog>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("API not responded while save Email Log Dtails.");
			throw new VedantaWebException("API not responded while save Email Log Details.");
		}
	}

	@Async
	public String sendEmailNotification(String from, String to, String subject, String text) {

		String message = "";

		if (to == null) {
			message = "EmailAddress can not be null";
		}

		int status;
		HashMap<String, String> map = new HashMap<>();
		map.put("from", from);
		map.put("to", to);
		map.put("subject", subject);
		map.put("text", text);
		try {
			JsonNode response = restServiceUtil.sendMailRequest(URLConstants.SEND_EMAIL, map, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {

				message = "API not responded while sending email notification";

			}
			String data = response.get(VedantaConstant.DATA).toString();

			String mailStatus = OBJECT_MAPPER.readValue(data, new TypeReference<String>() {
			});

			message = mailStatus;

		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("API not responded while sending email notification.");
			message = "API not responded while sending email notification";

		}
		return message;
	}

	@Async
	public String sendEmailNotificationForScoreCardAssign(ScoreCardGroupUser scoreCardGroupUser) {

		try {

			if (ObjectUtils.isEmpty(scoreCardGroupUser) || StringUtils.isEmpty(scoreCardGroupUser.getEmployeeId())
					|| ObjectUtils.isEmpty(scoreCardGroupUser.getVendorId())) {
				throw new VedantaWebException("scoreCardGroupUser cannot be null for send email notification.");
			}

			User currentUser = userService.getCurrentUser();
			if (ObjectUtils.isEmpty(currentUser) || StringUtils.isEmpty(currentUser.getEmail())) {
				throw new VedantaWebException("current user null on send email scorecard assign.");
			}

			Vendor vendorDetails = vendorService.getVendor(scoreCardGroupUser.getVendorId());
			if (ObjectUtils.isEmpty(vendorDetails)) {
				throw new VedantaWebException("vendor details null on send email scorecard assign.");
			}

			User assignedUser = emailTemplateservice.getUserDetailByEmployeeId(scoreCardGroupUser.getEmployeeId());
			if (ObjectUtils.isEmpty(assignedUser) || StringUtils.isEmpty(assignedUser.getEmail())
					|| ObjectUtils.isEmpty(assignedUser.getId())) {
				throw new VedantaWebException("assignedUser null on send email scorecard assign.");
			}

			EmailTemplate getEmailById = emailTemplateservice.getEmailTemplateDetail(1L);

			if (ObjectUtils.isEmpty(getEmailById) || StringUtils.isEmpty(getEmailById.getMsgContent())) {
				throw new VedantaWebException("email template null on send email scorecard assign.");
			}

			String message = getEmailById.getMsgContent();
			if (StringUtils.isNotBlank(message)) {

				if (StringUtils.isEmpty(assignedUser.getName())) {
					message = message.replace("[[Name]]", "");
				} else {
					message = message.replace("[[Name]]", assignedUser.getName());

				}
				if (StringUtils.isEmpty(scoreCardGroupUser.getCategoryName())) {
					message = message.replace("[[CategoryName]]", "");
				} else {
					message = message.replace("[[CategoryName]]", scoreCardGroupUser.getCategoryName());

				}
				if (StringUtils.isEmpty(scoreCardGroupUser.getSubCategoryName())) {
					message = message.replace("[[SubCategoryName]]", "");
				} else {
					message = message.replace("[[SubCategoryName]]", scoreCardGroupUser.getSubCategoryName());

				}

				if (StringUtils.isEmpty(scoreCardGroupUser.getEmailMessage())) {
					message = message.replace("[[Message]]", "");
				} else {
					message = message.replace("[[Message]]", scoreCardGroupUser.getEmailMessage());

				}

				if (StringUtils.isEmpty(scoreCardGroupUser.getContractManager())) {
					message = message.replace("[[Contract Manager]]", "");
				} else {
					message = message.replace("[[Contract Manager]]", scoreCardGroupUser.getContractManager());

				}
				if (StringUtils.isEmpty(vendorDetails.getName())) {
					message = message.replace("[[Vendor Name]]", "");
				} else {
					message = message.replace("[[Vendor Name]]", vendorDetails.getName());

				}
				if (StringUtils.isEmpty(currentUser.getName())) {
					message = message.replace("[[fromName]]", "");
				} else {
					message = message.replace("[[fromName]]", currentUser.getName());

				}
				Long contractId = scoreCardGroupUser.getScorecardId();
				String url = scorecardUrl + contractId;
				String link = "<a href='" + url + "'>Click Here</a>";
				message = message.replace("[[link]]", link);

				return sendEmailNotification(currentUser.getEmail(), assignedUser.getEmail(), "Scorecard Notification",
						message);
			}

			return null;
		} catch (VedantaWebException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("API not responded while sending email notification on scorecard assign.",
						401);
			}
			log.error("API not responded while sending email notification on scorecard assign.");
			throw new VedantaWebException("API not responded while sending email notification on scorecard assign.");
		}

	}

	public List<User> getAllUsersNotAdmin() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.ALL_USER_NOT_ADMIN, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching ALL_USER_NOT_ADMIN.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching ALL_USER_NOT_ADMIN listing");
		}
	}

	public List<Contract> getContractDetailseDetail(Long PlantId) {

		int status;
		String url = String.format(URLConstants.ALL_CONTRACTS_BY_PLANTID, PlantId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching Contract  information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Contract>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching Email Template information");
			throw new VedantaWebException("Error while fetching Email Template information");
		}
	}

	public List<Contract> getContractDetailsDetailByVendorId(Long vendorId) {

		int status;
		String url = String.format(URLConstants.ALL_CONTRACTS_BY_VENDOR_ID, vendorId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching Contract  information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Contract>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching Email Template information");
			throw new VedantaWebException("Error while fetching Email Template information");
		}
	}

	public Plant getAllPlantDetailByPlantId(Long planIdId) {

		int status;
		String url = String.format(URLConstants.GET_ALL_PLANT_BY_PLANTID, planIdId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching Plant  information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Plant>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching plant listing", 401);
			}

			log.error("Error while fetching plant information");
			throw new VedantaWebException("Error while fetching plant information");
		}
	}

	public List<Form> getFormDetailsDetail(Long categoryId, Long subCategoryId) {

		int status;
		String url = String.format(URLConstants.GET_ALL_FORMS_CATEGORYID, categoryId, subCategoryId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching Forms  information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Form>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching Email Template information");
			throw new VedantaWebException("Error while fetching Form information");
		}
	}
}
