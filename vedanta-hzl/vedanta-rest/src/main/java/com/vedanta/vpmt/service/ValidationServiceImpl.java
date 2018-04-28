package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.ValidationDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Validation;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 26/09/17.
 */
@Service("validationService")
@Slf4j
public class ValidationServiceImpl implements ValidationService {

	@Autowired
	private ValidationDao validationDao;

	@Override
	public Validation get(long id) {
		if (id <= 0) {
			log.info("Invalid validation id.");
			throw new VedantaException("Invalid validation id.");
		}
		try {
			return validationDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching unit by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Validation save(Validation entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Validation object cannot be null/empty");
			throw new VedantaException("Validation object cannot be null/empty");
		}
		try {
			Validation savedValidation = validationDao.save(entity);
			return savedValidation;
		} catch (VedantaException e) {
			String msg = "Error while saving validation information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Validation update(long id, Validation entity) {
		return null;
	}

	@Override
	public Boolean saveValidationList(List<Validation> entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Validation object list cannot be null/empty");
			throw new VedantaException("Validation object list cannot be null/empty");
		}
		try {
			validationDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving validation list information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public List<Validation> getAllValidations() {
		try {
			List<Validation> validations = validationDao.findAll();
			return validations;
		} catch (VedantaException e) {
			String msg = "Error while getting all validations";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Validation> getAllValidationsByFieldId(long fieldId) {
		try {
			List<Validation> validations = validationDao.findAllByFieldId(fieldId);
			return validations;
		} catch (VedantaException e) {
			String msg = "Error while getting all validations by field";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Validation getValidationByFieldIdAndSubCategoryId(long fieldId, long subCategoryId) {
		try {
			Validation validation = validationDao.getOneByFieldIdAndSubCategoryId(fieldId, subCategoryId);
			return validation;
		} catch (VedantaException e) {
			String msg = "Error while getting all validations by field";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean delete(List<Validation> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("Validation object list cannot be null/empty");
			throw new VedantaException("Validation object list cannot be null/empty");
		}
		try {
			validationDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting validation information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean delete(Validation entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Validation object cannot be null/empty");
			throw new VedantaException("Validation object cannot be null/empty");
		}
		try {
			validationDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting validation information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

}
