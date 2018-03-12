package com.github.lostizalith.common.adwords.domain.targeting;

import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Location;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
public class LocationSettingsConfigurer extends TargetingSettingsConfigurer {

    public void setLocations(final AdWordsSession session, final Map<Long, List<Long>> locationIds) {

        final Map<Long, List<Criterion>> criteriaById = locationIds.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
                        .map(id -> {
                            final Location location = new Location();
                            location.setId(id);
                            return (Criterion) location;
                        }).collect(toList())));

        mutate(session, criteriaById);
    }

}
