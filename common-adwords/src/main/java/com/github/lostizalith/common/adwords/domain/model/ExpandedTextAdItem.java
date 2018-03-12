package com.github.lostizalith.common.adwords.domain.model;

import com.github.lostizalith.common.adwords.domain.AdGroupItemInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExpandedTextAdItem extends AdWordsItem implements AdGroupItemInterface {

    private final String headline1;

    private final String headline2;

    private final String description;

    private final String finalUrl;

    private Long adGroupId;

    public static ExpandedTextAdItem anExpandedTextAdItem(final String headline1,
                                                          final String headline2,
                                                          final String description,
                                                          final String finalUrl) {

        if (StringUtils.isBlank(headline1)) {
            throw new IllegalArgumentException("Headline1 can't be empty");
        }

        if (StringUtils.isBlank(headline2)) {
            throw new IllegalArgumentException("Headline2 can't be empty");
        }

        if (StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("Description can't be empty");
        }

        if (StringUtils.isBlank(finalUrl)) {
            throw new IllegalArgumentException("Final URL can't be empty");
        }

        return new ExpandedTextAdItem(headline1, headline2, description, finalUrl);
    }
}
