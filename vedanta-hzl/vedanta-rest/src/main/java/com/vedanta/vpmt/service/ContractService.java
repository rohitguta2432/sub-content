package com.vedanta.vpmt.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vedanta.vpmt.model.Contract;

public interface ContractService extends VedantaService<Contract> {

	public List<Contract> getAllContracts();

	public List<Contract> getContractByVendorId(Long vendorId);

	public Contract findByContractNumber(String contractNumber);

	Optional<List<Contract>> getContract(Long categoryId, Long subCategoryId, Long businessUnitId);

	void save(List<Contract> contracts);

	Contract findAndModifyByNumber(Contract contract);

	List<Contract> getAllContractsByCurrentBusinessUnitId();

	List<Contract> getAllContractsByPlantId(Long plantId);

	List<Contract> getAllContractsByPlantIds(Set<Long> plantIds);

	public List<Contract> findAllByBusinessUnitId(Long businessUnitId);

	public List<Contract> findAllByBusinessUnitIdAndPlantCode(Long businessUnitId, Long plantId);

}
