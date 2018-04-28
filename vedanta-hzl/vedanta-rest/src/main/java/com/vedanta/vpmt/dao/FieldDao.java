package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldDao extends JpaRepository<Field, Long> {

	@Query("SELECT f FROM Field f WHERE f.status=?1")
	public List<Field> getAllFields(int status);

}
