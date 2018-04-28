package com.vedanta.vpmt.service;

import java.util.List;

import com.vedanta.vpmt.model.Group;

public interface GroupService extends VedantaService<Group> {

	public List<Group> getAllGroups();
}
