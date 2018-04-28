package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.FieldDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.DataUnit;
import com.vedanta.vpmt.model.Field;
import com.vedanta.vpmt.model.FieldOption;
import com.vedanta.vpmt.model.ScoringCriteria;
import com.vedanta.vpmt.model.TargetValue;
import com.vedanta.vpmt.model.Validation;

import lombok.extern.slf4j.Slf4j;

@Service("fieldService")
@Slf4j
public class FieldServiceImpl implements FieldService {

	@Autowired
	private FieldDao fieldDao;

	@Autowired
	@Qualifier("fieldOptionService")
	private FieldOptionService fieldOptionService;

	@Autowired
	@Qualifier("scoringCriteriaService")
	private ScoringCriteriaService scoringCriteriaService;

	@Autowired
	@Qualifier("dataUnitService")
	private DataUnitService dataUnitService;

	@Autowired
	@Qualifier("targetValueService")
	private TargetValueService targetValueService;

	@Autowired
	@Qualifier("validationService")
	private ValidationService validationService;

	@Override
	public Field get(long id) {
		if (id <= 0) {
			log.info("Invalid Field id.");
			throw new VedantaException("Invalid Field id.");
		}
		try {
			return fieldDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching field by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	// , propagation= Propagation.REQUIRED
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Field save(Field entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Field object cannot be null/empty");
			throw new VedantaException("Field object cannot be null/empty");
		}
		try {

			if ((entity.getId() != null) && (entity.getId() > 0)) {
				if (!CollectionUtils.isEmpty(entity.getFieldOptions())) {
					List<FieldOption> savedFieldOptions = fieldOptionService
							.getAllFieldOptionsByFieldId(entity.getId());
					if (!CollectionUtils.isEmpty(savedFieldOptions)) {
						fieldOptionService.delete(savedFieldOptions);
					}
				}

				if (!CollectionUtils.isEmpty(entity.getScoringCriteria())) {
					List<ScoringCriteria> savedScoringCriteria = scoringCriteriaService
							.getAllScoringCriteriaByFieldIdAndSubCategoryId(entity.getId(),
									Constants.DEFAULT_SUB_CATEGORY);
					if (!CollectionUtils.isEmpty(savedScoringCriteria)) {
						scoringCriteriaService.delete(savedScoringCriteria);
					}
				}

				if (!CollectionUtils.isEmpty(entity.getValidations())) {
					Validation savedValidation = validationService
							.getValidationByFieldIdAndSubCategoryId(entity.getId(), Constants.DEFAULT_SUB_CATEGORY);
					if (!ObjectUtils.isEmpty(savedValidation)) {
						validationService.delete(savedValidation);
					}
				}
				if (!CollectionUtils.isEmpty(entity.getTargetValue())) {
					TargetValue savedTargetValue = targetValueService.getTargetValuesForSubCategory(entity.getId(),
							Constants.DEFAULT_SUB_CATEGORY);
					if (!ObjectUtils.isEmpty(savedTargetValue)) {
						entity.getTargetValue().get(0).setLastValue(savedTargetValue.getValue());
						targetValueService.delete(savedTargetValue);
					}
				}
			}

			Field savedField = fieldDao.save(entity);
			return this.get(savedField.getId());
		} catch (VedantaException e) {
			String msg = "Error while saving field information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Field update(long id, Field entity) {
		return null;
	}

	@Override
	public List<Field> getAllFields() {
		try {
			List<Field> fields = fieldDao.getAllFields(Constants.STATUS_ACTIVE);
			return fields;
		} catch (VedantaException e) {
			String msg = "Error while getting all fields by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Field getFieldDetail(long id) {
		if (id <= 0) {
			log.info("Invalid Field id.");
			throw new VedantaException("Invalid Field id.");
		}
		try {
			Field field = fieldDao.findOne(id);
			if (!ObjectUtils.isEmpty(field)) {
				List<FieldOption> fieldOptions = fieldOptionService.getAllFieldOptionsByFieldId(field.getId());
				if (!CollectionUtils.isEmpty(fieldOptions)) {
					field.setFieldOptions(fieldOptions);
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
