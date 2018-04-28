package com.vedanta.vpmt.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.FormGroupDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormGroup;

import lombok.extern.slf4j.Slf4j;

@Service("formGroupService")
@Slf4j
public class FormGroupServiceImpl implements FormGroupService {

	@Autowired
	private FormGroupDao formGroupDao;

	@Override
	public FormGroup get(long id) {
		if (id <= 0) {
			String msg = "Invalid Form Group id.";
			log.info(msg);
			throw new IllegalArgumentException(msg);
		}
		try {
			return formGroupDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching form group by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormGroup save(FormGroup entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("FormGroup object cannot be null/empty");
			throw new VedantaException("FOrmGroup object cannot be null/empty");
		}

		if (entity.getFormGroupDetailId() <= 0) {
			log.info("Invalid group id");
			throw new IllegalArgumentException("Invalid group id");
		}
		if (entity.getFormId() <= 0) {
			log.info("Invalid template id");
			throw new IllegalArgumentException("Invalid template id");
		}

		try {
			return formGroupDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving form Group information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormGroup update(long id, FormGroup entity) {
		return null;
	}

	@Override
	public List<FormGroup> getAllFormGroup() {
		try {
			List<FormGroup> formGroups = formGroupDao.findAll();
			return formGroups;
		} catch (VedantaException e) {
			String msg = "Error while fetching all form groups";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormGroup> getAllFormGroupsByFormId(long formId) {
		if (formId <= 0) {
			log.info("Invalid form id");
			throw new IllegalArgumentException("Invalid form id");
		}
		try {
			List<FormGroup> formGroups = formGroupDao.getFormGroupByFormId(formId);
			return formGroups;
		} catch (VedantaException e) {
			String msg = "Error while fetching form groups by form";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean deleteAllFormGroupsByFormId(long formId) {
		if (formId <= 0) {
			log.info("Invalid form id");
			throw new IllegalArgumentException("Invalid form id");
		}
		try {
			List<FormGroup> formGroups = formGroupDao.getFormGroupByFormId(formId);
			if (!CollectionUtils.isEmpty(formGroups)) {
				formGroupDao.delete(formGroups);
			}
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting form groups by template";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean saveFormGroupList(long formId, List<FormGroup> formGroups) {
		if (CollectionUtils.isEmpty(formGroups)) {
			log.info("Form Group list cannot be null/empty");
			throw new VedantaException("Form Group list cannot be null/empty");
		}
		try {
			List<FormGroup> savedFormGroups = this.getAllFormGroupsByFormId(formId);
			if (!CollectionUtils.isEmpty(savedFormGroups)) {
				formGroupDao.delete(savedFormGroups);
			}
			formGroupDao.save(formGroups);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving form GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormGroup> getAllFormGroupsInGroupsId(Set<Long> groupIds) {
		if (groupIds.size() < 1) {
			log.info("Invalid form group ids");
			throw new IllegalArgumentException("Invalid form group ids");
		}
		try {
			List<FormGroup> formGroups = formGroupDao.getAllFormGroupsInFormGroupDetailId(groupIds);
			return formGroups;
		} catch (VedantaException e) {
			String msg = "Error while fetching form groups by group Ids";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
}