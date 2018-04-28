package com.vedanta.vpmt.dao;

import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vedanta.vpmt.model.UserDepartment;

public interface UserDepartmentDao extends JpaRepository<UserDepartment, Long> {

	@Query("SELECT TRIM(d.departmentName)  FROM UserDepartment d WHERE d.businessUnitId=?1")
	public HashSet<String> findAllUserDepartment(Long businessUnitId);

	@Query("SELECT TRIM(d.officeName)  FROM UserDepartment d WHERE d.businessUnitId=?1")
	public HashSet<String> findAllUserOffice(Long businessUnitId);

	@Query("SELECT u.departmentName from User u WHERE u.businessUnitId=?1 GROUP BY u.departmentName")
	public HashSet<String> getUniqueDepartment(Long businessUnitId);

	@Query("SELECT u.office from User u WHERE u.businessUnitId=?1 GROUP BY u.office")
	public HashSet<String> getUniqueOffice(Long businessUnitId);

	@Query("SELECT ud from UserDepartment ud WHERE ud.businessUnitId=?1")
	public HashSet<UserDepartment> getUserDepartmentByBusinessUnitId(Long businessUnitId);

	@Query("SELECT ud from UserDepartment ud WHERE ud.businessUnitId=?1")
	public List<UserDepartment> getUserDepartmentByLimit(Long businessUnitId, Pageable pageable);

	@Query("SELECT u FROM UserDepartment u WHERE u.businessUnitId=?1 AND (u.departmentName LIKE %?2% OR u.officeName LIKE %?2%)")
	public List<UserDepartment> getUserDepartmentByName(Long businessUnitId, String name);

}
