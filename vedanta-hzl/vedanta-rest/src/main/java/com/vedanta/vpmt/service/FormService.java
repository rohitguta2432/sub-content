package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.Form;

import java.util.List;

public interface FormService extends VedantaService<Form> {

	public List<Form> getAllForms();

	public List<Form> getFormsBySubCategoryId(long subCategoryId);

	public List<Form> getFormsBySubCategoryIdForAdmin(long subCategoryId);

	public List<Form> getForms(int dayOfFrequency, String frequency, Long businessUnitId);
}
