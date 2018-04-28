package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormValidation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
@Repository
public interface FormValidationDao extends JpaRepository<FormValidation, Long> {
	public List<FormValidation> findAllByFormFieldId(long fieldId);

	@Query("SELECT v FROM FormValidation v WHERE v.formFieldId=?1 AND v.subCategoryId=?2")
	public FormValidation getOneByFormFieldIdAndSubCategoryId(long formFieldId, long subCategoryId);
}
