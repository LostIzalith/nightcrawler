package com.github.lostizalith.adcreator.domain.generator;

import com.github.lostizalith.common.adwords.domain.model.AdGroupItem;
import com.github.lostizalith.common.adwords.domain.model.BudgetItem;
import com.github.lostizalith.common.adwords.domain.model.CampaignItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CampaignsGenerator {

    public List<CampaignItem> generate(final List<AdGroupItem> adGroupItems,
                                       final int number) {
        final BudgetItem budgetItem = BudgetItem.aBudgetItem("Budget name", 10L);
        return IntStream.range(0, number)
                .mapToObj(i -> {
                    final String name = UUID.randomUUID().toString();
                    final CampaignItem campaignItem = CampaignItem.aCampaignItem(name, budgetItem);
                    campaignItem.setAdGroupItems(adGroupItems);

                    return campaignItem;
                }).collect(Collectors.toList());
    }
}
