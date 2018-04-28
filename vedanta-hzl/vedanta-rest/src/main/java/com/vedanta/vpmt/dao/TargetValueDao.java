package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.TargetValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
@Repository
public interface TargetValueDao extends JpaRepository<TargetValue, Long> {

	public List<TargetValue> findAllByFieldId(long fieldId);

	@Query("SELECT tv FROM TargetValue tv WHERE tv.fieldId=?1 AND tv.subCategoryId=?2")
	public List<TargetValue> getAllByFieldIdAndSubCategoryId(long fieldId, Long subCategoryId);

	@Query("SELECT tv FROM TargetValue tv WHERE tv.fieldId=?1 AND tv.subCategoryId=?2")
	public TargetValue getOneByFieldIdAndSubCategoryId(long fieldId, long subCategoryId);

	@Query("SELECT tv FROM TargetValue tv GROUP BY tv.fieldId, tv.subCategoryId, tv.plantId, tv.departmentId")
	public List<TargetValue> getAllDistinctTargetValues();

}