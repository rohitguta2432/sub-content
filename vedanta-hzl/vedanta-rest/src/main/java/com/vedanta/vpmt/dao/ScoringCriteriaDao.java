package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.ScoringCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
@Repository
public interface ScoringCriteriaDao extends JpaRepository<ScoringCriteria, Long> {
	public List<ScoringCriteria> findAllByFieldId(long fieldId);

	@Query("SELECT sc FROM ScoringCriteria sc WHERE sc.fieldId=?1 AND sc.subCategoryId=?2")
	public List<ScoringCriteria> getAllByFieldIdAndSubCategoryId(long fieldId, long subCategoryId);

}
