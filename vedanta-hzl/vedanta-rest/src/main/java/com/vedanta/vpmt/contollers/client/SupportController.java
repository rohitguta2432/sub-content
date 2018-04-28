package com.vedanta.vpmt.contollers.client;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Support;
import com.vedanta.vpmt.service.SupportService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/support")
@Slf4j
public class SupportController {

	@Autowired
	private SupportService supportService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Support>> saveFormSaved(@RequestBody Support support)
			throws JsonParseException, JsonMappingException, IOException {
		Support supportdata;
		try {
			supportdata = supportService.save(support);
		} catch (VedantaException e) {
			log.error("Error saving Support data information");
			throw new VedantaException("Error saving Support data information");
		}
		return new ResponseEntity<Response<Support>>(
				new Response<Support>(HttpStatus.OK.value(), "Support data saved successfully.", supportdata),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/support-list", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Support>>> getSupportList() {
		List<Support> supports;
		try {
			supports = supportService.getSupportList();
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<Support>>>(
				new Response<List<Support>>(HttpStatus.OK.value(), "Template fields fetched successfully.", supports),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public ResponseEntity<Response<Support>> updateSupport(@RequestBody Support support) {
		Support supportResult;

		try {
			/* supportResult =supportService.updateSupport(support); */
			supportResult = supportService.update(support.getId(), support);
		} catch (VedantaException e) {
			log.error("Error in updating support");
			throw new VedantaException("Error in updating support");
		}
		return new ResponseEntity<Response<Support>>(
				new Response<Support>(HttpStatus.OK.value(), "support updated", supportResult), HttpStatus.OK);
	}

}
