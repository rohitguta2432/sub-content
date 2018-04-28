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
@Table(name = "business_unit")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class BusinessUnit implements Serializable {

    private static final long serialVersionUID = -592510224688544877L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "unit_name")
    private String unitName;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "company_code")
    private String companyCode;
    
    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
