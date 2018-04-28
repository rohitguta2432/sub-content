package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_form_group")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserFormGroup implements Serializable {

    private static final long serialVersionUID = -5648912678516867817L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @NotNull
    @Column(name = "vendor_id")
    private Long vendorId;

    @NotNull
    @Column(name = "contract_id")
    private Long contractId;

    @NotNull
    @Column(name = "form_id")
    private Long formId;

    @NotNull
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
    private int status;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

}
