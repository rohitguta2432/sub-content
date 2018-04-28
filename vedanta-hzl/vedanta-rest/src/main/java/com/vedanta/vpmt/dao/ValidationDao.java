package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
@Repository
public interface ValidationDao extends JpaRepository<Validation, Long> {
	public List<Validation> findAllByFieldId(long fieldId);

	@Query("SELECT v FROM Validation v WHERE v.fieldId=?1 AND v.subCategoryId=?2")
	public Validation getOneByFieldIdAndSubCategoryId(long fieldId, long subCategoryId);
}
