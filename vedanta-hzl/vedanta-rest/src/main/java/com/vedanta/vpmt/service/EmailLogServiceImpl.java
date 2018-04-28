package com.vedanta.vpmt.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.EmailLogDao;
import com.vedanta.vpmt.dao.ScoreCardDao;
import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.EmailLog;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailLogServiceImpl implements EmailLogService{
	@Autowired
	private EmailLogDao emailLogDao;
	
	@Autowired
	private ScoreCardDao scorecardDao;
	
	@Autowired
	private UserDao userDao;
	

	@Override
	public EmailLog get(long id) {
		return null;
	}

	@Override
	public EmailLog save(EmailLog entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Email object cannot be null");
			throw new VedantaException("Email object cannot be null");
		}
		return emailLogDao.save(entity); 

	}

	@Override
	public EmailLog update(long id, EmailLog entity) {
		return null;
	}

	//for scheduler scoreCard Email notification
	@Override
	public List<ScoreCard> getAllScoreCard(int status) {
		if (StringUtils.isEmpty(status)) {
			log.info("Email Notification status  cannot be null");
			throw new VedantaException("Email Notification status cannot be null");
		}
		return scorecardDao.getAllScoreCard(status);
	}

	@Override
	public User getUserDetailsbyUserName(String name) {		
		return emailLogDao.getUserDetails(name);
	}

	@Override
	public EmailLog getEmailLogDetails(Long userId, int stageId) {
		if (StringUtils.isEmpty(userId)) {
			log.info("Email Log Assigned UserId  cannot be null");
			throw new VedantaException("Email Log Assigned UserId  cannot be null");
		}
		return emailLogDao.getEmailLog(userId, stageId);
	}

	@Override
	public List<User> getAllUsersNotAdmin() {		
		try {
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);
				List<User> users = (List<User>) userDao.getAllUsersNotAdmin(authorties,Constants.STATUS_DELETE);
				return users;
			} catch (VedantaException e) {
				String msg = "Error while getting all users by web";
				log.error(msg);
				throw new VedantaException(msg);
			}
		}

	@Override
	public List<EmailLog> getEmailLogDetailsByStageAndStatus(int statusId, int stageId) {
		if (StringUtils.isEmpty(statusId)) {
			log.info("Email Log statusId  cannot be null");
		}
		return emailLogDao.getEmailLogByStageAndStatus(statusId, stageId);
	}

	@Override
	public Set<ScoreCardGroupUser> getScorecardgroupUserDetailsByContractNumber(String contractnumber) {
		return emailLogDao.getScorecardgroupUser(contractnumber);
	}

	@Override
	public List<Contract> getContractDetailsByPlantId(Long plantId) {	
		return emailLogDao.getContractDetailsByPlantId(plantId);
	}

	@Override
	public List<Contract> getContractDetailsByVendorId(Long vendorId) {
		return emailLogDao.getContractDetailsByVendorId(vendorId);
	}

	@Override
	public List<Form> getFormDetailsBycategoryIdAndSubcategoryId(Long categoryId, Long subcategoryId) {		
		return emailLogDao.getFormDetailsBycategoryIdAndSubcategoryId(categoryId, subcategoryId);
	}
}
