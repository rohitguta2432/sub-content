package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormFieldDao extends JpaRepository<FormField, Long> {

	public List<FormField> getFormFieldsByStatus(int status);
}
