package com.vedanta.vpmt.sesa.job;

import static com.vedanta.vpmt.contstant.Constants.MONTHLY;
import static com.vedanta.vpmt.contstant.Constants.WEEKLY;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.getInstance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.Template;
import com.vedanta.vpmt.service.ContractService;
import com.vedanta.vpmt.service.ScoreCardGroupUserService;
import com.vedanta.vpmt.service.ScoreCardService;
import com.vedanta.vpmt.service.TemplateService;
import com.vedanta.vpmt.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SesaPopulateScorecardJob implements Job {

/*	@Value("${sesa.cron.expression.populate.scorecard}")
	private String cronExpression;
*/
	@Autowired
	private TemplateService templateService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private ScoreCardService scoreCardService;

	@Autowired
	private ScoreCardGroupUserService scoreCardGroupUserService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("SESA-POPULATE-SCORECARD-JOB");

		List<Template> templates = getTemplates();
		if (!CollectionUtils.isEmpty(templates)) {
			populateScoreBoard(templates);
		}
		log.info("Finished SESA-POPULATE-SCORECARD-JOB");

	}

	private void populateScoreBoard(List<Template> templates) {
		if (CollectionUtils.isEmpty(templates)) {
			return;
		}

		templates.forEach(template -> {
			Optional<List<Contract>> contractOptional = contractService.getContract(template.getCategoryId(),
					template.getSubCategoryId(), Constants.SESA_BUSINESS_UNIT);
			if (contractOptional.isPresent()) {
				populateScoreCard(contractOptional.get(), template);
			}
		});
	}

	private void populateScoreCard(List<Contract> contracts, Template template) {
		contracts.forEach(contract -> {
			List<ScoreCardGroupUser> scoreCardGroupUsers = scoreCardGroupUserService
					.getScoreCardGroupUsersForContract(contract.getNumber(), template.getId(), 0L,Constants.SESA_BUSINESS_UNIT);
			scoreCardGroupUsers.forEach(scoreCardGroupUser -> {
				ScoreCard scoreCard = buildScoreCard(contract, template, scoreCardGroupUser);

				if (scoreCard != null) {
					scoreCardService.populate(scoreCard);
				}

			});
		});
	}

	private ScoreCard buildScoreCard(Contract contract, Template template, ScoreCardGroupUser scoreCardGroupUser) {

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);

		Long managerIdLong = Long.parseLong(scoreCardGroupUser.getContractManagerId());
		String managerIdString = String.valueOf(managerIdLong);

		Calendar contractStartDate = Calendar.getInstance();
		Calendar contractExpriationDate = Calendar.getInstance();
		
		if (!ObjectUtils.isEmpty(contract.getStartDate())) {
			contractStartDate.setTime(contract.getStartDate());
			log.info("scorecard startdate cannot be null");
		}
		if (!ObjectUtils.isEmpty(contract.getStartDate())) {
			contractExpriationDate.setTime(contract.getEndDate());
			log.info("scorecard enddate cannot be null");
		}
		
		int startFlag = DateUtil.checkStartDate(cal, contractStartDate);

		boolean flag = DateUtil.isScoreCardPopulate(cal, contractExpriationDate);

		if (startFlag == 1) {
			if (flag) {
				return ScoreCard.builder().contractId(contract.getId()).contractNumber(contract.getNumber())
						.vendorId(contract.getVendorId()).vendorCode(contract.getVendorCode())
						.categoryId(contract.getCategoryId()).categoryName(contract.getCategoryName())
						.subCategoryId(contract.getSubCategoryId()).subCategoryName(contract.getSubCategoryName())
						.templateId(template.getId()).templateName(template.getName())
						.departmentId(contract.getDepartmentId()).monthId(month).yearId(year)
						.contractManagerId(managerIdString).contractManagerName(scoreCardGroupUser.getContractManager())
						.poItem(scoreCardGroupUser.getPoItem()).plantId(scoreCardGroupUser.getPlantId())
						.plantCode(scoreCardGroupUser.getPlantCode()).departmentId(scoreCardGroupUser.getDepartmentId())
						.departmentCode(scoreCardGroupUser.getDepartmentCode())
						.poItemDescription(scoreCardGroupUser.getDescription())
						.dueDate(getDate(template.getDueDayOfFrequency())).totalScore(100.00).weight(100)
						.businessUnitId(scoreCardGroupUser.getBusinessUnitId()).build();

			} else {
				return null;

			}

		} else {
			return null;
		}

	}

	private Date getDate(int dueDayOfFrequency) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, dueDayOfFrequency);
		return calendar.getTime();
	}

	private List<Template> getTemplates() {
		List<Template> templates = new ArrayList<>();
		List<Template> weekTemplate = templateService.getTemplates(getInstance().get(DAY_OF_WEEK), WEEKLY,Constants.SESA_BUSINESS_UNIT);
		List<Template> monthTemplate = templateService.getTemplates(getInstance().get(DAY_OF_MONTH), MONTHLY,Constants.SESA_BUSINESS_UNIT);
		if (!CollectionUtils.isEmpty(weekTemplate)) {
			templates.addAll(weekTemplate);
		}
		if (!CollectionUtils.isEmpty(monthTemplate)) {
			templates.addAll(monthTemplate);
		}
		return templates;
	}

/*	@Bean(name = "sesaPopulateScorecardJobBean")
	public JobDetailFactoryBean populateScoreCardJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "sesaPopulateScorecardJobBeanTrigger")
	public CronTriggerFactoryBean populateScoreCardJobTrigger(
			@Qualifier("sesaPopulateScorecardJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}*/

}
