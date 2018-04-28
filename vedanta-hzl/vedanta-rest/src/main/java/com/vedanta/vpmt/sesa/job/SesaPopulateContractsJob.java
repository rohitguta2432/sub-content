package com.vedanta.vpmt.sesa.job;

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

import com.itextpdf.text.log.SysoCounter;
import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.job.ComplianceJob;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.service.PlantService;
import com.vedanta.vpmt.service.sap.SAPClientService;
import com.vedanta.vpmt.service.sap.SesaSapClientService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SesaPopulateContractsJob implements Job {
	
/*	@Value("${sesa.cron.expression.populate.contracts}")
	private String cronExpression;
	*/
	@Autowired
	private PlantService plantService;

	@Autowired
	private SesaSapClientService sesaSapClientService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
			
			log.info("Sesa-Populate-Contract-Job");
			
			List<Plant> plants = plantService.getAllPlantByBusinessUnitId(Constants.SESA_BUSINESS_UNIT);
			sesaSapClientService.createContracts(plants);
			
			log.info("Finished Sesa-Populate-Contract-Job");
			
		}
	
	/*@Bean(name = "sesaPopulateContractsJobBean")
	public JobDetailFactoryBean populateScoreCardJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "sesaPopulateContractsJobBeanTrigger")
	public CronTriggerFactoryBean populateScoreCardJobTrigger(
			@Qualifier("sesaPopulateContractsJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}*/
}
