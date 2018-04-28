package com.vedanta.vpmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.ScoreCard;

@Repository
public interface ComplianceDao extends JpaRepository<ScoreCard, Long> {

	@Query(value = "SELECT * FROM scorecard where business_unit_id= ?1 group by contract_number,month_id,year_id", nativeQuery = true)
	public List<ScoreCard> getPopulatedScorecard(Long businessUnitId);

	@Query("SELECT fgu FROM FormGroupUser fgu WHERE fgu.contractNumber=?1 AND fgu.businessUnitId=?2 AND fgu.categoryId=?3 AND fgu.subCategoryId=?4")
	public FormGroupUser getFormGroupUser(String contractNumber, Long businessUnitId, Long categoryId,
			Long subCategoryId);

	@Query("SELECT fs FROM FormSaved fs WHERE fs.contractNumber=?1 AND fs.businessUnitId=?2 AND fs.monthId=?3 AND fs.yearId=?4 AND fs.categoryId=?5 AND fs.subCategoryId=?6")
	public FormSaved getFormSaved(String contractNumber, Long businessUnitId, int monthId, int yearId, Long categoryId,
			Long subCategoryId);

}
