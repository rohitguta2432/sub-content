package com.vedanta.vpmt.web.service;

import com.vedanta.vpmt.model.Contract;

import java.util.List;

/**
 * <h3>This Interface manage the all Contracts activity</h3>
 * 
 * @author SAUD AHMAD
 * 
 * @since 16-sept-2017
 * @version v.0.0
 */
public interface ContractService extends Service<Contract> {

	List<Contract> getContracts();

	List<Contract> getContractByVendor(long id);

}
