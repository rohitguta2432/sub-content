package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.Field;

import java.util.List;

public interface FieldService extends VedantaService<Field> {
	public List<Field> getAllFields();

	public Field getFieldDetail(long id);
}
