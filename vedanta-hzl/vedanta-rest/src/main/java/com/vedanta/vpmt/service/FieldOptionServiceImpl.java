package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.FieldOptionDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FieldOption;

import lombok.extern.slf4j.Slf4j;

@Service("fieldOptionService")
@Slf4j
public class FieldOptionServiceImpl implements FieldOptionService {

	@Autowired
	private FieldOptionDao fieldOptionDao;

	@Override
	public FieldOption get(long id) {
		if (id <= 0) {
			log.info("Invalid FieldOption id.");
			throw new VedantaException("Invalid FieldOption id.");
		}
		try {
			return fieldOptionDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while saving field options";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FieldOption save(FieldOption entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("FieldOption object cannot be null/empty");
			throw new VedantaException("FieldOption object cannot be null/empty");
		}
		entity.setStatus(Constants.STATUS_ACTIVE);
		try {
			return fieldOptionDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving field options";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FieldOption update(long id, FieldOption entity) {
		return null;
	}

	@Transactional
	@Override
	public Boolean saveOptionsList(List<FieldOption> fieldOptions) {
		if (CollectionUtils.isEmpty(fieldOptions)) {
			log.info("No field options to save");
			throw new VedantaException("No field options to save");
		}
		try {
			fieldOptionDao.save(fieldOptions);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving field options list";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FieldOption> getAllFieldOptionsByFieldId(long fieldId) {
		if (fieldId <= 0) {
			log.info("Invalid FieldOption id.");
			throw new VedantaException("Invalid FieldOption id.");
		}
		try {
			return fieldOptionDao.findAllByFieldId(fieldId);
		} catch (VedantaException e) {
			String msg = "Error while fetching field options by field id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean delete(List<FieldOption> fieldOptions) {
		if (CollectionUtils.isEmpty(fieldOptions)) {
			log.info("No field options to delete");
			throw new VedantaException("No field options to delete");
		}
		try {
			fieldOptionDao.delete(fieldOptions);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting field options list";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
}
