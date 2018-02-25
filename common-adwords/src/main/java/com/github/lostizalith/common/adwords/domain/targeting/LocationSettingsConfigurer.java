package com.github.lostizalith.common.adwords.domain.targeting;

import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Location;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class LocationSettingsConfigurer extends TargetingSettingsConfigurer {

    public void setLocations(final AdWordsSession session, final Long campaignId, final List<Long> locationIds) {

        final List<Criterion> criteria = locationIds.stream()
                .map(id -> {
                    final Location language = new Location();
                    language.setId(id);
                    return language;
                }).collect(toList());

        mutate(session, campaignId, criteria);
    }

}
