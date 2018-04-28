package com.vedanta.vpmt.security;

import com.vedanta.vpmt.model.VedantaUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

  @Value("${scw.token.secret}")
  private String secret;

  @Value("${vedanta.token.expiration}")
  private long expiration;

  public String getUsernameFromToken(String token) {
    String username = null;
    try {
      final Claims claims = this.getClaimsFromToken(token);
      if(!ObjectUtils.isEmpty(claims)){
    	  username = claims.getSubject();
      }
      
    } catch (Exception e) {
      throw new AccessDeniedException(String.format("Invalid or expired authorization token : %s",
          token));
    }
    return username;
  }
  
  public String getBusinessUnitIdFromToken(String token) {
	    String businessUnitId = null;
	    try {
	      final Claims claims = this.getClaimsFromToken(token);
	      if(!ObjectUtils.isEmpty(claims)){
	    	  businessUnitId = claims.getId();
	      }
	      
	    } catch (Exception e) {
	      throw new AccessDeniedException(String.format("Invalid or expired authorization token : %s",
	          token));
	    }
	    return businessUnitId;
	  }

  public Date getCreatedDateFromToken(String token) {
    Date created;
    try {
      final Claims claims = this.getClaimsFromToken(token);
      created = new Date((Long) claims.get("created"));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }

  public Date getExpirationDateFromToken(String token) {
    Date expiration;
    try {
      final Claims claims = this.getClaimsFromToken(token);
      expiration = claims.getExpiration();
    } catch (Exception e) {
      expiration = null;
    }
    return expiration;
  }

  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }

  private Date generateCurrentDate() {
    return new Date(System.currentTimeMillis());
  }

  private Date generateExpirationDate() {
    return new Date(System.currentTimeMillis() + this.expiration * 1000);
  }

  public boolean isTokenExpired(String token) {
    final Date expiration = this.getExpirationDateFromToken(token);
    return expiration.after(this.generateCurrentDate());
  }

  public String generateToken(VedantaUser userDetails) {
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put("sub", userDetails.getLoginId());
    claims.put("created", this.generateCurrentDate());
    claims.put("jti", userDetails.getBusinessUnitId());
    return this.generateToken(claims);
  }

  private String generateToken(Map<String, Object> claims) {
    return Jwts.builder().setClaims(claims).setExpiration(this.generateExpirationDate())
        .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, this.secret).compact();
  }
}
