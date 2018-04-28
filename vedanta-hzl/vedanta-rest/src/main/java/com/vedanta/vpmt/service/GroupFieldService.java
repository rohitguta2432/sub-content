package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.GroupField;

import java.util.List;

/**
 * Created by manishsanger on 11/09/17.
 */
public interface GroupFieldService extends VedantaService<GroupField> {
	public boolean saveGroupFieldList(Long groupId, List<GroupField> entity);

	public List<GroupField> getAllGroupFields();

	public List<GroupField> getAllGroupFieldsByGroupId(long groupId);

	public boolean deleteByGroup(long groupId);
}
