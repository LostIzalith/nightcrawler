package com.github.lostizalith.common.adwords.domain.model;

import com.github.lostizalith.common.adwords.domain.model.enumeration.AdChannelType;
import com.github.lostizalith.common.adwords.domain.model.enumeration.CampaignStatus;
import com.github.lostizalith.common.adwords.domain.model.enumeration.GeoTargetType;
import com.github.lostizalith.common.adwords.domain.model.enumeration.StrategyGoalType;
import com.github.lostizalith.common.adwords.domain.model.enumeration.StrategyType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CampaignItem extends AdWordsItem {

    private final String name;

    private final BudgetItem budgetItem;

    private CampaignStatus campaignStatus;

    private StrategyType strategyType;

    private AdChannelType adChannelType;

    private StrategyGoalType strategyGoalType;

    private GeoTargetType positiveGoalTargetType;

    private GeoTargetType negativeGoalTargetType;

    private List<AdGroupItem> adGroupItems;

    private LocationSettings locationSettings;

    public static CampaignItem aCampaignItem(final String name,
                                             final BudgetItem budgetItem) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Campaign name can't be empty");
        }

        if (budgetItem == null) {
            throw new IllegalArgumentException("Campaign budget is mandatory");
        }

        final CampaignItem campaignItem = new CampaignItem(name, budgetItem);
        campaignItem.setCampaignStatus(CampaignStatus.PAUSED);
        campaignItem.setAdChannelType(AdChannelType.SEARCH);

        return campaignItem;
    }
}
