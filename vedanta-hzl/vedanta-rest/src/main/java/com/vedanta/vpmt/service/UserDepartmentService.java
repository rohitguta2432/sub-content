package com.vedanta.vpmt.service;

import java.util.HashSet;
import java.util.List;

import com.vedanta.vpmt.model.UserDepartment;

public interface UserDepartmentService extends VedantaService<UserDepartment> {

	public HashSet<String> getAllUserDepartment(Long businessUnitId);

	public HashSet<String> getAllUserOffice(Long businessUnitId);

	public HashSet<UserDepartment> getAllUserDepartmentByBuId(Long businessUnitId);

	public List<UserDepartment> getUserDepartmentByLimit(Long businessUnitId);

	List<UserDepartment> getUserDepartmentByName(String name, Long buId);

}
