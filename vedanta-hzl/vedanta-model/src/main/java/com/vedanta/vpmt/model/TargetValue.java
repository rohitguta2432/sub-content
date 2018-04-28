package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * Created by manishsanger on 26/09/17.
 */
@Entity
@Table(name = "target_value")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TargetValue implements Serializable{
    private static final long serialVersionUID = -3451012314765476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "field_id")
    private Long fieldId;

    @Column(name = "category_id", columnDefinition = "bigint(20) default 0")
    private Long categoryId = 0L;

    @Transient
    private String categoryName;

    @Column(name = "sub_category_id", columnDefinition = "bigint(20) default 0")
    private Long subCategoryId = 0L;
    
    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "contract_number")
    private String contractNumber;

    @Transient
    private String subCategoryName;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Transient
    private String plantName;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "department_code")
    private String departmentCode;

    @Transient
    private String departmentName;

    @Column(name = "value")
    private String value;

    @Column(name = "last_value")
    private String lastValue;

    @Column(name = "is_higher_value_allowed", columnDefinition = "tinyint(1) DEFAULT 0")
    private int isHigherValueAllowed;

    @Column(name = "is_edit_allowed", columnDefinition = "tinyint(1) DEFAULT 0")
    private int isEditAllowed;

    @Column(name = "last_updated_by")
    private String lastUpdateBy;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}