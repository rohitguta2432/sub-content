package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FieldOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldOptionDao extends JpaRepository<FieldOption, Long> {

	public List<FieldOption> findAllByFieldId(Long id);
}
