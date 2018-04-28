package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishsanger on 07/09/17.
 */
@Entity
@Table(name = "forms")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Form implements Serializable {

	private static final long serialVersionUID = -5648753214765476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;
    
    @NotNull
    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "sub_category_id")
    private Long subCategoryId;

    @Transient
    private Category category;

    @Transient
    private SubCategory subCategory;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "day_of_frequency")
    private int dayOfFrequency;

    @Column(name = "due_day_of_frequency")
    private int dueDayOfFrequency;

    @Column(name = "is_admin_form", columnDefinition = "int(1) DEFAULT 1")
    private int isAdminForm;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Transient
    private String userName;
    
    @Transient
    private Boolean isContractManager;
    
    @Transient
    private Boolean isApprover;
    
    @Transient
    private Boolean isAllGroupAccess;

}
