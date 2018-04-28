package com.vedanta.vpmt.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by manishsanger on 11/10/17.
 */
@Getter
@Setter
@AllArgsConstructor
public class ScoreCardGroupUserMapper {
    String contractNumber;
    String contractManagerId;
    String poItem;
    Long templateId;
    Long groupId;

}
