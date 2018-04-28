package com.vedanta.vpmt.service;

public interface VedantaService<T> {

	public T get(long id);

	public T save(T entity);

	public T update(long id, T entity);
}
