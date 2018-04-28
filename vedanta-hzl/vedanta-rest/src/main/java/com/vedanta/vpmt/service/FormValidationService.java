package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormValidation;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
public interface FormValidationService extends VedantaService<FormValidation> {
	public Boolean saveFormValidationList(List<FormValidation> entity);

	public List<FormValidation> getAllFormValidations();

	public List<FormValidation> getAllFormValidationsByFieldId(long fieldId);

	public FormValidation getFormValidationByFieldIdAndSubCategoryId(long fieldId, long subCategoryId);

	public Boolean delete(List<FormValidation> entity);

	public Boolean delete(FormValidation entity);
}
