package com.vedanta.vpmt.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.PlantDao;
import com.vedanta.vpmt.dao.PlantHeadDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.VedantaUser;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 03/10/17.
 */
@Service("plantService")
@Slf4j
public class PlantServiceImpl implements PlantService {
	@Autowired
	private PlantDao plantDao;

	@Autowired
	private PlantHeadDao plantHeadDao;

	@Override
	public List<Plant> getAllPlantsByCurrentBusinessUnitId() {
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			return plantDao.findAllByBusinessUnitId(vedantaUser.getBusinessUnitId());
		} catch (VedantaException e) {
			String msg = "Error while fetching all plants";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public List<Plant> getAllPlantsByCurrentUser() {
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			List<PlantHead> plantHeads = plantHeadDao.findAllByBusinessUnitIdAndEmployeeIdAndStatus(
					vedantaUser.getBusinessUnitId(), vedantaUser.getEmployeeId(), Constants.STATUS_ACTIVE);
			Set<String> plantCodes = new HashSet<>();
			for (PlantHead plantHead : plantHeads) {
				plantCodes.add(plantHead.getPlantCode());
			}
			if (!ObjectUtils.isEmpty(plantCodes) && plantCodes.size() > 0) {
				return plantDao.getAllPlantsByPlantCodesAndStatus(vedantaUser.getBusinessUnitId(), plantCodes,
						Constants.STATUS_ACTIVE);
			}
			return new ArrayList<>();
		} catch (VedantaException e) {
			String msg = "Error while fetching all plants";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public List<Plant> getAllPlants() {
		try {
			return plantDao.findAll();
		} catch (VedantaException e) {
			String msg = "Error while fetching all plants";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Plant get(long id) {
		if (id <= 0) {
			log.info("Invalid plant id.");
			throw new VedantaException("Invalid plant id.");
		}
		try {
			return plantDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching plant information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Plant save(Plant entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("plant object cannot be null/empty");
			throw new VedantaException("plant object cannot be null/empty");
		}

		try {
			return plantDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving plant information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Plant update(long id, Plant entity) {
		return null;
	}

	@Override
	public List<Plant> getAllPlantByBusinessUnitId(long businessUnitId) {
		try {
			return plantDao.findAllPlantByBusinessUnitId(businessUnitId);
		} catch (VedantaException e) {
			String msg = "Error while getting all plant details";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Plant> getAllPlantByPlantName(String plantName) {
		try {
			return plantDao.findAllPlantByPlantName(plantName);
		} catch (VedantaException e) {
			String msg = "Error while getting all plant details";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Plant getPlantByBusinessUnitIdAndPlantCode(Long businessUnitId, String plantCode) {
		if (businessUnitId <= 0 || StringUtils.isEmpty(plantCode)) {
			log.info("Invalid plant business unit id or plantCode.");
			throw new VedantaException("Invalid plant business unit id or plantCode.");
		}
		try {
			return plantDao.findOneByBusinessUnitIdAndPlantCode(businessUnitId, plantCode);
		} catch (VedantaException e) {
			String msg = "Error while fetching plant information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public List<Plant> getAllPlantByBusinessUnitIdAndPlantCode(Long businessUnitId, String plantCode) {
		if (businessUnitId <= 0 || StringUtils.isEmpty(plantCode)) {
			log.info("Invalid plant business unit id or plantCode.");
			throw new VedantaException("Invalid plant business unit id or plantCode.");
		}
		try {
			return plantDao.getAllByBusinessUnitIdAndPlantCode(businessUnitId, plantCode);
		} catch (VedantaException e) {
			String msg = "Error while fetching plant information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

}
