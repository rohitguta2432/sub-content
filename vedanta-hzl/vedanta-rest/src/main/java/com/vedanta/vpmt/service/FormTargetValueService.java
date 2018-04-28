package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormTargetValue;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
public interface FormTargetValueService extends VedantaService<FormTargetValue> {
	public Boolean saveFormTargetValueList(List<FormTargetValue> entity);

	public Boolean delete(FormTargetValue entity);

	public Boolean delete(List<FormTargetValue> entity);

	public List<FormTargetValue> getAllFormTargetValues();

	public FormTargetValue getFormTargetValuesForSubCategory(long fieldId, long subCategoryId);

	public List<FormTargetValue> getAllFormTargetValuesByFieldId(long fieldId);

	public List<FormTargetValue> getAllDistinctFormTargetValues();
}
