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
import com.vedanta.vpmt.model.HumanResource;
import com.vedanta.vpmt.service.HumanResourceService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "humanResource", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class HumanResourceController {

	@Autowired
	private HumanResourceService humanResourceService;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<HumanResource>> save(@RequestBody HumanResource humanResource) {
		try {
			humanResource = humanResourceService.save(humanResource);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<HumanResource>>(
				new Response<HumanResource>(HttpStatus.OK.value(), "Human resource saved successfully", humanResource),
				HttpStatus.OK);
	}

	@RequestMapping(value = "checkExistingHumanResource", method = RequestMethod.POST)
	public ResponseEntity<Response<HumanResource>> checkExistingPlantHead(@RequestBody HumanResource humanResource) {
		HumanResource humanResourceDetails;
		try {
			humanResourceDetails = humanResourceService.checkExistingHumanResource(humanResource);
		} catch (VedantaException e) {
			log.error("Error fetching human resource information");
			throw new VedantaException("Error fetching human resource  information");
		}
		return new ResponseEntity<Response<HumanResource>>(new Response<HumanResource>(HttpStatus.OK.value(),
				"Human resource information fetched successfully.", humanResourceDetails), HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "getHumanResource", method = RequestMethod.GET)
	public ResponseEntity<Response<List<HumanResource>>> getHumanResource() {
		List<HumanResource> humanResources = null;
		try {
			humanResources = humanResourceService.getAllHumanResource();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<HumanResource>>>(
				new Response<List<HumanResource>>(HttpStatus.OK.value(), "Human resources fetched successfully", humanResources),
				HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "getHumanResourceById/{humanResourceId}", method = RequestMethod.GET)
	public ResponseEntity<Response<HumanResource>> getHumanResourceById(@PathVariable("humanResourceId") long humanResourceId) {
		HumanResource humanResource;
		try {
			humanResource = humanResourceService.get(humanResourceId);
		} catch (VedantaException e) {
			log.error("Error fetching human resourcesinformation");
			throw new VedantaException("Error fetching human resources  information");
		}
		return new ResponseEntity<Response<HumanResource>>(new Response<HumanResource>(HttpStatus.OK.value(),
				"Human resources  information fetched successfully.", humanResource), HttpStatus.OK);
	}

}
