package com.vedanta.vpmt.contollers.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.EmailLog;
import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.service.EmailLogService;
import com.vedanta.vpmt.service.EmailTemplateService;
import com.vedanta.vpmt.service.MailSenderServices;

@RestController
@RequestMapping(value = "emailTemplate", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailTemplateController {
	private static final Logger logger = LoggerFactory.getLogger(EmailTemplateController.class);

	@Autowired
	private MailSenderServices mailSenderServices;

	@Autowired
	private EmailTemplateService emailTemplateService;

	@Autowired
	private EmailLogService emailLogService;

	@RequestMapping(value = "{emailTemplateId}", method = RequestMethod.GET)
	public ResponseEntity<Response<EmailTemplate>> getEmailTemplateById(@PathVariable("emailTemplateId") Long id) {

		EmailTemplate emailTemplate = null;
		try {
			emailTemplate = emailTemplateService.get(id);
		} catch (Exception e) {
			logger.error("Error occurred in fetching of email template id " + id);
			throw e;
		}
		return new ResponseEntity<Response<EmailTemplate>>(new Response<EmailTemplate>(HttpStatus.OK.value(),
				"Email template fetched successfully", emailTemplate), HttpStatus.OK);
	}

	@RequestMapping(value = "getAll", method = RequestMethod.GET)
	public ResponseEntity<Response<List<EmailTemplate>>> getAllEmailTemplate() {

		List<EmailTemplate> emailTemplate = new ArrayList<>();
		try {
			emailTemplate = emailTemplateService.getAllEmailTemplate();
		} catch (Exception e) {
			logger.error("Error occurred in fetching of email template ");
			throw e;
		}
		return new ResponseEntity<Response<List<EmailTemplate>>>(new Response<List<EmailTemplate>>(
				HttpStatus.OK.value(), "Email template fetched successfully", emailTemplate), HttpStatus.OK);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<EmailTemplate>> save(@RequestBody EmailTemplate emailTemplate) {

		try {
			emailTemplate = emailTemplateService.save(emailTemplate);
		} catch (Exception e) {
			logger.error("Error occurred during saving of email template ");
			throw e;
		}
		return new ResponseEntity<Response<EmailTemplate>>(
				new Response<EmailTemplate>(HttpStatus.OK.value(), "Email template saved successfully", emailTemplate),
				HttpStatus.OK);
	}

	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public ResponseEntity<Response<EmailTemplate>> update(@RequestBody EmailTemplate emailTemplate) {

		try {
			emailTemplate = emailTemplateService.save(emailTemplate);
		} catch (Exception e) {
			logger.error("Error occurred during updating of email template");
			throw e;
		}
		return new ResponseEntity<Response<EmailTemplate>>(
				new Response<EmailTemplate>(HttpStatus.OK.value(), "Email template update successfully", emailTemplate),
				HttpStatus.OK);
	}

	@RequestMapping(value = "Survey/template/save", method = RequestMethod.POST)
	public ResponseEntity<Response<EmailLog>> templatesave(@RequestBody EmailLog emailLog) {

		try {
			mailSenderServices.sendBulkEmail(emailLog);
		} catch (Exception e) {
			logger.error("Error occurred during saving of email Log Info ");
			throw e;
		}
		return new ResponseEntity<Response<EmailLog>>(
				new Response<EmailLog>(HttpStatus.OK.value(), "Email Log saved successfully", emailLog), HttpStatus.OK);
	}

	// for Email mailing services
	@RequestMapping(value = "send/mail", method = RequestMethod.POST)
	public ResponseEntity<Response<String>> mailNotification(@RequestBody Map<String, String> map) throws Exception {
		String message = "";
		try {

			mailSenderServices.sendTo(map.get("from"), map.get("to"), map.get("subject"), map.get("text"));
			message = "success";
		} catch (Exception e) {
			logger.error("Error occurred during sending mail.....");
			message = "Error";

		}
		return new ResponseEntity<Response<String>>(
				new Response<String>(HttpStatus.OK.value(), "Email Notification Send successfully", message),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getUser/{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<Response<User>> getUserByemployeeId(@PathVariable String employeeId) {

		User userDetails = null;
		try {

			userDetails = emailTemplateService.getUserByEmployeeId(employeeId);
		} catch (Exception e) {
			logger.error("Error occurred in fetching of User Employee id " + employeeId);
			throw e;
		}
		return new ResponseEntity<Response<User>>(
				new Response<User>(HttpStatus.OK.value(), "User fetched successfully", userDetails), HttpStatus.OK);
	}

	@RequestMapping(value = "getEmailLog/{userId}/{stageId}", method = RequestMethod.GET)
	public ResponseEntity<Response<EmailLog>> getUserByemployeeId(@PathVariable Long userId, int stageId) {

		EmailLog emailLog = null;
		try {
			emailLog = emailLogService.getEmailLogDetails(userId, stageId);
		} catch (Exception e) {
			logger.error("Error occurred in fetching of EmailLog By stageId And AssignedUserId " + userId);
			throw e;
		}
		return new ResponseEntity<Response<EmailLog>>(
				new Response<EmailLog>(HttpStatus.OK.value(), "User fetched successfully", emailLog), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUsersNotAdmin", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getUserALLuserNotAdmit() {

		List<User> userDetails = new ArrayList<>();
		try {
			userDetails = emailLogService.getAllUsersNotAdmin();
		} catch (Exception e) {
			logger.error("Error occurred in fetching of ALLuserNotAdmit " + userDetails);
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "ALLuserNotAdmit fetched successfully", userDetails),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getAllContract/{plantId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Contract>>> getContractById(@PathVariable("plantId") Long plantId) {

		List<Contract> contractDetails = null;
		try {
			contractDetails = emailLogService.getContractDetailsByPlantId(plantId);
		} catch (Exception e) {
			logger.error("Error occurred in fetching of contracts by plantId " + plantId);
			throw e;
		}
		return new ResponseEntity<Response<List<Contract>>>(new Response<List<Contract>>(HttpStatus.OK.value(),
				"contractDetails fetched successfully", contractDetails), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllContractByVendorId/{vendorId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Contract>>> getContractByVendorId(@PathVariable("vendorId") Long vendorId) {

		List<Contract> contractDetails = null;
		try {
			contractDetails = emailLogService.getContractDetailsByVendorId(vendorId);
		} catch (Exception e) {
			logger.error("Error occurred in fetching of contracts by plantId " + vendorId);
			throw e;
		}
		return new ResponseEntity<Response<List<Contract>>>(new Response<List<Contract>>(HttpStatus.OK.value(),
				"contractDetails fetched successfully", contractDetails), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllforms/{categoryId}/{subCategoryId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Form>>> getFormB(@PathVariable("categoryId") Long categoryId,
			@PathVariable("subCategoryId") Long subCategoryId) {

		List<Form> formDetails = null;
		try {
			formDetails = emailLogService.getFormDetailsBycategoryIdAndSubcategoryId(categoryId, subCategoryId);
		} catch (Exception e) {
			logger.error("Error occurred in fetching of Forms Details ");
			throw e;
		}
		return new ResponseEntity<Response<List<Form>>>(
				new Response<List<Form>>(HttpStatus.OK.value(), "formDetails fetched successfully", formDetails),
				HttpStatus.OK);
	}
}
