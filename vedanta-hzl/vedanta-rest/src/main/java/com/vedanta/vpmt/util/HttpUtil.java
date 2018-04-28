package com.vedanta.vpmt.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.vedanta.vpmt.exception.VedantaException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpUtil {

	private static final String CHARSET_UTF8 = "UTF-8";

	public String get(String url, Map<String, String> headers, Map<String, Object> parameters,
			boolean isSelfSignedCertificateRequest)
			throws URISyntaxException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

		if (StringUtils.isEmpty(url)) {
			log.info("URL cannot be empty/null");
			throw new IllegalArgumentException("URL cannot be empty/null");
		}
		CloseableHttpClient httpClient = null;
		if (isSelfSignedCertificateRequest) {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(builder.build(),
					new NoopHostnameVerifier());
			httpClient = HttpClients.custom().setSSLSocketFactory(factory).build();
		} else {
			httpClient = HttpClientBuilder.create().build();
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (!CollectionUtils.isEmpty(parameters)) {
			parameters.forEach((paramName, paramValue) -> {
				params.add(new BasicNameValuePair(paramName, String.valueOf(paramValue)));
			});
		}

		URI uri = new URIBuilder(url).setCharset(Charset.forName(CHARSET_UTF8)).addParameters(params).build();

		HttpGet getRequest = new HttpGet(uri);
		if (!CollectionUtils.isEmpty(headers)) {
			headers.forEach((headerName, headerValue) -> {
				getRequest.setHeader(headerName, headerValue);
			});
		}

		HttpResponse response = null;
		try {
			response = httpClient.execute(getRequest);
			if (response == null) {
				log.error(String.format("No response obtained for url request : %s ", url));
				throw new VedantaException(String.format("No response obtained for url request : %s ", url));
			}

			String parsedResponse = parseHttpResponse(response, url);
			httpClient.close();
			return parsedResponse;
		} catch (IOException e) {
			log.error("Error while obtaining response from url : %s", url);
			throw new VedantaException(String.format("Error while obtaining response from url : %s", url));
		}
	}

	private String parseHttpResponse(HttpResponse response, String url) {
		try (BOMInputStream in = new BOMInputStream(response.getEntity().getContent());) {
			ByteOrderMark bom = in.getBOM();
			String charsetName = bom == null ? CHARSET_UTF8 : bom.getCharsetName();
			return IOUtils.toString(in, charsetName);
		} catch (UnsupportedOperationException | IOException e) {
			log.error("Error while parsing response for URL : {}", url);
			throw new VedantaException("Error while parsing response for URL : " + url, e);
		}
	}

}
