package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.FormGroupUser;
import lombok.NonNull;

import java.util.List;

import javax.validation.constraints.NotNull;

public interface FormGroupUserService extends VedantaService<FormGroupUser> {

	List<FormGroupUser> save(String poItem, String contractNumber, List<FormGroupUser> formGroupUsers);

	List<FormGroupUser> getFormGroupUsersForContract(@NonNull String contractNumber, @NonNull long formId,
			@NonNull long groupId, @NotNull long businessUnitId);
}
