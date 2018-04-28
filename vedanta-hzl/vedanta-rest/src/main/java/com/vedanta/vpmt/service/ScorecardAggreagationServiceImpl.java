package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vedanta.vpmt.dao.ScorecardAggregationDao;
import com.vedanta.vpmt.model.ScorecardAggregation;

@Service("ScorecardAggregationService")
public class ScorecardAggreagationServiceImpl implements ScorecardAggregationService {

	@Autowired
	private ScorecardAggregationDao scorecardAggregationDao;

	@Override
	public List<Object[]> getAllUniqueContractMangerAndMonthIdAndYearId() {
		return scorecardAggregationDao.getAllScoreCardByuniqueContractmangerAndYearAndMonthId();
	}

	@Override
	public ScorecardAggregation saveScorecardAggregation(ScorecardAggregation entity) {
		return scorecardAggregationDao.save(entity);
	}

	@Override
	public List<ScorecardAggregation> getAllScoreCardAggregationByuniqueContractmangerAndYearAndMonthId() {
		return scorecardAggregationDao.getAllScoreCardAggregationByuniqueContractmangerAndYearAndMonthId();
	}

	@Override
	public ScorecardAggregation checkExitingData(String contractNumber, int monthId, int yearId) {
		return scorecardAggregationDao.checkScorecardDataExitsOrNot(contractNumber, monthId, yearId);
	}

	@Override
	public ScorecardAggregation updateScorecardAggregation(ScorecardAggregation entity) {
		return scorecardAggregationDao.save(entity);
	}

	@Override
	public ScorecardAggregation get(long id) {
		return null;
	}

	@Override
	public ScorecardAggregation save(ScorecardAggregation entity) {
		return scorecardAggregationDao.save(entity);
	}

	@Override
	public ScorecardAggregation update(long id, ScorecardAggregation entity) {
		return null;
	}

}
