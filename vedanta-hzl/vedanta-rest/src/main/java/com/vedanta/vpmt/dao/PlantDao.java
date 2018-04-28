package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Plant;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by manishsanger on 03/10/17.
 */
@Repository
public interface PlantDao extends JpaRepository<Plant, Long> {

	public List<Plant> findAllByBusinessUnitId(Long businessUnitId);

	@Query("SELECT p FROM Plant p WHERE p.businessUnitId=?1 AND p.plantCode IN (?2) AND p.status=?3")
	public List<Plant> getAllPlantsByPlantCodesAndStatus(Long businessId, Set<String> plantCode, int status);

	@Query("SELECT p FROM Plant p WHERE p.businessUnitId=?1 AND p.status=1")
	public List<Plant> findAllPlantByBusinessUnitId(Long businessUnitId);

	public Plant findOneByBusinessUnitIdAndPlantCode(Long businessUnitId, String plantCode);

	public List<Plant> getAllByBusinessUnitIdAndPlantCode(Long businessUnitId, String plantCode);

	@Query("SELECT p FROM Plant p WHERE p.name LIKE (?1) AND p.status=1")
	public List<Plant> findAllPlantByPlantName(String plantName);

	@Query("SELECT p FROM Plant p WHERE p.plantCode=?1 AND p.businessUnitId=?2")
	public List<Plant> getPlantByPlantCodeAndBusinessUnitId(String plantCode, Long businessunitId);
}
