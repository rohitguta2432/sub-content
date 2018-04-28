package com.vedanta.vpmt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.AuthenticationTokenException;
import com.vedanta.vpmt.model.AuthenticationToken;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.service.AuthenticationTokenService;
import com.vedanta.vpmt.service.UserDetailServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

  private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Value("${scw.token.header}")
  private String tokenHeader;

  @Value("${vedanta.token.inactivity.time}")
  private int inactivityTime;

  @Autowired
  private TokenUtils tokenUtils;

  @Autowired
  @Qualifier("userDetailServiceImpl")
  private UserDetailsService userDetailsService;
  
  @Autowired
  private UserDetailServiceImpl userDetailsServiceImpl;

  @Autowired
  private AuthenticationTokenService authenticationTokenService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String authToken = httpRequest.getHeader(this.tokenHeader);
    boolean isAdminRequest = httpRequest.getHeader(Constants.ADMIN_REQUEST) == null ? false : true;

    if (!StringUtils.isEmpty(authToken)
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        String username = this.tokenUtils.getUsernameFromToken(authToken);
        String businessUnitId=this.tokenUtils.getBusinessUnitIdFromToken(authToken);
        
        VedantaUser userDetails = (VedantaUser) userDetailsServiceImpl.loadUserByUsernameAndBuId(username, Long.parseLong(businessUnitId));
        	
        if (isAdminRequest) {
          validateTokenForInactivity(authToken, userDetails);
        }
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception e) {
        log.error(e.getMessage());
        writeErrorToResponse(e, response);
        return;
      }
    }
    chain.doFilter(request, response);
  }

  private void validateTokenForInactivity(String authToken, VedantaUser userDetails) {

    AuthenticationToken existingAuthenticationToken =
        authenticationTokenService.getAuthTokenByUserId(userDetails.getId());

    if (existingAuthenticationToken == null) {
    	log.info("No auth token found in DB.");
    	throw new AuthenticationTokenException("No auth token found in DB.");
    }

    long timeDIff =
        System.currentTimeMillis() - existingAuthenticationToken.getLastAccessed().getTime();

    if (TimeUnit.MILLISECONDS.toMinutes(timeDIff) >= inactivityTime) {
    	log.info("Token expired for user : ", userDetails.getId(), " because of inactivity.");
    	authenticationTokenService.deleteAuthenticationToken(userDetails.getId());
    	throw new AuthenticationTokenException(
    			"User auth token expired because of inactivity on APP, please generate a new auth token");
    } else {
    	authenticationTokenService.updateLastAccessedTime(existingAuthenticationToken.getId());
    }
  }

  private void writeErrorToResponse(Exception e, ServletResponse servletResponse)
      throws IOException {

    int statusCode = 401;

    if (e instanceof AuthenticationTokenException) {
//      statusCode = 900;
      statusCode = 401;
    }
    Response<String> response = new Response<String>(statusCode, e.getMessage(), null);

    try (PrintWriter writer = servletResponse.getWriter()) {
      writer.write(OBJECT_MAPPER.writeValueAsString(response));
      writer.flush();
    }

  }
}
