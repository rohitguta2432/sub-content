package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.BusinessUnit;

public interface BusinessUnitService extends VedantaService<BusinessUnit> {

	public List<BusinessUnit> getAllBusinessUnits();

	public List<BusinessUnit> getAllBusinessUnitsByRole();

}
