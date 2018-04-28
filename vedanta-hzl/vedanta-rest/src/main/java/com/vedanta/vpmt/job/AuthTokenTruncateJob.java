package com.vedanta.vpmt.job;

import java.util.List;

import org.quartz.CronExpression;
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
import com.vedanta.vpmt.model.AuthenticationToken;
import com.vedanta.vpmt.service.AuthenticationTokenService;
import com.vedanta.vpmt.service.AuthenticationTokenServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthTokenTruncateJob implements Job {

	@Value("${auth.token.truncate.job}")
	private String cronExpression;

	@Autowired
	private AuthenticationTokenService authTokenService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("STARTED-AUTH-TOKEN-TRUNCATE-JOB");
		List<AuthenticationToken> authToken = authTokenService.getAllAuthToken();

		if (!ObjectUtils.isEmpty(authToken)) {
			authTokenService.truncateAuthTokenUser();
		}

		log.info("FINISHED-AUTH-TOKEN-TRUNCATE-JOB");

	}

	@Bean(name = "authTokenJobBean")
	public JobDetailFactoryBean populateAuthTokenJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "authTokenJobTrigger")
	public CronTriggerFactoryBean populateAuthTokenJobTrigger(
			@Qualifier("authTokenJobBean") JobDetailFactoryBean fetchScheduled) {
		return QuartzConfig.createCronTrigger(fetchScheduled.getObject(), cronExpression);
	}

}
