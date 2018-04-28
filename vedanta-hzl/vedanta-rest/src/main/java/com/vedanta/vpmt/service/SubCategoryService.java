package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.SubCategory;

public interface SubCategoryService extends VedantaService<SubCategory> {

	public List<SubCategory> getAllCategories();

	SubCategory findBySapCategory(String sapCategory, Long businessUnitId);

	List<SubCategory> getAllSubCategoriesByCurrentBusinessUnitId();

	List<SubCategory> findAllByBusinessUnitId(Long businessUnitId);

	List<SubCategory> findAllSubCategoriesByCategoryId(Long categoryId);

	SubCategory sesaFindBySapCategoryName(String categoryName, Long businessUnitId);
}
