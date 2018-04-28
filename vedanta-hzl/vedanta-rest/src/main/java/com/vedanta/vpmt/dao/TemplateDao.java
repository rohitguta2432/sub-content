package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 11/09/17.
 */
@Repository
public interface TemplateDao extends JpaRepository<Template, Long> {

	@Query("SELECT t FROM Template t WHERE t.status=?1")
	public List<Template> getAllTemplates(int status);

	@Query("SELECT t FROM Template t WHERE t.subCategoryId=?1 AND t.status=?2")
	public Template findOneBySubCategoryId(long subCategoryId, int status);

	@Query("SELECT t FROM Template t WHERE t.dayOfFrequency=?1 AND t.frequency=?2 AND t.businessUnitId=?3")
	public List<Template> findAllByDayOfFrequencyAndFrequency(int dayOfFrequency, String frequency,
			long businessUnitId);

	@Query("SELECT t FROM Template t WHERE t.status=?1 AND t.businessUnitId=?2")
	public List<Template> getAllTemplatesByBusinessUnitId(int status, Long buId);

}
