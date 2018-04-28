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
import com.vedanta.vpmt.mapper.GroupFieldMapper;
import com.vedanta.vpmt.mapper.TemplateGroupMapper;
import com.vedanta.vpmt.model.DataUnit;
import com.vedanta.vpmt.model.Field;
import com.vedanta.vpmt.model.Group;
import com.vedanta.vpmt.model.GroupField;
import com.vedanta.vpmt.model.Template;
import com.vedanta.vpmt.model.TemplateForm;
import com.vedanta.vpmt.model.TemplateGroup;
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
public class TemplateService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public List<Template> getAllTemplates() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_TEMPLATES, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Template>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching template listing", 401);
			}

			log.error("Error while fetching template listing");
			throw new VedantaWebException("Error while fetching template listing");
		}
	}

	public Template getTemplateDetail(long templateId) {

		int status;
		String url = String.format(URLConstants.GET_TEMPLATE_DETAIL, templateId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Template>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching template information", 401);
			}
			log.error("Error while fetching template information");
			throw new VedantaWebException("Error while fetching template information");
		}
	}

	public Template saveTemplate(Template template) {
		if (ObjectUtils.isEmpty(template)) {
			throw new VedantaWebException("template info cannot be empty");
		}

		setMultiFormsToTemplateOnSave(template);
		int status;
		try {
			if (template.getStatus() < Constants.STATUS_ACTIVE) {
				template.setStatus(Constants.STATUS_ACTIVE);
			}
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_TEMPLATE_DETAIL, template, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving template template.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Template>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while saving template information", 401);
			}
			log.error("Error while saving template information");
			throw new VedantaWebException("Error while saving template information");
		}
	}

	private void setMultiFormsToTemplateOnSave(Template template) {

		if (!ObjectUtils.isEmpty(template.getForms()) && template.getForms().size() > 0) {
			List<TemplateForm> templateFormList = new ArrayList<TemplateForm>();
			for (Long formId : template.getForms()) {
				TemplateForm tf = new TemplateForm();
				tf.setFormId(formId);
				templateFormList.add(tf);
			}
			template.setTemplateForms(templateFormList);
		}
	}

	public Template deleteTemplate(long templateId) {
		if (templateId <= 0) {
			log.info("Invalid template id.");
			throw new VedantaWebException("Invalid template id.");
		}
		Template template = this.getTemplateDetail(templateId);
		if (ObjectUtils.isEmpty(template)) {
			throw new VedantaWebException("Invalid template id, no template found by id provided.");
		}
		try {
			if (template.getStatus() != Constants.STATUS_DELETE) {
				template.setStatus(Constants.STATUS_DELETE);
			}
			Template deleteTemplate = this.saveTemplate(template);
			return deleteTemplate;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while deleting template information", 401);
			}
			log.error("Error while deleting template information");
			throw new VedantaWebException("Error while deleting template information");
		}
	}

	public List<Field> getAllFields() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_FIELDS, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching field listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Field>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching field listing", 401);
			}
			log.error("Error while fetching field listing");
			throw new VedantaWebException("Error while fetching field listing");
		}
	}

	public Field getFieldDetail(long fieldId) {

		int status;
		String url = String.format(URLConstants.GET_FIELD_DETAIL, fieldId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching field information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Field>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching field information", 401);
			}
			log.error("Error while fetching field information");
			throw new VedantaWebException("Error while fetching field information");
		}
	}

	public void validateField(Field field) {
		if (!ListUtils.isEmpty(field.getScoringCriteria())) {

			field.getScoringCriteria().removeIf(scoringCriteria -> (scoringCriteria.getOperator() == null
					|| scoringCriteria.getOperator().equals("NA")));
			field.getValidations().removeIf(
					validation -> (validation.getDataType() == null || validation.getDataType().equals("NA")));
			field.getTargetValue()
					.removeIf(targetValue -> (targetValue.getValue() == null || targetValue.getValue().equals("NA")));
		}
		if (!ListUtils.isEmpty(field.getFieldOptions())) {
			field.getFieldOptions().removeIf(fieldOption -> fieldOption.getOptionName() == null);
		}
	}

	public Field saveField(Field field) {

		if (ObjectUtils.isEmpty(field)) {
			throw new VedantaWebException("field info cannot be empty");
		}
		int status;
		try {
			validateField(field);
			if (field.getStatus() < Constants.STATUS_ACTIVE) {
				field.setStatus(Constants.STATUS_ACTIVE);
			}
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_FIELD_DETAIL, field, null,
					HttpMethod.POST);

			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving field information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Field>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while saving field information", 401);
			}
			log.error("Error while saving field information");
			throw new VedantaWebException("Error while saving field information");
		}
	}

	public Field deleteField(long fieldId) {
		if (fieldId <= 0) {
			log.info("Invalid field id.");
			throw new VedantaWebException("Invalid field id.");
		}
		Field field = this.getFieldDetail(fieldId);
		if (ObjectUtils.isEmpty(field)) {
			throw new VedantaWebException("Invalid field id, no field found by id provided.");
		}
		try {
			if (field.getStatus() != Constants.STATUS_DELETE) {
				field.setStatus(Constants.STATUS_DELETE);
			}
			Field deletedField = this.saveField(field);
			return deletedField;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while deleting template information", 401);
			}
			log.error("Error while deleting template information");
			throw new VedantaWebException("Error while deleting template information");
		}
	}

	public List<Group> getAllGroups() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_GROUPS, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<Group>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching group listing", 401);
			}
			log.error("Error while fetching group listing");
			throw new VedantaWebException("Error while fetching group listing");
		}
	}

	public Group getGroupDetail(long groupId) {
		int status;
		String url = String.format(URLConstants.GET_GROUP_DETAIL, groupId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Group>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching group information", 401);
			}
			log.error("Error while fetching group information");
			throw new VedantaWebException("Error while fetching group information");
		}
	}

	public Group saveGroup(Group group) {

		if (ObjectUtils.isEmpty(group)) {
			throw new VedantaWebException("group info cannot be empty");
		}
		int status;
		try {
			if (group.getStatus() < Constants.STATUS_ACTIVE) {
				group.setStatus(Constants.STATUS_ACTIVE);
			}
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_GROUP_DETAIL, group, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving group information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Group>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while saving group information", 401);
			}
			log.error("Error while saving group information");
			throw new VedantaWebException("Error while group field information");
		}
	}

	public Group deleteGroup(long groupId) {
		if (groupId <= 0) {
			log.info("Invalid group id.");
			throw new VedantaWebException("Invalid group id.");
		}
		Group group = this.getGroupDetail(groupId);
		if (ObjectUtils.isEmpty(group)) {
			throw new VedantaWebException("Invalid group id, no group found by id provided.");
		}
		try {
			if (group.getStatus() != Constants.STATUS_DELETE) {
				group.setStatus(Constants.STATUS_DELETE);
			}
			Group deletedGroup = this.saveGroup(group);
			return deletedGroup;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while deleting group information", 401);
			}
			log.error("Error while deleting group information");
			throw new VedantaWebException("Error while deleting group information");
		}
	}

	// Group Field methods
	public List<GroupFieldMapper> getAllGroupFields() {

		int status;
		List<GroupFieldMapper> groupFieldMapperList = new ArrayList<>();
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_GROUP_FIELD, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group field listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			List<GroupField> groupFieldList = OBJECT_MAPPER.readValue(data, new TypeReference<List<GroupField>>() {
			});

			Map<Long, List<GroupField>> groupFieldMap = new HashMap<>();

			List<Group> groups = this.getAllGroups();
			Map<Long, String> groupMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(groups)) {
				groups.forEach(group -> {
					groupMap.put(group.getId(), group.getName());
				});
			}

			groupFieldList.forEach(groupField -> {
				List<GroupField> groupFieldListTobeSaved = new ArrayList<>();
				if (groupFieldMap.containsKey(groupField.getGroupId())) {
					groupFieldListTobeSaved = groupFieldMap.get(groupField.getGroupId());
					groupFieldListTobeSaved.add(groupField);
				} else {
					groupFieldListTobeSaved.add(groupField);
				}
				groupFieldMap.put(groupField.getGroupId(), groupFieldListTobeSaved);
			});

			groupFieldMap.forEach((groupId, groupFieldListFromMap) -> {
				GroupFieldMapper groupFieldMapper = new GroupFieldMapper();
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
				throw new VedantaWebException("Error while fetching group field listing", 401);
			}

			log.error("Error while fetching group field listing");
			throw new VedantaWebException("Error while fetching group field listing");
		}
	}

	public GroupField getGroupFieldDetail(long groupFieldId) {
		int status;
		String url = String.format(URLConstants.GET_GROUP_FIELDS_DETAIL, groupFieldId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group field detail information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<GroupField>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching group field detail information", 401);
			}
			log.error("Error while fetching group field detail information");
			throw new VedantaWebException("Error while fetching group field detail information");
		}
	}

	public List<GroupField> getGroupFields(long groupId) {
		int status;
		String url = String.format(URLConstants.GET_GROUP_FIELDS_BY_GROUP_ID, groupId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching group field information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<GroupField>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching group field information", 401);
			}
			log.error("Error while fetching group field information");
			throw new VedantaWebException("Error while fetching group field information");
		}
	}

	public GroupFieldMapper getGroupFieldMapper(List<GroupField> groupFields) {

		try {
			GroupFieldMapper groupFieldMapper = new GroupFieldMapper();
			if (!ObjectUtils.isEmpty(groupFields) && groupFields.size() > 0) {
				groupFieldMapper.setGroupId(groupFields.get(0).getGroupId());
				groupFieldMapper.setGroupFields(groupFields);
			}
			return groupFieldMapper;
		} catch (VedantaWebException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching group field information", 401);
			}
			log.error("Error while fetching group field information");
			throw new VedantaWebException("Error while fetching group field information");
		}
	}

	public Boolean saveGroupFields(GroupFieldMapper groupFieldMapper) {

		if (ObjectUtils.isEmpty(groupFieldMapper)) {
			throw new VedantaWebException("group field mapper cannot be empty");
		}
		int status;
		try {

			validateGroupFields(groupFieldMapper);

			List<GroupField> groupFieldListToSave = new ArrayList<>();
			groupFieldMapper.getGroupFields().forEach(groupField -> {
				groupField.setGroupId(groupFieldMapper.getGroupId());
				if (groupField.getStatus() < Constants.STATUS_ACTIVE) {
					groupField.setStatus(Constants.STATUS_ACTIVE);
				}
				groupFieldListToSave.add(groupField);
			});

			String url = String.format(URLConstants.SAVE_GROUP_FIELD_LIST, groupFieldMapper.getGroupId());
			JsonNode response = restServiceUtil.makeRequest(url, groupFieldListToSave, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving group field list information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while saving group field list information", 401);
			}
			log.error("Error while saving group field list information");
			throw new VedantaWebException("Error while group field list information");
		}
	}

	private void validateGroupFields(GroupFieldMapper groupFieldMapper) {
		if (ObjectUtils.isEmpty(groupFieldMapper) || ObjectUtils.isEmpty(groupFieldMapper.getGroupFields())
				|| groupFieldMapper.getGroupFields().size() <= 0) {
			log.error("Error while saving group field list information");
			throw new VedantaWebException("Error while group field list information");
		}

		List<GroupField> groupFields = groupFieldMapper.getGroupFields();

		List<GroupField> newGroupFields = new ArrayList<GroupField>();
		for (GroupField gFld : groupFields) {
			if (!ObjectUtils.isEmpty(gFld.getFieldId())) {
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
			String url = String.format(URLConstants.DELETE_GROUP_FIELD_LIST, groupId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving group field list information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while deleting group information", 401);
			}
			log.error("Error while deleting group information");
			throw new VedantaWebException("Error while deleting group information");
		}
	}

	// Template Groups methods
	public List<TemplateGroupMapper> getAllTemplateGroups() {

		int status;
		List<TemplateGroupMapper> templateGroupMapperList = new ArrayList<>();
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_TEMPLATE_GROUPS, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template group listing.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			List<TemplateGroup> templateGroupList = OBJECT_MAPPER.readValue(data,
					new TypeReference<List<TemplateGroup>>() {
					});

			Map<Long, List<TemplateGroup>> templateGroupsMap = new HashMap<>();

			List<Template> templates = this.getAllTemplates();
			Map<Long, String> templateMap = new HashMap<>();
			if (!CollectionUtils.isEmpty(templates)) {
				templates.forEach(template -> {
					templateMap.put(template.getId(), template.getName());
				});
			}

			templateGroupList.forEach(templateGroup -> {
				List<TemplateGroup> templateGroupsListTobeSaved = new ArrayList<>();

				if (templateGroupsMap.containsKey(templateGroup.getTemplateId())) {
					templateGroupsListTobeSaved = templateGroupsMap.get(templateGroup.getTemplateId());
					templateGroupsListTobeSaved.add(templateGroup);
				} else {
					templateGroupsListTobeSaved.add(templateGroup);
				}
				templateGroupsMap.put(templateGroup.getTemplateId(), templateGroupsListTobeSaved);
			});

			templateGroupsMap.forEach((templateId, templateGroupListFromMap) -> {
				TemplateGroupMapper templateGroupMapper = new TemplateGroupMapper();
				templateGroupMapper.setTemplateId(templateId);
				templateGroupMapper.setTemplateName(templateMap.get(templateId));
				templateGroupMapper.setTemplateGroups(templateGroupListFromMap);
				templateGroupMapperList.add(templateGroupMapper);
			});
			return templateGroupMapperList;
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching template groups listing", 401);
			}

			log.error("Error while fetching template groups listing");
			throw new VedantaWebException("Error while fetching template groups listing");
		}
	}

	public TemplateGroup getTemplateGroupDetail(long templateGroupId) {
		int status;
		String url = String.format(URLConstants.GET_TEMPLATE_GROUP_BY_TEMPLATE_GROUP_ID, templateGroupId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template group detail information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<TemplateGroup>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching tempate group detail information", 401);
			}
			log.error("Error while fetching tempate group detail information");
			throw new VedantaWebException("Error while fetching template group detail information");
		}
	}

	public List<TemplateGroup> getTemplateGroups(long templateId) {
		int status;
		String url = String.format(URLConstants.GET_TEMPLATE_GROUPS_BY_TEMPLATE_ID, templateId);
		try {
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching template groups information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<TemplateGroup>>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching template groups information", 401);
			}
			log.error("Error while fetching template groups information");
			throw new VedantaWebException("Error while fetching template groups information");
		}
	}

	public TemplateGroupMapper getTemplateGroupMapper(List<TemplateGroup> templateGroups) {

		try {
			TemplateGroupMapper templateGroupMapper = new TemplateGroupMapper();
			if (!ObjectUtils.isEmpty(templateGroups) && templateGroups.size() > 0) {
				templateGroupMapper.setTemplateId(templateGroups.get(0).getTemplateId());
				templateGroupMapper.setTemplateGroups(templateGroups);
			}
			return templateGroupMapper;
		} catch (VedantaWebException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching group field information", 401);
			}
			log.error("Error while fetching group field information");
			throw new VedantaWebException("Error while fetching group field information");
		}
	}

	public Boolean saveTemplateGroups(TemplateGroupMapper templateGroupMapper) {

		if (ObjectUtils.isEmpty(templateGroupMapper)) {
			throw new VedantaWebException("template group mapper cannot be empty");
		}
		int status;

		validateToSaveTemplateGroupMapper(templateGroupMapper);

		try {
			List<TemplateGroup> templateGroupListToSave = new ArrayList<>();
			templateGroupMapper.getTemplateGroups().forEach(templateGroup -> {
				templateGroup.setTemplateId(templateGroupMapper.getTemplateId());
				templateGroupListToSave.add(templateGroup);
			});

			String url = String.format(URLConstants.SAVE_TEMPLATE_GROUP_LIST, templateGroupMapper.getTemplateId());
			JsonNode response = restServiceUtil.makeRequest(url, templateGroupListToSave, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while saving group field list information.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while saving template group list information", 401);
			}
			log.error("Error while saving template group list information");
			throw new VedantaWebException("Error while saving template group list information");
		}
	}

	private void validateToSaveTemplateGroupMapper(TemplateGroupMapper templateGroupMapper) {

		if (ObjectUtils.isEmpty(templateGroupMapper) || ObjectUtils.isEmpty(templateGroupMapper.getTemplateGroups())
				|| templateGroupMapper.getTemplateGroups().size() <= 0) {
			log.error("Error while saving template group list information");
			throw new VedantaWebException("Error while saving template group list information");
		}
		List<TemplateGroup> templateGroups = new ArrayList<TemplateGroup>();

		for (TemplateGroup templateGroup : templateGroupMapper.getTemplateGroups()) {
			if (!ObjectUtils.isEmpty(templateGroup.getGroupId())) {
				templateGroups.add(templateGroup);
			}
		}
		templateGroupMapper.setTemplateGroups(templateGroups);
	}

	public Boolean deleteTemplateGroups(long templateId) {
		if (templateId <= 0) {
			log.info("Invalid template id.");
			throw new VedantaWebException("Invalid template id.");
		}
		int status;
		try {
			String url = String.format(URLConstants.DELETE_TEMPLATE_GROUPS_BY_TEMPLATE_ID, templateId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while deleting template groups by template id.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Boolean>() {
			});
		} catch (VedantaWebException | IOException e) {
			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while deleting template group information", 401);
			}
			log.error("Error while deleting template group information");
			throw new VedantaWebException("Error while deleting template group information");
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
				throw new VedantaWebException("Error while fetching field listing", 401);
			}
			log.error("Error while fetching field listing");
			throw new VedantaWebException("Error while fetching field listing");
		}
	}

    
    public List<TemplateGroupMapper> getTemplateGroupsByBusinessUnitId(Long buId) {

        int status;
        List<TemplateGroupMapper> templateGroupMapperList = new ArrayList<>();
        String url=String.format(URLConstants.GET_ALL_TEMPLATE_GROUPS_BY_BUSINESS_UNIT_ID,buId);
        try {
            JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
            status = response.get(VedantaConstant.STATUS_CODE).intValue();
            if (status != 200) {
                throw new VedantaWebException("API not responded while fetching template group listing.");
            }
            String data = response.get(VedantaConstant.DATA).toString();
            List<TemplateGroup> templateGroupList = OBJECT_MAPPER.readValue(data, new TypeReference<List<TemplateGroup>>() {
            });

            Map<Long, List<TemplateGroup>> templateGroupsMap = new HashMap<>();

            List<Template> templates = this.getAllTemplates();
            Map<Long, String> templateMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(templates)){
                templates.forEach(template -> {
                    templateMap.put(template.getId(), template.getName());
                });
            }

            templateGroupList.forEach(templateGroup -> {
                List<TemplateGroup> templateGroupsListTobeSaved = new ArrayList<>();

                if (templateGroupsMap.containsKey(templateGroup.getTemplateId())) {
                    templateGroupsListTobeSaved = templateGroupsMap.get(templateGroup.getTemplateId());
                    templateGroupsListTobeSaved.add(templateGroup);
                } else {
                    templateGroupsListTobeSaved.add(templateGroup);
                }
                templateGroupsMap.put(templateGroup.getTemplateId(), templateGroupsListTobeSaved);
            });

            templateGroupsMap.forEach((templateId, templateGroupListFromMap) -> {
                TemplateGroupMapper templateGroupMapper = new TemplateGroupMapper();
                templateGroupMapper.setTemplateId(templateId);
                templateGroupMapper.setTemplateName(templateMap.get(templateId));
                templateGroupMapper.setTemplateGroups(templateGroupListFromMap);
                templateGroupMapperList.add(templateGroupMapper);
            });
            return templateGroupMapperList;
        } catch (VedantaWebException | IOException e) {
        	
        	String errorCode = e.getMessage();
			if(errorCode.equals("401")){
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching template groups listing",401);
			}
        	
            log.error("Error while fetching template groups listing");
            throw new VedantaWebException("Error while fetching template groups listing");
        }
    }
   

}
