package com.github.lostizalith.adcreator.domain.generator;

import com.github.lostizalith.common.adwords.domain.model.KeywordItem;
import com.github.lostizalith.common.adwords.domain.model.enumeration.MatchType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class KeywordsGenerator {

    public List<KeywordItem> generate(final MatchType matchType, final int number) {
        return IntStream.range(0, number)
                .mapToObj(i -> {
                    final String text = UUID.randomUUID().toString();
                    return KeywordItem.aKeywordItem(text, matchType);
                }).collect(Collectors.toList());
    }
}
