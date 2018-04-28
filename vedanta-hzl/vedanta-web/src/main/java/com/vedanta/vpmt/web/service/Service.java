package com.vedanta.vpmt.web.service;

public interface Service<T> {

	public T get(long id);

	public T save(T entity);

	public T update(long id, T entity);

}