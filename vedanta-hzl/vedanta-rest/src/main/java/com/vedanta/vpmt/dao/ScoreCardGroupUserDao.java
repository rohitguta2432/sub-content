package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.mapper.ScoreCardGroupUserMapper;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 11/09/17.
 */
@Repository
public interface ScoreCardGroupUserDao extends JpaRepository<ScoreCardGroupUser, Long> {

	@Query("SELECT DISTINCT sgu.contractManagerId FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.employeeId=?2")
	public Long getDistinctContractManagerByContractNumberAndUser(String contractNumber, String employeeId);

	@Query("SELECT new com.vedanta.vpmt.mapper.ScoreCardGroupUserMapper (sgu.contractNumber, sgu.contractManagerId, sgu.poItem, sgu.templateId, sgu.groupId) FROM ScoreCardGroupUser sgu WHERE sgu.employeeId=?1")
	public List<ScoreCardGroupUserMapper> getDistinctContractNumbersAndManagersByUserId(String employeeId);

	@Query("SELECT new com.vedanta.vpmt.mapper.ScoreCardGroupUserMapper (sgu.contractNumber, sgu.contractManagerId, sgu.poItem, sgu.templateId, sgu.groupId) FROM ScoreCardGroupUser sgu WHERE sgu.employeeId=?1 AND sgu.templateId=?2")
	public List<ScoreCardGroupUserMapper> getDistinctContractNumbersAndManagersByUserId(String employeeId,
			long templateId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.contractManagerId=?2 AND sgu.templateId=?3 AND sgu.groupId=?4")
	public ScoreCardGroupUser getScoreCardGroupUserForGroup(String contractNumber, String contractManagerId,
			Long templateId, Long groupId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.contractManagerId=?2")
	public List<ScoreCardGroupUser> getScoreCardGroupsByContractNumberAndManagerId(String contractNumber,
			String contractManagerId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.contractManagerId=?2 AND sgu.templateId=?3 AND sgu.groupId=?4")
	public List<ScoreCardGroupUser> getScoreCardGroupsByContractNumberAndManagerIdAndTemplateIdAndGroupId(
			String contractNumber, String contractManagerId, Long templateId, Long groupId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.contractManagerId=?2 AND sgu.templateId=?3")
	public List<ScoreCardGroupUser> getScoreCardGroupsByContractNumberAndManagerIdAndTemplateIdAndGroupId(
			String contractNumber, String contractManagerId, Long templateId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.employeeId=?2")
	public List<ScoreCardGroupUser> getScoreCardGroupsByContractNumberAndUser(String contractNumber, String employeeId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.poItem=?2 AND sgu.templateId=?3 AND sgu.groupId=?4")
	public List<ScoreCardGroupUser> getScoreCardGroupUserForGroupAmdTemplate(String contractNumber, String poItem,
			Long templateId, Long groupId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.templateId=?2 AND sgu.groupId=?3 AND sgu.businessUnitId=?4")
	public List<ScoreCardGroupUser> getScoreCardGroupUserForContractAndTemplate(String contractNumber, Long templateId,
			Long groupId, Long businessUnitId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractManagerId=?1 OR sgu.employeeId=?2 AND sgu.groupId=?3")
	public List<ScoreCardGroupUser> getScoreCardGroupUserByContractMangerOrEmployeeIdAndAllGroups(
			String contractMangerId, String employeeId, Long groupId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.employeeId=?2 AND sgu.businessUnitId=?3 AND sgu.templateId=?4 AND sgu.groupId=?5")
	public List<ScoreCardGroupUser> getScoreCardGroupUserByContractNumberAndEmployeeIdAndBusinessUnitIdAndTemplateIdAndAllGroups(
			String contractMangerId, String employeeId, Long businessUnitId, Long templateId, Long groupId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.poItem=?2")
	public List<ScoreCardGroupUser> getScoreCardGroupUserByContractNumberAndPOItem(String contractNumber,
			String poItem);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE sgu.contractNumber=?1 AND sgu.contractManagerId=?2")
	public List<ScoreCardGroupUser> getScoreCardGroupUserByContractNumberAndContractManagerId(String contractNumber,
			String contractManagerId);

	@Query("SELECT sgu FROM ScoreCardGroupUser sgu WHERE  sgu.contractId=?1 AND sgu.categoryId=?2 AND sgu.plantCode=?3 AND sgu.poItem=?4 AND sgu.templateId=?5 AND sgu.vendorCode=?6 AND sgu.subCategoryId=?7 AND sgu.groupId=0L")
	public List<ScoreCardGroupUser> getAllAssignedUser(Long contractId, Long categoryId, String plantCode,
			String poItem, Long templateId, String vendorCode, Long subCategoryId);

}
