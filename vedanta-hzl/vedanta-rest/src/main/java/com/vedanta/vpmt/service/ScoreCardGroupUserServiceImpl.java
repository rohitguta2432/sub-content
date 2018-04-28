package com.vedanta.vpmt.service;

import com.vedanta.vpmt.dao.ScoreCardGroupUserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScoreCardGroupUserServiceImpl implements ScoreCardGroupUserService {

	@Autowired
	private ScoreCardGroupUserDao scoreCardGroupUserDao;

	@Override
	public ScoreCardGroupUser get(long id) {
		if (id <= 0) {
			log.info("Invalid ScoreCardGroupUser id.");
			throw new VedantaException("Invalid ScoreCardGroupUser id.");
		}
		return scoreCardGroupUserDao.findOne(id);
	}

	@Override
	public ScoreCardGroupUser save(@NonNull ScoreCardGroupUser entity) {
		return scoreCardGroupUserDao.save(entity);
	}

	@Override
	public ScoreCardGroupUser update(long id, @NonNull ScoreCardGroupUser entity) {
		if (id <= 0) {
			log.info("Invalid ScoreCardGroupUser id.");
			throw new VedantaException("Invalid ScoreCardGroupUser id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("ScoreCardGroupUser object cannot be null/empty.");
			throw new VedantaException("ScoreCardGroupUser object cannot be null/empty.");
		}

		ScoreCardGroupUser existingEntity = scoreCardGroupUserDao.findOne(id);
		if (ObjectUtils.isEmpty(existingEntity)) {
			String msg = String.format("ScoreCardGroupUser object does not exist for id %s", id);
			log.error(msg);
			throw new VedantaException("ScoreCardGroupUser object does not exist.");
		}
		entity.setId(id);
		return scoreCardGroupUserDao.save(entity);
	}

	@Override
	public List<ScoreCardGroupUser> save(@NonNull String poItem, @NonNull String contractNumber,
			@NonNull List<ScoreCardGroupUser> scoreCardGroupUsers) {
		try {

			List<ScoreCardGroupUser> savedScoreCardGroupUsers = new ArrayList<>();
			scoreCardGroupUsers.forEach(scoreCardGroupUser -> {

				if (!StringUtils.isEmpty(scoreCardGroupUser.getContractManagerId())) {
					List<ScoreCardGroupUser> exitingGroupUsers = scoreCardGroupUserDao
							.getScoreCardGroupUserByContractNumberAndContractManagerId(contractNumber,
									scoreCardGroupUser.getContractManagerId());
					if (!CollectionUtils.isEmpty(exitingGroupUsers)) {
						scoreCardGroupUserDao.delete(exitingGroupUsers);
					}

					if (scoreCardGroupUser.getGroupId() == null) {
						scoreCardGroupUser.setGroupId(0L);
					}
					ScoreCardGroupUser savedScoreCardGroupUser = scoreCardGroupUserDao.save(scoreCardGroupUser);
					savedScoreCardGroupUsers.add(savedScoreCardGroupUser);
				}
			});

			return savedScoreCardGroupUsers;
		} catch (Exception e) {
			String msg = "error while saving score card user list ";
			log.error(msg);
			throw new VedantaException("ScoreCardGroupUser object does not exist.");

		}
	}

	@Override
	public List<ScoreCardGroupUser> getScoreCardGroupUsersForContract(@NonNull String contractNumber, long templateId,
			long groupId, long businessUnitId) {
		try {
			return scoreCardGroupUserDao.getScoreCardGroupUserForContractAndTemplate(contractNumber, templateId,
					groupId, businessUnitId);
		} catch (Exception e) {
			String msg = "error while saving score card user list ";
			log.error(msg);
			throw new VedantaException("ScoreCardGroupUser object does not exist.");

		}
	}

	@Override
	public List<ScoreCardGroupUser> getScoreCardGroupUsersByEmployeeIdOrContractManagerIdAndAllGroups(
			@NonNull String contractManagerId, @NonNull String employeeId) {
		try {
			return scoreCardGroupUserDao
					.getScoreCardGroupUserByContractMangerOrEmployeeIdAndAllGroups(contractManagerId, employeeId, 0L);
		} catch (Exception e) {
			String msg = "error while saving score card user list ";
			log.error(msg);
			throw new VedantaException("ScoreCardGroupUser object does not exist.");

		}
	}

	@Override
	public List<ScoreCardGroupUser> getScoreCardGroupUsersByEmployeeIdAndContractNumberAndBusinessUnitIdAndTemplateIdAndAllGroups(
			@NonNull String contractNumber, @NonNull String employeeId, @NonNull Long businessUnitId,
			@NonNull Long templateId) {
		try {
			return scoreCardGroupUserDao
					.getScoreCardGroupUserByContractNumberAndEmployeeIdAndBusinessUnitIdAndTemplateIdAndAllGroups(
							contractNumber, employeeId, businessUnitId, templateId, 0L);
		} catch (Exception e) {
			String msg = "error while saving score card user list ";
			log.error(msg);
			throw new VedantaException("ScoreCardGroupUser object does not exist.");

		}
	}

}
