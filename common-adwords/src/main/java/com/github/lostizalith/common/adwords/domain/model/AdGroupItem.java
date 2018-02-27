package com.github.lostizalith.common.adwords.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdGroupItem extends AdWordsItem {

    private final String name;

    private Long campaignId;

    private Long bidAmount;

    private List<KeywordItem> keywordItems;

    public static AdGroupItem anAdGroupItem(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("AdGroup name can't be empty");
        }

        return new AdGroupItem(name);
    }
}
