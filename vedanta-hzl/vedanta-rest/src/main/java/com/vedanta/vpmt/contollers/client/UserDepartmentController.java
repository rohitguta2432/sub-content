package com.vedanta.vpmt.contollers.client;

import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.UserDepartment;
import com.vedanta.vpmt.service.UserDepartmentService;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "userDepartment", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserDepartmentController {

	@Autowired
	private UserDepartmentService userDepartmentService;

	@RequestMapping(value = "user-department/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<HashSet<String>>> getAllUserDepartment(
			@PathVariable("businessUnitId") Long businessUnitId) {

		HashSet<String> userDepartment = null;
		try {
			userDepartment = userDepartmentService.getAllUserDepartment(businessUnitId);
		} catch (VedantaException e) {
			log.error("Error fetching all user department information");
			throw new VedantaException("Error fetching all user department information");
		}
		return new ResponseEntity<Response<HashSet<String>>>(new Response<HashSet<String>>(HttpStatus.OK.value(),
				"User Departments fetched successfully", userDepartment), HttpStatus.OK);
	}

	@RequestMapping(value = "user-office/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<HashSet<String>>> getAllUserOffice(
			@PathVariable("businessUnitId") Long businessUnitId) {

		HashSet<String> userOffice = null;
		try {
			userOffice = userDepartmentService.getAllUserOffice(businessUnitId);
		} catch (VedantaException e) {
			log.error("Error fetching all user office information");
			throw new VedantaException("Error fetching all user office information");
		}
		return new ResponseEntity<Response<HashSet<String>>>(
				new Response<HashSet<String>>(HttpStatus.OK.value(), "User office fetched successfully", userOffice),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUserDepartmentByBuId/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<HashSet<UserDepartment>>> getAllUserDepartmentByBuId(
			@PathVariable("businessUnitId") Long businessUnitId) {

		HashSet<UserDepartment> userDepartment = null;
		try {
			userDepartment = userDepartmentService.getAllUserDepartmentByBuId(businessUnitId);
		} catch (VedantaException e) {
			log.error("Error fetching all user-Department by BuId information");
			throw new VedantaException("Error fetching all user-Department by BuId information");
		}
		return new ResponseEntity<Response<HashSet<UserDepartment>>>(new Response<HashSet<UserDepartment>>(
				HttpStatus.OK.value(), "User office fetched successfully", userDepartment), HttpStatus.OK);
	}

	@RequestMapping(value = "getUserDepartmentByLimit/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<UserDepartment>>> getUserDepartmentByLimit(
			@PathVariable("businessUnitId") Long businessUnitId) {

		List<UserDepartment> userDepartment = null;
		try {
			userDepartment = userDepartmentService.getUserDepartmentByLimit(businessUnitId);
		} catch (VedantaException e) {
			log.error("Error fetching all user-Department by BuId information");
			throw new VedantaException("Error fetching all user-Department by BuId information");
		}
		return new ResponseEntity<Response<List<UserDepartment>>>(new Response<List<UserDepartment>>(
				HttpStatus.OK.value(), "User office fetched successfully", userDepartment), HttpStatus.OK);
	}

	@RequestMapping(value = "getUserDepartmentByName/{name}/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<UserDepartment>>> getUserDepartmentByName(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {

		List<UserDepartment> departments = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<UserDepartment>>>(
					new Response<List<UserDepartment>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", departments),
					HttpStatus.FORBIDDEN);
		}

		try {
			departments = userDepartmentService.getUserDepartmentByName(name, buId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<UserDepartment>>>(
				new Response<List<UserDepartment>>(HttpStatus.OK.value(), "User fetched successfully", departments),
				HttpStatus.OK);
	}

}
