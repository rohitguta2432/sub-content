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
@Table(name = "notification")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Notification implements Serializable {

	private static final long serialVersionUID = -1073256805697898335L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "userId")
	private String userId;
	
	@Column(name = "scorecardId")
	private Long scorecardId;

	@Column(name = "contractId")
	private Long contractId;

	@Column(name = "contractNo")
	private String contractNo;

	@Column(name = "templateId")
	private Long templateId;

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "status", columnDefinition = "int(1) DEFAULT 0")
	private int status;
	
	@Column(name = "checked", columnDefinition = "int(1) DEFAULT 0")
	private int checked;
	
	@Column(name = "approved", columnDefinition = "int(1) DEFAULT 0")
	private int approved;

	@Column(name = "approvedBy")
	private String approvedBy;


	@Column(name = "assignedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date assignedDate;

	@Column(name = "submitted_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Date submittedOn;
	
	@Column(name = "submittedBy")
	private String submittedBy;
	
	
	@Column(name = "po_item")
	private String poItem;
    
	@Column(name = "userName")
	private String userName;

	@Column(name = "templateName")
	private String templateName;
	
	@Column(name = "groupName")
	private String groupName;
	
	@Column(name = "contract_manager_id")
	private String contractManagerId;

	@Column(name = "contract_manager")
	private String contractManager;



}
