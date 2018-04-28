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
@Table(name = "form_fields")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class FormField implements Serializable {
    private static final long serialVersionUID = -5641033214765476477L;

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

    @Column(name = "weight")
    private Double weight;

    @Column(name = "is_editable", columnDefinition = "tinyint(1) DEFAULT 1")
    private int isEditable;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "form_field_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<FormFieldOption> formFieldOptions;

    @Column(name = "data_unit_id")
    private Long dataUnitId;

    @Transient
    private DataUnit dataUnit;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "form_field_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<FormValidation> formValidations;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "form_field_id", referencedColumnName = "id")
    @Fetch(FetchMode.SELECT)
    private List<FormTargetValue> formTargetValue;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

}
