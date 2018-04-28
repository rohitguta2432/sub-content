package com.vedanta.vpmt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vedanta.vpmt.model.Support;

@Repository
public interface SupportDao extends JpaRepository<Support, Long> {

	@Query("SELECT s FROM Support s WHERE s.status<>?1 GROUP BY  s.tokenNo")
	public List<Support> getSupportList(int status);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Support s set s.status=?1 , s.lastUpdatedOn=CURRENT_TIMESTAMP() where s.id=?2")
	public int updateSupport(@Param("status") Integer status, @Param("id") Long id);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Support s SET s.tokenNo = :tokenNo WHERE s.id = :id")
	int updateToken(@Param("id") Long id, @Param("tokenNo") String tokenNo);

}
