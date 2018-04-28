package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Long> {

	public List<Department> findAllByPlantId(long plantId);

	Department findByDepartmentCode(String departmentCode);
}
