package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormFieldOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormFieldOptionDao extends JpaRepository<FormFieldOption, Long> {

	public List<FormFieldOption> findAllByFormFieldId(Long id);

}
