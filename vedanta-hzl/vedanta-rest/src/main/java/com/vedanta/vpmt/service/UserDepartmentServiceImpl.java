package com.vedanta.vpmt.service;

import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vedanta.vpmt.dao.UserDepartmentDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.UserDepartment;

import lombok.extern.slf4j.Slf4j;

@Service("UserDepartmentService")
@Slf4j
public class UserDepartmentServiceImpl implements UserDepartmentService {

	@Autowired
	private UserDepartmentDao userDepartmentDao;

	@Override
	public UserDepartment get(long id) {
		return null;
	}

	@Override
	public UserDepartment save(UserDepartment entity) {
		return userDepartmentDao.save(entity);
	}

	@Override
	public UserDepartment update(long id, UserDepartment entity) {
		return null;
	}

	@Override
	public HashSet<String> getAllUserDepartment(Long businessUnitId) {
		try {
			HashSet<String> userDepartment = userDepartmentDao.findAllUserDepartment(businessUnitId);
			return userDepartment;
		} catch (VedantaException e) {
			String msg = "Error while getting all user department";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public HashSet<String> getAllUserOffice(Long businessUnitId) {
		try {
			HashSet<String> userDepartment = userDepartmentDao.findAllUserOffice(businessUnitId);
			return userDepartment;
		} catch (VedantaException e) {
			String msg = "Error while getting all user office";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public HashSet<UserDepartment> getAllUserDepartmentByBuId(Long businessUnitId) {
		return userDepartmentDao.getUserDepartmentByBusinessUnitId(businessUnitId);
	}

	@Override
	public List<UserDepartment> getUserDepartmentByLimit(Long businessUnitId) {
		Pageable topTen = new PageRequest(0, 10);
		return userDepartmentDao.getUserDepartmentByLimit(businessUnitId, topTen);
	}

	@Override
	public List<UserDepartment> getUserDepartmentByName(String name, Long buId) {

		List<UserDepartment> departments = null;
		try {
			departments = userDepartmentDao.getUserDepartmentByName(buId, name);
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return departments;
	}

}
