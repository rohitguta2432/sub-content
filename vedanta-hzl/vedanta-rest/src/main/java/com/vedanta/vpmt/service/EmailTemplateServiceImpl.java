package com.vedanta.vpmt.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.dao.EmailTemplateDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.EmailTemplate;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.util.VedantaUtility;

@Service("EmailTemplateServiceImpl")
@Transactional
public class EmailTemplateServiceImpl implements EmailTemplateService {
	private static final Logger logger = LoggerFactory.getLogger(EmailTemplateServiceImpl.class);

	@Autowired
	private EmailTemplateDao emailTemplateDao;

	@Override
	public EmailTemplate get(long id) {
		if (id <= 0) {
			logger.info("Invalid email template id.");
			throw new VedantaException("Invalid email template id.");
		}
		return emailTemplateDao.findOne(id);
	}

	@Override
	public EmailTemplate save(EmailTemplate entity) {
		if (ObjectUtils.isEmpty(entity)) {
			logger.info("Email template object cannot be null");
			throw new VedantaException("Email template object cannot be null");
		}
		return emailTemplateDao.save(entity);
	}

	@Override
	public EmailTemplate update(long id, EmailTemplate entity) {
		if (id <= 0) {
			logger.info("Invalid Email template id.");
			throw new VedantaException("Invalid email template id.");
		}

		if (ObjectUtils.isEmpty(entity)) {
			logger.info("Email template object cannot be null");
			throw new VedantaException("Email template object cannot be null");
		}

		EmailTemplate existingEntity = emailTemplateDao.findOne(id);

		if (!ObjectUtils.isEmpty(existingEntity)) {
			logger.info("Email template object already exist.");
			throw new VedantaException("Email template object already exist.");
		}
		return null;
	}

	@Override
	public List<EmailTemplate> getAllEmailTemplate() {
		try {
			List<EmailTemplate> emailTemplate = null;
			if (VedantaUtility.getCurrentUserBuId() == 0
					&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
				emailTemplate = emailTemplateDao.getAllEmailtemplateByStatus();
			} else {
				emailTemplate = emailTemplateDao
						.getAllEmailtemplateByBusinessUnitId(VedantaUtility.getCurrentUserBuId());
			}

			return emailTemplate;
		} catch (VedantaException e) {
			logger.error("Error while getting all email template");
			throw new VedantaException("Error while getting all email template");
		}
	}

	@Override
	public User getUserByEmployeeId(String employeeId) {
		return emailTemplateDao.getUserByEmployeeId(employeeId);
	}

}
