package com.vedanta.vpmt.service.sap;

import java.util.List;

import com.vedanta.vpmt.model.Plant;

import lombok.NonNull;

public interface SAPClientService {

	void createContracts(@NonNull List<Plant> plantCodes);
}
