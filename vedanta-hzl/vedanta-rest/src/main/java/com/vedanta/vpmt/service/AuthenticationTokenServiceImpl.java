package com.vedanta.vpmt.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.vedanta.vpmt.dao.AuthenticationTokenDao;
import com.vedanta.vpmt.model.AuthenticationToken;

import lombok.extern.slf4j.Slf4j;

@Service("authenticationTokenService")
@Slf4j
public class AuthenticationTokenServiceImpl implements AuthenticationTokenService {

	@Autowired
	private AuthenticationTokenDao authenticationTokenDao;

	@Value("${vedanta.token.expiration}")
	private long tokenExpirationTime;

	@Value("${vedanta.token.inactivity.time}")
	private int inactivityTime;

	@Override
	public AuthenticationToken get(long id) {
		return null;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AuthenticationToken save(AuthenticationToken authenticationToken) {

		if (authenticationToken == null) {
			log.info("Authentication object cannot be null/empty");
			throw new IllegalArgumentException("Authentication object cannot be null/empty");
		}

		if (authenticationToken.getUserId() <= 0) {
			log.info("Invalid user id.");
			throw new IllegalArgumentException("Invalid user id.");
		}

		if (StringUtils.isEmpty(authenticationToken.getAuthToken())) {
			log.info("Authentication token cannot be null/empty");
			throw new IllegalArgumentException("Authentication token cannot be null/empty");
		}

		AuthenticationToken existingToken = this.getAuthTokenByUserId(authenticationToken.getUserId());
		if (existingToken != null) {
			this.deleteAuthenticationToken(authenticationToken.getUserId());
		}

		return authenticationTokenDao.save(authenticationToken);
	}

	@Override
	public AuthenticationToken update(long id, AuthenticationToken authenticationToken) {
		return null;
	}

	@Override
	public AuthenticationToken getAuthTokenByUserId(long userId) {

		AuthenticationToken authenticationToken = null;
		if (userId <= 0) {
			log.info("Invalid user id.");
			throw new IllegalArgumentException("Invalid user id.");
		}
		List<AuthenticationToken> authToken = authenticationTokenDao.getAuthToken(userId);
		if (!ObjectUtils.isEmpty(authToken)) {
			authenticationToken = authToken.get(0);
		}

		return authenticationToken;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteAuthenticationToken(long userId) {

		if (userId <= 0) {
			log.info("Invalid user id.");
			throw new IllegalArgumentException("Invalid user id.");
		}
		authenticationTokenDao.deleteByUserId(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateLastAccessedTime(long id) {

		if (id <= 0) {
			log.info("Invalid auth token id.");
			throw new IllegalArgumentException("Invalid auth token id.");
		}
		authenticationTokenDao.updateLastAccessedTime(id, new Date());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteExpiredAuthToken() {
		long currentTime = System.currentTimeMillis();
		List<AuthenticationToken> existingTokens = authenticationTokenDao.findAll();
		existingTokens.forEach(token -> {
			if (hasExpired(token, currentTime)) {
				authenticationTokenDao.delete(token);
			}
		});
	}

	private boolean hasExpired(AuthenticationToken authenticationToken, long currentTime) {
		long expirationTime = authenticationToken.isAdminToken() ? tokenExpirationTime
				: TimeUnit.MINUTES.toSeconds(inactivityTime);
		long timeDIff = currentTime - authenticationToken.getLastAccessed().getTime();
		return TimeUnit.MILLISECONDS.toSeconds(timeDIff) >= expirationTime;
	}

	@Override
	public boolean isExpired(AuthenticationToken authenticationToken) {

		long currentTime = new Date().getTime();
		long expirationTime = authenticationToken.isAdminToken() ? tokenExpirationTime
				: TimeUnit.MINUTES.toSeconds(inactivityTime);
		long timeDIff = currentTime - authenticationToken.getLastAccessed().getTime();

		return TimeUnit.MILLISECONDS.toSeconds(timeDIff) >= expirationTime;
	}

	@Override
	public List<AuthenticationToken> getAllAuthToken() {
		return authenticationTokenDao.findAll();
	}

	@Override
	public void truncateAuthTokenUser() {
		authenticationTokenDao.truncateAuthTokenUser();
	}

}
