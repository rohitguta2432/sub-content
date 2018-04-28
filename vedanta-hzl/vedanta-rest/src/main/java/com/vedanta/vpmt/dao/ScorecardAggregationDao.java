package com.vedanta.vpmt.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.ScorecardAggregation;

@Repository
public interface ScorecardAggregationDao extends JpaRepository<ScorecardAggregation, Long> {

	@Query(value = "select contract_id,contract_number,vendor_id,vendor_code,category_id,category_name,sub_category_id,sub_category_name,template_id,template_name,plant_id,plant_code,department_id,department_code,weight,total_score,target_score,actual_score,status,month_id,year_id,due_date,business_unit_id, AVG(actual_score), COUNT(po_item), SUM(actual_score), SUM(total_score), SUM(actual_score * weight /100) FROM scorecard where status= 7  GROUP BY contract_number,month_id,year_id", nativeQuery = true)
	public List<Object[]> getAllScoreCardByuniqueContractmangerAndYearAndMonthId();

	@Query("SELECT sa  FROM ScorecardAggregation sa GROUP BY sa.contractNumber, sa.monthId , sa.yearId")
	public List<ScorecardAggregation> getAllScoreCardAggregationByuniqueContractmangerAndYearAndMonthId();

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.contractNumber=?1 AND sa.monthId=?2 AND sa.yearId=?3")
	public ScorecardAggregation checkScorecardDataExitsOrNot(String contractNumber, int monthId, int yearId);

}
