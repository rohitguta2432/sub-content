package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormFieldOption;

import java.util.List;

public interface FormFieldOptionService extends VedantaService<FormFieldOption> {

	List<FormFieldOption> getAllFormFieldOptionsByFieldId(long fieldId);

	Boolean delete(List<FormFieldOption> fieldOptions);

}
