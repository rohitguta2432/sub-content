package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormTargetValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
@Repository
public interface FormTargetValueDao extends JpaRepository<FormTargetValue, Long> {

	public List<FormTargetValue> findAllByFormFieldId(long formFieldId);

	@Query("SELECT tv FROM FormTargetValue tv WHERE tv.formFieldId=?1 AND tv.subCategoryId=?2")
	public List<FormTargetValue> getAllByFormFieldIdAndSubCategoryId(long formFieldId, Long subCategoryId);

	@Query("SELECT tv FROM FormTargetValue tv WHERE tv.formFieldId=?1 AND tv.subCategoryId=?2")
	public FormTargetValue getOneByFormFieldIdAndSubCategoryId(long formFieldId, long subCategoryId);

	@Query("SELECT tv FROM FormTargetValue tv GROUP BY tv.formFieldId, tv.subCategoryId, tv.plantId, tv.departmentId")
	public List<FormTargetValue> getAllDistinctFormTargetValues();

}