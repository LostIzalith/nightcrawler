package com.github.lostizalith.common.adwords.domain.model;

import com.github.lostizalith.common.adwords.domain.model.enumeration.MatchType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordItem extends AdWordsItem {

    private String text;

    private MatchType matchType;

    private Long adGroupId;

    private String finalUrl;

    private Long cpcBid;
}
