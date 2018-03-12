package com.github.lostizalith.adcreator.domain.utils.generator;

import com.github.lostizalith.common.adwords.domain.model.BudgetItem;
import com.github.lostizalith.common.adwords.domain.model.CampaignItem;
import com.github.lostizalith.common.adwords.domain.model.LocationSettings;
import com.github.lostizalith.common.adwords.domain.model.enumeration.MatchType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class CampaignsGenerator {

    private final AdGroupsGenerator adGroupsGenerator;

    public List<CampaignItem> generate(final MatchType matchType, final int number) {
        final BudgetItem budgetItem = BudgetItem.aBudgetItem("Budget name", 10L);
        return IntStream.range(0, number)
                .mapToObj(i -> {
                    final String name = UUID.randomUUID().toString();
                    final CampaignItem campaignItem = CampaignItem.aCampaignItem(name, budgetItem);
                    campaignItem.setAdGroupItems(adGroupsGenerator.generate(matchType, number));

                    final LocationSettings locationSettings = new LocationSettings();
                    locationSettings.setCountryName("Germany");
                    locationSettings.setCityName("Berlin");

                    campaignItem.setLocationSettings(locationSettings);

                    return campaignItem;
                }).collect(Collectors.toList());
    }
}
