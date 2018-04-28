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
import com.vedanta.vpmt.mapper.UserFormGroupDetailsMapper;
import com.vedanta.vpmt.model.UserFormGroup;
import com.vedanta.vpmt.service.UserFormGroupService;

@RestController
@RequestMapping(value = "userFormGroup", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserFormGroupController {

	@Autowired
	private UserFormGroupService userFormGroupService;

	@RequestMapping(value = "{userFormGroupId}", method = RequestMethod.GET)
	public ResponseEntity<Response<UserFormGroup>> getUserFormGroupById(
			@PathVariable("userFormGroupId") Long userFormGroupId) {
		UserFormGroup userFormGroup = null;
		try {
			userFormGroup = userFormGroupService.get(userFormGroupId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<UserFormGroup>>(new Response<UserFormGroup>(HttpStatus.OK.value(),

				"UserFormGroup fetched successfully", userFormGroup), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "all-userFormGroups", method = RequestMethod.GET)
	public ResponseEntity<Response<List<UserFormGroup>>> getAllUserFormGroups() {

		List<UserFormGroup> userFormGroups = null;
		try {
			userFormGroups = userFormGroupService.getAllUserFormGroups();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<UserFormGroup>>>(
				new Response<List<UserFormGroup>>(HttpStatus.OK.value(),

						"UserFormGroups fetched successfully", userFormGroups),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('SRM', 'ADMIN', 'SEGMENT_HEAD', 'USER_MGMT')")
	@RequestMapping(value = "save", method = RequestMethod.POST)

	public ResponseEntity<Response<List<UserFormGroup>>> save(
			@RequestBody UserFormGroupDetailsMapper userFormGroupDetailsMapper) {
		return new ResponseEntity<Response<List<UserFormGroup>>>(
				new Response<List<UserFormGroup>>(HttpStatus.OK.value(), "UserFormGroups saved successfully",
						userFormGroupService.saveUserFormGroupDetailsMapper(userFormGroupDetailsMapper)),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "view-user-form-groupdata/user/{userId}/vendor/{vendorId}/contract/{contractId}/form/{formId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<UserFormGroup>>> viewUserFormGroup(@PathVariable("userId") Long userId,
			@PathVariable("vendorId") Long vendorId, @PathVariable("contractId") Long contractId,
			@PathVariable("formId") Long formId) {

		return new ResponseEntity<Response<List<UserFormGroup>>>(
				new Response<List<UserFormGroup>>(HttpStatus.OK.value(), "Fetched UserFormGroupData List successfully",
						userFormGroupService.getUserFormGroupDataByVendorIdAndContractIdAndUserIdAndFormIdAnd(userId,
								vendorId, contractId, formId)),
				HttpStatus.OK);
	}
}
