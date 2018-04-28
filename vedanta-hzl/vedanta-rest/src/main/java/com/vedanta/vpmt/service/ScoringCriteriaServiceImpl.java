package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.ScoringCriteriaDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.ScoringCriteria;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 26/09/17.
 */
@Service("scoringCriteriaService")
@Slf4j
public class ScoringCriteriaServiceImpl implements ScoringCriteriaService {
	@Autowired
	private ScoringCriteriaDao scoringCriteriaDao;

	@Override
	public ScoringCriteria get(long id) {
		if (id <= 0) {
			log.info("Invalid scoring criteria id.");
			throw new VedantaException("Invalid scoring criteria id.");
		}
		try {
			return scoringCriteriaDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching unit by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public ScoringCriteria save(ScoringCriteria entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("scoring criteria object cannot be null/empty");
			throw new VedantaException("scoring criteria object cannot be null/empty");
		}
		try {
			ScoringCriteria savedScoringCriteria = scoringCriteriaDao.save(entity);
			return savedScoringCriteria;
		} catch (VedantaException e) {
			String msg = "Error while saving data unit information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean saveScoringCriteriaList(List<ScoringCriteria> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("scoring criteria object list cannot be null/empty");
			throw new VedantaException("scoring criteria object list cannot be null/empty");
		}
		try {
			scoringCriteriaDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving scoring criteria information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public ScoringCriteria update(long id, ScoringCriteria entity) {
		return null;
	}

	@Override
	public List<ScoringCriteria> getAllScoringCriteriaByFieldId(long fieldId) {
		try {
			List<ScoringCriteria> scoringCriteria = scoringCriteriaDao.findAllByFieldId(fieldId);
			return scoringCriteria;
		} catch (VedantaException e) {
			String msg = "Error while getting all criteria by field";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<ScoringCriteria> getAllScoringCriteriaByFieldIdAndSubCategoryId(long fieldId, long subCategoryId) {
		try {
			List<ScoringCriteria> scoringCriteria = scoringCriteriaDao.getAllByFieldIdAndSubCategoryId(fieldId,
					subCategoryId);
			return scoringCriteria;
		} catch (VedantaException e) {
			String msg = "Error while getting all criteria by field abd sub category";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<ScoringCriteria> getAllScoringCriteria() {
		try {
			List<ScoringCriteria> scoringCriteria = scoringCriteriaDao.findAll();
			return scoringCriteria;
		} catch (VedantaException e) {
			String msg = "Error while getting all scoring criteria";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Boolean delete(List<ScoringCriteria> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("scoring criteria object list cannot be null/empty");
			throw new VedantaException("scoring criteria object list cannot be null/empty");
		}
		try {
			scoringCriteriaDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting scoring criteria information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

}
