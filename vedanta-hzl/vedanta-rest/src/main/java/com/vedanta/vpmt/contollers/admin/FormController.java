package com.vedanta.vpmt.contollers.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.DataUnit;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.FormField;
import com.vedanta.vpmt.model.FormGroup;
import com.vedanta.vpmt.model.FormGroupDetail;
import com.vedanta.vpmt.model.FormGroupField;
import com.vedanta.vpmt.model.FormTargetValue;
import com.vedanta.vpmt.service.DataUnitService;
import com.vedanta.vpmt.service.FormFieldService;
import com.vedanta.vpmt.service.FormGroupDetailService;
import com.vedanta.vpmt.service.FormGroupFieldService;
import com.vedanta.vpmt.service.FormGroupService;
import com.vedanta.vpmt.service.FormService;
import com.vedanta.vpmt.service.FormTargetValueService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 11/09/17.
 */
@RestController
@RequestMapping(value = "/admin/form", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FormController {

    @Autowired
    @Qualifier("formFieldService")
    private FormFieldService formFieldService;

    @Autowired
    @Qualifier("formGroupDetailService")
    private FormGroupDetailService formGroupDetailService;

    @Autowired
    @Qualifier("formGroupFieldService")
    private FormGroupFieldService formGroupFieldService;

    @Autowired
    @Qualifier("formService")
    private FormService formService;

    @Autowired
    @Qualifier("formGroupService")
    private FormGroupService formGroupService;

    @Autowired
    @Qualifier("dataUnitService")
    private DataUnitService dataUnitService;

    @Autowired
    @Qualifier("formTargetValueService")
    private FormTargetValueService formTargetValueService;

    @RequestMapping(value = "/field/all", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormField>>> getAllFormFields(HttpServletRequest request) {
        List<FormField> formFields;

        try {
            formFields = formFieldService.getAllFormFields();
        } catch (VedantaException e) {
            log.error("Error fetching all form fields");
            throw new VedantaException("Error fetching all form fields");
        }
        return new ResponseEntity<Response<List<FormField>>>(new Response<List<FormField>>(
                HttpStatus.OK.value(), "Form fields fetched successfully.", formFields),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/field/{formFieldId}", method = RequestMethod.GET)
    public ResponseEntity<Response<FormField>> getFormField(@PathVariable("formFieldId") long formFieldId) {
        FormField formField;

        try {
            formField = formFieldService.get(formFieldId);
        } catch (VedantaException e) {
            log.error("Error fetching form field information");
            throw new VedantaException("Error fetching form field information");
        }
        return new ResponseEntity<Response<FormField>>(new Response<FormField>(
                HttpStatus.OK.value(), "Form field information fetched successfully.", formField),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/field/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<FormField>> saveFormField(
            @RequestBody FormField formField) throws JsonParseException, JsonMappingException, IOException {
        FormField savedFormField;
        try {
            savedFormField = formFieldService.save(formField);
        } catch (VedantaException e) {
            log.error("Error saving template field information");
            throw new VedantaException("Error saving template field information");
        }
        return new ResponseEntity<Response<FormField>>(new Response<FormField>(
                HttpStatus.OK.value(), "Form field information saved successfully.", savedFormField), HttpStatus.OK);
    }


    @RequestMapping(value = "/group-detail/all", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormGroupDetail>>> getAllFormGroups(HttpServletRequest request) {
        List<FormGroupDetail> groups;

        try {
            groups = formGroupDetailService.getAllFormGroupDetails();
        } catch (VedantaException e) {
            log.error("Error fetching form groups");
            throw new VedantaException("Error fetching form groups");
        }
        return new ResponseEntity<Response<List<FormGroupDetail>>>(new Response<List<FormGroupDetail>>(
                HttpStatus.OK.value(), "Form groups details fetched successfully.", groups),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/group-detail/{groupDetailId}", method = RequestMethod.GET)
    public ResponseEntity<Response<FormGroupDetail>> getGroup(@PathVariable("groupDetailId") long groupDetailId) {
        FormGroupDetail group;
        try {
            group = formGroupDetailService.get(groupDetailId);
        } catch (VedantaException e) {
            log.error("Error fetching form group detail information");
            throw new VedantaException("Error fetching form group detail information");
        }
        return new ResponseEntity<Response<FormGroupDetail>>(new Response<FormGroupDetail>(
                HttpStatus.OK.value(), "Form group information fetched successfully.", group),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/group-detail/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<FormGroupDetail>> saveGroup(
            @RequestBody FormGroupDetail formGroupDetail) throws JsonParseException, JsonMappingException, IOException {
        FormGroupDetail savedGroup;
        try {
            savedGroup = formGroupDetailService.save(formGroupDetail);
        } catch (VedantaException e) {
            log.error("Error saving template group information");
            throw new VedantaException("Error saving template group information");
        }
        return new ResponseEntity<Response<FormGroupDetail>>(new Response<FormGroupDetail>(
                HttpStatus.OK.value(), "Form group detail information saved successfully.", savedGroup), HttpStatus.OK);
    }

    @RequestMapping(value = "/group/fields/all", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormGroupField>>> getAllFormGroupFields(HttpServletRequest request) {
        List<FormGroupField> groupFields;

        try {
            groupFields = formGroupFieldService.getAllFormGroupFields();
        } catch (VedantaException e) {
            log.error("Error fetching all form group field information");
            throw new VedantaException("Error fetching all form group field information");
        }
        return new ResponseEntity<Response<List<FormGroupField>>>(new Response<List<FormGroupField>>(
                HttpStatus.OK.value(), "Form groups fields fetched successfully.", groupFields),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/group/fields/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<FormGroupField>> getGroupFields(@PathVariable("id") long id) {
        FormGroupField groupField;
        try {
            groupField = formGroupFieldService.get(id);
        } catch (VedantaException e) {
            log.error("Error fetching template group field information");
            throw new VedantaException("Error fetching template group field information");
        }
        return new ResponseEntity<Response<FormGroupField>>(new Response<FormGroupField>(
                HttpStatus.OK.value(), "Form group field information fetched successfully.", groupField),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/group/fields/delete/{formGroupId}", method = RequestMethod.GET)
    public ResponseEntity<Response<Boolean>> deleteGroupFields(@PathVariable("formGroupId") long formGroupId) {
        Boolean isDeleted;
        try {
            isDeleted = formGroupFieldService.deleteByFormGroup(formGroupId);
        } catch (VedantaException e) {
            log.error("Error fetching form group field information");
            throw new VedantaException("Error deleting form group field information");
        }
        return new ResponseEntity<Response<Boolean>>(new Response<Boolean>(
                HttpStatus.OK.value(), "Form group field information deleted successfully.", isDeleted),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/group/fields/by-group/{groupDetailId}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormGroupField>>> getGroupFieldsByGroupId(@PathVariable("groupDetailId") long groupDetailId) {
        List<FormGroupField> groupFields;
        try {
            groupFields = formGroupFieldService.getAllFormGroupFieldsByGroupId(groupDetailId);
        } catch (VedantaException e) {
            log.error("Error fetching all form group fields information by group");
            throw new VedantaException("Error fetching all form group fields information by group");
        }
        return new ResponseEntity<Response<List<FormGroupField>>>(new Response<List<FormGroupField>>(
                HttpStatus.OK.value(), "Form group fields by group id information fetched successfully.", groupFields),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/group/fields/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<FormGroupField>> saveFormGroupField(
            @RequestBody FormGroupField formGroupField) throws JsonParseException, JsonMappingException, IOException {
        FormGroupField savedGroupField;
        try {
            savedGroupField = formGroupFieldService.save(formGroupField);
        } catch (VedantaException e) {
            log.error("Error saving form group field information");
            throw new VedantaException("Error saving form group field information");
        }
        return new ResponseEntity<Response<FormGroupField>>(new Response<FormGroupField>(
                HttpStatus.OK.value(), "Form group field information saved successfully.", savedGroupField), HttpStatus.OK);
    }

    @RequestMapping(value = "/group/fields/save-list/{formGroupDetailId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Boolean>> saveFormGroupFieldList(
            @RequestBody List<FormGroupField> formGroupFields,
            @PathVariable("formGroupDetailId") long formGroupDetailId) {
        Boolean saved;
        try {
            formGroupFieldService.saveFormGroupFieldList(formGroupDetailId, formGroupFields);
            saved = true;
        } catch (VedantaException e) {
            log.error("Error saving Form group field list information");
            throw new VedantaException("Error saving Form group field list information");
        }
        return new ResponseEntity<Response<Boolean>>(new Response<Boolean>(
                HttpStatus.OK.value(), "Form group field list saved successfully.", saved), HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Response<List<Form>>> getAllForms(HttpServletRequest request) {
        List<Form> forms;

        try {
            forms = formService.getAllForms();
            ////(forms);
        } catch (VedantaException e) {
            log.error("Error fetching all forms");
            throw new VedantaException("Error fetching all forms");
        }
        return new ResponseEntity<Response<List<Form>>>(new Response<List<Form>>(
                HttpStatus.OK.value(), "Forms  fetched successfully.", forms),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{formId}", method = RequestMethod.GET)
    public ResponseEntity<Response<Form>> getForm(@PathVariable("formId") long formId) {
        Form form;

        try {
            form = formService.get(formId);
        } catch (VedantaException e) {
            log.error("Error fetching form information");
            throw new VedantaException("Error fetching form information");
        }
        return new ResponseEntity<Response<Form>>(new Response<Form>(
                HttpStatus.OK.value(), "Form information fetched successfully.", form),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Form>> saveForm(
            @RequestBody Form form) throws JsonParseException, JsonMappingException, IOException {
        Form savedForm;
        try {
            savedForm = formService.save(form);
        } catch (VedantaException e) {
            log.error("Error saving from information");
            throw new VedantaException("Error saving form information");
        }
        return new ResponseEntity<Response<Form>>(new Response<Form>(
                HttpStatus.OK.value(), "Form information saved successfully.", savedForm), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/groups/all", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormGroup>>> getAllFormGroupList(HttpServletRequest request) {
        List<FormGroup> formGroups;

        try {
            formGroups = formGroupService.getAllFormGroup();
        } catch (VedantaException e) {
            log.error("Error fetching all form group information");
            throw new VedantaException("Error fetching all form group information");
        }
        return new ResponseEntity<Response<List<FormGroup>>>(new Response<List<FormGroup>>(
                HttpStatus.OK.value(), "Form groups fetched successfully.", formGroups),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/get/groups/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<FormGroup>> getFormGroup(@PathVariable("id") long id) {
        FormGroup fromGroup;
        try {
            fromGroup = formGroupService.get(id);
        } catch (VedantaException e) {
            log.error("Error fetching form group information");
            throw new VedantaException("Error fetching form group information");
        }
        return new ResponseEntity<Response<FormGroup>>(new Response<FormGroup>(
                HttpStatus.OK.value(), "Form group information fetched successfully.", fromGroup),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/get/groups/by-from/{formId}", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormGroup>>> getTemplateGroupsByGroupId(@PathVariable("formId") long formId) {
        List<FormGroup> formGroups;
        try {
            formGroups = formGroupService.getAllFormGroupsByFormId(formId);
        } catch (VedantaException e) {
            log.error("Error fetching group information by form");
            throw new VedantaException("Error fetching group information by form");
        }
        return new ResponseEntity<Response<List<FormGroup>>>(new Response<List<FormGroup>>(
                HttpStatus.OK.value(), "Form groups fetched successfully.", formGroups),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/groups/by-form/{formId}", method = RequestMethod.GET)
    public ResponseEntity<Response<Boolean>> deleteTemplateGroupsByGroupId(@PathVariable("formId") long formId) {
        Boolean isDeleted;
        try {
            isDeleted = formGroupService.deleteAllFormGroupsByFormId(formId);
        } catch (VedantaException e) {
            log.error("Error deleting group information by form");
            throw new VedantaException("Error deleting group information by form");
        }
        return new ResponseEntity<Response<Boolean>>(new Response<Boolean>(
                HttpStatus.OK.value(), "Form group by form id information deleted successfully.", isDeleted),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/post/groups/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<FormGroup>> saveFormGroup(
            @RequestBody FormGroup formGroup) throws JsonParseException, JsonMappingException, IOException {
        FormGroup savedFormGroup;
        try {
            savedFormGroup = formGroupService.save(formGroup);
        } catch (VedantaException e) {
            log.error("Error saving form group information");
            throw new VedantaException("Error saving form group information");
        }
        return new ResponseEntity<Response<FormGroup>>(new Response<FormGroup>(
                HttpStatus.OK.value(), "Form group information saved successfully.", savedFormGroup), HttpStatus.OK);
    }

    @RequestMapping(value = "/post/groups/save-list/{formId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Boolean>> saveTemplateGroupList(
            @RequestBody List<FormGroup> formGroups,
            @PathVariable("formId") long formId) {
        Boolean isSaved;
        try {
            isSaved = formGroupService.saveFormGroupList(formId, formGroups);
        } catch (VedantaException e) {
            log.error("Error saving form group information");
            throw new VedantaException("Error saving form group information");
        }
        return new ResponseEntity<Response<Boolean>>(new Response<Boolean>(
                HttpStatus.OK.value(), "Template group information saved successfully.", isSaved), HttpStatus.OK);
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
        return new ResponseEntity<Response<List<DataUnit>>>(new Response<List<DataUnit>>(
                HttpStatus.OK.value(), "Data units fetched successfully.", dataUnits),
                HttpStatus.OK);
    }


    @RequestMapping(value = "/get/distinct/target", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormTargetValue>>> getAllDistinctFormTargetValues(HttpServletRequest request) {
        List<FormTargetValue> formTargetValues;

        try {
            formTargetValues = formTargetValueService.getAllDistinctFormTargetValues();
        } catch (VedantaException e) {
            log.error("Error fetching distinct form target values");
            throw new VedantaException("Error fetching distinct form target values");
        }
        return new ResponseEntity<Response<List<FormTargetValue>>>(new Response<List<FormTargetValue>>(
                HttpStatus.OK.value(), "distinct target values fetched successfully.", formTargetValues),
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/target-value/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<FormTargetValue>> saveFormTargetValue(
            @RequestBody FormTargetValue targetvalue) throws JsonParseException, JsonMappingException, IOException {
        FormTargetValue target = targetvalue;
        try {
        	target = formTargetValueService.save(target);
        } catch (VedantaException e) {
            log.error("Error saving template field information");
            throw new VedantaException("Error saving template field information");
        }
        return new ResponseEntity<Response<FormTargetValue>>(new Response<FormTargetValue>(
                HttpStatus.OK.value(), "Form target value saved successfully.", target), HttpStatus.OK);
    }
    @RequestMapping(value = "/target-value/all", method = RequestMethod.GET)
    public ResponseEntity<Response<List<FormTargetValue>>> getAllFormTargetValue(HttpServletRequest request) {
        List<FormTargetValue> targetValue;

        try {
        	targetValue = formTargetValueService.getAllFormTargetValues();
        } catch (VedantaException e) {
            log.error("Error fetching all for target value information");
            throw new VedantaException("Error fetching all form target value information");
        }
        return new ResponseEntity<Response<List<FormTargetValue>>>(new Response<List<FormTargetValue>>(
                HttpStatus.OK.value(), "Form target values fetched successfully.", targetValue),
                HttpStatus.OK);
    }
    
    @RequestMapping(value = "/get/target-value/{id}", method = RequestMethod.GET)
    public ResponseEntity<Response<FormTargetValue>> get(@PathVariable("id") long id) {
        FormTargetValue targetValue;
        try {
        	targetValue = formTargetValueService.get(id) ;
        } catch (VedantaException e) {
            log.error("Error fetching Form target value information");
            throw new VedantaException("Error fetching form target value information");
        }
        return new ResponseEntity<Response<FormTargetValue>>(new Response<FormTargetValue>(
                HttpStatus.OK.value(), "Form target value information fetched successfully.", targetValue),
                HttpStatus.OK);
    }

}
