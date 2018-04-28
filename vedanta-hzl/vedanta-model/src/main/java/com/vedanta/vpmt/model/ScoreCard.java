package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by manishsanger on 06/10/17.
 */
@Entity
@Table(name = "scorecard")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Builder
public class ScoreCard implements Serializable {
    private static final long serialVersionUID = -5648753374852146477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "contract_id")
    private Long contractId;

    @NotNull
    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "po_item")
    private String poItem;

    @Column(name = "po_item_description")
    private String poItemDescription;

    @NotNull
    @Column(name = "vendor_id")
    private Long vendorId;

    @NotNull
    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Column(name = "sub_category_name")
    private String subCategoryName;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "department_code")
    private String departmentCode;

    @Transient
    private List<ScoreCardData> scoreCardData = new ArrayList<>();

    @Column(name = "weight", columnDefinition = "int(5) DEFAULT 100")
    private int weight = 100;

    @Column(name = "total_score", columnDefinition="Decimal(10,2) DEFAULT '100.00'")
    private double totalScore = 100.00;

    @Column(name = "target_score", columnDefinition = "int(5) DEFAULT 0")
    private int targetScore;

    @Column(name = "actual_score", columnDefinition = "Decimal(10,2) DEFAULT '0.00'")
    private double actualScore;

    @Column(name = "last_actual_score", columnDefinition = "Decimal(10,2) DEFAULT '0.00'")
    private double lastActualScore;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 0")
    private int status;

    @Column(name = "month_id")
    private int monthId;

    @Column(name = "year_id")
    private int yearId;
    
    @Column(name = "contract_manager_id")
    private String contractManagerId;

    @Column(name = "contract_manager_name")
    private String contractManagerName;

    @Column(name = "submitted_by")
    private Long submittedBy;

    @Column(name = "last_updated_by")
    private Long lastUpdateBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "submitted_on")
    @Temporal(TemporalType.DATE)
    private Date submittedOn;

    @Column(name = "approved_on")
    @Temporal(TemporalType.DATE)
    private Date approvedOn;

    @Column(name = "last_update")
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    
    @Column(name="rejected_by")
    private Long rejectedBy;
    
    @Column(name="rejected_on")
    @Temporal(TemporalType.DATE)
    private Date rejectedOn;
    
    @Column(name="approverby_id")
    private String approverById;
    
    @Column(name="approverby_name")
    private String approverByName;
    
    @Column(name="approver_remarks")
    private String approverRemarks;
    
    @Lob
    @Column(name = "scorecard_data")
    private String scorecardDataJson;

    @Transient
    private List<List<Param>> scoreParamList = new ArrayList<>();
    
    @Transient
    private Template template;
    
    @Transient
    private Vendor vendor;

    @Transient
    private List<TemplateForm> templateForms = new ArrayList<>();
    
    @Transient
    private Boolean isApprover;
    
    @Transient 
    private Boolean isContractManager;
    
    @Transient
    private String submittedNameBy;
    
    @Transient 
    private String approvedByName;
    
    @Transient
    private Boolean isEnable;
    
    @Transient
    private String scorecardEmailNotification;
    
	@Data
	public static class Param{
		private String id;
		private String fieldId;
		private String groupId;
		private String target;
		private String value;
		private String score;
		private String remark;
		private String scoreWeight;
		private String isAutoPopulated;

	}
}

