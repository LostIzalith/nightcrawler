package com.github.lostizalith.adcreator.domain.adwords;

import com.github.lostizalith.adcreator.domain.adwords.model.SelectorPredicate;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.Page;
import com.google.api.ads.adwords.axis.v201710.cm.Selector;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AdWordsUtils {

    public static final AdWordsServicesInterface AD_WORDS_SERVICES = AdWordsServices.getInstance();

    private static final int PAGE_SIZE = 100;

    public static <T, P extends Page> List<T> fetchItems(final Function<Selector, P> pagesFetcher,
                                                         final Function<P, T[]> itemsFetcher,
                                                         final SelectorPredicate selectorPredicate) {

        if (selectorPredicate == null) {
            throw new IllegalStateException("Selector predicate can'be null");
        }

        final Map<SelectorBuilder, Selector> builder2Selector = buildSelector(selectorPredicate);
        final Map.Entry<SelectorBuilder, Selector> entry = new ArrayList<>(builder2Selector.entrySet()).get(0);
        final SelectorBuilder builder = entry.getKey();
        Selector selector = entry.getValue();

        P page;
        T[] entities;
        int offset = 0;
        final List<T> items = new ArrayList<>();
        do {
            page = pagesFetcher.apply(selector);
            entities = itemsFetcher.apply(page);

            if (ArrayUtils.isEmpty(entities)) {
                break;
            }

            items.addAll(Arrays.asList(entities));

            offset += PAGE_SIZE;
            selector = builder.increaseOffsetBy(PAGE_SIZE).build();
        } while (offset < page.getTotalNumEntries());

        return items;
    }

    private static Map<SelectorBuilder, Selector> buildSelector(final SelectorPredicate selectorPredicate) {
        SelectorBuilder builder = new SelectorBuilder();
        builder = builder.fields(selectorPredicate.getFields());

        if (MapUtils.isNotEmpty(selectorPredicate.getInPredicate())) {
            for (final Map.Entry<String, String[]> predicate : selectorPredicate.getInPredicate().entrySet()) {
                builder = builder.in(predicate.getKey(), predicate.getValue());
            }
        }

        builder = builder
                .offset(0)
                .limit(PAGE_SIZE);

        return Collections.singletonMap(builder, builder.build());
    }

}
