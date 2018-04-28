package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.GroupField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupFieldDao extends JpaRepository<GroupField, Long> {

	public List<GroupField> findAllByGroupId(long groupId);

	public List<GroupField> findAllByStatus(int status);
}
