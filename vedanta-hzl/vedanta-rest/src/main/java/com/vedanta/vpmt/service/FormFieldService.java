package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormField;

import java.util.List;

public interface FormFieldService extends VedantaService<FormField> {
	public List<FormField> getAllFormFields();

	public FormField getFormFieldDetail(long id);
}
