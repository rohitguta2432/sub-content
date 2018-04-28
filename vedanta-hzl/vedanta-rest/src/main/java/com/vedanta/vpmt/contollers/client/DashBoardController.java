package com.vedanta.vpmt.contollers.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.service.DashboardService;
import com.vedanta.vpmt.service.PlantHeadService;
import com.vedanta.vpmt.service.PlantService;
import com.vedanta.vpmt.service.SubCategoryService;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DashBoardController {

	@Autowired
	@Qualifier("DashboardService")
	private DashboardService dashboardService;

	@Autowired
	private SubCategoryService subCategoryServices;

	@Autowired
	private PlantHeadService plantHeadService;

	@Autowired
	private PlantService plantService;

	@PreAuthorize("hasAnyRole('ADMIN', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD')")
	@RequestMapping(value = "dashboard-data", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getDashboardDataByBuId(
			@RequestParam(value = "bussinessId", required = false) Long bussinessId,
			@RequestParam(value = "plantId", required = false) Long plantId,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "subCategoryId", required = false) Long subCategoryId,
			@RequestParam(value = "contractId", required = false) Long contractId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {

		Map<String, Object> scorecardDashboardDataByBuId = null;
		if (!VedantaUtility.isAuthorized(bussinessId)) {
			return new ResponseEntity<Response<Map<String, Object>>>(
					new Response<Map<String, Object>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason",
							scorecardDashboardDataByBuId),
					HttpStatus.FORBIDDEN);
		}
		try {

			if (bussinessId == 0) {

				scorecardDashboardDataByBuId = dashboardService.getDashboardDataByBuId(bussinessId);
			} else {

				if (bussinessId > 0) {

					if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
						scorecardDashboardDataByBuId = dashboardService.getDashboardDataBySearchkeyBuId(bussinessId,
								plantId, categoryId, subCategoryId, contractId, fromDate, toDate);
					} else {

						scorecardDashboardDataByBuId = dashboardService.getDashboardDataBySearchkeyBuId(bussinessId,
								plantId, categoryId, subCategoryId, contractId, fromDate, toDate);
					}
				}

			}

		} catch (VedantaException e) {
			log.debug("error in api while fetch dashboard data..");
		}

		return new ResponseEntity<Response<Map<String, Object>>>(
				new Response<Map<String, Object>>(HttpStatus.OK.value(),

						"Scorecard dashboard data fetched successfully", scorecardDashboardDataByBuId),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD')")
	@RequestMapping(value = "subCategoryByBuId/{bussinessId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<SubCategory>>> getsubtegoryByBuId(
			@PathVariable("bussinessId") Long bussinessId) {

		List<SubCategory> listAllSubcategorybyId = null;
		if (!VedantaUtility.isAuthorized(bussinessId)) {
			return new ResponseEntity<Response<List<SubCategory>>>(
					new Response<List<SubCategory>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason",
							listAllSubcategorybyId),
					HttpStatus.FORBIDDEN);
		}
		try {
			listAllSubcategorybyId = subCategoryServices.findAllByBusinessUnitId(bussinessId);

		} catch (VedantaException e) {

		}
		return new ResponseEntity<Response<List<SubCategory>>>(new Response<List<SubCategory>>(HttpStatus.OK.value(),

				"Subcategory data fetched successfully", listAllSubcategorybyId), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD')")
	@RequestMapping(value = "getPlantByPlantCode/{plantCode}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Plant>>> getPlanByplantCode(@PathVariable("plantCode") String plantCode) {

		List<Plant> listAllPlantByPlantCode = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		try {
			listAllPlantByPlantCode = plantService
					.getAllPlantByBusinessUnitIdAndPlantCode(vedantaUser.getBusinessUnitId(), plantCode);

		} catch (VedantaException e) {

		}
		return new ResponseEntity<Response<List<Plant>>>(new Response<List<Plant>>(HttpStatus.OK.value(),

				"Subcategory data fetched successfully", listAllPlantByPlantCode), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD')")
	@RequestMapping(value = "getPlantHeadByEmployeeId/{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<PlantHead>>> getPlanHeadByEmployeeId(
			@PathVariable("employeeId") String employeeId) {

		List<PlantHead> listAllPlantHeadByEmployeeId = null;
		try {
			// listAllPlantHeadByEmployeeId =
			// dashboardDao.getPlantHeadByEmployeeId(employeeId);
			listAllPlantHeadByEmployeeId = plantHeadService.getByEmployeeId(employeeId);

		} catch (VedantaException e) {

		}
		return new ResponseEntity<Response<List<PlantHead>>>(new Response<List<PlantHead>>(HttpStatus.OK.value(),

				"Subcategory data fetched successfully", listAllPlantHeadByEmployeeId), HttpStatus.OK);
	}
}
