package com.vedanta.vpmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.HumanResource;

@Repository
public interface HumanResourceDao extends JpaRepository<HumanResource, Long> {

	@Query("SELECT hr FROM HumanResource hr WHERE hr.employeeId=?1 and hr.plantCode=?2 and hr.businessUnitId=?3 and hr.status=1")
	public HumanResource checkExistingHumanResource(String employeeId, String plantCode, Long businessUnitId);

	@Query("SELECT hr FROM HumanResource hr WHERE hr.status=1")
	public List<HumanResource> findAllHumanResource();

	@Query("SELECT hr FROM HumanResource hr WHERE hr.businessUnitId=?1 and hr.status=?2")
	public List<HumanResource> findAllHumanResourceByBusinessUnitId(Long businessUnitId, int status);

	public List<HumanResource> findAllByEmployeeIdAndStatus(String employeeId, int status);

}
