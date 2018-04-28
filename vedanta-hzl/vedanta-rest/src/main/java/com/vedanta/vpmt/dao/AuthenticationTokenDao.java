package com.vedanta.vpmt.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedanta.vpmt.model.AuthenticationToken;

@Repository
public interface AuthenticationTokenDao extends JpaRepository<AuthenticationToken, Long> {

	@Modifying
	@Query("UPDATE AuthenticationToken SET lastAccessed=?2 WHERE id=?1")
	public void updateLastAccessedTime(long tokenId, Date lastAccessedTime);

	public AuthenticationToken findOneByUserId(long userId);

	@Query("SELECT at from AuthenticationToken at  WHERE userId=?1 ORDER BY  at.id DESC")
	public List<AuthenticationToken> getAuthToken(long userId);

	public void deleteByUserId(long userId);

	@Modifying
	@Transactional
	@Query(value = "truncate table auth_token", nativeQuery = true)
	public void truncateAuthTokenUser();

}
