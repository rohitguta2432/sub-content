package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.SubCategoryDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.model.VedantaUser;

import lombok.extern.slf4j.Slf4j;

@Service("subcategoryService")
@Slf4j
public class SubCategoryServiceImpl implements SubCategoryService {

	@Autowired
	private SubCategoryDao subcategoryDao;

	@Override
	public SubCategory get(long id) {
		if (id <= 0) {
			log.info("Invalid SubCategory id.");
			throw new VedantaException("Invalid user id.");
		}
		return subcategoryDao.findOne(id);
	}

	@Override
	public SubCategory save(SubCategory entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("SubCategory object cannot be null/empty");
			throw new VedantaException("SubCategory object cannot be null/empty");
		}
		entity.setStatus(Constants.STATUS_ACTIVE);
		return subcategoryDao.save(entity);
	}

	@Override
	public SubCategory update(long id, SubCategory entity) {
		if (id <= 0) {
			log.info("Invalid SubCategory id.");
			throw new VedantaException("Invalid user id.");
		}
		if (ObjectUtils.isEmpty(entity)) {
			log.info("SubCategory object cannot be null/empty.");
			throw new VedantaException("SubCategory object cannot be null/empty.");
		}

		SubCategory existingEntity = subcategoryDao.findOne(id);
		if (!ObjectUtils.isEmpty(existingEntity)) {
			log.info("SubCategory object already exist.");
			throw new VedantaException("SubCategory object already exist.");
		}
		return null;
	}

	@Override
	public List<SubCategory> getAllCategories() {
		try {
			return subcategoryDao.findAll();
		} catch (VedantaException e) {
			String msg = "Error while getting all subcategory by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<SubCategory> getAllSubCategoriesByCurrentBusinessUnitId() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			return subcategoryDao.findAllByBusinessUnitId(vedantaUser.getBusinessUnitId());
		} catch (VedantaException e) {
			String msg = "Error while getting all subcategories by rest";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public SubCategory findBySapCategory(String sapCategory, Long businessUnitId) {

		if (StringUtils.isEmpty(sapCategory)) {
			return null;
		}
		try {
			return subcategoryDao.findBySapCategory(sapCategory, businessUnitId);
		} catch (VedantaException e) {
			String msg = "Error while getting subcategory for the given sapCategory " + sapCategory;
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<SubCategory> findAllByBusinessUnitId(Long businessUnitId) {
		return subcategoryDao.findAllByBusinessUnitId(businessUnitId);
	}

	@Override
	public List<SubCategory> findAllSubCategoriesByCategoryId(Long categoryId) {
		return subcategoryDao.findAllSubCategoriesByCategoryId(categoryId);
	}

	@Override
	public SubCategory sesaFindBySapCategoryName(String categoryName, Long businessUnitId) {
		if (StringUtils.isEmpty(categoryName)) {
			return null;
		}

		try {
			return subcategoryDao.sesaFindBySapCategory(categoryName, businessUnitId);
		} catch (VedantaException e) {
			String msg = "Error while getting subcategory for the given sapCategory-name " + categoryName;
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
}
