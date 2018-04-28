package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.mapper.UserFormGroupDetailsMapper;
import com.vedanta.vpmt.model.UserFormGroup;

public interface UserFormGroupService extends VedantaService<UserFormGroup> {

	public List<UserFormGroup> getAllUserFormGroups();

	List<UserFormGroup> saveUserFormGroupDetailsMapper(UserFormGroupDetailsMapper entity);

	List<UserFormGroup> getUserFormGroupDataByVendorIdAndContractIdAndUserIdAndFormIdAnd(Long userId, Long vendorId,
			Long contractId, Long formId);
}
