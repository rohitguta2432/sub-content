package com.vedanta.vpmt.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.ScorecardAggregation;

public interface DashboardDao extends JpaRepository<ScorecardAggregation, Long> {

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND sa.subCategoryId=?4")
	public List<ScorecardAggregation> getDashboardDataByBuId(Long businessUnitId, Long plantId, Long categoryId,
			Long subCategoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND sa.subCategoryId=?4 AND sa.contractId=?5")
	public List<ScorecardAggregation> getDashboardDataBySearchkeyBuId(Long businessUnitId, Long plantId,
			Long categoryId, Long subCategoryId, Long contractId);

	@Query("SELECT p FROM Plant p WHERE p.plantCode=?1")
	public List<Plant> getPlantByPlantCode(String plantCode);

	@Query("SELECT p FROM Plant p WHERE p.plantCode=?1 AND p.businessUnitId=?2")
	public List<Plant> getPlantByPlantCodeAndBusinessUnit(String plantCode, Long businessUnitId);

	@Query("SELECT ph FROM PlantHead ph WHERE ph.employeeId=?1")
	public List<PlantHead> getPlantHeadByEmployeeId(String employeeId);

	@Query("SELECT ph FROM PlantHead ph WHERE ph.employeeId=?1 AND ph.businessUnitId=?2")
	public List<PlantHead> getPlantHeadByEmployeeIdAndBusinessUnit(String employeeId, Long businessUnitId);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard_aggregation where  business_unit_id=?1 and plant_id=?2 and category_id=?3 and subcategory_id=?4 and contract_id=?5) scorecard_aggregation where d between CAST(?6 as date) and cast(?7 as date)", nativeQuery = true)
	public List<ScorecardAggregation> getDashboardDataByBuIdAndSearchKey(Long businessUnitId, Long plantId,
			Long categoryId, Long subCategoryId, Long contractId, String fDate, String tdate);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND sa.subCategoryId=?4 AND sa.contractId=?5")
	public List<ScorecardAggregation> getDashboardDataByBuIdAndSearchKey(Long businessUnitId, Long plantId,
			Long categoryId, Long subCategoryId, Long contractId);

}
