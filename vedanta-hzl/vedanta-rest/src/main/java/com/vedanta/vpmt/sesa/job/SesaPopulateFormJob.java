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

import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.service.ContractService;
import com.vedanta.vpmt.service.FormGroupUserService;
import com.vedanta.vpmt.service.FormSaveService;
import com.vedanta.vpmt.service.FormService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SesaPopulateFormJob implements Job {
/*	@Value("${sesa.cron.expression.populate.form}")
	private String cronExpression;
*/
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
		log.info("STARTED-SESA-POPULATE-FORM-JOB");

		List<Form> forms = getForms();
		if (!CollectionUtils.isEmpty(forms)) {
			populateFormSaved(forms);
		}
		log.info("Finished SESA-POPULATE-FORM-JOB");

	}

	private void populateFormSaved(List<Form> forms) {
		if (CollectionUtils.isEmpty(forms)) {
			return;
		}

		forms.forEach(form -> {
			Optional<List<Contract>> contractOptional = contractService.getContract(form.getCategoryId(),
					form.getSubCategoryId(), Constants.SESA_BUSINESS_UNIT);
			if (contractOptional.isPresent()) {
				populateFormSaved(contractOptional.get(), form);
			}
		});
	}

	private void populateFormSaved(List<Contract> contracts, Form form) {
		contracts.forEach(contract -> {
			List<FormGroupUser> formGroupUsers = formGroupUserService.getFormGroupUsersForContract(contract.getNumber(),
					form.getId(), 0L, Constants.SESA_BUSINESS_UNIT);
			formGroupUsers.forEach(formGroupUser -> {
				FormSaved formSaved = buildFormSaved(contract, form, formGroupUser);
				formSaveService.populate(formSaved);

			});
		});
	}

	private FormSaved buildFormSaved(Contract contract, Form form, FormGroupUser formGroupUser) {

		Long managerIdLong = Long.parseLong(formGroupUser.getContractManagerId());
		String managerIdString = String.valueOf(managerIdLong);

		return FormSaved.builder().contractId(contract.getId()).contractNumber(contract.getNumber())
				.vendorId(contract.getVendorId()).vendorCode(contract.getVendorCode())
				.categoryId(contract.getCategoryId()).categoryName(contract.getCategoryName())
				.subCategoryId(contract.getSubCategoryId()).subCategoryName(contract.getSubCategoryName())
				.formId(form.getId()).formName(form.getName()).departmentId(contract.getDepartmentId())
				.monthId(getInstance().get(Calendar.MONTH)).yearId(getInstance().get(Calendar.YEAR))
				.contractManagerId(managerIdString).contractManagerName(formGroupUser.getContractManager())
				.poItem(formGroupUser.getPoItem()).plantId(formGroupUser.getPlantId())
				.plantCode(formGroupUser.getPlantCode()).departmentId(formGroupUser.getDepartmentId())
				.departmentCode(formGroupUser.getDepartmentCode()).poItemDescription(formGroupUser.getDescription())
				.dueDate(getDate(form.getDueDayOfFrequency())).businessUnitId(formGroupUser.getBusinessUnitId())
				.build();
	}

	private Date getDate(int dueDayOfFrequency) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, dueDayOfFrequency);
		return calendar.getTime();
	}

	private List<Form> getForms() {
		List<Form> forms = new ArrayList<>();
		List<Form> weekForm = formService.getForms(getInstance().get(DAY_OF_WEEK), WEEKLY, Constants.SESA_BUSINESS_UNIT);
		List<Form> monthForm = formService.getForms(getInstance().get(DAY_OF_MONTH), MONTHLY,
				Constants.SESA_BUSINESS_UNIT);
		List<Form> dailyForm = formService.getForms(0, Constants.DAILY, Constants.SESA_BUSINESS_UNIT);
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

/*	@Bean(name = "sesaPopulateFormJobBean")
	public JobDetailFactoryBean populateScoreCardJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "sesaPopulateFormJobBeanTrigger")
	public CronTriggerFactoryBean populateScoreCardJobTrigger(
			@Qualifier("sesaPopulateFormJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}*/

}
