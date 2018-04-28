package com.vedanta.vpmt.contollers.client;

import com.google.gson.Gson;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dto.Response;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.mapper.ApproverUpdateMapper;
import com.vedanta.vpmt.mapper.ScoreCardAssign;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.ScorecardAggregation;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.service.MailSenderServices;
import com.vedanta.vpmt.service.ScoreCardService;
import com.vedanta.vpmt.service.SubCategoryService;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.mail.MessagingException;

/**
 * Created by manishsanger on 24/09/17.
 */
@RestController
@RequestMapping(value = "/scorecard", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ScoreCardController {

	@Autowired
	@Qualifier("scoreCardService")
	private ScoreCardService scoreCardService;

	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private MailSenderServices mailService;

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/{scoreCardId}", method = RequestMethod.GET)
	public ResponseEntity<Response<ScoreCard>> getScoreCardByScoreCardId(
			@PathVariable("scoreCardId") long scoreCardId) {
		ScoreCard scoreCard;
		try {
			scoreCard = scoreCardService.get(scoreCardId);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<ScoreCard>>(
				new Response<ScoreCard>(HttpStatus.OK.value(), "Scorecard fetched successfully.", scoreCard),
				HttpStatus.OK);
	}


	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER')")
	@RequestMapping(value = "update-approver", method = RequestMethod.POST)
	public ResponseEntity<Response<List<ApproverUpdateMapper>>> saveData(@RequestBody List<ApproverUpdateMapper> approverUpdateMapper) {
		
		scoreCardService.updateScoreCardForApprover(approverUpdateMapper);
		return new ResponseEntity<Response<List<ApproverUpdateMapper>>>(
				new Response<List<ApproverUpdateMapper>>(HttpStatus.OK.value(), "BusinessUnit saved successfully", approverUpdateMapper),
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD')")
	@RequestMapping(value = "/user/{scoreCardId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getUserScoreCardByScoreCardId(
			@PathVariable("scoreCardId") long scoreCardId) {
		Map<String, Object> scoreCardMap = new HashMap<>();
		try {
			scoreCardMap = scoreCardService.getScoreCardTemplateMapByScoreCardId(scoreCardId);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<Map<String, Object>>>(new Response<Map<String, Object>>(
				HttpStatus.OK.value(), "Scorecard fetched successfully.", scoreCardMap), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<ScoreCard>> saveScoreCard(@RequestBody ScoreCard scoreCard) throws MessagingException {
		ScoreCard savedScoreCard;
		try {
			savedScoreCard = scoreCardService.save(scoreCard);
			mailService.scoreCardEmailNotification(scoreCard);
		} catch (VedantaException e) {
			log.error("Error saving scorecard information");
			throw new VedantaException("Error saving scorecard information");
		}
		return new ResponseEntity<Response<ScoreCard>>(new Response<ScoreCard>(HttpStatus.OK.value(),
				"Scorecard field information saved successfully.", savedScoreCard), HttpStatus.OK);
	}

	// Save ScoreCard Group user
	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/group/user/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<ScoreCardGroupUser>> saveScoreCardGroupUser(
			@RequestBody ScoreCardGroupUser scoreCardGroupUser) {
		ScoreCardGroupUser savedScoreCardGroupUser;
		try {
			savedScoreCardGroupUser = scoreCardService.saveScoreCardGroupUser(scoreCardGroupUser);
		} catch (VedantaException e) {
			log.error("Error saving scorecard group user information");
			throw new VedantaException("Error saving scorecard group user information");
		}
		return new ResponseEntity<Response<ScoreCardGroupUser>>(
				new Response<ScoreCardGroupUser>(HttpStatus.OK.value(),
						"Scorecard group user information saved successfully.", savedScoreCardGroupUser),
				HttpStatus.OK);
	}

	/*
	 * Get All scorecards
	 */

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/contract/{contractNumber}/{status}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getScoreCardsByContractNumberAndStatus(
			@PathVariable("contractNumber") String contractNumber, @PathVariable("status") int status) {
		List<ScoreCard> scorecards = new ArrayList<>();
		try {
			scorecards = scoreCardService.getScoreCardByContractNumberAndStatus(contractNumber, status);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/get-user-scorecard/{status}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getUserScoreCardsByStatus(@PathVariable("status") int status) {
		List<ScoreCard> scorecards = new ArrayList<>();
		try {
			scorecards = scoreCardService.getUserScoreCardsByStatus(status);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/get-user-scorecard/{status}/{businessUnitId}/{plantCode}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getUserScoreCardsByStatusAndBusinessUnitIdAndPlantCode(
			@PathVariable("status") int status, @PathVariable("businessUnitId") Long businessUnitId,
			@PathVariable("plantCode") String plantCode) {
		List<ScoreCard> scorecards = new ArrayList<>();
		if (!VedantaUtility.isAuthorized(businessUnitId)) {
			return new ResponseEntity<Response<List<ScoreCard>>>(
					new Response<List<ScoreCard>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", scorecards),
					HttpStatus.FORBIDDEN);
		}
		try {

			if (plantCode.equals(String.valueOf(0))) {
				scorecards = scoreCardService.getUserScoreCardsByStatusAndBusinessUnitId(businessUnitId, status);
			} else {
				scorecards = scoreCardService.getUserScoreCardsByStatusAndBusinessUnitIdAndPlantCode(status,
						businessUnitId, plantCode);
			}

		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards),
				HttpStatus.OK);
	}

	// for admin, business Unit head and plant head
	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER','BUSINESS_UNIT_HEAD','PLANT_HEAD','HR')")
	@RequestMapping(value = "/get-user-scorecard/{status}/{businessUnitId}}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getUserScoreCardsByStatusAndBusinessUnitId(
			@PathVariable("status") int status, @PathVariable("businessUnitId") Long businessUnitId) {
		List<ScoreCard> scorecards = new ArrayList<>();
		if (!VedantaUtility.isAuthorized(businessUnitId)) {
			return new ResponseEntity<Response<List<ScoreCard>>>(
					new Response<List<ScoreCard>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", scorecards),
					HttpStatus.FORBIDDEN);
		}
		try {
			scorecards = scoreCardService.getUserScoreCardsByStatusAndBusinessUnitId(status, businessUnitId);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/get-user-scorecard/date-range", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getUserScoreCardsByDateRange(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "status", required = false) Integer status) {
		List<ScoreCard> scorecards = new ArrayList<>();
		try {
			scorecards = scoreCardService.getUserScoreCardsByDateRange(fromDate, toDate, status);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/get-user-scorecard/date-range-businessUnitId-plantCode", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getUserScoreCardsByDateRangeAndBusinessUnitIdAndPlantCode(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "businessUnitId", required = false) Long businessUnitId,
			@RequestParam(value = "plantCode", required = false) String plantCode) {
		List<ScoreCard> scorecards = new ArrayList<>();

		if (!VedantaUtility.isAuthorized(businessUnitId)) {
			return new ResponseEntity<Response<List<ScoreCard>>>(
					new Response<List<ScoreCard>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", scorecards),
					HttpStatus.FORBIDDEN);
		}
		try {
			if (status == 2) {
				status = Constants.ALL_SCORECARD;
			}
			scorecards = scoreCardService.getUserScoreCardsByDateRangeAndBusinessUnitIdAndPlantCode(fromDate, toDate,
					status, businessUnitId, plantCode);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards),
				HttpStatus.OK);
	}

	// for admin, business unit head and plant head
	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER','BUSINESS_UNIT_HEAD','PLANT_HEAD','HR')")
	@RequestMapping(value = "/get-user-scorecard/date-range-businessUnitId", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getUserScoreCardsByDateRangeAndBusinessUnitIdAndPlantCode(
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "businessUnitId", required = false) Long businessUnitId) {
		List<ScoreCard> scorecards = new ArrayList<>();

		if (!VedantaUtility.isAuthorized(businessUnitId)) {
			return new ResponseEntity<Response<List<ScoreCard>>>(
					new Response<List<ScoreCard>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", scorecards),
					HttpStatus.FORBIDDEN);
		}
		try {
			scorecards = scoreCardService.getUserScoreCardsByDateRangeAndBusinessUnitId(fromDate, toDate, status,
					businessUnitId);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/template/get/sub-category/{subCategoryId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getScoreCardByContractNumber(
			@PathVariable("subCategoryId") Long subCategoryId) {
		Map<String, Object> scoreCardTemplateMap = new HashMap<>();
		try {
			scoreCardTemplateMap = scoreCardService.getScoreCardTemplateMapBySubCategoryId(subCategoryId);
		} catch (VedantaException e) {
			log.error("Error fetching all fields by Sub category");
			throw new VedantaException("Error fetching all fields by sub category");
		}
		return new ResponseEntity<Response<Map<String, Object>>>(
				new Response<Map<String, Object>>(HttpStatus.OK.value(), "Scorecard Template fetched successfully.",
						scoreCardTemplateMap),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/template/get/base-data", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getScoreCardBaseData() {
		Map<String, Object> baseDataMap = new HashMap<>();
		try {
			baseDataMap = scoreCardService.getScoreCardBaseData();
		} catch (VedantaException e) {
			log.error("Error fetching score card base data");
			throw new VedantaException("Error fetching score card base data");
		}
		return new ResponseEntity<Response<Map<String, Object>>>(new Response<Map<String, Object>>(
				HttpStatus.OK.value(), "Scorecard base data fetched successfully.", baseDataMap), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "all-scorecard", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getAllUsers() {
		List<ScoreCard> scorecard = null;
		try {
			scorecard = scoreCardService.getAllScorecard();
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "User fetched successfully", scorecard),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD')")
	@RequestMapping(value = "dashboard-data", method = RequestMethod.GET)
	public ResponseEntity<Response<Map<String, Object>>> getScorseCardDashboardData(
			@RequestParam(value = "plantId", required = false) Long plantId,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "subCategoryId", required = false) Long subCategoryId,
			@RequestParam(value = "contractId", required = false) Long contractId,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate) {

		Map<String, Object> scorecardDashboardData = null;
		try {

			if (!ObjectUtils.isEmpty(plantId) || !ObjectUtils.isEmpty(categoryId) || !ObjectUtils.isEmpty(subCategoryId)
					|| !ObjectUtils.isEmpty(contractId))
				scorecardDashboardData = scoreCardService.getDashboardData(plantId, categoryId, subCategoryId,
						contractId, fromDate, toDate);
			else
				scorecardDashboardData = scoreCardService.getDashboardData();

		} catch (VedantaException e) {
			throw e;
		}

		return new ResponseEntity<Response<Map<String, Object>>>(
				new Response<Map<String, Object>>(HttpStatus.OK.value(),

						"Scorecard dashboard data fetched successfully", scorecardDashboardData),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/scorecard-assign", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<ScoreCardAssign>> saveScoreCardAssignment(
			@RequestBody ScoreCardAssign scoreCardAssign) {

		try {
			Gson g = new Gson();
			// (g.toJson(scoreCardAssign));
		} catch (VedantaException e) {
			log.error("Error saving scorecard information");
			throw new VedantaException("Error saving scorecard information");
		}
		return new ResponseEntity<Response<ScoreCardAssign>>(new Response<ScoreCardAssign>(HttpStatus.OK.value(),
				"ScoreCardAssign field information saved successfully.",
				scoreCardService.scoreCardAssign(scoreCardAssign)), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/get/scorecard-group-user/{scorecardGroupUserId}", method = RequestMethod.GET)
	public ResponseEntity<Response<ScoreCardGroupUser>> getScoreCardGroupUserById(
			@PathVariable("scorecardGroupUserId") Long scorecardGroupUserId) {

		return new ResponseEntity<Response<ScoreCardGroupUser>>(new Response<ScoreCardGroupUser>(HttpStatus.OK.value(),
				"Scorecard Group User fetched successfully by Id.",
				scoreCardService.getScoreCardGroupUserById(scorecardGroupUserId)), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER', 'DATA_UPLOAD','HR')")
	@RequestMapping(value = "/getScorecardBySubcategoryId/{subcategoryId}", method = RequestMethod.GET)
	public ResponseEntity<Response<Set<ScorecardAggregation>>> getScoreCardsBySubCategoryId(
			@PathVariable("subcategoryId") Long subcategoryId) {
		Set<ScorecardAggregation> scorecards = new HashSet<>();
		try {
			scorecards = scoreCardService.getScorecardBySubcategoryId(subcategoryId);
		} catch (VedantaException e) {
			log.error("Error fetching all fields");
			throw new VedantaException("Error fetching all fields");
		}
		return new ResponseEntity<Response<Set<ScorecardAggregation>>>(new Response<Set<ScorecardAggregation>>(
				HttpStatus.OK.value(), "Scorecards fetched successfully.", scorecards), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUserNotAssigned/{scoreCardId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserNotAssigned(@PathVariable("scoreCardId") Long scoreCardId) {

		List<User> user = null;
		try {
			user = scoreCardService.getAllUserNotAssigned(scoreCardId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUserNotAssignedByName/{scoreCardId}/{name}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserNotAssignedByName(
			@PathVariable("scoreCardId") Long scoreCardId, @PathVariable("name") String name) {

		List<User> user = null;
		try {
			user = scoreCardService.getAllUserNotAssignedByName(scoreCardId, name);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUserNotAdminByLimit/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserNotAdminByLimit(@PathVariable("buId") Long buId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = scoreCardService.getAllUserNotAdminByLimitAndBuId(buId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUserByName/{name}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserByName(@PathVariable("name") String name) {

		List<User> user = null;
		try {
			user = scoreCardService.getAllUserByName(name);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@RequestMapping(value = "getAllUserByName/{name}/bu-id/{buId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserByNameAndBuId(@PathVariable("name") String name,
			@PathVariable("buId") Long buId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = scoreCardService.getAllUserByNameAndBuId(name, buId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "allSubCategory", method = RequestMethod.GET)
	public ResponseEntity<Response<List<SubCategory>>> getAllPlants() {

		List<SubCategory> subCategory = new ArrayList<>();
		try {

			subCategory = subCategoryService.getAllCategories();
		} catch (VedantaException e) {
			log.error("Error fetching all subCategory information");
			throw new VedantaException("Error fetching all subCategory information");
		}
		return new ResponseEntity<Response<List<SubCategory>>>(
				new Response<List<SubCategory>>(HttpStatus.OK.value(), "SubCategory fetched successfully", subCategory),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER_MANAGER', 'DATA_UPLOAD', 'PLANT_HEAD', 'BUSINESS_UNIT_HEAD', 'CONTRACT_MANAGER','HR')")
	@RequestMapping(value = "getAllScorecardRoleBased", method = RequestMethod.GET)
	public ResponseEntity<Response<List<ScoreCard>>> getAllScorecardByRoleBased() {

		List<ScoreCard> scorecard = new ArrayList<>();
		try {
			scorecard = scoreCardService.getAllScorecardByRoleBased();

		} catch (VedantaException e) {
			log.error("Error fetching all scorecard information");
			throw new VedantaException("Error fetching all scorecard information");
		}
		return new ResponseEntity<Response<List<ScoreCard>>>(
				new Response<List<ScoreCard>>(HttpStatus.OK.value(), "scorecard fetched successfully", scorecard),
				HttpStatus.OK);
	}
	
	@RequestMapping(value = "getAllUserNotAdminByLimitAndNotApprover/{buId}/{scorecardId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserNotAdminByLimitAndNotApprover(@PathVariable("buId") Long buId,@PathVariable("scorecardId") Long scorecardId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = scoreCardService.getAllUserNotAdminByLimitAndBuIdAndNotApprover(buId,scorecardId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}
	@RequestMapping(value = "getAllUserByNameAndBuIdAndNotApprover/{name}/{buId}/{scorecardId}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<User>>> getAllUserByNameAndBuIdAndNotApprover(@PathVariable("name") String name,
			@PathVariable("buId") Long buId,@PathVariable("scorecardId") Long scorecardId) {

		List<User> user = null;
		if (!VedantaUtility.isAuthorized(buId)) {
			return new ResponseEntity<Response<List<User>>>(
					new Response<List<User>>(HttpStatus.FORBIDDEN.value(),
							"Page you are trying to reach is absolutely forbidden for some reason", user),
					HttpStatus.FORBIDDEN);
		}

		try {
			user = scoreCardService.getAllUserByNameAndBuIdAndNotApprover(name, buId,scorecardId);
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Response<List<User>>>(
				new Response<List<User>>(HttpStatus.OK.value(), "User fetched successfully", user), HttpStatus.OK);
	}
}
