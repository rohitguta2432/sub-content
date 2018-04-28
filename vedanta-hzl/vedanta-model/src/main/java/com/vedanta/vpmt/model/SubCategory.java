package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishsanger on 22/09/17.
 */
@Entity
@Table(name = "sub_categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SubCategory implements Serializable {
    private static final long serialVersionUID = -592510226531489427L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "sap_category")
    private String sapCategory;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

}
