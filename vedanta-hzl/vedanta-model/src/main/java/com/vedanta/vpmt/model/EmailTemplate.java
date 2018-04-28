package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "email_template")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class EmailTemplate implements Serializable {

	private static final long serialVersionUID = 7319434978181540872L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "template_name")
	private String name;

	@NotNull
	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "email_subject")
	private String subject;

	@Column(name = "msg_content",length=4096)
	private String msgContent;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
    private Long businessUnitId = 0L;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "department_code")
    private String departmentCode;


    @Column(name = "status", columnDefinition = "int(1) DEFAULT 1")
	private int status;

	@Column(name = "last_update")
	@Temporal(TemporalType.DATE)
	private Date lastUpdate;

	@Column(name = "created_on", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;	
}
