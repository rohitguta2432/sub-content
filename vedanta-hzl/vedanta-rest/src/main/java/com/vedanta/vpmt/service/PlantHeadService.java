package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;

public interface PlantHeadService extends VedantaService<PlantHead> {

	public List<PlantHead> getAllPlantHead();

	public PlantHead checkExistingPlantHead(PlantHead plantHead);

	public List<PlantHead> getByEmployeeId(String employeeId);

	public List<PlantHead> geByPlantCode(String plantCode);

	public List<Plant> getplantByPlantCodeAndBuId(String plantCode, Long businessUnitId);

}
