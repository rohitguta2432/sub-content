package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.DataUnitDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.DataUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 26/09/17.
 */
@Service("dataUnitService")
@Slf4j
public class DataUnitServiceImpl implements DataUnitService {
	@Autowired
	private DataUnitDao dataUnitDao;

	@Override
	public DataUnit get(long id) {
		if (id <= 0) {
			log.info("Invalid data unit id.");
			throw new VedantaException("Invalid data unit id.");
		}
		try {
			return dataUnitDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching unit by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public DataUnit save(DataUnit entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Data unit object cannot be null/empty");
			throw new VedantaException("data unit object cannot be null/empty");
		}
		try {
			DataUnit savedDataUnit = dataUnitDao.save(entity);
			return savedDataUnit;
		} catch (VedantaException e) {
			String msg = "Error while saving data unit information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean saveDataUnitList(List<DataUnit> entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Data unit object list cannot be null/empty");
			throw new VedantaException("data unit object list cannot be null/empty");
		}
		try {
			dataUnitDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving data unit list information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public DataUnit update(long id, DataUnit entity) {
		return null;
	}

	@Override
	public List<DataUnit> getAllDataUnits() {
		try {
			List<DataUnit> dataUnits = dataUnitDao.getAllDataUnits(Constants.STATUS_DELETE);
			return dataUnits;
		} catch (VedantaException e) {
			String msg = "Error while getting all data units";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
