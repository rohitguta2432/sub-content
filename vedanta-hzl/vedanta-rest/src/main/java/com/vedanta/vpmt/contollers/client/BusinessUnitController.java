package com.vedanta.vpmt.contollers.client;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.service.BusinessUnitService;
import com.vedanta.vpmt.service.PlantService;
import com.vedanta.vpmt.util.VedantaUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "business-unit", produces = MediaType.APPLICATION_JSON_VALUE)
public class BusinessUnitController {

	@Autowired
	private BusinessUnitService businessUnitService;

	@Autowired
	private PlantService plantService;

	@RequestMapping(value = "{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<BusinessUnit>> getBusinessById(@PathVariable("businessUnitId") Long businessUnitId) {

		BusinessUnit businessUnit = null;
		try {
			businessUnit = businessUnitService.get(businessUnitId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<BusinessUnit>>(
				new Response<BusinessUnit>(HttpStatus.OK.value(), "BusinessUnit fetched successfully", businessUnit),
				HttpStatus.OK);
	}

	@RequestMapping(value = "businessUnits", method = RequestMethod.GET)
	public ResponseEntity<Response<List<BusinessUnit>>> getAllBusinessUnits() {

		List<BusinessUnit> businessUnits = null;
		try {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			// for bu admin and plant admin
			if (VedantaUtility.isPlantUnitAdmin() || VedantaUtility.isBussinessUnitAdmin()) {

				VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();
				BusinessUnit businessUnit = businessUnitService.get(vedantaUser.getBusinessUnitId());
				businessUnits = new ArrayList<>();
				businessUnits.add(businessUnit);
			}

			// for super admin
			else {
				businessUnits = businessUnitService.getAllBusinessUnits();
			}
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<BusinessUnit>>>(new Response<List<BusinessUnit>>(HttpStatus.OK.value(),
				"BusinessUnits fetched successfully", businessUnits), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<BusinessUnit>> save(@RequestBody BusinessUnit businessUnit) {

		try {
			businessUnit = businessUnitService.save(businessUnit);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<BusinessUnit>>(
				new Response<BusinessUnit>(HttpStatus.OK.value(), "BusinessUnit saved successfully", businessUnit),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public ResponseEntity<Response<BusinessUnit>> update(@RequestBody BusinessUnit businessUnit) {

		try {
			businessUnit = businessUnitService.save(businessUnit);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<BusinessUnit>>(
				new Response<BusinessUnit>(HttpStatus.OK.value(), "BusinessUnit saved successfully", businessUnit),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getAllplant/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Plant>>> getPlantByBusinessId(
			@PathVariable("businessUnitId") Long businessUnitId) {

		List<Plant> plantDetail = null;
		try {
			plantDetail = plantService.getAllPlantByBusinessUnitId(businessUnitId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<Plant>>>(
				new Response<List<Plant>>(HttpStatus.OK.value(), "Plant fetched successfully", plantDetail),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getAllplant/getAllplant/plant-name/{plantName}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Plant>>> getPlantsByPlantName(@PathVariable("plantName") String plantName) {

		List<Plant> plantDetail = null;
		try {
			plantDetail = plantService.getAllPlantByPlantName(plantName);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<Plant>>>(
				new Response<List<Plant>>(HttpStatus.OK.value(), "Plant fetched successfully", plantDetail),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "businessUnitsByRole", method = RequestMethod.GET)
	public ResponseEntity<Response<List<BusinessUnit>>> getAllBusinessUnitsByRole() {

		List<BusinessUnit> businessUnits = null;

		try {
			businessUnits = businessUnitService.getAllBusinessUnitsByRole();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<BusinessUnit>>>(new Response<List<BusinessUnit>>(HttpStatus.OK.value(),
				"BusinessUnits fetched successfully", businessUnits), HttpStatus.OK);
	}

	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<BusinessUnit>>> getAll() {

		return new ResponseEntity<Response<List<BusinessUnit>>>(new Response<List<BusinessUnit>>(HttpStatus.OK.value(),
				"BusinessUnits fetched successfully", businessUnitService.getAllBusinessUnits()), HttpStatus.OK);
	}
}
