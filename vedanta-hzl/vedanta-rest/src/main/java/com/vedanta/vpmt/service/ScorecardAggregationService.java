package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.ScorecardAggregation;

public interface ScorecardAggregationService extends VedantaService<ScorecardAggregation> {

	List<Object[]> getAllUniqueContractMangerAndMonthIdAndYearId();

	List<ScorecardAggregation> getAllScoreCardAggregationByuniqueContractmangerAndYearAndMonthId();

	public ScorecardAggregation saveScorecardAggregation(ScorecardAggregation entity);

	public ScorecardAggregation checkExitingData(String contractNumber, int monthId, int yearId);

	public ScorecardAggregation updateScorecardAggregation(ScorecardAggregation entity);
}
