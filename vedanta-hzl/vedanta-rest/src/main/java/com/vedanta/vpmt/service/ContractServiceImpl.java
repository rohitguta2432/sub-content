package com.vedanta.vpmt.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.ContractDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.VedantaUser;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service("contractService")
@Slf4j
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractDao contractDao;

	@Override
	public Contract get(long id) {
		if (id <= 0) {
			log.info("Invalid Contract id.");
			throw new VedantaException("Invalid Contract id.");
		}
		return contractDao.findOne(id);
	}

	@Override
	public Contract save(Contract entity) {

		if (ObjectUtils.isEmpty(entity)) {
			log.info("Contract object cannot be null/empty");
			throw new VedantaException("Contract object cannot be null/empty");
		}
		return contractDao.save(entity);
	}

	@Override
	public Contract update(long id, Contract entity) {

		if (id <= 0) {
			log.info("Invalid Contract id.");
			throw new VedantaException("Invalid user id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("Contract object cannot be null/empty.");
			throw new VedantaException("Contract object cannot be null/empty.");
		}

		Contract existingEntity = contractDao.findOne(id);
		if (!ObjectUtils.isEmpty(existingEntity)) {
			log.info("Contract object already exist.");
			throw new VedantaException("Contract object already exist.");
		}
		return null;
	}

	@Override
	public List<Contract> getAllContracts() {
		try {
			return contractDao.findAll();
		} catch (VedantaException e) {
			String msg = "Error while getting all contracts by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Contract> getAllContractsByPlantId(Long plantId) {
		try {
			return contractDao.findAllByPlantId(plantId);
		} catch (VedantaException e) {
			String msg = "Error while getting all contracts by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Contract> getAllContractsByPlantIds(Set<Long> plantIds) {
		try {
			return contractDao.getAllContractsByPlantIdsAndStatus(plantIds, Constants.STATUS_ACTIVE);
		} catch (VedantaException e) {
			String msg = "Error while getting all contracts by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Contract> getAllContractsByCurrentBusinessUnitId() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			return contractDao.findAllByBusinessUnitId(vedantaUser.getBusinessUnitId());
		} catch (VedantaException e) {
			String msg = "Error while getting all contracts by rest";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Contract> getContractByVendorId(Long vendorId) {
		if (StringUtils.isEmpty(vendorId)) {
			log.info("Vendor id cannot be null/empty");
			throw new VedantaException("Vendor id cannot be null/empty");
		}

		try {
			return contractDao.findByVendorId(vendorId);
		} catch (VedantaException e) {
			String msg = "Error while getting Contact by Vendor id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Contract findByContractNumber(String contractNumber) {
		if (StringUtils.isEmpty(contractNumber)) {
			log.info("contract number cannot be null/empty");
			throw new VedantaException("contract number cannot be null/empty");
		}

		try {
			Contract contractDetails = contractDao.findByNumber(contractNumber);
			return contractDetails;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Optional<List<Contract>> getContract(Long categoryId, Long subCategoryId, Long bussinessUnitId) {

		if (ObjectUtils.isEmpty(categoryId) || categoryId < 1) {
			log.info("category id cannot be null or less than 1");
			throw new IllegalArgumentException("sub category id cannot be null/empty");
		}

		if (ObjectUtils.isEmpty(subCategoryId) || subCategoryId < 1) {
			log.info("subCategoryId id cannot be null or less than 1");
			throw new IllegalArgumentException("sub category id cannot be null/empty");
		}

		try {
			return contractDao.findContractByCategoryIdAndSubCategoryId(categoryId, subCategoryId, bussinessUnitId);
		} catch (Exception e) {
			String msg = "Error while getting Contract by categoryId " + categoryId + " and sub category id "
					+ subCategoryId;
			log.error(msg, e);
			throw new VedantaException(msg);
		}
	}

	@Override
	public void save(@NonNull List<Contract> contracts) {
		if (CollectionUtils.isEmpty(contracts)) {
			return;
		}

		try {
			for (Contract contract : contracts) {
				if (contractDao.existsByNumber(contract.getNumber())) {
					contractDao.save(contract);
				}
			}
		} catch (Exception e) {
			String msg = "Error while saving Contract";
			log.error(msg, e);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Contract findAndModifyByNumber(Contract contract) {
		Contract existing = findByContractNumber(contract.getNumber());
		if (existing == null) {
			return save(contract);
		}
		return existing;
	}

	@Override
	public List<Contract> findAllByBusinessUnitId(Long businessUnitId) {
		return contractDao.findAllByBusinessUnitId(businessUnitId);
	}

	@Override
	public List<Contract> findAllByBusinessUnitIdAndPlantCode(Long businessUnitId, Long plantId) {
		return contractDao.findAllByBusinessUnitIdAndPlantCode(businessUnitId, plantId);
	}

}
