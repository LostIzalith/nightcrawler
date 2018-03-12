package com.github.lostizalith.common.adwords.domain.targeting;

import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.Location;
import com.google.api.ads.adwords.axis.v201710.cm.LocationCriterion;
import com.google.api.ads.adwords.axis.v201710.cm.LocationCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.AD_WORDS_SERVICES;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
public class LocationFinder {

    private static final String[] FIELDS = new String[]{
            "Id",
            "LocationName",
            "CanonicalName",
            "DisplayType",
            "ParentLocations",
            "Reach",
            "TargetingStatus"
    };

    public Map<String, Location> findLocations(final AdWordsSession session, final List<String> locationNames) {

        final LocationCriterionServiceInterface locationCriterionService =
                AD_WORDS_SERVICES.get(session, LocationCriterionServiceInterface.class);

        final List<String> locations = locationNames.stream()
                .distinct()
                .collect(toList());

        final Selector selector = new SelectorBuilder()
                .fields(FIELDS)
                .in("LocationName", locations.toArray(new String[locationNames.size()]))
                .build();

        LocationCriterion[] locationCriteria = new LocationCriterion[]{};
        try {
            locationCriteria = locationCriterionService.get(selector);
        } catch (RemoteException e) {
            log.error(e.getMessage(), e);
        }

        return Arrays.stream(locationCriteria)
                .distinct()
                .collect(toMap(l -> l.getLocation().getLocationName(), LocationCriterion::getLocation, (a, b) -> a));
    }
}
