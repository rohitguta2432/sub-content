package com.vedanta.vpmt.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;

import com.vedanta.vpmt.model.Contract;
import com.vedanta.vpmt.model.EmailLog;
import com.vedanta.vpmt.model.Form;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.User;

public interface EmailLogService extends VedantaService<EmailLog> {

	@Query("SELECT sc FROM ScoreCard sc WHERE sc.status=?1")
	public List<ScoreCard> getAllScoreCard(int status);

	public User getUserDetailsbyUserName(String name);

	public EmailLog getEmailLogDetails(Long userId, int stageId);

	public List<User> getAllUsersNotAdmin();

	public List<EmailLog> getEmailLogDetailsByStageAndStatus(int statusId, int stageId);

	public Set<ScoreCardGroupUser> getScorecardgroupUserDetailsByContractNumber(String contractnumber);

	public List<Contract> getContractDetailsByPlantId(Long plantId);

	public List<Contract> getContractDetailsByVendorId(Long vendorId);

	public List<Form> getFormDetailsBycategoryIdAndSubcategoryId(Long categoryId, Long subcategoryId);
}
