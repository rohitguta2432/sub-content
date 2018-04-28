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
@Table(name = "fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Field implements Serializable {
    private static final long serialVersionUID = -5648753214765476477L;

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

    @Column(name = "type")
    private String type;

    @Column(name = "scoring_description")
    private String scoringDescription;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "is_editable", columnDefinition = "tinyint(1) DEFAULT 1")
    private int isEditable;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<FieldOption> fieldOptions;

    @Column(name = "data_unit_id")
    private Long dataUnitId;
    
    @Transient
    private DataUnit dataUnit;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<Validation> validations;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<ScoringCriteria> scoringCriteria;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<TargetValue> targetValue;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

}