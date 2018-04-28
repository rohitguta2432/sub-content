package com.vedanta.vpmt.job;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
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
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.ComplianceDao;
import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.service.FormGroupUserService;
import com.vedanta.vpmt.service.FormSaveService;
import com.vedanta.vpmt.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ComplianceJob implements Job {

	@Value("${cron.expression.scorecard.compliance}")
	private String cronExpression;

	@Autowired
	private ComplianceDao complianceDao;

	@Autowired
	private FormSaveService formsaveService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		log.info("scorecard-compliance scheduler started");

		List<ScoreCard> listScoreCard = complianceDao.getPopulatedScorecard(Constants.HZL_BUSINESS_UNIT);

		if (!CollectionUtils.isEmpty(listScoreCard)) {
					
			for(ScoreCard scoreCard:listScoreCard){
			
				FormSaved formSavedDetails = complianceDao.getFormSaved(scoreCard.getContractNumber(),
						scoreCard.getBusinessUnitId(), scoreCard.getMonthId(), scoreCard.getYearId(),
						Constants.HZL_DEFAULT_CATEGORY_ID, Constants.HZL_DEFAULT_SUB_CATEGORY_ID);

				if (ObjectUtils.isEmpty(formSavedDetails)) {
					
					FormSaved formSaved = new FormSaved();

					formSaved.setBusinessUnitId(scoreCard.getBusinessUnitId());
					formSaved.setCategoryId(Constants.HZL_DEFAULT_CATEGORY_ID);
					formSaved.setSubCategoryId(Constants.HZL_DEFAULT_SUB_CATEGORY_ID);
					formSaved.setFormId(Constants.HZL_DEFAULT_FORM_ID);
					formSaved.setContractId(scoreCard.getContractId());
					formSaved.setContractNumber(scoreCard.getContractNumber());
					formSaved.setVendorId(scoreCard.getVendorId());
					formSaved.setVendorCode(scoreCard.getVendorCode());
					formSaved.setCategoryName("others");
					formSaved.setSubCategoryName("others");
					formSaved.setFormName("Statutory compliances");
					formSaved.setPlantCode(scoreCard.getPlantCode());
					formSaved.setWeight(scoreCard.getWeight());
					formSaved.setMonthId(scoreCard.getMonthId());
					formSaved.setYearId(scoreCard.getYearId());
					formSaved.setContractManagerId(scoreCard.getContractManagerId());
					formSaved.setContractManagerName(scoreCard.getContractManagerName());
					formSaved.setBusinessUnitId(Constants.HZL_BUSINESS_UNIT);
					formSaved.setAverage(0d);
					formSaved.setDueDate(scoreCard.getDueDate());
					formSaved.setPlantId(scoreCard.getPlantId());
					formsaveService.populate(formSaved);
				}
			}
		}
	}

	@Bean(name = "scorecardComlianceJobBean")
	public JobDetailFactoryBean scorecardComplianceJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "scorecardComplianceJobBeanTrigger")
	public CronTriggerFactoryBean scorecardComplianceJobBeanTrigger(
			@Qualifier("scorecardComlianceJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}

}
