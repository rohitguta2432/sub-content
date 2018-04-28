package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.ScoreCardGroupUser;
import lombok.NonNull;

import java.util.List;

import javax.validation.constraints.NotNull;

public interface ScoreCardGroupUserService extends VedantaService<ScoreCardGroupUser> {

	List<ScoreCardGroupUser> save(String poItem, String contractNumber, List<ScoreCardGroupUser> scoreCardGroupUsers);

	List<ScoreCardGroupUser> getScoreCardGroupUsersForContract(@NonNull String contractNumber, @NonNull long templateId,
			@NonNull long groupId, @NotNull long businessUnitId);

	List<ScoreCardGroupUser> getScoreCardGroupUsersByEmployeeIdOrContractManagerIdAndAllGroups(String contractManagerId,
			String employeeId);

	List<ScoreCardGroupUser> getScoreCardGroupUsersByEmployeeIdAndContractNumberAndBusinessUnitIdAndTemplateIdAndAllGroups(
			String contractNumber, String employeeId, Long businessUnitId, Long templateId);
}
