package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by manishsanger on 24/09/17.
 */
public interface FormSaveService extends VedantaService<FormSaved> {

	public Map<String, Object> getFormSavedFormMapByFormSavedId(Long formSaveId);

	public List<FormSaved> getFormSavedByContractNumberAndStatus(String contractNumber, int status);

	public List<FormSaved> getUserFormSavedByStatus(int status);

	public FormGroupUser saveFormGroupUser(FormGroupUser formGroupUser);

	public List<FormSaved> getAllFormSaved();

	public FormSaved populate(FormSaved entity);

	public List<FormSaved> getComplianceFormByMonthAndStatus(String plantCode, String contractNumber, Long formId,
			int monthId, int yearId);

	FormSaved getFormSaveById(long id);

	List<FormSaved> getAllFormSavedByFormIdAndMonthAndYearAndStatus(Set<Long> formIds, int monthId, int yearId,
			int status, Long businessUnitId);

	Map<String, Object> getComplianceFormSavedFormMapByFormSavedId(Long formSavedId, Long scoreCardId);

	List<FormSaved> getFormSavedByBuIdAndPlantCodeAndStatus(Long bussinessUnitId, String plantCode, int status);

	public FormSaved getFormSavedByBusinessUnitIdCategoryIdAndSubCategoryIdAndMonthIdAndYearId(String contractNumber,
			Long businessUnitId, int monthId, int yearId, Long categoryId, Long subCategoryId);
	
	List<User> getAllUserNotAdminAndHrByLimit(Long buId);
	
	List<User> getAllHrUserByLimit(Long buId);
	
	List<User> getAllUserNotAdminAndHrByName(String name, Long buId);
	
	List<User> getAllHrUserByName(String name, Long buId);

}
