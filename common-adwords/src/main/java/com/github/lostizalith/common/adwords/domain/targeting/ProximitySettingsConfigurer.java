package com.github.lostizalith.common.adwords.domain.targeting;

import com.github.lostizalith.common.adwords.domain.model.ProximitySettings;
import com.google.api.ads.adwords.axis.v201710.cm.Address;
import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Proximity;
import com.google.api.ads.adwords.axis.v201710.cm.ProximityDistanceUnits;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ProximitySettingsConfigurer extends TargetingSettingsConfigurer {

    public void setProximity(final AdWordsSession session, final Long campaignId, final List<ProximitySettings> proximitySettings) {

        final List<Criterion> proximities = proximitySettings.stream()
                .map(ps -> {
                    final Address address = new Address();
                    address.setStreetAddress(ps.getStreetAddress());
                    address.setCityName(ps.getCityName());
                    address.setPostalCode(ps.getPostalCode());
                    address.setCountryCode(ps.getCountryCode());

                    final Proximity proximity = new Proximity();
                    proximity.setAddress(address);
                    proximity.setRadiusDistanceUnits(ProximityDistanceUnits.KILOMETERS);
                    proximity.setRadiusInUnits(ps.getRadius());

                    return proximity;
                }).map(p -> (Criterion) p)
                .collect(toList());

        mutate(session, campaignId, proximities);
    }
}
