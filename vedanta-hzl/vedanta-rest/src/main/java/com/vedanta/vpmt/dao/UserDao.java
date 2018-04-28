package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface UserDao extends CrudRepository<User, Long> {

	public User findByName(String name);

	public User findOneByEmail(String email);

	public User findOneByEmployeeId(String employeeId);

	public List<User> findAllByParentId(String parentId);

	@Query("SELECT u FROM User u WHERE u.employeeId IN (?1)")
	public List<User> getAllRMByParentId(String parentId);

	public User findOneByLoginId(String loginId);

	@Query("SELECT u FROM User u WHERE u.loginId=?1 AND u.businessUnitId=?2 AND u.status=?3")
	public User findByLoginId(String loginId, Long businessUnitId, int status);

	@Query("SELECT u FROM User u WHERE u.employeeId=?1 AND u.businessUnitId=?2")
	public User findByEmployeeIdAndBusinessUnitId(String employeeId, Long businessUnitId);

	@Query("SELECT u FROM User u WHERE u.authorities NOT IN (?1) and u.status!=?2")
	public List<User> getAllUsersNotAdmin(Set<String> authorities, int status);

	@Query("SELECT u FROM User u WHERE u.authorities NOT IN (?1) and u.status!=?2 AND (u.name LIKE %?3% OR u.employeeId LIKE %?3%)")
	public List<User> getAllUsersNotAdminByName(Set<String> authorities, int status, String name);

	@Query("SELECT u FROM User u  WHERE u.authorities NOT IN (?1) and u.status!=?2")
	public List<User> getAllUsersNotAdminByLimit(Set<String> authorities, int status, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.authorities NOT IN (?2) and u.status!=?3")
	public List<User> getAllUsersNotAdmin(Long businessUnitId, Set<String> authorities, int status);

	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.authorities NOT IN (?2) and u.status!=?3 AND (u.name LIKE %?4% OR u.employeeId LIKE %?4%)")
	public List<User> getAllUsersNotAdminAndName(Long businessUnitId, Set<String> authorities, int status, String name);

	@Query(value = "SELECT u FROM User u  WHERE u.businessUnitId=?1 AND u.authorities NOT IN (?2) and u.status!=?3")
	public List<User> getAllUsersNotAdminAndLimit(Long businessUnitId, Set<String> authorities, int status,

			Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.authorities =?2 and u.status=?3")
	public List<User> userByPlantHeadAndBusinessUnitId(Long businessUnitId, String authorities, int status);

	@Query("SELECT u FROM User u WHERE u.employeeId NOT IN (?1) AND u.authorities NOT IN (?2) AND u.businessUnitId=?3 AND u.status=?4")
	public List<User> getAllUsersNotAssigned(Set<String> userList, Set<String> authorities, Long businessUnitId,
			int status, Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.employeeId NOT IN (?1) AND u.authorities NOT IN (?2)  AND u.status=?3")
	public List<User> getAllUsersNotAssigned(Set<String> userList, Set<String> authorities, int status,
			Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.employeeId NOT IN (?1) AND u.authorities NOT IN (?2) AND u.businessUnitId=?3 AND u.status=?4 AND (u.name LIKE %?5% OR u.employeeId LIKE %?5%)")
	public List<User> getAllUserNotAssignedByName(Set<String> userList, Set<String> authorities, Long businessUnitId,
			int status, String name);

	@Query("SELECT u FROM User u WHERE u.employeeId NOT IN (?1) AND u.authorities NOT IN (?2)  AND u.status=?3 AND (u.name LIKE %?4% OR u.employeeId LIKE %?4%) ")
	public List<User> getAllUserNotAssignedByName(Set<String> userList, Set<String> authorities, int status,
			String name);

	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.status=?2")
	public List<User> findAllByBusinessUnitId(Long businessUnitId, int status);

	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.authorities  NOT IN (?2)")
	public List<User> userByBusinessUnitIdNotAdmin(Long businessUnitId, String authorities);

	@Query("SELECT u FROM User u WHERE u.employeeId=?1 AND u.businessUnitId=?2")
	public User findOneByEmployeeIdAndBusinessUnitId(String employeeId, Long businessUnitId);
	
	@Query("SELECT u FROM User u WHERE u.businessUnitId=?1 AND u.authorities =?2 and u.status=?3")
	public List<User> userByAuthorityAndBusinessUnitId(Long businessUnitId, String authorities, int status);


	@Query("SELECT u FROM User u WHERE u.employeeId IN (?1) AND u.businessUnitId=?2")
	public List<User> findAllByEmployeeIdAndBusinessUnitId(Set<String> employeeIds, long businessUnitId);


}