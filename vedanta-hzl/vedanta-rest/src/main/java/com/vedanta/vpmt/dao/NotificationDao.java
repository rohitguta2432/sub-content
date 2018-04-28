package com.vedanta.vpmt.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.Notification;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Long> {

	@Query("SELECT u.name FROM User u WHERE u.employeeId=?1 ")
	public String getUserNameByEmpId(String employeeId);

	@Query("SELECT u.labelName FROM Group u WHERE u.id=?1 ")
	public String getGroupName(Long groupId);

	// notification to user
	@Query("SELECT n FROM Notification n  WHERE  (n.userId=?1 and n.status IN (0,2,3)) OR (n.contractManagerId=?1 and n.status=1)  ORDER BY n.id DESC")
	public List<Notification> getById(String id, Pageable pageable);

	// notification to contract manager
	@Query("SELECT n FROM Notification n WHERE  (n.userId=?1 and n.status IN (0,2,3)) OR (n.contractManagerId=?1 and n.status=1) ORDER BY n.id DESC")
	public List<Notification> getByContractManagerId(String id, Pageable pageable);

	// notification to ADMIN
	@Query("SELECT n FROM Notification n  WHERE n.status=0 and n.userId=?1 ORDER BY n.id ")
	public List<Notification> getByAdminId(String userId, Pageable pageable);

	@Modifying
	@Query("UPDATE  Notification  SET checked=1 where  (status=2 and contractManagerId=?1) OR (userId=?1 and status=0)")
	public void checkedAdmin(String id);

	@Modifying
	@Query("UPDATE  Notification  SET checked=1 where  (userId=?1 and status IN (0,2,3)) OR (status=1 and contractManagerId=?1)")
	public void checkedContractManager(String id);

	@Modifying
	@Query("UPDATE  Notification  SET checked=1 where  (userId=?1 and status IN (0,2,3)) OR (status=1 and contractManagerId=?1) ")
	public void checked(String id);

	@Query("SELECT n FROM Notification n WHERE n.status=2   ORDER BY id DESC")
	public List<Notification> getAdminNotificationDetails();

	@Query("SELECT n FROM Notification n WHERE (n.status IN (0,2,3) and n.userId=?1) OR (n.status=1 and n.contractManagerId=?1)   ORDER BY id DESC")
	public List<Notification> getContractNotificationDetails(String userId);

	@Query("SELECT n FROM Notification n WHERE (n.status IN (0,2,3) and n.userId=?1)  OR (n.status=1 and n.contractManagerId=?1)  ORDER BY id DESC")
	public List<Notification> getUserNotificationDetails(String userId);

	@Query("SELECT sc.id FROM ScoreCard sc WHERE sc.contractId=?1 AND poItem=?2")
	public Long getDetailsByContractId(Long id, String poItem);

	@Query("SELECT u.name FROM User u WHERE u.employeeId=?1 ")
	public String getContarctManager(String employeeId);

}
