package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.mapper.FormGroupUserMapper;
import com.vedanta.vpmt.model.FormGroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 11/09/17.
 */
@Repository
public interface FormGroupUserDao extends JpaRepository<FormGroupUser, Long> {

	@Query("SELECT DISTINCT fgu.contractManagerId FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.employeeId=?2")
	public Long getDistinctContractManagerByContractNumberAndUser(String contractNumber, String employeeId);

	@Query("SELECT new com.vedanta.vpmt.mapper.FormGroupUserMapper (fgu.contractNumber, fgu.contractManagerId, fgu.formId, fgu.formGroupDetailId) FROM FormGroupUser fgu WHERE fgu.employeeId=?1")
	public List<FormGroupUserMapper> getDistinctContractNumbersAndManagersByUserId(String employeeId);

	@Query("SELECT new com.vedanta.vpmt.mapper.FormGroupUserMapper (fgu.contractNumber, fgu.contractManagerId, fgu.formId, fgu.formGroupDetailId) FROM FormGroupUser fgu WHERE fgu.employeeId=?1 AND fgu.formId=?2")
	public List<FormGroupUserMapper> getDistinctContractNumbersAndManagersByUserId(String employeeId, long formId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.contractManagerId=?2 AND fgu.formId=?3 AND fgu.formGroupDetailId=?4")
	public FormGroupUser getFormGroupUserForGroup(String contractNumber, String contractManagerId, Long formId,
			Long formGroupDetailId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.contractManagerId=?2")
	public List<FormGroupUser> getFormGroupsByContractNumberAndManagerId(String contractNumber,
			String contractManagerId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.contractManagerId=?2 AND fgu.formId=?3 AND fgu.formGroupDetailId=?4")
	public List<FormGroupUser> getFormGroupsByContractNumberAndManagerIdAndFormIdAndFormGroupDetailId(
			String contractNumber, String contractManagerId, Long formId, Long formGroupDetailId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.contractManagerId=?2 AND fgu.formId=?3")
	public List<FormGroupUser> getFormGroupsByContractNumberAndManagerIdAndFormIdAndFormGroupDetailId(
			String contractNumber, String contractManagerId, Long formId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.employeeId=?2")
	public List<FormGroupUser> getFormGroupsByContractNumberAndUser(String contractNumber, String employeeId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.contractManagerId=?2 AND fgu.formId=?3 AND fgu.formGroupDetailId=?4")
	public List<FormGroupUser> getFormGroupUserForGroupAndForm(String contractNumber, String contractManagerId,
			Long formId, long formGroupDetailId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.formId=?2 AND fgu.employeeId=?3")
	public List<FormGroupUser> getFormGroupUserForUserAndForm(String contractNumber, Long formId, String employeeId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.formId=?2 AND fgu.formGroupDetailId=?3 AND fgu.businessUnitId=?4")
	public List<FormGroupUser> getFormGroupUserForContractAndForm(String contractNumber, Long formId,
			Long formGroupDetailId, Long businessUnitId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.poItem=?2")
	public List<FormGroupUser> getFormGroupUserByContractNumberAndPOItem(String contractNumber, String poItem);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.contractManagerId=?2")
	public List<FormGroupUser> getFormGroupUserByContractNumberAndContractManagerId(String contractNumber,
			String contractManagerId);
}
