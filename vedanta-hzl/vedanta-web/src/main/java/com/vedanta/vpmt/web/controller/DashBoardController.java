package com.vedanta.vpmt.web.controller;

import com.google.gson.Gson;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.Vendor;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.parser.ParserUtils;
import com.vedanta.vpmt.web.service.DashboardService;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.ScoreCardService;
import com.vedanta.vpmt.web.service.UserService;
import com.vedanta.vpmt.web.service.VendorService;
import com.vedanta.vpmt.web.util.VedantaUserUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "web/dashboard")
@Slf4j
public class DashBoardController {

	private final static String DASHBOARD_VIEW_NAME = "web/dashboard";
	private final static String NEW_DASHBOARD = "web/NewDashboard";

	@Autowired
	private ScoreCardService scoreCardService;

	@Autowired
	private UserService userService;

	@Autowired
	private PlantService plantService;

	@Autowired
	private VendorService vendorServices;

	@Autowired
	private DashboardService dashboardService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login(Principal principal, @RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {

		ModelAndView mav = new ModelAndView(DASHBOARD_VIEW_NAME);
		List<ScoreCard> scoreCards = new ArrayList<>();
		int sts = 2;

		Map<String, String> filter = new HashMap<String, String>();

		if (ObjectUtils.isEmpty(status) || status == 2) {
			status = Constants.ALL_SCORECARD;
			filter.put("status", null);
		}

		if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
			scoreCards = scoreCardService.getAllScorecardDateRange(fromDate, toDate, status);
			sts = status;
			filter.put("status", String.valueOf(sts));
			status = 2;
		} else {
			scoreCards = scoreCardService.getAllScorecard(status);
			filter.put("status", null);
		}

		mav.addObject("scoreCards", scoreCards);
		mav.addObject("status", status);
		mav.addObject("userName", userService.getCurrentUser().getName());
		mav.addObject("parserUtils", new ParserUtils());

		filter.put("fromDate", fromDate);
		filter.put("toDate", toDate);
		mav.addObject("filter", filter);
		return mav;
	}

	@RequestMapping(value = "downloadExcelFile", method = RequestMethod.GET)
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("fileName") String fileName) {
		String dataDirectory = request.getServletContext().getRealPath("/WEB-INF/templateexcel/");
		Path file = Paths.get(dataDirectory, fileName);
		if (Files.exists(file)) {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				log.error("Error while downloading file");
				throw new VedantaWebException("Error while downloading file");
			}
		}
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public ModelAndView getClientView(@RequestParam(value = "success", required = false) boolean success,
			@RequestParam(value = "action", required = false) String action,
			@RequestParam(value = "fieldName", required = false) String fieldName,
			@RequestParam(value = "plantId", required = false) Long plantId,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "subCategoryId", required = false) Long subCategoryId,
			@RequestParam(value = "contractId", required = false) Long contractId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "bussinessId", required = false) Long bussinessId) {

		ModelAndView mav = new ModelAndView(NEW_DASHBOARD);
		boolean isSuperAdmin = true;
		boolean isBusinessUnitAdmin = false;
		boolean isPlantUnitAdmin = false;
		String plantcode = null;

		User user = userService.getCurrentUser();

		List<BusinessUnit> AllbussinessUnit = plantService.getAllBusinessUnitsByRole();

		user = userService.getUser(user.getId());

		mav.addObject("userName", user.getName());

		Map<String, Object> scorecardDashboardData = null;

		boolean flage = false;

		/* Super Admin */
		if (VedantaUserUtility.isSuperAdmin(user)) {

			if (ObjectUtils.isEmpty(bussinessId) || bussinessId == 0) {
				bussinessId = AllbussinessUnit.get(0).getId();
				scorecardDashboardData = dashboardService.getNewDashBoardDataByBuId(bussinessId);

			} else {

				if (bussinessId > 0) {

					if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);

					} else {

						if (ObjectUtils.isEmpty(contractId)) {
							contractId = 0L;
						}
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);
					}
				}
			}

		}

		/* Bussiness Unit Admin */
		if (VedantaUserUtility.isBussinessUnitAdmin(user)) {
			isSuperAdmin = false;
			isBusinessUnitAdmin = true;

			if (ObjectUtils.isEmpty(bussinessId) || bussinessId == 0) {
				bussinessId = user.getBusinessUnitId();
				scorecardDashboardData = dashboardService.getNewDashBoardDataByBuId(bussinessId);

			} else {

				if (bussinessId > 0) {

					if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);

					} else {
						if (ObjectUtils.isEmpty(contractId)) {
							contractId = 0L;
						}
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);
					}
				}

			}

		}

		/* Plant Admin */
		if (VedantaUserUtility.isPlantUnitAdmin(user)) {
			isSuperAdmin = false;
			isPlantUnitAdmin = true;

			if (ObjectUtils.isEmpty(bussinessId) || bussinessId == 0) {
				bussinessId = user.getBusinessUnitId();
				scorecardDashboardData = dashboardService.getNewDashBoardDataByBuId(bussinessId);

			} else {

				if (bussinessId > 0) {

					if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);
					} else {
						if (ObjectUtils.isEmpty(contractId)) {
							contractId = 0L;
						}
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);
					}
				}
			}

		}

		/* Plant Head */
		if (VedantaUserUtility.isPlantHead(user)) {
			isSuperAdmin = false;

			if (ObjectUtils.isEmpty(bussinessId) || bussinessId == 0) {
				bussinessId = user.getBusinessUnitId();
				scorecardDashboardData = dashboardService.getNewDashBoardDataByBuId(bussinessId);

			} else {

				if (bussinessId > 0) {

					if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);
					} else {
						if (ObjectUtils.isEmpty(contractId)) {
							contractId = 0L;
						}
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);
					}
				}

			}

		}

		/* Bussiness Head */
		if (VedantaUserUtility.isBusinessHead(user)) {
			isSuperAdmin = false;
			isBusinessUnitAdmin = true;

			if (ObjectUtils.isEmpty(bussinessId) || bussinessId == 0) {
				bussinessId = user.getBusinessUnitId();
				scorecardDashboardData = dashboardService.getNewDashBoardDataByBuId(bussinessId);

			} else {

				if (bussinessId > 0) {

					if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);

					} else {
						if (ObjectUtils.isEmpty(contractId)) {
							contractId = 0L;
						}
						scorecardDashboardData = dashboardService.getNewDashBoardDataBySearchKey(bussinessId, plantId,
								categoryId, subCategoryId, contractId, fromDate, toDate);
					}
				}

			}

		}

		mav.addObject("parserUtils", new ParserUtils());
		mav.addObject("searchBussinessId", bussinessId);
		mav.addObject("searchCategoryId", categoryId);
		mav.addObject("searchSubCategoryId", subCategoryId);
		mav.addObject("searchContractId", contractId);
		mav.addObject("isBusinessUnitAdmin", isBusinessUnitAdmin);
		mav.addObject("isSuperAdmin", isSuperAdmin);
		mav.addObject("searchCategoryId", categoryId);
		mav.addObject("searchSubCategoryId", subCategoryId);
		mav.addObject("searchPlantId", plantId);
		mav.addObject("dashBoardData", scorecardDashboardData);
		mav.addObject("AllbussinessUnit", AllbussinessUnit);

		Map<String, String> filter = new HashMap<String, String>();
		filter.put("fromDate", fromDate);
		filter.put("toDate", toDate);
		mav.addObject("filter", filter);
		return mav;
	}

	@RequestMapping(value = "getVendorByVendorId/{vendor}", method = RequestMethod.GET)
	@ResponseBody
	public Vendor gerVendorDetailsbyId(@PathVariable("vendor") Long vendor) {
		Vendor vendorDetails = vendorServices.getVendor(vendor);
		return vendorDetails;
	}
}
