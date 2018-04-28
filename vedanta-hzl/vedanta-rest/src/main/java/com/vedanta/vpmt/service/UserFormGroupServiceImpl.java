package com.vedanta.vpmt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.UserFormGroupDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.mapper.UserFormGroupDetailsMapper;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.FormGroupDetail;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.UserFormGroup;
import com.vedanta.vpmt.model.Vendor;

import lombok.extern.slf4j.Slf4j;

@Service("userFormGroupService")
@Slf4j
public class UserFormGroupServiceImpl implements UserFormGroupService {

	@Autowired
	private UserFormGroupDao userFormGroupDao;

	@Override
	public UserFormGroup get(long id) {
		if (id <= 0) {
			log.info("Invalid UserFormGroup id.");
			throw new VedantaException("Invalid UserFormGroup id.");
		}
		return userFormGroupDao.findOne(id);
	}

	@Override
	public UserFormGroup save(UserFormGroup entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("UserFormGroup object cannot be null/empty");
			throw new VedantaException("UserFormGroup object cannot be null/empty");
		}

		if (entity.getId() != null) {
			boolean updateEntity = userFormGroupDao.exists(entity.getId());
			if (updateEntity)
				return update(entity.getId(), entity);
		}
		entity.setStatus(1);
		return userFormGroupDao.save(entity);
	}

	@Override
	public List<UserFormGroup> saveUserFormGroupDetailsMapper(UserFormGroupDetailsMapper entity) {

		if (ObjectUtils.isEmpty(entity)) {
			log.info("UserFormGroupDetailsMapper object cannot be null/empty");
			throw new VedantaException("UserFormGroupDetailsMapper object cannot be null/empty");
		}
		List<UserFormGroup> userFormGroups = null;
		try {
			userFormGroups = new ArrayList<UserFormGroup>();
			User user = entity.getUser();
			Form form = entity.getForm();
			Vendor vendor = entity.getVendor();
			Contract contract = entity.getContract();
			for (FormGroupDetail formGroupDetail : entity.getFormGroupDetails()) {
				UserFormGroup userFormGroup = new UserFormGroup();
				userFormGroup.setUserId(user.getId());
				userFormGroup.setVendorId(vendor.getId());
				userFormGroup.setContractId(contract.getId());
				userFormGroup.setFormId(form.getId());
				userFormGroup.setGroupId(formGroupDetail.getId());
				userFormGroup.setStatus(1);
				userFormGroup = userFormGroupDao.save(userFormGroup);
				userFormGroups.add(userFormGroup);
			}
		} catch (VedantaException ee) {
			log.info("Error while saving UserFormGroupDetailsMapper to UserFormGroup");
			throw new VedantaException("Error while saving UserFormGroupDetailsMapper to UserFormGroup");
		}
		return userFormGroups;
	}

	@Override
	public UserFormGroup update(long id, UserFormGroup entity) {
		if (id <= 0) {
			log.info("Invalid UserFormGroup id.");
			throw new VedantaException("Invalid UserFormGroup id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("UserFormGroup object cannot be null/empty.");
			throw new VedantaException("UserFormGroup object cannot be null/empty.");
		}

		UserFormGroup existingEntity = userFormGroupDao.findOne(id);
		if (ObjectUtils.isEmpty(existingEntity)) {
			log.info("UserFormGroup object does not exist.");
			throw new VedantaException("UserFormGroup object does not exist.");
		}
		updateUserFormGroup(existingEntity, entity);
		return userFormGroupDao.save(existingEntity);
	}

	private void updateUserFormGroup(UserFormGroup existingEntity, UserFormGroup entity) {

		if (entity.getVendorId() != null)
			existingEntity.setVendorId(entity.getVendorId());
		if (entity.getFormId() != null)
			existingEntity.setFormId(entity.getFormId());
		if (entity.getGroupId() != null)
			existingEntity.setGroupId(entity.getGroupId());
		if (entity.getStatus() > 0)
			existingEntity.setStatus(entity.getStatus());
	}

	@Override
	public List<UserFormGroup> getAllUserFormGroups() {
		try {
			List<UserFormGroup> userFormGroup = (List<UserFormGroup>) userFormGroupDao
					.getUserFormGroupsByStatus(Constants.STATUS_ACTIVE);
			return userFormGroup;
		} catch (VedantaException e) {
			String msg = "Error while getting all userFormGroup by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<UserFormGroup> getUserFormGroupDataByVendorIdAndContractIdAndUserIdAndFormIdAnd(Long userId,
			Long vendorId, Long contractId, Long formId) {
		try {
			List<UserFormGroup> userFormGroup = (List<UserFormGroup>) userFormGroupDao
					.getUserFormGroupsByUserIdAndVendorIdAndContractIdAndFormId(Constants.STATUS_ACTIVE, userId,
							vendorId, contractId, formId);
			return userFormGroup;
		} catch (VedantaException e) {
			String msg = "Error while getting all userFormGroup by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
