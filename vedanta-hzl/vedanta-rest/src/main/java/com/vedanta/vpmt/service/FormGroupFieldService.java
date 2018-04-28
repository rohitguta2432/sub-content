package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormGroupField;

import java.util.List;

public interface FormGroupFieldService extends VedantaService<FormGroupField> {

	public boolean saveFormGroupFieldList(Long groupId, List<FormGroupField> entity);

	public List<FormGroupField> getAllFormGroupFields();

	public boolean deleteByFormGroup(long formGroupId);

	public List<FormGroupField> getAllFormGroupFieldsByGroupId(long groupId);
}
