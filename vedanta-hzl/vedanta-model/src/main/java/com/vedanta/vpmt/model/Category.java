package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by manishsanger on 07/09/17.
 */
@Entity
@Table(name = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Category implements Serializable {

    private static final long serialVersionUID = -592510226348544877L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "sap_scorecard_type")
    private String sapScorecardType;

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

    //    @Transient
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @Fetch(FetchMode.SELECT)
    private List<SubCategory> subCategories;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
