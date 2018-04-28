package com.vedanta.vpmt.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.NotificationDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Notification;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCard.Param;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;

import lombok.extern.slf4j.Slf4j;

@Service("NotificationService")
@Slf4j
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Autowired
	private UserDetailServiceImpl userDetail;

	// notification assigned to user
	@Override
	public Notification saveNotification(ScoreCardGroupUser scoreCardGroupUser) {
		if (ObjectUtils.isEmpty(scoreCardGroupUser)) {
			log.info("Notification object cannot be null");
			throw new VedantaException("Notification object cannot be null");
		}

		String userName = notificationDao.getUserNameByEmpId(scoreCardGroupUser.getEmployeeId());
		String groupName = notificationDao.getGroupName(scoreCardGroupUser.getGroupId());
		String contractManager = notificationDao.getContarctManager(scoreCardGroupUser.getContractManagerId());
		Notification notification = null;
		notification = new Notification();

		notification.setUserId(scoreCardGroupUser.getEmployeeId());
		notification.setContractId(scoreCardGroupUser.getContractId());
		notification.setTemplateId(scoreCardGroupUser.getTemplateId());
		notification.setContractNo(scoreCardGroupUser.getContractNumber());
		notification.setGroupId(scoreCardGroupUser.getGroupId());
		notification.setStatus(0);
		notification.setAssignedDate(new Date());
		notification.setPoItem(scoreCardGroupUser.getPoItem());
		notification.setUserName(userName);
		notification.setApproved(0);
		notification.setTemplateName(scoreCardGroupUser.getTemplateName());
		notification.setGroupName(groupName);
		notification.setContractManager(contractManager);
		notification.setContractManagerId(scoreCardGroupUser.getContractManagerId());
		notification.setScorecardId(scoreCardGroupUser.getScorecardId());

		return notificationDao.save(notification);

	}

	@Override
	public Notification get(long id) {
		return null;
	}

	@Override
	public Notification save(Notification entity) {
		return null;
	}

	@Override
	public Notification update(long id, Notification entity) {
		return null;
	}

	@Override
	public List<Notification> getAllNotification() {
		try {
			List<Notification> notifications = (List<Notification>) notificationDao.findAll();
			return notifications;
		} catch (VedantaException e) {
			log.error("Error while getting all notifications");
			throw new VedantaException("Error while getting all notifications");
		}
	}

	@Override
	@Transactional
	public void submitNotification(ScoreCard scoreCard) {

		if (ObjectUtils.isEmpty(scoreCard)) {
			log.info("Notification object cannot be null");
			throw new VedantaException("Notification object cannot be null");
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();
		Long empId = vedantaUser.getId();
		User userDetails = userDetail.getUserByUserId(empId);
		String userName = notificationDao.getUserNameByEmpId(userDetails.getEmployeeId());

		int status = scoreCard.getStatus();

		Long contractId = scoreCard.getContractId();
		Long templateId = scoreCard.getTemplateId();
		Date date = new Date();

		List<List<Param>> card = scoreCard.getScoreParamList();
		for (int i = 0; i < card.size(); i++) {

			Long groupId = Long.parseLong(card.get(i).get(0).getGroupId());
			String groupName = notificationDao.getGroupName(groupId);
			Notification notification = new Notification();

			notification.setContractId(contractId);
			notification.setTemplateId(templateId);
			notification.setTemplateName(scoreCard.getTemplateName());
			notification.setContractNo(scoreCard.getContractNumber());
			notification.setUserName(userName);
			notification.setUserId(userDetails.getEmployeeId());
			notification.setSubmittedOn(date);
			notification.setSubmittedBy(userName);
			notification.setStatus(1);
			notification.setPoItem(scoreCard.getPoItem());
			notification.setGroupId(groupId);
			notification.setGroupName(groupName);
			notification.setContractManagerId(scoreCard.getContractManagerId());
			notification.setContractManager(scoreCard.getContractManagerName());
			notification.setScorecardId(scoreCard.getId());

			try {
				if (status == Constants.STATUS_APPROVED) {
					notification.setApproved(1);
					notification.setApprovedBy(userName);
					notification.setStatus(2);
					notification.setChecked(0);

					notificationDao.save(notification);
				} else if (status == Constants.STATUS_REJECTED) {
					notification.setApproved(0);
					notification.setApprovedBy(userName);
					notification.setStatus(3);
					notification.setChecked(0);

					notificationDao.save(notification);
				} else {
					notificationDao.save(notification);

				}

			} catch (VedantaException e) {
				log.error("Error occurred while submitting notification details");
				throw new VedantaException("Error occurred while submitting notification details");
			}

		}

	}

	@Override
	public List<Notification> getNotificationById(String id) {
		try {
			Pageable limit = new PageRequest(0, 50);
			List<Notification> notifications = (List<Notification>) notificationDao.getById(id, limit);
			return notifications;
		} catch (VedantaException e) {
			log.error("Error occurred while getting all notifications");
			throw new VedantaException("Error occurred while getting all notifications");
		}
	}

	@Override
	public List<Notification> getContractNotification(String userId) {

		try {
			Pageable limit = new PageRequest(0, 50);
			List<Notification> notifications = (List<Notification>) notificationDao.getByContractManagerId(userId,
					limit);
			return notifications;
		} catch (VedantaException e) {
			log.error("Error occurred while getting all notifications");
			throw new VedantaException("Error occurred while getting all notifications");
		}
	}

	@Override
	public List<Notification> getNotificationToAdmin(String userId) {
		try {
			Pageable limit = new PageRequest(0, 5);
			List<Notification> notifications = (List<Notification>) notificationDao.getByAdminId(userId, limit);
			return notifications;
		} catch (VedantaException e) {
			log.error("Error while getting all notifications");
			throw new VedantaException("Error while getting all notifications");
		}
	}

	@Override
	public Long getDetailsByContractId(Long id, String poItem) {
		try {
			Long scorecardId = notificationDao.getDetailsByContractId(id, poItem);
			return scorecardId;
		} catch (VedantaException e) {
			log.error("Error while getting all notifications");
			throw new VedantaException("Error while getting all notifications");
		}

	}

	@Override
	@Transactional
	public void checked(String id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();
		Long empId = vedantaUser.getId();
		User userDetails = userDetail.getUserByUserId(empId);
		String userid = userDetails.getEmployeeId();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean authorized = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

		if (authorized) {
			try {
				notificationDao.checkedAdmin(id);
			} catch (Exception e) {
				log.error("Error occurred in fetching of Notification ");
				throw e;
			}
		} else {

			try {

				boolean user = authorities.contains(new SimpleGrantedAuthority("ROLE_CONTRACT_MANAGER"));
				if (user) {
					notificationDao.checkedContractManager(userid);
				} else {
					notificationDao.checked(userid);
				}

			} catch (VedantaException e) {
				log.error("Error while changing the status of  notification");
				throw new VedantaException("Error while changing the status of  notification");
			}

		}

	}

	@Override
	public List<Notification> getAllNotificationDetails() {

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
				notifications = notificationDao.getAdminNotificationDetails();
			} catch (Exception e) {
				log.error("Error occurred in fetching of Notification ");
				throw e;
			}

		} else {
			try {

				boolean user = authorities.contains(new SimpleGrantedAuthority("ROLE_CONTRACT_MANAGER"));
				if (user) {
					notifications = notificationDao.getContractNotificationDetails(userid);
				} else {
					notifications = notificationDao.getUserNotificationDetails(userid);
				}

			} catch (Exception e) {
				log.error("Error occurred in fetching of Notification ");
				throw e;
			}
		}

		return notifications;

	}

}
