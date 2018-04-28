package com.vedanta.vpmt.service;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.DepartmentDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Department;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service("departmentService")
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;

	@Override
	public Department get(long id) {
		if (id <= 0) {
			log.info("Invalid Department id.");
			throw new VedantaException("Invalid user id.");
		}
		return departmentDao.findOne(id);
	}

	@Override
	public Department save(Department entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Department object cannot be null/empty");
			throw new VedantaException("Department object cannot be null/empty");
		}

		return departmentDao.save(entity);
	}

	@Override
	public Department update(long id, Department entity) {
		if (id <= 0) {
			log.info("Invalid Department id.");
			throw new VedantaException("Invalid department id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("Department object cannot be null/empty.");
			throw new VedantaException("Department object cannot be null/empty.");
		}

		Department existingEntity = departmentDao.findOne(id);

		if (!ObjectUtils.isEmpty(existingEntity)) {
			log.info("Department object already exist.");
			throw new VedantaException("Department object already exist.");
		}

		return null;
	}

	@Override
	public List<Department> getAllDepartments() {
		try {
			List<Department> department = (List<Department>) departmentDao.findAll();
			return department;
		} catch (VedantaException e) {
			String msg = "Error while getting all departments";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<Department> getAllByPlantId(Long plantId) {
		if (plantId <= 0) {
			log.info("Invalid Plant id.");
			throw new VedantaException("Invalid plant id.");
		}
		try {
			List<Department> department = departmentDao.findAllByPlantId(plantId);
			return department;
		} catch (VedantaException e) {
			String msg = "Error while getting all departments by plant";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Department findByDepartmentCode(@NonNull @NotEmpty String departmentCode) {
		try {
			return departmentDao.findByDepartmentCode(departmentCode);
		} catch (VedantaException e) {
			String msg = "Error while getting department by department code " + departmentCode;
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
