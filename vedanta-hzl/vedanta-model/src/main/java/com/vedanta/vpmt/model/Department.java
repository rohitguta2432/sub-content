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
@Table(name = "departments")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Department implements Serializable {
    private static final long serialVersionUID = -5925657766238567817L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "description")
    private String description;

    @Column(name = "department_code")
    private String departmentCode;

    @NotNull
    @Column(name = "plant_id")
    private Long plantId;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
