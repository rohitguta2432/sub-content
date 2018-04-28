package com.vedanta.vpmt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.DashboardDao;
import com.vedanta.vpmt.dao.PlantHeadDao;
import com.vedanta.vpmt.dao.ScoreCardDao;
import com.vedanta.vpmt.dao.ScoreCardGroupUserDao;
import com.vedanta.vpmt.dao.TemplateFormDao;
import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.mapper.ApproverUpdateMapper;
import com.vedanta.vpmt.mapper.DashboardDataMapper;
import com.vedanta.vpmt.mapper.ScoreCardAssign;
import com.vedanta.vpmt.mapper.ScoreCardGroupUserMapper;
import com.vedanta.vpmt.model.*;
import com.vedanta.vpmt.model.ScoreCard.Param;
import com.vedanta.vpmt.util.DateUtil;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.mail.MessagingException;

/**
 * @author orange
 *
 */
@EnableAsync
@Service("scoreCardService")
@Slf4j
public class ScoreCardServiceImpl implements ScoreCardService {

	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Autowired
	private ScoreCardDao scoreCardDao;

	@Autowired
	private ScoreCardGroupUserDao scoreCardGroupUserDao;

	@Autowired
	private ContractService contractService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private GroupFieldService groupFieldService;

	@Autowired
	private TemplateGroupService templateGroupService;

	@Autowired
	private FieldService fieldService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private PlantService plantService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private UserDetailServiceImpl userDetail;

	@Autowired
	private DataUnitService dataUnitService;

	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private PlantHeadDao plantHeadDao;

	@Autowired
	private TemplateFormDao templateFormDao;

	@Autowired
	private FormSaveService formSaveService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private FormService formService;

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ScoreCardDao scorecardDaoBySubcategory;

	@Autowired
	private DashboardDao dashboardDao;

	@Autowired
	private MailSenderServices mailSenderService;

	@Autowired
	private PlantHeadService plantHeadService;
	
	@Value("${approver.list.mail}")
	private String approverUploadMail;

	@Override
	public ScoreCard get(long id) {
		if (id <= 0) {
			log.info("Invalid scorecard id.");
			throw new VedantaException("Invalid scorecard id.");
		}
		try {
			return scoreCardDao.findOne(id);
		} catch (VedantaException e) {
			String msg = "Error while fetching scorecard by Id";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public ScoreCard save(ScoreCard entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("ScoreCard object cannot be null/empty");
			throw new VedantaException("ScoreCard object cannot be null/empty");
		}

		try {

			if (!ObjectUtils.isEmpty(entity.getActualScore())) {
				double actualScore = scoreCardDao.getActualScore(entity.getId());
				entity.setLastActualScore(actualScore);
			}
			setScoreCardDataSave(entity);
			return scoreCardDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving scorecard information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	public ScoreCard populate(ScoreCard entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("ScoreCard object cannot be null/empty");
			throw new VedantaException("ScoreCard object cannot be null/empty");
		}
		try {
			return scoreCardDao.save(entity);
		} catch (VedantaException e) {
			String msg = "Error while saving scorecard information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public void firstScorecardEntry(ScoreCard scoreCard) {
		List<TemplateGroup> templateGroups = this.getAllTemplateGroupByScorecardId(scoreCard.getId());
		ScoreCard scorecardNew = scoreCardDao.findOne(scoreCard.getId());
		Gson g = new Gson();

		if (ObjectUtils.isEmpty(scorecardNew.getScorecardDataJson())) {
			List<ScoreCardData> scoreCardDataList = new ArrayList<>();
			Long scoreCardDataId = 0L;
			for (TemplateGroup templateGroup : templateGroups) {
				Group group = templateGroup.getGroup();
				for (Field field : group.getFields()) {
					ScoreCardData scoreCardData = new ScoreCardData();
					scoreCardData.setId(++scoreCardDataId);
					scoreCardData.setContractId(scorecardNew.getContractId());
					scoreCardData.setDepartmentId(scorecardNew.getDepartmentId());
					scoreCardData.setFieldId(field.getId());
					scoreCardData.setGroupId(group.getId());
					scoreCardData.setLastScore(0.0D);
					scoreCardData.setLastValue("");
					scoreCardData.setScore(0.0d);
					scoreCardData.setScorecardId(scoreCard.getId());
					scoreCardData.setStatus(Constants.STATUS_NOT_FILLED);
					scoreCardData.setUserId(Long.parseLong(scorecardNew.getContractManagerId()));
					scoreCardData.setValue("");
					scoreCardData.setVendorId(scorecardNew.getVendorId());
					scoreCardData.setWeightScore(field.getWeight());
					scoreCardData.setRemarks("");
					scoreCardData.setWeightScore(0.0D);
					scoreCardDataList.add(scoreCardData);

				}
			}
			scorecardNew.setScorecardDataJson(g.toJson(scoreCardDataList));
			scoreCardDao.save(scorecardNew);
			scorecardNew = scoreCardDao.findOne(scoreCard.getId());
			if (!ObjectUtils.isEmpty(scorecardNew.getScorecardDataJson())) {

				for (ScoreCardData scoreCardData : getScoreCardDataFromScoreCard(scorecardNew)) {
					for (List<Param> paramList : scoreCard.getScoreParamList()) {
						for (Param param : paramList) {
							if (Long.parseLong(param.getGroupId()) == scoreCardData.getGroupId()
									&& Long.parseLong(param.getFieldId()) == scoreCardData.getFieldId()) {
								param.setId(String.valueOf(scoreCardData.getId()));
							}
						}
					}
				}
			}

		}
	}

	@Override
	public List<ScoreCardData> getScoreCardDataFromScoreCard(ScoreCard scoreCard) {

		try {
			if (!ObjectUtils.isEmpty(scoreCard.getScorecardDataJson())) {
				return OBJECT_MAPPER.readValue(scoreCard.getScorecardDataJson(),
						new TypeReference<List<ScoreCardData>>() {
						});
			} else {
				return new ArrayList<ScoreCardData>();
			}
		} catch (IOException e) {
			log.info("ScoreCard Data object json parsing error.");
			throw new VedantaException("ScoreCard Data object json parsing error.");
		}
	}

	private void setScoreCardDataSave(ScoreCard scoreCard) {

		if (ObjectUtils.isEmpty(scoreCard) || ObjectUtils.isEmpty(scoreCard.getScoreParamList())) {
			log.info("ScoreCard object cannot be null/empty");
			throw new VedantaException("ScoreCard object cannot be null/empty");
		}

		try {

			List<List<Param>> scoreParamList = scoreCard.getScoreParamList();
			Gson g = new Gson();
			this.firstScorecardEntry(scoreCard);
			ScoreCard existedScoreCard = scoreCardDao.findOne(scoreCard.getId());

			if (!ObjectUtils.isEmpty(existedScoreCard.getContractManagerName())) {
				scoreCard.setContractManagerName(existedScoreCard.getContractManagerName());
			} else {
				scoreCard.setContractManagerName("NA");
			}

			if (ObjectUtils.isEmpty(scoreCard.getSubmittedBy())
					&& !ObjectUtils.isEmpty(existedScoreCard.getSubmittedBy())) {
				scoreCard.setSubmittedBy(existedScoreCard.getSubmittedBy());
			}

			if (ObjectUtils.isEmpty(scoreCard.getSubmittedOn())
					&& !ObjectUtils.isEmpty(existedScoreCard.getSubmittedOn())) {
				scoreCard.setSubmittedOn(existedScoreCard.getSubmittedOn());
			}

			if (ObjectUtils.isEmpty(scoreCard.getApprovedBy())
					&& !ObjectUtils.isEmpty(existedScoreCard.getApprovedBy())) {
				scoreCard.setApprovedBy(existedScoreCard.getApprovedBy());
			}

			if (ObjectUtils.isEmpty(scoreCard.getApprovedOn())
					&& !ObjectUtils.isEmpty(existedScoreCard.getApprovedOn())) {
				scoreCard.setApprovedOn(existedScoreCard.getApprovedOn());
			}

			if (!ObjectUtils.isEmpty(existedScoreCard.getDueDate())) {
				scoreCard.setDueDate(existedScoreCard.getDueDate());
			}

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();
			List<ScoreCardData> scoreCardDataList = getScoreCardDataFromScoreCard(existedScoreCard);

			if (ObjectUtils.isEmpty(scoreCardDataList) && scoreCardDataList.size() < 1) {
				scoreCardDataList = new ArrayList<>();
			}

			for (List<Param> paramList : scoreParamList) {
				for (Param param : paramList) {

					if (!ObjectUtils.isEmpty(param.getId())) {
						for (ScoreCardData scd : scoreCardDataList) {
							if (Long.parseLong(param.getId()) == scd.getId()) {

								scd.setLastScore(scd.getScore());
								scd.setLastValue(scd.getValue());
								scd.setLastWeightScore(scd.getWeightScore());

								scd.setUserId(vedantaUser.getId());

								if (scoreCard.getContractId() != null)
									scd.setContractId(scoreCard.getContractId());
								if (scoreCard.getDepartmentId() != null)
									scd.setDepartmentId(scoreCard.getDepartmentId());
								if (!ObjectUtils.isEmpty(param.getFieldId()))
									scd.setFieldId(Long.parseLong(param.getFieldId()));
								if (!ObjectUtils.isEmpty(param.getGroupId()))
									scd.setGroupId(Long.parseLong(param.getGroupId()));
								if (!ObjectUtils.isEmpty(param.getScore()))
									scd.setScore(Double.parseDouble(param.getScore()));
								if (!ObjectUtils.isEmpty(param.getScoreWeight()))
									scd.setWeightScore(Double.parseDouble(param.getScoreWeight()));
								if (param.getTarget() != null)
									scd.setTargetValue(param.getTarget());
								if (param.getValue() != null)
									scd.setValue(param.getValue());
								if (scoreCard.getVendorId() != null)
									scd.setVendorId(scoreCard.getVendorId());
								if (param.getRemark() != null)
									scd.setRemarks(param.getRemark());

							}
						}
					} else {
						ScoreCardData existedScoreCardData = new ScoreCardData();

						existedScoreCardData.setUserId(vedantaUser.getId());

						if (scoreCard.getContractId() != null)
							existedScoreCardData.setContractId(scoreCard.getContractId());
						if (scoreCard.getDepartmentId() != null)
							existedScoreCardData.setDepartmentId(scoreCard.getDepartmentId());
						if (param.getFieldId() != null)
							existedScoreCardData.setFieldId(Long.parseLong(param.getFieldId()));
						if (param.getGroupId() != null)
							existedScoreCardData.setGroupId(Long.parseLong(param.getGroupId()));
						if (!ObjectUtils.isEmpty(param.getScore())) {
							existedScoreCardData.setScore(Double.parseDouble(param.getScore()));
						} else {
							existedScoreCardData.setScore(0.0d);
						}
						if (!ObjectUtils.isEmpty(param.getScoreWeight())) {
							existedScoreCardData.setWeightScore(Double.parseDouble(param.getScoreWeight()));
						} else {
							existedScoreCardData.setWeightScore(0.0d);
						}
						if (!ObjectUtils.isEmpty(param.getTarget())) {
							existedScoreCardData.setTargetValue(param.getTarget());
						} else {
							existedScoreCardData.setTargetValue("NA");
						}
						if (!ObjectUtils.isEmpty(param.getValue())) {
							existedScoreCardData.setValue(param.getValue());
						} else {
							existedScoreCardData.setValue("NA");
						}
						if (scoreCard.getVendorId() != null)
							existedScoreCardData.setVendorId(scoreCard.getVendorId());
						if (param.getRemark() != null)
							existedScoreCardData.setRemarks(param.getRemark());

						scoreCardDataList.add(existedScoreCardData);
					}
				}
			}
			scoreCard.setScorecardDataJson(g.toJson(scoreCardDataList));
		} catch (VedantaException e) {
			String msg = "Error while saving scorecard information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	private List<TemplateGroup> getAllTemplateGroupByScorecardId(Long scoreCardId) {

		if (scoreCardId <= 0) {
			log.info("scorecard number cannot be null/empty");
			throw new VedantaException("scorecard number cannot be null/empty");
		}
		try {
			ScoreCard scoreCard = this.get(scoreCardId);

			if (!ObjectUtils.isEmpty(scoreCard)) {
				Contract contractDetails = contractService.findByContractNumber(scoreCard.getContractNumber());
				if (!ObjectUtils.isEmpty(contractDetails)) {
					Template templateDetail = templateService
							.getScoreCardTemplateBySubCategoryId(contractDetails.getSubCategoryId());
					if (!ObjectUtils.isEmpty(templateDetail)) {

						List<TemplateGroup> templateGroups = new ArrayList<>();
						templateGroups = templateGroupService.getAllTemplateGroupsByTemplateId(templateDetail.getId());

						if (!CollectionUtils.isEmpty(templateGroups)) {
							templateGroups.forEach(templateGroup -> {
								Group groupDetail = groupService.get(templateGroup.getGroupId());
								List<Field> groupFieldList = new ArrayList<>();
								List<GroupField> groupFields = groupFieldService
										.getAllGroupFieldsByGroupId(groupDetail.getId());
								groupFields.forEach(groupField -> {
									Field fieldDetail = fieldService.getFieldDetail(groupField.getFieldId());
									groupFieldList.add(fieldDetail);
								});
								groupDetail.setFields(groupFieldList);
								templateGroup.setGroup(groupDetail);
							});
							return templateGroups;
						}
					}
				}
			}
			return null;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public ScoreCard update(long id, ScoreCard entity) {
		return null;
	}

	@Override
	public ScoreCardGroupUser saveScoreCardGroupUser(ScoreCardGroupUser scoreCardGroupUser) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) auth.getPrincipal();
		if (ObjectUtils.isEmpty(scoreCardGroupUser)) {
			log.info("ScoreCard group user object cannot be null/empty");
			throw new VedantaException("ScoreCard group user object cannot be null/empty");
		}

		try {

			ScoreCardGroupUser existingScoreCardGroupUser;
			existingScoreCardGroupUser = scoreCardGroupUserDao.getScoreCardGroupUserForGroup(
					scoreCardGroupUser.getContractNumber(), scoreCardGroupUser.getContractManagerId(),
					scoreCardGroupUser.getTemplateId(), scoreCardGroupUser.getGroupId());

			if (scoreCardGroupUser.getGroupId() == 0) {
				ScoreCard scorecard = this.get(scoreCardGroupUser.getScorecardId());
				if (!ObjectUtils.isEmpty(scorecard)) {
					User user = userDetail.getUserByEmployeeIdAndBusinessUnitId(scoreCardGroupUser.getEmployeeId(),
							scoreCardGroupUser.getBusinessUnitId());
					if (!ObjectUtils.isEmpty(user)) {
						scorecard.setContractManagerId(scoreCardGroupUser.getEmployeeId());
						scorecard.setContractManagerName(user.getName() == null ? "" : user.getName());
						scorecard.setLastUpdate(new Date());
						scorecard.setLastUpdateBy(vedantaUser.getId());
						this.updateScorecard(scorecard);
					}
				}

				List<ScoreCardGroupUser> existingScoreCardGroupUserList = scoreCardGroupUserDao
						.getScoreCardGroupsByContractNumberAndManagerIdAndTemplateIdAndGroupId(
								scoreCardGroupUser.getContractNumber(), scoreCardGroupUser.getContractManagerId(),
								scoreCardGroupUser.getTemplateId());

				if (!ObjectUtils.isEmpty(existingScoreCardGroupUserList))
					scoreCardGroupUserDao.delete(existingScoreCardGroupUserList);
			} else {
				if (!ObjectUtils.isEmpty(existingScoreCardGroupUser)) {
					scoreCardGroupUserDao.delete(existingScoreCardGroupUser);
				}
			}

			try {
				mailSenderService.scorecardAssignEmailNotification(scoreCardGroupUser);
			} catch (MessagingException e) {

				e.printStackTrace();
			}
			return scoreCardGroupUserDao.save(scoreCardGroupUser);
		} catch (VedantaException e) {
			String msg = "Error while saving scorecard group user information";
			log.info(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Map<String, Object> getScoreCardBaseData() {
		try {
			Map<String, Object> baseDataMap = new HashMap<>();
			baseDataMap.put(Constants.ALL_CATEGORIES, categoryService.getAllCategories());
			baseDataMap.put(Constants.ALL_PLANTS, plantService.getAllPlants());
			baseDataMap.put(Constants.ALL_DEPARTMENT, departmentService.getAllDepartments());
			return baseDataMap;
		} catch (VedantaException e) {
			String msg = "Error while getting scorecard data";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private boolean isCurrentUserGranted() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_ADMIN)
					|| (authorities.getAuthority()).equals(Constants.ROLE_BUSINESS_UNIT_HEAD)) {
				return true;
			}
		}

		return false;
	}

	private boolean isCurrentUserContractManager() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_CONTRACT_MANAGER)) {
				return true;
			}
		}

		return false;
	}

	private boolean isCurrentUserPlantHead() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		for (GrantedAuthority authorities : vedantaUser.getAuthorities()) {
			if ((authorities.getAuthority()).equals(Constants.ROLE_PLANT_HEAD)) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * @param plantHeads
	 * @param employeeId
	 * @return
	 */
	private Boolean isPlantHeadListContainsEmployeeId(List<PlantHead> plantHeads, String employeeId) {
		if (!ObjectUtils.isEmpty(employeeId) && !ObjectUtils.isEmpty(plantHeads) && plantHeads.size() > 0) {
			return plantHeads.stream().anyMatch(plantHead -> plantHead.getEmployeeId().equals(employeeId));
		}
		return false;
	}

	@Override
	public Map<String, Object> getScoreCardTemplateMapByScoreCardId(Long scoreCardId) {
		if (scoreCardId <= 0) {
			log.info("scorecard number cannot be null/empty");
			throw new VedantaException("scorecard number cannot be null/empty");
		}
		try {
			Map<String, Object> scoreCardTemplateMap = new HashMap<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			ScoreCard scoreCard = this.get(scoreCardId);
			List<User> users = userDetail.getAllUsersNotAdmin();
			if (!ObjectUtils.isEmpty(scoreCard)) {
				scoreCard.setIsContractManager(false);
				scoreCard.setIsApprover(false);

				scoreCard.setScoreCardData(this.getScoreCardDataFromScoreCard(scoreCard));
				scoreCard.setIsEnable(true);
				scoreCardTemplateMap.put(Constants.SCORECARD_DETAIL, scoreCard);
				scoreCardTemplateMap.put(Constants.PLANT_DETAIL, plantService.get(scoreCard.getPlantId()));
				scoreCardTemplateMap.put(Constants.USER_DETAIL, users);
				Contract contractDetails = contractService.findByContractNumber(scoreCard.getContractNumber());
				if (!ObjectUtils.isEmpty(contractDetails)) {
					scoreCardTemplateMap.put(Constants.CONTRACT, contractDetails);
					Template templateDetail = templateService
							.getScoreCardTemplateBySubCategoryId(contractDetails.getSubCategoryId());
					templateDetail.setIsAllGroupAccess(false);
					if (!ObjectUtils.isEmpty(templateDetail)) {

						List<TemplateGroup> templateGroups = new ArrayList<>();

						Boolean isGranted = isCurrentUserGranted();
						
						Boolean contractMangerAndApprover=true;

						/* for Scorecard Approver */
						String scorecardApprover = Optional.ofNullable(scoreCard.getApproverById()).orElse("");

						if (scorecardApprover.equalsIgnoreCase(scoreCard.getApproverById())) {
							isGranted = true;
							scoreCard.setIsApprover(true);
							contractMangerAndApprover=false;
						}

						/* for Scorecard Contract-mangers */
						if (isCurrentUserContractManager()) {
							if (scoreCard.getContractManagerId().equals(vedantaUser.getEmployeeId())) {
								isGranted = true;
								scoreCard.setIsContractManager(true);
								if(contractMangerAndApprover)
								scoreCard.setIsApprover(false);
							}

						}
						/* for Scorecard Plant-head */
						if (isCurrentUserPlantHead()) {
							List<PlantHead> plantHead = plantHeadDao
									.findAllByPlantCodeAndStatus(scoreCard.getPlantCode(), Constants.STATUS_ACTIVE);
							isGranted = isPlantHeadListContainsEmployeeId(plantHead, vedantaUser.getEmployeeId());
							if (isGranted)
								scoreCard.setIsApprover(false);
						}

						if (isGranted) {
							templateGroups = templateGroupService
									.getAllTemplateGroupsByTemplateId(templateDetail.getId());
							templateDetail.setIsAllGroupAccess(true);
							User user = userDetail.getUserByUserId(vedantaUser.getId());
							if (!ObjectUtils.isEmpty(user)) {
								List<ScoreCardGroupUser> userAssignedChecked = scoreCardGroupUserDao
										.getScoreCardGroupUserForGroupAmdTemplate(contractDetails.getNumber(),
												scoreCard.getPoItem(), templateDetail.getId(), 0L);

								String assignedUserName = getAssignedUserName(scoreCard);

								if (assignedUserName.equals("none")) {
									templateDetail.setUserName(user.getName());
								} else {
									templateDetail.setUserName(assignedUserName);
								}
							} else {
								templateDetail.setUserName("");
							}

						} else {

							List<ScoreCardGroupUser> userAssigned = scoreCardGroupUserDao
									.getScoreCardGroupUserForGroupAmdTemplate(contractDetails.getNumber(),
											scoreCard.getPoItem(), templateDetail.getId(), 0L);

							if (!ObjectUtils.isEmpty(userAssigned) && userAssigned.size() > 0) {

								User user = userDetail.getUserByEmployeeId(userAssigned.get(0).getEmployeeId());

								long vedantaUserId = vedantaUser.getId();
								long userId = user.getId();

								if (userId == vedantaUserId) {
									templateDetail.setIsAllGroupAccess(true);
								}
								if (!ObjectUtils.isEmpty(user)) {

									List<ScoreCardGroupUser> userAssignedChecked = scoreCardGroupUserDao
											.getScoreCardGroupUserForGroupAmdTemplate(contractDetails.getNumber(),
													scoreCard.getPoItem(), templateDetail.getId(), 0L);

									String assignedUserName = getAssignedUserName(scoreCard);

									if (assignedUserName.equals("none")) {
										templateDetail.setUserName(user.getName());
									} else {
										templateDetail.setUserName(assignedUserName);
									}
								} else {
									templateDetail.setUserName("");
								}
							} else {

								List<ScoreCardGroupUser> userAssignedChecked = scoreCardGroupUserDao
										.getScoreCardGroupUserForGroupAmdTemplate(contractDetails.getNumber(),
												scoreCard.getPoItem(), templateDetail.getId(), 0L);

								String assignedUserName = getAssignedUserName(scoreCard);

								if (assignedUserName.equals("none")) {
									templateDetail.setUserName(scoreCard.getContractManagerName());
								} else {
									templateDetail.setUserName(assignedUserName);
								}
							}

							List<ScoreCardGroupUser> scoreCardGroupUsers = scoreCardGroupUserDao
									.getScoreCardGroupsByContractNumberAndUser(contractDetails.getNumber(),
											vedantaUser.getEmployeeId());
							Set<Long> groupIds = new HashSet<>();
							if (!CollectionUtils.isEmpty(scoreCardGroupUsers)) {
								scoreCardGroupUsers.forEach(scoreCardGroupUser -> {
									if (scoreCardGroupUser.getGroupId() > 0) {
										scoreCard.setIsEnable(false);
										groupIds.add(scoreCardGroupUser.getGroupId());
									} else {
										scoreCard.setIsEnable(true);
										/* for group id equals to 0 */
										// templateDetail.setIsAllGroupAccess(true);
									}
								});
								if ((groupIds.size() > 0)) {
									templateGroups = templateGroupService.getAllTemplateGroupsInGroupsId(groupIds);

								} else {
									templateGroups = templateGroupService
											.getAllTemplateGroupsByTemplateId(templateDetail.getId());
									/* for group id equals to 0 */
									templateDetail.setIsAllGroupAccess(true);
								}
								if (!ObjectUtils.isEmpty(templateGroups) && templateGroups.size() > 0) {
									for (TemplateGroup tGroup : templateGroups) {
										if (tGroup.getTemplateId() != scoreCard.getTemplateId()) {
											templateGroups.remove(tGroup);
										}
									}
								}
							}
						}
						scoreCardTemplateMap.put(Constants.TEMPLATE, templateDetail);

						if (!CollectionUtils.isEmpty(templateGroups)) {

							templateGroups.forEach(templateGroup -> {
								Group groupDetail = groupService.get(templateGroup.getGroupId());
								List<Field> groupFieldList = new ArrayList<>();
								List<GroupField> groupFields = groupFieldService
										.getAllGroupFieldsByGroupId(groupDetail.getId());
								groupFields.forEach(groupField -> {
									Field fieldDetail = fieldService.getFieldDetail(groupField.getFieldId());

									if (!ObjectUtils.isEmpty(fieldDetail.getDataUnitId())) {
										fieldDetail.setDataUnit(dataUnitService.get(fieldDetail.getDataUnitId()));
									}

									groupFieldList.add(fieldDetail);
								});
								groupDetail.setFields(groupFieldList);

								List<ScoreCardGroupUser> userAssignedGroup = scoreCardGroupUserDao
										.getScoreCardGroupUserForGroupAmdTemplate(contractDetails.getNumber(),
												scoreCard.getPoItem(), templateDetail.getId(), groupDetail.getId());

								if (!ObjectUtils.isEmpty(userAssignedGroup) && userAssignedGroup.size() > 0) {
									User user = userDetail
											.getUserByEmployeeId(userAssignedGroup.get(0).getEmployeeId());
									if (!ObjectUtils.isEmpty(user))
										groupDetail.setUserName(user.getName());
									else
										groupDetail.setUserName("");
								} else {

									List<ScoreCardGroupUser> userAssignedChecked = scoreCardGroupUserDao
											.getScoreCardGroupUserForGroupAmdTemplate(contractDetails.getNumber(),
													scoreCard.getPoItem(), templateDetail.getId(), 0L);

									String assignedUserName = getAssignedUserName(scoreCard);

									if (assignedUserName.equals("none")) {
										groupDetail.setUserName(templateDetail.getUserName());
									} else {
										groupDetail.setUserName(assignedUserName);
									}
								}

								templateGroup.setGroup(groupDetail);
							});

							scoreCardTemplateMap.put(Constants.TEMPLATE_GROUPS, templateGroups);
						}
					}
				}
			}

			scoreCardTemplateMap.put(Constants.TEMPLATE_FROMS, getCompliance(scoreCard));

			return scoreCardTemplateMap;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private List<TemplateForm> getCompliance(ScoreCard scoreCard) {
		if (ObjectUtils.isEmpty(scoreCard)) {
			log.info("scorecard cannot be null/empty");
			throw new VedantaException("scorecard cannot be null/empty");
		}

		try {
			Form form = null;
			List<FormSaved> formSavedList = null;
			FormSaved formSaved = null;
			List<TemplateForm> templateForms = templateFormDao.findByTemplateId(scoreCard.getTemplateId());

			if (!ObjectUtils.isEmpty(templateForms) && templateForms.size() > 0) {
				for (TemplateForm templateForm : templateForms) {
					formSavedList = formSaveService.getComplianceFormByMonthAndStatus(scoreCard.getPlantCode(),
							scoreCard.getContractNumber(), templateForm.getFormId(), scoreCard.getMonthId(),
							scoreCard.getYearId());
					if (!ObjectUtils.isEmpty(formSavedList) && formSavedList.size() > 0) {
						formSaved = formSavedList.get(0);
						if (!ObjectUtils.isEmpty(formSaved)) {
							if (formSaved.getStatus() == Constants.STATUS_APPROVED) {
								templateForm.setIsFormSaved(true);
							} else {
								templateForm.setIsFormSaved(false);
							}
						}
						templateForm.setFormSavedId(formSaved.getId());
					} else {
						templateForm.setIsFormSaved(false);
						templateForm.setFormSavedId(0L);
					}
					form = formService.get(templateForm.getFormId());
					if (!ObjectUtils.isEmpty(form)) {
						templateForm.setFormName(form.getName());
					}
				}
			}

			return templateForms;
		} catch (VedantaException e) {
			log.info("error while fetching Complains at scorecard get by Id.");
			throw new VedantaException("error while fetching Complains at scorecard get by Id.");
		}
	}

	private String getAssignedUserName(ScoreCard scorecard) {
		if (!ObjectUtils.isEmpty(scorecard)) {
			User user = userDetail.getUserByEmployeeIdAndBusinessUnitId(scorecard.getContractManagerId(),
					scorecard.getBusinessUnitId());
			if (!ObjectUtils.isEmpty(user))
				return user.getName();
		}
		return "none";

	}

	@Override
	public Map<String, Object> getScoreCardTemplateMapBySubCategoryId(Long subCategoryId) {
		if (subCategoryId <= 0) {
			log.info("sub category id cannot be null/empty");
			throw new VedantaException("sub category id cannot be null/empty");
		}
		try {
			Map<String, Object> scoreCardTemplateMap = new HashMap<>();

			Template templateDetail = templateService.getScoreCardTemplateBySubCategoryId(subCategoryId);
			if (!ObjectUtils.isEmpty(templateDetail)) {
				scoreCardTemplateMap.put(Constants.TEMPLATE, templateDetail);
				List<TemplateGroup> templateGroups = templateGroupService
						.getAllTemplateGroupsByTemplateId(templateDetail.getId());
				if (!CollectionUtils.isEmpty(templateGroups)) {
					templateGroups.forEach(templateGroup -> {
						Group groupDetail = groupService.get(templateGroup.getGroupId());
						List<Field> groupFieldList = new ArrayList<Field>();
						List<GroupField> groupFields = groupFieldService
								.getAllGroupFieldsByGroupId(groupDetail.getId());
						groupFields.forEach(groupField -> {
							Field fieldDetail = fieldService.getFieldDetail(groupField.getFieldId());
							groupFieldList.add(fieldDetail);
						});
						groupDetail.setFields(groupFieldList);
						templateGroup.setGroup(groupDetail);
					});
					scoreCardTemplateMap.put(Constants.TEMPLATE_GROUPS, templateGroups);
				}
			}
			return scoreCardTemplateMap;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}

	@Override
	public List<ScoreCard> getScoreCardByContractNumberAndStatus(String contractNumber, int status) {
		if (StringUtils.isEmpty(contractNumber)) {
			log.info("contract number cannot be null/empty");
			throw new VedantaException("contract number cannot be null/empty");
		}
		try {
			List<ScoreCard> scoreCards = new ArrayList<>();
			scoreCards = scoreCardDao.getContractScoreCardsByStatus(contractNumber, status);
			return scoreCards;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public ScoreCardGroupUser getScoreCardGroupUserById(Long scoreCardGroupUserId) {
		if (StringUtils.isEmpty(scoreCardGroupUserId)) {
			log.info("scoreCardGroupUserId cannot be null/empty");
			throw new VedantaException("scoreCardGroupUserId cannot be null/empty");
		}
		try {
			ScoreCardGroupUser scoreCardGroupUser = scoreCardGroupUserDao.findOne(scoreCardGroupUserId);
			return scoreCardGroupUser;
		} catch (VedantaException e) {
			String msg = "Error while getting scoreCardGroupUser by Id";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<ScoreCard> getUserScoreCardsByStatus(int status) {
		if (status < 0) {
			log.info("This is not a valid status");
			throw new VedantaException("This is not a valid status");
		}
		try {
			List<ScoreCard> scoreCards = new ArrayList<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (isCurrentUserGranted()) {
				// fetching score-card for role Admin by business unit id, plant
				// code and status
				if (VedantaUtility.isCurrentUserAdmin()) {

					if (status == Constants.ALL_SCORECARD) {

						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards = scoreCardDao.getAllScoreCardNotStatus(Constants.STATUS_DELETE);
							Gson g = new Gson();
							// (g.toJson(scoreCards));
						} else if (VedantaUtility.getCurrentUserBuId() != 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards = scoreCardDao.getAllScoreCardNotStatus(VedantaUtility.getCurrentUserBuId(),
									Constants.STATUS_DELETE);
						} else {
							scoreCards = scoreCardDao.getAllScoreCardNotStatus(VedantaUtility.getCurrentUserBuId(),
									VedantaUtility.getCurrentUserPlantCode(), Constants.STATUS_DELETE);
						}
					} else {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards = scoreCardDao.getAllScoreCard(status);
						} else if (VedantaUtility.getCurrentUserBuId() != 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards = scoreCardDao.getAllScoreCard(VedantaUtility.getCurrentUserBuId(), status);
						} else {
							scoreCards = scoreCardDao.getAllScoreCardStatus(VedantaUtility.getCurrentUserBuId(),
									VedantaUtility.getCurrentUserPlantCode(), status);
						}

					}

				}

				// fetching score-card for role Business Unit Head by business
				// unit id , plant
				// code and status .

				if (VedantaUtility.isCurrentUserBusinessUnitHead()) {

					if (status == Constants.ALL_SCORECARD) {

						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards = scoreCardDao.getAllScoreCardNotStatus(Constants.STATUS_DELETE);
						} else {
							scoreCards = scoreCardDao.getAllScoreCardNotStatus(VedantaUtility.getCurrentUserBuId(),
									Constants.STATUS_DELETE);
						}
					} else {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards = scoreCardDao.getAllScoreCard(status);
						} else {
							scoreCards = scoreCardDao.getAllScoreCard(VedantaUtility.getCurrentUserBuId(), status);
						}

					}

				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

					}
				}

				return scoreCards;
			}

			if (isCurrentUserPlantHead()) {
				String employeeId = vedantaUser.getEmployeeId();
				List<PlantHead> plantHeads = plantHeadDao.findAllByEmployeeIdAndStatus(employeeId,
						Constants.STATUS_ACTIVE);
				Set<String> plantCodes = new HashSet<>();
				for (PlantHead pH : plantHeads) {
					plantCodes.add(pH.getPlantCode());
				}

				if (!ObjectUtils.isEmpty(plantHeads) && plantHeads.size() > 0) {
					if (status == Constants.ALL_SCORECARD) {

						if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndNotStatus(plantCodes,
									Constants.STATUS_DELETE);
						} else {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndNotStatus(
									VedantaUtility.getCurrentUserBuId(), plantCodes, Constants.STATUS_DELETE);
						}
					} else {
						if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndStatus(plantCodes, status);
						} else {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndStatus(
									VedantaUtility.getCurrentUserBuId(), plantCodes, status);
						}
					}

					if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
						for (ScoreCard sc : scoreCards) {
							sc.setVendor(vendorService.get(sc.getVendorId()));
							sc.setTemplate(templateService.get(sc.getTemplateId()));

							if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
								User user = userDetail.getUserByUserId(sc.getSubmittedBy());
								if (!ObjectUtils.isEmpty(user)) {
									sc.setSubmittedNameBy(user.getName());
								}
							}

						}
					}
				}

				return scoreCards;
			}

			Boolean isContractManger = false;
			List<ScoreCard> scoreCardsList = new ArrayList<>();

			if (isCurrentUserContractManager()) {
				if (status == Constants.ALL_SCORECARD) {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatus(vedantaUser.getEmployeeId(),
								Constants.STATUS_DELETE);
					} else {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatus(
								VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(),
								Constants.STATUS_DELETE);
					}

				} else {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatus(vedantaUser.getEmployeeId(),
								status);
					} else {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatus(
								VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(), status);
					}
				}
				if (!ObjectUtils.isEmpty(scoreCardsList) && scoreCardsList.size() > 0) {
					for (ScoreCard sc : scoreCardsList) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));
					}
				}
				isContractManger = true;
			}

			List<ScoreCardGroupUserMapper> scoreCardGroupUserMappers = scoreCardGroupUserDao
					.getDistinctContractNumbersAndManagersByUserId(vedantaUser.getEmployeeId());

			Set<String> contractNumbers = new HashSet<>();
			Set<String> contractManagerIds = new HashSet<>();
			Set<String> poItems = new HashSet<>();

			if (!CollectionUtils.isEmpty(scoreCardGroupUserMappers)) {
				scoreCardGroupUserMappers.forEach(scoreCardGroupUserMapper -> {
					contractNumbers.add(scoreCardGroupUserMapper.getContractNumber());
					contractManagerIds.add(scoreCardGroupUserMapper.getContractManagerId());
					poItems.add(scoreCardGroupUserMapper.getPoItem());
				});
			}

			if (contractNumbers.size() > 0 && contractManagerIds.size() > 0 && poItems.size() > 0) {
				if (status == Constants.ALL_SCORECARD) {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatus(contractNumbers,
								contractManagerIds, poItems, Constants.STATUS_DELETE);
					} else {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatus(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
								Constants.STATUS_DELETE);
					}
				} else {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbers(contractNumbers,
								contractManagerIds, poItems, status);
					} else {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbers(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
								status);
					}
				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

					}
				}
			}

			if (isContractManger && scoreCardsList.size() > 0) {

				scoreCards = Stream.concat(scoreCardsList.stream(), scoreCards.stream()).distinct()
						.collect(Collectors.toList());
			}

			if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {

				for (ScoreCard sc : scoreCards) {

					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());
						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}
				}
			}

			return scoreCards;
		} catch (VedantaException e) {
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<ScoreCard> getUserScoreCardsByStatusAndBusinessUnitIdAndPlantCode(int status, Long businessUnitId,
			String plantCode) {
		if (status < 0) {
			log.info("This is not a valid status");
			throw new VedantaException("This is not a valid status");
		}
		try {
			List<ScoreCard> scoreCards = new ArrayList<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (isCurrentUserGranted() || VedantaUtility.isCurrentUserBusinessUnitHead()) {
				// fetching score-card for role Admin by business unit id, plant
				// code and status
				if (VedantaUtility.isCurrentUserAdmin()) {

					if (status == Constants.ALL_SCORECARD) {
						scoreCards = scoreCardDao.getAllScoreCardNotStatusAndBusinessUnitIdAndPlantCode(
								Constants.STATUS_DELETE, businessUnitId, plantCode);

					} else {
						scoreCards = scoreCardDao.getAllScoreCardByBusinessUnitIdAndPlantCode(status, businessUnitId,
								plantCode);

					}

				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}

				return scoreCards;
			}

			if (isCurrentUserPlantHead()) {

				if (status == Constants.ALL_SCORECARD) {

					scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndNotStatus(businessUnitId, plantCode,
							Constants.STATUS_DELETE);

				} else {

					scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndStatus(businessUnitId, plantCode, status);

				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());
							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}
				return scoreCards;
			}

			Boolean isContractManger = false;
			List<ScoreCard> scoreCardsList = new ArrayList<>();

			if (isCurrentUserContractManager()) {
				if (status == Constants.ALL_SCORECARD) {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatus(vedantaUser.getEmployeeId(),
								Constants.STATUS_DELETE);
					} else {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatus(
								VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(),
								Constants.STATUS_DELETE);
					}

				} else {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatus(vedantaUser.getEmployeeId(),
								status);
					} else {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatus(
								VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(), status);
					}
				}
				if (!ObjectUtils.isEmpty(scoreCardsList) && scoreCardsList.size() > 0) {
					for (ScoreCard sc : scoreCardsList) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));
					}
				}
				isContractManger = true;
			}

			List<ScoreCardGroupUserMapper> scoreCardGroupUserMappers = scoreCardGroupUserDao
					.getDistinctContractNumbersAndManagersByUserId(vedantaUser.getEmployeeId());

			Set<String> contractNumbers = new HashSet<>();
			Set<String> contractManagerIds = new HashSet<>();
			Set<String> poItems = new HashSet<>();

			if (!CollectionUtils.isEmpty(scoreCardGroupUserMappers)) {
				scoreCardGroupUserMappers.forEach(scoreCardGroupUserMapper -> {
					contractNumbers.add(scoreCardGroupUserMapper.getContractNumber());
					contractManagerIds.add(scoreCardGroupUserMapper.getContractManagerId());
					poItems.add(scoreCardGroupUserMapper.getPoItem());
				});
			}

			if (contractNumbers.size() > 0 && contractManagerIds.size() > 0 && poItems.size() > 0) {
				if (status == Constants.ALL_SCORECARD) {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatus(contractNumbers,
								contractManagerIds, poItems, Constants.STATUS_DELETE);
					} else {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatus(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
								Constants.STATUS_DELETE);
					}
				} else {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbers(contractNumbers,
								contractManagerIds, poItems, status);
					} else {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbers(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
								status);
					}
				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

					}
				}
			}

			if (isContractManger && scoreCardsList.size() > 0) {

				scoreCards = Stream.concat(scoreCardsList.stream(), scoreCards.stream()).distinct()
						.collect(Collectors.toList());
			}

			if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {

				for (ScoreCard sc : scoreCards) {

					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());
						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}
					if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
						User user = userDetail.getUserByUserId(sc.getApprovedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setApprovedByName(user.getName());
						}
					}
				}
			}

			return scoreCards;
		} catch (VedantaException e) {
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	// for admin and business unit head and PlantHead
	@Override
	public List<ScoreCard> getUserScoreCardsByStatusAndBusinessUnitId(int status, Long businessUnitId) {
		if (status < 0) {
			log.info("This is not a valid status");
			throw new VedantaException("This is not a valid status");
		}
		try {
			List<ScoreCard> scoreCards = new ArrayList<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (isCurrentUserGranted()) {
				// fetching score-card for role Admin by business unit id and
				// status
				if (VedantaUtility.isCurrentUserAdmin()) {
					if (status == Constants.ALL_SCORECARD) {
						scoreCards = scoreCardDao.getAllScoreCardNotStatusAndBusinessUnitId(Constants.STATUS_DELETE,
								businessUnitId);
					} else {
						scoreCards = scoreCardDao.getAllScoreCardByBusinessUnitId(status, businessUnitId);
					}

				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));
						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());
							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());
							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}
				return scoreCards;
			}

			List<Plant> plantDetails = new ArrayList<Plant>();
			Set<String> plantCode = new HashSet<String>();
			List<PlantHead> plantHeadDetails = plantHeadService.getByEmployeeId(vedantaUser.getEmployeeId());
			for (PlantHead plantHead : plantHeadDetails) {
				plantDetails.addAll(dashboardDao.getPlantByPlantCode(plantHead.getPlantCode()));
			}

			if (!ObjectUtils.isEmpty(plantDetails)) {
				plantDetails.forEach(plantDetail -> {
					plantCode.add(plantDetail.getPlantCode());
				});

			}

			if (VedantaUtility.isCurrentUserBusinessUnitHead()) {
				// fetching score-card for role Admin by business unit id and
				// status
				if (VedantaUtility.isCurrentUserAdmin()) {
					if (status == Constants.ALL_SCORECARD) {
						scoreCards = scoreCardDao.getAllScoreCardForPlantHeadNotStatusAndBusinessUnitIdAndPlantCode(
								Constants.STATUS_DELETE, businessUnitId, plantCode);
					} else {
						scoreCards = scoreCardDao.getAllScoreCardForPlantHeadByBusinessUnitIdAndPlantCode(status,
								businessUnitId, plantCode);
					}

				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));
						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}

			}

			return scoreCards;

		} catch (VedantaException e) {
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<ScoreCard> getUserScoreCardsByDateRange(String fromDate, String toDate, Integer status) {
		if (ObjectUtils.isEmpty(fromDate) || ObjectUtils.isEmpty(toDate)) {
			log.info("This is not a valid status");
			throw new VedantaException("This is not a valid status");
		}
		try {
			String fDate[] = fromDate.split("/");
			int fmonth = Integer.parseInt(fDate[0]) - 1;
			int fyear = Integer.parseInt(fDate[1]);

			String tDate[] = toDate.split("/");
			int tmonth = Integer.parseInt(tDate[0]) - 1;
			int tyear = Integer.parseInt(tDate[1]);

			List<ScoreCard> scoreCards = new ArrayList<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
			if (isCurrentUserGranted()) {

				if (VedantaUtility.isCurrentUserAdmin()) {
					if (status == Constants.ALL_SCORECARD) {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByNotStatusAndDateRange(
									Constants.STATUS_DELETE, fmonth, tmonth, fyear, tyear));
						} else if (VedantaUtility.getCurrentUserBuId() != 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByNotStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), Constants.STATUS_DELETE, fmonth, tmonth, fyear,
									tyear));
						} else {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByNotStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), VedantaUtility.getCurrentUserPlantCode(),
									Constants.STATUS_DELETE, fmonth, tmonth, fyear, tyear));
						}

					} else {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRange(status, fmonth, tmonth,
									fyear, tyear));
						} else if (VedantaUtility.getCurrentUserBuId() != 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), status, fmonth, tmonth, fyear, tyear));
						} else {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), VedantaUtility.getCurrentUserPlantCode(),
									status, fmonth, tmonth, fyear, tyear));
						}

					}
				}

				if (VedantaUtility.isCurrentUserBusinessUnitHead()) {
					if (status == Constants.ALL_SCORECARD) {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByNotStatusAndDateRange(
									Constants.STATUS_DELETE, fmonth, tmonth, fyear, tyear));
						} else {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByNotStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), Constants.STATUS_DELETE, fmonth, tmonth, fyear,
									tyear));
						}
					} else {
						if (VedantaUtility.getCurrentUserBuId() == 0
								&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRange(status, fmonth, tmonth,
									fyear, tyear));
						} else {
							scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), status, fmonth, tmonth, fyear, tyear));
						}

					}
				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

					}
				}

				return scoreCards;
			}

			if (isCurrentUserPlantHead()) {
				String employeeId = vedantaUser.getEmployeeId();
				List<PlantHead> plantHeads = plantHeadDao.findAllByEmployeeIdAndStatus(employeeId,
						Constants.STATUS_ACTIVE);
				Set<String> plantCodes = new HashSet<>();
				for (PlantHead pH : plantHeads) {
					plantCodes.add(pH.getPlantCode());
				}

				if (!ObjectUtils.isEmpty(plantHeads) && plantHeads.size() > 0) {
					if (status == Constants.ALL_SCORECARD) {

						if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndNotStatusAndDateRange(plantCodes,
									Constants.STATUS_DELETE, fmonth, tmonth, fyear, tyear);
						} else {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndNotStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), plantCodes, Constants.STATUS_DELETE, fmonth,
									tmonth, fyear, tyear);
						}
					} else {
						if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndStatusAndDateRange(plantCodes,
									status, fmonth, tmonth, fyear, tyear);
						} else {
							scoreCards = scoreCardDao.getAllScoreCardByPlantCodeAndStatusAndDateRange(
									VedantaUtility.getCurrentUserBuId(), plantCodes, status, fmonth, tmonth, fyear,
									tyear);
						}
					}

					if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
						for (ScoreCard sc : scoreCards) {
							sc.setVendor(vendorService.get(sc.getVendorId()));
							sc.setTemplate(templateService.get(sc.getTemplateId()));

							if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
								User user = userDetail.getUserByUserId(sc.getSubmittedBy());
								if (!ObjectUtils.isEmpty(user)) {
									sc.setSubmittedNameBy(user.getName());
								}
							}

						}
					}
				}

				return scoreCards;
			}

			Boolean isContractManger = false;
			List<ScoreCard> scoreCardsList = new ArrayList<>();

			if (isCurrentUserContractManager()) {
				if (status == Constants.ALL_SCORECARD) {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatusAndDateRange(
								vedantaUser.getEmployeeId(), Constants.STATUS_DELETE, fmonth, tmonth, fyear, tyear);
					} else {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatusAndDateRange(
								VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(),
								Constants.STATUS_DELETE, fmonth, tmonth, fyear, tyear);
					}

				} else {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatusAndDateRange(
								vedantaUser.getEmployeeId(), status, fmonth, tmonth, fyear, tyear);
					} else {
						scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatusAndDateRange(
								VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(), status, fmonth,
								tmonth, fyear, tyear);
					}
				}
				if (!ObjectUtils.isEmpty(scoreCardsList) && scoreCardsList.size() > 0) {
					for (ScoreCard sc : scoreCardsList) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));
					}
				}
				isContractManger = true;
			}

			List<ScoreCardGroupUserMapper> scoreCardGroupUserMappers = scoreCardGroupUserDao
					.getDistinctContractNumbersAndManagersByUserId(vedantaUser.getEmployeeId());

			Set<String> contractNumbers = new HashSet<>();
			Set<String> contractManagerIds = new HashSet<>();
			Set<String> poItems = new HashSet<>();

			if (!CollectionUtils.isEmpty(scoreCardGroupUserMappers)) {
				scoreCardGroupUserMappers.forEach(scoreCardGroupUserMapper -> {
					contractNumbers.add(scoreCardGroupUserMapper.getContractNumber());
					contractManagerIds.add(scoreCardGroupUserMapper.getContractManagerId());
					poItems.add(scoreCardGroupUserMapper.getPoItem());
				});
			}

			if (contractNumbers.size() > 0 && contractManagerIds.size() > 0 && poItems.size() > 0) {
				if (status == Constants.ALL_SCORECARD) {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(
								contractNumbers, contractManagerIds, poItems, Constants.STATUS_DELETE, fmonth, tmonth,
								fyear, tyear);
					} else {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
								Constants.STATUS_DELETE, fmonth, tmonth, fyear, tyear);
					}
				} else {

					if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersAndDateRange(contractNumbers,
								contractManagerIds, poItems, status, fmonth, tmonth, fyear, tyear);
					} else {
						scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersAndDateRange(
								VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
								status, fmonth, tmonth, fyear, tyear);
					}
				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

					}
				}
			}

			if (isContractManger && scoreCardsList.size() > 0) {
				scoreCards = Stream.concat(scoreCardsList.stream(), scoreCards.stream()).distinct()
						.collect(Collectors.toList());
			}

			if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
				for (ScoreCard sc : scoreCards) {
					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());
						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}
				}
			}
			return scoreCards;
		} catch (VedantaException e) {
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<ScoreCard> getUserScoreCardsByDateRangeAndBusinessUnitIdAndPlantCode(String fromDate, String toDate,
			Integer status, Long businessUnitId, String plantCode) {
		if (ObjectUtils.isEmpty(fromDate) || ObjectUtils.isEmpty(toDate)) {
			log.info("This is not a valid status");
			throw new VedantaException("This is not a valid status");
		}
		try {

			String fDate[] = fromDate.split("/");
			int fmonth = Integer.parseInt(fDate[0]) - 1;
			int fyear = Integer.parseInt(fDate[1]);

			String tDate[] = toDate.split("/");
			int tmonth = Integer.parseInt(tDate[0]) - 1;
			int tyear = Integer.parseInt(tDate[1]);

			String startDate = fyear + "-" + fmonth + "-01";
			String endDate = tyear + "-" + tmonth + "-01";

			List<ScoreCard> scoreCards = new ArrayList<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (VedantaUtility.isCurrentUserAdmin() || VedantaUtility.isCurrentUserBusinessUnitHead()) {
				if (status == Constants.ALL_SCORECARD) {
					if (plantCode.equals(String.valueOf(0))) {
						scoreCards.addAll(scoreCardDao.getAllScoreCardByNotStatusAndDateRangeAndBusinessUnitId(
								Constants.STATUS_DELETE, startDate, endDate, businessUnitId));
					} else {
						scoreCards.addAll(
								scoreCardDao.getAllScoreCardByNotStatusAndDateRangeAndBusinessUnitIdAndPlantCode(
										Constants.STATUS_DELETE, startDate, endDate, businessUnitId, plantCode));
					}
				} else {
					if (plantCode.equals(String.valueOf(0))) {
						scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRangeAndBusinessUnitId(status,
								startDate, endDate, businessUnitId));

					} else {
						scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRangeAndBusinessUnitIdAndPlantCode(
								status, startDate, endDate, businessUnitId, plantCode));
					}

				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}
				return scoreCards;
			}

			if (isCurrentUserPlantHead()) {
				List<Plant> plantDetails = new ArrayList<Plant>();
				Set<String> plantCodes = new HashSet<String>();
				List<PlantHead> plantHeadDetails = plantHeadService.getByEmployeeId(vedantaUser.getEmployeeId());
				for (PlantHead plantHead : plantHeadDetails) {
					plantDetails.addAll(dashboardDao.getPlantByPlantCode(plantHead.getPlantCode()));
				}

				if (!ObjectUtils.isEmpty(plantDetails)) {
					plantDetails.forEach(plantDetail -> {
						plantCodes.add(plantDetail.getPlantCode());
					});

				}

				if (status == Constants.ALL_SCORECARD) {
					scoreCards.addAll(scoreCardDao
							.getAllScoreCardForPlantHeadByNotStatusAndDateRangeAndBusinessUnitIdAndPlantCode(
									Constants.STATUS_DELETE, startDate, endDate, businessUnitId, plantCodes));

				} else {
					if (plantCode.equals(String.valueOf(0))) {
						scoreCards.addAll(scoreCardDao
								.getAllScoreCardForPlantHeadByStatusAndDateRangeAndBusinessUnitIdAndPlantCode(status,
										startDate, endDate, businessUnitId, plantCodes));

					} else {
						scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRangeAndBusinessUnitIdAndPlantCode(
								status, startDate, endDate, businessUnitId, plantCode));
					}

				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}

				return scoreCards;
			}

			Boolean isContractManger = false;
			List<ScoreCard> scoreCardsList = new ArrayList<>();

			if (isCurrentUserContractManager()) {
				if (status == Constants.ALL_SCORECARD) {
					scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatusAndDateRange(
							VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(), Constants.STATUS_DELETE,
							startDate, endDate);

				} else {
					scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatusAndDateRange(
							VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(), status, startDate,
							endDate);
				}
				if (!ObjectUtils.isEmpty(scoreCardsList) && scoreCardsList.size() > 0) {
					for (ScoreCard sc : scoreCardsList) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));
					}
				}
				isContractManger = true;
			}

			List<ScoreCardGroupUserMapper> scoreCardGroupUserMappers = scoreCardGroupUserDao
					.getDistinctContractNumbersAndManagersByUserId(vedantaUser.getEmployeeId());

			Set<String> contractNumbers = new HashSet<>();
			Set<String> contractManagerIds = new HashSet<>();
			Set<String> poItems = new HashSet<>();

			if (!CollectionUtils.isEmpty(scoreCardGroupUserMappers)) {
				scoreCardGroupUserMappers.forEach(scoreCardGroupUserMapper -> {
					contractNumbers.add(scoreCardGroupUserMapper.getContractNumber());
					contractManagerIds.add(scoreCardGroupUserMapper.getContractManagerId());
					poItems.add(scoreCardGroupUserMapper.getPoItem());
				});
			}

			if (contractNumbers.size() > 0 && contractManagerIds.size() > 0 && poItems.size() > 0) {
				if (status == Constants.ALL_SCORECARD) {
					scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatusAndDateRange(
							VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
							Constants.STATUS_DELETE, startDate, endDate);

				} else {
					scoreCards = scoreCardDao.getScoreCardsByManagersAndContractNumbersAndDateRange(
							VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems, status,
							startDate, endDate);
				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

					}
				}
			}

			if (isContractManger && scoreCardsList.size() > 0) {

				scoreCards = Stream.concat(scoreCardsList.stream(), scoreCards.stream()).distinct()
						.collect(Collectors.toList());
			}

			if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {

				for (ScoreCard sc : scoreCards) {

					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());
						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}

					if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
						User user = userDetail.getUserByUserId(sc.getApprovedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setApprovedByName(user.getName());
						}
					}
				}
			}
			return scoreCards;
		} catch (VedantaException e) {
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	// for admin , business unit head and plant head
	@Override
	public List<ScoreCard> getUserScoreCardsByDateRangeAndBusinessUnitId(String fromDate, String toDate, Integer status,
			Long businessUnitId) {
		if (ObjectUtils.isEmpty(fromDate) || ObjectUtils.isEmpty(toDate)) {
			log.info("This is not a valid status");
			throw new VedantaException("This is not a valid status");
		}
		try {

			String fDate[] = fromDate.split("/");
			int fmonth = Integer.parseInt(fDate[0]) - 1;
			int fyear = Integer.parseInt(fDate[1]);

			String tDate[] = toDate.split("/");
			int tmonth = Integer.parseInt(tDate[0]) - 1;
			int tyear = Integer.parseInt(tDate[1]);

			String startDate = fyear + "-" + fmonth + "-01";
			String endDate = tyear + "-" + tmonth + "-01";

			List<ScoreCard> scoreCards = new ArrayList<>();
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (VedantaUtility.isCurrentUserAdmin() || VedantaUtility.isCurrentUserBusinessUnitHead()) {
				if (status == Constants.ALL_SCORECARD) {
					scoreCards.addAll(scoreCardDao.getAllScoreCardByNotStatusAndDateRangeAndBusinessUnitId(
							Constants.STATUS_DELETE, startDate, endDate, businessUnitId));
				} else {
					scoreCards.addAll(scoreCardDao.getAllScoreCardByStatusAndDateRangeAndBusinessUnitId(status,
							startDate, endDate, businessUnitId));
				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}
				return scoreCards;
			}

			List<Plant> plantDetails = new ArrayList<Plant>();
			Set<String> plantCode = new HashSet<String>();

			List<PlantHead> plantHeadDetails = plantHeadService.getByEmployeeId(vedantaUser.getEmployeeId());
			for (PlantHead plantHead : plantHeadDetails) {
				plantDetails.addAll(dashboardDao.getPlantByPlantCode(plantHead.getPlantCode()));
			}

			if (!ObjectUtils.isEmpty(plantDetails)) {
				plantDetails.forEach(plantDetail -> {
					plantCode.add(plantDetail.getPlantCode());
				});

			}

			// Plant head
			if (VedantaUtility.isCurrentUserPlantHead()) {
				if (status == Constants.ALL_SCORECARD) {
					scoreCards.addAll(scoreCardDao
							.getAllScoreCardForPlantHeadByNotStatusAndDateRangeAndBusinessUnitIdAndPlantCode(
									Constants.STATUS_DELETE, startDate, endDate, businessUnitId, plantCode));
				} else {
					scoreCards.addAll(
							scoreCardDao.getAllScoreCardForPlantHeadByStatusAndDateRangeAndBusinessUnitIdAndPlantCode(
									status, startDate, endDate, businessUnitId, plantCode));
				}

				if (!ObjectUtils.isEmpty(scoreCards) && scoreCards.size() > 0) {
					for (ScoreCard sc : scoreCards) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());
							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}

			}

			return scoreCards;
		} catch (VedantaException e) {
			String msg = "Error while getting Contracts for User";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private Map<String, Date> getDateRange(String fromDate, String toDate) {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
			try {
				fromDate = "01/" + fromDate;
				String toDateNew = "01/" + toDate;
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateFormat.parse(toDateNew));
				toDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + toDate;

				Map<String, Date> dates = new HashMap<>();
				dates.put("fromDate", dateFormat.parse(fromDate));
				dates.put("toDate", dateFormat.parse(toDate));

				return dates;
			} catch (ParseException ee) {
				log.info("This is not a valid date");
				throw new VedantaException("This is not a valid date");
			}
		}
		return null;
	}

	@Override
	public List<ScoreCard> getAllScorecard() {

		try {
			List<ScoreCard> scorecard = (List<ScoreCard>) scoreCardDao.findAll();
			return scorecard;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public List<ScoreCard> getAllScoreCardByTemplateIdAndMonthIdAndYearIdAndStatus(Long templateId, int monthId,
			int yearId, int status, Long businessId) {

		if (templateId <= 0 || monthId < 0 || yearId < 0 || status < 0) {
			log.info("Invalid scorecard id or monthId or yearId or status.");
			throw new VedantaException("Invalid scorecard id or monthId or yearId or status.");
		}
		try {
			return scoreCardDao.getAllScoreCardByTemplateIdAndMonthAndYearAndStatus(templateId, monthId, yearId, status,
					businessId);
		} catch (VedantaException e) {
			String msg = "Error while fetching all scorecards by templateId and monthId and yearId and status";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private List<DashboardDataMapper> getDashBoardDataMapperList() {

		List<DashboardDataMapper> dashboardDataMappers = new ArrayList<DashboardDataMapper>();

		if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
			dashboardDataMappers = scoreCardDao.getDashboardData();
		} else {
			dashboardDataMappers = scoreCardDao.getDashboardData(VedantaUtility.getCurrentUserBuId());
		}
		return dashboardDataMappers;
	}

	@Override
	public Map<String, Object> getDashboardData() {
		try {
			Map<String, Object> dashboardData = new HashMap<>();
			List<DashboardDataMapper> dashboardDataMappers = this.getDashBoardDataMapperList();

			if (ObjectUtils.isEmpty(dashboardDataMappers) || dashboardDataMappers.size() <= 0) {
				dashboardData.put(Constants.NEW_DASHBOARD_DETAILS, new ArrayList<DashboardDataMapper>());
			} else {

				/* Plants Grouping */
				Set<Long> plants = new HashSet<>();
				dashboardDataMappers.forEach(dashboardDataMapper -> {
					plants.add(dashboardDataMapper.getPlantId());
				});

				/* Catagory Grouping */
				Map<Long, Set<String>> catgry = new HashMap<>();
				plants.forEach(plant -> {
					Set<String> cats = new HashSet<>();
					dashboardDataMappers.forEach(dashboardDataMapper -> {
						if (dashboardDataMapper.getPlantId() == plant) {
							cats.add(dashboardDataMapper.getCategoryName());
						}
					});
					catgry.put(plant, cats);
				});

				Map<Long, Map<String, Set<String>>> subCatgory = new HashMap<>();
				/* Sub CatagoryGrouping */
				catgry.forEach((plant, catList) -> {
					Map<String, Set<String>> catSubcat = new HashMap<>();
					catList.forEach(cat -> {

						Set<String> subCatList = new HashSet<>();
						dashboardDataMappers.forEach(dashboardDataMapper -> {
							if (dashboardDataMapper.getPlantId() == plant
									&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(cat)) {
								subCatList.add(dashboardDataMapper.getSubCategoryName());
							}
						});

						catSubcat.put(cat, subCatList);
					});

					subCatgory.put(plant, catSubcat);
				});
				Map<Long, Map<String, Map<String, Set<String>>>> contractMap = new HashMap<>();
				/* Contract Grouping */
				subCatgory.forEach((plant, catMap) -> {
					Map<String, Map<String, Set<String>>> catSubcatMap = new HashMap<>();
					catMap.forEach((cat, subCatList) -> {
						Map<String, Set<String>> subcatContract = new HashMap<>();
						subCatList.forEach(subCat -> {
							Set<String> contractList = new HashSet<>();
							dashboardDataMappers.forEach(dashboardDataMapper -> {
								if (dashboardDataMapper.getPlantId() == plant
										&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(cat)
										&& dashboardDataMapper.getSubCategoryName().equalsIgnoreCase(subCat)) {
									contractList.add(dashboardDataMapper.getContractNumber());
								}
							});
							subcatContract.put(subCat, contractList);
						});
						catSubcatMap.put(cat, subcatContract);
					});
					contractMap.put(plant, catSubcatMap);
				});

				Map<Long, Map<String, Map<String, Map<String, Set<DashboardDataMapper>>>>> dashboardMap = new HashMap<>();
				/* Final Data Grouping */
				contractMap.forEach((plant, categoryMap) -> {
					Map<String, Map<String, Map<String, Set<DashboardDataMapper>>>> categorySubcategoryMap = new HashMap<>();
					categoryMap.forEach((catogry, subCategoryMap) -> {
						Map<String, Map<String, Set<DashboardDataMapper>>> subCategoryContractMap = new HashMap<>();
						subCategoryMap.forEach((subCategory, contractList) -> {
							Map<String, Set<DashboardDataMapper>> contractDashboardMap = new HashMap<>();
							contractList.forEach(contract -> {
								Set<DashboardDataMapper> dashboardList = new HashSet<>();
								dashboardDataMappers.forEach(dashboardDataMapper -> {
									if (dashboardDataMapper.getPlantId() == plant
											&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(catogry)
											&& dashboardDataMapper.getSubCategoryName().equalsIgnoreCase(subCategory)
											&& dashboardDataMapper.getContractNumber().equalsIgnoreCase(contract)) {
										dashboardList.add(dashboardDataMapper);
									}
								});
								contractDashboardMap.put(contract, dashboardList);
							});
							subCategoryContractMap.put(subCategory, contractDashboardMap);
						});
						categorySubcategoryMap.put(catogry, subCategoryContractMap);
					});
					dashboardMap.put(plant, categorySubcategoryMap);
				});

				Map<String, Map<String, Map<String, Map<String, Set<DashboardDataMapper>>>>> dashboardDataMap = new HashMap<>();

				dashboardMap.forEach((plantId, value) -> {
					dashboardDataMap.put(plantService.get(plantId).getName(), value);
				});

				dashboardData.put(Constants.NEW_DASHBOARD_DETAILS, dashboardDataMap);
			}

			dashboardData.put(Constants.MONTH_LIST, getAllPreviousMonths());
			dashboardData.put(Constants.ALL_PLANTS, plantService.getAllPlants());
			dashboardData.put(Constants.ALL_CATEGORIES, categoryService.getAllCategories());
			dashboardData.put(Constants.ALL_SUBCATEGORIES, subCategoryService.getAllCategories());
			dashboardData.put(Constants.ALL_CONTRACTS, contractService.getAllContracts());

			return dashboardData;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private Map<Integer, Map<String, Object>> getAllPreviousMonths() {

		int year = DateUtil.getYear();
		int month = DateUtil.getMonthOfYear();

		Map<Integer, Map<String, Object>> data = new HashMap<>();

		Integer key = 11;
		for (int monthId = month; key > -1; monthId--) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (monthId == 0) {
				map.put("monthId", monthId);
				map.put("yearId", year);
				monthId = 12;
				year--;

			} else {
				map.put("monthId", monthId);
				map.put("yearId", year);
			}
			data.put(key, map);
			key--;
		}

		return data;
	}

	@Override
	public Map<String, Object> getDashboardData(Long searchPlantId, Long searchCategoryId, Long searchSubCategoryId,
			Long searchContractId, String fromDate, String toDate) {
		try {
			Map<String, Object> dashboardData = new HashMap<>();
			Set<ScorecardAggregation> allContracts = new HashSet<>();
			Map<String, Date> dateRange = null;
			if (!ObjectUtils.isEmpty(fromDate) && !ObjectUtils.isEmpty(toDate)) {
				dateRange = getDateRange(fromDate, toDate);
			} else {
				dateRange = null;
			}

			if (!ObjectUtils.isEmpty(searchSubCategoryId)) {

				allContracts = scorecardDaoBySubcategory.getScoreCardBySubCategoryIdId(searchSubCategoryId);
			}

			List<ScorecardAggregation> dashboardDataMappers = getQueryDataForDashboard(searchPlantId, searchCategoryId,
					searchSubCategoryId, searchContractId, dateRange);

			if (ObjectUtils.isEmpty(dashboardDataMappers) || dashboardDataMappers.size() <= 0) {
				dashboardData.put(Constants.NEW_DASHBOARD_DETAILS, new ArrayList<ScorecardAggregation>());
			} else {
				/* Plants Grouping */
				Set<Long> plants = new HashSet<>();
				dashboardDataMappers.forEach(dashboardDataMapper -> {
					plants.add(dashboardDataMapper.getPlantId());
				});

				/* Catagory Grouping */
				Map<Long, Set<String>> catgry = new HashMap<>();
				plants.forEach(plant -> {
					Set<String> cats = new HashSet<>();
					dashboardDataMappers.forEach(dashboardDataMapper -> {
						if (dashboardDataMapper.getPlantId() == plant) {
							cats.add(dashboardDataMapper.getCategoryName());
						}
					});
					catgry.put(plant, cats);
				});

				Map<Long, Map<String, Set<String>>> subCatgory = new HashMap<>();
				/* Sub CatagoryGrouping */
				catgry.forEach((plant, catList) -> {
					Map<String, Set<String>> catSubcat = new HashMap<>();
					catList.forEach(cat -> {
						Set<String> subCatList = new HashSet<>();
						dashboardDataMappers.forEach(dashboardDataMapper -> {
							if (dashboardDataMapper.getPlantId() == plant
									&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(cat)) {
								subCatList.add(dashboardDataMapper.getSubCategoryName());
							}
						});
						catSubcat.put(cat, subCatList);
					});

					subCatgory.put(plant, catSubcat);
				});

				Map<Long, Map<String, Map<String, Set<String>>>> contractMap = new HashMap<>();
				/* Contract Grouping */
				subCatgory.forEach((plant, catMap) -> {
					Map<String, Map<String, Set<String>>> catSubcatMap = new HashMap<>();
					catMap.forEach((cat, subCatList) -> {
						Map<String, Set<String>> subcatContract = new HashMap<>();
						subCatList.forEach(subCat -> {
							Set<String> contractList = new HashSet<>();
							dashboardDataMappers.forEach(dashboardDataMapper -> {
								if (dashboardDataMapper.getPlantId() == plant
										&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(cat)
										&& dashboardDataMapper.getSubCategoryName().equalsIgnoreCase(subCat)) {
									contractList.add(dashboardDataMapper.getContractNumber());
								}
							});
							subcatContract.put(subCat, contractList);
						});
						catSubcatMap.put(cat, subcatContract);
					});
					contractMap.put(plant, catSubcatMap);
				});

				Map<Long, Map<String, Map<String, Map<String, Set<ScorecardAggregation>>>>> dashboardMap = new HashMap<>();
				/* Final Data Grouping */
				contractMap.forEach((plant, categoryMap) -> {
					Map<String, Map<String, Map<String, Set<ScorecardAggregation>>>> categorySubcategoryMap = new HashMap<>();
					categoryMap.forEach((catogry, subCategoryMap) -> {
						Map<String, Map<String, Set<ScorecardAggregation>>> subCategoryContractMap = new HashMap<>();
						subCategoryMap.forEach((subCategory, contractList) -> {
							Map<String, Set<ScorecardAggregation>> contractDashboardMap = new HashMap<>();
							contractList.forEach(contract -> {
								Set<ScorecardAggregation> dashboardList = new HashSet<>();
								dashboardDataMappers.forEach(dashboardDataMapper -> {
									if (dashboardDataMapper.getPlantId() == plant
											&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(catogry)
											&& dashboardDataMapper.getSubCategoryName().equalsIgnoreCase(subCategory)
											&& dashboardDataMapper.getContractNumber().equalsIgnoreCase(contract)) {
										dashboardList.add(dashboardDataMapper);
									}
								});
								contractDashboardMap.put(contract, dashboardList);
							});
							subCategoryContractMap.put(subCategory, contractDashboardMap);
						});
						categorySubcategoryMap.put(catogry, subCategoryContractMap);
					});
					dashboardMap.put(plant, categorySubcategoryMap);
				});

				Map<String, Map<String, Map<String, Map<String, Set<ScorecardAggregation>>>>> dashboardDataMap = new HashMap<>();

				dashboardMap.forEach((plantId, value) -> {
					dashboardDataMap.put(plantService.get(plantId).getName(), value);
				});
				dashboardData.put(Constants.NEW_DASHBOARD_DETAILS, dashboardDataMap);
			}

			dashboardData.put(Constants.MONTH_LIST, getAllPreviousMonths());

			if (VedantaUtility.isCurrentUserAdmin()) {
				dashboardData.put(Constants.ALL_PLANTS, plantService.getAllPlants());
				dashboardData.put(Constants.ALL_CATEGORIES, categoryService.getAllCategories());
				dashboardData.put(Constants.ALL_SUBCATEGORIES, subCategoryService.getAllCategories());
				dashboardData.put(Constants.ALL_CONTRACTS, allContracts);
			}
			if (VedantaUtility.isCurrentUserBusinessUnitHead()) {

				dashboardData.put(Constants.ALL_PLANTS, plantService.getAllPlantsByCurrentBusinessUnitId());
				dashboardData.put(Constants.ALL_CATEGORIES, categoryService.getAllCategories());
				dashboardData.put(Constants.ALL_SUBCATEGORIES, subCategoryService.getAllCategories());
				dashboardData.put(Constants.ALL_CONTRACTS, allContracts);
			}

			if (VedantaUtility.isCurrentUserPlantHead()) {

				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
				User user = userDetailServiceImpl.getUserByUserId(vedantaUser.getId());

				List<Plant> plants = new ArrayList<>();
				if (!ObjectUtils.isEmpty(user)) {
					plants = plantService.getAllPlantsByCurrentUser();
					dashboardData.put(Constants.ALL_PLANTS, plants);

					Set<Long> plantIds = new HashSet<>();
					for (Plant plant : plants) {
						plantIds.add(plant.getId());
					}
					List<Contract> contracts = new ArrayList<>();
					if (!ObjectUtils.isEmpty(plantIds)) {
						dashboardData.put(Constants.ALL_CONTRACTS, contractService.getAllContractsByPlantIds(plantIds));
					} else {
						dashboardData.put(Constants.ALL_CONTRACTS, allContracts);
					}

				} else {
					dashboardData.put(Constants.ALL_PLANTS, plants);
				}
				dashboardData.put(Constants.ALL_CATEGORIES, categoryService.getAllCategories());
				dashboardData.put(Constants.ALL_SUBCATEGORIES, subCategoryService.getAllCategories());
			}

			return dashboardData;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private List<ScorecardAggregation> getQueryDataForDashboard(Long plantId, Long categoryId, Long subCategoryId,
			Long contractId, Map<String, Date> dateRangeMap) {

		List<ScorecardAggregation> dashboardDataMappers = null;

		int fmonth = 0;
		int fyear = 0;
		int tmonth = 0;
		int tyear = 0;

		if (dateRangeMap != null) {

			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

			// for from dateRange
			Date fromdate = dateRangeMap.get("fromDate");

			SimpleDateFormat mdyFormat = new SimpleDateFormat("MM-dd-yyyy");
			String fdateDetails = mdyFormat.format(fromdate);

			// for to dateRange
			Date todate = dateRangeMap.get("toDate");
			SimpleDateFormat mdyFormat1 = new SimpleDateFormat("MM-dd-yyyy");
			String todateDetails = mdyFormat1.format(todate);

			String fDate[] = fdateDetails.split("-");
			fmonth = Integer.parseInt(fDate[0]);
			fyear = Integer.parseInt(fDate[2]);

			String tDate[] = todateDetails.split("-");
			tmonth = Integer.parseInt(tDate[0]);
			tyear = Integer.parseInt(tDate[2]);
		}

		if (ObjectUtils.isEmpty(dateRangeMap)) {
			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& !ObjectUtils.isEmpty(subCategoryId) && subCategoryId > 0 && !ObjectUtils.isEmpty(contractId)
					&& contractId > 0) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractId(plantId, categoryId,
									subCategoryId, contractId);
				} else {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractId(
									VedantaUtility.getCurrentUserBuId(), plantId, categoryId, subCategoryId,
									contractId);
				}

				return dashboardDataMappers;
			}

			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && (ObjectUtils.isEmpty(categoryId) || categoryId <= 0)
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0)
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantId(plantId);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantId(VedantaUtility.getCurrentUserBuId(),
							plantId);
				}

				return dashboardDataMappers;
			}

			if ((ObjectUtils.isEmpty(plantId) || plantId <= 0) && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0)
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByCategoryId(categoryId);
				} else {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByCategoryId(VedantaUtility.getCurrentUserBuId(), categoryId);
				}

				return dashboardDataMappers;
			}

			if ((ObjectUtils.isEmpty(plantId) || plantId <= 0) && (ObjectUtils.isEmpty(categoryId) || categoryId <= 0)
					&& !ObjectUtils.isEmpty(subCategoryId) && subCategoryId > 0
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataBySubCategoryId(subCategoryId);
				} else {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataBySubCategoryId(VedantaUtility.getCurrentUserBuId(), subCategoryId);
				}
				return dashboardDataMappers;
			}

			if ((ObjectUtils.isEmpty(plantId) || plantId <= 0) && (ObjectUtils.isEmpty(categoryId) || categoryId <= 0)
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0) && !ObjectUtils.isEmpty(contractId)
					&& contractId > 0) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByContractId(contractId);
				} else {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByContractId(VedantaUtility.getCurrentUserBuId(), contractId);
				}

				return dashboardDataMappers;
			}

			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0)
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdAndByCategoryId(plantId, categoryId);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdAndByCategoryId(
							VedantaUtility.getCurrentUserBuId(), plantId, categoryId);
				}
				return dashboardDataMappers;
			}

			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& !ObjectUtils.isEmpty(subCategoryId) && subCategoryId > 0
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryId(
							plantId, categoryId, subCategoryId);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryId(
							VedantaUtility.getCurrentUserBuId(), plantId, categoryId, subCategoryId);
				}

				return dashboardDataMappers;
			}
		} else {
			// dateRange
			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& !ObjectUtils.isEmpty(subCategoryId) && subCategoryId > 0 && !ObjectUtils.isEmpty(contractId)
					&& contractId > 0) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractIdByDateRange(plantId,
									categoryId, subCategoryId, contractId, fmonth, tmonth, fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByPlantIdByCategoryIdBySubcategoryIdByContractIdByDateRange(
									VedantaUtility.getCurrentUserBuId(), plantId, categoryId, subCategoryId, contractId,
									fmonth, tmonth, fyear, tyear);
				}

				return dashboardDataMappers;
			}

			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && (ObjectUtils.isEmpty(categoryId) || categoryId <= 0)
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0)
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdByDateRange(plantId, fmonth, tmonth,
							fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdByDateRange(
							VedantaUtility.getCurrentUserBuId(), plantId, fmonth, tmonth, fyear, tyear);
				}
				return dashboardDataMappers;
			}

			if ((ObjectUtils.isEmpty(plantId) || plantId <= 0) && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0)
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByCategoryIdByDateRange(categoryId, fmonth,
							tmonth, fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByCategoryIdByDateRange(
							VedantaUtility.getCurrentUserBuId(), categoryId, fmonth, tmonth, fyear, tyear);
				}
				return dashboardDataMappers;
			}

			if ((ObjectUtils.isEmpty(plantId) || plantId <= 0) && (ObjectUtils.isEmpty(categoryId) || categoryId <= 0)
					&& !ObjectUtils.isEmpty(subCategoryId) && subCategoryId > 0
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataBySubCategoryIdByDateRange(subCategoryId,
							fmonth, tmonth, fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataBySubCategoryIdByDateRange(
							VedantaUtility.getCurrentUserBuId(), subCategoryId, fmonth, tmonth, fyear, tyear);
				}
				return dashboardDataMappers;
			}

			if ((ObjectUtils.isEmpty(plantId) || plantId <= 0) && (ObjectUtils.isEmpty(categoryId) || categoryId <= 0)
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0) && !ObjectUtils.isEmpty(contractId)
					&& contractId > 0) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByContractIdByDateRange(contractId, fmonth,
							tmonth, fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByContractIdByDateRange(
							VedantaUtility.getCurrentUserBuId(), contractId, fmonth, tmonth, fyear, tyear);
				}

				return dashboardDataMappers;
			}

			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0)
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdAndByCategoryIdByDateRange(plantId,
							categoryId, fmonth, tmonth, fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByPlantIdAndByCategoryIdByDateRange(
							VedantaUtility.getCurrentUserBuId(), plantId, categoryId, fmonth, tmonth, fyear, tyear);
				}
				return dashboardDataMappers;
			}

			if (!ObjectUtils.isEmpty(plantId) && plantId > 0 && !ObjectUtils.isEmpty(categoryId) && categoryId > 0
					&& !ObjectUtils.isEmpty(subCategoryId) && subCategoryId > 0
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryIdByDateRange(plantId, categoryId,
									subCategoryId, fmonth, tmonth, fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao
							.getDashboardDataByPlantIdAndByCategoryIdAndBySubcategoryIdByDateRange(
									VedantaUtility.getCurrentUserBuId(), plantId, categoryId, subCategoryId, fmonth,
									tmonth, fyear, tyear);
				}
				return dashboardDataMappers;
			}

			if ((ObjectUtils.isEmpty(plantId) || plantId <= 0) && (ObjectUtils.isEmpty(categoryId) || categoryId <= 0)
					&& (ObjectUtils.isEmpty(subCategoryId) || subCategoryId <= 0)
					&& (ObjectUtils.isEmpty(contractId) || contractId <= 0)) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					dashboardDataMappers = scoreCardDao.getDashboardDataByDateRange(fmonth, tmonth, fyear, tyear);
				} else {
					dashboardDataMappers = scoreCardDao.getDashboardDataByDateRange(VedantaUtility.getCurrentUserBuId(),
							fmonth, tmonth, fyear, tyear);
				}
				return dashboardDataMappers;
			}
		}

		return null;
	}

	@Override
	public ScoreCardAssign scoreCardAssign(ScoreCardAssign scoreCardAssign) {

		if (ObjectUtils.isEmpty(scoreCardAssign)) {

			log.error("Error while assigning scorecard");
			throw new VedantaException("Error while assigning scorecard");
		}

		try {

			if (StringUtils.isEmpty(scoreCardAssign.getScorecardId())) {
				log.error("Error while assigning scorecard");
				throw new VedantaException("Error while assigning scorecard");
			}

			ScoreCard scoreCard = this.get(Long.parseLong(scoreCardAssign.getScorecardId()));

			User userDetails = userDetail.getUserByEmployeeId(scoreCardAssign.getEmployeeId());

			if (ObjectUtils.isEmpty(userDetails)) {
				log.error("Error while assigning scorecard");
				throw new VedantaException("Error while assigning scorecard");
			}
			int currentWeight = scoreCard.getWeight();
			scoreCard.setWeight(Integer.parseInt(scoreCardAssign.getWeight()));
			ScoreCard newscoreCard = makeScoreCard(scoreCard, userDetails.getEmployeeId(), userDetails.getName());
			newscoreCard = scoreCardDao.save(newscoreCard);

			scoreCardAssign.setScorecardId(newscoreCard.getId().toString());

			if (ObjectUtils.isEmpty(newscoreCard) || StringUtils.isEmpty(scoreCardAssign.getEmployeeId())) {
				log.error("Error while assigning scorecard");
				throw new VedantaException("Error while assigning scorecard");
			}

			int newWeight = Integer.parseInt(scoreCardAssign.getWeight());
			int weightage = currentWeight - newWeight;
			scoreCard.setWeight(weightage);
			scoreCardDao.save(scoreCard);

			ScoreCardGroupUser scoreCardGroupUser = makeScoreCardGroupUser(newscoreCard,
					scoreCardAssign.getEmployeeId());
			scoreCardGroupUser = saveScoreCardGroupUser(scoreCardGroupUser);

			scoreCardAssign.setScoreCardGroupUserId(scoreCardGroupUser.getId().toString());
			try {
				notificationService.saveNotification(scoreCardGroupUser);
			} catch (VedantaException e) {
				log.error("Error while creating notification for assigning multiple scorecard." + e.getMessage());
			}

			return scoreCardAssign;
		} catch (VedantaException e) {
			log.error("Error while assigning scorecard");
			throw new VedantaException("Error while assigning scorecard");
		}

	}

	private ScoreCard makeScoreCard(ScoreCard scoreCard, String employeeId, String employeeName) {
		ScoreCard newScoreCard = new ScoreCard();
		newScoreCard.setBusinessUnitId(scoreCard.getBusinessUnitId());
		newScoreCard.setCategoryId(scoreCard.getCategoryId());
		newScoreCard.setCategoryName(scoreCard.getCategoryName());
		newScoreCard.setContractId(scoreCard.getContractId());
		newScoreCard.setContractManagerId(employeeId);
		newScoreCard.setContractManagerName(employeeName);
		newScoreCard.setContractNumber(scoreCard.getContractNumber());
		newScoreCard.setDepartmentCode(scoreCard.getDepartmentCode());
		newScoreCard.setDepartmentId(scoreCard.getDepartmentId());
		newScoreCard.setDueDate(scoreCard.getDueDate());
		newScoreCard.setMonthId(scoreCard.getMonthId());
		newScoreCard.setPlantCode(scoreCard.getPlantCode());
		newScoreCard.setPlantId(scoreCard.getPlantId());
		newScoreCard.setPoItem(scoreCard.getPoItem());
		newScoreCard.setPoItemDescription(scoreCard.getPoItemDescription());
		newScoreCard.setStatus(0);
		newScoreCard.setSubCategoryId(scoreCard.getSubCategoryId());
		newScoreCard.setSubCategoryName(scoreCard.getSubCategoryName());
		newScoreCard.setTargetScore(scoreCard.getTargetScore());
		newScoreCard.setTemplateId(scoreCard.getTemplateId());
		newScoreCard.setTemplateName(scoreCard.getTemplateName());
		newScoreCard.setTotalScore(scoreCard.getTotalScore());
		newScoreCard.setVendorId(scoreCard.getVendorId());
		newScoreCard.setVendorCode(scoreCard.getVendorCode());
		newScoreCard.setWeight(scoreCard.getWeight());
		newScoreCard.setYearId(scoreCard.getYearId());
		return newScoreCard;
	}

	private ScoreCardGroupUser makeScoreCardGroupUser(ScoreCard scoreCard, String employeeId) {

		ScoreCardGroupUser scoreCardGroupUser = new ScoreCardGroupUser();

		scoreCardGroupUser.setCategoryId(scoreCard.getCategoryId());
		scoreCardGroupUser.setCategoryName(scoreCard.getCategoryName());
		scoreCardGroupUser.setContractId(scoreCard.getContractId());
		scoreCardGroupUser.setContractManager(scoreCard.getContractManagerName());
		scoreCardGroupUser.setContractManagerId(scoreCard.getContractManagerId());
		scoreCardGroupUser.setContractNumber(scoreCard.getContractNumber());
		scoreCardGroupUser.setDepartmentCode(scoreCard.getDepartmentCode());
		scoreCardGroupUser.setDepartmentId(scoreCard.getDepartmentId());
		scoreCardGroupUser.setEmployeeId(employeeId);
		scoreCardGroupUser.setGroupId(0L);
		scoreCardGroupUser.setPlantCode(scoreCard.getPlantCode());
		scoreCardGroupUser.setPlantId(scoreCard.getPlantId());
		scoreCardGroupUser.setPoItem(scoreCard.getPoItem());
		scoreCardGroupUser.setScorecardId(scoreCard.getId());
		scoreCardGroupUser.setStatus(0);
		scoreCardGroupUser.setSubCategoryId(scoreCard.getSubCategoryId());
		scoreCardGroupUser.setSubCategoryName(scoreCard.getSubCategoryName());
		scoreCardGroupUser.setTemplateId(scoreCard.getTemplateId());
		scoreCardGroupUser.setTemplateName(scoreCard.getTemplateName());
		scoreCardGroupUser.setVendorId(scoreCard.getVendorId());
		scoreCardGroupUser.setVendorCode(scoreCard.getVendorCode());
		scoreCardGroupUser.setWeight(scoreCard.getWeight());
		return scoreCardGroupUser;
	}

	@Override
	public List<User> getAllUserNotAssigned(Long scoreCardId) {
		if (scoreCardId <= 0) {
			log.info("scorecard number cannot be null/empty");
			throw new VedantaException("scorecard number cannot be null/empty");
		}
		ScoreCard scoreCard = this.get(scoreCardId);
		List<User> userDetails = null;

		List<ScoreCardGroupUser> scoreCardGroupUsers = scoreCardGroupUserDao.getAllAssignedUser(
				scoreCard.getContractId(), scoreCard.getCategoryId(), scoreCard.getPlantCode(), scoreCard.getPoItem(),
				scoreCard.getTemplateId(), scoreCard.getVendorCode(), scoreCard.getSubCategoryId());

		if (ObjectUtils.isEmpty(scoreCardGroupUsers)) {

			userDetails = userDetail.getAllUsersNotAdminByLimit();
		} else {
			Set<String> userData = new HashSet<String>();
			scoreCardGroupUsers.forEach(scoreCardGroupUser -> {
				userData.add(scoreCardGroupUser.getEmployeeId());
			});
			Pageable topTen = new PageRequest(0, 10);

			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);

			if (VedantaUtility.getCurrentUserBuId() == 0) {
				userDetails = userDao.getAllUsersNotAssigned(userData, authorties, Constants.STATUS_ACTIVE, topTen);
			} else {
				userDetails = userDao.getAllUsersNotAssigned(userData, authorties, VedantaUtility.getCurrentUserBuId(),
						Constants.STATUS_ACTIVE, topTen);
			}

		}
		return userDetails;
	}

	@Override
	public List<User> getAllUserNotAssignedByName(Long scoreCardId, String name) {
		if (scoreCardId <= 0) {
			log.info("scorecard number cannot be null/empty");
			throw new VedantaException("scorecard number cannot be null/empty");
		}
		ScoreCard scoreCard = this.get(scoreCardId);
		List<User> userDetails = null;

		List<ScoreCardGroupUser> scoreCardGroupUsers = scoreCardGroupUserDao.getAllAssignedUser(
				scoreCard.getContractId(), scoreCard.getCategoryId(), scoreCard.getPlantCode(), scoreCard.getPoItem(),
				scoreCard.getTemplateId(), scoreCard.getVendorCode(), scoreCard.getSubCategoryId());

		if (ObjectUtils.isEmpty(scoreCardGroupUsers)) {

			userDetails = userDetail.getAllUsersNotAdminByName(name);
		} else {
			Set<String> userData = new HashSet<String>();
			scoreCardGroupUsers.forEach(scoreCardGroupUser -> {
				userData.add(scoreCardGroupUser.getEmployeeId());
			});
			Set<String> authorties = new HashSet<>();
			authorties.add(Constants.ROLE_ADMIN);
			authorties.add(Constants.ROLE_BUSINESS_UNIT_HEAD);
			authorties.add(Constants.ROLE_PLANT_HEAD);

			if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
				userDetails = userDao.getAllUserNotAssignedByName(userData, authorties, Constants.STATUS_ACTIVE, name);
			} else {
				userDetails = userDao.getAllUserNotAssignedByName(userData, authorties,
						VedantaUtility.getCurrentUserBuId(), Constants.STATUS_ACTIVE, name);
			}

		}
		return userDetails;
	}

	@Override
	public List<User> getAllUserNotAdminByLimit() {
		List<User> userDetails = null;

		try {
			userDetails = userDetail.getAllUsersNotAdminByLimit();
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	@Override
	public List<User> getAllUserNotAdminByLimitAndBuId(Long buId) {
		List<User> userDetails = null;
		try {
			userDetails = userDetail.getAllUsersNotAdminByLimitByBuId(buId);
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	@Override
	public List<User> getAllUserByName(String name) {

		List<User> userDetails = null;
		try {
			userDetails = userDetail.getAllUsersNotAdminByName(name);
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	@Override
	public List<User> getAllUserByNameAndBuId(String name, Long buId) {

		List<User> userDetails = null;
		try {
			userDetails = userDetail.getAllUsersNotAdminByNameAndBuId(name, buId);
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	@Override
	public Map<String, Object> getScoreCardTemplateMapByScoreCardIdOnFormAggrigation(Long scoreCardId) {
		if (scoreCardId <= 0) {
			log.info("scorecard number cannot be null/empty");
			throw new VedantaException("scorecard number cannot be null/empty");
		}
		try {
			Map<String, Object> scoreCardTemplateMap = new HashMap<>();
			ScoreCard scoreCard = this.get(scoreCardId);
			if (!ObjectUtils.isEmpty(scoreCard)) {
				scoreCard.setIsContractManager(false);
				scoreCard.setIsApprover(false);

				scoreCard.setScoreCardData(this.getScoreCardDataFromScoreCard(scoreCard));
				scoreCard.setIsEnable(true);
				scoreCardTemplateMap.put(Constants.SCORECARD_DETAIL, scoreCard);
				Contract contractDetails = contractService.findByContractNumber(scoreCard.getContractNumber());
				if (!ObjectUtils.isEmpty(contractDetails)) {
					scoreCardTemplateMap.put(Constants.CONTRACT, contractDetails);
					Template templateDetail = templateService
							.getScoreCardTemplateBySubCategoryId(contractDetails.getSubCategoryId());
					templateDetail.setIsAllGroupAccess(false);
					if (!ObjectUtils.isEmpty(templateDetail)) {

						List<TemplateGroup> templateGroups = new ArrayList<>();

						templateGroups = templateGroupService.getAllTemplateGroupsByTemplateId(templateDetail.getId());
						templateDetail.setIsAllGroupAccess(true);

						scoreCardTemplateMap.put(Constants.TEMPLATE, templateDetail);

						if (!CollectionUtils.isEmpty(templateGroups)) {

							templateGroups.forEach(templateGroup -> {
								Group groupDetail = groupService.get(templateGroup.getGroupId());
								List<Field> groupFieldList = new ArrayList<>();
								List<GroupField> groupFields = groupFieldService
										.getAllGroupFieldsByGroupId(groupDetail.getId());
								groupFields.forEach(groupField -> {
									Field fieldDetail = fieldService.getFieldDetail(groupField.getFieldId());

									if (!ObjectUtils.isEmpty(fieldDetail.getDataUnitId())) {
										fieldDetail.setDataUnit(dataUnitService.get(fieldDetail.getDataUnitId()));
									}

									groupFieldList.add(fieldDetail);
								});
								groupDetail.setFields(groupFieldList);

								templateGroup.setGroup(groupDetail);
							});

							scoreCardTemplateMap.put(Constants.TEMPLATE_GROUPS, templateGroups);
						}
					}
				}
			}

			return scoreCardTemplateMap;
		} catch (VedantaException e) {
			String msg = "Error while getting Contract by contract number";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Set<ScorecardAggregation> getScorecardBySubcategoryId(Long subCategoryId) {
		return scoreCardDao.getScoreCardBySubCategoryIdId(subCategoryId);
	}

	@Override
	public List<ScoreCard> getScorecardByContractNumber(String contractNumber) {
		return null;
	}

	@Override
	public ScoreCard updateScorecard(ScoreCard scorecard) {
		return scoreCardDao.save(scorecard);
	}

	@Override
	public List<ScoreCard> getAllScorecardByRoleBased() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		List<ScoreCard> scorecard = new ArrayList<>();
		Set<String> plantCode = new HashSet<>();
		if (VedantaUtility.isCurrentUserPlantHead()) {

			List<PlantHead> plantHeadetails = plantHeadService.getByEmployeeId(vedantaUser.getEmployeeId());
			plantHeadetails.forEach(plantHead -> {
				plantCode.add(plantHead.getPlantCode());
			});
			if (!ObjectUtils.isEmpty(plantCode)) {

				scorecard.addAll(scoreCardDao.getAllScorecardByListPlantCodeAndBusinessId(plantCode,
						vedantaUser.getBusinessUnitId()));

				if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
					for (ScoreCard sc : scorecard) {
						sc.setVendor(vendorService.get(sc.getVendorId()));
						sc.setTemplate(templateService.get(sc.getTemplateId()));

						if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
							User user = userDetail.getUserByUserId(sc.getSubmittedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setSubmittedNameBy(user.getName());
							}
						}

						if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
							User user = userDetail.getUserByUserId(sc.getApprovedBy());

							if (!ObjectUtils.isEmpty(user)) {
								sc.setApprovedByName(user.getName());
							}
						}

					}
				}
			}

		}

		if (VedantaUtility.isCurrentUserBusinessUnitHead()) {

			scorecard.addAll(scoreCardDao.getAllScorecardByBusinessId(vedantaUser.getBusinessUnitId()));

			if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
				for (ScoreCard sc : scorecard) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));

					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}

					if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
						User user = userDetail.getUserByUserId(sc.getApprovedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setApprovedByName(user.getName());
						}
					}

				}
			}
			return scorecard;
		}

		if (VedantaUtility.isCurrentUserAdmin()) {
			if (VedantaUtility.isSuperAdmin()) {
				scorecard.addAll(scoreCardDao.getAllScoreCardNotStatus(Constants.STATUS_DELETE));
			} else {
				scorecard.addAll(scoreCardDao.getAllScorecardByBusinessId(vedantaUser.getBusinessUnitId()));
			}

			if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
				for (ScoreCard sc : scorecard) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));

					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}

					if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
						User user = userDetail.getUserByUserId(sc.getApprovedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setApprovedByName(user.getName());
						}
					}

				}
			}
			return scorecard;
		}

		Boolean isContractManger = false;
		List<ScoreCard> scoreCardsList = new ArrayList<>();
		if (isCurrentUserContractManager()) {

			scoreCardsList = scoreCardDao.getScoreCardsAllAndNotStatus(VedantaUtility.getCurrentUserBuId(),
					Constants.STATUS_DELETE);

			for (ScoreCard details : scoreCardsList) {

				if (details.getContractManagerId().equals(vedantaUser.getEmployeeId())) {

					scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatus(
							VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(), Constants.STATUS_DELETE);
				} else {
				
					scoreCardsList = scoreCardDao.getScoreCardsByApproverByIdAndNotStatus(
							VedantaUtility.getCurrentUserBuId(), vedantaUser.getEmployeeId(), Constants.STATUS_DELETE);

				}

			}

			if (!ObjectUtils.isEmpty(scoreCardsList) && scoreCardsList.size() > 0) {
				for (ScoreCard sc : scoreCardsList) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));
				}
			}
			isContractManger = true;
		}

		List<ScoreCardGroupUserMapper> scoreCardGroupUserMappers = scoreCardGroupUserDao
				.getDistinctContractNumbersAndManagersByUserId(vedantaUser.getEmployeeId());

		Set<String> contractNumbers = new HashSet<>();
		Set<String> contractManagerIds = new HashSet<>();
		Set<String> poItems = new HashSet<>();

		if (!CollectionUtils.isEmpty(scoreCardGroupUserMappers)) {
			scoreCardGroupUserMappers.forEach(scoreCardGroupUserMapper -> {
				contractNumbers.add(scoreCardGroupUserMapper.getContractNumber());
				contractManagerIds.add(scoreCardGroupUserMapper.getContractManagerId());
				poItems.add(scoreCardGroupUserMapper.getPoItem());
			});
		}

		if (contractNumbers.size() > 0 && contractManagerIds.size() > 0 && poItems.size() > 0) {
			scorecard = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatus(
					VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
					Constants.STATUS_DELETE);

			if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
				for (ScoreCard sc : scorecard) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));

				}
			}
		}

		if (isContractManger && scoreCardsList.size() > 0) {

			scorecard = Stream.concat(scoreCardsList.stream(), scorecard.stream()).distinct()
					.collect(Collectors.toList());
		}

		if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {

			for (ScoreCard sc : scorecard) {

				if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
					User user = userDetail.getUserByUserId(sc.getSubmittedBy());
					if (!ObjectUtils.isEmpty(user)) {
						sc.setSubmittedNameBy(user.getName());
					}
				}

				if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
					User user = userDetail.getUserByUserId(sc.getApprovedBy());

					if (!ObjectUtils.isEmpty(user)) {
						sc.setApprovedByName(user.getName());
					}
				}
			}
		}
		return scorecard;

	}

	@Override
	public List<ScoreCard> getUserScoreCardsByStatusAndBusinessUnitId(Long businessUnitId, int status) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

		List<ScoreCard> scorecard = new ArrayList<>();
		Set<String> plantCode = new HashSet<>();

		if (VedantaUtility.isCurrentUserPlantHead()) {
			List<PlantHead> plantHeadetails = plantHeadService.getByEmployeeId(vedantaUser.getEmployeeId());
			plantHeadetails.forEach(plantHead -> {
				plantCode.add(plantHead.getPlantCode());
			});
			if (status == Constants.ALL_SCORECARD || status == 2) {
				scorecard.addAll(scoreCardDao.getAllScoreCardByBusinessUnitIdAndNotStatus(businessUnitId,
						Constants.STATUS_DELETE, plantCode));
			} else {
				scorecard.addAll(
						scoreCardDao.getAllScoreCardByBusinessUnitIdAndStatus(businessUnitId, status, plantCode));
			}

			if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
				for (ScoreCard sc : scorecard) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));

					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}

					if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
						User user = userDetail.getUserByUserId(sc.getApprovedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setApprovedByName(user.getName());
						}
					}

				}

			}
			return scorecard;
		}

		if (isCurrentUserGranted() || VedantaUtility.isCurrentUserBusinessUnitHead()) {
			// fetching score-card for role Admin by business unit id, plant
			// code and status
			if (VedantaUtility.isCurrentUserAdmin()) {

				if (status == Constants.ALL_SCORECARD || status == 2) {
					scorecard = scoreCardDao.getAllScoreCardNotStatusAndBusinessUnitId(Constants.STATUS_DELETE,
							businessUnitId);

				} else {
					scorecard = scoreCardDao.getAllScoreCardByBusinessUnitId(status, businessUnitId);

				}

			}
			if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
				for (ScoreCard sc : scorecard) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));

					if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
						User user = userDetail.getUserByUserId(sc.getSubmittedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setSubmittedNameBy(user.getName());
						}
					}

					if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
						User user = userDetail.getUserByUserId(sc.getApprovedBy());

						if (!ObjectUtils.isEmpty(user)) {
							sc.setApprovedByName(user.getName());
						}
					}

				}
			}
			return scorecard;
		}

		Boolean isContractManger = false;
		List<ScoreCard> scoreCardsList = new ArrayList<>();

		if (isCurrentUserContractManager()) {
			if (status == Constants.ALL_SCORECARD || status == 2) {

				scoreCardsList = scoreCardDao.getScoreCardsByManagerAndNotStatus(VedantaUtility.getCurrentUserBuId(),
						vedantaUser.getEmployeeId(), Constants.STATUS_DELETE);
			} else {
				scoreCardsList = scoreCardDao.getScoreCardsByManagerAndStatus(VedantaUtility.getCurrentUserBuId(),
						vedantaUser.getEmployeeId(), status);
			}
			if (!ObjectUtils.isEmpty(scoreCardsList) && scoreCardsList.size() > 0) {
				for (ScoreCard sc : scoreCardsList) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));
				}
			}
			isContractManger = true;
		}

		List<ScoreCardGroupUserMapper> scoreCardGroupUserMappers = scoreCardGroupUserDao
				.getDistinctContractNumbersAndManagersByUserId(vedantaUser.getEmployeeId());
		Set<String> contractNumbers = new HashSet<>();
		Set<String> contractManagerIds = new HashSet<>();
		Set<String> poItems = new HashSet<>();

		if (!CollectionUtils.isEmpty(scoreCardGroupUserMappers)) {
			scoreCardGroupUserMappers.forEach(scoreCardGroupUserMapper -> {
				contractNumbers.add(scoreCardGroupUserMapper.getContractNumber());
				contractManagerIds.add(scoreCardGroupUserMapper.getContractManagerId());
				poItems.add(scoreCardGroupUserMapper.getPoItem());
			});
		}
		if (contractNumbers.size() > 0 && contractManagerIds.size() > 0 && poItems.size() > 0) {
			if (status == Constants.ALL_SCORECARD || status == 2) {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					scorecard = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatus(contractNumbers,
							contractManagerIds, poItems, Constants.STATUS_DELETE);
				} else {
					scorecard = scoreCardDao.getScoreCardsByManagersAndContractNumbersNotStatus(
							VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems,
							Constants.STATUS_DELETE);
				}
			} else {

				if (VedantaUtility.isCurrentUserAdmin() && VedantaUtility.getCurrentUserBuId() == 0) {
					scorecard = scoreCardDao.getScoreCardsByManagersAndContractNumbers(contractNumbers,
							contractManagerIds, poItems, status);
				} else {
					scorecard = scoreCardDao.getScoreCardsByManagersAndContractNumbers(
							VedantaUtility.getCurrentUserBuId(), contractNumbers, contractManagerIds, poItems, status);
				}
			}

			if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
				for (ScoreCard sc : scorecard) {
					sc.setVendor(vendorService.get(sc.getVendorId()));
					sc.setTemplate(templateService.get(sc.getTemplateId()));
				}
			}
		}

		if (isContractManger && scoreCardsList.size() > 0) {
			scorecard = Stream.concat(scoreCardsList.stream(), scorecard.stream()).distinct()
					.collect(Collectors.toList());
		}

		if (!ObjectUtils.isEmpty(scorecard) && scorecard.size() > 0) {
			for (ScoreCard sc : scorecard) {
				if (!ObjectUtils.isEmpty(sc.getSubmittedBy())) {
					User user = userDetail.getUserByUserId(sc.getSubmittedBy());
					if (!ObjectUtils.isEmpty(user)) {
						sc.setSubmittedNameBy(user.getName());
					}
				}
				if (!ObjectUtils.isEmpty(sc.getApprovedBy())) {
					User user = userDetail.getUserByUserId(sc.getApprovedBy());

					if (!ObjectUtils.isEmpty(user)) {
						sc.setApprovedByName(user.getName());
					}
				}
			}
		}
		return scorecard;
	}

	@Override
	public ScoreCard getScorecardByContractNumberAndMonthIdAndYearId(Long businessUnitId, int monthId, int yearId,
			String contractNumber) {
		return scoreCardDao.getScorecardByContractNumberAndMonthIdAndYearId(businessUnitId, monthId, yearId,
				contractNumber);
	}

	@Override
	public List<User> getAllUserNotAdminByLimitAndBuIdAndNotApprover(Long buId, Long scorecardId) {
		List<User> userDetails = null;
		try {
			ScoreCard scoreCard = this.get(scorecardId);
			
			userDetails = userDetail.getAllUsersNotAdminByLimitByBuId(buId);
			
			if(org.apache.commons.lang3.StringUtils.isNotBlank(scoreCard.getApproverById())){
				userDetails.removeIf(obj->obj.getEmployeeId().equals(scoreCard.getApproverById()));
			}
			
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	@Override
	public List<User> getAllUserByNameAndBuIdAndNotApprover(String name, Long buId, Long scorecardId) {
		List<User> userDetails = null;
		try {
			ScoreCard scorecard=this.get(scorecardId);
			userDetails = userDetail.getAllUsersNotAdminByNameAndBuId(name, buId);
			
			if(org.apache.commons.lang3.StringUtils.isNotBlank(scorecard.getApproverById())){
				userDetails.removeIf(obj->obj.getEmployeeId().equals(scorecard.getApproverById()));
			}
		} catch (VedantaException e) {
			log.error("Error while fetching user details." + e.getMessage());
		}
		return userDetails;
	}

	private List<ApproverUpdateMapper> updateApprover(List<ApproverUpdateMapper> approverUpdateMapper){
		
		if(ObjectUtils.isEmpty(approverUpdateMapper) || approverUpdateMapper.size() <= 0){
			String msg = "Error while updating scorecard for Approver On File Upload, File data is null.";
			log.error(msg);
			throw new VedantaException(msg);
		}
		
		try{
			approverUpdateMapper.forEach(approver -> {
				List<ScoreCard> scorecards = scoreCardDao.getScoreCardByBusinessUnitIdAndMonthIdAndYearIdAndContractNumberAndContractManagerIdAndStatus(
												approver.getBusinessUnitId(),(approver.getMonthId()),DateUtil.getYear(),
												approver.getContractNumber(),approver.getContractManagerId(),0);
							
				if(!ObjectUtils.isEmpty(scorecards) && scorecards.size() > 0){
					scorecards.forEach(scorecard -> {
						if(approver.getStatus() == 1){
							scorecard.setApproverById(approver.getApproverById());
							scorecard.setApproverByName(approver.getApproverByName());
							scorecard.setLastUpdateBy(1L);
							scoreCardDao.save(scorecard);
						}
					});
				}else{
					approver.setDesc("May Be Contract Number or Contract Manager Id not found or Business Unit Id or Month not matched.");
					approver.setStatus(0);
				}
			});
			return approverUpdateMapper;
		}catch (VedantaException e) {
			String msg = "Error while updating scorecard for Approver On File Upload.";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
	
	@Async
	@Override
	public void updateScoreCardForApprover(List<ApproverUpdateMapper> approverUpdateMapper){
	
		if(ObjectUtils.isEmpty(approverUpdateMapper) || approverUpdateMapper.size() <= 0){
			String msg = "Error while updating scorecard for Approver, data is null.";
			log.error(msg);
			throw new VedantaException(msg);
		}
		
		try{
			
			approverUpdateMapper = verifyApprover(approverUpdateMapper);
			approverUpdateMapper = updateApprover(approverUpdateMapper);
			
			long faildCount = approverUpdateMapper.stream().filter( record-> record.getStatus() == 0).count();
			
			if(faildCount > 0){
				String text = getMailText(approverUpdateMapper, faildCount);
				mailSenderService.sendTo(approverUploadMail, approverUploadMail,"Scorecard Approver-File Upload Detail",text);
			}else{
				mailSenderService.sendTo(approverUploadMail, approverUploadMail,"Scorecard Approver-File Upload Detail","Your Approver List Successfully updated in scorecards.");
			}
		}catch (VedantaException | MessagingException e) {
			String msg = "Error while updating scorecard for Approver.";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
	
	private List<ApproverUpdateMapper> verifyApprover(List<ApproverUpdateMapper> approverUpdateMapper){
		
		if(ObjectUtils.isEmpty(approverUpdateMapper) || approverUpdateMapper.size() <= 0){
			String msg = "Error while verify approver on file upload, File data is null.";
			log.error(msg);
			throw new VedantaException(msg);
		}
		
		try{
		
			Set<String> approverList = new HashSet<>();
			long businessUnitId = 0L;
			for(ApproverUpdateMapper approver : approverUpdateMapper){
				approverList.add(approver.getApproverById());
				businessUnitId = approver.getBusinessUnitId();
			}
			final List<User> users = userDao.findAllByEmployeeIdAndBusinessUnitId(approverList,businessUnitId);
			
			if(!ObjectUtils.isEmpty(users) && users.size() > 0){
				approverUpdateMapper.forEach(approver -> {
					if(users.stream().anyMatch(user-> user.getEmployeeId().equals(approver.getApproverById()))){
						approver.setStatus(1);
					}else{
						approver.setDesc("Approver Not Found in Application.");
					}
				});
			}
			
			return approverUpdateMapper;
		}catch (VedantaException e) {
			String msg = "Error while verify approver on file upload.";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
	
	private String getMailText(List<ApproverUpdateMapper> approverUpdateMapper,long failedRecordCount){
		
		if(ObjectUtils.isEmpty(approverUpdateMapper) || approverUpdateMapper.size() <= 0){
			String msg = "Error while  making mail template for File Upload, File data is null.";
			log.error(msg);
			throw new VedantaException(msg);
		}
		
		try{
			String text = "<h3>Failed Records :</h3><br/><p>Here "+failedRecordCount+" records is failed to process out of "+approverUpdateMapper.size()+" records:</p><br/>";
			text += "<table style='width:70%;border:1px solid black;border-collapse: collapse;'><tr>";
			text += "<th style='width:20px;border:1px solid black;border-collapse: collapse;'>Sr. No.</th>";
			text += "<th style='width:40px;border:1px solid black;border-collapse: collapse;'>ContractNumber</th>";
			text += "<th style='width:40px;border:1px solid black;border-collapse: collapse;'>ContractManagerID</th>";
			text += "<th style='width:40px;border:1px solid black;border-collapse: collapse;'>ApproverID</th>";
			text += "<th style='width:40px;border:1px solid black;border-collapse: collapse;'>ApproverName</th>";
			text += "<th style='width:100%;border:1px solid black;border-collapse: collapse;'>Cause</th></tr>";
			
			int counter = 0;
			for(ApproverUpdateMapper processedRecord : approverUpdateMapper){
				if(processedRecord.getStatus() == 0){
					if(counter < 100){
						text += "<tr><td style='width:20px;border:1px solid black;border-collapse: collapse;'>"+(++counter)+"</td>";
						text += "<td style='width:40px;border:1px solid black;border-collapse: collapse;'>"+processedRecord.getContractNumber()+"</td>";
						text += "<td style='width:40px;border:1px solid black;border-collapse: collapse;'>"+processedRecord.getContractManagerId()+"</td>";
						text += "<td style='width:40px;border:1px solid black;border-collapse: collapse;'>"+processedRecord.getApproverById()+"</td>";
						text += "<td style='width:40px;border:1px solid black;border-collapse: collapse;'>"+processedRecord.getApproverByName()+"</td>";
						text += "<td style='width:100%;border:1px solid black;border-collapse: collapse;'><small>"+processedRecord.getDesc()+"</small></td></tr>";
					}
				}
			}
			text +="</table><br/>";
			
			if(counter >=100){
				text +="<p>we are not able to show all data please check your sheet .</p>";
			}		
			return text;
		}catch (VedantaException e) {
			String msg = "Error while making mail template for File Upload.";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}
}
