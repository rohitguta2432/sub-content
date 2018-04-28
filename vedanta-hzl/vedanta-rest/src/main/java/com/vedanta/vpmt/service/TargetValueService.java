package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.TargetValue;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
public interface TargetValueService extends VedantaService<TargetValue> {
	public Boolean saveTargetValueList(List<TargetValue> entity);

	public Boolean delete(TargetValue entity);

	public Boolean delete(List<TargetValue> entity);

	public List<TargetValue> getAllTargetValues();

	public TargetValue getTargetValuesForSubCategory(long fieldId, long subCategoryId);

	public List<TargetValue> getAllTargetValuesByFieldId(long fieldId);

	public List<TargetValue> getAllDistinctTargetValues();
}
