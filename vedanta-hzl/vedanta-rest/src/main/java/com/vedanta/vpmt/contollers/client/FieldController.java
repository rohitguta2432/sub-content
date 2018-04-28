package com.vedanta.vpmt.contollers.client;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.model.Field;
import com.vedanta.vpmt.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "field", produces = MediaType.APPLICATION_JSON_VALUE)
public class FieldController {

	@Autowired
	private FieldService fieldService;

	@RequestMapping(value = "{fieldId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Field>> getFieldById(@PathVariable("fieldId") Long fieldId) {

		Field field = null;
		try {
			field = fieldService.get(fieldId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Field>>(
				new Response<Field>(HttpStatus.OK.value(), "Field fetched successfully", field), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "fields", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Field>>> getAllFields() {

		List<Field> fields = null;
		try {
			fields = fieldService.getAllFields();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<Field>>>(
				new Response<List<Field>>(HttpStatus.OK.value(), "Fields fetched successfully", fields), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<Field>> save(@RequestBody Field field) {

		try {
			field = fieldService.save(field);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Field>>(
				new Response<Field>(HttpStatus.OK.value(), "Field saved successfully", field), HttpStatus.OK);
	}

}
