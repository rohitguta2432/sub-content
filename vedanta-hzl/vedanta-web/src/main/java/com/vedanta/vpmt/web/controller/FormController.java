package com.vedanta.vpmt.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.vedanta.vpmt.mapper.FormGroupFieldMapper;
import com.vedanta.vpmt.mapper.FormGroupMapper;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.FormField;
import com.vedanta.vpmt.model.FormGroup;
import com.vedanta.vpmt.model.FormGroupDetail;
import com.vedanta.vpmt.model.FormGroupField;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.CategoryService;
import com.vedanta.vpmt.web.service.FormService;
import com.vedanta.vpmt.web.service.PlantService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 12/09/17.
 */
@Controller
@RequestMapping(value = "admin/form")
@Slf4j
public class FormController {
	private final static String FORM_LISTING_VIEW_NAME = "web/form/form_listing";
	private final static String FORM_CREATE_VIEW_NAME = "web/form/form_create";
	private final static String FIELD_LIST_VIEW_NAME = "web/form/form_field_listing";
	private final static String FIELD_CREATE_VIEW_NAME = "web/form/form_field_create";
	private final static String GROUP_LISTING_VIEW_NAME = "web/form/form_group_detail_listing";
	private final static String GROUP_CREATE_VIEW_NAME = "web/form/form_group_detail_create";
	private final static String GROUP_FIELD_LISTING_VIEW_NAME = "web/form/form_group_field_listing";
	private final static String GROUP_FIELD_CREATE_VIEW_NAME = "web/form/form_group_field_create";
	private final static String FORM_GROUP_LISTING_VIEW_NAME = "web/form/form_group_listing";
	private final static String FORM_GROUP_CREATE_VIEW_NAME = "web/form/form_group_create";
	
	@Autowired
	private FormService formService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private PlantService plantServices;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getTemplates() {
		ModelAndView mav = new ModelAndView(FORM_LISTING_VIEW_NAME);
		List<Form> forms = new ArrayList<>();
		List<Category> categories = new ArrayList<>();
		List<BusinessUnit> businessUnits = new ArrayList<>();
		try {
		
			forms = formService.getAllForms();
			mav.addObject("forms", forms);
			categories = categoryService.getAllCategories();
			mav.addObject("categories", categories);
			businessUnits = plantServices.getAllBusinessUnitsByRole();
			mav.addObject("businessUnits", businessUnits);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing form data");
			throw new VedantaWebException("Error listing form data.", e.code);
		}
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public ModelAndView createForm() {
		ModelAndView mav = new ModelAndView(FORM_CREATE_VIEW_NAME);
		List<Category> categories = new ArrayList<>();
		categories = categoryService.getAllCategories();
		mav.addObject("categories", categories);
		mav.addObject("form", new Form());
		mav.addObject("bussinessId",plantServices.getAllBusinessUnitsByRole());
		return mav;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateTemplate(@PathVariable("id") Long id) {

		ModelAndView mav = new ModelAndView(FORM_CREATE_VIEW_NAME);
		Form form;
		List<Category> categories = new ArrayList<>();
		try {
			categories = categoryService.getAllCategories();
			mav.addObject("categories", categories);
			form = formService.getFormDetail(id);
			mav.addObject("form", form);
			mav.addObject("bussinessId",plantServices.getAllBusinessUnits());
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting form information");
			throw new VedantaWebException("Error getting form information", e.code);
		}
	}

	@RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
	public ModelAndView deleteForm(@PathVariable("id") Long id) {

		try {
			formService.deleteForm(id);
			return new ModelAndView("redirect:" + "/admin/form");
		} catch (VedantaWebException e) {
			log.error("Error getting form information");
			throw new VedantaWebException("Error getting form information", e.code);
		}
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public ModelAndView saveForm(@ModelAttribute("form") Form form, Model model) {
		try {

			
			formService.saveForm(form);
			return new ModelAndView("redirect:" + "/admin/form");
		} catch (VedantaWebException e) {
			log.error("Error saving form information");
			throw new VedantaWebException("Error saving form data.", e.code);
		}
	}

	@RequestMapping(value = "field", method = RequestMethod.GET)
	public ModelAndView getFieldListing() {

		ModelAndView mav = new ModelAndView(FIELD_LIST_VIEW_NAME);
		List<FormField> fields = new ArrayList<>();
		try {
			fields = formService.getAllFormFields();
			mav.addObject("fields", fields);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing form field information");
			throw new VedantaWebException("Error listing form field information.", e.code);
		}
	}

	@RequestMapping(value = "field/create", method = RequestMethod.GET)
	public ModelAndView createField() {
		ModelAndView mav = new ModelAndView(FIELD_CREATE_VIEW_NAME);
		mav.addObject("dataUnits", formService.getAllDataUnits());
		mav.addObject("field", new FormField());
		return mav;
	}

	@RequestMapping(value = "field/update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateField(@PathVariable("id") Long id) {

		ModelAndView mav = new ModelAndView(FIELD_CREATE_VIEW_NAME);
		FormField field;
		try {
			field = formService.getFieldDetail(id);
			mav.addObject("dataUnits", formService.getAllDataUnits());
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
			formService.deleteField(id);
			return new ModelAndView("redirect:" + "/admin/form/field");
		} catch (VedantaWebException e) {
			log.error("Error deleting field information");
			throw new VedantaWebException("Error deleting field information", e.code);
		}
	}

	@RequestMapping(value = "field/save", method = RequestMethod.POST)
	public ModelAndView saveField(@ModelAttribute("formField") FormField formField, Model model) {
		try {
			formService.saveField(formField);
			return new ModelAndView("redirect:" + "/admin/form/field");
		} catch (VedantaWebException e) {
			log.error("Error saving form field information");
			throw new VedantaWebException("Error saving form field data.", e.code);
		}
	}

	@RequestMapping(value = "group", method = RequestMethod.GET)
	public ModelAndView getGroupListing() {
		ModelAndView mav = new ModelAndView(GROUP_LISTING_VIEW_NAME);
		List<FormGroupDetail> groups = new ArrayList<>();
		try {
			groups = formService.getAllGroups();
			mav.addObject("groups", groups);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing form group detail information");
			throw new VedantaWebException("Error listing form group detail information.", e.code);
		}
	}

	@RequestMapping(value = "group/create", method = RequestMethod.GET)
	public ModelAndView createGroup() {
		ModelAndView mav = new ModelAndView(GROUP_CREATE_VIEW_NAME);
		mav.addObject("group", new FormGroupDetail());
		return mav;
	}

	@RequestMapping(value = "group/update/{id}", method = RequestMethod.GET)
	public ModelAndView getUpdateGroup(@PathVariable("id") Long id) {

		ModelAndView mav = new ModelAndView(GROUP_CREATE_VIEW_NAME);
		FormGroupDetail group;
		try {

			group = formService.getGroupDetail(id);
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
			formService.deleteGroup(id);
			return new ModelAndView("redirect:" + "/admin/form/group");
		} catch (VedantaWebException e) {
			log.error("Error deleting group information");
			throw new VedantaWebException("Error deleting group information", e.code);
		}
	}

	@RequestMapping(value = "group/save", method = RequestMethod.POST)
	public ModelAndView saveGroup(@ModelAttribute("formGroupDetail") FormGroupDetail formGroupDetail, Model model) {
		try {
			formService.saveGroup(formGroupDetail);
			return new ModelAndView("redirect:" + "/admin/form/group");
		} catch (VedantaWebException e) {
			log.error("Error saving group information");
			throw new VedantaWebException("Error saving group information.", e.code);
		}
	}

	// Group Fields Actions

	@RequestMapping(value = "group/fields", method = RequestMethod.GET)
	public ModelAndView getGroupFieldListing() {
		ModelAndView mav = new ModelAndView(GROUP_FIELD_LISTING_VIEW_NAME);
		List<FormGroupFieldMapper> groupFieldMappers = new ArrayList<>();
		try {
			groupFieldMappers = formService.getAllGroupFields();
			mav.addObject("groupFieldMappers", groupFieldMappers);
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing form group field mapper information");
			throw new VedantaWebException("Error listing form group field mapper information.", e.code);
		}
	}

	@RequestMapping(value = "group/fields/create", method = RequestMethod.GET)
	public ModelAndView createGroupField() {
		ModelAndView mav = new ModelAndView(GROUP_FIELD_CREATE_VIEW_NAME);
		mav.addObject("groupFieldMappers", new FormGroupFieldMapper());
		mav.addObject("fields", formService.getAllFormFields());
		mav.addObject("groups", formService.getAllGroups());
		return mav;
	}

	@RequestMapping(value = "group/fields/update/{groupId}", method = RequestMethod.GET)
	public ModelAndView getUpdateGroupFields(@PathVariable("groupId") Long groupId) {

		ModelAndView mav = new ModelAndView(GROUP_FIELD_CREATE_VIEW_NAME);
		List<FormGroupField> groupFields;
		try {
			groupFields = formService.getGroupFields(groupId);
			mav.addObject("groupFieldMappers",  formService.getFormGroupFieldMapper(groupFields));
			mav.addObject("groups", formService.getAllGroups());
			mav.addObject("fields", formService.getAllFormFields());
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting form field information");
			throw new VedantaWebException("Error getting form field information", e.code);
		}
	}

	@RequestMapping(value = "group/fields/delete/{groupId}", method = RequestMethod.GET)
	public ModelAndView deleteGroupField(@PathVariable("groupId") Long groupId) {

		try {
			formService.deleteGroupFields(groupId);
			return new ModelAndView("redirect:" + "/admin/form/group/fields");
		} catch (VedantaWebException e) {
			log.error("Error deleting group information");
			throw new VedantaWebException("Error deleting group information", e.code);
		}
	}

	@RequestMapping(value = "group/fields/save", method = RequestMethod.POST)
	public ModelAndView saveGroupFields(@ModelAttribute("formGroupFieldMapper") FormGroupFieldMapper groupFieldMapper,
			Model model) {
		try {

			formService.saveGroupFields(groupFieldMapper);
			return new ModelAndView("redirect:" + "/admin/form/group/fields");
		} catch (VedantaWebException e) {
			log.error("Error saving group field information");
			throw new VedantaWebException("Error saving group field information.", e.code);
		}
	}

	// Template Group Actions
	@RequestMapping(value = "groups", method = RequestMethod.GET)
	public ModelAndView getFormGroupListing() {
		ModelAndView mav = new ModelAndView(FORM_GROUP_LISTING_VIEW_NAME);
		List<FormGroupMapper> formGroupMappers = new ArrayList<>();
		try {
			formGroupMappers = formService.getAllFormGroups();
			mav.addObject("formGroupMappers", formGroupMappers);
			
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error listing template group information");
			throw new VedantaWebException("Error listing template group information.", e.code);
		}
	}

	@RequestMapping(value = "groups/create", method = RequestMethod.GET)
	public ModelAndView createFormGroups() {
		ModelAndView mav = new ModelAndView(FORM_GROUP_CREATE_VIEW_NAME);
		mav.addObject("formGroupMappers", new FormGroupMapper());
		mav.addObject("forms", formService.getAllForms());
		mav.addObject("groups", formService.getAllGroups());
		return mav;
	}

	@RequestMapping(value = "groups/update/{formId}", method = RequestMethod.GET)
	public ModelAndView getUpdateTemplateGroups(@PathVariable("formId") Long formId) {

		ModelAndView mav = new ModelAndView(FORM_GROUP_CREATE_VIEW_NAME);
		List<FormGroup> formGroups;
		try {
			formGroups = formService.getFormGroups(formId);
			
			mav.addObject("formGroupMappers", formService.getGroupFieldMapper(formGroups));
			mav.addObject("groups", formService.getAllGroups());
			mav.addObject("forms", formService.getAllForms());
			return mav;
		} catch (VedantaWebException e) {
			log.error("Error getting form group information");
			throw new VedantaWebException("Error getting form group information", e.code);
		}
	}

	@RequestMapping(value = "groups/delete/{formId}", method = RequestMethod.GET)
	public ModelAndView deleteFormGroups(@PathVariable("formId") Long formId) {

		try {
			formService.deleteFormGroups(formId);
			return new ModelAndView("redirect:" + "/admin/form/groups");
		} catch (VedantaWebException e) {
			log.error("Error deleting form groups information");
			throw new VedantaWebException("Error form groups information", e.code);
		}	
	}

	@RequestMapping(value = "groups/save", method = RequestMethod.POST)
	public ModelAndView saveFormGroups(
			@ModelAttribute("formGroupMapper") FormGroupMapper formGroupMapper, Model model) {
		try {
			formService.saveFormGroups(formGroupMapper);
			return new ModelAndView("redirect:" + "/admin/form/groups");
		} catch (VedantaWebException e) {
			log.error("Error saving form groups information");
			throw new VedantaWebException("Error saving form groups information.", e.code);
		}
	}

	// ScoreCardData Target Value configuration
//	@RequestMapping(value = "scorecard/target-value/sub-cat-id/{subCategoryId}", method = RequestMethod.GET)
//	public @ResponseBody Map<String, Object> getScoreCardForContract(
//			@PathVariable("subCategoryId") Long subCategoryId) {
//
//		Map<String, Object> scoreCardTemplate = new HashMap<>();
//		try {
//			scoreCardTemplate = scoreCardService.getScoreCardTemplateBySubCategoryId(subCategoryId);
//			return scoreCardTemplate;
//		} catch (VedantaWebException e) {
//			logger.error("Error getting template information");
//			throw new VedantaWebException("Error getting template information");
//		}
//	}
//
//	@RequestMapping(value = "scorecard/target-value/list", method = RequestMethod.GET)
//	public ModelAndView ScoreCardTargetValueList() {
//
//		ModelAndView mav = new ModelAndView(TARGET_LISTING);
//		List<TargetValue> scoreCardTargetValueList = new ArrayList<>();
//		try {
//			scoreCardTargetValueList = scoreCardService.getAllDistinctSavedScoreCardTarget();
//			mav.addObject("targetvalues", scoreCardTargetValueList);
//			return mav;
//		} catch (VedantaWebException e) {
//			logger.error("Error getting base data information");
//			throw new VedantaWebException("Error getting base data information");
//		}
//	}
//
//	@RequestMapping(value = "scorecard/target-value/create", method = RequestMethod.GET)
//	public ModelAndView scoreCardTargetValueCreate() {
//
//		ModelAndView mav = new ModelAndView(TARGET_CREATE);
//		Map<String, Object> scoreCardBaseData = new HashMap<>();
//		try {
//			scoreCardBaseData = scoreCardService.getScoreCardBaseData();
//			mav.addObject("categories", scoreCardBaseData.get(Constants.ALL_CATEGORIES));
//			mav.addObject("plants", scoreCardBaseData.get(Constants.ALL_PLANTS));
//			mav.addObject("departments", scoreCardBaseData.get(Constants.ALL_DEPARTMENT));
//			return mav;
//		} catch (VedantaWebException e) {
//			logger.error("Error getting base data information");
//			throw new VedantaWebException("Error getting base data information");
//		}
//	}
//	@RequestMapping(value = "targetvalue/save", method = RequestMethod.POST)
//	public ModelAndView savetargetvalue(@ModelAttribute("targetvalue") TargetValue targetValue,
//			Model model) {
//		try {
//
//			targetValueService.save(targetValue);
//		} catch (VedantaWebException e) {
//			logger.error("Error saving group field information");
//			throw new VedantaWebException("Error saving group field information.");
//		}
//		return new ModelAndView("redirect:" + "/admin/template/scorecard/target-value/list");
//	}
//	@RequestMapping(value = "get/targetvalue/{id}", method = RequestMethod.GET)
//    public ModelAndView getUserById(@PathVariable("id") long id) {
//
//        ModelAndView mav = new ModelAndView(TARGET_VIEW);
//        TargetValue target = null;
//        try {
//        	target = targetValueService.get(id);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            throw e;
//        }
//        mav.addObject("updateUser", new TargetValue());
//        mav.addObject("targetvalue", target);
//        return mav;
//    }

}
