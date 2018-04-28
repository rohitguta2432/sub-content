package com.vedanta.vpmt.service;

import java.util.Map;

import com.vedanta.vpmt.model.ScorecardAggregation;

public interface DashboardService extends VedantaService<ScorecardAggregation> {

	Map<String, Object> getDashboardDataByBuId(Long bussinessId);

	Map<String, Object> getDashboardDataBySearchkeyBuId(Long bussinessId, Long plantId, Long categoryId,
			Long subCategoryId, Long contractId, String fromdate, String toDate);

}
