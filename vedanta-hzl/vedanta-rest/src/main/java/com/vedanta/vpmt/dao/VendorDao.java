package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Vendor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorDao extends JpaRepository<Vendor, Long> {
	@Query("SELECT v from Vendor v WHERE v.vendorCode=?1")
	public Vendor getVendorByVendorCode(String vendorCode);

	@Query("SELECT v from Vendor v WHERE v.businessUnitId=?1")
	public List<Vendor> getAllVendorByBusinessUnitId(Long businessId);

	@Query("SELECT v from Vendor v WHERE v.businessUnitId=?1 AND v.plantCode=?2")
	public List<Vendor> getAllVendorByBusinessUnitIdAndPlantCode(Long businessId, String plantCode);
}
