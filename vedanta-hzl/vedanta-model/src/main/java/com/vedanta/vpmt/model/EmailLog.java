package com.vedanta.vpmt.model;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="email_log")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Getter
@Setter
@NoArgsConstructor
public class EmailLog implements Serializable{

@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;

@Transient
private List<Long> scorecard_users;

@Column(name = "surveytype_Id")
private Long survetypeId;

@Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
private Long businessUnitId = 0L;

@Column(name="survey_vendorId")
private Long surveyvendorId;

@Column(name="survey_contractId")
private Long surveycontractId;

@Column(name="survey_plantId")
private Long surveyplant_id;

@Column(name="assigned_userId")
private Long assigneduserid;

@Column(name="assigned_userEmailId")
private String assignedUserEmailid;

@Column(name="assigned_userGroupId")
private String assignedUserGroupid;

@Column(name="user_Message")
private String userMessage;

@Column(name="scorecard_contractManagerEmailId")
private String scorecardContractmanageremailid;

@Column(name="cc_userEmailId")
private String ccUseremailid;

@Column(name="stage")
private int stage;

@Column(name = "status", columnDefinition = "int(1) DEFAULT 0")
private int status;

@Column(name = "created_on")
private java.util.Date createdOn;

@Column(name = "created_by")
private String createdBy;

@Transient
private Long SendByUserId;

}
