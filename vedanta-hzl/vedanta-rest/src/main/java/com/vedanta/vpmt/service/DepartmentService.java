package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.Department;

import java.util.List;

public interface DepartmentService extends VedantaService<Department> {

	public List<Department> getAllDepartments();

	public List<Department> getAllByPlantId(Long plantId);

	Department findByDepartmentCode(String departmentCode);
}
