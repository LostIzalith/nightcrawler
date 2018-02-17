package com.github.lostizalith.adcreator.domain.adwords.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyType {

    MANUAL_CPC("Manual CPC"),

    MANUAL_CPM("Manual CPM"),

    PAGE_ONE_PROMOTED("Page one promoted"),

    TARGET_SPEND("Target spend"),

    ENHANCED_CPC("Enhanced CPC"),

    TARGET_CPA("Target CPA"),

    TARGET_ROAS("Target ROAS"),

    MAXIMIZE_CONVERSIONS("Maximize conversions"),

    TARGET_OUTRANK_SHARE("Target outrank share");

    private final String value;
}
