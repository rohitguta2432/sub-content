package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.Notification;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;

public interface NotificationService extends VedantaService<Notification> {

	public Notification saveNotification(ScoreCardGroupUser scoreCardGroupUser);

	public List<Notification> getAllNotification();

	public void submitNotification(ScoreCard scoreCardGroupUser);

	public List<Notification> getNotificationToAdmin(String userId);

	public List<Notification> getContractNotification(String UserId);

	public List<Notification> getNotificationById(String id);

	public Long getDetailsByContractId(Long id, String poItem);

	public void checked(String id);

	public List<Notification> getAllNotificationDetails();

}
