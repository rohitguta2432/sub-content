package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author RITESH SINGH
 * @since  14-sept-17
 */
@Entity
@Table(name = "form_field_option")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class FormFieldOption implements Serializable {
    private static final long serialVersionUID = -5648753214765476477L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "form_field_id")
    private Long formFieldId;

    @Column(name = "option_name")
    private String optionName;

	@Column(name = "option_value")
    private String optionValue;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
