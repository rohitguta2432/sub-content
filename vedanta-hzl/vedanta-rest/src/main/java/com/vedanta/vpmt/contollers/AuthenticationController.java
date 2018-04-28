package com.vedanta.vpmt.contollers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.DuplicateLoginException;
import com.vedanta.vpmt.model.AuthenticationToken;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.security.LDAPAuthenticationProvider;
import com.vedanta.vpmt.security.TokenUtils;
import com.vedanta.vpmt.service.AuthenticationTokenService;
import com.vedanta.vpmt.service.UserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AuthenticationController {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private UserDetailServiceImpl userDetailsService;

	@Autowired
	private AuthenticationTokenService authenticationTokenService;

	@Autowired
	private LDAPAuthenticationProvider ldapAuthenticationProvider;

	@Value("${vedanta.token.inactivity.time}")
	private int inactivityTime;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Map<String, Object>>> generateAuthentication(@RequestBody User user,
			HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> dataMap = new HashMap<>();

		boolean isAdminRequest = request.getHeader(Constants.ADMIN_REQUEST) == null ? false : true;

		// log.info(String.format("Attempting to generate authorization token for user :
		// %s",user.getLoginId()));
		String loginId = user.getLoginId();

		User userData = userDetailsService.findByLoginId(loginId, user.getBusinessUnitId());

		VedantaUser userDetails = new VedantaUser();

		Boolean isWithoutADAllowedFlag = false;
		if (!ObjectUtils.isEmpty(userData)) {
			if (userData.getIsWithoutAdAllowed() > 0) {
				isWithoutADAllowedFlag = true;
				String password = DigestUtils.md5Hex(user.getPassword());
				if (!password.equals(userData.getPassword())) {
					throw new BadCredentialsException("username and/or password not valid");
				}
				userDetails = getUserDetails(loginId,user.getBusinessUnitId());
			}
		}

		if (!isWithoutADAllowedFlag) {
			if (user.getBusinessUnitId() == 0) {
				throw new BadCredentialsException("username and/or password not valid");
			}
			JSONObject ldapResponse = ldapAuthenticationProvider.searchUser(loginId, user.getPassword(),
					user.getBusinessUnitId());
			userDetails = getUserDetails(loginId, ldapResponse, user.getBusinessUnitId());
		}

		AuthenticationToken authenticationToken = authenticationTokenService.getAuthTokenByUserId(userDetails.getId());

		Boolean isExpired = true;

		if (!ObjectUtils.isEmpty(authenticationToken)) {
			isExpired = authenticationTokenService.isExpired(authenticationToken);
			if (ObjectUtils.isEmpty(tokenUtils.getUsernameFromToken(authenticationToken.getAuthToken()))) {
				isExpired = true;
			}
		}
		if (!isExpired) {
			if (!ObjectUtils.isEmpty(authenticationToken)) {
				dataMap.put(Constants.AUTH_TOKEN, authenticationToken.getAuthToken());
				dataMap.put(Constants.USER, userDetails);

				return ResponseEntity.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(),
						"Token generated successfully", dataMap));
			}
		} else {
			authenticationTokenService.deleteAuthenticationToken(userDetails.getId());
		}

		if (!userDetails.isEnabled()) {
			log.info(String.format("User with user name : %s not active", user.getLoginId()));
			throw new DisabledException("User account is not active");
		}

		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), Constants.P_STRING,
				userDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
		String token = this.tokenUtils.generateToken(userDetails);
		String persistedToken;
		if (StringUtils.isEmpty(user.getDeviceToken())) {
			persistedToken = persistAuthToken(userDetails.getId(), token, isAdminRequest,userDetails.getBusinessUnitId());
		} else {
			persistedToken = persistAuthToken(userDetails.getId(), token, isAdminRequest, user.getDeviceToken(),userDetails.getBusinessUnitId());
		}

		log.info(String.format("Authorization token generated for user : %s successfully", user.getLoginId()));

		dataMap.put(Constants.AUTH_TOKEN, persistedToken);
		dataMap.put(Constants.USER, userDetails);
		/* dataMap.put(Constants.SAML_RESPONSE, ldapResponse.toString()); */

		// Return the token
		return ResponseEntity
				.ok(new Response<Map<String, Object>>(HttpStatus.OK.value(), "Token generated successfully", dataMap));
	}

	@RequestMapping(value = "logout/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Boolean>> logout(@PathVariable(value = "userId") long userId) {

		try {
			authenticationTokenService.deleteAuthenticationToken(userId);
		} catch (Exception e) {
			log.error("Error while deleting auth token for user id : ", userId, e);
			throw e;
		}

		return ResponseEntity
				.ok(new Response<Boolean>(HttpStatus.OK.value(), "User auth token deleted successfully", Boolean.TRUE));
	}

	@RequestMapping(value = "logout/token", method = RequestMethod.GET)
	public ResponseEntity<Response<Boolean>> deleteAuthToken() {

		long userId = 0;
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			userId = vedantaUser.getId();
			authenticationTokenService.deleteAuthenticationToken(vedantaUser.getId());
		} catch (Exception e) {
			log.error("Error while deleting auth token for user id." + userId, e);
			throw e;
		}

		return ResponseEntity
				.ok(new Response<Boolean>(HttpStatus.OK.value(), "User auth token deleted successfully", Boolean.TRUE));
	}

	private String persistAuthToken(Long userId, String token, boolean isAdminRequest, String deviceToken,Long businessUnitId)
			throws JsonParseException, JsonMappingException, IOException {

		AuthenticationToken existingAuthToken = authenticationTokenService.getAuthTokenByUserId(userId);

		if (existingAuthToken != null) {
			if (!existingAuthToken.isAdminToken() && isTokenValid(existingAuthToken)) {
				if (!StringUtils.isEmpty(existingAuthToken.getDeviceToken())
						&& existingAuthToken.getDeviceToken().equals(deviceToken)) {
					log.info("Authentication token exists for user,", userId, " hence using it.");
					authenticationTokenService.updateLastAccessedTime(existingAuthToken.getId());
					return existingAuthToken.getAuthToken();
				} else {
					log.info("Authentication token exists for user,", userId, " hence blocking it.");
					throw new DuplicateLoginException("Duplicate login is not allowed.");
				}
			} else {
				authenticationTokenService.deleteAuthenticationToken(userId);
				log.info("Persisting auth token to DB.");
				authenticationTokenService.save(new AuthenticationToken(userId, token, isAdminRequest, deviceToken,businessUnitId));
				return token;
			}
		} else {
			log.info("Persisting auth token to DB.");
			authenticationTokenService.save(new AuthenticationToken(userId, token, isAdminRequest, deviceToken,businessUnitId));
			return token;
		}
	}

	private String persistAuthToken(Long userId, String token, boolean isAdminRequest,Long businessUnitId)
			throws JsonParseException, JsonMappingException, IOException {

		AuthenticationToken existingAuthToken = authenticationTokenService.getAuthTokenByUserId(userId);

		if (existingAuthToken != null) {
			if (!existingAuthToken.isAdminToken() && isTokenValid(existingAuthToken)) {

				log.info("Authentication token exists for user,", userId, " hence blocking it.");
				throw new DuplicateLoginException("Duplicate login is not allowed.");
			} else {
				authenticationTokenService.deleteAuthenticationToken(userId);
				log.info("Persisting auth token to DB.");
				authenticationTokenService.save(new AuthenticationToken(userId, token, isAdminRequest,businessUnitId));
				return token;
			}
		} else {
			log.info("Persisting auth token to DB.");
			authenticationTokenService.save(new AuthenticationToken(userId, token, isAdminRequest,businessUnitId));
			return token;
		}
	}

	private VedantaUser getUserDetails(String userName, JSONObject ldapResponse, Long businessUnitId) {
		try {
			return (VedantaUser) this.userDetailsService.loadUserByUsername(userName);
		} catch (UsernameNotFoundException e) {
			User user = userDetailsService.saveLDAPUser(createUser(userName, ldapResponse, businessUnitId));

			return new VedantaUser(user.getId(), user.getName(), Constants.P_STRING, user.getLoginId(),
					AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities()), user.getEmail(),
					user.getEmployeeId(), user.getStatus() > 0, user.getIsWithoutAdAllowed(), user.getBusinessUnitId(),
					user.getPlantCode(), user.getPlantId());
		}
	}

	private VedantaUser getUserDetails(String userName,Long businessUnitId) {
		VedantaUser userDetails = (VedantaUser) this.userDetailsService.loadUserByUsernameAndBuId(userName,businessUnitId);
		if (userDetails == null) {
			log.error("No user found with login Id : ", userName);
			throw new UsernameNotFoundException("No user found with login Id : " + userName);
		}
		return userDetails;
	}

	private boolean isTokenValid(AuthenticationToken existingAuthToken) {
		long timeDIff = System.currentTimeMillis() - existingAuthToken.getLastAccessed().getTime();
		return TimeUnit.MILLISECONDS.toMinutes(timeDIff) < inactivityTime;
	}

	private User createUser(String userName, JSONObject ldapResponse, Long businessUnitId) {

		User user = new User();
		if (ldapResponse.has("cn")) {
			user.setName(ldapResponse.getString("cn"));
		}

		if (ldapResponse.has("sAMAccountName")) {
			user.setEmployeeId(ldapResponse.getString("sAMAccountName"));
		}

		if (ldapResponse.has("mail")) {
			user.setEmail(ldapResponse.getString("mail"));
		}

		if (ldapResponse.has("telephonenumber")) {
			user.setTelephone(ldapResponse.getString("telephonenumber"));
		}

		if (ldapResponse.has("l")) {
			user.setCity(ldapResponse.getString("l"));
		}

		if (ldapResponse.has("department")) {
			user.setDepartmentName(ldapResponse.getString("department"));
		}

		if (ldapResponse.has("company")) {
			user.setCompany(ldapResponse.getString("company"));
		}

		if (ldapResponse.has("physicalDeliveryOfficeName")) {
			user.setOffice(ldapResponse.getString("physicalDeliveryOfficeName"));
		}

		user.setAuthorities(Constants.ROLE_CONTRACT_MANAGER);
		user.setLoginId(userName);
		user.setStatus(Constants.STATUS_ACTIVE);
		user.setBusinessUnitId(businessUnitId);
		return user;
	}
}
