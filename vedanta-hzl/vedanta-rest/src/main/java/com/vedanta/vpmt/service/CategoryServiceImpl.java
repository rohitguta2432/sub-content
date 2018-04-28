package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.CategoryDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.model.VedantaUser;

import lombok.extern.slf4j.Slf4j;

@Service("categoryService")
@Slf4j
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public Category get(long id) {
		if (id <= 0) {
			log.info("Invalid Category id.");
			throw new VedantaException("Invalid user id.");
		}
		return categoryDao.findOne(id);
	}

	@Override
	public Category save(Category entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Category object cannot be null/empty");
			throw new VedantaException("Category object cannot be null/empty");
		}

		entity.setStatus(Constants.STATUS_ACTIVE);
		return categoryDao.save(entity);
	}

	@Override
	public Category update(long id, Category entity) {
		if (id <= 0) {
			log.info("Invalid Category id.");
			throw new VedantaException("Invalid user id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("Category object cannot be null/empty.");
			throw new VedantaException("Category object cannot be null/empty.");
		}

		Category existingEntity = categoryDao.findOne(id);

		if (!ObjectUtils.isEmpty(existingEntity)) {
			log.info("Category object already exist.");
			throw new VedantaException("Category object already exist.");
		}

		return null;
	}

	@Override
	public List<Category> getAllCategories() {
		try {
			return categoryDao.findAll();
		} catch (VedantaException e) {
			String msg = "Error while getting all category by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Category> getAllCategoriesByPlantId(Long plantId) {
		try {
			return categoryDao.findAllByPlantId(plantId);
		} catch (VedantaException e) {
			String msg = "Error while getting all category by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Category> getAllCategoriesByCurrentBusinessUnitId() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			return categoryDao.findAllByBusinessUnitId(vedantaUser.getBusinessUnitId());
		} catch (VedantaException e) {
			String msg = "Error while getting all categories by rest";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Category> getCategoriesByBusinessUnitId(long businessUnitId) {
		if (businessUnitId <= 0) {
			log.info("Invalid business unit id.");
			throw new VedantaException("Invalid business unit id.");
		}
		return categoryDao.findAllByBusinessUnitId(businessUnitId);
	}
}
