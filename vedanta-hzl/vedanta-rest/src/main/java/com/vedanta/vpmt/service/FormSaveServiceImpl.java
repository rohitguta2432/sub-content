package com.vedanta.vpmt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.ComplianceDao;
import com.vedanta.vpmt.dao.FormGroupUserDao;
import com.vedanta.vpmt.dao.FormSavedDao;
import com.vedanta.vpmt.dao.HumanResourceDao;
import com.vedanta.vpmt.dao.PlantHeadDao;
import com.vedanta.vpmt.dao.TemplateFormDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.mapper.FormGroupUserMapper;
import com.vedanta.vpmt.model.*;
import com.vedanta.vpmt.model.FormSaved.Param;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by manishsanger on 24/09/17.
 */
@Service("formSaveService")
@Slf4j
public class FormSaveServiceImpl implements FormSaveService {

	@Autowired
	private FormSavedDao formSavedDao;

	@Autowired
	private FormGroupUserDao formGroupUserDao;

	@Autowired
	private ContractService contractService;

	@Autowired
	private FormService formService;

	@Autowired
	private FormGroupDetailService formGroupDetailService;

	@Autowired
	private FormGroupFieldService formGroupFieldService;

	@Autowired
	private FormGroupService formGroupService;

	@Autowired
	private FormFieldService formFieldService;

	@Autowired
	private UserDetailServiceImpl userDetail;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private PlantHeadDao plantHeadDao;

	@Autowired
	private PlantService plantService;

	@Autowired
	@Qualifier("scoreCardService")
	private ScoreCardService scoreCardService;

	@Autowired
	private TemplateFormDao templateFormDao;

	@Autowired
	private ScoreCardGroupUserService scoreCardGroupUserService;

	@Autowired
	private ComplianceDao complianceDao;

	@Autowired
	private HumanResourceDao humanResourceDao;

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public FormSaved get(long id) {
		if (id <= 0) {
			log.info("Invalid form saved id.");
			throw new VedantaException("Invalid form saved id.");
		}
		try {
			return formSavedDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching form saved by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormSaved getFormSaveById(long id) {
		if (id <= 0) {
			log.info("Invalid form saved id.");
			throw new VedantaException("Invalid form saved id.");
		}
		try {

			FormSaved formSaved = formSavedDao.findOne(id);
			if (!ObjectUtils.isEmpty(formSaved)) {
				formSaved.setFormData(this.getFormDataFromFormSaved(formSaved));
			}
			return formSavedDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching form saved by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormSaved> getAllFormSavedByFormIdAndMonthAndYearAndStatus(Set<Long> formIds, int monthId, int yearId,
			int status, Long businessUnitId) {
		try {
			if (formIds.size() <= 0 || monthId < 0 || yearId < 0 || status < 0) {
				log.info("Invalid form id or monthId or yearId or status.");
				throw new VedantaException("Invalid form id or monthId or yearId or status.");
			}

			return formSavedDao.getFormSavedByFormIdAndMonthAndYearAndStatus(formIds, monthId, yearId, status,
					businessUnitId);
		} catch (VedantaException e) {
			String msg = "Error while fetching form saved by formId and monthId and yearId and status";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormSaved save(FormSaved entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("FormSaved object cannot be null/empty");
			throw new VedantaException("FormSaved object cannot be null/empty");
		}
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (!ObjectUtils.isEmpty(vedantaUser)) {

				FormSaved existedFormSaved = formSavedDao.findOne(entity.getId());

				if (!StringUtils.isEmpty(entity.getComplianceFormEmailNotification())) {
					if (entity.getComplianceFormEmailNotification().equals(Constants.COMPLIANCE_FORM_SUBMIT)) {
						entity.setSubmittedBy(vedantaUser.getId());
						entity.setSubmittedOn(new Date());
					} else if (entity.getComplianceFormEmailNotification().equals(Constants.COMPLIANCE_FORM_APPROVE)) {
						entity.setApprovedBy(vedantaUser.getEmployeeId());
						entity.setApprovedOn(new Date());
						entity.setSubmittedOn(existedFormSaved.getSubmittedOn());
					} else if (entity.getComplianceFormEmailNotification().equals(Constants.COMPLIANCE_FORM_REJECT)) {
						entity.setRejectedBy(vedantaUser.getEmployeeId());
						entity.setRejectedOn(new Date());
						entity.setSubmittedOn(existedFormSaved.getSubmittedOn());
					}
				}
			}

			if (!CollectionUtils.isEmpty(entity.getFormParamList())) {
				if (entity.getStatus() == Constants.STATUS_SUBMITTED
						|| entity.getStatus() == Constants.STATUS_APPROVED) {
					this.setAvgFormSave(entity);
				}
				this.setFormDataSave(entity);
			}

			return formSavedDao.save(entity);
		} catch (VedantaException e) {
			e.printStackTrace();
			String msg = "Error while saving scorecard information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	public static String getDateString(Date date) {
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		String dateString = formatter.format(date);
		return dateString;
	}

	private void setAvgFormSave(FormSaved entity) {
		Double avg = 0.0D;
		Long counter = 0L;

		if (!CollectionUtils.isEmpty(entity.getFormParamList()) && entity.getFormParamList().size() > 0) {
			Double sum = 0.0D;
			for (List<Param> fieldGrouped : entity.getFormParamList()) {
				for (Param param : fieldGrouped) {
					counter++;
					sum += Double.parseDouble(param.getValue());
				}
			}
			if (counter != 0) {
				avg = sum / counter;
			}
		}
		entity.setAverage(avg);
	}

	private void firstFormEntry(FormSaved formSaved) {
		Gson g = new Gson();
		List<FormGroup> formGroups = this.getAllFormGroupByFormSavedId(formSaved.getId());
		FormSaved formSavedNew = formSavedDao.findOne(formSaved.getId());

		if (ObjectUtils.isEmpty(formSavedNew.getFormDataJson())) {

			List<FormData> formDataList = new ArrayList<>();
			Long formDataId = 0L;

			for (FormGroup formGroup : formGroups) {
				FormGroupDetail group = formGroup.getFormGroupDetail();
				for (FormField field : group.getFormFields()) {
					FormData formData = new FormData();
					formData.setId(++formDataId);
					formData.setContractId(formSavedNew.getContractId());
					formData.setDepartmentId(formSavedNew.getDepartmentId());
					formData.setFormFieldId(field.getId());
					formData.setGroupId(group.getId());
					formData.setLastValue("");
					formData.setFormSavedId(formSaved.getId());
					formData.setStatus(Constants.STATUS_NOT_FILLED);
					formData.setUserId(0L);
					formData.setValue("");
					formData.setVendorId(formSavedNew.getVendorId());
					formData.setRemarks("");
					formDataList.add(formData);

				}
			}

			formSavedNew.setFormDataJson(g.toJson(formDataList));
			formSavedDao.save(formSavedNew);
			formSavedNew = formSavedDao.findOne(formSaved.getId());

			if (!ObjectUtils.isEmpty(formSavedNew.getFormDataJson())) {

				for (FormData formData : getFormDataFromFormSaved(formSavedNew)) {
					for (List<Param> paramList : formSaved.getFormParamList()) {
						for (Param param : paramList) {
							if (Long.parseLong(param.getGroupId()) == formData.getGroupId()
									&& Long.parseLong(param.getFieldId()) == formData.getFormFieldId()) {
								param.setId(String.valueOf(formData.getId()));
							}
						}
					}
				}
			}
		}
	}

	private List<FormData> getFormDataFromFormSaved(FormSaved formSaved) {

		try {
			if (!ObjectUtils.isEmpty(formSaved.getFormDataJson())) {
				return OBJECT_MAPPER.readValue(formSaved.getFormDataJson(), new TypeReference<List<FormData>>() {
				});
			} else {
				return new ArrayList<FormData>();
			}
		} catch (IOException e) {
			log.info("formData object json parsing error.");
			throw new VedantaException("formData object json parsing error.");
		}
	}

	private List<FormGroup> getAllFormGroupByFormSavedId(Long formSavedId) {

		if (formSavedId <= 0) {
			log.info("form saved number cannot be null/empty");
			throw new VedantaException("scorecard number cannot be null/empty");
		}
		try {

			FormSaved formSaved = this.get(formSavedId);
			if (!ObjectUtils.isEmpty(formSaved)) {
				List<FormGroup> formGroups = formGroupService.getAllFormGroupsByFormId(formSaved.getFormId());
				if (!CollectionUtils.isEmpty(formGroups)) {
					formGroups.forEach(formGroup -> {
						FormGroupDetail groupDetail = formGroupDetailService.get(formGroup.getFormGroupDetailId());
						List<FormField> groupFieldList = new ArrayList<>();
						List<FormGroupField> groupFields = formGroupFieldService
								.getAllFormGroupFieldsByGroupId(groupDetail.getId());
						groupFields.forEach(groupField -> {
							FormField fieldDetail = formFieldService.getFormFieldDetail(groupField.getFormFieldId());
							groupFieldList.add(fieldDetail);
						});
						groupDetail.setFormFields(groupFieldList);

						formGroup.setFormGroupDetail(groupDetail);
					});
				}
				return formGroups;
			}

			return null;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private void setFormDataSave(FormSaved formSaved) {

		if (ObjectUtils.isEmpty(formSaved)) {
			log.info("FormSaved object cannot be null/empty");
			throw new VedantaException("FormSaved object cannot be null/empty");
		}

		try {
			List<List<Param>> formParamList = formSaved.getFormParamList();
			Gson g = new Gson();
			this.firstFormEntry(formSaved);

			//// (g.toJson(formSaved));

			FormSaved existedFormSaved = formSavedDao.findOne(formSaved.getId());

			if (!ObjectUtils.isEmpty(existedFormSaved)) {
				formSaved.setMonthId(existedFormSaved.getMonthId());
				formSaved.setYearId(existedFormSaved.getYearId());
				formSaved.setDueDate(existedFormSaved.getDueDate());
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();

			List<FormData> formDataList = getFormDataFromFormSaved(existedFormSaved);
			if (ObjectUtils.isEmpty(formDataList) && formDataList.size() < 1) {
				formDataList = new ArrayList<>();
			}

			for (List<Param> paramList : formParamList) {
				for (Param param : paramList) {

					if (!ObjectUtils.isEmpty(param.getId())) {
						for (FormData scd : formDataList) {
							if (Long.parseLong(param.getId()) == scd.getId()) {

								if (param.getValue() == null) {
									param.setValue("");
								}
								scd.setLastValue(scd.getValue());
								scd.setUserId(vedantaUser.getId());

								if (formSaved.getContractId() != null)
									scd.setContractId(formSaved.getContractId());
								if (formSaved.getDepartmentId() != null)
									scd.setDepartmentId(formSaved.getDepartmentId());
								if (param.getFieldId() != null)
									scd.setFormFieldId(Long.parseLong(param.getFieldId()));
								if (param.getGroupId() != null)
									scd.setGroupId(Long.parseLong(param.getGroupId()));
								if (param.getTarget() != null)
									scd.setTargetValue(param.getTarget());
								if (param.getValue() != null)
									scd.setValue(param.getValue());
								if (formSaved.getVendorId() != null)
									scd.setVendorId(formSaved.getVendorId());
								if (param.getRemark() != null)
									scd.setRemarks(param.getRemark());

							}
						}
					} else {
						FormData existedFormData = new FormData();

						existedFormData.setUserId(vedantaUser.getId());

						if (param.getValue() == null) {
							existedFormData.setValue("");
						}

						if (formSaved.getContractId() != null)
							existedFormData.setContractId(formSaved.getContractId());
						if (formSaved.getDepartmentId() != null)
							existedFormData.setDepartmentId(formSaved.getDepartmentId());
						if (param.getFieldId() != null)
							existedFormData.setFormFieldId(Long.parseLong(param.getFieldId()));
						if (param.getGroupId() != null)
							existedFormData.setGroupId(Long.parseLong(param.getGroupId()));
						if (param.getTarget() != null)
							existedFormData.setTargetValue(param.getTarget());
						if (param.getValue() != null)
							existedFormData.setValue(param.getValue());
						if (formSaved.getVendorId() != null)
							existedFormData.setVendorId(formSaved.getVendorId());
						if (param.getRemark() != null)
							existedFormData.setRemarks(param.getRemark());

						formDataList.add(existedFormData);
					}
				}
			}

			// formSaved.setFormData(formDataList);

			formSaved.setFormDataJson(g.toJson(formDataList));

			//// (g.toJson(formSaved));

		} catch (VedantaException e) {
			String msg = "Error while saving formSaved information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormSaved update(long id, FormSaved entity) {
		return null;
	}

	@Override
	public FormGroupUser saveFormGroupUser(FormGroupUser formGroupUser) {
		if (ObjectUtils.isEmpty(formGroupUser)) {
			log.info("FormSaved group user object cannot be null/empty");
			throw new VedantaException("FormSaved group user object cannot be null/empty");
		}

		try {

			FormGroupUser existingFormGroupUser;
			existingFormGroupUser = formGroupUserDao.getFormGroupUserForGroup(formGroupUser.getContractNumber(),
					formGroupUser.getContractManagerId(), formGroupUser.getFormId(),
					formGroupUser.getFormGroupDetailId());

			if (formGroupUser.getFormGroupDetailId() == 0) {
				/*
				 * List<FormGroupUser> existingFormGroupUserList =
				 * formGroupUserDao.getFormGroupsByContractNumberAndManagerId(formGroupUser.
				 * getContractNumber(), formGroupUser.getContractManagerId());
				 */
				List<FormGroupUser> existingFormGroupUserList = formGroupUserDao
						.getFormGroupsByContractNumberAndManagerIdAndFormIdAndFormGroupDetailId(
								formGroupUser.getContractNumber(), formGroupUser.getContractManagerId(),
								formGroupUser.getFormId());
				if (!ObjectUtils.isEmpty(existingFormGroupUserList))
					formGroupUserDao.delete(existingFormGroupUserList);
			} else {
				if (!ObjectUtils.isEmpty(existingFormGroupUser)) {
					formGroupUserDao.delete(existingFormGroupUser);
				}
			}

			return formGroupUserDao.save(formGroupUser);
		} catch (VedantaException e) {
			String msg = "Error while saving scorecard group user information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	private boolean isCurrentUserGranted() {
		/* (authorities.getAuthority()).equals(Constants.ROLE_CONTRACT_MANAGER) || */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)
					|| (authorities.getAuthority()).equals(Constants.ROLE_BUSINESS_UNIT_HEAD)
					|| (authorities.getAuthority()).equals(Constants.ROLE_PLANT_HEAD)) {
				return true;
			}
		}

		return false;
	}

	private boolean isCurrentUserContractManager() {

		/* (authorities.getAuthority()).equals(Constants.ROLE_CONTRACT_MANAGER) || */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_CONTRACT_MANAGER)) {
				return true;
			}
		}

		return false;
	}
	
	private boolean isCurrentUserHr() {

		/* (authorities.getAuthority()).equals(Constants.ROLE_CONTRACT_MANAGER) || */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_HR)) {
				return true;
			}
		}

		return false;
	}
	
	
	

	private boolean isCurrentUserPlantHead() {

		/* (authorities.getAuthority()).equals(Constants.ROLE_CONTRACT_MANAGER) || */
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_PLANT_HEAD)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Map<String, Object> getFormSavedFormMapByFormSavedId(Long formSavedId) {
		if (formSavedId <= 0) {
			log.info("form saved number cannot be null/empty");
			throw new VedantaException("form saved number cannot be null/empty");
		}
		try {
			Map<String, Object> formSavedFormMap = new HashMap<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			FormSaved formSaved = this.get(formSavedId);
			List<Plant> plants = plantService.getAllPlantsByCurrentUser();
			List<User> users;
			if (!ObjectUtils.isEmpty(formSaved)) {
				users = userDetail.getAllUsersNotAdminAndBuId(formSaved.getBusinessUnitId());
				formSaved.setFormData(this.getFormDataFromFormSaved(formSaved));
				formSaved.setIsEnable(true);

				Contract contractDetails = contractService.findByContractNumber(formSaved.getContractNumber());
				if (!ObjectUtils.isEmpty(contractDetails)) {
					formSavedFormMap.put(Constants.CONTRACT, contractDetails);
					Form formDetail = formService.get(formSaved.getFormId());
					formDetail.setIsAllGroupAccess(false);

					if ((formSaved.getBusinessUnitId() == 1L && formSaved.getCategoryId() == 6L
							&& formSaved.getSubCategoryId() == 36L)
							|| (formSaved.getBusinessUnitId() == 2L && formSaved.getCategoryId() == 12L
									&& formSaved.getSubCategoryId() == 54L)
							|| (formSaved.getBusinessUnitId() == 7L && formSaved.getCategoryId() == 42L
									&& formSaved.getSubCategoryId() == 83L)
							|| (formSaved.getBusinessUnitId() == 8L && formSaved.getCategoryId() == 48L
									&& formSaved.getSubCategoryId() == 110L)
							|| (formSaved.getBusinessUnitId() == 9L && formSaved.getCategoryId() == 54L
									&& formSaved.getSubCategoryId() == 139L)
							|| (formSaved.getBusinessUnitId() == 5L && formSaved.getCategoryId() == 30L
									&& formSaved.getSubCategoryId() == 157L)
							|| (formSaved.getBusinessUnitId() == 4L && formSaved.getCategoryId() == 24L
									&& formSaved.getSubCategoryId() == 177L)
							|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 36L
									&& formSaved.getSubCategoryId() == 195L)
							|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 12L
									&& formSaved.getSubCategoryId() == 54L)
							|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 48L
									&& formSaved.getSubCategoryId() == 110L)) {
						formSaved.setIsCompliance(true);
					} else {
						formSaved.setIsCompliance(false);
					}

					if (authorities.contains(new SimpleGrantedAuthority("ROLE_HR"))) {
						formDetail.setIsApprover(true);
					} else {
						formDetail.setIsApprover(false);
					}

					formSavedFormMap.put(Constants.FORM_SAVED_DETAIL, formSaved);
					formSavedFormMap.put(Constants.USER_DETAIL, users);

					if (!ObjectUtils.isEmpty(formDetail)) {
						Boolean isGranted = isCurrentUserGranted();
						if (isGranted) {
							formDetail.setIsAllGroupAccess(true);
						}
						if (isCurrentUserPlantHead()) {

							if (formSaved.getBusinessUnitId() == 1L && formSaved.getFormId() == 20L
									&& formSaved.getCategoryId() == 6L && formSaved.getSubCategoryId() == 36L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 2L && formSaved.getFormId() == 30L
									&& formSaved.getCategoryId() == 12L && formSaved.getSubCategoryId() == 54L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 7L && formSaved.getFormId() == 65L
									&& formSaved.getCategoryId() == 42L && formSaved.getSubCategoryId() == 83L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 8L && formSaved.getFormId() == 66L
									&& formSaved.getCategoryId() == 48L && formSaved.getSubCategoryId() == 110L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 9L && formSaved.getFormId() == 85L
									&& formSaved.getCategoryId() == 54L && formSaved.getSubCategoryId() == 139L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 5L && formSaved.getFormId() == 45L
									&& formSaved.getCategoryId() == 30L && formSaved.getSubCategoryId() == 157L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 4L && formSaved.getFormId() == 36L
									&& formSaved.getCategoryId() == 24L && formSaved.getSubCategoryId() == 177L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 6L && formSaved.getFormId() == 55L
									&& formSaved.getCategoryId() == 36L && formSaved.getSubCategoryId() == 195L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							}
						}

						List<FormGroup> formGroups = new ArrayList<>();

						if (isCurrentUserContractManager()) {
							if (formSaved.getContractManagerId().equals(vedantaUser.getEmployeeId())) {
								isGranted = true;
								formDetail.setIsAllGroupAccess(true);
								formDetail.setIsContractManager(true);
							} else {
								formDetail.setIsContractManager(false);
							}

						}
						if (isCurrentUserHr()) {
								isGranted = true;
								formDetail.setIsAllGroupAccess(true);
						}
						

						if (!isGranted) {
							if ((formSaved.getBusinessUnitId() == 1L && formSaved.getCategoryId() == 6L
									&& formSaved.getSubCategoryId() == 36L)
									|| (formSaved.getBusinessUnitId() == 2L && formSaved.getCategoryId() == 12L
											&& formSaved.getSubCategoryId() == 54L)
									|| (formSaved.getBusinessUnitId() == 7L && formSaved.getCategoryId() == 42L
											&& formSaved.getSubCategoryId() == 83L)
									|| (formSaved.getBusinessUnitId() == 8L && formSaved.getCategoryId() == 48L
											&& formSaved.getSubCategoryId() == 110L)
									|| (formSaved.getBusinessUnitId() == 9L && formSaved.getCategoryId() == 54L
											&& formSaved.getSubCategoryId() == 139L)
									|| (formSaved.getBusinessUnitId() == 5L && formSaved.getCategoryId() == 30L
											&& formSaved.getSubCategoryId() == 157L)
									|| (formSaved.getBusinessUnitId() == 4L && formSaved.getCategoryId() == 24L
											&& formSaved.getSubCategoryId() == 177L)
									|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 36L
											&& formSaved.getSubCategoryId() == 195L)) {

								List<ScoreCardGroupUser> scorecardGroupUsers = scoreCardGroupUserService
										.getScoreCardGroupUsersByEmployeeIdOrContractManagerIdAndAllGroups(
												vedantaUser.getEmployeeId(), vedantaUser.getEmployeeId());
								if (!ObjectUtils.isEmpty(scorecardGroupUsers) && scorecardGroupUsers.size() > 0) {
									isGranted = true;
									formDetail.setIsAllGroupAccess(true);
									formDetail.setIsContractManager(true);
								}
							}
						}

						if (isGranted) {

							User user = userDetail.getUserByUserId(vedantaUser.getId());
							if (!ObjectUtils.isEmpty(user)) {

								List<FormGroupUser> userAssignedChecked = formGroupUserDao
										.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
												formSaved.getContractManagerId(), formDetail.getId(), 0L);
								if (!ObjectUtils.isEmpty(userAssignedChecked)) {
									String assignedUserName = getAssignedUserName(userAssignedChecked);
									if (assignedUserName.equals("none")) {
										formDetail.setUserName(user.getName());
									} else {
										formDetail.setUserName(assignedUserName);
									}
								} else {
									formDetail.setUserName("");
								}

							} else {
								formDetail.setUserName("");
							}

							formGroups = formGroupService.getAllFormGroupsByFormId(formDetail.getId());
						} else {

							List<FormGroupUser> userAssigned = formGroupUserDao.getFormGroupUserForGroupAndForm(
									formSaved.getContractNumber(), formSaved.getContractManagerId(), formDetail.getId(),
									0L);

							if (!ObjectUtils.isEmpty(userAssigned) && userAssigned.size() > 0) {

								User user = userDetail.getUserByEmployeeId(userAssigned.get(0).getEmployeeId());
								if (!ObjectUtils.isEmpty(user)) {

									if (user.getId() == vedantaUser.getId()) {
										formDetail.setIsAllGroupAccess(true);
									}

									List<FormGroupUser> userAssignedChecked = formGroupUserDao
											.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
													formSaved.getContractManagerId(), formDetail.getId(), 0L);

									String assignedUserName = getAssignedUserName(userAssignedChecked);

									if (assignedUserName.equals("none")) {
										formDetail.setUserName(user.getName());
									} else {
										formDetail.setUserName(assignedUserName);
									}

								} else {
									formDetail.setUserName("");
								}
							} else {
								List<FormGroupUser> userAssignedChecked = formGroupUserDao
										.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
												formSaved.getContractManagerId(), formDetail.getId(), 0L);

								String assignedUserName = getAssignedUserName(userAssignedChecked);

								if (assignedUserName.equals("none")) {
									formDetail.setUserName(formSaved.getContractManagerName());
								} else {
									formDetail.setUserName(assignedUserName);
								}

							}

							List<FormGroupUser> formGroupUsers = formGroupUserDao.getFormGroupUserForUserAndForm(
									formSaved.getContractNumber(), formDetail.getId(), vedantaUser.getEmployeeId());
							Set<Long> groupIds = new HashSet<>();
							if (!CollectionUtils.isEmpty(formGroupUsers)) {
								formGroupUsers.forEach(formGroupUser -> {
									if (formGroupUser.getFormGroupDetailId() > 0) {
										formSaved.setIsEnable(false);
										groupIds.add(formGroupUser.getFormGroupDetailId());
									} else {
										formSaved.setIsEnable(true);
									}
								});
								if ((groupIds.size() > 0)) {
									formGroups = formGroupService.getAllFormGroupsInGroupsId(groupIds);

								} else {
									formGroups = formGroupService.getAllFormGroupsByFormId(formDetail.getId());
								}
								if (!ObjectUtils.isEmpty(formGroups) && formGroups.size() > 0) {
									for (FormGroup fGroup : formGroups) {
										if (fGroup.getFormId() != formSaved.getFormId()) {
											formGroups.remove(fGroup);
										}
									}
								}
							}
						}

						formSavedFormMap.put(Constants.FORM, formDetail);

						if (!CollectionUtils.isEmpty(formGroups)) {
							formGroups.forEach(formGroup -> {
								FormGroupDetail groupDetail = formGroupDetailService
										.get(formGroup.getFormGroupDetailId());
								List<FormField> groupFieldList = new ArrayList<>();
								List<FormGroupField> groupFields = formGroupFieldService
										.getAllFormGroupFieldsByGroupId(groupDetail.getId());
								groupFields.forEach(groupField -> {
									FormField fieldDetail = formFieldService
											.getFormFieldDetail(groupField.getFormFieldId());
									groupFieldList.add(fieldDetail);
								});
								groupDetail.setFormFields(groupFieldList);

								List<FormGroupUser> userAssignedGroup = formGroupUserDao
										.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
												formSaved.getContractManagerId(), formDetail.getId(),
												groupDetail.getId());

								/*
								 * if(isCurrentUserGranted() && !ObjectUtils.isEmpty(formDetail)){
								 * groupDetail.setUserName(formDetail.getUserName()); }else{
								 * 
								 * }
								 */

								if (!ObjectUtils.isEmpty(userAssignedGroup) && userAssignedGroup.size() > 0) {
									User user = userDetail
											.getUserByEmployeeId(userAssignedGroup.get(0).getEmployeeId());
									if (!ObjectUtils.isEmpty(user))
										groupDetail.setUserName(user.getName());
									else
										groupDetail.setUserName("");
								} else {

									List<FormGroupUser> userAssignedChecked = formGroupUserDao
											.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
													formSaved.getContractManagerId(), formDetail.getId(), 0L);

									String assignedUserName = getAssignedUserName(userAssignedChecked);

									if (assignedUserName.equals("none")) {
										groupDetail.setUserName(formDetail.getUserName());
									} else {
										groupDetail.setUserName(assignedUserName);
									}

								}

								formGroup.setFormGroupDetail(groupDetail);
							});
							formSavedFormMap.put(Constants.FORM_GROUPS, formGroups);
						}
					}
				}
			}

			return formSavedFormMap;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private String getAssignedUserName(List<FormGroupUser> userAssignedChecked) {

		if (!ObjectUtils.isEmpty(userAssignedChecked) && userAssignedChecked.size() > 0) {

			User user = userDetail.getUserByEmployeeId(userAssignedChecked.get(0).getEmployeeId());
			if (!ObjectUtils.isEmpty(user))
				return user.getName();
		}

		return "none";

	}

	@Override
	public List<FormSaved> getFormSavedByContractNumberAndStatus(String contractNumber, int status) {
		if (StringUtils.isEmpty(contractNumber)) {
			log.info("contract number cannot be null/empty");
			throw new VedantaException("contract number cannot be null/empty");
		}
		try {
			List<FormSaved> formSaved = new ArrayList<>();
			formSaved = formSavedDao.getContractFormSavedByStatus(contractNumber, status);
			return formSaved;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormSaved> getUserFormSavedByStatus(int status) {
		if (status < 0) {
			log.info("This is not a valid status");
			throw new VedantaException("This is not a valid status");
		}
		try {
			List<FormSaved> formSaved = new ArrayList<>();

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (VedantaUtility.isCurrentUserAdmin() || VedantaUtility.isCurrentUserBusinessUnitHead()) {

				if (VedantaUtility.isCurrentUserAdmin()) {
					if (status == Constants.ALL_SAVEFORM) {

						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							formSaved = formSavedDao.getAllFormSavedNotStatus(Constants.STATUS_DELETE);
						} else if (VedantaUtility.getCurrentUserBuId() != 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							formSaved = formSavedDao.getAllFormSavedNotStatus(VedantaUtility.getCurrentUserBuId(),
									Constants.STATUS_DELETE);
						} else {
							formSaved = formSavedDao.getAllFormSavedNotStatus(VedantaUtility.getCurrentUserBuId(),
									VedantaUtility.getCurrentUserPlantCode(), Constants.STATUS_DELETE);
						}

					} else {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							formSaved = formSavedDao.getAllFormSaved(status);
						} else if (VedantaUtility.getCurrentUserBuId() != 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							formSaved = formSavedDao.getAllFormSaved(VedantaUtility.getCurrentUserBuId(), status);
						} else {
							formSaved = formSavedDao.getAllFormSaved(VedantaUtility.getCurrentUserBuId(),
									VedantaUtility.getCurrentUserPlantCode(), status);
						}

					}

				}

				if (VedantaUtility.isCurrentUserBusinessUnitHead()) {
					if (status == Constants.ALL_SAVEFORM) {

						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							formSaved = formSavedDao.getAllFormSavedNotStatus(Constants.STATUS_DELETE);
						} else {
							formSaved = formSavedDao.getAllFormSavedNotStatus(VedantaUtility.getCurrentUserBuId(),
									Constants.STATUS_DELETE);
						}

					} else {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							formSaved = formSavedDao.getAllFormSaved(status);
						} else {
							formSaved = formSavedDao.getAllFormSaved(VedantaUtility.getCurrentUserBuId(), status);
						}

					}
				}

				formSaved.forEach(fSaved -> {

					if (!ObjectUtils.isEmpty(fSaved.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(fSaved.getSubmittedBy());

						if (!ObjectUtils.isEmpty(user)) {
							fSaved.setSubmittedNameBy(user.getName());
						}
					}

					if (!ObjectUtils.isEmpty(fSaved.getVendorId())) {
						Vendor vendor = vendorService.get(fSaved.getVendorId());

						if (!ObjectUtils.isEmpty(vendor)) {
							fSaved.setVendorName(vendor.getName());
						}
					}
				});

				return formSaved;
			}

			if (VedantaUtility.isCurrentUserPlantHead()) {

				String employeeId = vedantaUser.getEmployeeId();
				List<PlantHead> plantHeads = plantHeadDao.findAllByEmployeeIdAndStatus(employeeId,
						Constants.STATUS_ACTIVE);
				Set<String> plantCodes = new HashSet<>();
				for (PlantHead pH : plantHeads) {
					plantCodes.add(pH.getPlantCode());
				}
				if (!ObjectUtils.isEmpty(plantHeads) && plantHeads.size() > 0) {
					if (status == Constants.ALL_SAVEFORM) {

						formSaved = formSavedDao.getAllFormSavedByPlantCodesAndBusinessIdNotStatus(plantCodes,
								VedantaUtility.getCurrentUserBuId(), Constants.STATUS_DELETE);
					} else {
						formSaved = formSavedDao.getAllFormSavedByPlantCodesAndBusinessIdAndStatus(plantCodes,
								VedantaUtility.getCurrentUserBuId(), status);
					}

					formSaved.forEach(fSaved -> {

						if (!ObjectUtils.isEmpty(fSaved.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(fSaved.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								fSaved.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(fSaved.getVendorId())) {
							Vendor vendor = vendorService.get(fSaved.getVendorId());

							if (!ObjectUtils.isEmpty(vendor)) {
								fSaved.setVendorName(vendor.getName());
							}
						}
					});
				}

				return formSaved;
			}

			if (VedantaUtility.isCurrentUserHr()) {

				String employeeId = vedantaUser.getEmployeeId();
				List<HumanResource> humanResources = humanResourceDao.findAllByEmployeeIdAndStatus(employeeId,
						Constants.STATUS_ACTIVE);
				Set<String> plantCodes = new HashSet<>();
				for (HumanResource hr : humanResources) {
					plantCodes.add(hr.getPlantCode());
				}

				HashMap<String, Long> map = getFormDataByBusinessUnitIdAndCategoryAndSubCatgeory(
						VedantaUtility.getCurrentUserBuId());
				Long category = 0L;
				Long subCategory = 0L;
				if (!ObjectUtils.isEmpty(map)) {
					category = map.get("category");
					subCategory = map.get("subCatgeory");
				}

				if (!ObjectUtils.isEmpty(humanResources) && humanResources.size() > 0) {
					if (status == Constants.ALL_SAVEFORM) {

						formSaved = formSavedDao
								.getAllFormSavedByPlantCodesAndBusinessIdAndCategoryAndSubcategoryNotStatus(plantCodes,
										VedantaUtility.getCurrentUserBuId(), category, subCategory,
										Constants.STATUS_DELETE);
					} else {
						formSaved = formSavedDao
								.getAllFormSavedByPlantCodesAndBusinessIdAndCategoryAndSubcategoryAndStatus(plantCodes,
										VedantaUtility.getCurrentUserBuId(), category, subCategory, status);
					}

					formSaved.forEach(fSaved -> {

						if (!ObjectUtils.isEmpty(fSaved.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(fSaved.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								fSaved.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(fSaved.getVendorId())) {
							Vendor vendor = vendorService.get(fSaved.getVendorId());

							if (!ObjectUtils.isEmpty(vendor)) {
								fSaved.setVendorName(vendor.getName());
							}
						}
					});
				}

				return formSaved;
			}

			List<FormSaved> formSavedList = new ArrayList<>();

			if (VedantaUtility.isCurrentUserContractManager()) {

				if (status == Constants.ALL_SCORECARD) {
					formSavedList = formSavedDao.getFormSavedByManagerAndNotStatus(VedantaUtility.getCurrentUserBuId(),
							vedantaUser.getEmployeeId(), Constants.STATUS_DELETE);
				} else {
					formSavedList = formSavedDao.getFormSavedByManagerAndStatus(VedantaUtility.getCurrentUserBuId(),
							vedantaUser.getEmployeeId(), status);
				}

			}

			List<FormGroupUserMapper> formGroupUserMappers = formGroupUserDao
					.getDistinctContractNumbersAndManagersByUserId(vedantaUser.getEmployeeId());

			Set<String> contractNumbers = new HashSet<>();
			Set<String> contractManagerIds = new HashSet<>();
			Set<Long> formIds = new HashSet<>();

			if (!CollectionUtils.isEmpty(formGroupUserMappers)) {
				formGroupUserMappers.forEach(formGroupUserMapper -> {
					contractNumbers.add(formGroupUserMapper.getContractNumber());
					contractManagerIds.add(formGroupUserMapper.getContractManagerId());
					formIds.add(formGroupUserMapper.getFormId());
				});

				if (contractNumbers.size() > 0 && contractManagerIds.size() > 0) {
					if (status == Constants.ALL_SAVEFORM) {
						formSaved = formSavedDao.getFormSavedByManagersAndContractNumbersAndFormIdsNotStatus(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, formIds,
								Constants.STATUS_DELETE);
					} else {
						formSaved = formSavedDao.getFormSavedByManagersAndContractNumbersAndFormIds(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, formIds,
								status);
					}
				}
			}

			if (VedantaUtility.isCurrentUserContractManager() && formSavedList.size() > 0) {

				formSaved = Stream.concat(formSavedList.stream(), formSavedList.stream()).distinct()
						.collect(Collectors.toList());

			}

			formSaved.forEach(fSaved -> {

				if (!ObjectUtils.isEmpty(fSaved.getSubmittedBy())) {
					User user = userDetail.getUserByUserId(fSaved.getSubmittedBy());

					if (!ObjectUtils.isEmpty(user)) {
						fSaved.setSubmittedNameBy(user.getName());
					}
				}

				if (!ObjectUtils.isEmpty(fSaved.getVendorId())) {
					Vendor vendor = vendorService.get(fSaved.getVendorId());

					if (!ObjectUtils.isEmpty(vendor)) {
						fSaved.setVendorName(vendor.getName());
					}
				}

			});

			return formSaved;
		} catch (VedantaException e) {
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormSaved> getAllFormSaved() {

		try {
			List<FormSaved> formSaved = formSavedDao.findAll();
			return formSaved;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormSaved populate(FormSaved entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form object cannot be null/empty");
			throw new VedantaException("Form object cannot be null/empty");
		}

		try {
			return formSavedDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving formSaved information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormSaved> getComplianceFormByMonthAndStatus(String plantCode, String contractNumber, Long formId,
			int monthId, int yearId) {
		if (StringUtils.isEmpty(plantCode)) {
			log.info("Invalid plant code.");
			throw new VedantaException("Invalid plant code.");
		}
		if (StringUtils.isEmpty(contractNumber)) {
			log.info("Invalid contract number.");
			throw new VedantaException("Invalid contract number.");
		}
		if (formId <= 0) {
			log.info("Invalid form id.");
			throw new VedantaException("Invalid form id.");
		}
		if (monthId < 0) {
			log.info("Invalid month id.");
			throw new VedantaException("Invalid month id.");
		}
		if (yearId <= 0) {
			log.info("Invalid year id.");
			throw new VedantaException("Invalid year id.");
		}

		try {
			return formSavedDao.getComplianceFormByMonthAndStatus(plantCode, contractNumber, formId, monthId, yearId);
		} catch (VedantaException e) {
			String msg = "Error while saving formSaved information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	private List<Long> getComplianceByScoreCardId(Long scoreCardId) {

		ScoreCard scoreCard = scoreCardService.get(scoreCardId);

		if (ObjectUtils.isEmpty(scoreCard)) {
			log.info("scorecard cannot be null/empty");
			throw new VedantaException("scorecard cannot be null/empty");
		}

		try {
			Form form = null;
			List<FormSaved> formSavedList = null;
			FormSaved formSaved = null;
			List<TemplateForm> templateForms = templateFormDao.findByTemplateId(scoreCard.getTemplateId());

			if (!ObjectUtils.isEmpty(templateForms) && templateForms.size() > 0) {
				for (TemplateForm templateForm : templateForms) {
					formSavedList = this.getComplianceFormByMonthAndStatus(scoreCard.getPlantCode(),
							scoreCard.getContractNumber(), templateForm.getFormId(), scoreCard.getMonthId(),
							scoreCard.getYearId());
					if (!ObjectUtils.isEmpty(formSavedList) && formSavedList.size() > 0) {
						formSaved = formSavedList.get(0);
						if (!ObjectUtils.isEmpty(formSaved)) {
							if (formSaved.getStatus() == Constants.STATUS_APPROVED) {
								templateForm.setIsFormSaved(true);
							} else {
								templateForm.setIsFormSaved(false);
							}
						}
						templateForm.setFormSavedId(formSaved.getId());
					} else {
						templateForm.setIsFormSaved(false);
						templateForm.setFormSavedId(0L);
					}
					form = formService.get(templateForm.getFormId());
					if (!ObjectUtils.isEmpty(form)) {
						templateForm.setFormName(form.getName());
					}
				}
			}

			List<Long> complainceForm = new ArrayList<Long>();
			templateForms.forEach(templateForm -> {
				complainceForm.add(templateForm.getFormId());
			});

			return complainceForm;
		} catch (VedantaException e) {
			log.info("error while fetching Complains at formsave get by Id.");
			throw new VedantaException("error while fetching Complains at formsave get by Id.");
		}
	}

	@Override
	public Map<String, Object> getComplianceFormSavedFormMapByFormSavedId(Long formSavedId, Long scoreCardId) {
		if (formSavedId <= 0) {
			log.info("form saved number cannot be null/empty");
			throw new VedantaException("form saved number cannot be null/empty");
		}
		try {
			Map<String, Object> formSavedFormMap = new HashMap<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			FormSaved formSaved = this.get(formSavedId);
			List<Plant> plants = plantService.getAllPlantsByCurrentUser();

			List<User> users;
			if (!ObjectUtils.isEmpty(formSaved)) {
				users = userDetail.getAllUsersNotAdminAndBuId(formSaved.getBusinessUnitId());
				formSaved.setFormData(this.getFormDataFromFormSaved(formSaved));
				formSaved.setIsEnable(true);

				Contract contractDetails = contractService.findByContractNumber(formSaved.getContractNumber());
				if (!ObjectUtils.isEmpty(contractDetails)) {
					formSavedFormMap.put(Constants.CONTRACT, contractDetails);
					Form formDetail = formService.get(formSaved.getFormId());
					formDetail.setIsAllGroupAccess(false);

					if ((formSaved.getBusinessUnitId() == 1L && formSaved.getCategoryId() == 6L
							&& formSaved.getSubCategoryId() == 36L)
							|| (formSaved.getBusinessUnitId() == 2L && formSaved.getCategoryId() == 12L
									&& formSaved.getSubCategoryId() == 54L)
							|| (formSaved.getBusinessUnitId() == 7L && formSaved.getCategoryId() == 42L
									&& formSaved.getSubCategoryId() == 83L)
							|| (formSaved.getBusinessUnitId() == 8L && formSaved.getCategoryId() == 48L
									&& formSaved.getSubCategoryId() == 110L)
							|| (formSaved.getBusinessUnitId() == 9L && formSaved.getCategoryId() == 54L
									&& formSaved.getSubCategoryId() == 139L)
							|| (formSaved.getBusinessUnitId() == 5L && formSaved.getCategoryId() == 30L
									&& formSaved.getSubCategoryId() == 157L)
							|| (formSaved.getBusinessUnitId() == 4L && formSaved.getCategoryId() == 24L
									&& formSaved.getSubCategoryId() == 177L)
							|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 36L
									&& formSaved.getSubCategoryId() == 195L)
							|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 12L
									&& formSaved.getSubCategoryId() == 54L)
							|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 48L
									&& formSaved.getSubCategoryId() == 110L)) {
						formSaved.setIsCompliance(true);
					} else {
						formSaved.setIsCompliance(false);
					}

					if (authorities.contains(new SimpleGrantedAuthority("ROLE_HR"))) {
						formDetail.setIsApprover(true);
					} else {
						formDetail.setIsApprover(false);
					}

					formSavedFormMap.put(Constants.FORM_SAVED_DETAIL, formSaved);
					formSavedFormMap.put(Constants.USER_DETAIL, users);

					if (!ObjectUtils.isEmpty(formDetail)) {
						Boolean isGranted = isCurrentUserGranted();
						if (isGranted) {
							formDetail.setIsAllGroupAccess(true);
						}
						if (isCurrentUserPlantHead()) {

							if (formSaved.getBusinessUnitId() == 1L && formSaved.getFormId() == 20L
									&& formSaved.getCategoryId() == 6L && formSaved.getSubCategoryId() == 36L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 2L && formSaved.getFormId() == 30L
									&& formSaved.getCategoryId() == 12L && formSaved.getSubCategoryId() == 54L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 7L && formSaved.getFormId() == 65L
									&& formSaved.getCategoryId() == 42L && formSaved.getSubCategoryId() == 83L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 8L && formSaved.getFormId() == 66L
									&& formSaved.getCategoryId() == 48L && formSaved.getSubCategoryId() == 110L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 9L && formSaved.getFormId() == 85L
									&& formSaved.getCategoryId() == 54L && formSaved.getSubCategoryId() == 139L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 5L && formSaved.getFormId() == 45L
									&& formSaved.getCategoryId() == 30L && formSaved.getSubCategoryId() == 157L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 4L && formSaved.getFormId() == 36L
									&& formSaved.getCategoryId() == 24L && formSaved.getSubCategoryId() == 177L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							} else if (formSaved.getBusinessUnitId() == 6L && formSaved.getFormId() == 55L
									&& formSaved.getCategoryId() == 36L && formSaved.getSubCategoryId() == 195L
									&& plants.stream()
											.anyMatch(plant -> plant.getPlantCode().equals(formSaved.getPlantCode()))) {
								// formDetail.setIsApprover(true);
								isGranted = true;
							}
						}

						List<FormGroup> formGroups = new ArrayList<>();

						if (isCurrentUserContractManager()) {
							if (formSaved.getContractManagerId().equals(vedantaUser.getEmployeeId())) {
								isGranted = true;
								formDetail.setIsAllGroupAccess(true);
								formDetail.setIsContractManager(true);
							} else {
								formDetail.setIsContractManager(false);
							}

						}
						
						if (isCurrentUserHr()) {
							isGranted = true;
							formDetail.setIsAllGroupAccess(true);
					}

						if (!isGranted) {
							if ((formSaved.getBusinessUnitId() == 1L && formSaved.getCategoryId() == 6L
									&& formSaved.getSubCategoryId() == 36L)
									|| (formSaved.getBusinessUnitId() == 2L && formSaved.getCategoryId() == 12L
											&& formSaved.getSubCategoryId() == 54L)
									|| (formSaved.getBusinessUnitId() == 7L && formSaved.getCategoryId() == 42L
											&& formSaved.getSubCategoryId() == 83L)
									|| (formSaved.getBusinessUnitId() == 8L && formSaved.getCategoryId() == 48L
											&& formSaved.getSubCategoryId() == 110L)
									|| (formSaved.getBusinessUnitId() == 9L && formSaved.getCategoryId() == 54L
											&& formSaved.getSubCategoryId() == 139L)
									|| (formSaved.getBusinessUnitId() == 5L && formSaved.getCategoryId() == 30L
											&& formSaved.getSubCategoryId() == 157L)
									|| (formSaved.getBusinessUnitId() == 4L && formSaved.getCategoryId() == 24L
											&& formSaved.getSubCategoryId() == 177L)
									|| (formSaved.getBusinessUnitId() == 6L && formSaved.getCategoryId() == 36L
											&& formSaved.getSubCategoryId() == 195L)) {

								List<ScoreCardGroupUser> scorecardGroupUsers = scoreCardGroupUserService
										.getScoreCardGroupUsersByEmployeeIdOrContractManagerIdAndAllGroups(
												vedantaUser.getEmployeeId(), vedantaUser.getEmployeeId());
								if ((!ObjectUtils.isEmpty(scorecardGroupUsers) && scorecardGroupUsers.size() > 0)) {
									isGranted = true;
									formDetail.setIsAllGroupAccess(true);
									formDetail.setIsContractManager(true);
								}
							}
						}

						List<Long> complainceList = getComplianceByScoreCardId(scoreCardId);
						if (complainceList.contains(formSaved.getFormId())) {
							ScoreCard scoreCard = scoreCardService.get(scoreCardId);

							List<ScoreCardGroupUser> scorecardGroupUser = scoreCardGroupUserService
									.getScoreCardGroupUsersByEmployeeIdAndContractNumberAndBusinessUnitIdAndTemplateIdAndAllGroups(
											scoreCard.getContractNumber(), vedantaUser.getEmployeeId(),
											scoreCard.getBusinessUnitId(), scoreCard.getTemplateId());
							if ((!ObjectUtils.isEmpty(scorecardGroupUser) && scorecardGroupUser.size() > 0)
									|| scoreCard.getContractManagerId().equals(vedantaUser.getEmployeeId())) {
								isGranted = true;
								formDetail.setIsAllGroupAccess(true);
							}
						}

						if (isGranted) {

							User user = userDetail.getUserByUserId(vedantaUser.getId());
							if (!ObjectUtils.isEmpty(user)) {

								List<FormGroupUser> userAssignedChecked = formGroupUserDao
										.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
												formSaved.getContractManagerId(), formDetail.getId(), 0L);

								String assignedUserName = getAssignedUserName(userAssignedChecked);

								if (assignedUserName.equals("none")) {
									formDetail.setUserName(user.getName());
								} else {
									formDetail.setUserName(assignedUserName);
								}

							} else {
								formDetail.setUserName("");
							}

							formGroups = formGroupService.getAllFormGroupsByFormId(formDetail.getId());
						} else {

							List<FormGroupUser> userAssigned = formGroupUserDao.getFormGroupUserForGroupAndForm(
									formSaved.getContractNumber(), formSaved.getContractManagerId(), formDetail.getId(),
									0L);

							if (!ObjectUtils.isEmpty(userAssigned) && userAssigned.size() > 0) {

								User user = userDetail.getUserByEmployeeId(userAssigned.get(0).getEmployeeId());
								if (!ObjectUtils.isEmpty(user)) {

									if (user.getId() == vedantaUser.getId()) {
										formDetail.setIsAllGroupAccess(true);
									}

									List<FormGroupUser> userAssignedChecked = formGroupUserDao
											.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
													formSaved.getContractManagerId(), formDetail.getId(), 0L);

									String assignedUserName = getAssignedUserName(userAssignedChecked);

									if (assignedUserName.equals("none")) {
										formDetail.setUserName(user.getName());
									} else {
										formDetail.setUserName(assignedUserName);
									}

								} else {
									formDetail.setUserName("");
								}
							} else {
								List<FormGroupUser> userAssignedChecked = formGroupUserDao
										.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
												formSaved.getContractManagerId(), formDetail.getId(), 0L);

								String assignedUserName = getAssignedUserName(userAssignedChecked);

								if (assignedUserName.equals("none")) {
									formDetail.setUserName(formSaved.getContractManagerName());
								} else {
									formDetail.setUserName(assignedUserName);
								}

							}

							List<FormGroupUser> formGroupUsers = formGroupUserDao.getFormGroupUserForUserAndForm(
									formSaved.getContractNumber(), formDetail.getId(), vedantaUser.getEmployeeId());
							Set<Long> groupIds = new HashSet<>();
							if (!CollectionUtils.isEmpty(formGroupUsers)) {
								formGroupUsers.forEach(formGroupUser -> {
									if (formGroupUser.getFormGroupDetailId() > 0) {
										formSaved.setIsEnable(false);
										groupIds.add(formGroupUser.getFormGroupDetailId());
									} else {
										formSaved.setIsEnable(true);
									}
								});
								if ((groupIds.size() > 0)) {
									formGroups = formGroupService.getAllFormGroupsInGroupsId(groupIds);

								} else {
									formGroups = formGroupService.getAllFormGroupsByFormId(formDetail.getId());
								}
								if (!ObjectUtils.isEmpty(formGroups) && formGroups.size() > 0) {
									for (FormGroup fGroup : formGroups) {
										if (fGroup.getFormId() != formSaved.getFormId()) {
											formGroups.remove(fGroup);
										}
									}
								}
							}
						}

						formSavedFormMap.put(Constants.FORM, formDetail);

						if (!CollectionUtils.isEmpty(formGroups)) {
							formGroups.forEach(formGroup -> {
								FormGroupDetail groupDetail = formGroupDetailService
										.get(formGroup.getFormGroupDetailId());
								List<FormField> groupFieldList = new ArrayList<>();
								List<FormGroupField> groupFields = formGroupFieldService
										.getAllFormGroupFieldsByGroupId(groupDetail.getId());
								groupFields.forEach(groupField -> {
									FormField fieldDetail = formFieldService
											.getFormFieldDetail(groupField.getFormFieldId());
									groupFieldList.add(fieldDetail);
								});
								groupDetail.setFormFields(groupFieldList);

								List<FormGroupUser> userAssignedGroup = formGroupUserDao
										.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
												formSaved.getContractManagerId(), formDetail.getId(),
												groupDetail.getId());

								/*
								 * if(isCurrentUserGranted() && !ObjectUtils.isEmpty(formDetail)){
								 * groupDetail.setUserName(formDetail.getUserName()); }else{
								 * 
								 * }
								 */

								if (!ObjectUtils.isEmpty(userAssignedGroup) && userAssignedGroup.size() > 0) {
									User user = userDetail
											.getUserByEmployeeId(userAssignedGroup.get(0).getEmployeeId());
									if (!ObjectUtils.isEmpty(user))
										groupDetail.setUserName(user.getName());
									else
										groupDetail.setUserName("");
								} else {

									List<FormGroupUser> userAssignedChecked = formGroupUserDao
											.getFormGroupUserForGroupAndForm(formSaved.getContractNumber(),
													formSaved.getContractManagerId(), formDetail.getId(), 0L);

									String assignedUserName = getAssignedUserName(userAssignedChecked);

									if (assignedUserName.equals("none")) {
										groupDetail.setUserName(formDetail.getUserName());
									} else {
										groupDetail.setUserName(assignedUserName);
									}

								}

								formGroup.setFormGroupDetail(groupDetail);
							});
							formSavedFormMap.put(Constants.FORM_GROUPS, formGroups);
						}
					}
				}
			}

			return formSavedFormMap;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormSaved> getFormSavedByBuIdAndPlantCodeAndStatus(Long bussinessUnitId, String plantCode, int status) {

		List<FormSaved> formSavedList = new ArrayList<>();
		if (status == Constants.ALL_SAVEFORM) {
			formSavedList = formSavedDao.getFormSavedByBuIdAndPlantCode(bussinessUnitId, plantCode);
			formSavedList.forEach(fSaved -> {

				if (!ObjectUtils.isEmpty(fSaved.getSubmittedBy())) {
					User user = userDetail.getUserByUserId(fSaved.getSubmittedBy());

					if (!ObjectUtils.isEmpty(user)) {
						fSaved.setSubmittedNameBy(user.getName());
					}
				}

				if (!ObjectUtils.isEmpty(fSaved.getVendorId())) {
					Vendor vendor = vendorService.get(fSaved.getVendorId());

					if (!ObjectUtils.isEmpty(vendor)) {
						fSaved.setVendorName(vendor.getName());
					}
				}
			});

		} else {
			formSavedList = formSavedDao.getFormSavedByBuIdAndPlantCodeAndStatus(bussinessUnitId, plantCode, status);
			formSavedList.forEach(fSaved -> {

				if (!ObjectUtils.isEmpty(fSaved.getSubmittedBy())) {
					User user = userDetail.getUserByUserId(fSaved.getSubmittedBy());

					if (!ObjectUtils.isEmpty(user)) {
						fSaved.setSubmittedNameBy(user.getName());
					}
				}

				if (!ObjectUtils.isEmpty(fSaved.getVendorId())) {
					Vendor vendor = vendorService.get(fSaved.getVendorId());

					if (!ObjectUtils.isEmpty(vendor)) {
						fSaved.setVendorName(vendor.getName());
					}
				}
			});
		}

		return formSavedList;
	}

	@Override
	public FormSaved getFormSavedByBusinessUnitIdCategoryIdAndSubCategoryIdAndMonthIdAndYearId(String contractNumber,
			Long businessUnitId, int monthId, int yearId, Long categoryId, Long subCategoryId) {
		return complianceDao.getFormSaved(contractNumber, businessUnitId, monthId, yearId, categoryId, subCategoryId);
	}

	@Override
	public List<User> getAllUserNotAdminAndHrByLimit(Long buId) {
		List<User> userDetails = null;
		try {

			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
			authorties.add(Constants.ROLE_HR);
			Pageable topTen = new PageRequest(0, 10);
			userDetails = formSavedDao.getAllUserNotAdminAndHrByLimit(buId, authorties, Constants.STATUS_DELETE,
					topTen);

		} catch (VedantaException e) {
			log.error("Error while fetching user details.");
		}
		return userDetails;
	}

	@Override
	public List<User> getAllHrUserByLimit(Long buId) {
		List<User> userDetails = null;
		try {
			Pageable topTen = new PageRequest(0, 10);
			userDetails = formSavedDao.getAllHrUserByLimit(buId, Constants.ROLE_HR, Constants.STATUS_DELETE, topTen);
		} catch (VedantaException e) {
			log.error("Error while fetching user details.");
		}
		return userDetails;
	}

	@Override
	public List<User> getAllUserNotAdminAndHrByName(String name, Long buId) {

		List<User> userDetails = null;
		try {
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
			authorties.add(Constants.ROLE_HR);
			userDetails = formSavedDao.getAllUserNotAdminAndHrByName(buId, authorties, Constants.STATUS_DELETE, name);
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	@Override
	public List<User> getAllHrUserByName(String name, Long buId) {

		List<User> userDetails = null;
		try {
			userDetails = formSavedDao.getAllUserNotAdminAndHrByName(buId, Constants.ROLE_HR, Constants.STATUS_DELETE,
					name);
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	private HashMap<String, Long> getFormDataByBusinessUnitIdAndCategoryAndSubCatgeory(Long businessUnitId) {

		HashMap<String, Long> map = new HashMap<String, Long>();

		// HZL_BUSINESS_UNIT
		if (businessUnitId == 1) {
			map.put("category", Constants.HZL_DEFAULT_CATEGORY_ID);
			map.put("subCatgeory", Constants.HZL_DEFAULT_SUB_CATEGORY_ID);

		}
		// STERLITE_BUSINESS_UNIT
		if (businessUnitId == 2) {
			map.put("category", Constants.STERLITE_DEFAULT_CATEGORY_ID);
			map.put("subCatgeory", Constants.STERLITE_DEFAULT_SUB_CATEGORY_ID);

		}
		// CAIRN_BUSINESS_UNIT
		if (businessUnitId == 3) {
			map.put("category", Constants.CAIRN_DEFAULT_CATEGORY_ID);
			map.put("subCatgeory", Constants.CAIRN_DEFAULT_SUB_CATEGORY_ID);

		}
		// SESA_BUSINESS_UNIT
		if (businessUnitId == 4) {
			map.put("category", Constants.SESA_DEFAULT_CATEGORY_ID);
			map.put("subCatgeory", Constants.SESA_DEFAULT_SUB_CATEGORY_ID);

		}
		// VGCB_BUSINESS_UNIT
		if (businessUnitId == 5) {
			map.put("category", Constants.VGCB_DEFAULT_CATEGORY_ID);
			map.put("subCatgeory", Constants.VGCB_DEFAULT_SUB_CATEGORY_ID);

		}
		// FUJAIRAH_BUSINESS_UNIT
		if (businessUnitId == 6) {
			map.put("category", Constants.FUJAIRAH_DEFAULT_CATEGORY_ID);
			map.put("subCatgeory", Constants.FUJAIRAH_DEFAULT_SUB_CATEGORY_ID);

		}
		// BALCO_BUSINESS_UNIT
		if (businessUnitId == 7) {
			map.put("category", Constants.BALCO_DEFAULT_CATEGORY_ID);
			map.put("subCategory", Constants.BALCO_DEFAULT_SUB_CATEGORY_ID);

		}
		// JHARSUGUDA_BUSINESS_UNIT
		if (businessUnitId == 8) {
			map.put("category", Constants.JHARSUGDA_DEFAULT_CATEGORY_ID);
			map.put("subCategory", Constants.JHARSUGDA_DEFAULT_SUB_CATEGORY_ID);

		}
		// VALLANJIGARH_BUSINESS_UNIT
		if (businessUnitId == 9) {
			map.put("category", Constants.VAL_DEFAULT_CATEGORY_ID);
			map.put("subCategory", Constants.VAL_DEFAULT_SUB_CATEGORY_ID);

		}
		/*
		 * //TSPL_BUSINESS_UNIT if (businessUnitId == 10) { map.put("category",
		 * Constants.HZL_DEFAULT_CATEGORY_ID); map.put("subCategory",
		 * Constants.HZL_DEFAULT_SUB_CATEGORY_ID);
		 * 
		 * } //KCM_BUSINESS_UNIT if (businessUnitId == 11) { map.put("category",
		 * Constants.HZL_DEFAULT_CATEGORY_ID); map.put("subCategory",
		 * Constants.HZL_DEFAULT_SUB_CATEGORY_ID);
		 * 
		 * } //ZI_BUSINESS_UNIT if (businessUnitId == 12) { map.put("category",
		 * Constants.HZL_DEFAULT_CATEGORY_ID); map.put("subCategory",
		 * Constants.HZL_DEFAULT_SUB_CATEGORY_ID);
		 * 
		 * }
		 */

		return map;

	}

}
