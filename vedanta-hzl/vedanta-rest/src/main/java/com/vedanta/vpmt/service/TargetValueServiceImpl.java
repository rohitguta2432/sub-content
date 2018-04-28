package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.TargetValueDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.TargetValue;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 26/09/17.
 */
@Service("targetValueService")
@Slf4j
public class TargetValueServiceImpl implements TargetValueService {
	@Autowired
	private TargetValueDao targetValueDao;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SubCategoryService subcategoryService;

	@Autowired
	private PlantService plantService;

	@Autowired
	private DepartmentService departmentService;

	@Override
	public TargetValue get(long id) {
		if (id <= 0) {
			log.info("Invalid target value id.");
			throw new VedantaException("Invalid target value id.");
		}
		try {
			return targetValueDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching unit by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public TargetValue save(TargetValue entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Target value object cannot be null/empty");
			throw new VedantaException("Target value object cannot be null/empty");
		}
		try {
			TargetValue savedTargetValue = targetValueDao.save(entity);
			return savedTargetValue;
		} catch (VedantaException e) {
			String msg = "Error while saving target value information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public TargetValue update(long id, TargetValue entity) {
		return null;
	}

	@Override
	public Boolean saveTargetValueList(List<TargetValue> entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Target value object list cannot be null/empty");
			throw new VedantaException("Target value object list cannot be null/empty");
		}
		try {
			targetValueDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving target value information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean delete(TargetValue entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Target value object cannot be null/empty");
			throw new VedantaException("Target value object cannot be null/empty");
		}
		try {
			targetValueDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting target value information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean delete(List<TargetValue> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("Target value object list cannot be null/empty");
			throw new VedantaException("Target value object list cannot be null/empty");
		}
		try {
			targetValueDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting target values information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public List<TargetValue> getAllTargetValues() {
		try {
			List<TargetValue> targetValues = targetValueDao.findAll();
			return targetValues;
		} catch (VedantaException e) {
			String msg = "Error while getting all target values";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public TargetValue getTargetValuesForSubCategory(long fieldId, long subCategoryId) {
		try {
			TargetValue targetValues = targetValueDao.getOneByFieldIdAndSubCategoryId(fieldId, subCategoryId);
			return targetValues;
		} catch (VedantaException e) {
			String msg = "Error while getting target value by category";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<TargetValue> getAllTargetValuesByFieldId(long fieldId) {
		try {
			List<TargetValue> targetValues = targetValueDao.findAllByFieldId(fieldId);
			return targetValues;
		} catch (VedantaException e) {
			String msg = "Error while getting all target values";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<TargetValue> getAllDistinctTargetValues() {
		try {
			List<TargetValue> targetValues = targetValueDao.getAllDistinctTargetValues();
			targetValues.forEach(targetValue -> {
				if (targetValue.getCategoryId() > 0) {
					targetValue.setCategoryName(categoryService.get(targetValue.getCategoryId()).getCategoryName());
				}
				if (targetValue.getSubCategoryId() > 0) {
					targetValue.setSubCategoryName(
							subcategoryService.get(targetValue.getSubCategoryId()).getCategoryName());
				}
				if (targetValue.getPlantId() > 0) {
					targetValue.setPlantName(plantService.get(targetValue.getPlantId()).getName());
				}
				if (targetValue.getDepartmentId() > 0) {
					targetValue.setDepartmentName(departmentService.get(targetValue.getDepartmentId()).getName());
				}
			});

			return targetValues;
		} catch (VedantaException e) {
			String msg = "Error while getting all target values";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
