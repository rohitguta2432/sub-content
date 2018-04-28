package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.FormValidationDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormValidation;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 26/09/17.
 */
@Service("formValidationService")
@Slf4j
public class FormValidationServiceImpl implements FormValidationService {

	@Autowired
	private FormValidationDao formValidationDao;

	@Override
	public FormValidation get(long id) {
		if (id <= 0) {
			log.info("Invalid form validation id.");
			throw new VedantaException("Invalid form validation id.");
		}
		try {
			return formValidationDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching form validation by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormValidation save(FormValidation entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form validation object cannot be null/empty");
			throw new VedantaException("Form validation object cannot be null/empty");
		}
		try {
			FormValidation savedValidation = formValidationDao.save(entity);
			return savedValidation;
		} catch (VedantaException e) {
			String msg = "Error while saving form validation information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public FormValidation update(long id, FormValidation entity) {
		return null;
	}

	@Override
	public Boolean saveFormValidationList(List<FormValidation> entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form validation object list cannot be null/empty");
			throw new VedantaException("Form validation object list cannot be null/empty");
		}
		try {
			formValidationDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving form validation list information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public List<FormValidation> getAllFormValidations() {
		try {
			List<FormValidation> validations = formValidationDao.findAll();
			return validations;
		} catch (VedantaException e) {
			String msg = "Error while getting all form validations";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormValidation> getAllFormValidationsByFieldId(long fieldId) {
		try {
			List<FormValidation> validations = formValidationDao.findAllByFormFieldId(fieldId);
			return validations;
		} catch (VedantaException e) {
			String msg = "Error while getting all form validations by field";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormValidation getFormValidationByFieldIdAndSubCategoryId(long fieldId, long subCategoryId) {
		try {
			FormValidation validation = formValidationDao.getOneByFormFieldIdAndSubCategoryId(fieldId, subCategoryId);
			return validation;
		} catch (VedantaException e) {
			String msg = "Error while getting all validations by field";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean delete(List<FormValidation> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("Form Validation object list cannot be null/empty");
			throw new VedantaException("Form Validation object list cannot be null/empty");
		}
		try {
			formValidationDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting form validation information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean delete(FormValidation entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form Validation object cannot be null/empty");
			throw new VedantaException("Form Validation object cannot be null/empty");
		}
		try {
			formValidationDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting form validation information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

}
