package com.vedanta.vpmt.service;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.FormDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service("formService")
@Slf4j
public class FormServiceImpl implements FormService {

	@Autowired
	private FormDao formDao;

	@Override
	public Form get(long id) {
		if (id <= 0) {
			log.info("Invalid form id.");
			throw new VedantaException("Invalid form id.");
		}
		try {
			return formDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching form by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Form save(Form entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form object cannot be null/empty");
			throw new VedantaException("Form object cannot be null/empty");
		}

		if (StringUtils.isEmpty(entity.getName())) {
			log.info("Form name can't be empty");
			throw new IllegalArgumentException("Form name can't be empty");
		}

		try {
			entity.setSlug(String.valueOf((new Date().getTime())));
			return formDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving form information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Form update(long id, Form entity) {
		return null;
	}

	@Override
	public List<Form> getAllForms() {
		try {
			List<Form> forms = null;

			if (VedantaUtility.getCurrentUserBuId() == 0
					&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
				forms = formDao.getAllForms(Constants.STATUS_ACTIVE);
			} else {
				forms = formDao.getAllForms(Constants.STATUS_ACTIVE, VedantaUtility.getCurrentUserBuId());
			}

			return forms;
		} catch (VedantaException e) {
			String msg = "Error while fetching all forms";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Form> getFormsBySubCategoryId(long subCategoryId) {

		try {
			List<Form> forms = formDao.getAllBySubCategoryId(subCategoryId);
			if (CollectionUtils.isEmpty(forms)) {
				String msg = "No form found with given sub category";
				log.info(msg);
				throw new VedantaException(msg);
			}
			return forms;
		} catch (VedantaException e) {
			String msg = "Error while fetching form by sub category information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Form> getFormsBySubCategoryIdForAdmin(long subCategoryId) {

		try {
			List<Form> forms = formDao.getAllBySubCategoryIdForAdmin(subCategoryId);
			return forms;
		} catch (VedantaException e) {
			String msg = "Error while fetching form by sub category information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Form> getForms(int dayOfFrequency, String frequency, Long businessUnitId) {
		if (dayOfFrequency < 0) {
			log.info("Not a valid day of frequency");
			throw new IllegalArgumentException("Not a valid day of frequency");
		}

		if (StringUtils.isEmpty(frequency)) {
			log.info("Not a valid frequency");
			throw new IllegalArgumentException("Not a valid frequency");
		}

		try {
			List<Form> forms = formDao.findAllByDayOfFrequencyAndFrequency(dayOfFrequency, frequency, businessUnitId);
			return (CollectionUtils.isEmpty(forms)) ? Collections.emptyList() : forms;
		} catch (Exception e) {
			String msg = "Error while fetching Template information for given day of frequency " + frequency
					+ " and frequency " + frequency;
			log.error(msg, e);
			throw new VedantaException(msg);
		}
	}
}
