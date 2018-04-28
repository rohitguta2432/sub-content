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
@Table(name = "scoring_criteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ScoringCriteria implements Serializable {

    private static final long serialVersionUID = -3452314146541476477L;

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

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "department_code")
    private String departmentCode;



    //Equal (Math/String), Greater than/equal, less than/equal, Boolean true/false
    @Column(name = "operator")
    private String operator;

    //String, Number, Boolean, Field.Value
    @Column(name = "left_operand_type")
    private String leftOperandType;

    @Column(name = "left_operand")
    private String leftOperand;

    //String, Number, Boolean, Field.Value
    @Column(name = "right_operand_type")
    private String rightOperandType;

    @Column(name = "right_operand")
    private String rightOperand;

    @Column(name = "out_score", columnDefinition = "int(10) DEFAULT 0")
    private int outScore;

    @Column(name = "is_mandatory", columnDefinition = "int(1) DEFAULT 1")
    private int isMandatory;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

}
