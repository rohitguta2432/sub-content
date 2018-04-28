package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormDao extends JpaRepository<Form, Long> {

	@Query("SELECT f FROM Form f WHERE f.status=?1")
	public List<Form> getAllForms(int status);

	@Query("SELECT f FROM Form f WHERE f.status=?1 AND f.businessUnitId=?2")
	public List<Form> getAllForms(int status, Long businessId);

	@Query("SELECT f FROM Form f WHERE f.subCategoryId=?1")
	public List<Form> getAllBySubCategoryId(long subCategoryId);

	@Query("SELECT f FROM Form f WHERE f.subCategoryId=?1 AND f.isAdminForm=1")
	public List<Form> getAllBySubCategoryIdForAdmin(long subCategoryId);

	@Query("SELECT f FROM Form f WHERE f.dayOfFrequency=?1 AND f.frequency=?2 AND f.businessUnitId=?3")
	public List<Form> findAllByDayOfFrequencyAndFrequency(int dayOfFrequency, String frequency, Long bussinessUnitId);

}
