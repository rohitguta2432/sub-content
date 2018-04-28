package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

	@Query("SELECT g FROM Group g WHERE g.status=?1")
	List<Group> getFormGroupsByStatus(int status);
}