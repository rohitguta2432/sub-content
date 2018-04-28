package com.vedanta.vpmt.web.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.service.cache.CacheService;
import com.vedanta.vpmt.web.util.AuthorizationUtil;
import com.vedanta.vpmt.web.util.VedantaConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;

@Component("vedantaAuthenticationProvider")
public class VedantaAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	@Qualifier("memcacheService")
	private CacheService<String, Object> cacheService;

	@Autowired
	private AuthorizationUtil authorizationUtil;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = (authentication.getName()).substring(0, (authentication.getName()).indexOf("!"));
		String password = (String) authentication.getCredentials();
		String buId = (authentication.getName()).substring((authentication.getName()).indexOf("!") + 1);

		JsonNode responseData = authorizationUtil.generateAuthToken(username, password, buId);

		if (responseData == null) {
			throw new VedantaWebException("Internal server error, please contact administrator.");
		}
		int statusCode = Integer.parseInt(responseData.get(VedantaConstant.STATUS_CODE).toString());

		if (statusCode == 401 || statusCode == 900 || statusCode == 423 || statusCode == 500) {
			if (statusCode == 423) {
				throw new BadCredentialsException("Already Logged In");
			}
			throw new BadCredentialsException("Invalid credentials.");
		}

		JsonNode dataNode = responseData.get(VedantaConstant.DATA);
		JsonNode userNode = dataNode.get(Constants.USER);
		JsonNode authoritiesNode = userNode.get(Constants.AUTHORITIES);

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute(Constants.USER, username);
		session.setAttribute(Constants.USER_BU_ID, buId);
		session.setAttribute(VedantaConstant.AUTH_HEADER, dataNode.get(Constants.AUTH_TOKEN).asText());

		/*
		 * cacheService.put(VedantaConstant.AUTH_HEADER,
		 * dataNode.get(Constants.AUTH_TOKEN).asText());
		 * cacheService.put(Constants.USER, username);
		 * cacheService.put(Constants.USER_BU_ID, buId);
		 */

		List<GrantedAuthority> authorities = new ArrayList<>();

		authoritiesNode.forEach(authority -> {
			authorities.add(new SimpleGrantedAuthority(authority.get(Constants.AUTHORITY).asText()));
		});
		return new UsernamePasswordAuthenticationToken(username, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
