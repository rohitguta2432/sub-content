package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.TemplateForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 11/09/17.
 */
@Repository
public interface TemplateFormDao extends JpaRepository<TemplateForm, Long> {

	public List<TemplateForm> findByTemplateId(Long templateId);

	public void deleteByTemplateId(Long templateId);
}
