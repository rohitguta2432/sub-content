package com.vedanta.vpmt.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.Contract;

@Repository
public interface ContractDao extends JpaRepository<Contract, Long> {

	public List<Contract> findByVendorId(Long vendorId);

	public Contract findByNumber(String number);

	@Query("select c FROM Contract c where c.categoryId=?1 AND c.subCategoryId=?2 AND c.businessUnitId=?3")
	Optional<List<Contract>> findContractByCategoryIdAndSubCategoryId(Long categoryId, Long subCategoryId,
			Long businessUnitId);

	@Query("select count(c.number)>0 from Contract c where c.number=?1")
	boolean existsByNumber(String number);

	public List<Contract> findAllByBusinessUnitId(Long businessUnitId);

	@Query("SELECT c FROM Contract c WHERE  c.businessUnitId=?1 AND c.plantId=?2")
	public List<Contract> findAllByBusinessUnitIdAndPlantCode(Long businessUnitId, Long plantId);

	public List<Contract> findAllByPlantId(Long plantId);

	@Query("SELECT c FROM Contract c WHERE c.plantId IN (?1) AND c.status=?2")
	public List<Contract> getAllContractsByPlantIdsAndStatus(Set<Long> plantIds, int status);

}
