package com.vedanta.vpmt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name="scheduler_log")
public class SchedulerLog {
	
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long Id;
	
	@Column(name="scheduler_id")
	private Long schedulerId;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="created_on")
	private Date createdOn;
	
	@Column(name="created_by")
	private Long createdBy;
	
	@Column(name="status")
	private int status;
	
	@Column(name="message")
	private String message;
	
	@Column(name="business_unit_id")
	private Long businessUnitId;
	
}
