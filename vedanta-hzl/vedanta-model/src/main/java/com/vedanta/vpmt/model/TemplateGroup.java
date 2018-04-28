package com.vedanta.vpmt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by manishsanger on 07/09/17.
 */
@Entity
@Table(name = "template_groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TemplateGroup implements Serializable {
    private static final long serialVersionUID = -5648723148374563477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "template_id")
    private Long templateId;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;

    @Transient
    private Group group;

    @Column(name = "sort_order", columnDefinition = "int(4) DEFAULT 1")
    private int sortOrder;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Transient
    private String userName;
    
}
