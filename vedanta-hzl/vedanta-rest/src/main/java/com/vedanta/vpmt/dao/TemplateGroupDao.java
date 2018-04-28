package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.TemplateGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

/**
 * Created by manishsanger on 11/09/17.
 */
@Repository
public interface TemplateGroupDao extends JpaRepository<TemplateGroup, Long> {

	public List<TemplateGroup> findAllByTemplateId(long templateId);

	@Query("SELECT tg FROM TemplateGroup tg WHERE tg.groupId IN (?1)")
	public List<TemplateGroup> getAllTemplateGroupsInGroupsId(Set<Long> groupIds);

	@Query("SELECT tg FROM TemplateGroup  tg , Template t where t.id=tg.templateId AND t.businessUnitId =?1")
	public List<TemplateGroup> getAllTemplateGroupsByBusinessUnitId(Long buId);

}
