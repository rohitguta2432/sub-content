package com.vedanta.vpmt.contollers.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.DataUnit;
import com.vedanta.vpmt.model.Field;
import com.vedanta.vpmt.model.Group;
import com.vedanta.vpmt.model.GroupField;
import com.vedanta.vpmt.model.TargetValue;
import com.vedanta.vpmt.model.Template;
import com.vedanta.vpmt.model.TemplateGroup;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.service.DataUnitService;
import com.vedanta.vpmt.service.FieldService;
import com.vedanta.vpmt.service.GroupFieldService;
import com.vedanta.vpmt.service.GroupService;
import com.vedanta.vpmt.service.TargetValueService;
import com.vedanta.vpmt.service.TemplateGroupService;
import com.vedanta.vpmt.service.TemplateService;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 11/09/17.
 */
@RestController
@RequestMapping(value = "/admin/template", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class TemplateController {

	@Autowired
	@Qualifier("fieldService")
	private FieldService fieldService;

	@Autowired
	@Qualifier("groupService")
	private GroupService groupService;

	@Autowired
	@Qualifier("groupFieldService")
	private GroupFieldService groupFieldService;

	@Autowired
	@Qualifier("templateService")
	private TemplateService templateService;

	@Autowired
	@Qualifier("templateGroupService")
	private TemplateGroupService templateGroupService;

	@Autowired
	@Qualifier("dataUnitService")
	private DataUnitService dataUnitService;

	@Autowired
	@Qualifier("targetValueService")
	private TargetValueService targetValueService;

	@RequestMapping(value = "/field/all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Field>>> getAllFields(HttpServletRequest request) {
		List<Field> fields;

		try {
			fields = fieldService.getAllFields();
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<Field>>>(
				new Response<List<Field>>(HttpStatus.OK.value(), "Template fields fetched successfully.", fields),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/field/{fieldId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Field>> getField(@PathVariable("fieldId") long fieldId) {
		Field field;

		try {
			field = fieldService.get(fieldId);
		} catch (VedantaException e) {
			log.error("Error fetching template field information");
			throw new VedantaException("Error fetching template field information");
		}
		return new ResponseEntity<Response<Field>>(
				new Response<Field>(HttpStatus.OK.value(), "Template field information fetched successfully.", field),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/field/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Field>> saveField(@RequestBody Field field) {
		Field savedField = field;
		try {
			savedField = fieldService.save(field);
		} catch (VedantaException e) {
			log.error("Error saving template field information");
			throw new VedantaException("Error saving template field information");
		}
		return new ResponseEntity<Response<Field>>(new Response<Field>(HttpStatus.OK.value(),
				"Template field information saved successfully.", savedField), HttpStatus.OK);
	}

	@RequestMapping(value = "/group/all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Group>>> getAllGroups(HttpServletRequest request) {
		List<Group> groups;

		try {
			groups = groupService.getAllGroups();
		} catch (VedantaException e) {
			log.error("Error fetching template groups");
			throw new VedantaException("Error fetching template groups");
		}
		return new ResponseEntity<Response<List<Group>>>(
				new Response<List<Group>>(HttpStatus.OK.value(), "Template groups fetched successfully.", groups),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Group>> getGroup(@PathVariable("groupId") long groupId) {
		Group group;
		try {
			group = groupService.get(groupId);
		} catch (VedantaException e) {
			log.error("Error fetching template group information");
			throw new VedantaException("Error fetching template group information");
		}
		return new ResponseEntity<Response<Group>>(
				new Response<Group>(HttpStatus.OK.value(), "Template group information fetched successfully.", group),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/group/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Group>> saveGroup(@RequestBody Group group) {
		Group savedGroup;
		try {
			savedGroup = groupService.save(group);
		} catch (VedantaException e) {
			log.error("Error saving template group information");
			throw new VedantaException("Error saving template group information");
		}
		return new ResponseEntity<Response<Group>>(new Response<Group>(HttpStatus.OK.value(),
				"Template group information saved successfully.", savedGroup), HttpStatus.OK);
	}

	@RequestMapping(value = "/group/fields/all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<GroupField>>> getAllGroupFields(HttpServletRequest request) {
		List<GroupField> groupFields;

		try {
			groupFields = groupFieldService.getAllGroupFields();
		} catch (VedantaException e) {
			log.error("Error fetching all template group field information");
			throw new VedantaException("Error fetching all template group field information");
		}
		return new ResponseEntity<Response<List<GroupField>>>(new Response<List<GroupField>>(HttpStatus.OK.value(),
				"Template groups fields fetched successfully.", groupFields), HttpStatus.OK);
	}

	@RequestMapping(value = "/group/fields/{id}", method = RequestMethod.GET)
	public ResponseEntity<Response<GroupField>> getGroupFields(@PathVariable("id") long id) {
		GroupField groupField;
		try {
			groupField = groupFieldService.get(id);
		} catch (VedantaException e) {
			log.error("Error fetching template group field information");
			throw new VedantaException("Error fetching template group field information");
		}
		return new ResponseEntity<Response<GroupField>>(new Response<GroupField>(HttpStatus.OK.value(),
				"Template group field information fetched successfully.", groupField), HttpStatus.OK);
	}

	@RequestMapping(value = "/group/fields/delete/{groupId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Boolean>> deleteGroupFields(@PathVariable("groupId") long groupId) {
		Boolean isDeleted;
		try {
			isDeleted = groupFieldService.deleteByGroup(groupId);
		} catch (VedantaException e) {
			log.error("Error fetching template group field information");
			throw new VedantaException("Error fetching template group field information");
		}
		return new ResponseEntity<Response<Boolean>>(new Response<Boolean>(HttpStatus.OK.value(),
				"Template group field information deleted successfully.", isDeleted), HttpStatus.OK);
	}

	@RequestMapping(value = "/group/fields/by-group/{groupId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<GroupField>>> getGroupFieldsByGroupId(@PathVariable("groupId") long groupId) {
		List<GroupField> groupFields;
		try {
			groupFields = groupFieldService.getAllGroupFieldsByGroupId(groupId);
		} catch (VedantaException e) {
			log.error("Error fetching all template group fields information by group");
			throw new VedantaException("Error fetching all template group fields information by group");
		}
		return new ResponseEntity<Response<List<GroupField>>>(
				new Response<List<GroupField>>(HttpStatus.OK.value(),
						"Template group fields by group id information fetched successfully.", groupFields),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/group/fields/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<GroupField>> saveGroupField(@RequestBody GroupField groupField) {
		GroupField savedGroupField;
		try {
			savedGroupField = groupFieldService.save(groupField);
		} catch (VedantaException e) {
			log.error("Error saving template group field information");
			throw new VedantaException("Error saving template group field information");
		}
		return new ResponseEntity<Response<GroupField>>(new Response<GroupField>(HttpStatus.OK.value(),
				"Template group field information saved successfully.", savedGroupField), HttpStatus.OK);
	}

	@RequestMapping(value = "/group/fields/save-list/{groupId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Boolean>> saveGroupFieldList(@RequestBody List<GroupField> groupField,
			@PathVariable("groupId") long groupId) {
		Boolean saved;
		try {
			groupFieldService.saveGroupFieldList(groupId, groupField);
			saved = true;
		} catch (VedantaException e) {
			log.error("Error saving template group field list information");
			throw new VedantaException("Error saving template group field list information");
		}
		return new ResponseEntity<Response<Boolean>>(
				new Response<Boolean>(HttpStatus.OK.value(), "Template group field list saved successfully.", saved),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Template>>> getAllTemplates(HttpServletRequest request) {
		List<Template> templates;

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			// Long userid = vedantaUser.getId();

			// for plant admin and bu admin
			if (VedantaUtility.isPlantUnitAdmin() || VedantaUtility.isBussinessUnitAdmin()) {
				templates = templateService.getAllTemplatesByBusinessUnitId(vedantaUser.getBusinessUnitId());
			}
			// for super admin
			else {
				templates = templateService.getAllTemplates();
			}

		} catch (VedantaException e) {
			log.error("Error fetching all templates");
			throw new VedantaException("Error fetching all templates");
		}
		return new ResponseEntity<Response<List<Template>>>(
				new Response<List<Template>>(HttpStatus.OK.value(), "Templates  fetched successfully.", templates),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{templateId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Template>> getTemplate(@PathVariable("templateId") long templateId) {
		Template template;

		try {
			template = templateService.get(templateId);
		} catch (VedantaException e) {
			log.error("Error fetching template information");
			throw new VedantaException("Error fetching template information");
		}
		return new ResponseEntity<Response<Template>>(
				new Response<Template>(HttpStatus.OK.value(), "Template information fetched successfully.", template),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Template>> saveTemplate(@RequestBody Template template) {
		Template savedTemplate;
		try {
			savedTemplate = templateService.save(template);
		} catch (VedantaException e) {
			log.error("Error saving template information");
			throw new VedantaException("Error saving template information");
		}
		return new ResponseEntity<Response<Template>>(new Response<Template>(HttpStatus.OK.value(),
				"Template information saved successfully.", savedTemplate), HttpStatus.OK);
	}

	@RequestMapping(value = "/get/groups/all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<TemplateGroup>>> getAllTemplateFields(HttpServletRequest request) {
		List<TemplateGroup> templateGroups;

		try {
			templateGroups = templateGroupService.getAllTemplateGroup();
		} catch (VedantaException e) {
			log.error("Error fetching all template group information");
			throw new VedantaException("Error fetching all template group information");
		}
		return new ResponseEntity<Response<List<TemplateGroup>>>(new Response<List<TemplateGroup>>(
				HttpStatus.OK.value(), "Template groups fetched successfully.", templateGroups), HttpStatus.OK);
	}

	@RequestMapping(value = "/get/groups/{id}", method = RequestMethod.GET)
	public ResponseEntity<Response<TemplateGroup>> getTemplateGroup(@PathVariable("id") long id) {
		TemplateGroup templateGroup;
		try {
			templateGroup = templateGroupService.get(id);
		} catch (VedantaException e) {
			log.error("Error fetching template group information");
			throw new VedantaException("Error fetching template group information");
		}
		return new ResponseEntity<Response<TemplateGroup>>(new Response<TemplateGroup>(HttpStatus.OK.value(),
				"Template group information fetched successfully.", templateGroup), HttpStatus.OK);
	}

	@RequestMapping(value = "/get/groups/by-template/{templateId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<TemplateGroup>>> getTemplateGroupsByGroupId(
			@PathVariable("templateId") long templateId) {
		List<TemplateGroup> templateGroups;
		try {
			templateGroups = templateGroupService.getAllTemplateGroupsByTemplateId(templateId);
		} catch (VedantaException e) {
			log.error("Error fetching group information by template");
			throw new VedantaException("Error fetching group information by template");
		}
		return new ResponseEntity<Response<List<TemplateGroup>>>(
				new Response<List<TemplateGroup>>(HttpStatus.OK.value(),
						"Template group by template information fetched successfully.", templateGroups),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/groups/by-template/{templateId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Boolean>> deleteTemplateGroupsByGroupId(
			@PathVariable("templateId") long templateId) {
		Boolean isDeleted;
		try {
			isDeleted = templateGroupService.deleteAllTemplateGroupsByTemplateId(templateId);
		} catch (VedantaException e) {
			log.error("Error deleting group information by template");
			throw new VedantaException("Error deleting group information by template");
		}
		return new ResponseEntity<Response<Boolean>>(new Response<Boolean>(HttpStatus.OK.value(),
				"Template group by template information deleted successfully.", isDeleted), HttpStatus.OK);
	}

	@RequestMapping(value = "/post/groups/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<TemplateGroup>> saveTemplateGroup(@RequestBody TemplateGroup templateGroup) {
		TemplateGroup savedTemplateGroup;
		try {
			savedTemplateGroup = templateGroupService.save(templateGroup);
		} catch (VedantaException e) {
			log.error("Error saving template group information");
			throw new VedantaException("Error saving template group information");
		}
		return new ResponseEntity<Response<TemplateGroup>>(new Response<TemplateGroup>(HttpStatus.OK.value(),
				"Template group information saved successfully.", savedTemplateGroup), HttpStatus.OK);
	}

	@RequestMapping(value = "/post/groups/save-list/{templateId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<Boolean>> saveTemplateGroupList(@RequestBody List<TemplateGroup> templateGroups,
			@PathVariable("templateId") long templateId) {
		Boolean isSaved;
		try {
			isSaved = templateGroupService.saveTemplateGroupList(templateId, templateGroups);
		} catch (VedantaException e) {
			log.error("Error saving template group information");
			throw new VedantaException("Error saving template group information");
		}
		return new ResponseEntity<Response<Boolean>>(
				new Response<Boolean>(HttpStatus.OK.value(), "Template group information saved successfully.", isSaved),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/data-unit/all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<DataUnit>>> getAllDataUnits(HttpServletRequest request) {
		List<DataUnit> dataUnits;

		try {
			dataUnits = dataUnitService.getAllDataUnits();
		} catch (VedantaException e) {
			log.error("Error fetching all data units");
			throw new VedantaException("Error fetching all data units");
		}
		return new ResponseEntity<Response<List<DataUnit>>>(
				new Response<List<DataUnit>>(HttpStatus.OK.value(), "Data units fetched successfully.", dataUnits),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/get/distinct/target", method = RequestMethod.GET)
	public ResponseEntity<Response<List<TargetValue>>> getAllDistinctTarget(HttpServletRequest request) {
		List<TargetValue> targetValues;

		try {
			targetValues = targetValueService.getAllDistinctTargetValues();
		} catch (VedantaException e) {
			log.error("Error fetching all data units");
			throw new VedantaException("Error fetching all data units");
		}
		return new ResponseEntity<Response<List<TargetValue>>>(new Response<List<TargetValue>>(HttpStatus.OK.value(),
				"distinct target values fetched successfully.", targetValues), HttpStatus.OK);
	}

	@RequestMapping(value = "/targetvalue/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<TargetValue>> saveTargetValue(@RequestBody TargetValue targetvalue) {
		TargetValue target = targetvalue;
		try {
			target = targetValueService.save(target);
		} catch (VedantaException e) {
			log.error("Error saving template field information");
			throw new VedantaException("Error saving template field information");
		}
		return new ResponseEntity<Response<TargetValue>>(new Response<TargetValue>(HttpStatus.OK.value(),
				"Template field information saved successfully.", target), HttpStatus.OK);
	}

	@RequestMapping(value = "/targetvalue/all", method = RequestMethod.GET)
	public ResponseEntity<Response<List<TargetValue>>> getAllTargetValue(HttpServletRequest request) {
		List<TargetValue> targetvalue;

		try {
			targetvalue = targetValueService.getAllTargetValues();
		} catch (VedantaException e) {
			log.error("Error fetching all template group field information");
			throw new VedantaException("Error fetching all template group field information");
		}
		return new ResponseEntity<Response<List<TargetValue>>>(new Response<List<TargetValue>>(HttpStatus.OK.value(),
				"Template groups fields fetched successfully.", targetvalue), HttpStatus.OK);
	}

	@RequestMapping(value = "/get/targetvalue/{id}", method = RequestMethod.GET)
	public ResponseEntity<Response<TargetValue>> get(@PathVariable("id") long id) {
		TargetValue targetvalue;
		try {
			targetvalue = targetValueService.get(id);
		} catch (VedantaException e) {
			log.error("Error fetching template group information");
			throw new VedantaException("Error fetching template group information");
		}
		return new ResponseEntity<Response<TargetValue>>(new Response<TargetValue>(HttpStatus.OK.value(),
				"Template group information fetched successfully.", targetvalue), HttpStatus.OK);
	}

	@RequestMapping(value = "/get/groups/getAllTemplateGroupsByBusinessUnit/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<TemplateGroup>>> getAllTemplateGroupsByBusinessUnit(
			@PathVariable("buId") Long buId, HttpServletRequest request) {
		List<TemplateGroup> templateGroups;

		try {

			templateGroups = templateGroupService.getAllTemplateGroupsByBusinessUnit(buId);
		} catch (VedantaException e) {
			log.error("Error fetching all template group information");
			throw new VedantaException("Error fetching all template group information");
		}
		return new ResponseEntity<Response<List<TemplateGroup>>>(new Response<List<TemplateGroup>>(
				HttpStatus.OK.value(), "Template groups fetched successfully.", templateGroups), HttpStatus.OK);
	}

}
