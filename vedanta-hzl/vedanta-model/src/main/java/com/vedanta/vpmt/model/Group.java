package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by manishsanger on 07/09/17.
 */
@Entity
@Table(name = "groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Group implements Serializable {
    private static final long serialVersionUID = -5648753214765106477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "label_name")
    private String labelName;

    @Column(name = "description")
    private String description;
    
    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Transient
    private List<Field> fields;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Transient
    private String userName;
    
}
