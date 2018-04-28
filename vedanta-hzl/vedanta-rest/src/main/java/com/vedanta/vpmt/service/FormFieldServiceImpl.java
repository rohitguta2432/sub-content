package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.FormFieldDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.DataUnit;
import com.vedanta.vpmt.model.FormField;
import com.vedanta.vpmt.model.FormFieldOption;
import com.vedanta.vpmt.model.FormTargetValue;
import com.vedanta.vpmt.model.FormValidation;

import lombok.extern.slf4j.Slf4j;

@Service("formFieldService")
@Slf4j
public class FormFieldServiceImpl implements FormFieldService {

	@Autowired
	private FormFieldDao formFieldDao;

	@Autowired
	@Qualifier("formFieldOptionService")
	private FormFieldOptionService formFieldOptionService;

	@Autowired
	@Qualifier("dataUnitService")
	private DataUnitService dataUnitService;

	@Autowired
	@Qualifier("formTargetValueService")
	private FormTargetValueService formTargetValueService;

	@Autowired
	@Qualifier("formValidationService")
	private FormValidationService formValidationService;

	@Override
	public FormField get(long id) {
		if (id <= 0) {
			log.info("Invalid Form Field id.");
			throw new VedantaException("Invalid Form Field id.");
		}
		try {
			return formFieldDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching form field by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public FormField save(FormField entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form Field object cannot be null/empty");
			throw new VedantaException("Form Field object cannot be null/empty");
		}
		try {

			if ((entity.getId() != null) && (entity.getId() > 0)) {
				if (!CollectionUtils.isEmpty(entity.getFormFieldOptions())) {
					List<FormFieldOption> savedFieldOptions = formFieldOptionService
							.getAllFormFieldOptionsByFieldId(entity.getId());
					if (!CollectionUtils.isEmpty(savedFieldOptions)) {
						formFieldOptionService.delete(savedFieldOptions);
					}
				}

				if (!CollectionUtils.isEmpty(entity.getFormValidations())) {
					FormValidation savedValidation = formValidationService
							.getFormValidationByFieldIdAndSubCategoryId(entity.getId(), Constants.DEFAULT_SUB_CATEGORY);
					if (!ObjectUtils.isEmpty(savedValidation)) {
						formValidationService.delete(savedValidation);
					}
				}
				if (!CollectionUtils.isEmpty(entity.getFormTargetValue())) {
					FormTargetValue savedTargetValue = formTargetValueService
							.getFormTargetValuesForSubCategory(entity.getId(), Constants.DEFAULT_SUB_CATEGORY);
					if (!ObjectUtils.isEmpty(savedTargetValue)) {
						entity.getFormTargetValue().get(0).setLastValue(savedTargetValue.getValue());
						formTargetValueService.delete(savedTargetValue);
					}
				}
			}

			FormField savedField = formFieldDao.save(entity);
			return this.get(savedField.getId());
		} catch (VedantaException e) {
			String msg = "Error while saving form field information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public FormField update(long id, FormField entity) {
		return null;
	}

	@Override
	public List<FormField> getAllFormFields() {
		try {
			List<FormField> fields = formFieldDao.getFormFieldsByStatus(Constants.STATUS_ACTIVE);
			return fields;
		} catch (VedantaException e) {
			String msg = "Error while getting all fields by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormField getFormFieldDetail(long id) {
		if (id <= 0) {
			log.info("Invalid form Field id.");
			throw new VedantaException("Invalid form Field id.");
		}
		try {
			FormField field = formFieldDao.findOne(id);
			if (!ObjectUtils.isEmpty(field)) {
				List<FormFieldOption> fieldOptions = formFieldOptionService
						.getAllFormFieldOptionsByFieldId(field.getId());
				if (!CollectionUtils.isEmpty(fieldOptions)) {
					field.setFormFieldOptions(fieldOptions);
				}
				if (field.getDataUnitId() <= 0) {
					DataUnit dataUnit = dataUnitService.get(field.getDataUnitId());
					if (!ObjectUtils.isEmpty(dataUnit)) {
						field.setDataUnit(dataUnit);
					}
				}
			}
			return field;
		} catch (VedantaException e) {
			String msg = "Error while getting field detail";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}

}
