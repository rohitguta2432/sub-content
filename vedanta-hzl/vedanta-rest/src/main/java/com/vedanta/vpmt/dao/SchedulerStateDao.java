package com.vedanta.vpmt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.SchedulerState;

@Repository
public interface SchedulerStateDao extends JpaRepository<SchedulerState, String> {

	public SchedulerState findOneBySchedulerName(String schedulerName);

}
