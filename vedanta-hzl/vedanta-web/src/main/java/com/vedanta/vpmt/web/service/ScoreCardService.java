package com.vedanta.vpmt.web.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vedanta.vpmt.mapper.ScoreCardAssign;
import com.vedanta.vpmt.model.BusinessUnit;
import com.vedanta.vpmt.model.ScoreCard;
import com.vedanta.vpmt.model.ScoreCardGroupUser;
import com.vedanta.vpmt.model.ScorecardAggregation;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.model.TargetValue;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.web.VedantaWebException;
import com.vedanta.vpmt.web.util.RestServiceUtil;
import com.vedanta.vpmt.web.util.URLConstants;
import com.vedanta.vpmt.web.util.VedantaConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by manishsanger on 23/09/17.
 */
@Component
@Slf4j
public class ScoreCardService {
	public static final Logger log = LoggerFactory.getLogger(ScoreCardService.class);

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private RestServiceUtil restServiceUtil;

	public Map<String, Object> getScoreCardTemplateByContract(String contractNumber) {

		int status;
		try {
			String url = String.format(URLConstants.GET_SCORECARD_TEMPLATE_BY_CONTRACT, contractNumber);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard template.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching all form fields details", 401);
			}
			log.error("Error while fetching scorecard template");
			throw new VedantaWebException("Error while fetching scorecard template");
		}
	}

	public Map<String, Object> getScoreCardTemplateByScoreCardId(long scoreCardId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_SCORECARD_TEMPLATE_BY_SCORECARD_ID, scoreCardId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard template.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}

			log.error("Error while fetching scorecard template");
			throw new VedantaWebException("Error while fetching scorecard template");
		}
	}

	public Map<String, Object> getScoreCardTemplateBySubCategoryId(long subCategoryId) {
		int status;
		if (subCategoryId <= 0) {
			log.info("contract number cannot be null/empty");
			throw new VedantaWebException("contract number cannot be null/empty");
		}
		try {
			String url = String.format(URLConstants.GET_SCORECARD_TEMPLATE_BY_SUB_CATEGORY, subCategoryId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard template.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}

			log.error("Error while fetching scorecard template");
			throw new VedantaWebException("Error while fetching scorecard template");
		}
	}

	public Map<String, Object> getScoreCardBaseData() {
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_SCORECARD_BASE_DATA, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard base data.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("Error while fetching scorecard template");
			throw new VedantaWebException("Error while fetching scorecard template");
		}
	}

	public List<TargetValue> getAllDistinctSavedScoreCardTarget() {
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_DISTINCT_TEMPLATE_TARGET, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard target.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<TargetValue>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("Error while fetching scorecard target");
			throw new VedantaWebException("Error while fetching scorecard target");
		}
	}

	public ScoreCard save(ScoreCard entity) {
		if (entity == null) {
			throw new VedantaWebException("ScoreCard cannot be null");
		}
		int status;
		try {

			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_SCORECARD, entity, null, HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save ScoreCard.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<ScoreCard>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("API not responded while save ScoreCard. ");
			throw new VedantaWebException("API not responded while save ScoreCard.");
		}
	}

	public ScoreCardGroupUser saveScoreCardGroupUser(ScoreCardGroupUser entity) {

		if (entity == null) {
			throw new VedantaWebException("ScoreCardGroupUser cannot be null");
		}
		int status;

		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SAVE_SCORECARD_GROUP_USER, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save ScoreCardGroupUser.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<ScoreCardGroupUser>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("API not responded while save ScoreCardGroupUser. ");
			throw new VedantaWebException("API not responded while save ScoreCardGroupUser.");
		}
	}

	public ScoreCardAssign assignScoreCard(ScoreCardAssign entity) {

		if (entity == null) {
			throw new VedantaWebException("ScoreCardGroupUser cannot be null");
		}
		int status;

		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.SCORECARD_ASSIGN, entity, null,
					HttpMethod.POST);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while save ScoreCardAssign.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			ScoreCardAssign sA = OBJECT_MAPPER.readValue(data, new TypeReference<ScoreCardAssign>() {
			});

			try {

				if (!ObjectUtils.isEmpty(sA) && !StringUtils.isEmpty(sA.getScoreCardGroupUserId())) {
					ScoreCardGroupUser scoreCardGroupUser = getScorecardGroupUserById(
							Long.parseLong(sA.getScoreCardGroupUserId()));

				}
			} catch (VedantaWebException e) {
				log.error("Email Nofication failed while assignign multiple scorecard" + e.getMessage());
			}

			return sA;

		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching ScoreCardAssign", 401);
			}
			log.error("API not responded while save ScoreCardAssign. ");
			throw new VedantaWebException("API not responded while save ScoreCardAssign.");
		}
	}

	public ScoreCardGroupUser getScorecardGroupUserById(Long scoreCardGroupUserId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_SCORECARD_GROUP_USER_ID, scoreCardGroupUserId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while scorecard GroupUser by Id.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<ScoreCardGroupUser>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard GroupUser by Id", 401);
			}
			log.error("Error while fetching scorecard GroupUser by Id");
			throw new VedantaWebException("Error while fetching scorecard GroupUser by Id");
		}
	}

	public List<ScoreCard> getAllScorecard() {

		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_SCORECARD, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all scorecard details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public List<ScoreCard> getAllScorecard(Integer statusValue) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_SCORECARD_STATUS, statusValue);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all scorecard details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public List<ScoreCard> getAllScorecard(Integer statusValue, Long businessUnitId, String plantCode) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_SCORECARD_STATUS_BUSINESSUNITID_PLANTCODE, statusValue,
					businessUnitId, plantCode);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all scorecard details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public List<ScoreCard> getAllScorecard(Integer statusValue, Long businessUnitId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_SCORECARD_STATUS_BUSINESS_UNIT_ID, statusValue,
					businessUnitId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all scorecard details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public List<ScoreCard> getAllScorecardDateRange(String fromDate, String toDate, Integer statusCode) {

		int status;
		try {

			String url = URLConstants.GET_ALL_SCORECARD_DATE_RANGE + "fromDate=" + URLEncoder.encode(fromDate, "UTF-8")
					+ "&toDate=" + URLEncoder.encode(toDate, "UTF-8") + "&status=" + statusCode;

			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all scorecard details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public List<ScoreCard> getAllScorecardDateRange(String fromDate, String toDate, Integer statusCode,
			Long businessUnitId, String plantCode) {

		int status;
		try {

			String url = URLConstants.GET_ALL_SCORECARD_DATE_RANGE_BUSINESSUNITID_PLANTCODE + "fromDate="
					+ URLEncoder.encode(fromDate, "UTF-8") + "&toDate=" + URLEncoder.encode(toDate, "UTF-8")
					+ "&status=" + statusCode + "&businessUnitId=" + businessUnitId + "&plantCode=" + plantCode;

			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("Error while fetching scorecard template", status);
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public List<ScoreCard> getAllScorecardDateRange(String fromDate, String toDate, Integer statusCode,
			Long businessUnitId) {

		try {

			String url = URLConstants.GET_ALL_SCORECARD_DATE_RANGE_BUSINESSUNITID + "fromDate="
					+ URLEncoder.encode(fromDate, "UTF-8") + "&toDate=" + URLEncoder.encode(toDate, "UTF-8")
					+ "&status=" + statusCode + "&businessUnitId=" + businessUnitId;

			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);

			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public Map<String, Object> getNewDashBoardData(Long plantId, Long categoryId, Long subCategoryId, Long contractId,
			String fromDate, String toDate) {

		int status;
		try {
			String url = null;
			if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {

				url = URLConstants.NEW_DASHBOARD_DATA + "plantId=" + plantId + "&categoryId=" + categoryId
						+ "&subCategoryId=" + subCategoryId + "&contractId=" + contractId + "&fromDate="
						+ URLEncoder.encode(fromDate, "UTF-8") + "&toDate=" + URLEncoder.encode(toDate, "UTF-8");
			} else {
				url = URLConstants.NEW_DASHBOARD_DATA + "plantId=" + plantId + "&categoryId=" + categoryId
						+ "&subCategoryId=" + subCategoryId + "&contractId=" + contractId;
			}

			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching new dashboard data.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("Error while fetching new dashboard data");
			throw new VedantaWebException("Error while fetching new dashboard data");
		}
	}

	public Map<String, Object> getNewDashBoardData() {

		int status;
		try {

			JsonNode response = restServiceUtil.makeRequest(URLConstants.NEW_DASHBOARD_DATA, null, null,
					HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException(" " + status);
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			if (errorCode.equals("403")) {
				throw new VedantaWebException("API not responded while fetching new dashboard data.", 403);
			}

			log.error("Error while fetching new dashboard data");
			throw new VedantaWebException("Error while fetching new dashboard data");
		}
	}

	public Set<ScorecardAggregation> getAllScorecardBySubCategoryID(Long subcategoryID) {

		int status;
		try {
			String url = String.format(URLConstants.GET_SCORECARD__BY_SUBCATEGORY_ID, subcategoryID);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching all scorecard details.");
			}
			String data = response.get(VedantaConstant.DATA).toString();

			return OBJECT_MAPPER.readValue(data, new TypeReference<Set<ScorecardAggregation>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard template", 401);
			}
			log.error("Error while fetching all scorecard details");
			throw new VedantaWebException("Error while fetching all scorecard details");
		}
	}

	public List<User> getAllUsersNotAssigned(long scoreCardId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_NOT_ASSIGNED, scoreCardId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching ALL_USER_NOT_ASSIGNED .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching ALL_USER_NOT_ASSIGNED listing", 401);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching ALL_USER_NOT_ASSIGNED listing");
		}
	}

	public List<User> getAllUserNotAssignedByName(long scoreCardId, String name) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_NOT_ASSIGNED_BY_NAME, scoreCardId, name);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching GET_ALL_USER_NOT_ASSIGNED_BY_NAME .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching GET_ALL_USER_NOT_ASSIGNED_BY_NAME listing", 401);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching GET_ALL_USER_NOT_ASSIGNED_BY_NAME listing");
		}
	}

	public List<User> getAllUserNotAdminByLimit(Long buId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_NOT_ADMIN_BY_LIMIT, buId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching ALL_USER_NOT_ASSIGNED .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching ALL_USER_NOT_ASSIGNED listing", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching ALL_USER_NOT_ASSIGNED listing");
		}
	}

	public List<User> getAllUserByName(String name) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_BY_NAME, name);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching GET_ALL_USER_BY_NAME .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching GET_ALL_USER_BY_NAME listing", 401);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching GET_ALL_USER_BY_NAME listing");
		}
	}

	public List<User> getAllUserByNameAndBuId(String name, Long buId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_BY_NAME_AND_BUID, name, buId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching GET_ALL_USER_BY_NAME .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching GET_ALL_USER_BY_NAME listing", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching GET_ALL_USER_BY_NAME listing");
		}
	}

	public List<SubCategory> getAllSubcategories() {
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_SUBCATEGORIES, null, null,
					HttpMethod.GET);

			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching subCategory.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<SubCategory>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching subcategory", 401);
			}
			log.error("Error while fetching subcategory");
			throw new VedantaWebException("Error while fetching subcategory");
		}
	}

	public List<BusinessUnit> getAllBussinessUnit() {
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_BUSINESS_UNIT, null, null,
					HttpMethod.GET);

			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching AllBussiness Unit.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<BusinessUnit>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching bussiness unit", 401);
			}
			log.error("Error while fetching subcategory");
			throw new VedantaWebException("Error while fetching bussiness unit");
		}
	}

	public ScoreCard getScoreCardById(long scoreCardId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_SCORECARD_BY_ID, scoreCardId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<ScoreCard>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard ", 401);
			}

			log.error("Error while fetching scorecard ");
			throw new VedantaWebException("Error while fetching scorecard ");
		}
	}

	public List<ScoreCard> getAllScorecardByRoleBased() {
		int status;
		try {
			JsonNode response = restServiceUtil.makeRequest(URLConstants.GET_ALL_SCORECARD_ROLE_BASED, null, null,
					HttpMethod.GET);

			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching scorecard List.");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<ScoreCard>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching scorecard List", 401);
			}
			log.error("Error while fetching scorecard List");
			throw new VedantaWebException("Error while fetching scorecard List");
		}
	}

	public List<User> getAllUserNotAdminByLimitAndNotApprover(Long buId, Long scorecardId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_NOT_ADMIN_BY_LIMIT_AND_NOT_APPROVER, buId,
					scorecardId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching ALL_USER_NOT_APPROVER .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching ALL_USER_NOT_ASSIGNED listing", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching ALL_USER_NOT_ASSIGNED listing");
		}
	}
	public List<User> getAllUserByNameAndBuIdAndNotApprover(String name, Long buId,Long scorecardId) {

		int status;
		try {
			String url = String.format(URLConstants.GET_ALL_USER_BY_NAME_AND_BUID_AND_NOT_APPROVER, name, buId,scorecardId);
			JsonNode response = restServiceUtil.makeRequest(url, null, null, HttpMethod.GET);
			status = response.get(VedantaConstant.STATUS_CODE).intValue();
			if (status != 200) {
				throw new VedantaWebException("API not responded while fetching GET_ALL_USER_BY_NAME .");
			}
			String data = response.get(VedantaConstant.DATA).toString();
			return OBJECT_MAPPER.readValue(data, new TypeReference<List<User>>() {
			});
		} catch (VedantaWebException | IOException e) {

			String errorCode = e.getMessage();
			if (errorCode.equals("401")) {
				log.error("Token Expired.");
				throw new VedantaWebException("Error while fetching GET_ALL_USER_BY_NAME listing", 401);
			}
			if (errorCode.equals("403")) {
				log.error("Page you are trying to reach is absolutely forbidden for some reason ");
				throw new VedantaWebException("Page you are trying to reach is absolutely forbidden for some reason",
						403);
			}

			log.error("Error while fetching ALL_USER_NOT_ADMIN");
			throw new VedantaWebException("Error while fetching GET_ALL_USER_BY_NAME listing");
		}
	}

}
