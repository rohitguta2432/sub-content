package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.PlantHead;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantHeadDao extends JpaRepository<PlantHead, Long> {

	public List<PlantHead> findAllByEmployeeIdAndStatus(String employeeId, int status);

	public List<PlantHead> findAllByBusinessUnitIdAndEmployeeIdAndStatus(Long businessUnitId, String employeeId,
			int status);

	public List<PlantHead> findAllByPlantCodeAndStatus(String plantCode, int status);

	@Query("SELECT p FROM PlantHead p WHERE p.status=1")
	public List<PlantHead> findAllPlantHead();

	@Query("SELECT p FROM PlantHead p WHERE p.employeeId=?1 and p.plantCode=?2 and p.businessUnitId=?3 and p.status=1")
	public PlantHead checkExistingPlantHead(String employeeId, String plantCode, Long businessUnitId);

	@Query("SELECT p FROM PlantHead p WHERE p.businessUnitId=?1 and p.status=?2")
	public List<PlantHead> findAllPlantHeadByBusinessUnitId(Long businessUnitId, int status);

	@Query("SELECT p FROM PlantHead p WHERE p.plantCode=?1")
	public List<PlantHead> getByPlantCode(String plantCode);
}
