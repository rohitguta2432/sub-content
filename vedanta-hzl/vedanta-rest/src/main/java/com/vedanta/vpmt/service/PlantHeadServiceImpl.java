package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.PlantDao;
import com.vedanta.vpmt.dao.PlantHeadDao;
import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;

@Service("PlantHeadService")
@Slf4j
public class PlantHeadServiceImpl implements PlantHeadService {

	@Autowired
	private PlantHeadDao plantHeadDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PlantService plantService;

	@Autowired
	private PlantDao plantDao;

	@Override
	public PlantHead get(long id) {
		if (id <= 0) {
			log.info("Invalid Plant Head Id.");
			throw new VedantaException("Invalid Plant Head Id.");
		}
		try {
			return plantHeadDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching Plant head by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public PlantHead save(PlantHead entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("PlantHead object cannot be null/empty");
			throw new VedantaException("PlantHead object cannot be null/empty");
		}

		if (!ObjectUtils.isEmpty(entity.getEmployeeId())) {
			User user = userDao.findOneByEmployeeId(entity.getEmployeeId());
			if (!ObjectUtils.isEmpty(user)) {
				entity.setName(user.getName());
			}
		}

		if (!ObjectUtils.isEmpty(entity.getBusinessUnitId())) {
			Plant plant = plantService.getPlantByBusinessUnitIdAndPlantCode(entity.getBusinessUnitId(),
					entity.getPlantCode());
			if (!ObjectUtils.isEmpty(plant)) {
				entity.setPlantName(plant.getName());

			}
		}

		try {
			return plantHeadDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while  save plant head";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public PlantHead update(long id, PlantHead entity) {
		return null;
	}

	@Override
	public List<PlantHead> getAllPlantHead() {
		try {
			List<PlantHead> plantHead = null;
			if (VedantaUtility.getCurrentUserBuId() == 0
					&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
				plantHead = plantHeadDao.findAllPlantHead();
			} else {
				plantHead = plantHeadDao.findAllPlantHeadByBusinessUnitId(VedantaUtility.getCurrentUserBuId(),
						Constants.STATUS_ACTIVE);

			}
			return plantHead;
		} catch (VedantaException e) {
			String msg = "Error while getting all Plant head";
			throw new VedantaException(msg);
		}
	}

	@Override
	public PlantHead checkExistingPlantHead(PlantHead plantHead) {

		if (ObjectUtils.isEmpty(plantHead)) {
			log.info("PlantHead object cannot be null/empty");
			throw new VedantaException("PlantHead object cannot be null/empty");
		}
		PlantHead plantHeadDetails = null;
		try {
			plantHeadDetails = plantHeadDao.checkExistingPlantHead(plantHead.getEmployeeId(), plantHead.getPlantCode(),
					plantHead.getBusinessUnitId());
		} catch (VedantaException e) {
			String msg = "Error while getting all Plant head";
			throw new VedantaException(msg);
		}
		return plantHeadDetails;
	}

	@Override
	public List<PlantHead> getByEmployeeId(String employeeId) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();

			List<PlantHead> plantHead = null;
			plantHead = plantHeadDao.findAllByBusinessUnitIdAndEmployeeIdAndStatus(vedantaUser.getBusinessUnitId(),
					employeeId, Constants.STATUS_ACTIVE);
			return plantHead;
		} catch (VedantaException e) {
			String msg = "Error while getting all Plant head";
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<PlantHead> geByPlantCode(String plantCode) {
		return plantHeadDao.getByPlantCode(plantCode);
	}

	@Override
	public List<Plant> getplantByPlantCodeAndBuId(String plantCode, Long businessUnitId) {
		return plantDao.getPlantByPlantCodeAndBusinessUnitId(plantCode, businessUnitId);
	}

}
