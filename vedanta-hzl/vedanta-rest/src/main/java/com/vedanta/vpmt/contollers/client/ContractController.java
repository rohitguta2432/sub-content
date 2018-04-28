package com.vedanta.vpmt.contollers.client;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "contract", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContractController {

	@Autowired
	private ContractService contractService;

	@RequestMapping(value = "{contractId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Contract>> getContractById(@PathVariable("contractId") Long contractId) {

		Contract contract = null;
		try {
			contract = contractService.get(contractId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Contract>>(
				new Response<Contract>(HttpStatus.OK.value(), "Contract fetched successfully", contract),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "contracts", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Contract>>> getAllContracts() {

		List<Contract> contracts = null;
		try {
			contracts = contractService.getAllContracts();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<Contract>>>(
				new Response<List<Contract>>(HttpStatus.OK.value(), "Contracts fetched successfully", contracts),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<Contract>> save(@RequestBody Contract contract) {

		try {
			contract = contractService.save(contract);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Contract>>(
				new Response<Contract>(HttpStatus.OK.value(), "Contract saved successfully", contract), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "update", method = RequestMethod.PUT)
	public ResponseEntity<Response<Contract>> update(@RequestBody Contract contract) {

		try {
			contract = contractService.save(contract);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<Contract>>(
				new Response<Contract>(HttpStatus.OK.value(), "Contract saved successfully", contract), HttpStatus.OK);
	}

	@RequestMapping(value = "vendorId/{vendorId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Contract>>> getcontractByVendorId(@PathVariable("vendorId") Long vendorId) {

		List<Contract> contract = null;
		try {
			contract = contractService.getContractByVendorId(vendorId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<Contract>>>(
				new Response<List<Contract>>(HttpStatus.OK.value(), "Contract fetched successfully", contract),
				HttpStatus.OK);
	}
}
