package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishsanger on 06/10/17.
 */
@Entity
@Table(name = "form_group_users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormGroupUser implements Serializable {
    private static final long serialVersionUID = -5616486574852146477L;

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

    @Column(name = "description")
    private String description;

    @Column(name = "contract_manager_id")
    private String contractManagerId;

    @Column(name = "contract_manager")
    private String contractManager;

    @NotNull
    @Column(name = "vendor_id")
    private Long vendorId;

    @NotNull
    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "category_id")
    private Long categoryId;

    @NotNull
    @Column(name = "category_name")
    private String categoryName;

    @NotNull
    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @NotNull
    @Column(name = "sub_category_name")
    private String subCategoryName;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "department_code")
    private String departmentCode;

    @Column(name = "employee_id")
    private String employeeId;

    @NotNull
    @Column(name = "form_id", columnDefinition = "bigint(20) default 0")
    private Long formId = 0L;

    @NotNull
    @Column(name = "form_name")
    private String formName;

    @NotNull
    @Column(name = "form_group_detail_id", columnDefinition = "bigint(20) default 0")
    private Long formGroupDetailId = 0L;

    @Column(name = "submitted_by", columnDefinition = "bigint(20) default 0")
    private Long submittedBy = 0L;

    @Column(name = "last_updated_by", columnDefinition = "bigint(20) default 0")
    private Long lastUpdateBy = 0L;

    @Column(name = "approved_by", columnDefinition = "bigint(20) default 0")
    private Long approvedBy = 0L;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;
    
    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Transient
    private String emailMessage;

}
