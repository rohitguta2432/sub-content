package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.mapper.DashboardDataMapper;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScorecardAggregation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by manishsanger on 11/09/17.
 */
@Repository
public interface ScoreCardDao extends JpaRepository<ScoreCard, Long> {

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.templateId!=?1 AND sc.monthId=?2 AND sc.yearId=?3 AND sc.status=?4 AND sc.businessUnitId=?5")
	public List<ScoreCard> getAllScoreCardByTemplateIdAndMonthAndYearAndStatus(Long templateId, int monthId, int yearId,
			int status, Long businessId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode IN (?2) AND sc.status=?3")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndStatus(Long businessId, Set<String> plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status=?3")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndStatus(Long businessId, String plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode  IN (?2) AND sc.status!=?3")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndNotStatus(Long businessId, Set<String> plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status!=?3")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndNotStatus(Long businessId, String plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.plantCode IN (?1) AND sc.status=?2")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndStatus(Set<String> plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.plantCode=?1 AND sc.status=?2")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndStatus(String plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.plantCode  IN (?1) AND sc.status!=?2")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndNotStatus(Set<String> plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.plantCode=?1 AND sc.status!=?2")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndNotStatus(String plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.plantCode  IN (?1) AND sc.status!=?2 AND (sc.monthId BETWEEN ?3 AND ?4) AND (sc.yearId  BETWEEN ?5 AND ?6)")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndNotStatusAndDateRange(Set<String> plantCode, int status,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.plantCode  IN (?1) AND sc.status=?2 AND (sc.monthId BETWEEN ?3 AND ?4) AND (sc.yearId  BETWEEN ?5 AND ?6)")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndStatusAndDateRange(Set<String> plantCode, int status,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode  IN (?2) AND sc.status!=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndNotStatusAndDateRange(Long businessId, Set<String> plantCode,
			int status, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status!=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndNotStatusAndDateRange(Long businessId, String plantCode,
			int status, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode IN (?2) AND sc.status=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndStatusAndDateRange(Long businessId, Set<String> plantCode,
			int status, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getAllScoreCardByPlantCodeAndStatusAndDateRange(Long businessId, String plantCode,
			int status, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status=?1")
	public List<ScoreCard> getAllScoreCard(int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status=?1 AND sc.businessUnitId=?2 AND sc.plantCode=?3")
	public List<ScoreCard> getAllScoreCardByBusinessUnitIdAndPlantCode(int status, Long businessId, String plantCode);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status=?1 AND sc.businessUnitId=?2 AND sc.plantCode IN ?3")
	public List<ScoreCard> getAllScoreCardForPlantHeadByBusinessUnitIdAndPlantCode(int status, Long businessId,
			Set<String> plantCode);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status=?1 AND sc.businessUnitId=?2")
	public List<ScoreCard> getAllScoreCardByBusinessUnitId(int status, Long businessId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status!=?1")
	public List<ScoreCard> getAllScoreCardNotStatus(int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status!=?1 AND sc.businessUnitId=?2 AND sc.plantCode=?3")
	public List<ScoreCard> getAllScoreCardNotStatusAndBusinessUnitIdAndPlantCode(int status, Long businessId,
			String plantCode);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status!=?1 AND sc.businessUnitId=?2 AND sc.plantCode IN ?3")
	public List<ScoreCard> getAllScoreCardForPlantHeadNotStatusAndBusinessUnitIdAndPlantCode(int status,
			Long businessId, Set<String> plantCode);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status!=?1 AND sc.businessUnitId=?2")
	public List<ScoreCard> getAllScoreCardNotStatusAndBusinessUnitId(int status, Long businessId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.status=?2")
	public List<ScoreCard> getAllScoreCard(Long businessId, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.status!=?2")
	public List<ScoreCard> getAllScoreCardNotStatus(Long businessId, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status!=?3")
	public List<ScoreCard> getAllScoreCardNotStatus(Long businessId, String plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status=?3")
	public List<ScoreCard> getAllScoreCardStatus(Long businessId, String plantCode, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status!=?1 AND sc.monthId=?2 AND sc.yearId=?3")
	public List<ScoreCard> getAllScoreCardByNotStatusAndMonthAndYear(int status, int monthId, int yearId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status=?1 AND sc.monthId=?2 AND sc.yearId=?3")
	public List<ScoreCard> getAllScoreCardByStatusAndMonthAndYear(int status, int monthId, int yearId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractId<>?1")
	public List<ScoreCard> getScoreCardByContractId(long contractId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber<>?1 AND sc.contractManagerId=?2 AND sc.monthId=?3 AND sc.yearId=?4")
	public ScoreCard getScoreCardByContractNumberAndManagerAndMonthAndYear(String contractNumber, long contractManager,
			int monthId, int yearId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber<>?1 AND sc.monthId=?2 AND sc.yearId=?3")
	public List<ScoreCard> getScoreCardByContractNumberAndMonthAndYear(String contractNumber, int monthId, int yearId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.vendorCode<>?1")
	public List<ScoreCard> getScoreCardByVendorCode(String vendorCode);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.vendorId<>?1")
	public List<ScoreCard> getScoreCardByVendorId(long vendorId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.vendorCode<>?1 AND sc.monthId=?2 AND sc.yearId=?3")
	public List<ScoreCard> getScoreCardByVendorCodeAndMonthAndYear(String vendorCode, int monthId, int yearId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber=?1")
	public List<ScoreCard> getScoreCardByContractNumber(String contractNumber);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber<>?1 AND sc.status=?2")
	public List<ScoreCard> getContractScoreCardsByStatus(String contractNumber, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.poItem IN (?3) AND sc.status=?4")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbers(Set<String> contractNumbers,
			Set<String> contractManagerIds, Set<String> poItems, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.poItem IN (?3) AND sc.status!=?4")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatus(Set<String> contractNumbers,
			Set<String> contractManagerIds, Set<String> poItems, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.poItem IN (?3) AND sc.status=?4 AND (sc.monthId BETWEEN ?5 AND ?6) AND (sc.yearId  BETWEEN ?7 AND ?8)")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersAndDateRange(Set<String> contractNumbers,
			Set<String> contractManagerIds, Set<String> poItems, int status, int fmonth, int tmonth, int fyear,
			int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.poItem IN (?3) AND sc.status!=?4 AND (sc.monthId BETWEEN ?5 AND ?6) AND (sc.yearId  BETWEEN ?7 AND ?8)")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(Set<String> contractNumbers,
			Set<String> contractManagerIds, Set<String> poItems, int status, int fmonth, int tmonth, int fyear,
			int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractNumber IN (?2) AND sc.contractManagerId IN (?3) AND sc.poItem IN (?4) AND sc.status=?5")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbers(Long businessUnitId, Set<String> contractNumbers,
			Set<String> contractManagerIds, Set<String> poItems, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractNumber IN (?2) AND sc.contractManagerId IN (?3) AND sc.poItem IN (?4) AND sc.status!=?5")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatus(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, Set<String> poItems, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractNumber IN (?2) AND sc.contractManagerId IN (?3) AND sc.poItem IN (?4) AND sc.status=?5 AND (sc.monthId BETWEEN ?6 AND ?7) AND (sc.yearId  BETWEEN ?8 AND ?9)")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersAndDateRange(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, Set<String> poItems, int status, int fmonth,
			int tmonth, int fyear, int tyear);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where business_unit_id=?1 and  contract_number IN (?2) and contract_manager_id IN (?3) and po_item IN (?4) and status=?5) scorecard where d between CAST(?6 as date) and cast(?7 as date)", nativeQuery = true)
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersAndDateRange(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, Set<String> poItems, int status, String fdate,
			String tdate);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractNumber IN (?2) AND sc.contractManagerId IN (?3) AND sc.poItem IN (?4) AND sc.status!=?5 AND (sc.monthId BETWEEN ?6 AND ?7) AND (sc.yearId  BETWEEN ?8 AND ?9)")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, Set<String> poItems, int status, int fmonth,
			int tmonth, int fyear, int tyear);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where business_unit_id=?1 and  contract_number IN (?2) and contract_manager_id IN (?3) and po_item IN (?4) and status!=?5) scorecard where d between CAST(?6 as date) and cast(?7 as date)", nativeQuery = true)
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, Set<String> poItems, int status, String fdate,
			String tdate);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.status!=?3 AND sc.monthId=?2 AND sc.yearId=?3")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatus(Set<String> contractNumbers,
			Set<String> contractManagerIds, int status, int monthId, int yearId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.status=?3 AND sc.monthId=?2 AND sc.yearId=?3")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersStatus(Set<String> contractNumbers,
			Set<String> contractManagerIds, int status, int monthId, int yearId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status=?1 AND (sc.monthId BETWEEN ?2 AND ?3) AND (sc.yearId  BETWEEN ?4 AND ?5)")
	public List<ScoreCard> getAllScoreCardByStatusAndDateRange(int status, int fmonth, int tmonth, int fyear,
			int tyear);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where status=?1 and business_unit_id=?4 and plant_code=?5) scorecard where d between CAST(?2 as date) and cast(?3 as date)", nativeQuery = true)
	public List<ScoreCard> getAllScoreCardByStatusAndDateRangeAndBusinessUnitIdAndPlantCode(int status, String fdate,
			String tdate, Long businessUnitId, String plantCode);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where status=?1 and business_unit_id=?4 and plant_code IN (?5)) scorecard where d between CAST(?2 as date) and cast(?3 as date)", nativeQuery = true)
	public List<ScoreCard> getAllScoreCardForPlantHeadByStatusAndDateRangeAndBusinessUnitIdAndPlantCode(int status,
			String fdate, String tdate, Long businessUnitId, Set<String> plantCode);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where status=?1 and business_unit_id=?4) scorecard where d between CAST(?2 as date) and cast(?3 as date)", nativeQuery = true)
	public List<ScoreCard> getAllScoreCardByStatusAndDateRangeAndBusinessUnitId(int status, String fdate, String tdate,
			Long businessUnitId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status!=?1 AND (sc.monthId BETWEEN ?2 AND ?3) AND (sc.yearId  BETWEEN ?4 AND ?5)")
	public List<ScoreCard> getAllScoreCardByNotStatusAndDateRange(int status, int fmonth, int tmonth, int fyear,
			int tyear);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where status!=?1 and business_unit_id=?4 and plant_code=?5) scorecard where d between CAST(?2 as date) and cast(?3 as date)", nativeQuery = true)
	public List<ScoreCard> getAllScoreCardByNotStatusAndDateRangeAndBusinessUnitIdAndPlantCode(int status, String fdate,
			String tdate, Long businessUnitId, String plantCode);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where status!=?1 and business_unit_id=?4 and plant_code IN (?5)) scorecard where d between CAST(?2 as date) and cast(?3 as date)", nativeQuery = true)
	public List<ScoreCard> getAllScoreCardForPlantHeadByNotStatusAndDateRangeAndBusinessUnitIdAndPlantCode(int status,
			String fdate, String tdate, Long businessUnitId, Set<String> plantCode);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where status!=?1 and business_unit_id=?4) scorecard where d between CAST(?2 as date) and cast(?3 as date)", nativeQuery = true)
	public List<ScoreCard> getAllScoreCardByNotStatusAndDateRangeAndBusinessUnitId(int status, String fdate,
			String tdate, Long businessUnitId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.status=?2 AND (sc.monthId BETWEEN ?3 AND ?4) AND (sc.yearId  BETWEEN ?5 AND ?6)")
	public List<ScoreCard> getAllScoreCardByStatusAndDateRange(Long businessUnitId, int status, int fmonth, int tmonth,
			int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getAllScoreCardByStatusAndDateRange(Long businessUnitId, String plantCode, int status,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.status!=?2 AND (sc.monthId BETWEEN ?3 AND ?4) AND (sc.yearId  BETWEEN ?5 AND ?6)")
	public List<ScoreCard> getAllScoreCardByNotStatusAndDateRange(Long businessUnitId, int status, int fmonth,
			int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.plantCode=?2 AND sc.status!=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getAllScoreCardByNotStatusAndDateRange(Long businessUnitId, String plantCode, int status,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.status=?3 AND sc.createdOn BETWEEN ?4 AND ?5")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersStatusAndDateRange(Set<String> contractNumbers,
			Set<String> contractManagerIds, int status, Date from, Date to);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractNumber IN (?1) AND sc.contractManagerId IN (?2) AND sc.status!=?3 AND sc.createdOn BETWEEN ?4 AND ?5")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(Set<String> contractNumbers,
			Set<String> contractManagerIds, int status, Date from, Date to);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractNumber IN (?2) AND sc.contractManagerId IN (?3) AND sc.status=?4 AND sc.createdOn BETWEEN ?5 AND ?6")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersStatusAndDateRange(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, int status, Date from, Date to);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractNumber IN (?2) AND sc.contractManagerId IN (?3) AND sc.status!=?4 AND sc.createdOn BETWEEN ?5 AND ?6")
	public List<ScoreCard> getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(Long businessUnitId,
			Set<String> contractNumbers, Set<String> contractManagerIds, int status, Date from, Date to);

	@Query("SELECT new com.vedanta.vpmt.mapper.DashboardDataMapper(sc.contractId, sc.contractNumber, sc.vendorId, sc.vendorCode, sc.categoryId, sc.categoryName, sc.subCategoryId, sc.subCategoryName, sc.templateId, sc.templateName, sc.plantId, sc.plantCode, sc.departmentId, sc.departmentCode, sc.weight, sc.totalScore, sc.targetScore, sc.actualScore, sc.status, sc.monthId, sc.yearId, sc.dueDate, AVG(sc.actualScore), COUNT(sc.poItem), SUM(sc.actualScore), SUM(sc.totalScore), SUM(sc.actualScore * sc.weight /100))  FROM ScoreCard sc GROUP BY sc.plantCode, sc.categoryName, sc.subCategoryName, sc.contractNumber, sc.yearId, sc.monthId")
	public List<DashboardDataMapper> getDashboardData();

	@Query("SELECT new com.vedanta.vpmt.mapper.DashboardDataMapper(sc.contractId, sc.contractNumber, sc.vendorId, sc.vendorCode, sc.categoryId, sc.categoryName, sc.subCategoryId, sc.subCategoryName, sc.templateId, sc.templateName, sc.plantId, sc.plantCode, sc.departmentId, sc.departmentCode, sc.weight, sc.totalScore, sc.targetScore, sc.actualScore, sc.status, sc.monthId, sc.yearId, sc.dueDate, AVG(sc.actualScore), COUNT(sc.poItem), SUM(sc.actualScore), SUM(sc.totalScore), SUM(sc.actualScore * sc.weight /100))  FROM ScoreCard sc WHERE sc.businessUnitId=?1 GROUP BY sc.plantCode, sc.categoryName, sc.subCategoryName, sc.contractNumber, sc.yearId, sc.monthId")
	public List<DashboardDataMapper> getDashboardData(Long businessUnitId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractManagerId =?2 AND sc.status=?3")
	public List<ScoreCard> getScoreCardsByManagerAndStatus(Long businessId, String contractManagerId, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractManagerId =?2 AND sc.status!=?3")
	public List<ScoreCard> getScoreCardsByManagerAndNotStatus(Long businessId, String contractManagerId, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractManagerId =?2 AND sc.status=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getScoreCardsByManagerAndStatusAndDateRange(Long businessId, String contractManagerId,
			int status, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.contractManagerId =?2 AND sc.status!=?3 AND (sc.monthId BETWEEN ?4 AND ?5) AND (sc.yearId  BETWEEN ?6 AND ?7)")
	public List<ScoreCard> getScoreCardsByManagerAndNotStatusAndDateRange(Long businessId, String contractManagerId,
			int status, int fmonth, int tmonth, int fyear, int tyear);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where business_unit_id=?1 and contract_manager_id =?2 and status!=?3) scorecard where d between CAST(?4 as date) and cast(?5 as date)", nativeQuery = true)
	public List<ScoreCard> getScoreCardsByManagerAndNotStatusAndDateRange(Long businessId, String contractManagerId,
			int status, String fdate, String tdate);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where business_unit_id=?1 and contract_manager_id =?2 and status=?3) scorecard where d between CAST(?4 as date) and cast(?5 as date)", nativeQuery = true)
	public List<ScoreCard> getScoreCardsByManagerAndStatusAndDateRange(Long businessId, String contractManagerId,
			int status, String fdate, String tdate);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractManagerId =?1 AND sc.status=?2")
	public List<ScoreCard> getScoreCardsByManagerAndStatus(String contractManagerId, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractManagerId =?1 AND sc.status!=?2")
	public List<ScoreCard> getScoreCardsByManagerAndNotStatus(String contractManagerId, int status);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractManagerId =?1 AND sc.status=?2 AND (sc.monthId BETWEEN ?3 AND ?4) AND (sc.yearId  BETWEEN ?5 AND ?6)")
	public List<ScoreCard> getScoreCardsByManagerAndStatusAndDateRange(String contractManagerId, int status, int fmonth,
			int tmonth, int fyear, int tyear);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.contractManagerId =?1 AND sc.status!=?2 AND (sc.monthId BETWEEN ?3 AND ?4) AND (sc.yearId  BETWEEN ?5 AND ?6)")
	public List<ScoreCard> getScoreCardsByManagerAndNotStatusAndDateRange(String contractManagerId, int status,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query(value = "select * from (select *, concat(year_id,'-',month_id,'-','01') as d from scorecard where contract_manager_id =?1 and status!=?2) scorecard where d between CAST(?2 as date) and cast(?3 as date)", nativeQuery = true)
	public List<ScoreCard> getScoreCardsByManagerAndNotStatusAndDateRange(String contractManagerId, int status,
			String fdate, String tdate);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1")
	public List<ScorecardAggregation> getDashboardDataByPlantId(Long plantId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2")
	public List<ScorecardAggregation> getDashboardDataByPlantId(Long businessUnitId, Long plantId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.categoryId=?1")
	public List<ScorecardAggregation> getDashboardDataByCategoryId(Long categoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.categoryId=?2")
	public List<ScorecardAggregation> getDashboardDataByCategoryId(Long businessUnitId, Long categoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.subCategoryId=?1")
	public List<ScorecardAggregation> getDashboardDataBySubCategoryId(Long subCategoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.subCategoryId=?2")
	public List<ScorecardAggregation> getDashboardDataBySubCategoryId(Long businessUnitId, Long subCategoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.contractId=?1")
	public List<ScorecardAggregation> getDashboardDataByContractId(Long contractId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.contractId=?2")
	public List<ScorecardAggregation> getDashboardDataByContractId(Long businessUnitId, Long contractId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1 AND sa.categoryId=?2")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryId(Long plantId, Long categoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryId(Long businessUnitId, Long plantId,
			Long categoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1 AND sa.categoryId=?2 AND sa.subCategoryId=?3")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryId(Long plantId,
			Long categoryId, Long subCategoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND sa.subCategoryId=?4")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryId(Long businessUnitId,
			Long plantId, Long categoryId, Long subCategoryId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1 AND sa.categoryId=?2 AND sa.subCategoryId=?3 AND sa.contractId=?4")
	public List<ScorecardAggregation> getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractId(Long plantId,
			Long categoryId, Long subCategoryId, Long contractId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND sa.subCategoryId=?4 AND sa.contractId=?5")
	public List<ScorecardAggregation> getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractId(
			Long businessUnitId, Long plantId, Long categoryId, Long subCategoryId, Long contractId);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1 AND (sa.monthId BETWEEN ?2 AND ?3) AND (sa.yearId  BETWEEN ?4 AND ?5)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdByDateRange(Long plantId, int fmonth, int tmonth,
			int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND (sa.monthId BETWEEN ?3 AND ?4) AND (sa.yearId  BETWEEN ?5 AND ?6)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdByDateRange(Long businessUnitId, Long plantId,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.categoryId=?1 AND (sa.monthId BETWEEN ?2 AND ?3) AND (sa.yearId  BETWEEN ?4 AND ?5)")
	public List<ScorecardAggregation> getDashboardDataByCategoryIdByDateRange(Long categoryId, int fmonth, int tmonth,
			int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.categoryId=?2 AND (sa.monthId BETWEEN ?3 AND ?4) AND (sa.yearId  BETWEEN ?5 AND ?6)")
	public List<ScorecardAggregation> getDashboardDataByCategoryIdByDateRange(Long businessUnitId, Long categoryId,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.subCategoryId=?1  AND (sa.monthId BETWEEN ?2 AND ?3) AND (sa.yearId  BETWEEN ?4 AND ?5)")
	public List<ScorecardAggregation> getDashboardDataBySubCategoryIdByDateRange(Long subCategoryId, int fmonth,
			int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.subCategoryId=?2  AND (sa.monthId BETWEEN ?3 AND ?4) AND (sa.yearId  BETWEEN ?5 AND ?6)")
	public List<ScorecardAggregation> getDashboardDataBySubCategoryIdByDateRange(Long businessUnitId,
			Long subCategoryId, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.contractId=?1 AND (sa.monthId BETWEEN ?2 AND ?3) AND (sa.yearId  BETWEEN ?4 AND ?5)")
	public List<ScorecardAggregation> getDashboardDataByContractIdByDateRange(Long contractId, int fmonth, int tmonth,
			int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.contractId=?2 AND (sa.monthId BETWEEN ?3 AND ?4) AND (sa.yearId  BETWEEN ?5 AND ?6)")
	public List<ScorecardAggregation> getDashboardDataByContractIdByDateRange(Long businessUnitId, Long contractId,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1 AND sa.categoryId=?2 AND (sa.monthId BETWEEN ?3 AND ?4) AND (sa.yearId  BETWEEN ?5 AND ?6)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryIdByDateRange(Long plantId, Long categoryId,
			int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND (sa.monthId BETWEEN ?4 AND ?5) AND (sa.yearId  BETWEEN ?6 AND ?7)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryIdByDateRange(Long businessUnitId,
			Long plantId, Long categoryId, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1 AND sa.categoryId=?2 AND sa.subCategoryId=?3 AND (sa.monthId BETWEEN ?4 AND ?5) AND (sa.yearId  BETWEEN ?6 AND ?7)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryIdByDateRange(
			Long plantId, Long categoryId, Long subCategoryId, int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND sa.subCategoryId=?4 AND (sa.monthId BETWEEN ?5 AND ?6) AND (sa.yearId  BETWEEN ?7 AND ?8)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryIdByDateRange(
			Long businessUnitId, Long plantId, Long categoryId, Long subCategoryId, int fmonth, int tmonth, int fyear,
			int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.plantId=?1 AND sa.categoryId=?2 AND sa.subCategoryId=?3 AND sa.contractId=?4 AND (sa.monthId BETWEEN ?5 AND ?6) AND (sa.yearId  BETWEEN ?7 AND ?8)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractIdByDateRange(
			Long plantId, Long categoryId, Long subCategoryId, Long contractId, int fmonth, int tmonth, int fyear,
			int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND sa.plantId=?2 AND sa.categoryId=?3 AND sa.subCategoryId=?4 AND  sa.contractId=?5 AND (sa.monthId BETWEEN ?6 AND ?7) AND (sa.yearId  BETWEEN ?8 AND ?9)")
	public List<ScorecardAggregation> getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractIdByDateRange(
			Long businessUnitId, Long plantId, Long categoryId, Long subCategoryId, Long contractId, int fmonth,
			int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE (sa.monthId BETWEEN ?1 AND ?2) AND (sa.yearId  BETWEEN ?3 AND ?4)")
	public List<ScorecardAggregation> getDashboardDataByDateRange(int fmonth, int tmonth, int fyear, int tyear);

	@Query("SELECT sa FROM ScorecardAggregation sa WHERE sa.businessUnitId=?1 AND (sa.monthId BETWEEN ?2 AND ?3) AND (sa.yearId  BETWEEN ?4 AND ?5)")
	public List<ScorecardAggregation> getDashboardDataByDateRange(Long businessUnitId, int fmonth, int tmonth,
			int fyear, int tyear);

	@Query("SELECT  sa FROM ScorecardAggregation sa WHERE sa.subCategoryId=?1  GROUP BY sa.contractNumber")
	public Set<ScorecardAggregation> getScoreCardBySubCategoryIdId(Long subCategoryId);

	@Query("SELECT sc.actualScore FROM ScoreCard sc WHERE sc.id=?1")
	public double getActualScore(Long id);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.plantCode IN ?1 AND sc.businessUnitId=?2")
	public List<ScoreCard> getAllScorecardByListPlantCodeAndBusinessId(Set<String> plantCode, Long businessUnitId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1")
	public List<ScoreCard> getAllScorecardByBusinessId(Long businessUnitId);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.status=?2 AND sc.plantCode IN ?3")
	public List<ScoreCard> getAllScoreCardByBusinessUnitIdAndStatus(Long businessId, int status, Set<String> plantCode);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.monthId =?2 AND sc.yearId =?3 AND sc.contractNumber =?4")
	public ScoreCard getScorecardByContractNumberAndMonthIdAndYearId(Long businessUnitId, int monthId, int yearId,
			String contractNumber);

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.status!=?2 AND sc.plantCode IN ?3")
	public List<ScoreCard> getAllScoreCardByBusinessUnitIdAndNotStatus(Long businessId, int status,
			Set<String> plantCode);
	
	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.approverById =?2 AND sc.status!=?3")
	public List<ScoreCard> getScoreCardsByApproverByIdAndNotStatus(Long businessId, String approverById, int status);
	
	@Query("SELECT sc FROM ScoreCard sc WHERE sc.businessUnitId=?1 AND sc.status!=?2")
	public List<ScoreCard> getScoreCardsAllAndNotStatus(Long businessId,int status);

	public List<ScoreCard> getScoreCardByBusinessUnitIdAndMonthIdAndYearIdAndContractNumberAndContractManagerIdAndStatus(Long businessUnitId,
							int monthId,int yearId,String contractNumber,String contractManagerId,int status);
	
}
