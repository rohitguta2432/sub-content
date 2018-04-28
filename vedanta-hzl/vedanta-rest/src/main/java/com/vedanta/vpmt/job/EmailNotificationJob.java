package com.vedanta.vpmt.job;

import com.vedanta.vpmt.config.quartz.QuartzConfig;
import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.service.EmailLogService;
import com.vedanta.vpmt.service.EmailTemplateService;
import com.vedanta.vpmt.service.MailSenderServices;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class EmailNotificationJob implements Job {

	@Value("${cron.expression.email.notification}")
	private String cronExpression;

	@Value("${scorecard.scheduler.notification.url}")
	private String scorecardUrl;

	@Autowired
	private EmailLogService emailLogService;

	@Autowired
	private EmailTemplateService emailTemplateService;

	@Autowired
	private MailSenderServices mailSenderServices;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("ScoreCard Email Notication Started ");

		int scorecardStatus = 0;

		List<ScoreCard> scorecard = new ArrayList<ScoreCard>();
		scorecard = emailLogService.getAllScoreCard(scorecardStatus);

		if (ObjectUtils.isEmpty(scorecard)) {
			log.info("Pending Scorecard List is Null");
		} else {
			try {
				for (ScoreCard scorecardList : scorecard) {
					Date dueDate = scorecardList.getDueDate();

					Date createdOn = scorecardList.getCreatedOn();

					/* for created on current date email notification */
					if (createdOn != null) {

						String createddate = new SimpleDateFormat("yyyy-MM-dd").format(createdOn);
						String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

						if (currentDate.equals(createddate)) {

							log.info("Email Notification for  current date created Scorecard");

								String employeeId = scorecardList.getContractManagerId();
								User userDetails = emailTemplateService.getUserByEmployeeId(employeeId);
								String assignedUserEmailId = userDetails.getEmail();
								String assignedUsername = userDetails.getName();

								/*
								 * Email Notification for current date created
								 * Scorecard
								 */
								EmailTemplate getEmailById = emailTemplateService.get(8l);

								if (getEmailById != null) {

									String emailMessageTemplate = getEmailById.getMsgContent();
									String message = emailMessageTemplate;

									if (StringUtils.isNotBlank(message)) {

										if (StringUtils.isEmpty(assignedUsername)) {
											message = message.replace("[[Name]]", "");
										} else {
											message = message.replace("[[Name]]", assignedUsername);

										}

										if (dueDate == null) {
											message = message.replace("[[dueDate]]", "");
										} else {

											String dueDatetScorecard = new SimpleDateFormat("yyyy-MM-dd")
													.format(dueDate);
											message = message.replace("[[dueDate]]", dueDatetScorecard);

										}

										String url = scorecardUrl + scorecardList.getContractId();
										String link = "<a href='" + url + "'>Click Here</a>";
										message = message.replace("[[link]]", link);

										mailSenderServices.schedulerEmailSendTo("", assignedUserEmailId, "Scorecard-Notification",
												message);
									}
								}
							

						}

					}
					if (dueDate != null) {

						Calendar cal = Calendar.getInstance();
						cal.setTime(dueDate);
						cal.add(Calendar.DATE, -3);
						SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						String threedaysBeforeDueDate = format1.format(cal.getTime());

						// for current date equals to three days before due date
						String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
						if (currentDate.equals(threedaysBeforeDueDate)) {
							log.info("three days before current date");
							String contractNumber = scorecardList.getContractNumber();
							Set<ScoreCardGroupUser> groupUserDetails = emailLogService
									.getScorecardgroupUserDetailsByContractNumber(contractNumber);

							for (ScoreCardGroupUser details : groupUserDetails) {
								String employeeId = details.getEmployeeId();
								User userDetails = emailTemplateService.getUserByEmployeeId(employeeId);
								String assignedUserEmailId = userDetails.getEmail();
								String assignedUsername = userDetails.getName();

								// for email Notification for three days before
								EmailTemplate getEmailById = emailTemplateService.get(7l);

								if (getEmailById != null) {

									String emailMessageTemplate = getEmailById.getMsgContent();
									String message = emailMessageTemplate;

									if (StringUtils.isNotBlank(message)) {

										if (StringUtils.isEmpty(assignedUsername)) {
											message = message.replace("[[Name]]", "");
										} else {
											message = message.replace("[[Name]]", assignedUsername);

										}

										if (dueDate == null) {
											message = message.replace("[[dueDate]]", "");
										} else {

											String dueDatetScorecard = new SimpleDateFormat("yyyy-MM-dd")
													.format(dueDate);
											message = message.replace("[[dueDate]]", dueDatetScorecard);

										}
										String url = scorecardUrl + scorecardList.getContractId();
										String link = "<a href='" + url + "'>Click Here</a>";
										message = message.replace("[[link]]", link);

										mailSenderServices.sendTo("", assignedUserEmailId, "scorecard Notification",
												message);
									}
								}
							}
						} else {
							log.info("one days before current date");

							Calendar cal2 = Calendar.getInstance();
							cal.setTime(dueDate);
							cal.add(Calendar.DATE, -1);
							SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
							String onedaysBeforeDueDate = format1.format(cal.getTime());

							// for current date equals to three days before due
							// date
							String currentDate2 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
							if (currentDate2.equals(onedaysBeforeDueDate)) {
								log.info("current date");
								String contractNumber = scorecardList.getContractNumber();
								Set<ScoreCardGroupUser> groupUserDetails = emailLogService
										.getScorecardgroupUserDetailsByContractNumber(contractNumber);

								for (ScoreCardGroupUser details : groupUserDetails) {
									String employeeId = details.getEmployeeId();
									User userDetails = emailTemplateService.getUserByEmployeeId(employeeId);
									String assignedUserEmailId = userDetails.getEmail();
									String assignedUsername = userDetails.getName();

									// for email Notification for three days
									// before
									EmailTemplate getEmailById = emailTemplateService.get(7l);

									if (getEmailById != null) {

										String emailMessageTemplate = getEmailById.getMsgContent();
										String message = emailMessageTemplate;

										if (StringUtils.isNotBlank(message)) {

											if (StringUtils.isEmpty(assignedUsername)) {
												message = message.replace("[[Name]]", "");
											} else {
												message = message.replace("[[Name]]", assignedUsername);

											}

											if (dueDate == null) {
												message = message.replace("[[dueDate]]", "");
											} else {

												String dueDatetScorecard = new SimpleDateFormat("yyyy-MM-dd")
														.format(dueDate);
												message = message.replace("[[dueDate]]", dueDatetScorecard);

											}

											String url = scorecardUrl + scorecardList.getContractId();
											String link = "<a href='" + url + "'>Click Here</a>";
											message = message.replace("[[link]]", link);

											mailSenderServices.sendTo("", assignedUserEmailId, "scorecard Notification",
													message);
										}
									}
								}
							}
						}

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Bean(name = "emailNoticationJobBean")
	public JobDetailFactoryBean emailNoticationJobBean() {
		return QuartzConfig.createJobDetail(this.getClass());
	}

	@Bean(name = "emailNoticationJobBeanTrigger")
	public CronTriggerFactoryBean emailNoticationJobBeanTrigger(
			@Qualifier("emailNoticationJobBean") JobDetailFactoryBean fetchScheduledJobBean) {
		return QuartzConfig.createCronTrigger(fetchScheduledJobBean.getObject(), cronExpression);
	}
}
