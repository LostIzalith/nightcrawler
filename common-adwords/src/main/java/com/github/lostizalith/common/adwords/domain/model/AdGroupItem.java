package com.github.lostizalith.common.adwords.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class AdGroupItem extends AdWordsItem {

    private String name;

    private Long campaignId;

    private Long bidAmount;

}
