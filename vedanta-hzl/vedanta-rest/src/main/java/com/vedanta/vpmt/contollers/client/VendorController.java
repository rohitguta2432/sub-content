package com.vedanta.vpmt.contollers.client;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.model.Vendor;
import com.vedanta.vpmt.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "vendor", produces = MediaType.APPLICATION_JSON_VALUE)
public class VendorController {

	@Autowired
	private VendorService vendorService;

	@RequestMapping(value = "{vendorId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Vendor>> getVendorById(@PathVariable("vendorId") Long vendorId) {
		Vendor vendor = null;
		try {
			vendor = vendorService.get(vendorId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Vendor>>(
				new Response<Vendor>(HttpStatus.OK.value(), "Vendor fetched successfully", vendor), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "vendors", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Vendor>>> getAllVendors() {

		List<Vendor> vendors = null;
		try {
			vendors = vendorService.getAllVendors();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<Vendor>>>(
				new Response<List<Vendor>>(HttpStatus.OK.value(), "Vendors fetched successfully", vendors),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<Vendor>> save(@RequestBody Vendor vendor) {
		try {
			vendor = vendorService.save(vendor);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Vendor>>(
				new Response<Vendor>(HttpStatus.OK.value(), "Vendor saved successfully", vendor), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public ResponseEntity<Response<Vendor>> update(@RequestBody Vendor vendor) {
		try {
			vendor = vendorService.save(vendor);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Vendor>>(
				new Response<Vendor>(HttpStatus.OK.value(), "Vendor saved successfully", vendor), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "getAllVendorsByBusinessUnit/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Vendor>>> getAllVendorsByBusinessUnit(@PathVariable("buId") Long buId) {
		List<Vendor> vendors = null;
		try {
			vendors = vendorService.getAllVendorsByBusinessUnit(buId);

		} catch (Exception e) {
			throw e;
		}

		return new ResponseEntity<Response<List<Vendor>>>(
				new Response<List<Vendor>>(HttpStatus.OK.value(), "Vendor fetched successfully", vendors),
				HttpStatus.OK);

	}

}
