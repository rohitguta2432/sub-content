package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.ListUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.mapper.FormGroupFieldMapper;
import com.vedanta.vpmt.mapper.FormGroupMapper;
import com.vedanta.vpmt.model.DataUnit;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.FormField;
import com.vedanta.vpmt.model.FormGroup;
import com.vedanta.vpmt.model.FormGroupDetail;
import com.vedanta.vpmt.model.FormGroupField;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 13/09/17.
 */
@Component
@Slf4j
public class FormService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public List<Form> getAllForms() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_FORMS, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Form>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form listing");
			throw new VedantaWebException("Error while fetching form listing");
		}
	}

	public Form getFormDetail(long formId) {

		int status;
		String url = String.format(URLConstants.GET_FORM_DETAIL, formId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Form>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}
			log.error("Error while fetching form information");
			throw new VedantaWebException("Error while fetching form information");
		}
	}

	public Form saveForm(Form form) {
		if (ObjectUtils.isEmpty(form)) {
			throw new VedantaWebException("form info cannot be empty");
		}
		int status;
		try {
			if (form.getStatus() < Constants.STATUS_ACTIVE) {
				form.setStatus(Constants.STATUS_ACTIVE);
			}
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_FORM_DETAIL, form, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving form.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Form>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}
			log.error("Error while saving form information");
			throw new VedantaWebException("Error while saving form information");
		}
	}

	public Form deleteForm(long formId) {
		if (formId <= 0) {
			log.info("Invalid form id.");
			throw new VedantaWebException("Invalid form id.");
		}
		Form form = this.getFormDetail(formId);
		if (ObjectUtils.isEmpty(form)) {
			throw new VedantaWebException("Invalid form id, no form found by id provided.");
		}
		try {
			if (form.getStatus() != Constants.STATUS_DELETE) {
				form.setStatus(Constants.STATUS_DELETE);
			}
			Form deleteForm = this.saveForm(form);
			return deleteForm;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while deleting template information");
			throw new VedantaWebException("Error while deleting template information");
		}
	}

	public List<FormField> getAllFormFields() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_FORM_FIELDS, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form field listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<FormField>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form field listing");
			throw new VedantaWebException("Error while fetching form field listing");
		}
	}

	public FormField getFieldDetail(long fieldId) {

		int status;
		String url = String.format(URLConstants.GET_FORM_FIELD_DETAIL, fieldId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form field information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormField>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form field information");
			throw new VedantaWebException("Error while fetching form field information");
		}
	}

	public void validateField(FormField field) {
		field.getFormValidations()
				.removeIf(validation -> (validation.getDataType() == null || validation.getDataType().equals("NA")));
		field.getFormTargetValue()
				.removeIf(targetValue -> (targetValue.getValue() == null || targetValue.getValue().equals("NA")));
		if (!ListUtils.isEmpty(field.getFormFieldOptions())) {
			field.getFormFieldOptions().removeIf(fieldOption -> fieldOption.getOptionName() == null);
		}
	}

	public FormField saveField(FormField field) {

		if (ObjectUtils.isEmpty(field)) {
			throw new VedantaWebException("form field info cannot be empty");
		}
		int status;
		try {
			validateField(field);

			if (field.getStatus() < Constants.STATUS_ACTIVE) {
				field.setStatus(Constants.STATUS_ACTIVE);
			}
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_FORM_FIELD_DETAIL, field, null,
					HttpMethod.POST);

			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving field information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormField>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while saving form field information");
			throw new VedantaWebException("Error while saving form field information");
		}
	}

	public FormField deleteField(long fieldId) {
		if (fieldId <= 0) {
			log.info("Invalid field id.");
			throw new VedantaWebException("Invalid field id.");
		}
		FormField field = this.getFieldDetail(fieldId);
		if (ObjectUtils.isEmpty(field)) {
			throw new VedantaWebException("Invalid form field id, no field found by id provided.");
		}
		try {
			if (field.getStatus() != Constants.STATUS_DELETE) {
				field.setStatus(Constants.STATUS_DELETE);
			}
			FormField deletedField = this.saveField(field);
			return deletedField;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while deleting form field information");
			throw new VedantaWebException("Error while deleting form field information");
		}
	}

	public List<FormGroupDetail> getAllGroups() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_FORM_GROUPS_DETAIL, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form group detail listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<FormGroupDetail>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form group listing");
			throw new VedantaWebException("Error while fetching form group listing");
		}
	}

	public FormGroupDetail getGroupDetail(long groupId) {
		int status;
		String url = String.format(URLConstants.GET_FORM_GROUP_DETAIL, groupId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormGroupDetail>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form group detail information");
			throw new VedantaWebException("Error while fetching form group detail information");
		}
	}

	public FormGroupDetail saveGroup(FormGroupDetail group) {

		if (ObjectUtils.isEmpty(group)) {
			throw new VedantaWebException("form group detail info cannot be empty");
		}
		int status;
		try {
			if (group.getStatus() < Constants.STATUS_ACTIVE) {
				group.setStatus(Constants.STATUS_ACTIVE);
			}
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_FORM_GROUP_DETAIL, group, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving group information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormGroupDetail>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while saving form group detail information");
			throw new VedantaWebException("Error while form group detail information");
		}
	}

	public FormGroupDetail deleteGroup(long groupId) {
		if (groupId <= 0) {
			log.info("Invalid form group detail id.");
			throw new VedantaWebException("Invalid Form group detail id.");
		}
		FormGroupDetail group = this.getGroupDetail(groupId);
		if (ObjectUtils.isEmpty(group)) {
			throw new VedantaWebException("Invalid form group detail id, no group found by id provided.");
		}
		try {
			if (group.getStatus() != Constants.STATUS_DELETE) {
				group.setStatus(Constants.STATUS_DELETE);
			}
			FormGroupDetail deletedGroup = this.saveGroup(group);
			return deletedGroup;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while deleting group information");
			throw new VedantaWebException("Error while deleting group information");
		}
	}

	// Group Field methods
	public List<FormGroupFieldMapper> getAllGroupFields() {

		int status;
		List<FormGroupFieldMapper> groupFieldMapperList = new ArrayList<>();
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_FORM_GROUP_FIELD, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group field listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			List<FormGroupField> groupFieldList = OBJECT_MAPPER.readValue(data,
					new TypeReference<List<FormGroupField>>() {
					});

			Map<Long, List<FormGroupField>> groupFieldMap = new HashMap<>();

			List<FormGroupDetail> groups = this.getAllGroups();
			Map<Long, String> groupMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(groups)) {
				groups.forEach(group -> {
					groupMap.put(group.getId(), group.getName());
				});
			}

			groupFieldList.forEach(groupField -> {
				List<FormGroupField> groupFieldListTobeSaved = new ArrayList<>();
				if (groupFieldMap.containsKey(groupField.getFormGroupDetailId())) {
					groupFieldListTobeSaved = groupFieldMap.get(groupField.getFormGroupDetailId());
					groupFieldListTobeSaved.add(groupField);
				} else {
					groupFieldListTobeSaved.add(groupField);
				}
				groupFieldMap.put(groupField.getFormGroupDetailId(), groupFieldListTobeSaved);
			});

			groupFieldMap.forEach((groupId, groupFieldListFromMap) -> {
				FormGroupFieldMapper groupFieldMapper = new FormGroupFieldMapper();
				groupFieldMapper.setGroupId(groupId);
				groupFieldMapper.setGroupName(groupMap.get(groupId));
				groupFieldMapper.setGroupFields(groupFieldListFromMap);
				groupFieldMapperList.add(groupFieldMapper);
			});
			return groupFieldMapperList;
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form group field listing");
			throw new VedantaWebException("Error while fetching form group field listing");
		}
	}

	public FormGroupField getGroupFieldDetail(long groupFieldId) {
		int status;
		String url = String.format(URLConstants.GET_FORM_GROUP_FIELDS_DETAIL, groupFieldId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group field detail information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormGroupField>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form group field detail information");
			throw new VedantaWebException("Error while fetching form group field detail information");
		}
	}

	public List<FormGroupField> getGroupFields(long groupId) {
		int status;
		String url = String.format(URLConstants.GET_FORM_GROUP_FIELDS_BY_GROUP_ID, groupId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form group field information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<FormGroupField>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form group field information");
			throw new VedantaWebException("Error while fetching form group field information");
		}
	}

	public FormGroupFieldMapper getFormGroupFieldMapper(List<FormGroupField> formGroupFields) {

		try {
			FormGroupFieldMapper formGroupFieldMapper = new FormGroupFieldMapper();
			if (!ObjectUtils.isEmpty(formGroupFields) && formGroupFields.size() > 0) {
				formGroupFieldMapper.setGroupId(formGroupFields.get(0).getFormGroupDetailId());
				formGroupFieldMapper.setGroupFields(formGroupFields);
			}
			return formGroupFieldMapper;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching group field information");
			throw new VedantaWebException("Error while fetching group field information");
		}
	}

	public Boolean saveGroupFields(FormGroupFieldMapper groupFieldMapper) {

		if (ObjectUtils.isEmpty(groupFieldMapper)) {
			throw new VedantaWebException("form group field mapper cannot be empty");
		}
		int status;
		try {

			validateGroupFields(groupFieldMapper);

			List<FormGroupField> groupFieldListToSave = new ArrayList<>();
			groupFieldMapper.getGroupFields().forEach(groupField -> {
				groupField.setFormGroupDetailId(groupFieldMapper.getGroupId());
				if (groupField.getStatus() < Constants.STATUS_ACTIVE) {
					groupField.setStatus(Constants.STATUS_ACTIVE);
				}
				groupFieldListToSave.add(groupField);
			});

			String url = String.format(URLConstants.SAVE_FORM_GROUP_FIELD_LIST, groupFieldMapper.getGroupId());
			JsonNode response = restServiceUtil.makeRequest(url, groupFieldListToSave, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving form group field list information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while saving form group field list information");
			throw new VedantaWebException("Error while saving form group field list information");
		}
	}

	private void validateGroupFields(FormGroupFieldMapper groupFieldMapper) {
		if (ObjectUtils.isEmpty(groupFieldMapper) || ObjectUtils.isEmpty(groupFieldMapper.getGroupFields())
				|| groupFieldMapper.getGroupFields().size() <= 0) {
			log.error("Error while saving group field list information");
			throw new VedantaWebException("Error while group field list information");
		}

		List<FormGroupField> groupFields = groupFieldMapper.getGroupFields();

		List<FormGroupField> newGroupFields = new ArrayList<FormGroupField>();
		for (FormGroupField gFld : groupFields) {
			if (!ObjectUtils.isEmpty(gFld.getFormFieldId())) {
				newGroupFields.add(gFld);
			}
		}
		groupFieldMapper.setGroupFields(newGroupFields);
	}

	public Boolean deleteGroupFields(long groupId) {
		if (groupId <= 0) {
			log.info("Invalid group id.");
			throw new VedantaWebException("Invalid group id.");
		}
		int status;
		try {
			String url = String.format(URLConstants.DELETE_FORM_GROUP_FIELD_LIST, groupId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving form group field list information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while deleting group information");
			throw new VedantaWebException("Error while deleting group information");
		}
	}

	// Form Groups methods
	public List<FormGroupMapper> getAllFormGroups() {

		int status;
		List<FormGroupMapper> formGroupMapperList = new ArrayList<>();
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_FORM_GROUPS, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form group listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			List<FormGroup> formGroupList = OBJECT_MAPPER.readValue(data, new TypeReference<List<FormGroup>>() {
			});
			Map<Long, List<FormGroup>> formGroupsMap = new HashMap<>();

			List<Form> forms = this.getAllForms();
			Map<Long, String> formMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(forms)) {
				forms.forEach(form -> {
					formMap.put(form.getId(), form.getName());
				});
			}
			formGroupList.forEach(formGroup -> {
				List<FormGroup> formGroupsListTobeSaved = new ArrayList<>();

				if (formGroupsMap.containsKey(formGroup.getFormId())) {
					formGroupsListTobeSaved = formGroupsMap.get(formGroup.getFormId());
					formGroupsListTobeSaved.add(formGroup);
				} else {
					formGroupsListTobeSaved.add(formGroup);
				}
				formGroupsMap.put(formGroup.getFormId(), formGroupsListTobeSaved);
			});
			formGroupsMap.forEach((formId, formGroupListFromMap) -> {
				FormGroupMapper formGroupMapper = new FormGroupMapper();
				formGroupMapper.setFormId(formId);
				formGroupMapper.setFormName(formMap.get(formId));
				formGroupMapper.setFormGroups(formGroupListFromMap);
				formGroupMapperList.add(formGroupMapper);
			});
			return formGroupMapperList;
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form groups listing");
			throw new VedantaWebException("Error while form template groups listing");
		}
	}

	public FormGroup getFormGroupDetail(long formGroupId) {
		int status;
		String url = String.format(URLConstants.GET_FORM_GROUP_BY_FORM_GROUP_ID, formGroupId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template group detail information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<FormGroup>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form group detail information");
			throw new VedantaWebException("Error while fetching form group detail information");
		}
	}

	public List<FormGroup> getFormGroups(long formId) {
		int status;
		String url = String.format(URLConstants.GET_FORM_GROUPS_BY_FORM_ID, formId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching form groups information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<FormGroup>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching form groups information");
			throw new VedantaWebException("Error while fetching form groups information");
		}
	}

	public FormGroupMapper getGroupFieldMapper(List<FormGroup> formGroups) {

		try {
			FormGroupMapper formGroupMapper = new FormGroupMapper();
			if (!ObjectUtils.isEmpty(formGroups) && formGroups.size() > 0) {
				formGroupMapper.setFormId(formGroups.get(0).getFormId());
				formGroupMapper.setFormGroups(formGroups);
			}
			return formGroupMapper;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching group field information");
			throw new VedantaWebException("Error while fetching group field information");
		}
	}

	public Boolean saveFormGroups(FormGroupMapper formGroupMapper) {

		if (ObjectUtils.isEmpty(formGroupMapper)) {
			throw new VedantaWebException("form group mapper cannot be empty");
		}
		int status;

		validateToSaveFormGroupMapper(formGroupMapper);

		try {
			List<FormGroup> formGroupListToSave = new ArrayList<>();
			formGroupMapper.getFormGroups().forEach(formGroup -> {
				formGroup.setFormId(formGroupMapper.getFormId());
				formGroupListToSave.add(formGroup);
			});

			String url = String.format(URLConstants.SAVE_FORM_GROUP_LIST, formGroupMapper.getFormId());
			JsonNode response = restServiceUtil.makeRequest(url, formGroupListToSave, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving form group field list information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while saving form group list information");
			throw new VedantaWebException("Error while saving form group list information");
		}
	}

	private void validateToSaveFormGroupMapper(FormGroupMapper formGroupMapper) {

		if (ObjectUtils.isEmpty(formGroupMapper) || ObjectUtils.isEmpty(formGroupMapper.getFormGroups())
				|| formGroupMapper.getFormGroups().size() <= 0) {
			log.error("Error while saving form group list information");
			throw new VedantaWebException("Error while saving form group list information");
		}
		List<FormGroup> formGroups = new ArrayList<FormGroup>();

		for (FormGroup formGroup : formGroupMapper.getFormGroups()) {
			if (!ObjectUtils.isEmpty(formGroup.getFormGroupDetailId())) {
				formGroups.add(formGroup);
			}
		}
		formGroupMapper.setFormGroups(formGroups);
	}

	public Boolean deleteFormGroups(long formId) {
		if (formId <= 0) {
			log.info("Invalid form id.");
			throw new VedantaWebException("Invalid form id.");
		}
		int status;
		try {
			String url = String.format(URLConstants.DELETE_FORM_GROUPS_BY_FORM_ID, formId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while deleting form groups by template id.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while deleting form group information");
			throw new VedantaWebException("Error while deleting form group information");
		}
	}

	public List<DataUnit> getAllDataUnits() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_DATA_UNITS, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching data units.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<DataUnit>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching form listing", 401);
			}

			log.error("Error while fetching field listing");
			throw new VedantaWebException("Error while fetching field listing");
		}
	}

}
