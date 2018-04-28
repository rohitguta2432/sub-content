package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.Category;

public interface CategoryService extends VedantaService<Category> {

	public List<Category> getAllCategories();

	List<Category> getAllCategoriesByCurrentBusinessUnitId();

	List<Category> getAllCategoriesByPlantId(Long plantId);

	List<Category> getCategoriesByBusinessUnitId(long businessUnitId);
}
