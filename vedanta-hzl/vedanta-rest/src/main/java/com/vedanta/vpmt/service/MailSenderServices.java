package com.vedanta.vpmt.service;

import static java.util.Calendar.DAY_OF_MONTH;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.EmailLog;
import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.FormGroupUser;
import com.vedanta.vpmt.model.FormSaved;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.Support;
import com.vedanta.vpmt.model.ScoreCardGroupUser;

import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.model.Vendor;

import lombok.extern.slf4j.Slf4j;

@Service("mailService")
@Slf4j
@EnableAsync
public class MailSenderServices {

	@Value("${smtp.email.from}")
	private String fromAddress;

	@Value("${survey.email.user.url}")
	private String surveyUrl;

	@Value("${form.email.user.url}")
	private String formUrl;

	@Value("${scorecard.email.user.url}")
	private String scorecardUrl;

	@Value("${file.location.support}")
	private String directory;

	@Value("${support.email.to}")
	private String supportMailTo;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UserDetailServiceImpl userServices;

	@Autowired
	private FormService formService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FormGroupUserService formGroupUserService;

	@Autowired
	private FormSaveService formSavedService;

	@Autowired
	private EmailTemplateService emailTemplateService;

	@Autowired
	private PlantHeadService plantHeadService;

	@Autowired
	private UserDetailServiceImpl userDetail;

	@Autowired
	private EmailLogService emailLogService;

	@Autowired
	private BusinessUnitService businessUnitService;

	@Async
	public String sendTo(String from, String to, String subject, String msg) throws MessagingException {
		String message = "";
		try {
		
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);

			Document doc = Jsoup.parse(msg);
			String text = new HtmlToPlainText().getPlainText(doc);

			mimeMessage.setText(msg, "UTF-8", "html");
			mimeMessage.setFrom(new InternetAddress(fromAddress));
			mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
			javaMailSender.send(mimeMessage);
			message = "success";
		}catch (Exception e) {
			message = "error";
			e.printStackTrace();
			log.info("log message " + e.getMessage());

		}
		
		return message;
	}
	
	
	public String schedulerEmailSendTo(String from, String to, String subject, String msg) throws MessagingException {
		String message = "";
		try {
		
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage, true);

			Document doc = Jsoup.parse(msg);
			String text = new HtmlToPlainText().getPlainText(doc);

			mimeMessage.setText(msg, "UTF-8", "html");
			mimeMessage.setFrom(new InternetAddress(fromAddress));
			mimeMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
			javaMailSender.send(mimeMessage);
			message = "success";
		}

		catch (Exception e) {
			message = "error";
			e.printStackTrace();
			log.info("log message " + e.getMessage());

		}
		
		return message;
	}

	@Async
	public String supportMailSend(Support support) {

		String message = "";

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			String emailTemplateDetails = "";
			BusinessUnit businessUnitDetails = null;
			User currentUser = userDetail.getUserByUserId(vedantaUser.getId());

			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			EmailTemplate emailTemplate = emailTemplateService.get(Constants.DEFAULT_EMAIL_TEMPLATE_SUPPORT);

			if (!ObjectUtils.isEmpty(emailTemplate)) {

				String msg = emailTemplate.getMsgContent();
				emailTemplateDetails = msg;

				if (StringUtils.isNotBlank(currentUser.getName())) {
					emailTemplateDetails = emailTemplateDetails.replace("[[Name]]", currentUser.getName());

				} else {
					emailTemplateDetails = emailTemplateDetails.replace("[[Name]]", "");
				}

				if (currentUser.getBusinessUnitId() != 0L) {
					businessUnitDetails = businessUnitService.get(currentUser.getBusinessUnitId());

					if (StringUtils.isNotBlank(businessUnitDetails.getUnitName())) {
						emailTemplateDetails = emailTemplateDetails.replace("[[businessUnitName]]",
								businessUnitDetails.getUnitName());
					} else {
						emailTemplateDetails = emailTemplateDetails.replace("[[businessUnitName]]", "");
					}

				} else {
					emailTemplateDetails = emailTemplateDetails.replace("[[businessUnitName]]", "");
				}

				if (StringUtils.isNotBlank(currentUser.getEmail())) {
					emailTemplateDetails = emailTemplateDetails.replace("[[emailId]]", currentUser.getEmail());
				} else {
					emailTemplateDetails = emailTemplateDetails.replace("[[emailId]]", "");
				}
				if (StringUtils.isNotBlank(support.getMessage())) {
					emailTemplateDetails = emailTemplateDetails.replace("[[Message]]", support.getMessage());
				} else {
					emailTemplateDetails = emailTemplateDetails.replace("[[Message]]", "");
				}
				if (StringUtils.isNotBlank(currentUser.getEmployeeId())) {
					emailTemplateDetails = emailTemplateDetails.replace("[[employeeId]]", currentUser.getEmployeeId());
				} else {
					emailTemplateDetails = emailTemplateDetails.replace("[[employeeId]]", "");
				}

			}

			Document doc = Jsoup.parse(emailTemplateDetails);
			String text = new HtmlToPlainText().getPlainText(doc);

			helper.setFrom(new InternetAddress(fromAddress));
			helper.setTo(new InternetAddress(supportMailTo));
			helper.setSubject(support.getTitle());
			helper.setText(text);
			String folder = "supportFiles";

			if (!ObjectUtils.isEmpty(support.getFileList())) {

				for (String fileName : support.getFileList()) {
					FileSystemResource file = new FileSystemResource(directory + folder + "/" + fileName);
					helper.addAttachment(file.getFilename(), file);

				}
				javaMailSender.send(mimeMessage);
			} else {
				sendTo("", supportMailTo, support.getTitle(), emailTemplateDetails);
			}

			message = "success";
		}

		catch (MailAuthenticationException | MailSendException | MessagingException e) {
			message = "error";
			e.printStackTrace();
			log.error("log message " + e.getMessage());

		}
		return message;
	}

	@Async
	public void sendBulkEmail(EmailLog emailLog) {

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			User currentUser = userDetail.getUserByUserId(vedantaUser.getId());
			Long templateId = 6l;

			List<Long> selectedUser = emailLog.getScorecard_users();

			Long formId = emailLog.getSurvetypeId();
			Long contractId = emailLog.getSurveycontractId();
			Long plantId = emailLog.getSurveyplant_id();
			Long vendorId = emailLog.getSurveyvendorId();

			for (Long user : selectedUser) {

				User userDetails = userServices.getUserByUserId(user);
				String userName = userDetails.getName();
				String employeeId = userDetails.getEmployeeId();
				String toEmailId = userDetails.getEmail() == null ? "" : userDetails.getEmail();

				// for Save FormGroupUser
				Form formDesc = formService.get(formId);
				Long categoryId = formDesc.getCategoryId();
				Long subCategoryId = formDesc.getSubCategoryId();
				String surveyName = formDesc.getName();
				Contract contract = contractService.get(contractId);
				String contractNumber = contract.getNumber();
				Vendor vendorDetails = vendorService.get(vendorId);
				String vendorCode = vendorDetails.getVendorCode();
				Category categoryDetails = categoryService.get(categoryId);
				String categoryName = categoryDetails.getCategoryName();

				FormGroupUser formGroupEntity = new FormGroupUser();
				formGroupEntity.setCategoryName(categoryName);
				formGroupEntity.setContractNumber(contractNumber);
				formGroupEntity.setCategoryId(categoryId);
				formGroupEntity.setContractId(contractId);
				formGroupEntity.setFormId(formId);
				formGroupEntity.setSubCategoryId(subCategoryId);
				formGroupEntity.setEmployeeId(employeeId);
				formGroupEntity.setContractManagerId(employeeId);
				formGroupEntity.setContractManager(userName);
				formGroupEntity.setStatus(0);
				formGroupEntity.setFormName(surveyName);
				formGroupEntity.setPlantId(plantId);
				formGroupEntity.setVendorId(vendorId);
				// formGroupEntity.setSubmittedBy(vedantaUser.getId());
				formGroupEntity.setVendorCode(vendorCode);
				formGroupEntity.setSubCategoryName(vendorDetails.getSubCategoryName());
				formGroupEntity.setFormGroupDetailId(0l);
				formGroupEntity.setBusinessUnitId(emailLog.getBusinessUnitId());
				formGroupUserService.save(formGroupEntity);

				// for FormSave data
				FormSaved formSavedEntity = FormSaved.builder().businessUnitId(emailLog.getBusinessUnitId())
						.contractId(contract.getId()).contractNumber(contract.getNumber())
						.vendorId(vendorDetails.getId()).vendorCode(vendorDetails.getVendorCode())
						.categoryId(categoryDetails.getId()).categoryName(categoryDetails.getCategoryName())
						.subCategoryId(formDesc.getSubCategoryId()).subCategoryName(vendorDetails.getSubCategoryName())
						.formId(formDesc.getId()).formName(formDesc.getName()).plantId(plantId)
						.yearId(Calendar.getInstance().get(Calendar.YEAR))
						.monthId(Calendar.getInstance().get(Calendar.MONTH)).plantCode(contract.getPlantCode())
						.contractManagerId(employeeId).contractManagerName(userName).dueDate(getDate(25)).average(0D)
						.build();

				FormSaved savedData = formSavedService.populate(formSavedEntity);

				long formSavedId = savedData.getId();

				// for Email Notification
				if (StringUtils.isNotEmpty(toEmailId)) {

					String catergoryName = "";
					String subCatergoryName = "";
					String scorecardMessage = emailLog.getUserMessage();
					String contractManger = "";
					String vendorName = "";

					EmailTemplate getEmailById = emailTemplateService.get(templateId);
					String msgContent = getEmailById.getMsgContent();

					String message = msgContent;

					if (StringUtils.isNotBlank(message)) {

						if (StringUtils.isEmpty(userName)) {
							message = message.replace("[[Name]]", "");
						} else {
							message = message.replace("[[Name]]", userName);

						}
						if (StringUtils.isEmpty(catergoryName)) {
							message = message.replace("[[CategoryName]]", "");
						} else {
							message = message.replace("[[CategoryName]]", catergoryName);

						}
						if (StringUtils.isEmpty(subCatergoryName)) {
							message = message.replace("[[SubCategoryName]]", "");
						} else {
							message = message.replace("[[SubCategoryName]]", subCatergoryName);

						}

						if (StringUtils.isEmpty(scorecardMessage)) {
							message = message.replace("[[Message]]", "");
						} else {
							message = message.replace("[[Message]]", scorecardMessage);

						}

						if (StringUtils.isEmpty(contractManger)) {
							message = message.replace("[[Contract Manager]]", "");
						} else {
							message = message.replace("[[Contract Manager]]", contractManger);

						}
						if (StringUtils.isEmpty(vendorName)) {
							message = message.replace("[[Vendor Name]]", "");
						} else {
							message = message.replace("[[Vendor Name]]", vendorName);

						}
						if (StringUtils.isEmpty("")) {
							message = message.replace("[[fromName]]", "");
						} else {
							message = message.replace("[[fromName]]", "");

						}
						if (StringUtils.isEmpty(surveyName)) {
							message = message.replace("[[surveyName]]", "");
						} else {
							message = message.replace("[[surveyName]]", surveyName);

						}

						String url = surveyUrl + formSavedId;
						String link = "<a href='" + url + "'>Click Here</a>";
						message = message.replace("[[link]]", link);
						try {
							sendTo("", toEmailId, "Survey Invitation", message);
						} catch (MessagingException e) {
							e.printStackTrace();
						}
						EmailLog emailLogDetails = new EmailLog();
						emailLog.setAssignedUserEmailid(toEmailId);
						emailLog.setCreatedBy(currentUser.getEmail());
						emailLog.setCreatedOn(new Date());
						emailLog.setBusinessUnitId(emailLog.getBusinessUnitId());
						emailLogService.save(emailLogDetails);

					}
				}
			}

		} catch (VedantaException e) {
			log.error("Error saving email log information");
		}

	}

	private Date getDate(int dueDayOfFrequency) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, dueDayOfFrequency);
		return calendar.getTime();
	}

	@Async
	public void complianceFormEmailNotification(FormSaved formSaved) throws MessagingException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

		User currentUser = userDetail.getUserByUserId(vedantaUser.getId());

		if (Constants.COMPLIANCE_FORM_SUBMIT.equals(formSaved.getComplianceFormEmailNotification())) {

			List<PlantHead> getAllPlantHeadByPlantCode = plantHeadService.geByPlantCode(formSaved.getPlantCode());

			if (!CollectionUtils.isEmpty(getAllPlantHeadByPlantCode)) {
				getAllPlantHeadByPlantCode.forEach(plantHead -> {
					User plantHeadUserDetails = userServices.getUserByEmployeeId(plantHead.getEmployeeId());

					if (!ObjectUtils.isEmpty(plantHeadUserDetails)) {
						EmailTemplate getEmailById = emailTemplateService
								.get(Constants.DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_SUBMIT);

						if (!ObjectUtils.isEmpty(getEmailById)) {
							String message = getEmailById.getMsgContent();
							if (StringUtils.isNotBlank(message)) {

								if (StringUtils.isEmpty(plantHeadUserDetails.getName())) {
									message = message.replace("[[Name]]", "");
								} else {
									message = message.replace("[[Name]]", plantHeadUserDetails.getName());

								}
								if (StringUtils.isNotBlank(formSaved.getContractNumber())) {
									message = message.replace("[[contractNumber]]", formSaved.getContractNumber());
								} else {
									message = message.replace("[[contractNumber]]", "");
								}
								if (!ObjectUtils.isEmpty(formSaved.getMonthId())) {
									message = message.replace("[[monthId]]", getMonthString(formSaved.getMonthId()));
								} else {
									message = message.replace("[[monthId]]", "");
								}

								if (!ObjectUtils.isEmpty(formSaved.getYearId())) {
									message = message.replace("[[yearId]]", String.valueOf(formSaved.getYearId()));
								} else {
									message = message.replace("[[yearId]]", "");
								}
								if (StringUtils.isNotBlank(currentUser.getName())) {
									message = message.replace("[[submittedBy]]", currentUser.getName());
								} else {
									message = message.replace("[[submittedBy]]", "");
								}
								message = message.replace("[[submittedOn]]", getDateString(new Date()));

								Long formId = formSaved.getId();

								String url = formUrl + formId;
								String link = "<a href='" + url + "'>Click Here</a>";
								message = message.replace("[[link]]", link);
							}

							try {
								sendTo(currentUser.getEmail(), plantHeadUserDetails.getEmail(), "Form Notification",
										message);
							} catch (Exception e) {
							e.printStackTrace();
							}

							EmailLog emailLog = new EmailLog();
							emailLog.setAssignedUserEmailid(plantHeadUserDetails.getEmail());
							emailLog.setCreatedBy(currentUser.getEmail());
							emailLog.setCreatedOn(new Date());
							emailLog.setBusinessUnitId(formSaved.getBusinessUnitId());
							emailLogService.save(emailLog);
						}
					}
				});

			}
		}

		if (Constants.COMPLIANCE_FORM_APPROVE.equals(formSaved.getComplianceFormEmailNotification())) {

			if (!ObjectUtils.isEmpty(formSaved.getContractManagerId())) {
				User userDetailsSubmittedBy = userDetail.getUserByEmployeeIdAndBusinessUnitId(formSaved.getContractManagerId(),formSaved.getBusinessUnitId());

				if (!ObjectUtils.isEmpty(userDetailsSubmittedBy)) {
					EmailTemplate getEmailById = emailTemplateService
							.get(Constants.DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_APPROVE);

					if (!ObjectUtils.isEmpty(getEmailById)) {

						String message = getEmailById.getMsgContent();

						if (StringUtils.isNotBlank(message)) {

							if (StringUtils.isEmpty(userDetailsSubmittedBy.getName())) {
								message = message.replace("[[Name]]", "");
							} else {
								message = message.replace("[[Name]]", userDetailsSubmittedBy.getName());

							}
							if (StringUtils.isNotBlank(formSaved.getContractNumber())) {
								message = message.replace("[[contractNumber]]", formSaved.getContractNumber());
							} else {
								message = message.replace("[[contractNumber]]", "");
							}

							if (StringUtils.isNotBlank(currentUser.getName())) {
								message = message.replace("[[submittedBy]]", currentUser.getName());
							} else {
								message = message.replace("[[submittedBy]]", "");
							}
							message = message.replace("[[submittedOn]]", getDateString(new Date()));

							Long formId = formSaved.getId();

							String url = formUrl + formId;
							String link = "<a href='" + url + "'>Click Here</a>";
							message = message.replace("[[link]]", link);
						}

						sendTo(currentUser.getEmail(), userDetailsSubmittedBy.getEmail(), "Form Notification", message);

						EmailLog emailLog = new EmailLog();
						emailLog.setAssignedUserEmailid(userDetailsSubmittedBy.getEmail());
						emailLog.setCreatedBy(currentUser.getEmail());
						emailLog.setCreatedOn(new Date());
						emailLog.setBusinessUnitId(formSaved.getBusinessUnitId());
						emailLogService.save(emailLog);
					}

				}
			}

		}
		if (Constants.COMPLIANCE_FORM_REJECT.equals(formSaved.getComplianceFormEmailNotification())) {

			if (!ObjectUtils.isEmpty(formSaved.getSubmittedBy())) {
				User userDetailsSubmittedBy = userDetail.getUserByUserId(formSaved.getSubmittedBy());

				if (!ObjectUtils.isEmpty(userDetailsSubmittedBy)) {
					EmailTemplate getEmailById = emailTemplateService
							.get(Constants.DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_REJECT);

					if (!ObjectUtils.isEmpty(getEmailById)) {
						String message = getEmailById.getMsgContent();
						if (StringUtils.isNotBlank(message)) {

							if (StringUtils.isEmpty(userDetailsSubmittedBy.getName())) {
								message = message.replace("[[Name]]", "");
							} else {
								message = message.replace("[[Name]]", userDetailsSubmittedBy.getName());

							}
							if (StringUtils.isNotBlank(formSaved.getContractNumber())) {
								message = message.replace("[[contractNumber]]", formSaved.getContractNumber());
							} else {
								message = message.replace("[[contractNumber]]", "");
							}
							if (StringUtils.isNotBlank(currentUser.getName())) {
								message = message.replace("[[submittedBy]]", currentUser.getName());
							} else {
								message = message.replace("[[submittedBy]]", "");
							}
							message = message.replace("[[submittedOn]]", getDateString(new Date()));
							Long formId = formSaved.getId();
							String url = formUrl + formId;
							String link = "<a href='" + url + "'>Click Here</a>";
							message = message.replace("[[link]]", link);
						}

						sendTo(currentUser.getEmail(), userDetailsSubmittedBy.getEmail(), "Form Notification", message);

						EmailLog emailLog = new EmailLog();
						emailLog.setAssignedUserEmailid(userDetailsSubmittedBy.getEmail());
						emailLog.setCreatedBy(currentUser.getEmail());
						emailLog.setCreatedOn(new Date());
						emailLog.setBusinessUnitId(formSaved.getBusinessUnitId());
						emailLogService.save(emailLog);
					}

				}
			}
		}
	}

	@Async
	public void AssignFormUserEmailNotification(FormGroupUser formGroupUser) throws MessagingException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

		User currentUser = userDetail.getUserByUserId(vedantaUser.getId());

		if (StringUtils.isNotBlank(formGroupUser.getEmployeeId())) {
			User asignedUserDetails = userServices.getUserByEmployeeId(formGroupUser.getEmployeeId());

			if (!ObjectUtils.isEmpty(asignedUserDetails)) {
				EmailTemplate getEmailById = emailTemplateService
						.get(Constants.DEFAULT_EMAIL_TEMPLATE_COMPLIANCE_FORM_ASSIGN);

				if (!ObjectUtils.isEmpty(getEmailById)) {
					String message = getEmailById.getMsgContent();

					if (StringUtils.isNotBlank(message)) {

						if (StringUtils.isEmpty(asignedUserDetails.getName())) {
							message = message.replace("[[Name]]", "");
						} else {
							message = message.replace("[[Name]]", asignedUserDetails.getName());

						}
						if (StringUtils.isEmpty(formGroupUser.getCategoryName())) {
							message = message.replace("[[CategoryName]]", "");
						} else {
							message = message.replace("[[CategoryName]]", formGroupUser.getCategoryName());

						}
						if (StringUtils.isEmpty(formGroupUser.getSubCategoryName())) {
							message = message.replace("[[SubCategoryName]]", "");
						} else {
							message = message.replace("[[SubCategoryName]]", formGroupUser.getSubCategoryName());

						}

						if (StringUtils.isEmpty(formGroupUser.getEmailMessage())) {
							message = message.replace("[[Message]]", "");
						} else {
							message = message.replace("[[Message]]", formGroupUser.getEmailMessage());

						}

						if (StringUtils.isEmpty(formGroupUser.getContractManager())) {
							message = message.replace("[[Contract Manager]]", "");
						} else {
							message = message.replace("[[Contract Manager]]", formGroupUser.getContractManager());

						}
						if (!ObjectUtils.isEmpty(formGroupUser.getVendorId())) {
							Vendor vendorDetails = vendorService.get(formGroupUser.getVendorId());
							if (StringUtils.isEmpty(vendorDetails.getName())) {
								message = message.replace("[[Vendor Name]]", "");
							} else {
								message = message.replace("[[Vendor Name]]", vendorDetails.getName());

							}
						}
						Long formId = formGroupUser.getFormId();

						String url = formUrl + formId;
						String link = "<a href='" + url + "'>Click Here</a>";
						message = message.replace("[[link]]", link);
					}

					sendTo("", asignedUserDetails.getEmail(), "ScoreCard-Form Notification", message);
					EmailLog emailLog = new EmailLog();
					emailLog.setAssignedUserEmailid(asignedUserDetails.getEmail());
					emailLog.setCreatedBy(currentUser.getEmail());
					emailLog.setCreatedOn(new Date());
					emailLog.setBusinessUnitId(formGroupUser.getBusinessUnitId());
					emailLogService.save(emailLog);

				}
			}
		}
	}

	public static String getMonthString(int monthId) {
		String monthFullName = new DateFormatSymbols().getMonths()[monthId];
		return monthFullName;
	}

	public static String getDateString(Date date) {
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		String dateString = formatter.format(date);
		return dateString;
	}

	@Async
	public void scoreCardEmailNotification(ScoreCard scorecard) throws MessagingException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		User currentUser = userDetail.getUserByUserId(vedantaUser.getId());

		if (StringUtils.isNotBlank(scorecard.getScorecardEmailNotification())) {

			if (scorecard.getScorecardEmailNotification().equals(Constants.SCORECARD_SUBMIT)) {

				List<PlantHead> getAllPlantHeadByPlantCode = plantHeadService.geByPlantCode(scorecard.getPlantCode());

				if (!CollectionUtils.isEmpty(getAllPlantHeadByPlantCode)) {
					getAllPlantHeadByPlantCode.forEach(plantHead -> {
						User plantHeadUserDetails = userServices.getUserByEmployeeId(plantHead.getEmployeeId());

						if (!ObjectUtils.isEmpty(plantHeadUserDetails)) {
							EmailTemplate getEmailById = emailTemplateService
									.get(Constants.DEFAULT_EMAIL_TEMPLATE_SCORECARD_SUBMIT);

							if (!ObjectUtils.isEmpty(getEmailById)) {
								String message = getEmailById.getMsgContent();
								if (StringUtils.isNotBlank(message)) {

									if (StringUtils.isEmpty(plantHeadUserDetails.getName())) {
										message = message.replace("[[Name]]", "");
									} else {
										message = message.replace("[[Name]]", plantHeadUserDetails.getName());

									}
									if (StringUtils.isNotBlank(scorecard.getContractNumber())) {
										message = message.replace("[[contractNumber]]", scorecard.getContractNumber());
									} else {
										message = message.replace("[[contractNumber]]", "");
									}
									if (!ObjectUtils.isEmpty(scorecard.getMonthId())) {
										message = message.replace("[[monthId]]",
												getMonthString(scorecard.getMonthId()));
									} else {
										message = message.replace("[[monthId]]", "");
									}

									if (!ObjectUtils.isEmpty(scorecard.getYearId())) {
										message = message.replace("[[yearId]]", String.valueOf(scorecard.getYearId()));
									} else {
										message = message.replace("[[yearId]]", "");
									}
									if (StringUtils.isNotBlank(currentUser.getName())) {
										message = message.replace("[[submittedBy]]", currentUser.getName());
									} else {
										message = message.replace("[[submittedBy]]", "");
									}
									message = message.replace("[[submittedOn]]", getDateString(new Date()));

									Long scorecardId = scorecard.getId();
									EmailLog emailLog = new EmailLog();
									emailLog.setAssignedUserEmailid(plantHeadUserDetails.getEmail());
									emailLog.setCreatedBy(currentUser.getEmail());
									emailLog.setCreatedOn(new Date());
									emailLog.setBusinessUnitId(scorecard.getBusinessUnitId());
									emailLogService.save(emailLog);
									String url = scorecardUrl + scorecardId;
									String link = "<a href='" + url + "'>Click Here</a>";
									message = message.replace("[[link]]", link);
								}

								try {
									sendTo(currentUser.getEmail(), plantHeadUserDetails.getEmail(), "ScoreCard Submit",
											message);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								EmailLog emailLog = new EmailLog();
								emailLog.setAssignedUserEmailid(plantHeadUserDetails.getEmail());
								emailLog.setCreatedBy(currentUser.getEmail());
								emailLog.setCreatedOn(new Date());
								emailLog.setBusinessUnitId(scorecard.getBusinessUnitId());
								emailLogService.save(emailLog);
							}
						}
					});

				}

			}

			if (scorecard.getScorecardEmailNotification().equals(Constants.SCORECARD_APPROVE)) {

				if (!ObjectUtils.isEmpty(scorecard.getContractManagerId())) {
					User userDetailsSubmittedBy = userDetail.getUserByEmployeeIdAndBusinessUnitId(scorecard.getContractManagerId(), scorecard.getBusinessUnitId());
							
					if (!ObjectUtils.isEmpty(userDetailsSubmittedBy)) {
						EmailTemplate getEmailById = emailTemplateService
								.get(Constants.DEFAULT_EMAIL_TEMPLATE_SCORECARD_APPROVE);

						if (!ObjectUtils.isEmpty(getEmailById)) {
							String message = getEmailById.getMsgContent();
							if (StringUtils.isNotBlank(message)) {

								if (StringUtils.isEmpty(userDetailsSubmittedBy.getName())) {
									message = message.replace("[[Name]]", "");
								} else {
									message = message.replace("[[Name]]", userDetailsSubmittedBy.getName());

								}
								if (StringUtils.isNotBlank(scorecard.getContractNumber())) {
									message = message.replace("[[contractNumber]]", scorecard.getContractNumber());
								} else {
									message = message.replace("[[contractNumber]]", "");
								}
								if (StringUtils.isNotBlank(currentUser.getName())) {
									message = message.replace("[[submittedBy]]", currentUser.getName());
								} else {
									message = message.replace("[[submittedBy]]", "");
								}
								message = message.replace("[[submittedOn]]", getDateString(new Date()));

								Long scorecardId = scorecard.getId();

								String url = scorecardUrl + scorecardId;
								String link = "<a href='" + url + "'>Click Here</a>";
								message = message.replace("[[link]]", link);
							}

							sendTo(currentUser.getEmail(), userDetailsSubmittedBy.getEmail(), "ScoreCard-Approve",
									message);
							EmailLog emailLog = new EmailLog();
							emailLog.setAssignedUserEmailid(userDetailsSubmittedBy.getEmail());
							emailLog.setCreatedBy(currentUser.getEmail());
							emailLog.setCreatedOn(new Date());
							emailLog.setBusinessUnitId(scorecard.getBusinessUnitId());
							emailLogService.save(emailLog);

						}
					}
				}

			}

			if (scorecard.getScorecardEmailNotification().equals(Constants.SCORECARD_REJECT)) {

				if (!ObjectUtils.isEmpty(scorecard.getSubmittedBy())) {
					User userDetailsSubmittedBy = userDetail.getUserByUserId(scorecard.getSubmittedBy());

					if (!ObjectUtils.isEmpty(userDetailsSubmittedBy)) {
						EmailTemplate getEmailById = emailTemplateService
								.get(Constants.DEFAULT_EMAIL_TEMPLATE_SCORECARD_REJECT);

						if (!ObjectUtils.isEmpty(getEmailById)) {
							String message = getEmailById.getMsgContent();
							if (StringUtils.isNotBlank(message)) {

								if (StringUtils.isEmpty(userDetailsSubmittedBy.getName())) {
									message = message.replace("[[Name]]", "");
								} else {
									message = message.replace("[[Name]]", userDetailsSubmittedBy.getName());

								}
								if (StringUtils.isNotBlank(scorecard.getContractNumber())) {
									message = message.replace("[[contractNumber]]", scorecard.getContractNumber());
								} else {
									message = message.replace("[[contractNumber]]", "");
								}
								if (StringUtils.isNotBlank(currentUser.getName())) {
									message = message.replace("[[submittedBy]]", currentUser.getName());
								} else {
									message = message.replace("[[submittedBy]]", "");
								}
								message = message.replace("[[submittedOn]]", getDateString(new Date()));

								Long scorecardId = scorecard.getId();

								String url = scorecardUrl + scorecardId;
								String link = "<a href='" + url + "'>Click Here</a>";
								message = message.replace("[[link]]", link);
							}

							sendTo(currentUser.getEmail(), userDetailsSubmittedBy.getEmail(), "ScoreCard-Reject",
									message);
							EmailLog emailLog = new EmailLog();
							emailLog.setAssignedUserEmailid(userDetailsSubmittedBy.getEmail());
							emailLog.setCreatedBy(currentUser.getEmail());
							emailLog.setCreatedOn(new Date());
							emailLog.setBusinessUnitId(scorecard.getBusinessUnitId());
							emailLogService.save(emailLog);

						}
					}
				}
			}
		}
	}

	@Async
	public void scorecardAssignEmailNotification(ScoreCardGroupUser scorecardGroupUser) throws MessagingException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		User currentUser = userDetail.getUserByUserId(vedantaUser.getId());
		Vendor vendorDetails = vendorService.get(scorecardGroupUser.getVendorId());
		User assignedUser = userDetail.getUserByEmployeeId(scorecardGroupUser.getEmployeeId());
		EmailTemplate getEmailById = emailTemplateService.get(Constants.DEFAULT_EMAIL_TEMPLATE_SCORECARD_ASSIGN);
		String message = getEmailById.getMsgContent();

		if (StringUtils.isNotBlank(message)) {

			if (StringUtils.isEmpty(assignedUser.getName())) {
				message = message.replace("[[Name]]", "");
			} else {
				message = message.replace("[[Name]]", assignedUser.getName());

			}
			if (StringUtils.isEmpty(scorecardGroupUser.getCategoryName())) {
				message = message.replace("[[CategoryName]]", "");
			} else {
				message = message.replace("[[CategoryName]]", scorecardGroupUser.getCategoryName());

			}
			if (StringUtils.isEmpty(scorecardGroupUser.getSubCategoryName())) {
				message = message.replace("[[SubCategoryName]]", "");
			} else {
				message = message.replace("[[SubCategoryName]]", scorecardGroupUser.getSubCategoryName());

			}

			if (StringUtils.isEmpty(scorecardGroupUser.getEmailMessage())) {
				message = message.replace("[[Message]]", "");
				message = message.replace("[[comment]]", "");
			} else {
				message = message.replace("[[comment]]", "Comment: ");
				message = message.replace("[[Message]]", scorecardGroupUser.getEmailMessage());
			}
			if (StringUtils.isEmpty(vendorDetails.getName())) {
				message = message.replace("[[Vendor Name]]", "");
			} else {
				message = message.replace("[[Vendor Name]]", vendorDetails.getName());

			}

			Long scorecardId = scorecardGroupUser.getScorecardId();
			String url = scorecardUrl + scorecardId;
			String link = "<a href='" + url + "'>Click Here</a>";
			message = message.replace("[[link]]", link);
			sendTo("", assignedUser.getEmail(), "Scorecard Assigned", message);
			EmailLog emailLog = new EmailLog();
			emailLog.setAssignedUserEmailid(assignedUser.getEmail());
			emailLog.setCreatedBy(currentUser.getEmail());
			emailLog.setCreatedOn(new Date());
			emailLog.setBusinessUnitId(scorecardGroupUser.getBusinessUnitId());
			emailLogService.save(emailLog);
		}
	}
	public void schedulerLogMailNotification(String logMessage) throws MessagingException {
        String msg = null;
        EmailTemplate getEmailById = emailTemplateService.get(Constants.DEFAULT_EMAIL_TEMPLATE_SCHEDULER_LOG);

        if (!ObjectUtils.isEmpty(getEmailById)) {

            msg = getEmailById.getMsgContent();

            if (StringUtils.isNoneBlank(msg)) {
                msg = msg.replace("[[Message]]", logMessage);
            }
            msg = msg.replace("[[Name]]", "Admin");

        }
         sendTo("", supportMailTo, "Server-Notification", msg);
    } 
}
