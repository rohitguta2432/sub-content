package com.vedanta.vpmt.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.EmailLog;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.User;

@Repository
public interface EmailLogDao extends JpaRepository<EmailLog, Long> {

	@Query("SELECT u FROM User u WHERE u.name=?1")
	public User getUserDetails(String name);

	@Query("SELECT e FROM EmailLog e WHERE e.assigneduserid=?1 AND e.stage=?2")
	public EmailLog getEmailLog(Long userId, int stageId);

	@Query("SELECT e FROM EmailLog e WHERE e.status=?1 AND e.stage=?2")
	public List<EmailLog> getEmailLogByStageAndStatus(int statusId, int stageId);

	@Query("SELECT DISTINCT e FROM ScoreCardGroupUser e WHERE e.contractNumber=?1")
	public Set<ScoreCardGroupUser> getScorecardgroupUser(String contractnumber);

	@Query("SELECT p FROM Contract p WHERE p.plantId=?1")
	public List<Contract> getContractDetailsByPlantId(Long plantId);

	@Query("SELECT v FROM Contract v WHERE v.vendorId=?1")
	public List<Contract> getContractDetailsByVendorId(Long vendorId);

	@Query("SELECT f FROM Form f WHERE f.categoryId=?1 AND f.subCategoryId=?2")
	public List<Form> getFormDetailsBycategoryIdAndSubcategoryId(Long categoryId, Long subcategoryId);
}
