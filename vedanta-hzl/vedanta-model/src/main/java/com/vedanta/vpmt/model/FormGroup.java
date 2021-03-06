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
@Table(name = "form_groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class FormGroup implements Serializable{
    private static final long serialVersionUID = -5648723148765476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "form_id")
    private Long formId;

    @NotNull
    @Column(name = "form_group_detail_id")
    private Long formGroupDetailId;

    @Transient
    private FormGroupDetail formGroupDetail;

    @Column(name = "sort_order", columnDefinition = "int(4) DEFAULT 1")
    private int sortOrder;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;
    
    @Transient
    private String userName;
    
    /*@Transient
    private String formName;*/
}
