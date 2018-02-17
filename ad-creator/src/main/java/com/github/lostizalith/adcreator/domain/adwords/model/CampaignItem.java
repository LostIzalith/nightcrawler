package com.github.lostizalith.adcreator.domain.adwords.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignItem extends AdWordsItem {

    private String name;

    private BudgetItem budgetItem;

    private CampaignStatus campaignStatus;

    private StrategyType strategyType;

    private AdChannelType adChannelType;

    private StrategyGoalType strategyGoalType;

    private GeoTargetType positiveGoalTargetType;

    private GeoTargetType negativeGoalTargetType;
}
