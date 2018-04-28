package com.vedanta.vpmt.job;

import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.*;
import com.vedanta.vpmt.service.*;
import lombok.extern.slf4j.Slf4j;
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

import java.util.*;

import static com.vedanta.vpmt.contstant.Constants.MONTHLY;
import static com.vedanta.vpmt.contstant.Constants.WEEKLY;
import static java.util.Calendar.*;

@Component
@Slf4j
public class PopulateFormsJob implements Job {

	@Value("${cron.expression.populate.forms}")
	private String cronExpression;

	@Autowired
	private ContractService contractService;

	@Autowired
	private FormService formService;

	@Autowired
	private FormGroupUserService formGroupUserService;

	@Autowired
	private FormSaveService formSaveService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Started Populate Forms Job");
		List<Form> forms = getForms();
		if (!CollectionUtils.isEmpty(forms)) {
			populateFormSaved(forms);
		}
		log.info("Finished Populate Forms Job");
	}

	private void populateFormSaved(List<Form> forms) {
		if (CollectionUtils.isEmpty(forms)) {
			return;
		}

		forms.forEach(form -> {
			Optional<List<Contract>> contractOptional = contractService.getContract(form.getCategoryId(),
					form.getSubCategoryId(), Constants.BALCO_BUSINESS_UNIT);
			if (contractOptional.isPresent()) {
				populateFormSaved(contractOptional.get(), form);
			}
		});
	}

	private void populateFormSaved(List<Contract> contracts, Form form) {
		contracts.forEach(contract -> {
			List<FormGroupUser> formGroupUsers = formGroupUserService.getFormGroupUsersForContract(contract.getNumber(),
					form.getId(), 0L, Constants.BALCO_BUSINESS_UNIT);
			formGroupUsers.forEach(formGroupUser -> {
				FormSaved formSaved = buildFormSaved(contract, form, formGroupUser);

				FormSaved formSavedDetails = formSaveService
						.getFormSavedByBusinessUnitIdCategoryIdAndSubCategoryIdAndMonthIdAndYearId(
								formSaved.getContractNumber(), formSaved.getBusinessUnitId(), formSaved.getMonthId(),
								formSaved.getYearId(), formSaved.getCategoryId(), formSaved.getSubCategoryId());

				if (ObjectUtils.isEmpty(formSavedDetails)) {
					formSaveService.populate(formSaved);

				}

			});
		});
	}

	private FormSaved buildFormSaved(Contract contract, Form form, FormGroupUser formGroupUser) {

		/*
		 * Long managerIdLong =
		 * Long.parseLong(formGroupUser.getContractManagerId()); String
		 * managerIdString = String.valueOf(managerIdLong);
		 */
		return FormSaved.builder().contractId(contract.getId()).contractNumber(contract.getNumber())
				.vendorId(contract.getVendorId()).vendorCode(contract.getVendorCode())
				.categoryId(contract.getCategoryId()).categoryName(contract.getCategoryName())
				.subCategoryId(contract.getSubCategoryId()).subCategoryName(contract.getSubCategoryName())
				.formId(form.getId()).formName(form.getName()).departmentId(contract.getDepartmentId())
				.monthId(getInstance().get(Calendar.MONTH)).yearId(getInstance().get(Calendar.YEAR))
				.contractManagerId(formGroupUser.getContractManagerId())
				.contractManagerName(formGroupUser.getContractManager()).poItem(formGroupUser.getPoItem())
				.plantId(formGroupUser.getPlantId()).plantCode(formGroupUser.getPlantCode())
				.departmentId(formGroupUser.getDepartmentId()).departmentCode(formGroupUser.getDepartmentCode())
				.poItemDescription(formGroupUser.getDescription()).dueDate(getDate(form.getDueDayOfFrequency()))
				.businessUnitId(formGroupUser.getBusinessUnitId()).build();
	}

	private Date getDate(int dueDayOfFrequency) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, dueDayOfFrequency);
		return calendar.getTime();
	}

	private List<Form> getForms() {
		List<Form> forms = new ArrayList<>();
		List<Form> weekForm = formService.getForms(getInstance().get(DAY_OF_WEEK), WEEKLY,
				Constants.BALCO_BUSINESS_UNIT);
		List<Form> monthForm = formService.getForms(getInstance().get(DAY_OF_MONTH), MONTHLY,
				Constants.BALCO_BUSINESS_UNIT);
		List<Form> dailyForm = formService.getForms(0, Constants.DAILY, Constants.BALCO_BUSINESS_UNIT);
		if (!CollectionUtils.isEmpty(dailyForm)) {
			forms.addAll(dailyForm);
		}
		if (!CollectionUtils.isEmpty(weekForm)) {
			forms.addAll(weekForm);
		}
		if (!CollectionUtils.isEmpty(monthForm)) {
			forms.addAll(monthForm);
		}
		return forms;
	}

	@Bean(name = "populateFormJobBean")
	public JobDetailFactoryBean populateFormJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "populateFormJobBeanTrigger")
	public CronTriggerFactoryBean populateFormJobTrigger(
			@Qualifier("populateFormJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}
}
