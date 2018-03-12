package com.github.lostizalith.common.adwords.domain.targeting;

import com.github.lostizalith.common.adwords.domain.model.ProximitySettings;
import com.google.api.ads.adwords.axis.v201710.cm.Address;
import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Proximity;
import com.google.api.ads.adwords.axis.v201710.cm.ProximityDistanceUnits;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class ProximitySettingsConfigurer extends TargetingSettingsConfigurer {

    public void setProximity(final AdWordsSession session, final Map<Long, List<ProximitySettings>> proximitySettings) {

        final Map<Long, List<Criterion>> criteria = proximitySettings.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, ps -> ps.getValue().stream()
                        .map(s -> {
                            final Address address = new Address();
                            address.setStreetAddress(s.getStreetAddress());
                            address.setCityName(s.getCityName());
                            address.setPostalCode(s.getPostalCode());
                            address.setCountryCode(s.getCountryCode());

                            final Proximity proximity = new Proximity();
                            proximity.setAddress(address);
                            proximity.setRadiusDistanceUnits(ProximityDistanceUnits.KILOMETERS);
                            proximity.setRadiusInUnits(s.getRadius());

                            return (Criterion) proximity;
                        }).collect(toList())));

        mutate(session, criteria);
    }
}
