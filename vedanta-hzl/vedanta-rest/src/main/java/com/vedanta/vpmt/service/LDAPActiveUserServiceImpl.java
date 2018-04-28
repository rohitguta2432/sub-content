package com.vedanta.vpmt.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.User;

import lombok.extern.slf4j.Slf4j;

@Transactional
@Service("LDAPActiveUserService")
@Slf4j
public class LDAPActiveUserServiceImpl implements LDAPActiveUserService {

	@Autowired
	private UserDao userDao;

	@Override
	public User get(long id) {
		return null;
	}

	@Override
	public User save(User entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("User object cannot be null/empty");
			throw new VedantaException("User object cannot be null/empty");
		}
		User user = userDao.findOneByEmployeeIdAndBusinessUnitId(entity.getEmployeeId().trim(),
				entity.getLdapBusinessUnitId());

		try {
			if (ObjectUtils.isEmpty(user)) {
				user = userDao.save(entity);
			}

		} catch (VedantaException e) {
			String msg = "Error while saving user information";
			log.info(msg);
			throw new VedantaException(msg);
		}
		return user;

	}

	@Override
	public User update(long id, User entity) {
		return null;
	}

}
