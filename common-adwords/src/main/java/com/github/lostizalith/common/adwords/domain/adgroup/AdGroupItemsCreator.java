package com.github.lostizalith.common.adwords.domain.adgroup;

import com.github.lostizalith.common.adwords.domain.keyword.KeywordCreator;
import com.github.lostizalith.common.adwords.domain.model.AdGroupItem;
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

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdGroupItemsCreator {

    private final KeywordCreator keywordCreator;

    public List<AdGroupItem> create(final AdWordsSession session, final List<AdGroupItem> adGroupItems) {

        final List<AdGroupItem> successGroups = adGroupItems.stream()
                .filter(g -> Objects.nonNull(g.getId()))
                .collect(toList());

        final List<KeywordItem> keywordItems = successGroups.stream()
                .peek(g -> g.getKeywordItems().forEach(k -> k.setAdGroupId(g.getId())))
                .flatMap(g -> g.getKeywordItems().stream())
                .collect(toList());

        final List<KeywordItem> createdKeywordItems = keywordCreator.create(session, keywordItems);
        final Map<Long, List<KeywordItem>> keywordsByGroupsId = createdKeywordItems.stream()
                .collect(toMap(KeywordItem::getAdGroupId, Arrays::asList, CollisionsResolver::concat));

        adGroupItems.forEach(g -> g.setKeywordItems(keywordsByGroupsId.get(g.getId())));

        return adGroupItems;
    }
}
