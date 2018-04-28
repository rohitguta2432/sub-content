package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.FormGroupDetailDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormGroupDetail;

import lombok.extern.slf4j.Slf4j;

@Service("formGroupDetailService")
@Slf4j
public class FormGroupDetailServiceImpl implements FormGroupDetailService {

	@Autowired
	private FormGroupDetailDao formGroupDetailDao;

	@Override
	public FormGroupDetail get(long id) {
		if (id <= 0) {
			log.info("Invalid FormGroupDetail id.");
			throw new VedantaException("Invalid FormGroupDetail id.");
		}
		return formGroupDetailDao.findOne(id);
	}

	@Override
	public FormGroupDetail save(FormGroupDetail entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("FormGroupDetail object cannot be null/empty");
			throw new VedantaException("FormGroupDetail object cannot be null/empty");
		}

		if (entity.getId() != null) {
			boolean updateEntity = formGroupDetailDao.exists(entity.getId());
			if (updateEntity)
				return update(entity.getId(), entity);
		}
		entity.setStatus(1);
		return formGroupDetailDao.save(entity);
	}

	@Override
	public FormGroupDetail update(long id, FormGroupDetail entity) {
		if (id <= 0) {
			log.info("Invalid FormGroupDetail id.");
			throw new VedantaException("Invalid FormGroupDetail id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("FormGroupDetail object cannot be null/empty.");
			throw new VedantaException("FormGroupDetail object cannot be null/empty.");
		}

		FormGroupDetail existingEntity = formGroupDetailDao.findOne(id);

		if (ObjectUtils.isEmpty(existingEntity)) {
			log.info("FormGroupDetail object does not exist.");
			throw new VedantaException("FormGroupDetail object does not exist.");
		}

		updateFormGroupDetail(existingEntity, entity);
		return formGroupDetailDao.save(existingEntity);
	}

	private void updateFormGroupDetail(FormGroupDetail existingEntity, FormGroupDetail entity) {

		if (entity.getDescription() != null)
			existingEntity.setDescription(entity.getDescription());
		if (entity.getName() != null)
			existingEntity.setName(entity.getName());
		if (entity.getLabelName() != null)
			existingEntity.setLabelName(entity.getLabelName());
		if (entity.getStatus() > 0)
			existingEntity.setStatus(entity.getStatus());
	}

	@Override
	public List<FormGroupDetail> getAllFormGroupDetails() {
		try {
			List<FormGroupDetail> formGroupDetail = formGroupDetailDao
					.getFormGroupDetailsByStatus(Constants.STATUS_ACTIVE);
			return formGroupDetail;
		} catch (VedantaException e) {
			String msg = "Error while getting all formGroupDetail by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
