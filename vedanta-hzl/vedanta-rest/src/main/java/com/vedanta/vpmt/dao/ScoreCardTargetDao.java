package com.vedanta.vpmt.dao;

import com.vedanta.vpmt.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by manishsanger on 11/09/17.
 */
public interface ScoreCardTargetDao extends JpaRepository<Group, Long> {
}
