package com.github.lostizalith.adcreator.domain.utils.generator;

import com.github.lostizalith.common.adwords.domain.model.ExpandedTextAdItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Component
public class ExpandedTextAdGenerator {

    public List<ExpandedTextAdItem> generate(final int number) {
        final String finalUrl = "http://github.com";
        return IntStream.range(0, number)
                .mapToObj(i -> ExpandedTextAdItem.anExpandedTextAdItem(
                        UUID.randomUUID().toString().substring(0, 8),
                        UUID.randomUUID().toString().substring(0, 8),
                        UUID.randomUUID().toString().substring(0, 8),
                        finalUrl
                )).collect(toList());
    }
}
