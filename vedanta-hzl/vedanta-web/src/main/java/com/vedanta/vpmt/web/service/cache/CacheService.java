package com.vedanta.vpmt.web.service.cache;

public interface CacheService<String, T> {

	public T get(String key);

	public void put(String key, T data);

	public void remove(String key);
}
