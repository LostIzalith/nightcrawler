package com.github.lostizalith.common.adwords.domain.model;

import com.github.lostizalith.common.adwords.domain.model.enumeration.MatchType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class KeywordItem extends AdWordsItem {

    private final String text;

    private final MatchType matchType;

    private Long adGroupId;

    private String finalUrl;

    private Long cpcBid;

    public static KeywordItem aKeywordItem(final String text,
                                           final MatchType matchType) {

        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Keyword text can't be empty");
        }

        if (matchType == null) {
            throw new IllegalArgumentException("Keyword match type is mandatory");
        }

        return new KeywordItem(text, matchType);

    }
}
