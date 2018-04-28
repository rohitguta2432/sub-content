package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.GroupFieldDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.GroupField;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 11/09/17.
 */
@Service("groupFieldService")
@Slf4j
public class GroupFieldServiceImpl implements GroupFieldService {

	@Autowired
	private GroupFieldDao groupFieldDao;

	@Override
	public GroupField get(long id) {
		if (id <= 0) {
			String msg = "Invalid Group Field id.";
			log.info(msg);
			throw new IllegalArgumentException(msg);
		}
		try {
			return groupFieldDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching groupField by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public GroupField save(GroupField entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("GroupField object cannot be null/empty");
			throw new VedantaException("GroupField object cannot be null/empty");
		}

		if (entity.getFieldId() <= 0) {
			log.info("Invalid field id");
			throw new IllegalArgumentException("Invalid field id");
		}
		if (entity.getGroupId() <= 0) {
			log.info("Invalid group id");
			throw new IllegalArgumentException("Invalid group id");
		}

		try {
			return groupFieldDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public boolean saveGroupFieldList(Long groupId, List<GroupField> entity) {
		if (CollectionUtils.isEmpty(entity)) {
			log.info("GroupField list cannot be null/empty");
			throw new VedantaException("GroupField list cannot be null/empty");
		}
		try {
			List<GroupField> savedGroupFields = this.getAllGroupFieldsByGroupId(groupId);
			if (!CollectionUtils.isEmpty(savedGroupFields)) {
				groupFieldDao.delete(savedGroupFields);
			}
			groupFieldDao.save(entity);
			return true;
		} catch (VedantaException e) {
			String msg = "Error while saving GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public GroupField update(long id, GroupField entity) {
		return null;
	}

	@Override
	public List<GroupField> getAllGroupFields() {
		try {
			List<GroupField> fields = (List<GroupField>) groupFieldDao.findAllByStatus(Constants.STATUS_ACTIVE);
			return fields;
		} catch (VedantaException e) {
			String msg = "Error while getting all group field";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<GroupField> getAllGroupFieldsByGroupId(long groupId) {
		if (groupId <= 0) {
			log.info("Invalid group id");
			throw new IllegalArgumentException("Invalid group id");
		}
		try {
			List<GroupField> fields = groupFieldDao.findAllByGroupId(groupId);
			return fields;
		} catch (VedantaException e) {
			String msg = "Error while fetching group fields by group id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public boolean deleteByGroup(long groupId) {
		if (groupId <= 0) {
			log.info("Invalid group id");
			throw new IllegalArgumentException("Invalid group id");
		}

		try {
			List<GroupField> savedGroupFields = this.getAllGroupFieldsByGroupId(groupId);
			if (!CollectionUtils.isEmpty(savedGroupFields)) {
				groupFieldDao.delete(savedGroupFields);
			}
			return true;
		} catch (VedantaException e) {
			String msg = "Error while removing GroupField information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}
}
