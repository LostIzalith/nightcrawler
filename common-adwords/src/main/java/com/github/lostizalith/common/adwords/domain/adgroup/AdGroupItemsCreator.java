package com.github.lostizalith.common.adwords.domain.adgroup;

import com.github.lostizalith.common.adwords.domain.AdGroupItemInterface;
import com.github.lostizalith.common.adwords.domain.ad.ExpandedTextAdCreator;
import com.github.lostizalith.common.adwords.domain.keyword.KeywordCreator;
import com.github.lostizalith.common.adwords.domain.model.AdGroupItem;
import com.github.lostizalith.common.adwords.domain.model.ExpandedTextAdItem;
import com.github.lostizalith.common.adwords.domain.model.KeywordItem;
import com.github.lostizalith.common.utils.CollisionsResolver;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdGroupItemsCreator {

    private final KeywordCreator keywordCreator;
    private final ExpandedTextAdCreator expandedTextAdCreator;

    public List<AdGroupItem> create(final AdWordsSession session,
                                    final List<AdGroupItem> adGroupItems) {

        final List<AdGroupItem> successGroups = adGroupItems.stream()
                .filter(g -> Objects.nonNull(g.getId()))
                .collect(toList());

        final List<KeywordItem> keywordItems = getItems(successGroups, AdGroupItem::getKeywordItems);
        final List<KeywordItem> createdKeywordItems = keywordCreator.create(session, keywordItems);
        final Map<Long, List<KeywordItem>> keywordsByGroupsId = createdKeywordItems.stream()
                .collect(toMap(KeywordItem::getAdGroupId, Arrays::asList, CollisionsResolver::concat));

        final List<ExpandedTextAdItem> expandedTextAdItems = getItems(successGroups, AdGroupItem::getExpandedTextAdItems);
        final List<ExpandedTextAdItem> createdAdsItems = expandedTextAdCreator.create(session, expandedTextAdItems);
        final Map<Long, List<ExpandedTextAdItem>> adsByGroupsId = createdAdsItems.stream()
                .collect(toMap(ExpandedTextAdItem::getAdGroupId, Arrays::asList, CollisionsResolver::concat));

        adGroupItems.forEach(g -> {
            g.setKeywordItems(keywordsByGroupsId.get(g.getId()));
            g.setExpandedTextAdItems(adsByGroupsId.get(g.getId()));
        });

        return adGroupItems;
    }

    private static <T extends AdGroupItemInterface> List<T> getItems(final List<AdGroupItem> successGroups, final Function<AdGroupItem, List<T>> itemsExtractor) {
        return successGroups.stream()
                .peek(g -> itemsExtractor.apply(g).forEach(ad -> ad.setAdGroupId(g.getId())))
                .flatMap(g -> itemsExtractor.apply(g).stream())
                .collect(toList());
    }
}
