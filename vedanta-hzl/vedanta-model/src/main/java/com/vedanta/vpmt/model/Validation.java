package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishsanger on 26/09/17.
 */
@Entity
@Table(name = "validations")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Validation implements Serializable {
    private static final long serialVersionUID = -3451953214765476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "field_id")
    private Long fieldId;

    @Column(name = "category_id", columnDefinition = "bigint(20) default 0")
    private Long categoryId = 0L;

    @Column(name = "sub_category_id", columnDefinition = "bigint(20) default 0")
    private Long subCategoryId = 0L;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "is_mandatory", columnDefinition = "int(1) DEFAULT 1")
    private int isMandatory;

    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

}
