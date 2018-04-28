package com.vedanta.vpmt.web.parser;

import java.util.List;

import com.vedanta.vpmt.model.FormField;
import com.vedanta.vpmt.model.FormGroupDetail;

public class FormGroupFieldParser {

	private FormGroupDetail formGroupDetail;
	private List<FormField> formFields;

	public FormGroupDetail getFormGroupDetail() {
		return formGroupDetail;
	}

	public void setFormGroupDetail(FormGroupDetail formGroupDetail) {
		this.formGroupDetail = formGroupDetail;
	}

	public List<FormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}
}
