package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.TemplateForm;

import java.util.List;

/**
 * Created by manishsanger on 03/10/17.
 */
public interface TemplateFormService extends VedantaService<TemplateForm> {
	public List<TemplateForm> saveTemplateFormList(List<TemplateForm> templateForms);

	public List<TemplateForm> getTemplateFormByTemplateId(long templateId);
}
