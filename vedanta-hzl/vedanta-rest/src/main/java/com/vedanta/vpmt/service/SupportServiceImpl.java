package com.vedanta.vpmt.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.SupportDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Support;
import com.vedanta.vpmt.model.VedantaUser;

import lombok.extern.slf4j.Slf4j;

@Service("supportService")
@Slf4j
public class SupportServiceImpl implements SupportService {

	@Autowired
	private SupportDao supportDao;

	@Autowired
	private MailSenderServices mailSenderServices;

	@Override
	public Support get(long id) {
		return null;
	}

	@Override
	@Transactional
	public Support save(Support entity) {

		Support support = null;

		if (ObjectUtils.isEmpty(entity)) {
			log.info("Support object cannot be null");
			throw new VedantaException("Support object cannot be null");
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();
		String token = "";
		entity.setTokenNo(token);

		entity.setEmployeeId(vedantaUser.getEmployeeId());
		entity.setBusinessUnitId(vedantaUser.getBusinessUnitId());
		entity.setCreatedOn(new Date());
		entity.setStatus(Constants.STATUS_OPENED);
		mailSenderServices.supportMailSend(entity);
		if (!ObjectUtils.isEmpty(entity.getFileList())) {

			for (String fileName : entity.getFileList()) {
				Support supportData = new Support();
				supportData.setBusinessUnitId(entity.getBusinessUnitId());
				supportData.setEmailId(entity.getEmailId());
				supportData.setMessage(entity.getMessage());
				supportData.setEmployeeId(entity.getEmployeeId());
				supportData.setTitle(entity.getTitle());
				supportData.setTokenNo(entity.getTokenNo());
				supportData.setStatus(entity.getStatus());
				supportData.setFileName(fileName);
				supportData.setCreatedOn(entity.getCreatedOn());
				support = supportDao.save(supportData);
			}

		} else {
			support = supportDao.save(entity);
		}

		// update token
		token = "VPM_" + support.getId();
		int isTokenUpdated = supportDao.updateToken(support.getId(), token);
		log.debug("Token " + token + " updated with id " + support.getId() + "  result:" + isTokenUpdated);
		return support;

	}

	@Override
	public Support update(long id, Support entity) {
		entity.setId(id);
		int result = supportDao.updateSupport(entity.getStatus(), id);
		log.debug("support updated: " + result);
		return entity;
	}

	private String genrateSupportToken() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();

		RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange('0', '9')
				.filteredBy(CharacterPredicates.DIGITS).build();
		String randomNo = randomStringGenerator.generate(6);

		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		String dt = dateFormat.format(new Date());
		Long buId = vedantaUser.getBusinessUnitId();
		String token = "VDT" + buId + "_" + randomNo + dt;

		return token;
	}

	@Override
	public List<Support> getSupportList() {
		try {
			List<Support> supports = supportDao.getSupportList(Constants.STATUS_DELETE);
			return supports;
		} catch (VedantaException e) {
			String msg = "Error while getting support list";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	/*
	 * @Override public Support updateSupport(Support support) { try { int result =
	 * supportDao.updateSupport(support.getStatus(),support.getId());
	 * System.out.println(result+"----- result"); return support; } catch
	 * (VedantaException e) { String msg = "Error while updating support";
	 * log.error(msg); throw new VedantaException(msg); } }
	 */

}
