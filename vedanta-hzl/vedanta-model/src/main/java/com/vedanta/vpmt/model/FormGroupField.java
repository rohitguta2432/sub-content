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
@Table(name = "form_group_fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class FormGroupField implements Serializable {
    private static final long serialVersionUID = -5648753214765476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "form_field_id")
    private Long formFieldId;

    @NotNull
    @Column(name = "form_group_detail_id")
    private Long formGroupDetailId;

    @Column(name = "sort_order", columnDefinition = "int(4) DEFAULT 1")
    private int sortOrder;
    
    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
