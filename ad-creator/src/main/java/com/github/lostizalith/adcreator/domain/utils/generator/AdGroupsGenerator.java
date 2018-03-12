package com.github.lostizalith.adcreator.domain.utils.generator;

import com.github.lostizalith.common.adwords.domain.model.AdGroupItem;
import com.github.lostizalith.common.adwords.domain.model.KeywordItem;
import com.github.lostizalith.common.adwords.domain.model.enumeration.MatchType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class AdGroupsGenerator {

    private final KeywordsGenerator keywordsGenerator;
    private final ExpandedTextAdGenerator expandedTextAdGenerator;

    public List<AdGroupItem> generate(final MatchType matchType, final int number) {
        return IntStream.range(0, number)
                .mapToObj(i -> {
                    final String name = UUID.randomUUID().toString();
                    final AdGroupItem adGroupItem = AdGroupItem.anAdGroupItem(name);
                    adGroupItem.setExpandedTextAdItems(expandedTextAdGenerator.generate(number));
                    adGroupItem.setKeywordItems(keywordsGenerator.generate(matchType, number));
                    return adGroupItem;
                }).collect(toList());
    }
}
