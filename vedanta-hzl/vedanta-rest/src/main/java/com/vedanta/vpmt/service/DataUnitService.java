package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.DataUnit;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
public interface DataUnitService extends VedantaService<DataUnit> {
	public Boolean saveDataUnitList(List<DataUnit> entity);

	public List<DataUnit> getAllDataUnits();
}
