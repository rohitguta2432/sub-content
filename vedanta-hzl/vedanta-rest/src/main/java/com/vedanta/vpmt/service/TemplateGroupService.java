package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.TemplateGroup;

import java.util.List;
import java.util.Set;

/**
 * Created by manishsanger on 11/09/17.
 */
public interface TemplateGroupService extends VedantaService<TemplateGroup> {
	public List<TemplateGroup> getAllTemplateGroup();

	public List<TemplateGroup> getAllTemplateGroupsByTemplateId(long templateId);

	public Boolean deleteAllTemplateGroupsByTemplateId(long templateId);

	public Boolean saveTemplateGroupList(long templateId, List<TemplateGroup> templateGroup);

	public List<TemplateGroup> getAllTemplateGroupsInGroupsId(Set<Long> groupIds);

	public List<TemplateGroup> getAllTemplateGroupsByBusinessUnit(Long buId);

}
