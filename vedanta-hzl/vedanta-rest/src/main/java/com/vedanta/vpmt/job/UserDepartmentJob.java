package com.vedanta.vpmt.job;

import java.util.HashSet;
import java.util.Set;

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
import com.vedanta.vpmt.dao.UserDepartmentDao;
import com.vedanta.vpmt.model.UserDepartment;
import com.vedanta.vpmt.service.UserDepartmentService;
import com.vedanta.vpmt.service.UserDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserDepartmentJob implements Job {

	@Autowired
	@Value("${crone.expression.user.department}")
	private String CronExpression;

	@Autowired
	private UserDetailServiceImpl userService;

	@Autowired
	private UserDepartmentDao userDepartmentDao;

	@Autowired
	private UserDepartmentService userDepartmentService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		log.info("User-Department Scheduler Started");

		// for unique-department
		HashSet<String> userDepartmentfromUsers = userDepartmentDao.getUniqueDepartment(Constants.HZL_BUSINESS_UNIT);
		HashSet<String> DepartmentsfromUserDepartment = userDepartmentService.getAllUserDepartment(Constants.HZL_BUSINESS_UNIT);

		Set<String> Department = new HashSet<>();

		for (String departmentName : userDepartmentfromUsers) {

			if (departmentName != null) {
				if (DepartmentsfromUserDepartment != null) {

					if (DepartmentsfromUserDepartment.contains(departmentName.trim())) {
						// log.info("department name" + departmentName);
					} else {
						Department.add(departmentName);
					}
				}

			}
		}

		// for unique-officeobj
		HashSet<String> officeFromUser = userDepartmentDao.getUniqueOffice(Constants.HZL_BUSINESS_UNIT);
		HashSet<String> officeFromUserDepartment = userDepartmentService.getAllUserOffice(Constants.HZL_BUSINESS_UNIT);

		Set<String> office = new HashSet<>();

		for (String officeName : officeFromUser) {
			if (officeName != null) {
				if (officeFromUserDepartment != null) {

					if (officeFromUserDepartment.contains(officeName.trim())) {
						// log.info("Office name" + officeName);
					} else {
						office.add(officeName);
					}
				}
			}
		}

		for (String DepartmentUser : Department) {
			UserDepartment userd = new UserDepartment();
			userd.setDepartmentName(DepartmentUser);
			userDepartmentService.save(userd);
		}

		for (String Uoffice : office) {
			UserDepartment userd = new UserDepartment();
			userd.setOfficeName(Uoffice);
			userDepartmentService.save(userd);
		}
	}

	
	@Bean(name = "userDepartmentJobBean")
	public JobDetailFactoryBean scorecardAggregationJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "userDepartmentJobBeanTrigger")
	public CronTriggerFactoryBean scorecardAggregationJobBeanTrigger(
			@Qualifier("userDepartmentJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), CronExpression);
	}
}
