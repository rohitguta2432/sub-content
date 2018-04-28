package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.FormFieldOptionDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormFieldOption;

import lombok.extern.slf4j.Slf4j;

@Service("formFieldOptionService")
@Slf4j
public class FormFieldOptionServiceImpl implements FormFieldOptionService {

	@Autowired
	private FormFieldOptionDao formFieldOptionDao;

	@Override
	public FormFieldOption get(long id) {
		if (id <= 0) {
			log.info("Invalid form FieldOption id.");
			throw new VedantaException("Invalid form FieldOption id.");
		}
		try {
			return formFieldOptionDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching form field options";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormFieldOption save(FormFieldOption entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form FieldOption object cannot be null/empty");
			throw new VedantaException("Form FieldOption object cannot be null/empty");
		}
		try {
			entity.setStatus(Constants.STATUS_ACTIVE);
			return formFieldOptionDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving form field options";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormFieldOption update(long id, FormFieldOption entity) {
		return null;
	}

	@Override
	public List<FormFieldOption> getAllFormFieldOptionsByFieldId(long fieldId) {
		if (fieldId <= 0) {
			log.info("Invalid form FieldOption id.");
			throw new VedantaException("Invalid form FieldOption id.");
		}
		try {
			return formFieldOptionDao.findAllByFormFieldId(fieldId);
		} catch (VedantaException e) {
			String msg = "Error while fetching form field options by field id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean delete(List<FormFieldOption> formFieldOptions) {
		if (CollectionUtils.isEmpty(formFieldOptions)) {
			log.info("No form field options to delete");
			throw new VedantaException("No form field options to delete");
		}
		try {
			formFieldOptionDao.delete(formFieldOptions);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting form field options list";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
}
