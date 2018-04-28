package com.vedanta.vpmt.service.sap;

import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.vedanta.vpmt.contstant.Constants;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.*;
import com.vedanta.vpmt.service.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class SAPClientServiceImpl implements SAPClientService {

	private static final String ZBAPI_PR_USER_GET_DETAIL = "ZBAPI_PR_USER_GET_DETAIL";

	private static final String PO_HEADER = "PO_HEADER";

	private static final String ZBAPI_PO_GETDETAIL = "ZBAPI_PO_GETDETAIL";

	private static final String TABLE_PO_ITEMS = "PO_ITEMS";

	private static final String IMPORT_PARAM_PURCHASE_ORDER = "PURCHASEORDER";

	private static final String IMPORT_PARAM_PLANT = "PLANT";

	private static final String BAPI_PO_GETITEMS = "BAPI_PO_GETITEMS";

	private static final String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";

	private static final String SAP_FUNCTION_ERROR = "%s not found in SAP.";

	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

	@Value("${sap.jco.host}")
	private String host;

	@Value("${sap.jco.system.number}")
	private String systemNumber;

	@Value("${sap.jco.instance.number}")
	private String instanceNumber;

	@Value("${sap.jco.user}")
	private String user;

	@Value("${sap.jco.password}")
	private String password;

	@Value("${sap.jco.language}")
	private String language;

	@Autowired
	private SubCategoryService subCategoryService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private VendorService vendorService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private FormService formService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private ScoreCardGroupUserService scoreCardGroupUserService;

	@Autowired
	private FormGroupUserService formGroupUserService;

	@PostConstruct
	public void init() {
		Properties connectProperties = new Properties();
		connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, host);
		connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR, systemNumber);
		connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, instanceNumber);
		connectProperties.setProperty(DestinationDataProvider.JCO_USER, user);
		connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, password);
		connectProperties.setProperty(DestinationDataProvider.JCO_LANG, language);
		createDestinationDataFile(ABAP_AS_POOLED, connectProperties);
	}

	private static void createDestinationDataFile(String destinationName, Properties connectProperties) {
		File destCfg = new File(destinationName + ".jcoDestination");
		try {
			FileOutputStream fos = new FileOutputStream(destCfg, false);
			connectProperties.store(fos, "Connection Parameters");
			fos.close();
		} catch (Exception e) {
			throw new RuntimeException("Unable to create the destination files", e);
		}
	}

	public void createContracts(@NonNull List<Plant> plants) {

		if (CollectionUtils.isEmpty(plants)) {
			return;
		}

		for (Plant plant : plants) {
			try {
				JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);
				JCoFunction function = destination.getRepository().getFunction(BAPI_PO_GETITEMS);
				if (function == null) {
					throw new RuntimeException(String.format(SAP_FUNCTION_ERROR, BAPI_PO_GETITEMS));
				}

				function.getImportParameterList().setValue(IMPORT_PARAM_PLANT, plant.getPlantCode());
				//("Plant Code: " + plant.getPlantCode());
				//(formatter.format(new Date()));
				// function.getImportParameterList().setValue("DOC_DATE",
				// formatter.format(new Date()));

				function.getImportParameterList().setValue("DOC_DATE", "20170422");

				function.execute(destination);

				JCoTable getItemsPOItems = function.getTableParameterList().getTable(TABLE_PO_ITEMS);
				//("Number of Rows:" + getItemsPOItems.getNumRows());
				JCoTable getDetailPOItems;
				JCoStructure getDetailPOHeader;

				for (int i = 0; i < getItemsPOItems.getNumRows(); i++) {
					getItemsPOItems.setRow(i);

					function = destination.getRepository().getFunction(ZBAPI_PO_GETDETAIL);
					if (function == null) {
						throw new RuntimeException(String.format(SAP_FUNCTION_ERROR, ZBAPI_PO_GETDETAIL));
					}
					//("PO Number 1: " + getItemsPOItems.getString("PO_NUMBER").trim());
					function.getImportParameterList().setValue(IMPORT_PARAM_PURCHASE_ORDER,
							getItemsPOItems.getString("PO_NUMBER").trim());
					// function.getImportParameterList().setValue(IMPORT_PARAM_PURCHASE_ORDER,
					// "5100018250");
					function.execute(destination);
					getDetailPOItems = function.getTableParameterList().getTable(TABLE_PO_ITEMS);
					getDetailPOHeader = function.getExportParameterList().getStructure(PO_HEADER);
					if ((getDetailPOItems.getNumRows() > 0)) {
						//("getDetailPOItems:");
						String category = getDetailPOItems.getString("CATEGORY").trim();
						//("CATEGORY:" + getDetailPOItems.getString("CATEGORY").trim());
						String PREQNumber = getDetailPOItems.getString("PREQ_NO").trim();
						//("PREQ_NO:" + getDetailPOItems.getString("PREQ_NO").trim());

						//("PO Number: " + getDetailPOItems.getString("PO_NUMBER").trim());

						if (!PREQNumber.equals("")) {
							if (!category.equals("")) {
								//("PO Number With Category & PREQ: "
										//+ getDetailPOHeader.getString("PO_NUMBER").trim() + " ----PREQ Number: "
										//+ PREQNumber);
								// System.out.printf("PREQ Number With category:
								// " + PREQNumber);
								Contract contract = createContract(plant, getItemsPOItems, getDetailPOItems,
										getDetailPOHeader);
								contract = contractService.findAndModifyByNumber(contract);
								if (contract != null) {
									String poItem = getItemsPOItems.getString("PO_ITEM").trim();
									String shortText = getItemsPOItems.getString("SHORT_TEXT").trim();
									Map<String, Object> groupUsers = createGroupUser(plant, contract, destination,
											getDetailPOHeader, poItem, shortText);

									List<ScoreCardGroupUser> scoreCardGroupUsers = (List<ScoreCardGroupUser>) groupUsers
											.get(Constants.SCORECARD_GROUP_USERS);
									List<FormGroupUser> formGroupUsers = (List<FormGroupUser>) groupUsers
											.get(Constants.FORM_GROUP_USERS);
									// List<ScoreCardGroupUser>
									// scoreCardGroupUsers =
									// createScoreCardGroupUser(plant, contract,
									// destination, getDetailPOHeader, poItem,
									// shortText);
									if (!CollectionUtils.isEmpty(scoreCardGroupUsers)) {
										scoreCardGroupUserService.save(poItem, contract.getNumber(),
												scoreCardGroupUsers);
									}
									if (!CollectionUtils.isEmpty(formGroupUsers)) {
										formGroupUserService.save(poItem, contract.getNumber(), formGroupUsers);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				String message = "Error while executing functions";
				log.error(message, e);
				throw new VedantaException(message, e);
			}
		}
		;
	}

	private Map<String, Object> createGroupUser(@NonNull Plant plant, @NonNull Contract contract,
			@NonNull JCoDestination destination, JCoStructure getDetailPOHeader, String poItem, String shortText)
			throws JCoException {

		Template template = templateService.getScoreCardTemplateBySubCategoryId(contract.getSubCategoryId());
		if (template == null) {
			log.error("template not found for template sub category id " + contract.getSubCategoryId());
			return null;
		}

		List<Form> forms = formService.getFormsBySubCategoryIdForAdmin(contract.getSubCategoryId());

		Map<String, Object> groupUsers = new HashMap<>();

		JCoFunction function = destination.getRepository().getFunction(ZBAPI_PR_USER_GET_DETAIL);
		if (function == null) {
			throw new RuntimeException(String.format(SAP_FUNCTION_ERROR, ZBAPI_PR_USER_GET_DETAIL));
		}

		function.getImportParameterList().setValue("BANFN", contract.getPreqNo());
		// function.getImportParameterList().setValue("BANFN", "1300014709");
		function.execute(destination);
		JCoTable codes = function.getTableParameterList().getTable("IT_TABLE");
		String managerId = null;
		String managerName = null;
		ScoreCardGroupUser scoreCardGroupUser = null;
		FormGroupUser formGroupUser;
		List<ScoreCardGroupUser> scoreCardGroupUsers = new ArrayList<>();
		List<FormGroupUser> formGroupUsers = new ArrayList<>();
		for (int i = 0; i < codes.getNumRows(); i++) {
			codes.setRow(i);
			managerId = codes.getString("UNAME").trim();
			managerName = codes.getString("NAME").trim();
			if (StringUtils.isEmpty(managerId) || StringUtils.isEmpty(managerName)) {
				continue;
			}

			scoreCardGroupUser = buildScoreCardGroupUser(plant, contract, poItem, shortText, template, managerId,
					managerName);

			if (!CollectionUtils.isEmpty(scoreCardGroupUsers)
					&& this.containsEmployee(scoreCardGroupUsers, scoreCardGroupUser.getContractManagerId())) {
				continue;
			}
			scoreCardGroupUsers.add(scoreCardGroupUser);

			if (!CollectionUtils.isEmpty(forms)) {
				for (Form form : forms) {
					formGroupUser = buildFormGroupUser(plant, contract, poItem, shortText, form, managerId,
							managerName);
					if (!CollectionUtils.isEmpty(formGroupUsers)
							&& this.containsFormGroupUsers(formGroupUsers, formGroupUser.getContractManagerId())) {
						continue;
					}
					formGroupUsers.add(formGroupUser);
				}
			}
		}

		groupUsers.put(Constants.SCORECARD_GROUP_USERS, scoreCardGroupUsers);
		groupUsers.put(Constants.FORM_GROUP_USERS, formGroupUsers);

		return groupUsers;
	}

	// To remove duplicate scorecard user for same contract, to ensure only one
	// scorecard is generated for a user.
	private boolean containsEmployee(final List<ScoreCardGroupUser> scoreCardGroupUsers,
			final String contractManagerId) {
		return scoreCardGroupUsers.stream().filter(sgu -> sgu.getContractManagerId().equals(contractManagerId))
				.findFirst().isPresent();
	}

	// To remove duplicate scorecard user for same contract, to ensure only one
	// scorecard is generated for a user.
	private boolean containsFormGroupUsers(final List<FormGroupUser> formGroupUsers, final String contractManagerId) {
		return formGroupUsers.stream().filter(fgu -> fgu.getContractManagerId().equals(contractManagerId)).findFirst()
				.isPresent();
	}

	private Contract createContract(Plant plant, JCoTable getItemsPOItems, JCoTable getDetailPOItems,
			JCoStructure getDetailPOHeader) {
		String sapCategory = getDetailPOItems.getString("CATEGORY").trim();
		SubCategory subCategory = subCategoryService.findBySapCategory(sapCategory, Constants.HZL_BUSINESS_UNIT);
		if (subCategory == null) {
			log.info("sub category not found for" + sapCategory);
			return null;
		}

		Category category = categoryService.get(subCategory.getCategoryId());
		if (category == null) {
			log.info("Category not found for category id " + subCategory.getCategoryId());
			return null;
		}

		String preqNum = getDetailPOItems.getString("PREQ_NO").trim();
		if (StringUtils.isEmpty(preqNum)) {
			log.info("PREQ number not found");
			return null;
		}

		String departmentCode = getDetailPOItems.getString("PLANT").trim();
		Department department = null;
		if (!StringUtils.isEmpty(departmentCode)) {
			department = departmentService.findByDepartmentCode(departmentCode);
		}

		String vendorCode = getDetailPOHeader.getString("VENDOR").trim().replaceFirst("^0+(?!$)", "");
		Vendor vendor = null;
		if (!StringUtils.isEmpty(vendorCode)) {
			vendor = vendorService.getVendor(subCategory, category, vendorCode,
					getDetailPOHeader.getString("VEND_NAME"),plant);
		}

		return buildContract(plant, getDetailPOItems, getDetailPOHeader, subCategory, category, department, vendor);
	}

	private Contract buildContract(Plant plant, JCoTable getDetailPOItems, JCoStructure getDetailPOHeader,
			SubCategory subCategory, Category category, Department department, Vendor vendor) {
				Contract contract = Contract.builder().number(getDetailPOItems.getString("PO_NUMBER").trim())
				.plantName(plant.getName()).plantId(plant.getId()).plantCode(plant.getPlantCode())
				.categoryId(category.getId()).categoryName(category.getCategoryName())
				.subCategoryId(subCategory.getId()).subCategoryName(subCategory.getCategoryName())
				.preqNo(getDetailPOItems.getString("PREQ_NO").trim()).businessUnitId(Constants.HZL_BUSINESS_UNIT)
				.createdBy(getDetailPOHeader.getString("CREATED_BY").trim())
				.startDate(getDetailPOHeader.getDate("VPER_START")).endDate(getDetailPOHeader.getDate("VPER_END"))
				.build();

		if (department != null) {
			contract.setDepartmentId(department.getId());
			contract.setDepartmentName(department.getName());
		}

		if (vendor != null) {
			contract.setVendorId(vendor.getId());
			contract.setVendorName(vendor.getName());
			contract.setVendorCode(vendor.getVendorCode());
		}
		return contract;
	}

	private ScoreCardGroupUser buildScoreCardGroupUser(Plant plant, Contract contract, String poItem, String shortText,
			Template template, String managerId, String managerName) {

		Long managerIdLong = Long.parseLong(managerId);
		String managerIdString = String.valueOf(managerIdLong);

		return ScoreCardGroupUser.builder().contractId(contract.getId()).contractNumber(contract.getNumber())
				.vendorId(contract.getVendorId()).vendorCode(contract.getVendorCode())
				.categoryId(contract.getCategoryId()).categoryName(contract.getCategoryName())
				.subCategoryId(contract.getSubCategoryId()).subCategoryName(contract.getSubCategoryName())
				.departmentId(contract.getDepartmentId()).departmentCode(contract.getDepartmentName())
				.plantId(plant.getId()).plantCode(plant.getPlantCode()).poItem(poItem).description(shortText)
				.templateId(template.getId()).templateName(template.getName()).employeeId(managerIdString.replaceFirst("^0+(?!$)", ""))
				.contractManagerId(managerIdString).contractManager(managerName)
				.businessUnitId(Constants.HZL_BUSINESS_UNIT).build();
	}

	private FormGroupUser buildFormGroupUser(Plant plant, Contract contract, String poItem, String shortText, Form form,
			String managerId, String managerName) {

		Long managerIdLong = Long.parseLong(managerId);
		String managerIdString = String.valueOf(managerIdLong);

		return FormGroupUser.builder().contractId(contract.getId()).contractNumber(contract.getNumber()).poItem(poItem)
				.description(shortText).contractManagerId(managerIdString).contractManager(managerName)
				.vendorId(contract.getVendorId()).vendorCode(contract.getVendorCode())
				.categoryId(contract.getCategoryId()).categoryName(contract.getCategoryName())
				.subCategoryId(contract.getSubCategoryId()).subCategoryName(contract.getSubCategoryName())
				.plantId(plant.getId()).plantCode(plant.getPlantCode()).employeeId(managerId.replaceFirst("^0+(?!$)", "")).formId(form.getId())
				.formName(form.getName()).businessUnitId(Constants.HZL_BUSINESS_UNIT).build();
	}

}
