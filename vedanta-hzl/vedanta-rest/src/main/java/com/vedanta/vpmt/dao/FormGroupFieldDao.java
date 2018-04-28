package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormGroupField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormGroupFieldDao extends JpaRepository<FormGroupField, Long> {

	public List<FormGroupField> getFormGroupFieldByformGroupDetailId(Long formGroupDetailId);
}
