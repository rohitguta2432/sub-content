package com.vedanta.vpmt.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.parser.ParserUtils;
import com.vedanta.vpmt.web.service.DashboardService;
import com.vedanta.vpmt.web.service.EmailLogService;
import com.vedanta.vpmt.web.service.PlantHeadService;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.ScoreCardService;
import com.vedanta.vpmt.web.service.UserService;
import com.vedanta.vpmt.web.util.VedantaUserUtility;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "reports")
@Slf4j
public class ReportsController {

	private final static String SCORE_CARD_LIST = "web/reports/scorecard_report";
	private final static String SCORE_CARD_EXPORT_REPORTS = "web/reports/scorecard_reportExports";

	@Autowired
	private ScoreCardService scoreCardService;

	@Autowired
	private UserService userServices;

	@Autowired
	private EmailLogService emailLogServices;

	@Autowired
	private PlantService plantService;

	@Autowired
	private PlantHeadService plantHeadService;

	@Autowired
	private DashboardService dashboardService;

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
		boolean businessUnitDiv = true;
		boolean plantCodeDiv = true;

		SimpleDateFormat dt = new SimpleDateFormat("MM/YYYY");
		String defaultDate = dt.format(new Date());
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);

		if (ObjectUtils.isEmpty(fromDate)) {
			cal.add(Calendar.MONTH, -month);
			fromDate = dt.format(cal.getTime());
		}

		if (ObjectUtils.isEmpty(toDate)) {
			toDate = defaultDate;
		}

		if (ObjectUtils.isEmpty(status)) {
			status = Constants.ALL_SCORECARD;
		}

		businessUnits = plantService.getAllBusinessUnitsByRole();
		int sts = 2;
		boolean flag = true;
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
						if (ObjectUtils.isEmpty(fromDate) || ObjectUtils.isEmpty(toDate)) {
							scoreCards = scoreCardService.getAllScorecard(status, businessUnitId, plantCode);
						} else {
							scoreCards = scoreCardService.getAllScorecardDateRange(fromDate, toDate, status,
									businessUnitId, plantCode);
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

			List<ScoreCard> resultPending = scoreCards.stream().filter(scoreCard -> scoreCard.getStatus() == 0)
					.collect(Collectors.toList());
			List<ScoreCard> resultDraft = scoreCards.stream().filter(scoreCard -> scoreCard.getStatus() == 3)
					.collect(Collectors.toList());
			List<ScoreCard> resultSubmitted = scoreCards.stream().filter(scoreCard -> scoreCard.getStatus() == 5)
					.collect(Collectors.toList());
			List<ScoreCard> resultRejected = scoreCards.stream().filter(scoreCard -> scoreCard.getStatus() == 6)
					.collect(Collectors.toList());
			List<ScoreCard> resultApproved = scoreCards.stream().filter(scoreCard -> scoreCard.getStatus() == 7)
					.collect(Collectors.toList());

			int pending = resultPending.size();
			int draft = resultDraft.size();
			int submitted = resultSubmitted.size();
			int rejected = resultRejected.size();
			int approved = resultApproved.size();

			int totalScoreCard = scoreCards.size();

			List<User> userList = userServices.getAllUsers();

			mav.addObject("userList", userList);
			mav.addObject("businessUnits", businessUnits);
			mav.addObject("plantDetails", plantDetails);
			mav.addObject("parserUtils", new ParserUtils());
			mav.addObject("userDetails", emailLogServices.getAllUsersNotAdmin());
			filter.put("fromDate", fromDate);
			filter.put("toDate", toDate);
			mav.addObject("filter", filter);
			mav.addObject("businessUnitId", businessUnitId);
			mav.addObject("plantCode", plantCode);
			mav.addObject("businessUnitDiv", businessUnitDiv);
			mav.addObject("plantCodeDiv", plantCodeDiv);
			mav.addObject("pending", pending);
			mav.addObject("draft", draft);
			mav.addObject("submitted", submitted);
			mav.addObject("rejected", rejected);
			mav.addObject("approved", approved);
			mav.addObject("totalScoreCard", totalScoreCard);
			return mav;

		} catch (VedantaWebException e) {
			log.error("Error getting scorecards");
			throw new VedantaWebException("Error getting scorecards", e.code);
		}
	}

	@RequestMapping(value = "scorecard-export-reports", method = RequestMethod.GET)
	public ModelAndView getScoreCardExportsReports(@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "businessUnitId", required = false) Long businessUnitId,
			@RequestParam(value = "plantCode", required = false) String plantCode) {
		ModelAndView mav = new ModelAndView(SCORE_CARD_EXPORT_REPORTS);
		List<ScoreCard> scoreCards = new ArrayList<>();
		List<BusinessUnit> businessUnits = null;
		List<Plant> plantDetails = new ArrayList<Plant>();
		List<PlantHead> plantHeadDetails = null;

		businessUnits = plantService.getAllBusinessUnitsByRole();
		int sts = 2;
		boolean flag = true;
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
						if (ObjectUtils.isEmpty(fromDate) || ObjectUtils.isEmpty(toDate)) {
							scoreCards = scoreCardService.getAllScorecard(status, businessUnitId, plantCode);
						} else {
							scoreCards = scoreCardService.getAllScorecardDateRange(fromDate, toDate, status,
									businessUnitId, plantCode);
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

}
