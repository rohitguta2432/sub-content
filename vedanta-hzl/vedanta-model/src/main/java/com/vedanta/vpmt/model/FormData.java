package com.vedanta.vpmt.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishsanger on 07/09/17.
 */

@Getter
@Setter
public class FormData implements Serializable {
    private static final long serialVersionUID = -5648753214765476477L;

    private Long id;

    private Long formSavedId;

    private Long formFieldId;

    private Long groupId;

    private String targetValue;

    private String value;

    private String lastValue;

    private String remarks;

    private Long vendorId;

    private Long contractId;

    private Long departmentId;

    private Long userId;

    private int status;

    @Temporal(TemporalType.DATE)
    private Date savedOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
}
