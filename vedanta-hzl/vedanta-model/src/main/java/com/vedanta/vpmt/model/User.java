package com.vedanta.vpmt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class User implements Serializable {

	private static final long serialVersionUID = -5925102261368544877L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	private String name;

	@NotNull
	private String authorities;

	@Column(name = "employee_id")
	private String employeeId;

	@Column(name = "parent_id")
	private String parentId;

    @JsonProperty(value = "username")
    @Column(name = "login_id")
	private String loginId;

    @Column(name = "plant_id", columnDefinition = "bigint(20) default 0")
    private Long plantId = 0L;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "plant_code")
    private String plantCode;

    @Column(name = "department_id", columnDefinition = "bigint(20) default 0")
    private Long departmentId = 0L;

    @Column(name = "department_name")
    private String departmentName;   

    @Column(name = "business_unit_id", columnDefinition = "bigint(20) default 1")
    private Long businessUnitId = 1L;

    @Column(name = "department_code")
    private String departmentCode;


    @Column(name= "status", columnDefinition = "tinyint(1) DEFAULT 0")
    private int status;

    @Column(name= "is_without_ad_allowed", columnDefinition = "tinyint(1) DEFAULT 0")
    private int isWithoutAdAllowed;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
	private String password;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "city")
    private String city;

    @Column(name = "company")
    private String company;

    @Column(name = "office")
    private String office;

    @Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;
    
    @Transient
    private Long ldapBusinessUnitId;
    
}
