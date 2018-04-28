package com.vedanta.vpmt.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.mapper.GroupFieldMapper;
import com.vedanta.vpmt.mapper.TemplateGroupMapper;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.model.Field;
import com.vedanta.vpmt.model.Group;
import com.vedanta.vpmt.model.GroupField;
import com.vedanta.vpmt.model.TargetValue;
import com.vedanta.vpmt.model.Template;
import com.vedanta.vpmt.model.TemplateGroup;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.CategoryService;
import com.vedanta.vpmt.web.service.FormService;
import com.vedanta.vpmt.web.service.PlantService;
import com.vedanta.vpmt.web.service.ScoreCardService;
import com.vedanta.vpmt.web.service.TargetValueService;
import com.vedanta.vpmt.web.service.TemplateService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 12/09/17.
 */
@Controller
@RequestMapping(value = "admin/template")
@Slf4j
public class TemplateController {
	private final static String TEMPLATE_LISTING_VIEW_NAME = "web/template/template_listing";
	private final static String TEMPLATE_CREATE_VIEW_NAME = "web/template/template_create";
	private final static String FIELD_LIST_VIEW_NAME = "web/template/field_listing";
	private final static String FIELD_CREATE_VIEW_NAME = "web/template/field_create";
	private final static String GROUP_LISTING_VIEW_NAME = "web/template/group_listing";
	private final static String GROUP_CREATE_VIEW_NAME = "web/template/group_create";
	private final static String GROUP_FIELD_LISTING_VIEW_NAME = "web/template/group_field_listing";
	private final static String GROUP_FIELD_CREATE_VIEW_NAME = "web/template/group_field_create";
	private final static String TEMPLATE_GROUP_LISTING_VIEW_NAME = "web/template/template_group_listing";
	private final static String TEMPLATE_GROUP_CREATE_VIEW_NAME = "web/template/template_group_create";
	private final static String TARGET_LISTING = "web/template/target_listing";
	private final static String TARGET_CREATE = "web/template/target_create";
	private final static String TARGET_VIEW = "web/template/target_view";

	@Autowired
	private TemplateService templateService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ScoreCardService scoreCardService;

	@Autowired
	private TargetValueService targetValueService;

	@Autowired
	private FormService formService;

	@Autowired
	private PlantService plantServices;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAutoGrowCollectionLimit(1524);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getTemplates() {
		ModelAndView mav = new ModelAndView(TEMPLATE_LISTING_VIEW_NAME);
		List<Template> templates = new ArrayList<>();
		List<Category> categories = new ArrayList<>();
		try {
			templates = templateService.getAllTemplates();
			mav.addObject("templates", templates);
			categories = categoryService.getAllCategories();
			mav.addObject("categories", categories);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing template data");
			throw new VedantaWebException("Error listing template data.", e.code);
		}
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView createTemplate() {
		ModelAndView mav = new ModelAndView(TEMPLATE_CREATE_VIEW_NAME);
		List<Category> categories = new ArrayList<>();
		categories = categoryService.getAllCategories();
		mav.addObject("categories", categories);
		mav.addObject("forms", formService.getAllForms());
		mav.addObject("template", new Template());
		mav.addObject("bussinessId", plantServices.getAllBusinessUnits());

		return mav;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateTemplate(@PathVariable("id") Long id) {

		ModelAndView mav = new ModelAndView(TEMPLATE_CREATE_VIEW_NAME);
		Template template;
		List<Category> categories = new ArrayList<>();
		try {
			categories = categoryService.getAllCategories();
			mav.addObject("categories", categories);
			mav.addObject("forms", formService.getAllForms());
			template = templateService.getTemplateDetail(id);
			mav.addObject("template", template);
			mav.addObject("bussinessId", plantServices.getAllBusinessUnits());

			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting template information");
			throw new VedantaWebException("Error getting template information", e.code);
		}
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteTemplate(@PathVariable("id") Long id) {

		try {
			templateService.deleteTemplate(id);
		} catch (VedantaWebException e) {
			log.error("Error getting template information");
			throw new VedantaWebException("Error getting template information", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template");
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView saveTemplate(@ModelAttribute("template") Template template, Model model) {
		try {
			templateService.saveTemplate(template);
		} catch (VedantaWebException e) {
			log.error("Error saving template data");
			throw new VedantaWebException("Error saving template data.", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template");
	}

	@RequestMapping(value = "field", method = RequestMethod.GET)
	public ModelAndView getFieldListing() {

		ModelAndView mav = new ModelAndView(FIELD_LIST_VIEW_NAME);
		List<Field> fields = new ArrayList<>();
		try {
			fields = templateService.getAllFields();
			mav.addObject("fields", fields);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing field information");
			throw new VedantaWebException("Error listing field information.", e.code);
		}
	}

	@RequestMapping(value = "field/create", method = RequestMethod.GET)
	public ModelAndView createField() {
		ModelAndView mav = new ModelAndView(FIELD_CREATE_VIEW_NAME);
		mav.addObject("dataUnits", templateService.getAllDataUnits());
		mav.addObject("field", new Field());
		return mav;
	}

	@RequestMapping(value = "field/update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateField(@PathVariable("id") Long id) {

		ModelAndView mav = new ModelAndView(FIELD_CREATE_VIEW_NAME);
		Field field;
		try {
			field = templateService.getFieldDetail(id);
			mav.addObject("dataUnits", templateService.getAllDataUnits());
			mav.addObject("field", field);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting field information");
			throw new VedantaWebException("Error getting field information", e.code);
		}
	}

	@RequestMapping(value = "field/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteField(@PathVariable("id") Long id) {
		try {
			templateService.deleteField(id);
		} catch (VedantaWebException e) {
			log.error("Error deleting field information");
			throw new VedantaWebException("Error deleting field information", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/field");
	}

	// test
	@RequestMapping(value = "field/save", method = RequestMethod.POST)
	public ModelAndView saveField(@ModelAttribute("field") Field field, Model model) {
		try {
			templateService.saveField(field);
		} catch (VedantaWebException e) {
			log.error("Error saving field information");
			throw new VedantaWebException("Error saving field data.", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/field");
	}

	@RequestMapping(value = "group", method = RequestMethod.GET)
	public ModelAndView getGroupListing() {
		ModelAndView mav = new ModelAndView(GROUP_LISTING_VIEW_NAME);
		List<Group> groups = new ArrayList<>();
		try {
			groups = templateService.getAllGroups();
			mav.addObject("groups", groups);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing group information");
			throw new VedantaWebException("Error listing group information.", e.code);
		}
	}

	@RequestMapping(value = "group/create", method = RequestMethod.GET)
	public ModelAndView createGroup() {
		ModelAndView mav = new ModelAndView(GROUP_CREATE_VIEW_NAME);
		mav.addObject("group", new Group());
		return mav;
	}

	@RequestMapping(value = "group/update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateGroup(@PathVariable("id") Long id) {

		ModelAndView mav = new ModelAndView(GROUP_CREATE_VIEW_NAME);
		Group group;
		try {
			group = templateService.getGroupDetail(id);
			mav.addObject("group", group);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting field information");
			throw new VedantaWebException("Error getting field information", e.code);
		}
	}

	@RequestMapping(value = "group/delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteGroup(@PathVariable("id") Long id) {

		try {
			templateService.deleteGroup(id);
		} catch (VedantaWebException e) {
			log.error("Error deleting group information");
			throw new VedantaWebException("Error deleting group information", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/group");
	}

	@RequestMapping(value = "group/save", method = RequestMethod.POST)
	public ModelAndView saveGroup(@ModelAttribute("group") Group group, Model model) {
		try {
			templateService.saveGroup(group);
		} catch (VedantaWebException e) {
			log.error("Error saving group information");
			throw new VedantaWebException("Error saving group information.", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/group");
	}

	// Group Fields Actions

	@RequestMapping(value = "group/fields", method = RequestMethod.GET)
	public ModelAndView getGroupFieldListing() {
		ModelAndView mav = new ModelAndView(GROUP_FIELD_LISTING_VIEW_NAME);
		List<GroupFieldMapper> groupFieldMappers = new ArrayList<>();
		try {
			groupFieldMappers = templateService.getAllGroupFields();
			mav.addObject("groupFieldMappers", groupFieldMappers);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing group field mapper information");
			throw new VedantaWebException("Error listing group field mapper information.", e.code);
		}
	}

	@RequestMapping(value = "group/fields/create", method = RequestMethod.GET)
	public ModelAndView createGroupField() {
		ModelAndView mav = new ModelAndView(GROUP_FIELD_CREATE_VIEW_NAME);
		mav.addObject("groupFieldMappers", new GroupFieldMapper());
		mav.addObject("fields", templateService.getAllFields());
		mav.addObject("groups", templateService.getAllGroups());
		return mav;
	}

	@RequestMapping(value = "group/fields/update/{groupId}", method = RequestMethod.GET)
	public ModelAndView getUpdateGroupFields(@PathVariable("groupId") Long groupId) {

		ModelAndView mav = new ModelAndView(GROUP_FIELD_CREATE_VIEW_NAME);
		List<GroupField> groupFields;
		try {
			groupFields = templateService.getGroupFields(groupId);
			mav.addObject("groupFieldMappers", templateService.getGroupFieldMapper(groupFields));
			mav.addObject("groups", templateService.getAllGroups());
			mav.addObject("fields", templateService.getAllFields());
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting field information");
			throw new VedantaWebException("Error getting field information", e.code);
		}
	}

	@RequestMapping(value = "group/fields/delete/{groupId}", method = RequestMethod.GET)
	public ModelAndView deleteGroupField(@PathVariable("groupId") Long groupId) {

		try {
			templateService.deleteGroupFields(groupId);
		} catch (VedantaWebException e) {
			log.error("Error deleting group information");
			throw new VedantaWebException("Error deleting group information", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/group/fields");
	}

	@RequestMapping(value = "group/fields/save", method = RequestMethod.POST)
	public ModelAndView saveGroupFields(@ModelAttribute("groupFieldMapper") GroupFieldMapper groupFieldMapper,
			Model model) {
		try {
			templateService.saveGroupFields(groupFieldMapper);
		} catch (VedantaWebException e) {
			log.error("Error saving group field information");
			throw new VedantaWebException("Error saving group field information.", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/group/fields");
	}

	// Template Group Actions
	@RequestMapping(value = "groups", method = RequestMethod.GET)
	public ModelAndView getTemplateGroupListing(@RequestParam(value = "buId", required = false) Long buId) {

		ModelAndView mav = new ModelAndView(TEMPLATE_GROUP_LISTING_VIEW_NAME);
		List<TemplateGroupMapper> templateGroupMappers = new ArrayList<>();
		List<BusinessUnit> businessUnitList = null;
		try {
			businessUnitList = plantServices.getAllBusinessUnits();
			if (buId != null)
				templateGroupMappers = templateService.getTemplateGroupsByBusinessUnitId(buId);
			else {
				if (businessUnitList.size() > 0)
					templateGroupMappers = templateService
							.getTemplateGroupsByBusinessUnitId(businessUnitList.get(0).getId());
			}

			mav.addObject("businessUnits", businessUnitList);
			mav.addObject("searchBussinessId", buId);
			mav.addObject("templateGroupMappers", templateGroupMappers);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing template group information");
			throw new VedantaWebException("Error listing template group information.", e.code);
		}
	}

	@RequestMapping(value = "groups/create", method = RequestMethod.GET)
	public ModelAndView createTemplateGroups() {
		ModelAndView mav = new ModelAndView(TEMPLATE_GROUP_CREATE_VIEW_NAME);
		mav.addObject("templateGroupMappers", new TemplateGroupMapper());
		mav.addObject("templates", templateService.getAllTemplates());
		mav.addObject("groups", templateService.getAllGroups());
		return mav;
	}

	@RequestMapping(value = "groups/update/{templateId}", method = RequestMethod.GET)
	public ModelAndView getUpdateTemplateGroups(@PathVariable("templateId") Long templateId) {

		ModelAndView mav = new ModelAndView(TEMPLATE_GROUP_CREATE_VIEW_NAME);
		List<TemplateGroup> templateGroups;
		try {
			templateGroups = templateService.getTemplateGroups(templateId);
			mav.addObject("templateGroupMappers", templateService.getTemplateGroupMapper(templateGroups));
			mav.addObject("groups", templateService.getAllGroups());
			mav.addObject("templates", templateService.getAllTemplates());
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting template group information");
			throw new VedantaWebException("Error getting template group information", e.code);
		}
	}

	@RequestMapping(value = "groups/delete/{templateId}", method = RequestMethod.GET)
	public ModelAndView deleteTemplateGroups(@PathVariable("templateId") Long templateId) {

		try {
			templateService.deleteTemplateGroups(templateId);
		} catch (VedantaWebException e) {
			log.error("Error deleting template groups information");
			throw new VedantaWebException("Error deleting template groups information", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/groups");
	}

	@RequestMapping(value = "groups/save", method = RequestMethod.POST)
	public ModelAndView saveTemplateGroups(
			@ModelAttribute("templateGroupMapper") TemplateGroupMapper templateGroupMapper, Model model) {
		try {
			templateService.saveTemplateGroups(templateGroupMapper);
		} catch (VedantaWebException e) {
			log.error("Error saving template groups information");
			throw new VedantaWebException("Error saving template groups information.", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/groups");
	}

	// ScoreCardData Target Value configuration
	@RequestMapping(value = "scorecard/target-value/sub-cat-id/{subCategoryId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getScoreCardForContract(
			@PathVariable("subCategoryId") Long subCategoryId) {

		Map<String, Object> scoreCardTemplate = new HashMap<>();
		try {
			scoreCardTemplate = scoreCardService.getScoreCardTemplateBySubCategoryId(subCategoryId);
			return scoreCardTemplate;
		} catch (VedantaWebException e) {
			log.error("Error getting template information");
			throw new VedantaWebException("Error getting template information", e.code);
		}
	}

	@RequestMapping(value = "scorecard/target-value/list", method = RequestMethod.GET)
	public ModelAndView ScoreCardTargetValueList() {

		ModelAndView mav = new ModelAndView(TARGET_LISTING);
		List<TargetValue> scoreCardTargetValueList = new ArrayList<>();
		try {
			scoreCardTargetValueList = scoreCardService.getAllDistinctSavedScoreCardTarget();
			mav.addObject("targetvalues", scoreCardTargetValueList);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting base data information");
			throw new VedantaWebException("Error getting base data information", e.code);
		}
	}

	@RequestMapping(value = "scorecard/target-value/create", method = RequestMethod.GET)
	public ModelAndView scoreCardTargetValueCreate() {

		ModelAndView mav = new ModelAndView(TARGET_CREATE);
		Map<String, Object> scoreCardBaseData = new HashMap<>();
		try {
			scoreCardBaseData = scoreCardService.getScoreCardBaseData();
			mav.addObject("categories", scoreCardBaseData.get(Constants.ALL_CATEGORIES));
			mav.addObject("plants", scoreCardBaseData.get(Constants.ALL_PLANTS));
			mav.addObject("departments", scoreCardBaseData.get(Constants.ALL_DEPARTMENT));
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting base data information");
			throw new VedantaWebException("Error getting base data information", e.code);
		}
	}

	@RequestMapping(value = "targetvalue/save", method = RequestMethod.POST)
	public ModelAndView savetargetvalue(@ModelAttribute("targetvalue") TargetValue targetValue, Model model) {
		try {
			targetValueService.save(targetValue);
		} catch (VedantaWebException e) {
			log.error("Error saving group field information");
			throw new VedantaWebException("Error saving group field information.", e.code);
		}
		return new ModelAndView("redirect:" + "/admin/template/scorecard/target-value/list");
	}

	@RequestMapping(value = "get/targetvalue/{id}", method = RequestMethod.GET)
	public ModelAndView getUserById(@PathVariable("id") long id) {
		ModelAndView mav = new ModelAndView(TARGET_VIEW);
		List<Category> categories = new ArrayList<>();
		TargetValue target = null;
		try {
			target = targetValueService.get(id);
			categories = categoryService.getAllCategories();

		} catch (VedantaWebException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		mav.addObject("updateUser", new TargetValue());
		mav.addObject("categories", categories);
		mav.addObject("targetvalue", target);
		return mav;
	}

}
