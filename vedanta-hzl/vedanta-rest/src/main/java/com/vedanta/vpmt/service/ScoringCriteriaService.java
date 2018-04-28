package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.ScoringCriteria;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
public interface ScoringCriteriaService extends VedantaService<ScoringCriteria> {
	public Boolean saveScoringCriteriaList(List<ScoringCriteria> entity);

	public List<ScoringCriteria> getAllScoringCriteriaByFieldId(long fieldId);

	public List<ScoringCriteria> getAllScoringCriteriaByFieldIdAndSubCategoryId(long fieldId, long subCategoryId);

	public List<ScoringCriteria> getAllScoringCriteria();

	public Boolean delete(List<ScoringCriteria> entity);
}
