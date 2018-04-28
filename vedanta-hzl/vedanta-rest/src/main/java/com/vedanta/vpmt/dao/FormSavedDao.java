package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by manishsanger on 11/09/17.
 */
@Repository
public interface FormSavedDao extends JpaRepository<FormSaved, Long> {

	@Query("SELECT fs FROM FormSaved fs WHERE fs.status=?1")
	public List<FormSaved> getAllFormSaved(int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.status!=?1")
	public List<FormSaved> getAllFormSavedNotStatus(int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.status=?2")
	public List<FormSaved> getAllFormSaved(Long businessUnitId, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.plantCode=?2 AND fs.status=?3")
	public List<FormSaved> getAllFormSaved(Long businessUnitId, String plantCode, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.status!=?2")
	public List<FormSaved> getAllFormSavedNotStatus(Long businessUnitId, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.plantCode=?2 AND fs.status!=?3")
	public List<FormSaved> getAllFormSavedNotStatus(Long businessUnitId, String plantCode, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.plantCode  IN (?1) AND fs.businessUnitId=?2 AND fs.status=?3")
	public List<FormSaved> getAllFormSavedByPlantCodesAndBusinessIdAndStatus(Set<String> plantCode, Long businessUnitId,
			int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.plantCode  IN (?1) AND fs.businessUnitId=?2 AND fs.status!=?3")
	public List<FormSaved> getAllFormSavedByPlantCodesAndBusinessIdNotStatus(Set<String> plantCode, Long businessUnitId,
			int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractManagerId =?1 AND fs.status=?2")
	public List<FormSaved> getFormSavedByManagerAndStatus(String contractManagerId, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractManagerId =?1 AND fs.status!=?2")
	public List<FormSaved> getFormSavedByManagerAndNotStatus(String contractManagerId, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.contractManagerId =?2 AND fs.status=?3")
	public List<FormSaved> getFormSavedByManagerAndStatus(Long businessUnitId, String contractManagerId, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.contractManagerId =?2 AND fs.status!=?3")
	public List<FormSaved> getFormSavedByManagerAndNotStatus(Long businessUnitId, String contractManagerId, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.status=?1 AND fs.monthId=?2 AND fs.yearId=?3")
	public List<FormSaved> getAllFormSavedByStatusAndMonthAndYear(int status, int monthId, int yearId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractId=?1")
	public List<FormSaved> getFormSavedByContractId(long contractId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber=?1 AND fs.contractManagerId=?2 AND fs.monthId=?3 AND fs.yearId=?4")
	public FormSaved getFormSavedByContractNumberAndManagerAndMonthAndYear(String contractNumber, long contractManager,
			int monthId, int yearId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber=?1 AND fs.monthId=?2 AND fs.yearId=?3")
	public List<FormSaved> getFormSavedByContractNumberAndMonthAndYear(String contractNumber, int monthId, int yearId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.vendorCode=?1")
	public List<FormSaved> getFormSavedByVendorCode(String vendorCode);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.vendorId=?1")
	public List<FormSaved> getFormSavedByVendorId(long vendorId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.vendorCode=?1 AND fs.monthId=?2 AND fs.yearId=?3")
	public List<FormSaved> getFormSavedByVendorCodeAndMonthAndYear(String vendorCode, int monthId, int yearId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.formId IN (?1) AND fs.monthId=?2 AND fs.yearId=?3 AND fs.status=?4 AND fs.businessUnitId=?5")
	public List<FormSaved> getFormSavedByFormIdAndMonthAndYearAndStatus(Set<Long> formIds, int monthId, int yearId,
			int status, Long businessUnitId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber=?1")
	public List<FormSaved> getFormSavedByContractNumber(String contractNumber);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber=?1 AND fs.status=?2")
	public List<FormSaved> getContractFormSavedByStatus(String contractNumber, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber IN (?1) AND fs.contractManagerId IN (?2) AND fs.status=?3")
	public List<FormSaved> getFormSavedByManagersAndContractNumbers(Set<String> contractNumbers,
			Set<String> contractManagerIds, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber IN (?1) AND fs.contractManagerId IN (?2) AND fs.status!=?3")
	public List<FormSaved> getFormSavedByManagersAndContractNumbersNotStatus(Set<String> contractNumbers,
			Set<String> contractManagerIds, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber IN (?1) AND fs.contractManagerId IN (?2) AND fs.formId IN (?3) AND fs.status=?4")
	public List<FormSaved> getFormSavedByManagersAndContractNumbersAndFormIds(Set<String> contractNumbers,
			Set<String> contractManagerIds, Set<Long> formIds, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber IN (?1) AND fs.contractManagerId IN (?2) AND fs.formId IN (?3) AND fs.status!=?4")
	public List<FormSaved> getFormSavedByManagersAndContractNumbersAndFormIdsNotStatus(Set<String> contractNumbers,
			Set<String> contractManagerIds, Set<Long> formIds, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.contractNumber IN (?2) AND fs.contractManagerId IN (?3) AND fs.formId IN (?4) AND fs.status=?5")
	public List<FormSaved> getFormSavedByManagersAndContractNumbersAndFormIds(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, Set<Long> formIds, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.contractNumber IN (?2) AND fs.contractManagerId IN (?3) AND fs.formId IN (?4) AND fs.status!=?5")
	public List<FormSaved> getFormSavedByManagersAndContractNumbersAndFormIdsNotStatus(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, Set<Long> formIds, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.plantCode=?1 AND fs.contractNumber=?2 AND fs.formId=?3 AND fs.monthId=?4 AND fs.yearId=?5")
	public List<FormSaved> getComplianceFormByMonthAndStatus(String plantCode, String contractNumber, Long formId,
			int monthId, int yearId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.plantCode=?2 AND fs.status=?3")
	public List<FormSaved> getFormSavedByBuIdAndPlantCodeAndStatus(Long businessUnitId, String plantCode, int status);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.businessUnitId=?1 AND fs.plantCode=?2")
	public List<FormSaved> getFormSavedByBuIdAndPlantCode(Long businessUnitId, String plantCode);

	@Query(value = "SELECT u FROM User u  WHERE u.businessUnitId=?1 AND u.authorities NOT IN (?2) and u.status!=?3")
	public List<User> getAllUserNotAdminAndHrByLimit(Long businessUnitId, Set<String> authorities, int status,
			Pageable pageable);
	
	
	@Query(value = "SELECT u FROM User u  WHERE u.businessUnitId=?1 AND u.authorities=?2 and u.status!=?3")
	public List<User> getAllHrUserByLimit(Long businessUnitId, String authorities, int status,
			Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.authorities NOT IN (?2) and u.status!=?3 AND (u.name LIKE %?4% OR u.employeeId LIKE %?4%)")
	public List<User> getAllUserNotAdminAndHrByName(Long businessUnitId, Set<String> authorities, int status, String name);

	
	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.authorities=?2 and u.status!=?3 AND (u.name LIKE %?4% OR u.employeeId LIKE %?4%)")
	public List<User> getAllUserNotAdminAndHrByName(Long businessUnitId, String authorities, int status, String name);

	
	@Query("SELECT fs FROM FormSaved fs WHERE fs.plantCode  IN (?1) AND fs.businessUnitId=?2 AND fs.categoryId=?3 AND fs.subCategoryId=?4 AND fs.status!=?5")
	public List<FormSaved> getAllFormSavedByPlantCodesAndBusinessIdAndCategoryAndSubcategoryNotStatus(Set<String> plantCode, Long businessUnitId,Long categoryId,Long subCategoryId,int status);

	
	@Query("SELECT fs FROM FormSaved fs WHERE fs.plantCode  IN (?1) AND fs.businessUnitId=?2 AND fs.categoryId=?3 AND fs.subCategoryId=?4 AND fs.status=?5")
	public List<FormSaved> getAllFormSavedByPlantCodesAndBusinessIdAndCategoryAndSubcategoryAndStatus(Set<String> plantCode, Long businessUnitId,Long categoryId,Long subCategoryId,int status);
	
}
