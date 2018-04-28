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
import com.vedanta.vpmt.model.Department;
import com.vedanta.vpmt.service.DepartmentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "department", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "{departmentId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Department>> getDepartmentById(@PathVariable("departmentId") Long departmentId) {

		Department department = null;
		try {
			department = departmentService.get(departmentId);
		} catch (VedantaException e) {
			log.error("Error fetching department information");
			throw new VedantaException("Error fetching department information");
		}
		return new ResponseEntity<Response<Department>>(
				new Response<Department>(HttpStatus.OK.value(), "Department fetched successfully", department),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Department>>> getAllDepartments() {

		List<Department> departments = null;
		try {
			departments = departmentService.getAllDepartments();
		} catch (VedantaException e) {
			log.error("Error fetching all department information");
			throw new VedantaException("Error fetching all department information");
		}
		return new ResponseEntity<Response<List<Department>>>(
				new Response<List<Department>>(HttpStatus.OK.value(), "Departments fetched successfully", departments),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<Department>> save(@RequestBody Department department) {

		try {
			department = departmentService.save(department);
		} catch (VedantaException e) {
			log.error("Error saving department information");
			throw new VedantaException("Error saving department information");
		}
		return new ResponseEntity<Response<Department>>(
				new Response<Department>(HttpStatus.OK.value(), "Department saved successfully", department),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public ResponseEntity<Response<Department>> update(@RequestBody Department department) {

		try {
			department = departmentService.save(department);
		} catch (VedantaException e) {
			log.error("Error updating department information");
			throw new VedantaException("Error updating department information");
		}
		return new ResponseEntity<Response<Department>>(
				new Response<Department>(HttpStatus.OK.value(), "Department updated successfully", department),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "by-plant/{plantId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Department>>> getDepartmentByPlantId(@PathVariable("plantId") Long plantId) {

		List<Department> department = null;
		try {
			department = departmentService.getAllByPlantId(plantId);
		} catch (VedantaException e) {
			log.error("Error fetching department information by plant");
			throw new VedantaException("Error fetching department information by plant");
		}
		return new ResponseEntity<Response<List<Department>>>(
				new Response<List<Department>>(HttpStatus.OK.value(), "Department fetched successfully", department),
				HttpStatus.OK);
	}

}
