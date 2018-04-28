package com.vedanta.vpmt.security;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LDAPAuthenticationProvider {

	private static final String LDAP_FILTER = "(&(objectClass=*) (sAMAccountName=%s))";

	@Value("${vedanta.adfs.u.key}")
	private String userName;
	
	@Value("${vedanta.adfs.p.key}")
	private String password;

	@Value("${ldap.attributes}")
	private String[] attributes;
	
	/*HZL LDAP Attributes*/
	
	@Value("${hzl.ldap.timeout}")
	private int hzlTimeout;

	@Value("${hzl.ldap.url}")
	private String hzlLdapURL;

	@Value("${hzl.ldap.context.factory}")
	private String hzlContextFactory;
	
	@Value("${hzl.ldap.security.authentication}")
	private String hzlAuthentication;

	@Value("${hzl.ldap.domain}")
	private String hzlDomain;
	
	@Value("${hzl.ldap.userLDAP}")
	private String hzlUserLDAP;
	
	@Value("${hzl.ldap.passwordLDAP}")
	private String hzlPasswordLDAP;

	@Value("${hzl.ldap.domain.content}")
	private String hzlDomainContent;
	
	/*sesa LDAP Attributes*/
	
	@Value("${sesa.ldap.timeout}")
	private int sesaTimeout; 

	@Value("${sesa.ldap.url}")
	private String sesaLdapURL;

	@Value("${sesa.ldap.context.factory}")
	private String sesaContextFactory;
	
	@Value("${sesa.ldap.security.authentication}")
	private String sesaAuthentication;

	@Value("${sesa.ldap.domain}")
	private String sesaDomain;
	
	@Value("${sesa.ldap.userLDAP}")
	private String sesaUserLDAP;
	
	@Value("${sesa.ldap.passwordLDAP}")
	private String sesaPasswordLDAP;

	@Value("${sesa.ldap.domain.content}")
	private String sesaDomainContent;
	
	/*sterlite LDAP Attributes*/
	
	@Value("${sterlite.ldap.timeout}")
	private int sterliteTimeout;  

	@Value("${sterlite.ldap.url}")
	private String sterliteLdapURL;

	@Value("${sterlite.ldap.context.factory}")
	private String sterliteContextFactory;
	
	@Value("${sterlite.ldap.security.authentication}")
	private String sterliteAuthentication;

	@Value("${sterlite.ldap.domain}")
	private String sterliteDomain;
	
	@Value("${sterlite.ldap.userLDAP}")
	private String sterliteUserLDAP;
	
	@Value("${sterlite.ldap.passwordLDAP}")
	private String sterlitePasswordLDAP;

	@Value("${sterlite.ldap.domain.content}")
	private String sterliteDomainContent;
	
	/*val LDAP Attributes*/
	
	@Value("${val.ldap.timeout}")
	private int valTimeout;
	
	@Value("${val.ldap.url}")
	private String valLdapURL;

	@Value("${val.ldap.context.factory}")
	private String valContextFactory;
	
	@Value("${val.ldap.security.authentication}")
	private String valAuthentication;

	@Value("${val.ldap.domain}")
	private String valDomain;
	
	@Value("${val.ldap.userLDAP}")
	private String valUserLDAP;
	
	@Value("${val.ldap.passwordLDAP}")
	private String valPasswordLDAP;

	@Value("${val.ldap.domain.content}")
	private String valDomainContent;
	
	/*Jharsugda LDAP Attributes*/
	
	@Value("${jharsuguda.ldap.timeout}")
	private int jharsugudaTimeout;
	
	@Value("${jharsuguda.ldap.url}")
	private String jharsugudaLdapURL;

	@Value("${jharsuguda.ldap.context.factory}")
	private String jharsugudaContextFactory;
	
	@Value("${jharsuguda.ldap.security.authentication}")
	private String jharsugudaAuthentication;

	@Value("${jharsuguda.ldap.domain}")
	private String jharsugudaDomain;
	
	@Value("${jharsuguda.ldap.userLDAP}")
	private String jharsugudaUserLDAP;
	
	@Value("${jharsuguda.ldap.passwordLDAP}")
	private String jharsugudaPasswordLDAP;

	@Value("${jharsuguda.ldap.domain.content}")
	private String jharsugudaDomainContent;
	
	/*cairn LDAP Attributes*/
	
	@Value("${cairn.ldap.timeout}")
	private int cairnTimeout;
	
	@Value("${cairn.ldap.url}")
	private String cairnLdapURL;

	@Value("${cairn.ldap.context.factory}")
	private String cairnContextFactory;
	
	@Value("${cairn.ldap.security.authentication}")
	private String cairnAuthentication;

	@Value("${cairn.ldap.domain}")
	private String cairnDomain;
	
	@Value("${cairn.ldap.userLDAP}")
	private String cairnUserLDAP;
	
	@Value("${cairn.ldap.passwordLDAP}")
	private String cairnPasswordLDAP;

	@Value("${cairn.ldap.domain.content}")
	private String cairnDomainContent;
	
	/*balco LDAP Attributes*/
	
	@Value("${balco.ldap.timeout}")
	private int balcoTimeout;
	
	@Value("${balco.ldap.url}")
	private String balcoLdapURL;

	@Value("${balco.ldap.context.factory}")
	private String balcoContextFactory;
	
	@Value("${balco.ldap.security.authentication}")
	private String balcoAuthentication;

	@Value("${balco.ldap.domain}")
	private String balcoDomain;
	
	@Value("${balco.ldap.userLDAP}")
	private String balcoUserLDAP;
	
	@Value("${balco.ldap.passwordLDAP}")
	private String balcoPasswordLDAP;

	@Value("${balco.ldap.domain.content}")
	private String balcoDomainContent;

	public JSONObject searchUser(String userName, String password, Long businessUnitId) {
		DirContext context = null;
		JSONObject response = new JSONObject();
		final Hashtable<String, String> envConstants = this.getLdapCredential(businessUnitId);
		if(ObjectUtils.isEmpty(envConstants)) {
			   throw new BadCredentialsException("LDAP credential not found.");
			  }
		try {
			
			context = getContext(userName, password, businessUnitId);
			final NamingEnumeration<SearchResult> answer = context.search(envConstants.get("domainContent"),
					String.format(LDAP_FILTER, userName), searchControles(businessUnitId));
			if (answer.hasMoreElements()) {
				while (answer.hasMoreElements()) {
					final SearchResult result = answer.nextElement();
					final Hashtable<String, String> env = new Hashtable<>(6);
					env.put(Context.INITIAL_CONTEXT_FACTORY, envConstants.get(Context.INITIAL_CONTEXT_FACTORY));
					env.put(Context.PROVIDER_URL, envConstants.get(Context.PROVIDER_URL));
					env.put(Context.SECURITY_AUTHENTICATION, envConstants.get(Context.SECURITY_AUTHENTICATION));
					env.put(Context.SECURITY_PRINCIPAL, result.getNameInNamespace());
					env.put(Context.SECURITY_CREDENTIALS, password);
					env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(envConstants.get("com.sun.jndi.ldap.connect.timeout")));
					new InitialDirContext(env);
					final NamingEnumeration<String> attrs = result.getAttributes().getIDs();
					String value = null;
					while (attrs.hasMoreElements()) {
						final String id = attrs.nextElement();
						value = (result.getAttributes().get(id) != null) ? result.getAttributes().get(id).toString()
								: StringUtils.EMPTY;
						if (StringUtils.isNotBlank(value)) {
							response.put(id, removeKey(value));
						}
					}
				}
			}
		} catch (Exception ex) {
			log.error("Error while connecting LDAP server " + envConstants.get(Context.PROVIDER_URL).trim() + " for user " + userName, ex);
			throw new UsernameNotFoundException(
					"Error while connecting LDAP server " + envConstants.get(Context.PROVIDER_URL).trim() + " for user " + userName, ex);
		} finally {
			if (context != null)
				try {
					context.close();
				} catch (NamingException ne) {
					log.error("Error while closing the connection", ne);
				}

		}
		return response;
	}

	private String removeKey(String value) {
		return value.substring(value.indexOf(":") + 1).trim();
	}

	private SearchControls searchControles(Long businessUnitId) {
		final Hashtable<String, String> envConstants = this.getLdapCredential(businessUnitId);
		final SearchControls searchControls = new SearchControls();
		searchControls.setDerefLinkFlag(true);
		searchControls.setTimeLimit(Integer.parseInt(envConstants.get("com.sun.jndi.ldap.connect.timeout")));
		searchControls.setReturningAttributes(attributes);
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		return searchControls;
	}

	private DirContext getContext(String userId, String password, Long businessUnitId) throws NamingException {
		
		final Hashtable<String, String> envConstants = this.getLdapCredential(businessUnitId);
		
		final Hashtable<String, String> env = new Hashtable<>(6);
		env.put(Context.INITIAL_CONTEXT_FACTORY, envConstants.get(Context.INITIAL_CONTEXT_FACTORY));
		env.put(Context.PROVIDER_URL, envConstants.get(Context.PROVIDER_URL));
		env.put(Context.SECURITY_AUTHENTICATION, envConstants.get(Context.SECURITY_AUTHENTICATION));
		env.put(Context.SECURITY_PRINCIPAL, userId + "@" + envConstants.get("domain"));
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(envConstants.get("com.sun.jndi.ldap.connect.timeout")));
		return new InitialDirContext(env);
	}

	// fetching all user form LDAP

	public List<JSONObject> getAllLDAPUser(Long businessUnitId) {
	    List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
	    JSONObject response = null;
		DirContext context = null;
		final Hashtable<String, String> envConstants = this.getLdapCredential(businessUnitId);
		try {
			
			context = getContext(envConstants.get("userLDAP"), envConstants.get(Context.SECURITY_CREDENTIALS), businessUnitId);
			 NamingEnumeration<SearchResult> answer = context.search(envConstants.get("domainContent"),String.format("(&(objectClass=user) (sAMAccountName=%s))", "*"), searchControles(businessUnitId));	
			if (answer.hasMoreElements()) {
			
				while (answer.hasMoreElements()) {
					response = new JSONObject();
					final SearchResult result = answer.nextElement();
					final Hashtable<String, String> env = new Hashtable<>(7);
					env.put(Context.INITIAL_CONTEXT_FACTORY, envConstants.get(Context.INITIAL_CONTEXT_FACTORY));
					env.put(Context.PROVIDER_URL, envConstants.get(Context.PROVIDER_URL));
					env.put(Context.SECURITY_AUTHENTICATION, envConstants.get(Context.SECURITY_AUTHENTICATION));
					env.put(Context.SECURITY_PRINCIPAL, envConstants.get(Context.SECURITY_PRINCIPAL));
					env.put(Context.SECURITY_CREDENTIALS, envConstants.get(Context.SECURITY_CREDENTIALS));
					env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(envConstants.get("com.sun.jndi.ldap.connect.timeout")));
					env.put(Context.REFERRAL, "follow");
					new InitialDirContext(env);
					final NamingEnumeration<String> attrs = result.getAttributes().getIDs();
					String value = null;
					while (attrs.hasMoreElements()) {
						final String id = attrs.nextElement();
						value = (result.getAttributes().get(id) != null) ? result.getAttributes().get(id).toString()
								: StringUtils.EMPTY;
						if (StringUtils.isNotBlank(value)) {
							response.put(id, removeKey(value));
							
						}
					}	
				
					 jsonObjects.add(response);
				}
			}

			return jsonObjects;

		} catch (Exception ex) {
			log.error("Error while connecting LDAP server " + envConstants.get(Context.PROVIDER_URL).trim() + " for user " + userName, ex);
			throw new UsernameNotFoundException(
					"Error while connecting LDAP server " + envConstants.get(Context.PROVIDER_URL).trim() + " for user " + userName, ex);
		} finally {
			if (context != null)
				try {
					context.close();
				} catch (NamingException ne) {
					log.error("Error while closing the connection", ne);
				}

			}
	     
	}
	
	private Hashtable<String, String> getLdapCredential(Long businessUnitId){
		final Hashtable<String, String> env = new Hashtable<>(6);
		
		if(!ObjectUtils.isEmpty(businessUnitId)){
			
			if(businessUnitId == Constants.HZL_BUSINESS_UNIT){
				env.put(Context.INITIAL_CONTEXT_FACTORY, hzlContextFactory);
				env.put(Context.PROVIDER_URL, hzlLdapURL);
				env.put(Context.SECURITY_AUTHENTICATION, hzlAuthentication);
				env.put(Context.SECURITY_CREDENTIALS, hzlPasswordLDAP);
				env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(hzlTimeout));
				env.put(Context.SECURITY_PRINCIPAL, hzlUserLDAP + "@" + hzlDomain);
				env.put("domain", hzlDomain);
				env.put("domainContent", hzlDomainContent);
				env.put("userLDAP", hzlUserLDAP);
				//(hzlLdapURL);
			}
			
			if(businessUnitId == Constants.SESA_BUSINESS_UNIT){
				env.put(Context.INITIAL_CONTEXT_FACTORY, sesaContextFactory);
				env.put(Context.PROVIDER_URL, sesaLdapURL);
				env.put(Context.SECURITY_AUTHENTICATION, sesaAuthentication);
				env.put(Context.SECURITY_CREDENTIALS, sesaPasswordLDAP);
				env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(sesaTimeout));
				env.put(Context.SECURITY_PRINCIPAL, sesaUserLDAP + "@" + sesaDomain);
				env.put("domain", sesaDomain);
				env.put("domainContent", sesaDomainContent);
				env.put("userLDAP", sesaUserLDAP);
				//(sesaLdapURL);
			}
			
			if(businessUnitId == Constants.STERLITE_BUSINESS_UNIT){
				env.put(Context.INITIAL_CONTEXT_FACTORY, sterliteContextFactory);
				env.put(Context.PROVIDER_URL, sterliteLdapURL);
				env.put(Context.SECURITY_AUTHENTICATION, sterliteAuthentication);
				env.put(Context.SECURITY_CREDENTIALS, sterlitePasswordLDAP);
				env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(sterliteTimeout));
				env.put(Context.SECURITY_PRINCIPAL, sterliteUserLDAP + "@" + sterliteDomain);
				env.put("domain", sterliteDomain);
				env.put("domainContent", sterliteDomainContent);
				env.put("userLDAP", sterliteUserLDAP);
				//(sterliteLdapURL);
			}
			
			if(businessUnitId == Constants.JHARSUGUDA_BUSINESS_UNIT){
				env.put(Context.INITIAL_CONTEXT_FACTORY, jharsugudaContextFactory);
				env.put(Context.PROVIDER_URL, jharsugudaLdapURL);
				env.put(Context.SECURITY_AUTHENTICATION, jharsugudaAuthentication);
				env.put(Context.SECURITY_CREDENTIALS,jharsugudaPasswordLDAP);
				env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(jharsugudaTimeout));
				env.put(Context.SECURITY_PRINCIPAL,jharsugudaUserLDAP + "@" + jharsugudaDomain);
				env.put("domain", jharsugudaDomain);
				env.put("domainContent", jharsugudaDomainContent);
				env.put("userLDAP", jharsugudaUserLDAP);
				//(jharsugudaLdapURL);
			}
			
			if(businessUnitId == Constants.CAIRN_BUSINESS_UNIT){
				env.put(Context.INITIAL_CONTEXT_FACTORY, cairnContextFactory);
				env.put(Context.PROVIDER_URL, cairnLdapURL);
				env.put(Context.SECURITY_AUTHENTICATION, cairnAuthentication);
				env.put(Context.SECURITY_CREDENTIALS, cairnPasswordLDAP);
				env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(cairnTimeout));
				env.put(Context.SECURITY_PRINCIPAL, cairnUserLDAP + "@" + cairnDomain);
				env.put("domain", cairnDomain);
				env.put("domainContent", cairnDomainContent);
				env.put("userLDAP", cairnUserLDAP);
				//(cairnLdapURL);
			}
			if(businessUnitId == Constants.BALCO_BUSINESS_UNIT){
				env.put(Context.INITIAL_CONTEXT_FACTORY, balcoContextFactory);
				env.put(Context.PROVIDER_URL, balcoLdapURL);
				env.put(Context.SECURITY_AUTHENTICATION, balcoAuthentication);
				env.put(Context.SECURITY_CREDENTIALS, balcoPasswordLDAP);
				env.put("com.sun.jndi.ldap.connect.timeout", String.valueOf(balcoTimeout));
				env.put(Context.SECURITY_PRINCIPAL, balcoUserLDAP + "@" + balcoDomain);
				env.put("domain", balcoDomain);
				env.put("domainContent", balcoDomainContent);
				env.put("userLDAP", balcoUserLDAP);
				//(balcoLdapURL);
			}
		}
		   
		return env;
	}

}
