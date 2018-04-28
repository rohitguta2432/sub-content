package com.vedanta.vpmt.contollers.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Notification;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.service.NotificationService;
import com.vedanta.vpmt.service.UserDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "notification", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserDetailServiceImpl userDetail;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Notification>> saveNotification(@RequestBody ScoreCardGroupUser scoreCardGroupUser) {
		Notification notification;
		try {
			notification = notificationService.saveNotification(scoreCardGroupUser);
		} catch (VedantaException e) {
			log.error("Error occurred while saving the notification details");
			throw new VedantaException("Error occurred while saving the notification details");
		}
		return new ResponseEntity<Response<Notification>>(
				new Response<Notification>(HttpStatus.OK.value(), "Notification saved successfully.", notification),
				HttpStatus.OK);
	}

	@RequestMapping(value = "getAll", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Notification>>> getAllNotification() {

		List<Notification> notifications = new ArrayList<>();
		try {
			notifications = notificationService.getAllNotification();
		} catch (Exception e) {
			log.error("Error occurred in fetching of Notification ");
			throw e;
		}
		return new ResponseEntity<Response<List<Notification>>>(new Response<List<Notification>>(HttpStatus.OK.value(),
				"Notification fetched successfully", notifications), HttpStatus.OK);
	}

	// submit scorecard template
	@RequestMapping(value = "submitNotification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Void>> submitNotification(@RequestBody ScoreCard scoreCard) {

		try {
			notificationService.submitNotification(scoreCard);
		} catch (VedantaException e) {
			log.error("Error occurred while saving the notification data");
			throw new VedantaException("Error occurred while saving the notification data");
		}
		return new ResponseEntity<Response<Void>>(
				new Response<Void>(HttpStatus.OK.value(), "Notification saved successfully.", null), HttpStatus.OK);
	}

	// get All notification by user Id
	@RequestMapping(value = "getById", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Notification>>> getNotificationById() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();
		Long empId = vedantaUser.getId();
		User userDetails = userDetail.getUserByUserId(empId);
		String userid = userDetails.getEmployeeId();

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean authorized = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

		List<Notification> notifications = new ArrayList<Notification>();
		if (authorized) {
			try {
				notifications = notificationService.getNotificationToAdmin(userid);
			} catch (Exception e) {
				log.error("Error occurred in fetching of Notification ");
				throw e;
			}

		} else {
			try {

				boolean user = authorities.contains(new SimpleGrantedAuthority("ROLE_CONTRACT_MANAGER"));
				if (user) {
					notifications = notificationService.getContractNotification(userid);
				} else {
					notifications = notificationService.getNotificationById(userid);
				}

			} catch (Exception e) {
				log.error("Error occurred in fetching of Notification ");
				throw e;
			}
		}

		return new ResponseEntity<Response<List<Notification>>>(new Response<List<Notification>>(HttpStatus.OK.value(),
				"Notification fetched successfully", notifications), HttpStatus.OK);
	}

	@RequestMapping(value = "getScorecadId/{id}/{poItem}", method = RequestMethod.GET)
	public ResponseEntity<Response<Long>> getDetailsByContractId(@PathVariable("id") long id,
			@PathVariable String poItem) {

		Long scorcardId = null;
		try {
			scorcardId = notificationService.getDetailsByContractId(id, poItem);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<Long>>(
				new Response<Long>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorcardId),
				HttpStatus.OK);
	}

	// notification checked

	@RequestMapping(value = "checked/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Void>> changeStatusByParamter(@PathVariable String id) {

		try {
			notificationService.checked(id);
		} catch (VedantaException e) {
			log.error("Error occurred while chnaging the status");
			throw new VedantaException("Error occurred while chnaging the status");
		}

		return new ResponseEntity<Response<Void>>(
				new Response<Void>(HttpStatus.OK.value(), "Change successfully.", null), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllNotification", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Notification>>> getAllNotificationDetails() {

		List<Notification> notifications = new ArrayList<Notification>();
		try {
			notifications = notificationService.getAllNotificationDetails();
		} catch (Exception e) {
			log.error("Error occurred in fetching of notification");
			throw e;
		}
		return new ResponseEntity<Response<List<Notification>>>(new Response<List<Notification>>(HttpStatus.OK.value(),
				"Notification fetched successfully", notifications), HttpStatus.OK);
	}

}
