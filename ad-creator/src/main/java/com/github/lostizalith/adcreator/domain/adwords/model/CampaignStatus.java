package com.github.lostizalith.adcreator.domain.adwords.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CampaignStatus {

    ENABLED("Enabled"),

    PAUSED("Paused"),

    REMOVED("Removed");

    private final String value;
}
