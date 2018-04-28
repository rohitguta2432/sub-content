package com.vedanta.vpmt.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.exception.VedantaException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ADFSAuthenticationUtil {

	@Value("${vedanta.adfs.url}")
	private String ADFS_URL;

	@Value("${vedanta.adfs.user.prefix}")
	private String USER_PREFIX;

	@Value("${vedanta.adfs.u.key}")
	private String USER_NAME;

	@Value("${vedanta.adfs.p.key}")
	private String PASSWORD;

	@Value("${vedanta.adfs.relying_party}")
	private String RELYING_PARTY;

	@Value("${vedanta.adfs.relying_party.value}")
	private String RELYING_PARTY_VALUE;

	@Value("${vedanta.adfs.sign_in_other_site}")
	private String SIGN_IN_OTHER_SITE;

	@Value("${vedanta.adfs.sign_in_submit}")
	private String SIGN_IN_SUBMIT;

	@Value("${vedanta.adfs.sign_in_submit.value}")
	private String SIGN_IN_SUBMIT_VALUE;

	@Value("${vedanta.adfs.single_sign_out}")
	private String SINGLE_SIGN_OUT;

	@Value("${vedanta.adfs.auth.method}")
	private String AUTH_METHOD;

	@Value("${vedanta.adfs.auth.method.value}")
	private String AUTH_METHOD_VALUE;

	@Value("${vedanta.adfs.path}")
	private String PATH;

	@Value("${vedanta.adfs.domain}")
	private String DOMAIN;

	@Value("${vedanta.adfs.key.set_cookie}")
	private String KEY_SET_COOKIE;

	@Value("${vedanta.adfs.MSIS.saml.request}")
	private String MSISSAMLREQUEST;

	@Value("${vedanta.adfs.MSIS.auth}")
	private String MSISSAUTH;

	@Autowired
	private SSLContextUtil sslContextUtil;

	public String authenticate(String userName, String password) {

		if (StringUtils.isEmpty(userName)) {
			log.info("User name cannot be null/empty");
			throw new VedantaException("User name cannot be null/empty");
		}

		if (StringUtils.isEmpty(password)) {
			log.info("Password cannot be null/empty");
			throw new VedantaException("Password cannot be null/empty");
		}

		HttpResponse response = makeRequest(getFormParams(userName, password), null);
		Header[] headers = response.getHeaders(KEY_SET_COOKIE);
		CookieStore cookieStore = getCookieStore(headers, userName);
		isUserValidated(cookieStore);
		HttpResponse httpResponse = get(headers, cookieStore);

		try {
			String responseAsString = EntityUtils.toString(httpResponse.getEntity());
			Document parseHTML = parseHTML(responseAsString);
			String samlResponse = null;
			for (Element input : parseHTML.select("input")) {
				if (input.attr("name").equalsIgnoreCase("SAMLResponse")) {
					samlResponse = input.attr("value");
					break;
				}
			}
			return samlResponse;
		} catch (ParseException | IOException e) {
			log.error("Error while generating auth token for user");
			throw new VedantaException("Error while generating auth token for user");
		}
	}

	private HttpResponse get(Header[] headers, CookieStore cookieStore) {

		SSLContext sslcontext = sslContextUtil.getSSLContext(Constants.KEY_STORE_NAME_ADFS);
		SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext);
		try {
			CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore)
					.setSSLSocketFactory(factory).build();
			HttpGet httpGet = new HttpGet(ADFS_URL);
			return httpClient.execute(httpGet);
		} catch (VedantaException | IOException e) {
			log.error("Error while fetching saml response");
			throw new VedantaException("Error while fetching saml response");
		}
	}

	private void isUserValidated(CookieStore cookieStore) {
		boolean isUserValid = false;
		boolean isUserAuthenticated = false;
		for (Cookie cookie : cookieStore.getCookies()) {
			if (MSISSAMLREQUEST.equals(cookie.getName())) {
				isUserValid = true;
			} else if (MSISSAUTH.equals(cookie.getName())) {
				isUserAuthenticated = true;
			}
		}

		if (!isUserValid || !isUserAuthenticated) {
			log.error("Invalid user credentials.");
			throw new BadCredentialsException("Invalid user credentials.");
		}
	}

	private CookieStore getCookieStore(Header[] headers, String userName) {
		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = null;
		String cookieValue;
		for (Header hd : headers) {
			if (hd.getValue().contains(MSISSAMLREQUEST)) {
				cookieValue = hd.getValue().split(";")[0];
				if (StringUtils.isEmpty(cookieValue)) {
					log.error("Some error occurred while logging user");
					throw new VedantaException("Some error occurred while logging user");
				}
				cookie = new BasicClientCookie(MSISSAMLREQUEST, cookieValue.replace("MSISSamlRequest=", ""));
			} else if (hd.getValue().contains(MSISSAUTH)) {
				cookieValue = hd.getValue().split(";")[0];
				if (StringUtils.isEmpty(cookieValue)) {
					log.error("Bad credentials for user");
					throw new BadCredentialsException("Bad credentials for user");
				}
				cookie = new BasicClientCookie(MSISSAUTH, cookieValue.replace("MSISAuth=", ""));
			}
			if (PATH != null) {
				cookie.setPath(PATH);
			}
			if (DOMAIN != null) {
				cookie.setDomain(DOMAIN);
			}

			cookie.setSecure(Boolean.TRUE);
			cookieStore.addCookie(cookie);
		}
		return cookieStore;
	}

	private HttpResponse makeRequest(List<NameValuePair> params, Header header) {

		SSLContext sslcontext = sslContextUtil.getSSLContext(Constants.KEY_STORE_NAME_ADFS);
		SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext);
		CloseableHttpClient httpClient = null;

		try {
			HttpPost httpPost = new HttpPost(ADFS_URL);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response;
			httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();
			response = httpClient.execute(httpPost);

			if (response == null) {
				log.error(String.format("No response obtained for url request : %s ", ADFS_URL));
				throw new VedantaException(String.format("No response obtained for url request : %s ", ADFS_URL));
			}
			return response;
		} catch (IOException e) {
			log.error("Error while obtaining response from url : %s", ADFS_URL);
			throw new VedantaException(String.format("Error while obtaining response from url : %s", ADFS_URL));
		}
	}

	private List<NameValuePair> getFormParams(String userName, String password) {
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair(AUTH_METHOD, AUTH_METHOD_VALUE));
		formparams.add(new BasicNameValuePair(PASSWORD, password));
		formparams.add(new BasicNameValuePair(USER_NAME, String.format(USER_PREFIX, userName)));
		formparams.add(new BasicNameValuePair(RELYING_PARTY, RELYING_PARTY_VALUE));
		formparams.add(new BasicNameValuePair(SIGN_IN_OTHER_SITE, SIGN_IN_OTHER_SITE));
		formparams.add(new BasicNameValuePair(SIGN_IN_SUBMIT, SIGN_IN_SUBMIT_VALUE));
		formparams.add(new BasicNameValuePair(SINGLE_SIGN_OUT, SINGLE_SIGN_OUT));
		return formparams;
	}

	private Document parseHTML(String html) {
		return Jsoup.parse(html);
	}

}
