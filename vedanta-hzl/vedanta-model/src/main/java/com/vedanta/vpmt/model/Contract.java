package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishsanger on 07/09/17.
 */
@Entity
@Table(name = "contracts")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract implements Serializable {

    private static final long serialVersionUID = -5648912678516867817L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "number")
    private String number;
    
    @NotNull
    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "vendor_code")
    private String vendorCode;

    @Column(name = "vendor_name")
    private String vendorName;

	@Column(name = "description")
    private String description;

    @Column(name = "value", columnDefinition = "DECIMAL(20,2) DEFAULT '0.0'")
    private Double value;

    @Column(name = "year_id")
    private Long yearId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "plant_id")
    private Long plantId;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "preq_no")
    private String preqNo;

//    @Column(name = "zz_scorecard")
//    private String zzScorecard;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Column(name = "sub_category_name")
    private String subCategoryName;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    
}
