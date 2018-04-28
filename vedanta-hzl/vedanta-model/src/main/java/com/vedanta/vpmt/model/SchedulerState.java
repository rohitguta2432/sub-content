package com.vedanta.vpmt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "scheduler_state")
@Getter
@Setter
@NoArgsConstructor
public class SchedulerState {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	@Column(name = "scheduler_name", unique = true)
	private String schedulerName;

	@Column(name = "state", length = 10485760)
	private String state;

	private Date createDate;

	private Date updateDate;

	public SchedulerState(String schedulerName, String state) {
		this.schedulerName = schedulerName;
		this.state = state;
	}

	@PrePersist
	protected void onCreate() {
		createDate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		updateDate = new Date();
	}

}
