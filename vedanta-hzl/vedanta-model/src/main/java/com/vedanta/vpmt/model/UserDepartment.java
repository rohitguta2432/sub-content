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

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_department")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserDepartment implements Serializable {

	private static final long serialVersionUID = 9052798616429675207L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "department_name")
	private String departmentName;

	@Column(name = "office_name")
	private String officeName;
	
	@Column(name="businessUnitId")
	private Long businessUnitId;

	@Column(name = "last_updated", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;

}
