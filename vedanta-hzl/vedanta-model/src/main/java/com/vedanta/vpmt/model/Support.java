package com.vedanta.vpmt.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "support")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Support implements Serializable {

	private static final long serialVersionUID = -6616611486833944085L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "employee_id")
	private String employeeId;

	@Column(name = "business_unit_id", columnDefinition = "bigint(20) default 0")
	private Long businessUnitId = 0L;
	
	@Column(name = "message",length=4096)
	private String message;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "token_no")
	private String tokenNo;
	
    @Column(name = "title")
	private String title;
    
    @Column(name = "status", columnDefinition = "int(1) DEFAULT 0")
	private int status;
    
    
    @Transient
    @JsonIgnore
    private List<MultipartFile> files;
    
    @Transient
    private List<String> fileList;
    
    @Column(name = "file_name")
    private String fileName;
    
    
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    
    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;
    
    /*@Transient
    @Column(name = "business_unit_name")
	private String businessUnitName ;
	*/
       
}
