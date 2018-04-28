package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FormGroupDao extends JpaRepository<FormGroup, Long> {

	List<FormGroup> getFormGroupByFormId(Long formId);

	List<FormGroup> getFormGroupByStatus(int status);

	@Query("SELECT fg FROM FormGroup fg WHERE fg.formGroupDetailId IN (?1)")
	public List<FormGroup> getAllFormGroupsInFormGroupDetailId(Set<Long> formGroupDetailId);
}