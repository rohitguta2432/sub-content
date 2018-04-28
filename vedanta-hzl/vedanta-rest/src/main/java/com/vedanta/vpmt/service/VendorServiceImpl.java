package com.vedanta.vpmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.vedanta.vpmt.dao.VendorDao;
import com.vedanta.vpmt.exception.VedantaException;
import com.vedanta.vpmt.model.Category;
import com.vedanta.vpmt.model.Plant;
import com.vedanta.vpmt.model.SubCategory;
import com.vedanta.vpmt.model.Vendor;
import com.vedanta.vpmt.util.VedantaUtility;

import lombok.extern.slf4j.Slf4j;

@Service("VendorService")
@Slf4j
public class VendorServiceImpl implements VendorService {

	@Autowired
	private VendorDao vendorDao;

	@Override
	public Vendor get(long id) {
		if (id <= 0) {
			log.info("Invalid Vendor id.");
			throw new VedantaException("Invalid Vendor id.");
		}
		return vendorDao.findOne(id);
	}

	@Override
	public Vendor save(Vendor entity) {
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Vendor object cannot be null/empty");
			throw new VedantaException("Vendor object cannot be null/empty");
		}
		return vendorDao.save(entity);
	}

	@Override
	public Vendor update(long id, Vendor entity) {
		if (id <= 0) {
			log.info("Invalid Vendor id.");
			throw new VedantaException("Invalid user id.");
		}
		if (ObjectUtils.isEmpty(entity)) {
			log.info("Vendor object cannot be null/empty.");
			throw new VedantaException("Vendor object cannot be null/empty.");
		}

		Vendor existingEntity = vendorDao.findOne(id);
		if (!ObjectUtils.isEmpty(existingEntity)) {
			log.info("Vendor sobject already exist.");
			throw new VedantaException("Vendor object already exist.");
		}
		return null;
	}

	@Override
	public List<Vendor> getAllVendors() {
		try {
			List<Vendor> Vendor = null;
			// fetching vendor on the basis of business unit id and plant code
			if (VedantaUtility.getCurrentUserBuId() == 0
					&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
				Vendor = vendorDao.findAll();
			} else if (VedantaUtility.getCurrentUserBuId() != 0
					&& Integer.valueOf(VedantaUtility.getCurrentUserPlantCode()) == 0) {
				Vendor = vendorDao.getAllVendorByBusinessUnitId(VedantaUtility.getCurrentUserBuId());
			} else {
				Vendor = vendorDao.getAllVendorByBusinessUnitIdAndPlantCode(VedantaUtility.getCurrentUserBuId(),
						VedantaUtility.getCurrentUserPlantCode());
			}
			return Vendor;
		} catch (VedantaException e) {
			String msg = "Error while getting all Vendor by web";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Vendor getVendorDetailByVendorCode(String vendorCode) {
		if (StringUtils.isEmpty(vendorCode)) {
			log.info("Vendor code cannot be null/empty");
			throw new VedantaException("Vendor code cannot be null/empty");
		}

		try {
			Vendor vendorDetail = vendorDao.getVendorByVendorCode(vendorCode);
			return vendorDetail;
		} catch (VedantaException e) {
			String msg = "Error while getting Vendor by vendor code";
			log.error(msg);
			throw new VedantaException(msg);
		}
	}

	@Override
	public Vendor getVendor(SubCategory subCategory, Category category, String vendorCode, String vendorName,
			Plant plant) {
		Vendor vendor = getVendorDetailByVendorCode(vendorCode);
		if (vendor != null) {
			return vendor;
		}
		vendor = new Vendor();
		vendor.setCategoryId(category.getId());
		vendor.setName(vendorName);
		vendor.setCategoryName(category.getCategoryName());
		vendor.setSubCategoryId(subCategory.getId());
		vendor.setSubCategoryName(subCategory.getCategoryName());
		vendor.setVendorCode(vendorCode);
		vendor.setBusinessUnitId(subCategory.getBusinessUnitId());
		vendor.setPlantCode(plant.getPlantCode());
		return save(vendor);
	}

	@Override
	public List<Vendor> getAllVendorsByBusinessUnit(Long buId) {
		try {
			List<Vendor> Vendor = null;
			Vendor = vendorDao.getAllVendorByBusinessUnitId(buId);
			return Vendor;
		} catch (VedantaException e) {
			String msg = "Error while getting all Vendor by buId";
			log.error(msg);
			throw new VedantaException(msg);
		}

	}
}
