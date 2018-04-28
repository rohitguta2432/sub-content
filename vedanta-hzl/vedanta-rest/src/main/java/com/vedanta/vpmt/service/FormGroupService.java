package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormGroup;

import java.util.List;
import java.util.Set;

public interface FormGroupService extends VedantaService<FormGroup> {

	public List<FormGroup> getAllFormGroup();

	public List<FormGroup> getAllFormGroupsByFormId(long formId);

	public Boolean deleteAllFormGroupsByFormId(long formId);

	public Boolean saveFormGroupList(long formId, List<FormGroup> formGroups);

	public List<FormGroup> getAllFormGroupsInGroupsId(Set<Long> formIds);
}
