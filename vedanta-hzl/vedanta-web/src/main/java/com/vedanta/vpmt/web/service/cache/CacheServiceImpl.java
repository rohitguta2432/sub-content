package com.vedanta.vpmt.web.service.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service("cacheService")
@Slf4j
public class CacheServiceImpl implements CacheService<String, Object> {

	private static Map<String, Object> CACHCE = new HashMap<>();

	private final static Lock writeLock = new ReentrantLock();

	@Override
	public Object get(final String key) {

		if (StringUtils.isEmpty(key)) {
			log.info("Key cannot be null/empty.");
			throw new IllegalArgumentException("Key cannot be null/empty.");
		}
		return CACHCE.get(key);
	}

	@Override
	public void put(final String key, final Object data) {
		if (StringUtils.isEmpty(key)) {
			log.info("Key cannot be null/empty.");
			throw new IllegalArgumentException("Key cannot be null/empty.");
		}

		if (ObjectUtils.isEmpty(data)) {
			log.info("Object to be stored cannot be null/empty.");
			throw new IllegalArgumentException("Object to be stored cannot be null/empty.");
		}

		writeLock.lock();
		if (CACHCE.get(key) != null) {
			CACHCE.remove(key);
		}
		CACHCE.put(key, data);
		writeLock.unlock();

	}

	@Override
	public void remove(final String key) {

		if (StringUtils.isEmpty(key)) {
			log.info("Key cannot be null/empty.");
			throw new IllegalArgumentException("Key cannot be null/empty.");
		}
		writeLock.lock();
		CACHCE.remove(key);
		writeLock.unlock();
	}
}
