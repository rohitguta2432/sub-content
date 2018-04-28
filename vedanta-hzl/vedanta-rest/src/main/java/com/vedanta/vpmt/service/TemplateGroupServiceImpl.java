package com.vedanta.vpmt.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.TemplateGroupDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.TemplateGroup;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 11/09/17.
 */
@Service("templateGroupService")
@Slf4j
public class TemplateGroupServiceImpl implements TemplateGroupService {

	@Autowired
	private TemplateGroupDao templateGroupDao;

	@Override
	public TemplateGroup get(long id) {
		if (id <= 0) {
			String msg = "Invalid Template Group id.";
			log.info(msg);
			throw new IllegalArgumentException(msg);
		}
		try {
			return templateGroupDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching template group by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public TemplateGroup save(TemplateGroup entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("TemplateGroup object cannot be null/empty");
			throw new VedantaException("GroupTemplate object cannot be null/empty");
		}

		if (entity.getGroupId() <= 0) {
			log.info("Invalid group id");
			throw new IllegalArgumentException("Invalid group id");
		}
		if (entity.getTemplateId() <= 0) {
			log.info("Invalid template id");
			throw new IllegalArgumentException("Invalid template id");
		}

		try {
			return templateGroupDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving Template Group information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public TemplateGroup update(long id, TemplateGroup entity) {
		return null;
	}

	@Override
	public List<TemplateGroup> getAllTemplateGroup() {
		try {
			List<TemplateGroup> templateGroups = (List<TemplateGroup>) templateGroupDao.findAll();
			return templateGroups;
		} catch (VedantaException e) {
			String msg = "Error while fetching all template groups";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<TemplateGroup> getAllTemplateGroupsByTemplateId(long templateId) {
		if (templateId <= 0) {
			log.info("Invalid template id");
			throw new IllegalArgumentException("Invalid template id");
		}
		try {
			List<TemplateGroup> templateGroups = templateGroupDao.findAllByTemplateId(templateId);
			return templateGroups;
		} catch (VedantaException e) {
			String msg = "Error while fetching template groups by template";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean deleteAllTemplateGroupsByTemplateId(long templateId) {
		if (templateId <= 0) {
			log.info("Invalid template id");
			throw new IllegalArgumentException("Invalid template id");
		}
		try {
			List<TemplateGroup> templateGroups = templateGroupDao.findAllByTemplateId(templateId);
			if (!CollectionUtils.isEmpty(templateGroups)) {
				templateGroupDao.delete(templateGroups);
			}
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting template groups by template";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean saveTemplateGroupList(long templateId, List<TemplateGroup> templateGroups) {
		if (CollectionUtils.isEmpty(templateGroups)) {
			log.info("Template Group list cannot be null/empty");
			throw new VedantaException("Template Group list cannot be null/empty");
		}
		try {
			List<TemplateGroup> savedTemplateGroups = this.getAllTemplateGroupsByTemplateId(templateId);
			if (!CollectionUtils.isEmpty(savedTemplateGroups)) {
				templateGroupDao.delete(savedTemplateGroups);
			}
			templateGroupDao.save(templateGroups);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<TemplateGroup> getAllTemplateGroupsInGroupsId(Set<Long> groupIds) {
		if (groupIds.size() < 1) {
			log.info("Invalid group ids");
			throw new IllegalArgumentException("Invalid group ids");
		}
		try {
			List<TemplateGroup> templateGroups = templateGroupDao.getAllTemplateGroupsInGroupsId(groupIds);
			return templateGroups;
		} catch (VedantaException e) {
			String msg = "Error while fetching template groups by group Ids";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<TemplateGroup> getAllTemplateGroupsByBusinessUnit(Long buId) {

		try {
			List<TemplateGroup> templateGroups = templateGroupDao.getAllTemplateGroupsByBusinessUnitId(buId);
			return templateGroups;
		} catch (VedantaException e) {
			String msg = "Error while fetching template groups by businessUnit";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}
}
