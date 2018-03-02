package com.github.lostizalith.common.adwords.domain.campaign;

import com.github.lostizalith.common.adwords.domain.adgroup.AdGroupCreator;
import com.github.lostizalith.common.adwords.domain.model.AdGroupItem;
import com.github.lostizalith.common.adwords.domain.model.CampaignItem;
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
public class CampaignItemsCreator {

    private final AdGroupCreator adGroupCreator;

    public List<CampaignItem> create(final AdWordsSession session, final List<CampaignItem> campaignItems) {

        final List<CampaignItem> successCampaigns = campaignItems.stream()
                .filter(g -> Objects.nonNull(g.getId()))
                .collect(toList());

        final List<AdGroupItem> adGroupItems = successCampaigns.stream()
                .peek(c -> c.getAdGroupItems().forEach(g -> g.setCampaignId(c.getId())))
                .flatMap(c -> c.getAdGroupItems().stream())
                .collect(toList());

        final List<AdGroupItem> createdAdGroupItems = adGroupCreator.create(session, adGroupItems);
        final Map<Long, List<AdGroupItem>> adGroupsByCampaignId = createdAdGroupItems.stream()
                .collect(toMap(AdGroupItem::getCampaignId, Arrays::asList, CollisionsResolver::concat));

        campaignItems.forEach(c -> c.setAdGroupItems(adGroupsByCampaignId.get(c.getId())));

        return campaignItems;
    }
}
