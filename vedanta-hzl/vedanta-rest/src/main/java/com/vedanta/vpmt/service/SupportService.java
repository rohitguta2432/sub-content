package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.Support;

public interface SupportService extends VedantaService<Support> {

	public List<Support> getSupportList();

	// public Support updateSupport(Support support);

}
