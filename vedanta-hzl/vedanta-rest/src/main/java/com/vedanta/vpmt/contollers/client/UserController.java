package com.vedanta.vpmt.contollers.client;

import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.service.UserDetailServiceImpl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserDetailServiceImpl userDetail;

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<Response<User>> getUserByEmployeeId(@PathVariable("employeeId") String employeeId) {

		User user = null;
		try {
			user = userDetail.getUserByEmployeeId(employeeId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<User>>(
				new Response<User>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@RequestMapping(value = "byUserId/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Response<User>> getUserByUserId(@PathVariable("userId") Long userId) {

		User user = null;
		try {
			user = userDetail.getUserByUserId(userId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<User>>(
				new Response<User>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "all-users", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUsers() {

		List<User> user = null;
		try {
			user = userDetail.getAllUsers();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "/parent/{parentId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getUsersByParentId(@PathVariable("parentId") String parentId) {

		List<User> users = null;
		try {
			users = userDetail.getUsersByParentId(parentId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "Users fetched successfully", users), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "/get-all-rm/{parentId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllRMUsersByParentId(@PathVariable("parentId") String parentId) {

		List<User> users = null;
		try {
			users = userDetail.getAllRMUsersByParentId(parentId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "RM users fetched successfully", users), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ResponseEntity<Response<User>> update(@RequestBody User user) {

		String msg = "";
		try {
			if (user.getIsWithoutAdAllowed() == 1) {
				String password = user.getPassword();
				if (StringUtils.isNotBlank(password)) {
					String digest = DigestUtils.md5Hex(password);
					digest.equals(password);
					user.setPassword(digest);
				} else {
					Long userId = user.getId();
					User userDetails = userDetail.getUserByUserId(userId);
					String oldPassword = userDetails.getPassword();
					user.setPassword(oldPassword);
				}
			}
			user = userDetail.save(user);
			if (ObjectUtils.isEmpty(user.getId())) {
				msg = "User Already Exist";
			} else {
				msg = "User saved successfully";
			}
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<User>>(new Response<User>(HttpStatus.OK.value(), msg, user), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "currentUser", method = RequestMethod.GET)
	public ResponseEntity<Response<User>> getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

		Long userid = vedantaUser.getId();
		User userDetails = userDetail.getUserByUserId(userid);
		String emailId = userDetails.getEmail();

		User user = new User();
		user.setName(vedantaUser.getUsername());
		user.setEmail(emailId);
		user.setId(vedantaUser.getId());
		user.setLoginId(vedantaUser.getLoginId());
		user.setEmployeeId(vedantaUser.getEmployeeId());
		user.setBusinessUnitId(vedantaUser.getBusinessUnitId());
		user.setAuthorities(userDetails.getAuthorities());
		user.setPlantCode(userDetails.getPlantCode());

		return new ResponseEntity<Response<User>>(
				new Response<User>(HttpStatus.OK.value(), "Current User fetched successfully", user), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "/userValidation/{loginId}", method = RequestMethod.GET)
	public ResponseEntity<Response<User>> getUsersByloginId(@PathVariable("loginId") String loginId) {

		User userDetails = null;
		try {
			userDetails = userDao.findOneByLoginId(loginId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<User>>(
				new Response<User>(HttpStatus.OK.value(), "Users fetched successfully", userDetails), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUserByPlantHeadAndBusinessUnitId/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getUsersByBusinessId(
			@PathVariable("businessUnitId") Long businessUnitId) {

		List<User> users = null;
		try {
			users = userDetail.userByPlantHeadAndBusinessUnitId(businessUnitId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "Users fetched successfully", users), HttpStatus.OK);
	}

	// user list by buId
	// @PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "getAllUserByBussinessId/{businessId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getUsersByBusinessIdNotAdmin(
			@PathVariable("businessId") Long businessId) {

		List<User> users = null;
		try {
			users = userDetail.getUserByBusinessUnitIdNotAdmin(businessId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "Users fetched successfully", users), HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "getAllUserByHumanResourceAndBusinessUnitId/{businessUnitId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserByHumanResourceAndBusinessUnitId(
			@PathVariable("businessUnitId") Long businessUnitId) {
		List<User> users = null;
		try {
			users = userDetail.getAllUserByHumanResourceAndBusinessUnitId(businessUnitId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "Users fetched successfully", users), HttpStatus.OK);
	}
	

}
