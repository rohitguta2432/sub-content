package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.AuthenticationToken;

public interface AuthenticationTokenService extends VedantaService<AuthenticationToken> {

	public AuthenticationToken getAuthTokenByUserId(long userId);

	public void deleteAuthenticationToken(long userId);

	public void updateLastAccessedTime(long userId);

	public void deleteExpiredAuthToken();

	boolean isExpired(AuthenticationToken authenticationToken);

	public List<AuthenticationToken> getAllAuthToken();

	public void truncateAuthTokenUser();

}
