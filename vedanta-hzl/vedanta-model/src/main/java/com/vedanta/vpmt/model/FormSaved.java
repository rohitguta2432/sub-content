package com.vedanta.vpmt.model;

import lombok.*;

import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by manishsanger on 06/10/17.
 */
@Entity
@Table(name = "forms_saved")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Builder
@DynamicUpdate
public class FormSaved implements Serializable {
    private static final long serialVersionUID = -5648753374852146477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "po_item")
    private String poItem;

    @Column(name = "po_item_description")
    private String poItemDescription;

    @Column(name = "vendor_id")
    private Long vendorId;

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

    @Column(name = "form_id")
    private Long formId;

    @Column(name = "form_name")
    private String formName;

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
    private List<FormData> formData = new ArrayList<>();

    @Lob
    @Column(name = "form_data")
    private String formDataJson;
    
    @Column(name = "weight", columnDefinition = "int(5) DEFAULT 100")
    private int weight;

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
    
    @Column(name = "average", columnDefinition="Decimal(10,2) DEFAULT '00.00'")
    private Double average;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name = "submitted_on",updatable=false)
    @Temporal(TemporalType.DATE)
    private Date submittedOn;

    @Column(name = "last_update")
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    @Column(name = "created_on")
    private Date createdOn;
    
    @Column(name="approved_by")
    private String approvedBy;
    
    @Column(name="approved_on",updatable=false)
    @Temporal(TemporalType.DATE)
    private Date approvedOn;
    
    @Column(name="rejected_by")
    private String rejectedBy;
    
    @Column(name="rejected_on",updatable=false)
    @Temporal(TemporalType.DATE)
    private Date rejectedOn;
    
    @Transient
    private List<List<Param>> formParamList = new ArrayList<>();
    
    @Transient
    private String submittedNameBy;
    
    @Transient
    private String vendorName;
    
    @Transient
    private Boolean isEnable;
    
    @Transient
    private Boolean isCompliance;
    
    @Transient
    private String complianceFormEmailNotification;
    
    @Data
    public static class Param{
		private String id;
		private String fieldId;
		private String groupId;
		private String target;
		private String value;
		private String remark;
	}
}

