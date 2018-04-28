package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.FormGroupFieldDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormGroupField;

import lombok.extern.slf4j.Slf4j;

@Service("formGroupFieldService")
@Slf4j
public class FormGroupFieldServiceImpl implements FormGroupFieldService {

	@Autowired
	private FormGroupFieldDao formGroupFieldDao;

	@Override
	public FormGroupField get(long id) {
		if (id <= 0) {
			String msg = "Invalid Form Group Field id.";
			log.info(msg);
			throw new IllegalArgumentException(msg);
		}
		try {
			return formGroupFieldDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching formGroupField by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormGroupField save(FormGroupField entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form GroupField object cannot be null/empty");
			throw new VedantaException("Form GroupField object cannot be null/empty");
		}

		if (entity.getFormFieldId() <= 0) {
			log.info("Invalid form field id");
			throw new IllegalArgumentException("Invalid form field id");
		}
		if (entity.getFormGroupDetailId() <= 0) {
			log.info("Invalid form group detail id");
			throw new IllegalArgumentException("Invalid form group detail id");
		}

		try {
			return formGroupFieldDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving form GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormGroupField update(long id, FormGroupField entity) {
		return null;
	}

	@Override
	public boolean saveFormGroupFieldList(Long groupId, List<FormGroupField> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("Form GroupField list cannot be null/empty");
			throw new VedantaException("Form GroupField list cannot be null/empty");
		}
		try {
			List<FormGroupField> savedGroupFields = this.getAllFormGroupFieldsByGroupId(groupId);
			if (!CollectionUtils.isEmpty(savedGroupFields)) {
				formGroupFieldDao.delete(savedGroupFields);
			}
			formGroupFieldDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormGroupField> getAllFormGroupFields() {
		try {
			List<FormGroupField> fields = formGroupFieldDao.findAll();
			return fields;
		} catch (VedantaException e) {
			String msg = "Error while getting all form group field";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormGroupField> getAllFormGroupFieldsByGroupId(long groupId) {
		if (groupId <= 0) {
			log.info("Invalid form group id");
			throw new IllegalArgumentException("Invalid form group id");
		}
		try {
			List<FormGroupField> fields = formGroupFieldDao.getFormGroupFieldByformGroupDetailId(groupId);
			return fields;
		} catch (VedantaException e) {
			String msg = "Error while fetching group fields by group id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public boolean deleteByFormGroup(long groupId) {
		if (groupId <= 0) {
			log.info("Invalid form group id");
			throw new IllegalArgumentException("Invalid form group id");
		}

		try {
			List<FormGroupField> savedGroupFields = this.getAllFormGroupFieldsByGroupId(groupId);
			if (!CollectionUtils.isEmpty(savedGroupFields)) {
				formGroupFieldDao.delete(savedGroupFields);
			}
			return true;
		} catch (VedantaException e) {
			String msg = "Error while removing form GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}
}
