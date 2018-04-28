package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.BusinessUnitDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;

@Service("businessUnitService")
@Slf4j
public class BusinessUnitServiceImpl implements BusinessUnitService {

	@Autowired
	private BusinessUnitDao businessUnitDao;

	@Override
	public BusinessUnit get(long id) {
		if (id <= 0) {
			log.info("Invalid BusinessUnit id.");
			throw new VedantaException("Invalid user id.");
		}
		return businessUnitDao.findOne(id);
	}

	@Override
	public BusinessUnit save(BusinessUnit entity) {

		if (ObjectUtils.isEmpty(entity)) {
			log.info("BusinessUnit object cannot be null/empty");
			throw new VedantaException("BusinessUnit object cannot be null/empty");
		}

		return businessUnitDao.save(entity);
	}

	@Override
	public BusinessUnit update(long id, BusinessUnit entity) {

		if (id <= 0) {
			log.info("Invalid BusinessUnit id.");
			throw new VedantaException("Invalid user id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("BusinessUnit object cannot be null/empty.");
			throw new VedantaException("BusinessUnit object cannot be null/empty.");
		}

		BusinessUnit existingEntity = businessUnitDao.findOne(id);

		if (!ObjectUtils.isEmpty(existingEntity)) {
			log.info("BusinessUnit object already exist.");
			throw new VedantaException("BusinessUnit object already exist.");
		}

		return null;
	}

	public List<BusinessUnit> getAllBusinessUnits() {
		try {
			List<BusinessUnit> businessUnits = (List<BusinessUnit>) businessUnitDao.findAll();
			return businessUnits;
		} catch (VedantaException e) {
			String msg = "Error while getting all businessUnits by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<BusinessUnit> getAllBusinessUnitsByRole() {
		try {
			List<BusinessUnit> businessUnits = null;

			if (VedantaUtility.getCurrentUserBuId() == 0
					&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
				businessUnits = (List<BusinessUnit>) businessUnitDao.findAll();
			} else {
				businessUnits = businessUnitDao.getAllBusinessUnit(VedantaUtility.getCurrentUserBuId());
			}

			return businessUnits;
		} catch (VedantaException e) {
			String msg = "Error while getting all businessUnits by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
