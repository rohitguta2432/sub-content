package com.vedanta.vpmt.web.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.model.Notification;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "notification")
@Slf4j
public class NotificationController {

	private final static String NOTIFICATION_VIEW = "web/email/notificationDetails";

	@Autowired
	private NotificationService notificationService;

	@RequestMapping(value = "getNotification", method = RequestMethod.GET)
	public @ResponseBody List<Notification> getNotification() {
		List<Notification> notifications = null;
		try {
			notifications = notificationService.getAllNotification();
			return notifications;
		} catch (VedantaWebException e) {
			log.error("Error occurred while fetching notification details");
			throw new VedantaWebException("Error occurred while fetching notification details", e.code);
		}
	}

	@RequestMapping(value = "getNotificationById", method = RequestMethod.GET)
	public @ResponseBody List<Notification> getNotificationById() {

		List<Notification> notifications = null;

		try {
			notifications = notificationService.getNotificationById();
			return notifications;
		} catch (VedantaWebException e) {
			log.error("Error occurred while fetching notification details");
			throw new VedantaWebException("Error occurred while fetching notification details", e.code);
		}
	}

	@RequestMapping(value = "getAllnotification", method = RequestMethod.GET)
	public ModelAndView getAllnotification() {

		ModelAndView mav = new ModelAndView(NOTIFICATION_VIEW);
		List<Notification> notification = new ArrayList<Notification>();
		try {
			notification = notificationService.getAllnotification();
			mav.addObject("notification", notification);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error occurred while fetching template information");
			throw new VedantaWebException("Error occurred while fetching template information", e.code);
		}
	}

	@RequestMapping(value = "getAllnotificationData", method = RequestMethod.GET)
	public @ResponseBody List<Notification> getAllnotificationData() {

		List<Notification> notifications = null;

		try {
			notifications = notificationService.getAllnotification();
			return notifications;
		} catch (VedantaWebException e) {
			log.error("Error occurred while fetching notification details");
			throw new VedantaWebException("Error occurred while fetching notification details", e.code);
		}
	}

	@RequestMapping(value = "checked", method = RequestMethod.POST)
	public @ResponseBody void checked(@RequestBody Notification[] notifications) {

		try {

			for (Notification notification : notifications) {
				notificationService.checked(notification.getUserId());
			}

		} catch (VedantaWebException e) {
			log.error("Error occurred while changing the status of  notification ");
			throw new VedantaWebException("Error occurred while changing the status of  notification", e.code);
		}
	}

}
