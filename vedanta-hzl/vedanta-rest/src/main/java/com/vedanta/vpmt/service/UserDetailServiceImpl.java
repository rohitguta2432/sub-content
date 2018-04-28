package com.vedanta.vpmt.service;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userDetailServiceImpl")
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PlantService plantService;

	public User findByLoginId(String username, Long businessUnitId) {
		if (StringUtils.isEmpty(username)) {
			log.info("User id cannot be null/empty");
			throw new VedantaException("User id cannot be null/empty");
		}
		return userDao.findByLoginId(username, businessUnitId, Constants.STATUS_ACTIVE);
	}

	public UserDetails loadUserByUsernameAndBuId(String userName, Long businessUnitId) {

		User user = userDao.findByLoginId(userName, businessUnitId, Constants.STATUS_ACTIVE);

		if (user == null) {
			log.info("No user found with user name");
			throw new UsernameNotFoundException("No user found with loginId");
		}
		if (user.getIsWithoutAdAllowed() > 0) {
			return new VedantaUser(user.getId(), user.getName(), user.getPassword(), user.getLoginId(),
					AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities()), user.getEmployeeId(),
					user.getEmail(), user.getStatus() > 0, user.getIsWithoutAdAllowed(), user.getBusinessUnitId(),
					user.getPlantCode(), user.getPlantId());
		}

		return new VedantaUser(user.getId(), user.getName(), Constants.P_STRING, user.getLoginId(),
				AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities()), user.getEmployeeId(),
				user.getEmail(), user.getStatus() > 0, user.getIsWithoutAdAllowed(), user.getBusinessUnitId(),
				user.getPlantCode(), user.getPlantId());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userDao.findOneByLoginId(username);
		if (user == null) {
			log.info("No user found with user name");
			throw new UsernameNotFoundException("No user found with loginId");
		}

		if (user.getIsWithoutAdAllowed() > 0) {
			return new VedantaUser(user.getId(), user.getName(), user.getPassword(), user.getLoginId(),
					AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities()), user.getEmployeeId(),
					user.getEmail(), user.getStatus() > 0, user.getIsWithoutAdAllowed(), user.getBusinessUnitId(),
					user.getPlantCode(), user.getPlantId());
		}
		return new VedantaUser(user.getId(), user.getName(), Constants.P_STRING, user.getLoginId(),
				AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities()), user.getEmployeeId(),
				user.getEmail(), user.getStatus() > 0, user.getIsWithoutAdAllowed(), user.getBusinessUnitId(),
				user.getPlantCode(), user.getPlantId());
	}

	public User getUserByUserId(Long id) {
		if (StringUtils.isEmpty(id)) {
			log.info("User id cannot be null/empty");
			throw new VedantaException("User id cannot be null/empty");
		}

		try {
			return userDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while getting user by user id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<User> getAllUsers() {

		try {
			List<User> users = null;

			if (VedantaUtility.getCurrentUserBuId() == 0
					&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
				users = (List<User>) userDao.findAll();
			} else {
				users = userDao.findAllByBusinessUnitId(VedantaUtility.getCurrentUserBuId(), Constants.STATUS_ACTIVE);
			}

			return users;

		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<User> getAllUsersNotAdmin() {

		try {
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
			List<User> users = new ArrayList<>();

			if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
				users = (List<User>) userDao.getAllUsersNotAdmin(authorties, Constants.STATUS_DELETE);
			} else {
				users = (List<User>) userDao.getAllUsersNotAdmin(VedantaUtility.getCurrentUserBuId(), authorties,
						Constants.STATUS_DELETE);
			}
			return users;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<User> getAllUsersNotAdminAndBuId(Long buId) {

		try {
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
			List<User> users = new ArrayList<>();
			users = (List<User>) userDao.getAllUsersNotAdmin(buId, authorties, Constants.STATUS_DELETE);
			return users;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<User> getAllUsersNotAdminByLimit() {

		try {
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
			List<User> users = new ArrayList<>();
			Pageable topTen = new PageRequest(0, 10);
			if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {

				users = (List<User>) userDao.getAllUsersNotAdminByLimit(authorties, Constants.STATUS_DELETE, topTen);
			} else {
				users = (List<User>) userDao.getAllUsersNotAdminAndLimit(VedantaUtility.getCurrentUserBuId(),
						authorties, Constants.STATUS_DELETE, topTen);

			}
			return users;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<User> getAllUsersNotAdminByLimitByBuId(Long buId) {

		try {
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
			List<User> users = new ArrayList<>();
			Pageable topTen = new PageRequest(0, 10);
			users = (List<User>) userDao.getAllUsersNotAdminAndLimit(buId, authorties, Constants.STATUS_DELETE, topTen);
			return users;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	// like query
	public List<User> getAllUsersNotAdminByName(String name) {

		try {
			List<User> users = new ArrayList<>();
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);

			if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
				users = (List<User>) userDao.getAllUsersNotAdminByName(authorties, Constants.STATUS_DELETE, name);
			} else {
				users = (List<User>) userDao.getAllUsersNotAdminAndName(VedantaUtility.getCurrentUserBuId(), authorties,
						Constants.STATUS_DELETE, name);
			}
			return users;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<User> getAllUsersNotAdminByNameAndBuId(String name, Long buId) {

		try {
			List<User> users = new ArrayList<>();
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
			users = (List<User>) userDao.getAllUsersNotAdminAndName(buId, authorties, Constants.STATUS_DELETE, name);
			return users;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public User getUserByEmployeeId(String employeeId) {

		if (StringUtils.isEmpty(employeeId)) {
			log.info("Employee id cannot be null/empty");
			throw new VedantaException("Employee id cannot be null/empty");
		}

		try {
			return userDao.findOneByEmployeeId(employeeId);
		} catch (VedantaException e) {
			String msg = "Error while getting user by employee id";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}

	public List<User> getAllRMUsersByParentId(String parentId) {

		if (StringUtils.isEmpty(parentId)) {
			log.info("Parent id cannot be null/empty");
			throw new VedantaException("Parent id cannot be null/empty");
		}

		try {
			User user = this.getUserByEmployeeId(parentId);
			if (user == null) {
				log.error("No user found by given employeeId");
				throw new VedantaException("No user found by given employeeId");
			}

			List<User> users = new ArrayList<>();
			if (user.getAuthorities().contains(Constants.ROLE_SRM)) {
				users.addAll(userDao.findAllByParentId(parentId));
			} else if (user.getAuthorities().contains(Constants.ROLE_SEGMENT_HEAD)) {
				List<User> srmUsers = userDao.findAllByParentId(parentId);
				srmUsers.forEach(srmUser -> {
					users.addAll(userDao.findAllByParentId(srmUser.getEmployeeId()));
				});
			}
			return users;
		} catch (VedantaException e) {
			String msg = "Error while getting user by parent id";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}

	public List<User> getUsersByParentId(String parentId) {

		if (StringUtils.isEmpty(parentId)) {
			log.info("Parent id cannot be null/empty");
			throw new VedantaException("Parent id cannot be null/empty");
		}

		try {
			return userDao.findAllByParentId(parentId);
		} catch (VedantaException e) {
			String msg = "Error while getting user by parent id";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}

	public User save(User updateUser) {

		if (updateUser == null) {
			log.info("User can not be empty or null");
			throw new VedantaException("User can not be empty or null");
		}
		/*
		 * boolean flag = this.validateUser(updateUser); if (!flag) { return updateUser;
		 * }
		 */
		if (ObjectUtils.isEmpty(updateUser.getId())) {
			Plant plant = plantService.getPlantByBusinessUnitIdAndPlantCode(updateUser.getBusinessUnitId(),
					updateUser.getPlantCode());
			if (!ObjectUtils.isEmpty(plant)) {
				updateUser.setPlantId(plant.getId());
				updateUser.setPlantName(plant.getName());
			}
		}
		try {

			return userDao.save(updateUser);
		} catch (VedantaException e) {
			String msg = "Error while  updating by user id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public User saveLDAPUser(User updateUser) {

		if (updateUser == null) {
			log.info("User can not be empty or null");
			throw new VedantaException("User can not be empty or null");
		}
		try {

			return userDao.save(updateUser);
		} catch (VedantaException e) {
			String msg = "Error while  updating by user id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private Boolean validateUser(User entity) {

		if (ObjectUtils.isEmpty(entity.getLoginId()) || ObjectUtils.isEmpty(entity.getEmployeeId())) {
			log.info("User login Id or Employee Id can not be empty or null");
			throw new VedantaException("User login Id or Employee Id can not be empty or null");
		}

		User user = null;
		user = userDao.findOneByLoginId(entity.getLoginId());

		if (!ObjectUtils.isEmpty(user))
			return false;

		user = userDao.findOneByEmployeeId(entity.getEmployeeId());

		if (!ObjectUtils.isEmpty(user))
			return false;

		return true;
	}

	public List<User> userByPlantHeadAndBusinessUnitId(Long businessUnitId) {

		if (StringUtils.isEmpty(businessUnitId)) {
			log.info("business unit id cannot be null/empty");
			throw new VedantaException("business unit id cannot be null/empty");
		}
		try {
			return userDao.userByPlantHeadAndBusinessUnitId(businessUnitId, Constants.ROLE_PLANT_HEAD,
					Constants.STATUS_ACTIVE);
		} catch (VedantaException e) {
			String msg = "Error while getting user by business unit id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public List<User> getUserByBusinessUnitIdNotAdmin(Long businessUnitId) {

		if (StringUtils.isEmpty(businessUnitId)) {
			log.info("business unit id cannot be null/empty");
			throw new VedantaException("business unit id cannot be null/empty");
		}
		try {

			return userDao.userByBusinessUnitIdNotAdmin(businessUnitId, Constants.ROLE_ADMIN);
		} catch (VedantaException e) {
			String msg = "Error while getting user by business unit id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	public User getUserByEmployeeIdAndBusinessUnitId(String employeeId, Long businessUnitId) {

		if (StringUtils.isEmpty(employeeId)) {
			log.info("Employee id cannot be null/empty");
			throw new VedantaException("Employee id cannot be null/empty");
		}
		try {
			return userDao.findByEmployeeIdAndBusinessUnitId(employeeId, businessUnitId);
		} catch (VedantaException e) {
			String msg = "Error while getting user by employee id";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}

	public List<User> getAllUserByHumanResourceAndBusinessUnitId(Long businessUnitId) {

		if (StringUtils.isEmpty(businessUnitId)) {
			log.info("business unit id cannot be null/empty");
			throw new VedantaException("business unit id cannot be null/empty");
		}
		try {
			return userDao.userByAuthorityAndBusinessUnitId(businessUnitId, Constants.ROLE_HR, Constants.STATUS_ACTIVE);
		} catch (VedantaException e) {
			String msg = "Error while getting user by business unit id and role";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

}
