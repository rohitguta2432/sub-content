package com.vedanta.vpmt.service;

import com.vedanta.vpmt.dao.FormGroupUserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.FormGroupUser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FormGroupUserServiceImpl implements FormGroupUserService {

	@Autowired
	private FormGroupUserDao formGroupUserDao;

	@Override
	public FormGroupUser get(long id) {
		if (id <= 0) {
			log.info("Invalid FormGroupUser id.");
			throw new VedantaException("Invalid FormGroupUser id.");
		}
		return formGroupUserDao.findOne(id);
	}

	@Override
	public FormGroupUser save(@NonNull FormGroupUser entity) {
		return formGroupUserDao.save(entity);
	}

	@Override
	public FormGroupUser update(long id, @NonNull FormGroupUser entity) {
		if (id <= 0) {
			log.info("Invalid FormGroupUser id.");
			throw new VedantaException("Invalid FormGroupUser id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("FormGroupUser object cannot be null/empty.");
			throw new VedantaException("FormGroupUser object cannot be null/empty.");
		}

		FormGroupUser existingEntity = formGroupUserDao.findOne(id);
		if (ObjectUtils.isEmpty(existingEntity)) {
			String msg = String.format("FormGroupUser object does not exist for id %s", id);
			log.error(msg);
			throw new VedantaException("FormGroupUser object does not exist.");
		}
		entity.setId(id);
		return formGroupUserDao.save(entity);
	}

	@Override
	public List<FormGroupUser> save(@NonNull String poItem, @NonNull String contractNumber,
			@NonNull List<FormGroupUser> formGroupUsers) {
		try {

			List<FormGroupUser> savedFormGroupUsers = new ArrayList<>();

			formGroupUsers.forEach(formGroupUser -> {
				List<FormGroupUser> exitingGroupUsers = formGroupUserDao
						.getFormGroupUserByContractNumberAndContractManagerId(contractNumber,
								formGroupUser.getContractManagerId());
				if (!CollectionUtils.isEmpty(exitingGroupUsers)) {
					formGroupUserDao.delete(exitingGroupUsers);
				}

				if (formGroupUser.getFormGroupDetailId() == null) {
					formGroupUser.setFormGroupDetailId(0L);
				}
				FormGroupUser savedFormGroupUser = formGroupUserDao.save(formGroupUser);
				savedFormGroupUsers.add(savedFormGroupUser);
			});
			return savedFormGroupUsers;
		} catch (Exception e) {
			String msg = "error while saving score card user list ";
			log.error(msg);
			throw new VedantaException("ScoreCardGroupUser object does not exist.");

		}
	}

	@Override
	public List<FormGroupUser> getFormGroupUsersForContract(@NonNull String contractNumber, long templateId,
			long groupId, long businessUnitId) {
		try {
			return formGroupUserDao.getFormGroupUserForContractAndForm(contractNumber, templateId, groupId,
					businessUnitId);
		} catch (Exception e) {
			String msg = "error while saving score card user list ";
			log.error(msg);
			throw new VedantaException("FormGroupUser object does not exist.");

		}
	}

}
