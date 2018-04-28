package com.vedanta.vpmt.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.vedanta.vpmt.mapper.ScoreCardAssign;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.ScorecardAggregation;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.parser.ParserUtils;
import com.vedanta.vpmt.web.service.DashboardService;
import com.vedanta.vpmt.web.service.EmailLogService;
import com.vedanta.vpmt.web.service.NotificationService;
import com.vedanta.vpmt.web.service.PlantHeadService;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.ScoreCardService;
import com.vedanta.vpmt.web.service.UserService;
import com.vedanta.vpmt.web.util.VedantaUserUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 23/09/17.
 */
@Controller
@RequestMapping(value = "scorecard")
@Slf4j
public class ScoreCardController {
	private final static String SCORECARD_VIEW_NAME = "web/scorecard/scorecard_template";
	private final static String SCORE_CARD_LIST = "web/scorecard/scorecard_listing";

	@Autowired
	private ScoreCardService scoreCardService;

	@Autowired
	private UserService userServices;

	@Autowired
	private NotificationService notificationServices;

	@Autowired
	private EmailLogService emailLogServices;

	@Autowired
	private PlantService plantService;

	@Autowired
	private PlantHeadService plantHeadService;

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping(value = "{contractNumber}", method = RequestMethod.GET)
	public ModelAndView getScoreCardForContract(@PathVariable("contractNumber") String contractNumber) {

		ModelAndView mav = new ModelAndView(SCORECARD_VIEW_NAME);

		Map<String, Object> scoreCardTemplate = new HashMap<>();
		try {
			scoreCardTemplate = scoreCardService.getScoreCardTemplateByContract(contractNumber);

			mav.addObject("contract", scoreCardTemplate.get(Constants.CONTRACT));
			mav.addObject("template", scoreCardTemplate.get(Constants.TEMPLATE));
			mav.addObject("templateGroups", scoreCardTemplate.get(Constants.TEMPLATE_GROUPS));
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting template information");
			throw new VedantaWebException("Error getting template information", e.code);
		}
	}

	@RequestMapping(value = "user/{scoreCardId}", method = RequestMethod.GET)
	public ModelAndView getScoreCardForScorecardId(@PathVariable("scoreCardId") long scoreCardId) {

		ModelAndView mav = new ModelAndView(SCORECARD_VIEW_NAME);

		Map<String, Object> scoreCardTemplate = new HashMap<>();
		try {
			scoreCardTemplate = scoreCardService.getScoreCardTemplateByScoreCardId(scoreCardId);
			
			mav.addObject("plantDetails", scoreCardTemplate.get(Constants.PLANT_DETAIL));
			mav.addObject("userDetails", scoreCardTemplate.get(Constants.USER_DETAIL));
			mav.addObject("contract", scoreCardTemplate.get(Constants.CONTRACT));
			mav.addObject("template", scoreCardTemplate.get(Constants.TEMPLATE));
			mav.addObject("templateGroups", scoreCardTemplate.get(Constants.TEMPLATE_GROUPS));
			mav.addObject("scoreCardDetail", scoreCardTemplate.get(Constants.SCORECARD_DETAIL));
			mav.addObject("templateForms", scoreCardTemplate.get(Constants.TEMPLATE_FROMS));
			mav.addObject("parserUtils", new ParserUtils());

			if (ObjectUtils.isEmpty(scoreCardTemplate.get(Constants.TEMPLATE_GROUPS))) {
				log.error("Error getting template information");
				throw new VedantaWebException("Error getting template information", 403);
			}
			return mav;
		} catch (VedantaWebException e) {

			if (e.code == 403) {
				return new ModelAndView("redirect:" + "/scorecard");
			} else {
				log.error("Error getting template information");
				throw new VedantaWebException("Error getting template information", e.code);
			}
		}
	}

	@RequestMapping(value = "assign", method = RequestMethod.POST)
	public @ResponseBody ScoreCardGroupUser assignUserToGroup(@RequestBody ScoreCardGroupUser scoreCardGroupUser,
			HttpServletRequest request) {

		try {
			if (scoreCardGroupUser.getGroupId() == null)
				scoreCardGroupUser.setGroupId(0L);
			scoreCardService.saveScoreCardGroupUser(scoreCardGroupUser);
			notificationServices.saveNotification(scoreCardGroupUser);

		} catch (VedantaWebException e) {
			log.error(e.getMessage());
			throw e;
		}

		return scoreCardGroupUser;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView saveNewScorecard(@ModelAttribute("scoreCard") ScoreCard scoreCard, Model model) {

		try {
			scoreCard.setStatus(Constants.STATUS_SUBMITTED);
			User currentUser = userServices.getCurrentUser();

			if (!ObjectUtils.isEmpty(currentUser)) {
				scoreCard.setSubmittedBy(currentUser.getId());
				scoreCard.setSubmittedOn(new Date());
				scoreCard.setLastUpdate(new Date());
				scoreCard.setLastUpdateBy(currentUser.getId());
			}
			scoreCard.setScorecardEmailNotification(Constants.SCORECARD_SUBMIT);
			scoreCardService.save(scoreCard);
			notificationServices.submitNotification(scoreCard);
		} catch (VedantaWebException e) {
			log.error("Error saving scorecard information");
			throw new VedantaWebException("Error saving scorecard information", e.code);
		}
		return new ModelAndView("redirect:" + "/scorecard");
	}

	@RequestMapping(value = "save-approve", method = RequestMethod.POST)
	public ModelAndView saveScorecardAndApprove(@ModelAttribute("scoreCard") ScoreCard scoreCard, Model model) {

		try {

			scoreCard.setStatus(Constants.STATUS_APPROVED);

			User currentUser = userServices.getCurrentUser();

			if (!ObjectUtils.isEmpty(currentUser)) {
				scoreCard.setApprovedBy(currentUser.getId());
				scoreCard.setApprovedOn(new Date());
				scoreCard.setLastUpdate(new Date());
				scoreCard.setLastUpdateBy(currentUser.getId());
			}
			scoreCard.setScorecardEmailNotification(Constants.SCORECARD_APPROVE);
			scoreCardService.save(scoreCard);
			notificationServices.submitNotification(scoreCard);

		} catch (VedantaWebException e) {
			log.error("Error saving scorecard and approve information");
			throw new VedantaWebException("Error saving scorecard and approve information", e.code);
		}
		return new ModelAndView("redirect:" + "/scorecard");
	}

	@RequestMapping(value = "save-draft", method = RequestMethod.POST)
	public ModelAndView saveScorecardAsDraft(@ModelAttribute("scoreCard") ScoreCard scoreCard, Model model) {

		try {
			User currentUser = userServices.getCurrentUser();

			scoreCard.setStatus(Constants.STATUS_DRAFT);
			scoreCard.setLastUpdate(new Date());
			if (!ObjectUtils.isEmpty(currentUser)) {
				scoreCard.setLastUpdateBy(currentUser.getId());
			}

			scoreCardService.save(scoreCard);
		} catch (VedantaWebException e) {
			log.error("Error saving scorecard as draft  information");
			throw new VedantaWebException("Error saving scorecard as draft information", e.code);
		}
		return new ModelAndView("redirect:" + "/scorecard");
	}

	@RequestMapping(value = "save-reject", method = RequestMethod.POST)
	public ModelAndView saveScorecardAsReject(@ModelAttribute("scoreCard") ScoreCard scoreCard, Model model) {

		try {
			scoreCard.setStatus(Constants.STATUS_REJECTED);
			scoreCard.setScorecardEmailNotification(Constants.SCORECARD_REJECT);
			User currentUser = userServices.getCurrentUser();

			if (!ObjectUtils.isEmpty(currentUser)) {
				scoreCard.setRejectedBy(currentUser.getId());
				scoreCard.setRejectedOn(new Date());
				scoreCard.setLastUpdate(new Date());
				scoreCard.setLastUpdateBy(currentUser.getId());
			}
			scoreCardService.save(scoreCard);
			notificationServices.submitNotification(scoreCard);

		} catch (VedantaWebException e) {
			log.error("Error saving scorecard as Rejected  information");
			throw new VedantaWebException("Error saving scorecard as Rejected information", e.code);
		}
		return new ModelAndView("redirect:" + "/scorecard");
	}
	
	@RequestMapping(value = "set-notapplicable/{scoreCardId}", method = RequestMethod.GET)
	public ModelAndView saveScorecardAsNotApplicable(@PathVariable("scoreCardId") long scoreCardId) {

		try {
			
			ScoreCard scoreCard = scoreCardService.getScoreCardById(scoreCardId);
			scoreCard.setStatus(Constants.STATUS_REJECTED);
			scoreCard.setScorecardEmailNotification(Constants.SCORECARD_REJECT);
			User currentUser = userServices.getCurrentUser();

			if (!ObjectUtils.isEmpty(currentUser)) {
				scoreCard.setRejectedBy(currentUser.getId());
				scoreCard.setRejectedOn(new Date());
				scoreCard.setLastUpdate(new Date());
				scoreCard.setLastUpdateBy(currentUser.getId());
			}
			scoreCardService.save(scoreCard);
			notificationServices.submitNotification(scoreCard);

		} catch (VedantaWebException e) {
			log.error("Error saving scorecard as Rejected  information");
			throw new VedantaWebException("Error saving scorecard as Rejected information", e.code);
		}
		return new ModelAndView("redirect:" + "/scorecard");
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getScoreCard(@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "businessUnitId", required = false) Long businessUnitId,
			@RequestParam(value = "plantCode", required = false) String plantCode) {
		ModelAndView mav = new ModelAndView(SCORE_CARD_LIST);
		List<ScoreCard> scoreCards = new ArrayList<>();
		List<BusinessUnit> businessUnits = null;
		List<Plant> plantDetails = new ArrayList<Plant>();
		List<PlantHead> plantHeadDetails = null;
		
		businessUnits = plantService.getAllBusinessUnitsByRole();
		int sts = 2;
		boolean flag = true;
		if(ObjectUtils.isEmpty(status)) {
			status = 2;
		}
		try {
			Map<String, String> filter = new HashMap<String, String>();
			User user = userServices.getCurrentUser();

			if (ObjectUtils.isEmpty(status) && ObjectUtils.isEmpty(fromDate) && ObjectUtils.isEmpty(toDate)
					&& ObjectUtils.isEmpty(businessUnitId) && ObjectUtils.isEmpty(plantCode)) {
				scoreCards = scoreCardService.getAllScorecardByRoleBased();
				plantCode = String.valueOf(0);
				flag = false;
			} else {

				if (!ObjectUtils.isEmpty(plantCode)) {

					if (plantCode.equals(String.valueOf(0)) && ObjectUtils.isEmpty(businessUnitId)) {
						scoreCards = scoreCardService.getAllScorecardByRoleBased();
						plantCode = String.valueOf(0);
						filter.put("status", String.valueOf(status));
						flag = false;

					} else if (!ObjectUtils.isEmpty(businessUnitId) && plantCode.equals(String.valueOf(0))) {
						if(ObjectUtils.isEmpty(fromDate) || ObjectUtils.isEmpty(toDate)) {
							scoreCards = scoreCardService.getAllScorecard(status, businessUnitId, plantCode);
						}else {
							scoreCards = scoreCardService.getAllScorecardDateRange(fromDate, toDate, status, businessUnitId,
									plantCode);
						}
						
				
						plantCode = String.valueOf(0);
						filter.put("status", String.valueOf(status));
						flag = false;

					}
				}
			}

			if (VedantaUserUtility.isPlantHead(user)) {
				plantHeadDetails = plantHeadService.getAllPlantHeadByEmployeeId(user.getEmployeeId());
				for (PlantHead plantHead : plantHeadDetails) {
					plantDetails.addAll(dashboardService.getPlantByPlantCode(plantHead.getPlantCode()));
				}

			} else {

				if (VedantaUserUtility.isPlantUnitAdmin(user)) {
					plantDetails = dashboardService.getPlantByPlantCode(user.getPlantCode());
					businessUnitId = plantDetails.get(0).getBusinessUnitId();
				} else {
					if (ObjectUtils.isEmpty(businessUnitId)) {

						plantDetails = plantService.getPlantByBusinessId(businessUnits.get(0).getId());
					} else {
						plantDetails = plantService.getPlantByBusinessId(businessUnitId);
					}

				}
			}

			boolean businessUnitDiv = true;
			boolean plantCodeDiv = true;

			if (VedantaUserUtility.isAdmin(user) || VedantaUserUtility.isBussinessUnitAdmin(user)
					|| VedantaUserUtility.isPlantHead(user) || VedantaUserUtility.isBusinessHead(user)) {
				businessUnitDiv = false;

			}
			if (user.getAuthorities().equals("ROLE_CONTRACT_MANAGER") || VedantaUserUtility.isPlantUnitAdmin(user)) {
				businessUnitDiv = false;
				plantCodeDiv = false;

			}
			if (VedantaUserUtility.isSuperAdmin(user)) {
				businessUnitDiv = true;
				plantCodeDiv = true;
			}

			if (ObjectUtils.isEmpty(status) || status == 2) {
				status = Constants.ALL_SCORECARD;
				filter.put("status", null);
			}

			if (ObjectUtils.isEmpty(businessUnitId)) {
				if (!ObjectUtils.isEmpty(businessUnits)) {
					businessUnitId = businessUnits.get(0).getId();
				}

			}
			if (ObjectUtils.isEmpty(plantCode)) {
					plantCode = "0";

			}
			if (flag) {
				if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {

					scoreCards = scoreCardService.getAllScorecardDateRange(fromDate, toDate, status, businessUnitId,
							plantCode);
					sts = status;
					filter.put("status", String.valueOf(sts));
					status = 2;
				} else {

					scoreCards = scoreCardService.getAllScorecard(status, businessUnitId, plantCode);
					filter.put("status", null);
				}
			}

			List<User> userList = userServices.getAllUsers();

			mav.addObject("userList", userList);
			mav.addObject("businessUnits", businessUnits);
			mav.addObject("plantDetails", plantDetails);
			mav.addObject("scoreCards", scoreCards);
			mav.addObject("status", status);
			mav.addObject("parserUtils", new ParserUtils());
			mav.addObject("userDetails", emailLogServices.getAllUsersNotAdmin());
			filter.put("fromDate", fromDate);
			filter.put("toDate", toDate);
			mav.addObject("filter", filter);
			mav.addObject("businessUnitId", businessUnitId);
			mav.addObject("plantCode", plantCode);
			mav.addObject("businessUnitDiv", businessUnitDiv);
			mav.addObject("plantCodeDiv", plantCodeDiv);

			return mav;

		} catch (VedantaWebException e) {
			log.error("Error getting scorecards");
			throw new VedantaWebException("Error getting scorecards", e.code);
		}
	}

	@RequestMapping(value = "assign-scorecards", method = RequestMethod.POST)
	public @ResponseBody ScoreCardAssign assignScoreCards(@RequestBody ScoreCardAssign scoreCardAssign,
			HttpServletRequest request) {

		try {
			scoreCardService.assignScoreCard(scoreCardAssign);

		} catch (VedantaWebException e) {
			log.error(e.getMessage());
			throw e;
		}

		return scoreCardAssign;
	}

	@RequestMapping(value = "getScorecardBysubCategoryId/{subcategoryId}", method = RequestMethod.GET)
	@ResponseBody
	public Set<ScorecardAggregation> getSubCategoryDetails(@PathVariable("subcategoryId") long subcategoryId) {
		Set<ScorecardAggregation> getScorecard = null;

		try {
			getScorecard = scoreCardService.getAllScorecardBySubCategoryID(subcategoryId);

			return getScorecard;
		} catch (VedantaWebException e) {

			log.error("Error fetching category details");
			throw new VedantaWebException("Error fetching category details", e.code);
		}
	}

	@RequestMapping(value = "getAllUserNotAssigned/{scoreCardId}", method = RequestMethod.POST)
	public @ResponseBody List<User> getAllUserNotAssigned(@PathVariable("scoreCardId") long scoreCardId) {
		List<User> userList = null;

		try {
			// userList = emailLogServices.getAllUsersNotAdmin();
			userList = scoreCardService.getAllUsersNotAssigned(scoreCardId);
			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users");
			throw new VedantaWebException("Error fetching all users", e.code);
		}
	}

	// get all user that are not assigned and having similar name - like *(name)
	@RequestMapping(value = "getAllUserNotAssignedByName/{scoreCardId}/{name}", method = RequestMethod.POST)
	public @ResponseBody List<User> getAllUserNotAssignedByName(@PathVariable("scoreCardId") long scoreCardId,
			@PathVariable("name") String name) {
		List<User> userList = null;

		try {

			userList = scoreCardService.getAllUserNotAssignedByName(scoreCardId, name);
			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users");
			throw new VedantaWebException("Error fetching all users by name", e.code);
		}
	}

	@RequestMapping(value = "getAllUserNotAdminByLimit/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUserNotAdminByLimit(@PathVariable("buId") Long buId) {
		List<User> userList = null;
		try {
			userList = scoreCardService.getAllUserNotAdminByLimit(buId);
			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users");
			throw new VedantaWebException("Error fetching all users", e.code);
		}
	}

	@RequestMapping(value = "getAllUserByName/{name}/bu-id/{buId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUserByName(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {
		List<User> userList = null;

		try {
			userList = scoreCardService.getAllUserByNameAndBuId(name, buId);
			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users");
			throw new VedantaWebException("Error fetching all users by name", e.code);
		}
	}

	@RequestMapping(value = "getAllUserNotAdminByLimitAndNotApprover/{buId}/{scorecardId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUserNotAdminByLimitAndNotApprover(@PathVariable("buId")Long buId,
			@PathVariable("scorecardId") Long scorecardId) {
		List<User> userList = null;
		try {
			userList = scoreCardService.getAllUserNotAdminByLimitAndNotApprover(buId, scorecardId);

			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users");
			throw new VedantaWebException("Error fetching all users", e.code);
		}
	}
	
	@RequestMapping(value = "getAllUserByNameAndBuIdAndNotApprover/{name}/{buId}/{scorecardId}", method = RequestMethod.GET)
	public @ResponseBody List<User> getAllUserByNameAndBuIdAndNotApprover(@PathVariable("name") String name,
			@PathVariable("buId") Long buId,@PathVariable("scorecardId") Long scorecardId) {
		List<User> userList = null;

		try {
			userList = scoreCardService.getAllUserByNameAndBuIdAndNotApprover(name, buId,scorecardId);
			return userList;
		} catch (VedantaWebException e) {
			log.error("Error fetching all users");
			throw new VedantaWebException("Error fetching all users by name", e.code);
		}
	}

}
