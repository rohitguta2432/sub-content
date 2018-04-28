package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.DataUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by manishsanger on 26/09/17.
 */
@Repository
public interface DataUnitDao extends JpaRepository<DataUnit, Long> {

	@Query("SELECT du FROM DataUnit du WHERE du.status<>?1")
	public List<DataUnit> getAllDataUnits(int statusDelete);
}
