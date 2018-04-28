package com.vedanta.vpmt.service;

import com.vedanta.vpmt.mapper.ApproverUpdateMapper;
import com.vedanta.vpmt.mapper.ScoreCardAssign;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardData;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.ScorecardAggregation;
import com.vedanta.vpmt.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

/**
 * Created by manishsanger on 24/09/17.
 */
public interface ScoreCardService extends VedantaService<ScoreCard> {

	public Map<String, Object> getScoreCardBaseData();

	public Map<String, Object> getScoreCardTemplateMapByScoreCardId(Long scoreCardId);

	public List<ScoreCard> getScoreCardByContractNumberAndStatus(String contractNumber, int status);

	public List<ScoreCard> getUserScoreCardsByStatus(int status);

	public List<ScoreCard> getUserScoreCardsByStatusAndBusinessUnitIdAndPlantCode(int status, Long businessUnitId,
			String plantCode);

	// for admin, business Unit head and plant head
	public List<ScoreCard> getUserScoreCardsByStatusAndBusinessUnitId(int status, Long businessUnitId);

	public Map<String, Object> getScoreCardTemplateMapBySubCategoryId(Long subCategoryId);

	public ScoreCardGroupUser saveScoreCardGroupUser(ScoreCardGroupUser scoreCardGroupUser);

	public List<ScoreCard> getAllScorecard();

	public ScoreCard populate(ScoreCard scoreCard);

	public List<ScoreCard> getUserScoreCardsByDateRange(String fromDate, String toDate, Integer status);

	public List<ScoreCard> getUserScoreCardsByDateRangeAndBusinessUnitIdAndPlantCode(String fromDate, String toDate,
			Integer status, Long businessUnitId, String plantCode);

	// for admin, business unit head and plant head
	public List<ScoreCard> getUserScoreCardsByDateRangeAndBusinessUnitId(String fromDate, String toDate, Integer status,
			Long businessUnitId);

	public Map<String, Object> getDashboardData();

	Map<String, Object> getDashboardData(Long searchPlantId, Long searchCategoryId, Long searchSubCategoryId,
			Long searchContractId, String fromDate, String toDate);

	ScoreCardAssign scoreCardAssign(ScoreCardAssign scoreCardAssign);

	ScoreCardGroupUser getScoreCardGroupUserById(Long scoreCardGroupUserId);

	public Set<ScorecardAggregation> getScorecardBySubcategoryId(Long subCategoryId);

	public List<User> getAllUserNotAssigned(Long scoreCardId);

	public List<User> getAllUserNotAssignedByName(Long scoreCardId, String name);

	public List<User> getAllUserNotAdminByLimit();

	public List<User> getAllUserByName(String name);

	List<ScoreCardData> getScoreCardDataFromScoreCard(ScoreCard scoreCard);

	Map<String, Object> getScoreCardTemplateMapByScoreCardIdOnFormAggrigation(Long scoreCardId);

	void firstScorecardEntry(ScoreCard scoreCard);

	List<ScoreCard> getAllScoreCardByTemplateIdAndMonthIdAndYearIdAndStatus(Long templateId, int monthId, int yearId,
			int status, Long businessId);

	public List<ScoreCard> getScorecardByContractNumber(String contractNumber);

	List<User> getAllUserNotAdminByLimitAndBuId(Long buId);

	List<User> getAllUserByNameAndBuId(String name, Long buId);

	public ScoreCard updateScorecard(ScoreCard scorecard);

	public List<ScoreCard> getAllScorecardByRoleBased();

	public List<ScoreCard> getUserScoreCardsByStatusAndBusinessUnitId(Long businessUnitId, int status);

	public ScoreCard getScorecardByContractNumberAndMonthIdAndYearId(Long businessUnitId, int monthId, int yearId,
			String contractNumber);
	
	List<User> getAllUserNotAdminByLimitAndBuIdAndNotApprover(Long buId,Long scorecardId);
	List<User> getAllUserByNameAndBuIdAndNotApprover(String name, Long buId,Long scorecardId);

	void updateScoreCardForApprover(List<ApproverUpdateMapper> approverUpdateMapper);

}
