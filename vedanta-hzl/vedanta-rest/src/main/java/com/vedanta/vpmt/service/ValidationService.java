package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.Validation;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
public interface ValidationService extends VedantaService<Validation> {
	public Boolean saveValidationList(List<Validation> entity);

	public List<Validation> getAllValidations();

	public List<Validation> getAllValidationsByFieldId(long fieldId);

	public Validation getValidationByFieldIdAndSubCategoryId(long fieldId, long subCategoryId);

	public Boolean delete(List<Validation> entity);

	public Boolean delete(Validation entity);
}
