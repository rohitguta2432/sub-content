package com.vedanta.vpmt.service;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.TemplateDao;
import com.vedanta.vpmt.dao.TemplateFormDao;
import com.vedanta.vpmt.dao.TemplateGroupDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by manishsanger on 11/09/17.
 */
@Service("templateService")
@Slf4j
public class TemplateServiceImpl implements TemplateService {
	@Autowired
	private TemplateDao templateDao;

	@Autowired
	private TemplateFormDao templateFormDao;

	@Autowired
	private TemplateGroupService templateGroupService;

	@Override
	public Template get(long id) {
		if (id <= 0) {
			log.info("Invalid template id.");
			throw new VedantaException("Invalid template id.");
		}
		try {
			return templateDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching template group by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	@Transactional
	public Template save(Template entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("TemplateGroup object cannot be null/empty");
			throw new VedantaException("GroupTemplate object cannot be null/empty");
		}

		if (StringUtils.isEmpty(entity.getName())) {
			log.info("Template name can't be empty");
			throw new IllegalArgumentException("Template name can't be empty");
		}

		if (!ObjectUtils.isEmpty(entity.getId())) {
			templateFormDao.deleteByTemplateId(entity.getId());
		}

		// if status =4(delete) then delete template group as well
		if (entity.getStatus() == 4) {
			templateGroupService.deleteAllTemplateGroupsByTemplateId(entity.getId());
		}

		try {
			entity.setSlug(String.valueOf((new Date().getTime())));
			return templateDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving Template information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Template update(long id, Template entity) {
		return null;
	}

	@Override
	public List<Template> getAllTemplates() {
		try {
			List<Template> templates = templateDao.getAllTemplates(Constants.STATUS_ACTIVE);
			return templates;
		} catch (VedantaException e) {
			String msg = "Error while fetching all templates";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Template getScoreCardTemplateBySubCategoryId(long subCategoryId) {

		if (subCategoryId <= 0) {
			log.info("Not a valid Sub category");
			throw new IllegalArgumentException("Not a valid sub category");
		}

		try {
			Template template = templateDao.findOneBySubCategoryId(subCategoryId, Constants.STATUS_ACTIVE);
			if (ObjectUtils.isEmpty(template)) {
				String msg = "No template found with given sub category";
				log.info(msg);
				throw new VedantaException(msg);
			}
			return template;
		} catch (VedantaException e) {
			String msg = "Error while saving Template information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Template> getTemplates(int dayOfFrequency, String frequency, long businessUnitId) {
		if (dayOfFrequency < 1) {
			log.info("Not a valid day of frequency");
			throw new IllegalArgumentException("Not a valid day of frequency");
		}

		if (StringUtils.isEmpty(frequency)) {
			log.info("Not a valid frequency");
			throw new IllegalArgumentException("Not a valid frequency");
		}

		try {
			List<Template> templates = templateDao.findAllByDayOfFrequencyAndFrequency(dayOfFrequency, frequency,
					businessUnitId);
			return (CollectionUtils.isEmpty(templates)) ? Collections.emptyList() : templates;
		} catch (Exception e) {
			String msg = "Error while fetching Template information for given day of frequency " + frequency
					+ " and frequency " + frequency;
			log.error(msg, e);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Template> getAllTemplatesByBusinessUnitId(Long buId) {
		try {
			return templateDao.getAllTemplatesByBusinessUnitId(Constants.STATUS_ACTIVE, buId);
		} catch (Exception e) {
			String msg = "Error in fetch the Templates for Business Unit Id" + buId;
			log.error(msg, e);
			throw new VedantaException(msg);
		}
	}

}