package com.vedanta.vpmt.contollers.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.service.FormSaveService;

import com.vedanta.vpmt.service.MailSenderServices;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 24/09/17. @Autowired private PlantService
 * plantService;
 */
@RestController
@RequestMapping(value = "/form-save", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FormSaveController {

	@Autowired
	@Qualifier("formSaveService")
	private FormSaveService formSaveService;

	@Autowired
	private MailSenderServices mailSenderServices;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/{formSavedId}", method = RequestMethod.GET)
	public ResponseEntity<Response<FormSaved>> getFormSavedByScoreCardId(
			@PathVariable("formSavedId") long formSavedId) {
		FormSaved formSaved;
		try {
			formSaved = formSaveService.get(formSavedId);
		} catch (VedantaException e) {
			log.error("Error fetching FormSaved");
			throw new VedantaException("Error fetching FormSaved");
		}
		return new ResponseEntity<Response<FormSaved>>(
				new Response<FormSaved>(HttpStatus.OK.value(), "FormSaved fetched successfully.", formSaved),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/user/{formSavedId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getUserFormSavedByFormSavedId(
			@PathVariable("formSavedId") long formSavedId) {
		Map<String, Object> formSavedMap = new HashMap<>();
		try {
			formSavedMap = formSaveService.getFormSavedFormMapByFormSavedId(formSavedId);
		} catch (VedantaException e) {
			log.error("Error fetching FormSaved Form");
			throw new VedantaException("Error fetching FormSaved Form");
		}
		return new ResponseEntity<Response<Map<String, Object>>>(new Response<Map<String, Object>>(
				HttpStatus.OK.value(), "FormSaved Form fetched successfully.", formSavedMap), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/user/compliance/{formSavedId}/scorecard/{scorecard}", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getComplianceUserFormSavedByFormSavedId(
			@PathVariable("formSavedId") long formSavedId, @PathVariable("scorecard") long scorecard) {
		Map<String, Object> formSavedMap = new HashMap<>();
		try {
			formSavedMap = formSaveService.getComplianceFormSavedFormMapByFormSavedId(formSavedId, scorecard);
		} catch (VedantaException e) {
			log.error("Error fetching FormSaved Form");
			throw new VedantaException("Error fetching FormSaved Form");
		}
		return new ResponseEntity<Response<Map<String, Object>>>(new Response<Map<String, Object>>(
				HttpStatus.OK.value(), "FormSaved Form fetched successfully.", formSavedMap), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<FormSaved>> saveFormSaved(@RequestBody FormSaved formSaved)
			throws JsonParseException, JsonMappingException, IOException {
		FormSaved savedFormSaved;
		try {
			savedFormSaved = formSaveService.save(formSaved);
			try {
				mailSenderServices.complianceFormEmailNotification(formSaved);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} catch (VedantaException e) {
			log.error("Error saving FormSaved data information");
			throw new VedantaException("Error saving FormSaved data information");
		}
		return new ResponseEntity<Response<FormSaved>>(
				new Response<FormSaved>(HttpStatus.OK.value(), "FormSaved data saved successfully.", savedFormSaved),
				HttpStatus.OK);
	}

	// Save ScoreCard Group user
	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/group/user/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<FormGroupUser>> saveFormGroupUser(@RequestBody FormGroupUser formGroupUser) {
		FormGroupUser savedFormGroupUser;
		try {
			savedFormGroupUser = formSaveService.saveFormGroupUser(formGroupUser);
			try {
				mailSenderServices.AssignFormUserEmailNotification(formGroupUser);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		} catch (VedantaException e) {
			log.error("Error saving form group user information");
			throw new VedantaException("Error saving form group user information");
		}
		return new ResponseEntity<Response<FormGroupUser>>(new Response<FormGroupUser>(HttpStatus.OK.value(),
				"Form group user information saved successfully.", savedFormGroupUser), HttpStatus.OK);
	}

	/*
	 * Get All scorecards
	 */
	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/contract/{contractNumber}/{status}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<FormSaved>>> getFormSavedByContractNumberAndStatus(
			@PathVariable("contractNumber") String contractNumber, @PathVariable("status") int status) {
		List<FormSaved> formSavedList = new ArrayList<>();
		try {
			formSavedList = formSaveService.getFormSavedByContractNumberAndStatus(contractNumber, status);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<FormSaved>>>(
				new Response<List<FormSaved>>(HttpStatus.OK.value(), "FormSaved fetched successfully.", formSavedList),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/get-user-scorecard/{status}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<FormSaved>>> getUserFormSavedByStatus(@PathVariable("status") int status) {
		List<FormSaved> formSavedList = new ArrayList<>();
		try {
			formSavedList = formSaveService.getUserFormSavedByStatus(status);
			new Gson();
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<FormSaved>>>(
				new Response<List<FormSaved>>(HttpStatus.OK.value(), "FormSaved fetched successfully.", formSavedList),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "all-form-saved", method = RequestMethod.GET)
	public ResponseEntity<Response<List<FormSaved>>> getAllSavedForms() {

		List<FormSaved> formSavedList = null;
		try {
			formSavedList = formSaveService.getAllFormSaved();
		} catch (VedantaException e) {
			log.error("Error fetching all form saved");
			throw new VedantaException("Error fetching all form saved");
		}
		return new ResponseEntity<Response<List<FormSaved>>>(
				new Response<List<FormSaved>>(HttpStatus.OK.value(), "FormSaved fetched successfully", formSavedList),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/populateFormSaved", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<FormSaved>> populateFormSaved(@RequestBody FormSaved formSaved)
			throws JsonParseException, JsonMappingException, IOException {
		FormSaved savedFormSaved;
		try {
			savedFormSaved = formSaveService.populate(formSaved);
		} catch (VedantaException e) {
			log.error("Error saving FormSaved data information");
			throw new VedantaException("Error saving FormSaved data information");
		}
		return new ResponseEntity<Response<FormSaved>>(
				new Response<FormSaved>(HttpStatus.OK.value(), "FormSaved data saved successfully.", savedFormSaved),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/getFormSaved/{status}/{bussinessUnitId}/{plantCode}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<FormSaved>>> getFormSavedByPlantCodeAndBuId(@PathVariable("status") int status,
			@PathVariable("bussinessUnitId") Long bussinessUnitId, @PathVariable("plantCode") String plantCode) {
		List<FormSaved> formSavedList = new ArrayList<>();
		try {
			formSavedList = formSaveService.getFormSavedByBuIdAndPlantCodeAndStatus(bussinessUnitId, plantCode, status);

		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<FormSaved>>>(
				new Response<List<FormSaved>>(HttpStatus.OK.value(), "FormSaved fetched successfully.", formSavedList),
				HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "getAllUserNotAdminAndHrByLimit/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserNotAdminAndHrByLimit(@PathVariable("buId") Long buId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = formSaveService.getAllUserNotAdminAndHrByLimit(buId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}
	
	@RequestMapping(value = "getAllHrUserByLimit/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllHrUserByLimit(@PathVariable("buId") Long buId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = formSaveService.getAllHrUserByLimit(buId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "getAllUserNotAdminAndHrByName/{name}/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserNotAdminAndHrByName(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = formSaveService.getAllUserNotAdminAndHrByName(name, buId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}
	
	@RequestMapping(value = "getAllHrUserByName/{name}/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllHrUserByName(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = formSaveService.getAllHrUserByName(name, buId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

}
