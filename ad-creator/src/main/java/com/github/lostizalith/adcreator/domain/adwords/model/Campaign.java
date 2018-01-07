package com.github.lostizalith.adcreator.domain.adwords.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Campaign {

    private String name;

    private Budget budget;

    private CampaignStatus status;

    private StrategyType strategyType;
}
