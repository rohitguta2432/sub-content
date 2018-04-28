package com.vedanta.vpmt.service;

import com.vedanta.vpmt.dao.TemplateFormDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.TemplateForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service("templateFormService")
@Slf4j
public class TemplateFormServiceImpl implements TemplateFormService {

	@Autowired
	private TemplateFormDao templateFormDao;

	@Override
	public TemplateForm get(long id) {
		if (id <= 0) {
			log.info("Invalid TemplateForm id.");
			throw new VedantaException("Invalid TemplateForm id.");
		}
		return templateFormDao.findOne(id);
	}

	@Override
	public TemplateForm save(TemplateForm entity) {

		if (ObjectUtils.isEmpty(entity)) {
			log.info("TemplateForm object cannot be null/empty");
			throw new VedantaException("TemplateForm object cannot be null/empty");
		}
		return templateFormDao.save(entity);
	}

	@Override
	public TemplateForm update(long id, TemplateForm entity) {
		return null;
	}

	public List<TemplateForm> saveTemplateFormList(List<TemplateForm> templateForms) {
		if (CollectionUtils.isEmpty(templateForms)) {
			log.info("TemplateForm object cannot be null/empty");
			throw new VedantaException("TemplateForm object cannot be null/empty");
		}

		try {
			List<TemplateForm> templateFormList = templateFormDao.save(templateForms);
			return templateFormList;
		} catch (VedantaException e) {
			String msg = "Error while saving templateForm list";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<TemplateForm> getTemplateFormByTemplateId(long templateId) {
		if (templateId <= 0) {
			log.info("Not a valid templateId");
			throw new VedantaException("Not a valid templateId");
		}

		try {
			return templateFormDao.findByTemplateId(templateId);
		} catch (VedantaException e) {
			String msg = "Error while getting TemplateForm by Template id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
