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
@Table(name = "plants")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Plant implements Serializable {

    private static final long serialVersionUID = -5925657766865476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "plant_code")
    private String plantCode;

    @NotNull
    @Column(name = "business_unit_id")
    private Long businessUnitId;

    @Column(name = "city")
    private String city;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 0")
    private int status;

    @Column(name = "country")
    private String country;

    @Column(name = "pin")
    private String pin;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

}
