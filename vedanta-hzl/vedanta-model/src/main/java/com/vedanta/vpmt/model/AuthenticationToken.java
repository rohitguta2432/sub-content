package com.vedanta.vpmt.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth_token")
@Getter
@Setter
@NoArgsConstructor
public class AuthenticationToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "auth_token", nullable = false, length = 500)
	private String authToken;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "last_accessed")
	private Date lastAccessed;

	@Column(name = "is_admin_token", columnDefinition = "tinyint(1) DEFAULT 0")
	private boolean isAdminToken;

	@Column(name = "device_token")
	private String deviceToken;
	
	@Column(name="business_unit_id")
	private Long businessUnitId;

	public AuthenticationToken(long userId, String authToken, boolean isAdminToken, String deviceToken,Long businessUnitId) {
		this.userId = userId;
		this.authToken = authToken;
		this.isAdminToken = isAdminToken;
		this.deviceToken = deviceToken;
		this.businessUnitId=businessUnitId;
	}

	public AuthenticationToken(long userId, String authToken, boolean isAdminToken,Long businessUnitId) {
		this.userId = userId;
		this.authToken = authToken;
		this.isAdminToken = isAdminToken;
		this.businessUnitId=businessUnitId;
	}

	@PrePersist
	protected void onCreate() {
		createDate = new Date();
		lastAccessed = new Date();
	}

}
