package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.HumanResource;

public interface HumanResourceService extends VedantaService<HumanResource>{
	
	public HumanResource checkExistingHumanResource(HumanResource humanResource);
	
	public List<HumanResource> getAllHumanResource();

}
