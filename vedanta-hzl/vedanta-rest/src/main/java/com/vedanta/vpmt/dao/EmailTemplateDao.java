package com.vedanta.vpmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.User;

@Repository
public interface EmailTemplateDao extends JpaRepository<EmailTemplate, Long> {

	@Query("select e from EmailTemplate e where e.status!=4")
	public List<EmailTemplate> getAllEmailtemplateByStatus();

	@Query("SELECT e FROM User e WHERE e.employeeId=?1")
	public User getUserByEmployeeId(String employeeId);

	@Query("select e from EmailTemplate e where e.businessUnitId=?1")
	public List<EmailTemplate> getAllEmailtemplateByBusinessUnitId(Long businessUnitId);
}
