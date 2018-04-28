package com.vedanta.vpmt.web.service.cache;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.transcoders.SerializingTranscoder;

@Component("memcacheService")
@Slf4j
public class MemcacheServiceImpl implements CacheService<String, Object> {

	private final MemcachedClient cacheClient;

	private final int CACHE_ENTRY_EXPIRATION_TIME = 3600;

	@Autowired
	public MemcacheServiceImpl(@Value("${cache.server.address}") String cacheAddress) throws IOException {
		this.cacheClient = new MemcachedClient(AddrUtil.getAddresses(cacheAddress));
		final SerializingTranscoder stc = (SerializingTranscoder) cacheClient.getTranscoder();
		stc.setCompressionThreshold(600000000);
	}

	@Override
	public Object get(String key) {
		if (StringUtils.isEmpty(key)) {
			log.info("Key cannot be null/empty.");
			throw new IllegalArgumentException("Key cannot be null/empty.");
		}
		return cacheClient.get(key);
	}

	@Override
	public void put(String key, Object data) {

		if (StringUtils.isEmpty(key)) {
			log.info("Key cannot be null/empty.");
			throw new IllegalArgumentException("Key cannot be null/empty.");
		}

		if (ObjectUtils.isEmpty(data)) {
			log.info("Object to be stored cannot be null/empty.");
			throw new IllegalArgumentException("Object to be stored cannot be null/empty.");
		}

		if (get(key) != null) {
			log.info(String.format("Item found with key : %s, hence removing it.", key));
			remove(key);
		}

		cacheClient.add(key, CACHE_ENTRY_EXPIRATION_TIME, data);
	}

	@Override
	public void remove(String key) {

		if (StringUtils.isEmpty(key)) {
			log.info("Key cannot be null/empty.");
			throw new IllegalArgumentException("Key cannot be null/empty.");
		}
		cacheClient.delete(key);
	}
}
