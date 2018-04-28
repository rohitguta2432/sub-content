package com.vedanta.vpmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.vedanta.vpmt.model.UserFormGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFormGroupDao extends CrudRepository<UserFormGroup, Long> {

	@Query(value = "SELECT * FROM user_form_group ufg WHERE ufg.status=?1  GROUP BY ufg.user_id,ufg.vendor_id,ufg.contract_id,ufg.form_id", nativeQuery = true)
	public List<UserFormGroup> getUserFormGroupsByStatus(int status);

	@Query(value = "SELECT * FROM user_form_group d WHERE d.status=?1 AND d.user_id =?2 AND d.vendor_id =?3 AND d.contract_id =?4 AND d.form_id =?5", nativeQuery = true)
	public List<UserFormGroup> getUserFormGroupsByUserIdAndVendorIdAndContractIdAndFormId(int statusActive, Long userId,
			Long vendorId, Long contractId, Long formId);

}
