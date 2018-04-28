package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.GroupDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Group;

import lombok.extern.slf4j.Slf4j;

@Service("groupService")
@Slf4j
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupDao groupDao;

	@Override
	public Group get(long id) {
		if (id <= 0) {
			log.info("Invalid Group id.");
			throw new VedantaException("Invalid Group id.");
		}
		return groupDao.findOne(id);
	}

	@Override
	public Group save(Group entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Group object cannot be null/empty");
			throw new VedantaException("Group object cannot be null/empty");
		}

		if (entity.getId() != null) {
			boolean updateEntity = groupDao.exists(entity.getId());
			if (updateEntity)
				return update(entity.getId(), entity);
		}
		entity.setStatus(1);
		return groupDao.save(entity);
	}

	@Override
	public Group update(long id, Group entity) {
		if (id <= 0) {
			log.info("Invalid Group id.");
			throw new VedantaException("Invalid Group id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			log.info("Group object cannot be null/empty.");
			throw new VedantaException("Group object cannot be null/empty.");
		}

		Group existingEntity = groupDao.findOne(id);

		if (ObjectUtils.isEmpty(existingEntity)) {
			log.info("Group object does not exist.");
			throw new VedantaException("Group object does not exist.");
		}

		updateGroup(existingEntity, entity);
		return groupDao.save(existingEntity);
	}

	private void updateGroup(Group existingEntity, Group entity) {

		if (entity.getLabelName() != null)
			existingEntity.setLabelName(entity.getLabelName());
		if (entity.getName() != null)
			existingEntity.setName(entity.getName());
		if (entity.getLabelName() != null)
			existingEntity.setLabelName(entity.getLabelName());
		if (entity.getDescription() != null)
			existingEntity.setDescription(entity.getDescription());
		if (entity.getStatus() > 0)
			existingEntity.setStatus(entity.getStatus());
	}

	@Override
	public List<Group> getAllGroups() {
		try {
			List<Group> group = (List<Group>) groupDao.getFormGroupsByStatus(Constants.STATUS_ACTIVE);
			return group;
		} catch (VedantaException e) {
			String msg = "Error while getting all group by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
