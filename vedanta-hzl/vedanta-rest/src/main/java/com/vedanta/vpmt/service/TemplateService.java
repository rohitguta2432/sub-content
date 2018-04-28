package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.Template;

/**
 * Created by manishsanger on 11/09/17.
 */
public interface TemplateService extends VedantaService<Template> {
	List<Template> getAllTemplates();

	Template getScoreCardTemplateBySubCategoryId(long subCategoryId);

	List<Template> getTemplates(int dayOfFrequency, String frequency, long businessUnitId);

	List<Template> getAllTemplatesByBusinessUnitId(Long buId);

}
