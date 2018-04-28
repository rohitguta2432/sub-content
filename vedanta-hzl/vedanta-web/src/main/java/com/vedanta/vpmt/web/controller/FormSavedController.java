package com.vedanta.vpmt.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.parser.ParserUtils;
import com.vedanta.vpmt.web.service.DashboardService;
import com.vedanta.vpmt.web.service.FormSaveService;
import com.vedanta.vpmt.web.service.PlantHeadService;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.UserService;
import com.vedanta.vpmt.web.util.VedantaUserUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 23/09/17.
 */
@Controller
@RequestMapping(value = "form-save")
@Slf4j
public class FormSavedController {
	private final static String FORM_SAVED_VIEW_NAME = "web/form-saved/form_saved_template";
	private final static String FORM_SAVED_LIST = "web/form-saved/form_saved_listing";

	@Autowired
	private FormSaveService formSaveService;

	@Autowired
	private UserService userServices;

	@Autowired
	private PlantService plantService;

	@Autowired
	private PlantHeadService plantHeadService;

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping(value = "user/{formSavedId}", method = RequestMethod.GET)
	public ModelAndView getScoreCardForScorecardId(@PathVariable("formSavedId") long formSavedId) {

		ModelAndView mav = new ModelAndView(FORM_SAVED_VIEW_NAME);
		Map<String, Object> formSavedForm = new HashMap<>();
		try {
			formSavedForm = formSaveService.getFormSavedFormByFormSavedId(formSavedId);

			mav.addObject("userDetails", formSavedForm.get(Constants.USER_DETAIL));
			mav.addObject("contract", formSavedForm.get(Constants.CONTRACT));
			mav.addObject("form", formSavedForm.get(Constants.FORM));
			mav.addObject("formGroups", formSavedForm.get(Constants.FORM_GROUPS));
			mav.addObject("formSavedDetail", formSavedForm.get(Constants.FORM_SAVED_DETAIL));
			mav.addObject("parserUtils", new ParserUtils());

			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting formSaved form information");
			throw new VedantaWebException("Error getting formSaved form information", e.code);
		}
	}

	@RequestMapping(value = "user/complaince/{formSavedId}/scorecard/{scorecard}", method = RequestMethod.GET)
	public ModelAndView getComplainceScoreCardForScorecardId(@PathVariable("formSavedId") long formSavedId,
			@PathVariable("scorecard") long scorecard) {

		ModelAndView mav = new ModelAndView(FORM_SAVED_VIEW_NAME);
		Map<String, Object> formSavedForm = new HashMap<>();
		try {
			formSavedForm = formSaveService.getComplainceFormSavedFormByFormSavedId(formSavedId, scorecard);

			mav.addObject("userDetails", formSavedForm.get(Constants.USER_DETAIL));
			mav.addObject("contract", formSavedForm.get(Constants.CONTRACT));
			mav.addObject("form", formSavedForm.get(Constants.FORM));
			mav.addObject("formGroups", formSavedForm.get(Constants.FORM_GROUPS));
			mav.addObject("formSavedDetail", formSavedForm.get(Constants.FORM_SAVED_DETAIL));
			mav.addObject("scorecardId", scorecard);
			mav.addObject("parserUtils", new ParserUtils());

			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting formSaved form information");
			throw new VedantaWebException("Error getting formSaved form information", e.code);
		}
	}

	@RequestMapping(value = "assign", method = RequestMethod.POST)
	public @ResponseBody FormGroupUser assignUserToGroup(@RequestBody FormGroupUser formGroupUser) {

		try {
			if (ObjectUtils.isEmpty(formGroupUser.getFormGroupDetailId())) {
				formGroupUser.setFormGroupDetailId(0L);
			}

			formSaveService.saveFormGroupUser(formGroupUser);
		} catch (VedantaWebException e) {
			log.error("Error saving FormGroupUser information");
			throw new VedantaWebException("Error saving FormGroupUser information", e.code);
		}
		return formGroupUser;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView saveFormData(@ModelAttribute("formSaved") FormSaved formSaved, Model model) {

		try {
			formSaved.setStatus(Constants.STATUS_SUBMITTED);
			formSaved.setComplianceFormEmailNotification(Constants.COMPLIANCE_FORM_SUBMIT);
			formSaveService.save(formSaved);
		} catch (VedantaWebException e) {
			log.error("Error saving FormSaved information");
			throw new VedantaWebException("Error saving FormSaved information", e.code);
		}
		return new ModelAndView("redirect:" + "/form-save");
	}

	@RequestMapping(value = "save-approve", method = RequestMethod.POST)
	public ModelAndView saveFormDataAndApprove(@ModelAttribute("formSaved") FormSaved formSaved, Model model) {

		try {
			formSaved.setStatus(Constants.STATUS_APPROVED);
			formSaved.setComplianceFormEmailNotification(Constants.COMPLIANCE_FORM_APPROVE);
			formSaveService.save(formSaved);
		} catch (VedantaWebException e) {
			log.error("Error saving FormSaved information");
			throw new VedantaWebException("Error saving FormSaved information", e.code);
		}
		// return new ModelAndView("redirect:" + "/form-save/user/"+formSaved.getId());
		return new ModelAndView("redirect:" + "/form-save");
	}

	@RequestMapping(value = "save-approve-by-scorecard/{id}", method = RequestMethod.POST)
	public ModelAndView saveFormDataAndApproveByscorecard(@ModelAttribute("formSaved") FormSaved formSaved, Model model,
			@PathVariable("id") Long scorecardId) {

		try {
			formSaved.setStatus(Constants.STATUS_APPROVED);
			formSaved.setComplianceFormEmailNotification(Constants.COMPLIANCE_FORM_APPROVE);
			formSaveService.save(formSaved);
		} catch (VedantaWebException e) {
			log.error("Error saving FormSaved information");
			throw new VedantaWebException("Error saving FormSaved information", e.code);
		}
		return new ModelAndView(
				"redirect:" + "/form-save/user/complaince/" + formSaved.getId() + "/scorecard/" + scorecardId);
	}

	@RequestMapping(value = "save-draft", method = RequestMethod.POST)
	public ModelAndView saveFormDataAsDraft(@ModelAttribute("formSaved") FormSaved formSaved, Model model) {

		try {
			formSaved.setStatus(Constants.STATUS_DRAFT);
			formSaveService.save(formSaved);
		} catch (VedantaWebException e) {
			log.error("Error saving FormSaved information");
			throw new VedantaWebException("Error saving FormSaved information", e.code);
		}
		return new ModelAndView("redirect:" + "/form-save");
		// return new ModelAndView("redirect:" + "/form-save/user/"+formSaved.getId());
	}

	@RequestMapping(value = "save-draft-by-scorecard/{id}", method = RequestMethod.POST)
	public ModelAndView saveFormDataAsDraftByScorecard(@ModelAttribute("formSaved") FormSaved formSaved, Model model,
			@PathVariable("id") Long scorecardId) {

		try {
			formSaved.setStatus(Constants.STATUS_DRAFT);
			formSaveService.save(formSaved);
		} catch (VedantaWebException e) {
			log.error("Error saving FormSaved information");
			throw new VedantaWebException("Error saving FormSaved information", e.code);
		}
		return new ModelAndView(
				"redirect:" + "/form-save/user/complaince/" + formSaved.getId() + "/scorecard/" + scorecardId);
	}

	@RequestMapping(value = "save-reject", method = RequestMethod.POST)
	public ModelAndView saveFormDataAsRejected(@ModelAttribute("formSaved") FormSaved formSaved, Model model) {

		try {
			formSaved.setStatus(Constants.STATUS_REJECTED);
			formSaved.setComplianceFormEmailNotification(Constants.COMPLIANCE_FORM_REJECT);
			formSaveService.save(formSaved);
		} catch (VedantaWebException e) {
			log.error("Error saving FormSaved information");
			throw new VedantaWebException("Error saving FormSaved information", e.code);
		}
		return new ModelAndView("redirect:" + "/form-save");
		// return new ModelAndView("redirect:" + "/form-save/user/"+formSaved.getId());
	}

	@RequestMapping(value = "save-reject-by-scorecard/{id}", method = RequestMethod.POST)
	public ModelAndView saveFormDataAsRejectedByScorecard(@ModelAttribute("formSaved") FormSaved formSaved, Model model,
			@PathVariable("id") Long scorecardId) {

		try {
			formSaved.setStatus(Constants.STATUS_REJECTED);
			formSaved.setComplianceFormEmailNotification(Constants.COMPLIANCE_FORM_REJECT);
			formSaveService.save(formSaved);
		} catch (VedantaWebException e) {
			log.error("Error saving FormSaved information");
			throw new VedantaWebException("Error saving FormSaved information", e.code);
		}
		return new ModelAndView(
				"redirect:" + "/form-save/user/complaince/" + formSaved.getId() + "/scorecard/" + scorecardId);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getFormSaved(@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "bussinessId", required = false) Long bussinessId,
			@RequestParam(value = "plantCode", required = false) String plantCode) {
		ModelAndView mav = new ModelAndView(FORM_SAVED_LIST);
		List<FormSaved> formSavedList = new ArrayList<>();

		List<BusinessUnit> AllbussinessUnit = plantService.getAllBusinessUnitsByRole();
		boolean flag = false;
		boolean buBlock = false;

		try {
			User currentUserDetails = userServices.getCurrentUser();
			List<Plant> getAllplantsByBuId = new ArrayList<>();
			List<PlantHead> allPlantHead = new ArrayList<>();

			if (bussinessId == null) {
				bussinessId = AllbussinessUnit.get(0).getId();
			}

			if (plantCode == null) {
				getAllplantsByBuId = plantService.getPlantByBusinessId(bussinessId);
				if (!CollectionUtils.isEmpty(getAllplantsByBuId)) {

					plantCode = getAllplantsByBuId.get(0).getPlantCode();
				} else {
					plantCode = currentUserDetails.getPlantCode();
				}
			}

			if (status == null) {
				status = Constants.ALL_SAVEFORM;
			}

			/* for Super-Admin */
			if (VedantaUserUtility.isSuperAdmin(currentUserDetails)) {
				flag = true;
				buBlock = false;

				formSavedList = formSaveService.getAllFormSavedByPlantCodeAndBuIdAndStatus(status, bussinessId,
						plantCode);

			}

			/* for bussiness-Unit-Admin */
			if (VedantaUserUtility.isBussinessUnitAdmin(currentUserDetails)) {
				flag = true;
				buBlock = false;
				formSavedList = formSaveService.getAllFormSavedByPlantCodeAndBuIdAndStatus(status, bussinessId,
						plantCode);

			}
			/* for plant admin */
			if (VedantaUserUtility.isPlantUnitAdmin(currentUserDetails)) {
				flag = false;
				buBlock = false;
				bussinessId = currentUserDetails.getBusinessUnitId();
				plantCode = currentUserDetails.getPlantCode();
				formSavedList = formSaveService.getAllFormSavedByPlantCodeAndBuIdAndStatus(status, bussinessId,
						plantCode);

			}

			/* for bussinessUnit-head */
			if (VedantaUserUtility.isBusinessHead(currentUserDetails)) {
				flag = true;
				buBlock = false;
				formSavedList = formSaveService.getAllFormSavedByPlantCodeAndBuIdAndStatus(status, bussinessId,
						plantCode);
			}

			/* for plant-head */
			if (VedantaUserUtility.isPlantHead(currentUserDetails)) {
				flag = true;
				buBlock = true;

				allPlantHead = plantHeadService.getAllPlantHeadByEmployeeId(currentUserDetails.getEmployeeId());

				List<Plant> plantDetails = new ArrayList<>();
				getAllplantsByBuId.clear();
				for (PlantHead plantHead : allPlantHead) {
					plantDetails = dashboardService.getPlantByPlantCode(plantHead.getPlantCode());
					getAllplantsByBuId.addAll(plantDetails);
				}
				if (!ObjectUtils.isEmpty(plantDetails)) {
					formSavedList = formSaveService.getAllFormSavedByPlantCodeAndBuIdAndStatus(status, bussinessId,
							plantDetails.get(0).getPlantCode());
				}

			} else {
				getAllplantsByBuId = plantService.getPlantByBusinessId(bussinessId);
			}

			/* for contract-manager */
			if (currentUserDetails.getAuthorities().equals(Constants.ROLE_CONTRACT_MANAGER)
					|| currentUserDetails.getAuthorities().equals(Constants.ROLE_HR)) {
				flag = false;
				buBlock = false;

				formSavedList = formSaveService.getAllFormSavedByStatus(status);
			}

			mav.addObject("buBlock", buBlock);
			mav.addObject("searchPlantId", plantCode);
			mav.addObject("searchBussinessId", bussinessId);
			mav.addObject("flag", flag);
			mav.addObject("formSavedList", formSavedList);
			mav.addObject("status", status);
			mav.addObject("parserUtils", new ParserUtils());
			mav.addObject("AllbussinessUnit", AllbussinessUnit);
			mav.addObject("getAllplantsByBuId", getAllplantsByBuId);
		} catch (VedantaWebException e) {
			throw new VedantaWebException("Error saving FormSaved information by status", e.code);

		}

		return mav;
	}

	@RequestMapping(value = "getAllUserNotAdminAndHrByLimit/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUserNotAdminAndHrByLimit(@PathVariable("buId") final Long buId) {
		return formSaveService.getAllUserNotAdminAndHrByLimit(buId);
	}

	@RequestMapping(value = "getAllHrUserByLimit/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllHrUserByLimit(@PathVariable("buId") final Long buId) {
		return formSaveService.getAllHrUserByLimit(buId);
	}

	@RequestMapping(value = "getAllUserNotAdminAndHrByName/{name}/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUserNotAdminAndHrByName(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {
		return formSaveService.getAllUserNotAdminAndHrByName(name, buId);

	}

	@RequestMapping(value = "getAllHrUserByName/{name}/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllHrUserByName(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {
		return formSaveService.getAllHrUserByName(name, buId);
	}

}
