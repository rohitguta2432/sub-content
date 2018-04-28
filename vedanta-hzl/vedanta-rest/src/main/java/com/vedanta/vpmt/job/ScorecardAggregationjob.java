package com.vedanta.vpmt.job;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.model.ScorecardAggregation;
import com.vedanta.vpmt.model.Vendor;
import com.vedanta.vpmt.service.ScorecardAggregationService;
import com.vedanta.vpmt.service.VendorService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j

public class ScorecardAggregationjob implements Job {

	@Value("${cron.expression.scorecard.aggregation}")
	private String cronExpression;

	@Autowired
	private ScorecardAggregationService ScorecardAggregationDetails;
	
	@Autowired
	private VendorService vendorService;


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		log.info("Scorecard-Aggregation scheduler Started");

		List<Object[]> ScorecardDetails = ScorecardAggregationDetails.getAllUniqueContractMangerAndMonthIdAndYearId();

		if (ScorecardDetails != null) {

			for (Object[] s : ScorecardDetails) {

				ScorecardAggregation scorecardAggreagtionDetails = new ScorecardAggregation();

				BigInteger cId = (BigInteger) s[0];
				Long contractId = cId.longValue();

				String contractNumber = String.valueOf(s[1]);

				BigInteger vId = (BigInteger) s[2];
				Long vendorId = vId.longValue();

				String vendorCode = String.valueOf(s[3]);

				BigInteger catId = (BigInteger) s[4];
				Long categoryId = catId.longValue();

				String categoryName = String.valueOf(s[5]);

				BigInteger SubcatId = (BigInteger) s[6];
				Long subCategoryId = SubcatId.longValue();

				String subCategoryName = String.valueOf(s[7]);

				BigInteger tId = (BigInteger) s[8];
				Long templateId = tId.longValue();

				String templateName = String.valueOf(s[9]);

				BigInteger pId = (BigInteger) s[10];
				Long plantId = pId.longValue();

				String plantCode = String.valueOf(s[11]);

				int weight = (Integer) s[14];

				BigDecimal ts = (BigDecimal) s[15];
				double totalScore = ts.doubleValue();

				int targetScore = (Integer) s[16];

				BigDecimal as = (BigDecimal) s[17];
				double actualScore = as.doubleValue();

				int status = (Integer) s[18];
				int monthId = (Integer) s[19];
				int yearId = (Integer) s[20];
				Date dueDate = (Date) s[21];

				BigInteger buId = (BigInteger) s[22];
				Long bussinessUnitId = buId.longValue();

				BigDecimal avgActScore = (BigDecimal) s[23];
				double averageActualScore = avgActScore.doubleValue();
				double resultAverageActualScore = (int) (averageActualScore * 100) / 100d;

				BigInteger poI = (BigInteger) s[24];
				Long countPoItem = poI.longValue();

				BigDecimal sumActScore = (BigDecimal) s[25];
				double sumActualScore = sumActScore.doubleValue();
				double resultSumActualScore = (int) (sumActualScore * 100) / 100d;
				Vendor vendor=vendorService.get(vendorId);

				ScorecardAggregation flag = ScorecardAggregationDetails.checkExitingData(contractNumber, monthId,
						yearId);

				if (ObjectUtils.isEmpty(flag)) {

					scorecardAggreagtionDetails.setContractId(contractId);
					scorecardAggreagtionDetails.setContractNumber(contractNumber);
					scorecardAggreagtionDetails.setVendorId(vendorId);
					scorecardAggreagtionDetails.setVendorCode(vendorCode);
					scorecardAggreagtionDetails.setCategoryId(categoryId);
					scorecardAggreagtionDetails.setCategoryName(categoryName);
					scorecardAggreagtionDetails.setSubCategoryId(subCategoryId);
					scorecardAggreagtionDetails.setSubCategoryName(subCategoryName);
					scorecardAggreagtionDetails.setTemplateId(templateId);
					scorecardAggreagtionDetails.setTemplateName(templateName);
					scorecardAggreagtionDetails.setPlantId(plantId);
					scorecardAggreagtionDetails.setPlantCode(plantCode);
					scorecardAggreagtionDetails.setWeight(weight);
					scorecardAggreagtionDetails.setTotalScore(totalScore);
					scorecardAggreagtionDetails.setTargetScore(targetScore);
					scorecardAggreagtionDetails.setActualScore(actualScore);
					scorecardAggreagtionDetails.setStatus(status);
					scorecardAggreagtionDetails.setMonthId(monthId);
					scorecardAggreagtionDetails.setYearId(yearId);
					scorecardAggreagtionDetails.setDueDate(dueDate);
					scorecardAggreagtionDetails.setBusinessUnitId(bussinessUnitId);
					if(!ObjectUtils.isEmpty(vendor)) {
						scorecardAggreagtionDetails.setVendorName(vendor.getName());	
					}


					scorecardAggreagtionDetails.setAvgActualScore(resultAverageActualScore);
					scorecardAggreagtionDetails.setSumActualScore(resultSumActualScore);

					ScorecardAggregationDetails.save(scorecardAggreagtionDetails);

				} else {
					scorecardAggreagtionDetails.setId(flag.getId());
					scorecardAggreagtionDetails.setContractId(contractId);
					scorecardAggreagtionDetails.setContractNumber(contractNumber);
					scorecardAggreagtionDetails.setVendorId(vendorId);
					scorecardAggreagtionDetails.setVendorCode(vendorCode);
					scorecardAggreagtionDetails.setCategoryId(categoryId);
					scorecardAggreagtionDetails.setCategoryName(categoryName);
					scorecardAggreagtionDetails.setSubCategoryId(subCategoryId);
					scorecardAggreagtionDetails.setSubCategoryName(subCategoryName);
					scorecardAggreagtionDetails.setTemplateId(templateId);
					scorecardAggreagtionDetails.setTemplateName(templateName);
					scorecardAggreagtionDetails.setPlantId(plantId);
					scorecardAggreagtionDetails.setPlantCode(plantCode);
					scorecardAggreagtionDetails.setWeight(weight);
					scorecardAggreagtionDetails.setTotalScore(totalScore);
					scorecardAggreagtionDetails.setTargetScore(targetScore);
					scorecardAggreagtionDetails.setActualScore(actualScore);
					scorecardAggreagtionDetails.setStatus(status);
					scorecardAggreagtionDetails.setMonthId(monthId);
					scorecardAggreagtionDetails.setYearId(yearId);
					scorecardAggreagtionDetails.setDueDate(dueDate);
					scorecardAggreagtionDetails.setBusinessUnitId(bussinessUnitId);
					
					if(!ObjectUtils.isEmpty(vendor)) {
						scorecardAggreagtionDetails.setVendorName(vendor.getName());	
					}


					scorecardAggreagtionDetails.setAvgActualScore(resultAverageActualScore);
					scorecardAggreagtionDetails.setSumActualScore(resultSumActualScore);

					ScorecardAggregationDetails.updateScorecardAggregation(scorecardAggreagtionDetails);

				}
			}
		}
	}

	@Bean(name = "scorecardAggregationJobBean")
	public JobDetailFactoryBean scorecardAggregationJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "scorecardAggregationJobBeanTrigger")
	public CronTriggerFactoryBean scorecardAggregationJobBeanTrigger(
			@Qualifier("scorecardAggregationJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}
}
