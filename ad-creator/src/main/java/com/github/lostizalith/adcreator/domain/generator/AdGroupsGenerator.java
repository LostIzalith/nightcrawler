package com.github.lostizalith.adcreator.domain.generator;

import com.github.lostizalith.common.adwords.domain.model.AdGroupItem;
import com.github.lostizalith.common.adwords.domain.model.KeywordItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Component
public class AdGroupsGenerator {

    public List<AdGroupItem> generate(final List<KeywordItem> keywordItems,
                                      final int number) {
        return IntStream.range(0, number)
                .mapToObj(i -> {
                    final String name = UUID.randomUUID().toString();
                    final AdGroupItem adGroupItem = AdGroupItem.anAdGroupItem(name);
                    adGroupItem.setKeywordItems(keywordItems);

                    return adGroupItem;
                }).collect(toList());
    }
}
