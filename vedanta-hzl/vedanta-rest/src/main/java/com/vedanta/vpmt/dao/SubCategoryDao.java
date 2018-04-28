package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.SubCategory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryDao extends JpaRepository<SubCategory, Long> {

	@Query("SELECT sc FROM SubCategory sc WHERE sc.sapCategory=?1 AND sc.businessUnitId=?2")
	public SubCategory findBySapCategory(String sapCategory, Long businessUnitId);

	@Query("SELECT sc FROM SubCategory sc WHERE  sc.businessUnitId=?1")
	List<SubCategory> findAllByBusinessUnitId(Long businessUnitId);

	@Query("SELECT sc FROM SubCategory sc WHERE  sc.categoryId=?1")
	List<SubCategory> findAllSubCategoriesByCategoryId(Long categoryId);

	@Query("SELECT sc FROM SubCategory sc WHERE sc.categoryName=?1 AND sc.businessUnitId=?2")
	public SubCategory sesaFindBySapCategory(String categoryName, Long businessUnitId);
}
