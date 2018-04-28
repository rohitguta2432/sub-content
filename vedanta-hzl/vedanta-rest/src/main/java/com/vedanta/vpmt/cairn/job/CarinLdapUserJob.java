package com.vedanta.vpmt.cairn.job;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
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
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.security.LDAPAuthenticationProvider;
import com.vedanta.vpmt.service.LDAPActiveUserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CarinLdapUserJob implements Job {

	/*@Value("${carin.cron.expression.LDAPActiveUser}")
	private String cronExpression;
*/
	@Autowired
	private LDAPAuthenticationProvider ldapAuthenticationProvider;

	@Autowired
	private LDAPActiveUserService ldapActiveUserService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		log.info("STARTED-CAIRN-LDAP-USER");
		List<Long> businessUnits = new ArrayList<>();
		businessUnits.add(Constants.CAIRN_BUSINESS_UNIT);

		businessUnits.forEach((businessUnitId) -> {

			(ldapAuthenticationProvider.getAllLDAPUser(businessUnitId)).forEach(activeUserResponse -> {
				User entity = createLDAPUser(activeUserResponse, businessUnitId);
				entity.setLdapBusinessUnitId(Constants.CAIRN_BUSINESS_UNIT);
				ldapActiveUserService.save(entity);
			});
		});
		log.info("FINISHED-CAIRN-LDAP-USER");
	}

	private User createLDAPUser(JSONObject ldapResponse, Long businessUnitId) {
		User user = new User();
		if (ldapResponse.has("cn")) {
			user.setName(ldapResponse.getString("cn"));
		}

		if (ldapResponse.has("sAMAccountName")) {
			user.setEmployeeId(ldapResponse.getString("sAMAccountName").replaceFirst("^0+(?!$)", ""));
			user.setLoginId(ldapResponse.getString("sAMAccountName").replaceFirst("^0+(?!$)", ""));
		}

		if (ldapResponse.has("mail")) {
			user.setEmail(ldapResponse.getString("mail"));
		}

		if (ldapResponse.has("telephonenumber")) {
			user.setTelephone(ldapResponse.getString("telephonenumber"));
		}

		if (ldapResponse.has("l")) {
			user.setCity(ldapResponse.getString("l"));
		}

		if (ldapResponse.has("department")) {
			user.setDepartmentName(ldapResponse.getString("department"));
		}

		if (ldapResponse.has("company")) {
			user.setCompany(ldapResponse.getString("company"));
		}

		if (ldapResponse.has("physicalDeliveryOfficeName")) {
			user.setOffice(ldapResponse.getString("physicalDeliveryOfficeName"));
		}

		user.setAuthorities(Constants.ROLE_CONTRACT_MANAGER);
		user.setStatus(Constants.STATUS_ACTIVE);
		user.setBusinessUnitId(businessUnitId);
		return user;

	}

	/*@Bean(name = "cairnLDAPActiveUserJobBean")
	public JobDetailFactoryBean LDAPActiveUserJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "cairnLDAPActiveUserJobBeanTrigger")
	public CronTriggerFactoryBean LDAPActiveUserJobBeanTrigger(
			@Qualifier("cairnLDAPActiveUserJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}*/
}
