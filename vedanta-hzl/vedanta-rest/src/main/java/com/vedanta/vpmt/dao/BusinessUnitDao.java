package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.BusinessUnit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessUnitDao extends JpaRepository<BusinessUnit, Long> {

	@Query("SELECT bu FROM BusinessUnit bu WHERE bu.id=?1")
	public List<BusinessUnit> getAllBusinessUnit(Long businessId);

}
