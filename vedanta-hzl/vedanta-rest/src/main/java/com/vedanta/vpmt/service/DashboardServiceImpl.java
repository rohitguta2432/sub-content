package com.vedanta.vpmt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.dao.DashboardDao;
import com.vedanta.vpmt.dao.UserDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.PlantHead;
import com.vedanta.vpmt.model.ScorecardAggregation;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.model.User;
import com.vedanta.vpmt.model.VedantaUser;
import com.vedanta.vpmt.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Service("DashboardService")
@Slf4j
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private DashboardDao dashboardDao;

	@Autowired
	private ContractService contractService;

	@Autowired
	private PlantService plantService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private UserDao userDao;

	@Override
	public ScorecardAggregation get(long id) {
		return null;
	}

	@Override
	public ScorecardAggregation save(ScorecardAggregation entity) {
		return null;
	}

	@Override
	public ScorecardAggregation update(long id, ScorecardAggregation entity) {
		return null;
	}

	private List<ScorecardAggregation> getDashBoardDataMapperList(Long bussinessId) {

		Long plantId = 0l;
		Long categoryId = 0l;
		Long subCategoryId = 0l;

		List<ScorecardAggregation> dashboardDataMappers = new ArrayList<ScorecardAggregation>();

		if (bussinessId != 0) {

			List<Plant> getAllplantsByBuId = plantService.getAllPlantByBusinessUnitId(bussinessId);
			if (!ObjectUtils.isEmpty(getAllplantsByBuId)) {
				Plant plant = (Plant) getAllplantsByBuId.get(0);
				if (ObjectUtils.isEmpty(plant)) {
					plantId = plant.getId();
				}
			}

			List<Category> getAllcategoryByBuId = categoryService.getCategoriesByBusinessUnitId(bussinessId);
			if (!ObjectUtils.isEmpty(getAllcategoryByBuId)) {
				Category category = (Category) getAllcategoryByBuId.get(0);
				if (ObjectUtils.isEmpty(category)) {
					categoryId = category.getId();
				}
			}

			List<SubCategory> getAllsubCategoryByBuId = subCategoryService.findAllByBusinessUnitId(bussinessId);
			if (!ObjectUtils.isEmpty(getAllsubCategoryByBuId)) {
				SubCategory subCategory = (SubCategory) getAllsubCategoryByBuId.get(0);
				if (ObjectUtils.isEmpty(subCategory)) {
					subCategoryId = subCategory.getId();
				}
			}

			dashboardDataMappers = dashboardDao.getDashboardDataByBuId(bussinessId, plantId, categoryId, subCategoryId);

		}

		return dashboardDataMappers;
	}

	@Override
	public Map<String, Object> getDashboardDataByBuId(Long bussinessId) {

		try {
			Map<String, Object> dashboardData = new HashMap<>();
			List<ScorecardAggregation> dashboardDataMappers = this.getDashBoardDataMapperList(bussinessId);

			if (ObjectUtils.isEmpty(dashboardDataMappers) || dashboardDataMappers.size() <= 0) {
				dashboardData.put(Constants.NEW_DASHBOARD_DETAILS, new ArrayList<ScorecardAggregation>());
			} else {

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

			if (bussinessId != 0) {
				dashboardData.put(Constants.ALL_PLANTS, plantService.getAllPlantByBusinessUnitId(bussinessId));
				dashboardData.put(Constants.ALL_CATEGORIES, categoryService.getCategoriesByBusinessUnitId(bussinessId));
				dashboardData.put(Constants.ALL_SUBCATEGORIES, subCategoryService.findAllByBusinessUnitId(bussinessId));
				dashboardData.put(Constants.ALL_CONTRACTS, contractService.findAllByBusinessUnitId(bussinessId));
			}

			else {
				dashboardData.put(Constants.ALL_PLANTS, null);
				dashboardData.put(Constants.ALL_CATEGORIES, null);
				dashboardData.put(Constants.ALL_SUBCATEGORIES, null);
				dashboardData.put(Constants.ALL_CONTRACTS, null);

			}
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

	private List<ScorecardAggregation> getDashBoardDataList(Long bussinessId, Long plantId, Long categoryId,
			Long subCategoryId, Long contractId, String fromDate, String toDate) {

		List<ScorecardAggregation> dashboardDataMappers = new ArrayList<ScorecardAggregation>();
		if (!ObjectUtils.isEmpty(toDate) && !ObjectUtils.isEmpty(fromDate)) {

			String fDate[] = fromDate.split("/");
			int fmonth = Integer.parseInt(fDate[0]) - 1;
			int fyear = Integer.parseInt(fDate[1]);

			String tDate[] = toDate.split("/");
			int tmonth = Integer.parseInt(tDate[0]) - 1;
			int tyear = Integer.parseInt(tDate[1]);

			String startDate = fyear + "-" + fmonth + "-01";
			String endDate = tyear + "-" + tmonth + "-01";

			dashboardDataMappers = dashboardDao.getDashboardDataByBuIdAndSearchKey(bussinessId, plantId, categoryId,
					subCategoryId, contractId, startDate, endDate);
		} else {
			if (ObjectUtils.isEmpty(contractId) || contractId == 0L) {

				dashboardDataMappers = dashboardDao.getDashboardDataByBuId(bussinessId, plantId, categoryId,
						subCategoryId);
			} else {
				dashboardDataMappers = dashboardDao.getDashboardDataByBuIdAndSearchKey(bussinessId, plantId, categoryId,
						subCategoryId, contractId);
			}
		}

		return dashboardDataMappers;
	}

	@Override
	public Map<String, Object> getDashboardDataBySearchkeyBuId(Long bussinessId, Long plantid, Long categoryId,
			Long subCategoryId, Long contractId, String fromDate, String toDate) {
		List<ScorecardAggregation> dashboardDataMappers;
		try {
			Map<String, Object> dashboardData = new HashMap<>();

			if (!ObjectUtils.isEmpty(toDate) && !ObjectUtils.isEmpty(fromDate)) {

				dashboardDataMappers = this.getDashBoardDataList(bussinessId, plantid, categoryId, subCategoryId,
						contractId, fromDate, toDate);
			} else {
				dashboardDataMappers = this.getDashBoardDataList(bussinessId, plantid, categoryId, subCategoryId,
						contractId, fromDate, toDate);
			}

			if (ObjectUtils.isEmpty(dashboardDataMappers) || dashboardDataMappers.size() <= 0) {
				dashboardData.put(Constants.NEW_DASHBOARD_DETAILS, new ArrayList<ScorecardAggregation>());
			} else {
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
						//System.out.println(cat);
						//System.out.println(subCatList);
						Map<String, Set<String>> subcatContract = new HashMap<>();
						subCatList.forEach(subCat -> {
							Set<String> contractList = new HashSet<>();
							//System.out.println(dashboardDataMappers);
							dashboardDataMappers.forEach(dashboardDataMapper -> {
								if ((long)dashboardDataMapper.getPlantId() == plant
										&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(cat)
										&& dashboardDataMapper.getSubCategoryName().equalsIgnoreCase(subCat)) {
									//System.out.println(dashboardDataMapper.getContractNumber() + "count " + count++);
									
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
									if ((long)dashboardDataMapper.getPlantId() == plant
											&& dashboardDataMapper.getCategoryName().equalsIgnoreCase(catogry)
											&& dashboardDataMapper.getSubCategoryName().equalsIgnoreCase(subCategory)
											&& dashboardDataMapper.getContractNumber().equalsIgnoreCase(contract)) {
										dashboardList.add(dashboardDataMapper);
									}
								});
								//System.out.println(dashboardList);
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

			List<Plant> getAllPlants = new ArrayList<>();

			getAllPlants = plantService.getAllPlantByBusinessUnitId(bussinessId);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

			if (this.isPlantUnitAdmin()) {

				getAllPlants = dashboardDao.getPlantByPlantCode(vedantaUser.getPlantCode());
			}
			if (this.isPlantHead()) {
				List<PlantHead> AllPlantHead = new ArrayList<>();
				AllPlantHead = dashboardDao.getPlantHeadByEmployeeIdAndBusinessUnit(vedantaUser.getEmployeeId(),
						vedantaUser.getBusinessUnitId());

				List<Plant> plantDetails = new ArrayList<>();
				getAllPlants.clear();
				for (PlantHead plantHead : AllPlantHead) {
					plantDetails = dashboardDao.getPlantByPlantCodeAndBusinessUnit(plantHead.getPlantCode(),
							vedantaUser.getBusinessUnitId());
					getAllPlants.addAll(plantDetails);
				}

			}
			if (this.isSuperAdmin()) {
				getAllPlants = plantService.getAllPlantByBusinessUnitId(bussinessId);
			}
			if (this.isBussinessUnitAdmin()) {
				getAllPlants = plantService.getAllPlantByBusinessUnitId(bussinessId);
			}

			List<Category> categories = categoryService.getCategoriesByBusinessUnitId(bussinessId);

			dashboardData.put(Constants.ALL_PLANTS, getAllPlants);
			dashboardData.put(Constants.ALL_CATEGORIES, categories);
			if (ObjectUtils.isEmpty(categoryId)) {
				dashboardData.put(Constants.ALL_SUBCATEGORIES,
						subCategoryService.findAllSubCategoriesByCategoryId(categories.get(0).getId()));
			} else {
				dashboardData.put(Constants.ALL_SUBCATEGORIES,
						subCategoryService.findAllSubCategoriesByCategoryId(categoryId));
			}

			if (ObjectUtils.isEmpty(getAllPlants)) {
				dashboardData.put(Constants.ALL_CONTRACTS,
						contractService.findAllByBusinessUnitIdAndPlantCode(bussinessId, plantid));
			} else {
				if(this.isSuperAdmin()){
					dashboardData.put(Constants.ALL_CONTRACTS,
							contractService.findAllByBusinessUnitIdAndPlantCode(bussinessId, plantid));
				}else {
					dashboardData.put(Constants.ALL_CONTRACTS,
							contractService.findAllByBusinessUnitIdAndPlantCode(bussinessId, plantid));
				}
				
			}

			return dashboardData;
		} catch (VedantaException e) {
			String msg = "Error while getting all users by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	private boolean isPlantUnitAdmin() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();

		User user = userDao.findOne(vedantaUser.getId());

		if ((Constants.ROLE_ADMIN).equalsIgnoreCase(user.getAuthorities().trim())) {

			if (!user.getPlantCode().equals("0") && user.getBusinessUnitId() > 0) {

				return true;
			}
			return true;
		}
		return false;
	}

	private boolean isPlantHead() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		User user = userDao.findOne(vedantaUser.getId());
		if ((Constants.ROLE_PLANT_HEAD).equalsIgnoreCase((user.getAuthorities().trim()))) {
			return true;
		}
		return false;
	}

	private boolean isSuperAdmin() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		User user = userDao.findOne(vedantaUser.getId());

		if ((Constants.ROLE_ADMIN).equalsIgnoreCase(user.getAuthorities().trim())) {

			if (user.getPlantCode().equals("0") && user.getBusinessUnitId() == 0) {

				return true;
			}
			return false;
		}
		return false;
	}

	private boolean isBussinessUnitAdmin() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		VedantaUser vedantaUser = (VedantaUser) authentication.getPrincipal();
		User user = userDao.findOne(vedantaUser.getId());

		if ((Constants.ROLE_ADMIN).equalsIgnoreCase(user.getAuthorities().trim())) {

			if (user.getPlantCode().equals("0") && user.getBusinessUnitId() > 0) {

				return true;
			}
			return false;
		}

		return false;
	}
}
