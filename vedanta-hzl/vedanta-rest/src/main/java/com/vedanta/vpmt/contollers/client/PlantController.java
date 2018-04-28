package com.vedanta.vpmt.contollers.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.service.PlantHeadService;
import com.vedanta.vpmt.service.PlantService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 03/10/17.
 */
@RestController
@RequestMapping(value = "plants", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PlantController {

	@Autowired
	private PlantService plantService;

	@Autowired
	private PlantHeadService plantHeadService;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "{plantId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Plant>> getPlantById(@PathVariable("plantId") Long plantId) {

		Plant plant = null;
		try {
			plant = plantService.get(plantId);
		} catch (VedantaException e) {
			log.error("Error fetching plant information");
			throw new VedantaException("Error fetching plant information");
		}
		return new ResponseEntity<Response<Plant>>(
				new Response<Plant>(HttpStatus.OK.value(), "Plant fetched successfully", plant), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "current-business-unit-id", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Plant>>> getPlantByCurrentBusinessUnitId() {
		return new ResponseEntity<Response<List<Plant>>>(
				new Response<List<Plant>>(HttpStatus.OK.value(), "Current Business Unit Plants fetched successfully",
						plantService.getAllPlantsByCurrentBusinessUnitId()),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "current-user", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Plant>>> getPlantByCurrentUser() {
		return new ResponseEntity<Response<List<Plant>>>(new Response<List<Plant>>(HttpStatus.OK.value(),
				"Current User Plants fetched successfully", plantService.getAllPlantsByCurrentUser()), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Plant>>> getAllPlants() {

		List<Plant> plants = new ArrayList<>();
		try {
			plants = plantService.getAllPlants();
		} catch (VedantaException e) {
			log.error("Error fetching all plant information");
			throw new VedantaException("Error fetching all plant information");
		}
		return new ResponseEntity<Response<List<Plant>>>(
				new Response<List<Plant>>(HttpStatus.OK.value(), "Departments fetched successfully", plants),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<Plant>> save(@RequestBody Plant plant) {

		Plant savedPlant = null;
		try {
			savedPlant = plantService.save(plant);
		} catch (VedantaException e) {
			log.error("Error saving plant information");
			throw new VedantaException("Error saving plant information");
		}
		return new ResponseEntity<Response<Plant>>(
				new Response<Plant>(HttpStatus.OK.value(), "Department saved successfully", savedPlant), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "getAllPlantByPlantCodeAndBusinessUnitId/{plantCode}/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Plant>>> getPlantById(@PathVariable("plantCode") String plantCode,
			@PathVariable("businessUnitId") Long businessUnitId) {

		List<Plant> plant = null;
		try {
			plant = plantHeadService.getplantByPlantCodeAndBuId(plantCode, businessUnitId);
		} catch (VedantaException e) {
			log.error("Error fetching plant information");
			throw new VedantaException("Error fetching plant information");
		}
		return new ResponseEntity<Response<List<Plant>>>(
				new Response<List<Plant>>(HttpStatus.OK.value(), "Plant fetched successfully", plant), HttpStatus.OK);
	}
}
