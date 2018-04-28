package com.vedanta.vpmt.web.controller;

import com.vedanta.vpmt.model.*;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.*;
import com.vedanta.vpmt.web.util.VedantaUserUtility;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;

@Controller
@RequestMapping(value = "email")
@Slf4j

public class EmailControllerApi {

	private final static Logger logger = LoggerFactory.getLogger(EmailControllerApi.class);

	@Autowired
	private ScoreCardService scoreCardService;

	@Autowired
	private EmailTemplateService emailTemplateservice;

	@Autowired
	private UserService userServices;

	@Autowired
	private EmailLogService emailLogServices;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private PlantService plantServices;

	@Value("${survey.email.user.url}")
	private String surveyUrl;

	@RequestMapping(value = "admin/template", method = RequestMethod.GET)
	public ModelAndView getEmailTempateData() {

		ModelAndView mav = new ModelAndView("web/email/email_template");
		List<EmailTemplate> emailTemplate = new ArrayList<>();

		try {
			emailTemplate = emailTemplateservice.getAllTemplate();
			mav.addObject("emailTemplate", emailTemplate);

			return mav;
		} catch (VedantaWebException e) {
			logger.error("Error listing form data");
			throw new VedantaWebException("Error listing form data.", e.code);
		}
	}

	@RequestMapping(value = "template/create", method = RequestMethod.GET)
	public ModelAndView createEmailTempateData() {
		// //("crate controller");
		ModelAndView mav = new ModelAndView("web/email/create_template");

		mav.addObject("bussinessId", plantServices.getAllBusinessUnits());
		List<ScoreCard> scoreCards = new ArrayList<>();
		int status = 0;
		try {

			scoreCards = scoreCardService.getAllScorecard(status);

		} catch (VedantaWebException e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
		mav.addObject("scoreCards", scoreCards);
		mav.addObject("EmailTemplate", new EmailTemplate());

		return mav;
	}

	@RequestMapping(value = "template/save", method = RequestMethod.POST)

	public @ResponseBody ModelAndView saveTemplateDetails(@RequestBody String template, HttpServletRequest request)
			throws JSONException {
		EmailTemplate emailTemplateEntity = new EmailTemplate();

		try {
			User userDetails = userServices.getCurrentUser();

			JSONObject jsonObject = new JSONObject(template);
			String title = jsonObject.getString("title");
			String name = jsonObject.getString("name");
			String Description = jsonObject.getString("Description");
			String msgContent = jsonObject.getString("msgContent");
			Long businessUnitId = jsonObject.getLong("businessUnitId");

			emailTemplateEntity.setMsgContent(msgContent);
			emailTemplateEntity.setName(name);
			emailTemplateEntity.setDescription(Description);
			emailTemplateEntity.setTitle(title);
			emailTemplateEntity.setBusinessUnitId(businessUnitId);
			emailTemplateEntity.setCreatedBy(userDetails.getEmployeeId());

			emailTemplateservice.save(emailTemplateEntity);
		} catch (VedantaWebException e) {
			throw e;
		}
		return new ModelAndView("redirect:" + "admin/template");
	}

	@RequestMapping(value = "template/updateById", method = RequestMethod.POST)
	public @ResponseBody String updateTemplateDetails(@RequestBody String template) throws JSONException {

		EmailTemplate emailTemplateEntity = new EmailTemplate();
		try {
			String createdBy = null;
			User userDetails = userServices.getCurrentUser();
			JSONObject jsonObject = new JSONObject(template);
			int id = jsonObject.getInt("id");
			String title = jsonObject.getString("title");
			String name = jsonObject.getString("name");
			String Description = jsonObject.getString("Description");
			String msgContent = jsonObject.getString("msgContent");
			Long businessUnitId = jsonObject.getLong("businessUnitId");
			EmailTemplate emailDetails = emailTemplateservice.getEmailTemplateDetail(id);
			if (!ObjectUtils.isEmpty(emailDetails)) {
				createdBy = emailDetails.getCreatedBy();
			}
			if (!ObjectUtils.isEmpty(createdBy)) {
				emailTemplateEntity.setCreatedBy(createdBy);
			}

			emailTemplateEntity.setMsgContent(msgContent);
			emailTemplateEntity.setName(name);
			emailTemplateEntity.setDescription(Description);
			emailTemplateEntity.setTitle(title);
			emailTemplateEntity.setBusinessUnitId(businessUnitId);
			emailTemplateEntity.setLastUpdate(new Date());
			emailTemplateEntity.setModifiedBy(userDetails.getEmployeeId());

			emailTemplateservice.update(id, emailTemplateEntity);

		} catch (VedantaWebException e) {
			throw e;
		}
		return "done";
	}

	@RequestMapping(value = "template/update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateTemplate(@PathVariable("id") Long id) {

		ModelAndView mav = new ModelAndView("web/email/create_template");
		try {

			EmailTemplate emailTemplate = emailTemplateservice.getEmailTemplateDetail(id);
			mav.addObject("EmailTemplate", emailTemplate);
			mav.addObject("bussinessId", plantServices.getAllBusinessUnitsByRole());
			return mav;
		} catch (VedantaWebException e) {
			logger.error("Error getting Email Template information");
			throw new VedantaWebException("Error getting Email Template information", e.code);
		}
	}

	@RequestMapping(value = "template/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteEmailTemplate(@PathVariable("id") Long id) {

		try {
			emailTemplateservice.deleteEmailTemplate(id);
		} catch (VedantaWebException e) {
			logger.error("Error getting email template information");
			throw new VedantaWebException("Error getting email template information", e.code);
		}
		return new ModelAndView("redirect:" + "/email/admin/template");
	}

	@RequestMapping(value = "survey/reminder", method = RequestMethod.GET)
	public ModelAndView surveyRemainder() {

		ModelAndView mav = new ModelAndView("web/email/survey_remainder");
		User currentUser = userServices.getCurrentUser();
		boolean flag = true;
		boolean isPlantAdmin = false;
		List<Plant> plantAdminList = new ArrayList<>();
		try {

			if (VedantaUserUtility.isPlantUnitAdmin(currentUser)) {
				flag = false;
				isPlantAdmin = true;
				plantAdminList = plantServices.getPlantByPlantCodeAndBuId(currentUser.getPlantCode(),
						currentUser.getBusinessUnitId());
			}

			if (VedantaUserUtility.isBussinessUnitAdmin(currentUser)) {
				flag = false;
			}

			mav.addObject("plantAdmin", plantAdminList);
			mav.addObject("bussinessId", plantServices.getAllBusinessUnitsByRole());
			mav.addObject("flag", flag);
			mav.addObject("isPlantAdmin", isPlantAdmin);
			return mav;
		} catch (VedantaWebException e) {
			logger.error("Error Survey Remainder data");
			throw new VedantaWebException("Error Survey Remainder data.", e.code);
		}

	}

	@RequestMapping(value = "emailtemplate/save", method = RequestMethod.POST)
	public ModelAndView saveEmailLog(@ModelAttribute("emailLog") EmailLog emailLog, Model model,
			RedirectAttributes attributes) {

		try {
			User currentUserDetails = userServices.getCurrentUser();
			emailLog.setSendByUserId(currentUserDetails.getId());
			emailLog.setBusinessUnitId(currentUserDetails.getBusinessUnitId());
			emailLogServices.save(emailLog);
			attributes.addFlashAttribute("success", "Survey has been submitted successfully.");
		} catch (VedantaWebException e) {
			log.error("Error While Sending Bulk Email details");
			throw new VedantaWebException("Error While Sending Bulk Email details", e.code);
		}

		return new ModelAndView("redirect:" + "/email/survey/reminder");

	}

	private Date getDate(int dueDayOfFrequency) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, dueDayOfFrequency);
		return calendar.getTime();
	}

	@RequestMapping(value = "getContractDetails/{vendorId}", method = RequestMethod.GET)
	public @ResponseBody List<Contract> getContractDetails(@PathVariable("vendorId") Long vendorId) {
		List<Contract> contractDetail = null;
		try {
			contractDetail = emailLogServices.getContractDetailsDetailByVendorId(vendorId);

			return contractDetail;
		} catch (VedantaWebException e) {
			log.error("Error fetching category details");
			throw new VedantaWebException("Error fetching category details", e.code);
		}
	}

	@RequestMapping(value = "getVendorDetails/{plantId}", method = RequestMethod.GET)
	public @ResponseBody List<Vendor> getVendorDetails(@PathVariable("plantId") Long plantId) {
		final List<Vendor> vendortDetail = new ArrayList<Vendor>();
		
		try {
			
			final List<Contract> contractDetail = emailLogServices.getContractDetailseDetail(plantId);
			for (final Contract contract : contractDetail) {

				final Vendor vendor = vendorService.getVendor(contract.getVendorId());
				vendortDetail.add(vendor);
			}
			return vendortDetail;
		} catch (VedantaWebException e) {
			log.error("Error fetching category details");
			throw new VedantaWebException("Error fetching category details", e.code);
		}
	}

	@RequestMapping(value = "getSurveyDetails/{contractId}", method = RequestMethod.GET)
	public @ResponseBody List<Form> getSurveyDetails(@PathVariable("contractId") Long contractId) {
		List<Form> formDetail = null;
		try {
			Contract contractDetails = contractService.get(contractId);

			Long categoryId = contractDetails.getCategoryId();
			Long subCategoryId = contractDetails.getSubCategoryId();

			formDetail = emailLogServices.getFormDetailsDetail(categoryId, subCategoryId);

			return formDetail;
		} catch (VedantaWebException e) {
			log.error("Error form  details");
			throw new VedantaWebException("Error form details", e.code);
		}
	}

	@RequestMapping(value = "getUserByBuId/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getUserDetailsbyBuId(@PathVariable("buId") Long buId) {
		List<User> userDetails = null;
		try {

			userDetails = userServices.userByBusinessUnitId(buId);

			return userDetails;
		} catch (VedantaWebException e) {
			log.error("Error userDetails  details");
			throw new VedantaWebException("Error userDetails", e.code);
		}
	}

}
