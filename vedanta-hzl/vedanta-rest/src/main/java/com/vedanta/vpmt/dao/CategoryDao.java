package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {

	public List<Category> findAllByBusinessUnitId(Long businessUnitId);

	public List<Category> findAllByPlantId(Long plantId);
}
