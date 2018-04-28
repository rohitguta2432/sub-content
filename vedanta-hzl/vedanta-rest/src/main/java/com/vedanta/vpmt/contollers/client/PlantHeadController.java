package com.vedanta.vpmt.contollers.client;

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
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.service.PlantHeadService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "plantHead", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class PlantHeadController {

	@Autowired
	private PlantHeadService plantHeadService;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "getAllPlantHead", method = RequestMethod.GET)
	public ResponseEntity<Response<List<PlantHead>>> getAllPlantHead() {
		List<PlantHead> plantHead = null;
		try {
			plantHead = plantHeadService.getAllPlantHead();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<PlantHead>>>(
				new Response<List<PlantHead>>(HttpStatus.OK.value(), "Plant head fetched successfully", plantHead),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<PlantHead>> save(@RequestBody PlantHead plantHead) {

		try {
			plantHead = plantHeadService.save(plantHead);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<PlantHead>>(
				new Response<PlantHead>(HttpStatus.OK.value(), "Plant head saved successfully", plantHead),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getById/{PlantHeadId}", method = RequestMethod.GET)
	public ResponseEntity<Response<PlantHead>> getById(@PathVariable("PlantHeadId") long PlantHeadId) {
		PlantHead plantHead;
		try {
			plantHead = plantHeadService.get(PlantHeadId);
		} catch (VedantaException e) {
			log.error("Error fetching plant head information");
			throw new VedantaException("Error fetching plant head  information");
		}
		return new ResponseEntity<Response<PlantHead>>(new Response<PlantHead>(HttpStatus.OK.value(),
				"Plant head  information fetched successfully.", plantHead), HttpStatus.OK);
	}

	@RequestMapping(value = "checkExistingPlantHead", method = RequestMethod.POST)
	public ResponseEntity<Response<PlantHead>> checkExistingPlantHead(@RequestBody PlantHead plantHead) {
		PlantHead plantHeadDetails;
		try {
			plantHeadDetails = plantHeadService.checkExistingPlantHead(plantHead);
		} catch (VedantaException e) {
			log.error("Error fetching plant head information");
			throw new VedantaException("Error fetching plant head  information");
		}
		return new ResponseEntity<Response<PlantHead>>(new Response<PlantHead>(HttpStatus.OK.value(),
				"Plant head  information fetched successfully.", plantHeadDetails), HttpStatus.OK);
	}

	@RequestMapping(value = "getByEmployeeId/{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<PlantHead>>> getByEmployeeId(@PathVariable("employeeId") String employeeId) {
		List<PlantHead> plantHead;
		try {
			plantHead = plantHeadService.getByEmployeeId(employeeId);
		} catch (VedantaException e) {
			log.error("Error fetching plant head information");
			throw new VedantaException("Error fetching plant head  information");
		}
		return new ResponseEntity<Response<List<PlantHead>>>(new Response<List<PlantHead>>(HttpStatus.OK.value(),
				"Plant head  information fetched successfully.", plantHead), HttpStatus.OK);
	}

	@RequestMapping(value = "getByPlantCode/{plantcode}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<PlantHead>>> getByPlantCode(@PathVariable("plantcode") String plantcode) {
		List<PlantHead> plantHead;
		try {
			plantHead = plantHeadService.geByPlantCode(plantcode);
		} catch (VedantaException e) {
			log.error("Error fetching plant head information");
			throw new VedantaException("Error fetching plant head  information");
		}
		return new ResponseEntity<Response<List<PlantHead>>>(new Response<List<PlantHead>>(HttpStatus.OK.value(),
				"Plant head  information fetched successfully.", plantHead), HttpStatus.OK);
	}

}
