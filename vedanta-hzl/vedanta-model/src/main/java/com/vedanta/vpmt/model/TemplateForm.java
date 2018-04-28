package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishsanger on 07/09/17.
 */
@Entity
@Table(name = "template_form")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class TemplateForm implements Serializable {
    private static final long serialVersionUID = -5648753374885476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "form_id")
    private Long formId;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Transient
    private String formName;
    
    @Transient
    private Boolean isFormSaved;
    
    @Transient
    private Long formSavedId;
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
