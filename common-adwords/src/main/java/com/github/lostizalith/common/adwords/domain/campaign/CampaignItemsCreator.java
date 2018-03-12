package com.github.lostizalith.common.adwords.domain.campaign;

import com.github.lostizalith.common.adwords.domain.adgroup.AdGroupCreator;
import com.github.lostizalith.common.adwords.domain.model.AdGroupItem;
import com.github.lostizalith.common.adwords.domain.model.AdWordsItem;
import com.github.lostizalith.common.adwords.domain.model.CampaignItem;
import com.github.lostizalith.common.adwords.domain.model.LocationSettings;
import com.github.lostizalith.common.adwords.domain.targeting.LocationFinder;
import com.github.lostizalith.common.adwords.domain.targeting.LocationSettingsConfigurer;
import com.github.lostizalith.common.utils.CollisionsResolver;
import com.google.api.ads.adwords.axis.v201710.cm.Location;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CampaignItemsCreator {

    private final AdGroupCreator adGroupCreator;
    private final LocationFinder locationFinder;
    private final LocationSettingsConfigurer locationSettingsConfigurer;

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

        final List<String> locationNames = campaignItems.stream()
                .map(CampaignItem::getLocationSettings)
                .filter(Objects::nonNull)
                .map(LocationSettings::getCityName)
                .distinct()
                .collect(toList());

        final Map<String, Location> locations = locationFinder.findLocations(session, locationNames);
        final Map<Long, List<Long>> locationMap = campaignItems.stream()
                .filter(c -> c.getLocationSettings() != null)
                .collect(toMap(AdWordsItem::getId, c -> singletonList(locations.get(c.getLocationSettings().getCityName()).getId())));
        locationMap.forEach((key, value) -> locationSettingsConfigurer.setLocations(session, locationMap));

        return campaignItems;
    }
}
