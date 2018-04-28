package com.vedanta.vpmt.service;

import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.model.Vendor;

import java.util.List;

public interface VendorService extends VedantaService<Vendor> {

	public List<Vendor> getAllVendors();

	public Vendor getVendorDetailByVendorCode(String vendorCode);

	public List<Vendor> getAllVendorsByBusinessUnit(Long buId);

	Vendor getVendor(SubCategory subCategory, Category category, String vendorCode, String vendorName, Plant plant);
}
