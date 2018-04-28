package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.HumanResourceDao;
import com.vedanta.vpmt.dao.PlantDao;
import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.HumanResource;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.User;
import lombok.extern.slf4j.Slf4j;

@Service("HumanResourceService")
@Slf4j
public class HumanResourceServiceImpl implements HumanResourceService {

	@Autowired
	private HumanResourceDao humanResourceDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private PlantDao plantDao;

	@Override
	public HumanResource get(long humanResourceId) {
		if (humanResourceId <= 0) {
			log.info("Invalid human resource Id.");
			throw new VedantaException("Invalid human resource Id.");
		}
		try {
			return humanResourceDao.findOne(humanResourceId);
		} catch (VedantaException e) {
			String msg = "Error while fetching human resource by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public HumanResource save(HumanResource entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Human resource object cannot be null/empty");
			throw new VedantaException("Human resource object cannot be null/empty");
		}

		if (!ObjectUtils.isEmpty(entity.getEmployeeId())) {
			User user = userDao.findOneByEmployeeId(entity.getEmployeeId());
			if (!ObjectUtils.isEmpty(user)) {
				entity.setName(user.getName());
			}
		}

		if ((!ObjectUtils.isEmpty(entity.getBusinessUnitId()) & (!ObjectUtils.isEmpty(entity.getPlantCode())))) {
			Plant plant = plantDao.findOneByBusinessUnitIdAndPlantCode(entity.getBusinessUnitId(),
					entity.getPlantCode());
			if (!ObjectUtils.isEmpty(plant)) {
				entity.setPlantName(plant.getName());

			}
		}

		try {
			return humanResourceDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while  save Human resource";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public HumanResource update(long id, HumanResource entity) {
		return null;
	}

	@Override
	public HumanResource checkExistingHumanResource(HumanResource humanResource) {

		if (ObjectUtils.isEmpty(humanResource)) {
			log.info("Human resource object cannot be null/empty");
			throw new VedantaException("Human resource object cannot be null/empty");
		}
		HumanResource humanResourceDetails = null;
		try {
			humanResourceDetails = humanResourceDao.checkExistingHumanResource(humanResource.getEmployeeId(),
					humanResource.getPlantCode(), humanResource.getBusinessUnitId());
		} catch (VedantaException e) {
			String msg = "Error while getting all human resource";
			throw new VedantaException(msg);
		}
		return humanResourceDetails;
	}

	@Override
	public List<HumanResource> getAllHumanResource() {
		try {
			return humanResourceDao.findAllHumanResource();
		} catch (VedantaException e) {
			throw new VedantaException("Error while getting human resource");
		}
	}

}
