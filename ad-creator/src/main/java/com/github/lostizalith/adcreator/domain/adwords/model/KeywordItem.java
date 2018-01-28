package com.github.lostizalith.adcreator.domain.adwords.model;

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
