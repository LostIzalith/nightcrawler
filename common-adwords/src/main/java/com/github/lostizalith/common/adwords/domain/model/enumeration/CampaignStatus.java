package com.github.lostizalith.common.adwords.domain.model.enumeration;

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
