package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.FormTargetValueDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormTargetValue;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 26/09/17.
 */
@Service("formTargetValueService")
@Slf4j
public class FormTargetValueServiceImpl implements FormTargetValueService {

	@Autowired
	private FormTargetValueDao formTargetValueDao;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SubCategoryService subcategoryService;

	@Autowired
	private PlantService plantService;

	@Autowired
	private DepartmentService departmentService;

	@Override
	public FormTargetValue get(long id) {
		if (id <= 0) {
			log.info("Invalid form target value id.");
			throw new VedantaException("Invalid form target value id.");
		}
		try {
			return formTargetValueDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching for target value by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormTargetValue save(FormTargetValue entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form Target value object cannot be null/empty");
			throw new VedantaException("Form Target value object cannot be null/empty");
		}
		try {
			FormTargetValue savedTargetValue = formTargetValueDao.save(entity);
			return savedTargetValue;
		} catch (VedantaException e) {
			String msg = "Error while saving form target value information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public FormTargetValue update(long id, FormTargetValue entity) {
		return null;
	}

	@Override
	public Boolean saveFormTargetValueList(List<FormTargetValue> entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form Target value object list cannot be null/empty");
			throw new VedantaException("Form Target value object list cannot be null/empty");
		}
		try {
			formTargetValueDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving form target value information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean delete(FormTargetValue entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Form Target value object cannot be null/empty");
			throw new VedantaException("Form Target value object cannot be null/empty");
		}
		try {
			formTargetValueDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting for target value information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public Boolean delete(List<FormTargetValue> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("Form Target value object list cannot be null/empty");
			throw new VedantaException("Target value object list cannot be null/empty");
		}
		try {
			formTargetValueDao.delete(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while deleting target values information";
			log.info(msg);
			throw new VedantaException(e);
		}
	}

	@Override
	public List<FormTargetValue> getAllFormTargetValues() {
		try {
			List<FormTargetValue> targetValues = formTargetValueDao.findAll();
			return targetValues;
		} catch (VedantaException e) {
			String msg = "Error while getting all form target values";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public FormTargetValue getFormTargetValuesForSubCategory(long fieldId, long subCategoryId) {
		try {
			FormTargetValue targetValues = formTargetValueDao.getOneByFormFieldIdAndSubCategoryId(fieldId,
					subCategoryId);
			return targetValues;
		} catch (VedantaException e) {
			String msg = "Error while getting target value by category";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormTargetValue> getAllFormTargetValuesByFieldId(long fieldId) {
		try {
			List<FormTargetValue> targetValues = formTargetValueDao.findAllByFormFieldId(fieldId);
			return targetValues;
		} catch (VedantaException e) {
			String msg = "Error while getting all form target values";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<FormTargetValue> getAllDistinctFormTargetValues() {
		try {
			List<FormTargetValue> targetValues = formTargetValueDao.getAllDistinctFormTargetValues();
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
			String msg = "Error while getting all form target values";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
