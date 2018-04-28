package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.FormGroupDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormGroupDetailDao extends JpaRepository<FormGroupDetail, Long> {

	List<FormGroupDetail> getFormGroupDetailsByStatus(int status);

}
