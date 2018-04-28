package com.vedanta.vpmt.job;

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

import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.service.PlantService;
import com.vedanta.vpmt.service.sap.SAPClientService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PopulateContractsJob implements Job {

	@Value("${cron.expression.populate.contracts}")
	private String cronExpression;
	
	@Autowired
	private PlantService plantService;

	@Autowired
	private SAPClientService sapClientService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Started Populate Contracts Job");
		List<Plant> plants = plantService.getAllPlants();
		sapClientService.createContracts(plants);
		log.info("Finished Populate Contracts Job");
	}

	@Bean(name = "populateContractsJobBean")
	public JobDetailFactoryBean populateScoreCardJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "populateContractsJobBeanTrigger")
	public CronTriggerFactoryBean populateScoreCardJobTrigger(
			@Qualifier("populateContractsJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}
}