package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.Plant;

import java.util.List;

/**
 * Created by manishsanger on 03/10/17.
 */
public interface PlantService extends VedantaService<Plant> {
	public List<Plant> getAllPlants();

	List<Plant> getAllPlantsByCurrentBusinessUnitId();

	List<Plant> getAllPlantsByCurrentUser();

	List<Plant> getAllPlantByBusinessUnitId(long businessUnitId);

	Plant getPlantByBusinessUnitIdAndPlantCode(Long businessUnitId, String plantCode);

	List<Plant> getAllPlantByBusinessUnitIdAndPlantCode(Long businessUnitId, String plantCode);

	List<Plant> getAllPlantByPlantName(String plantName);

}
