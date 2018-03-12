package com.github.lostizalith.common.adwords.domain.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BudgetDeliveryMethod {

    STANDARD("Standard"),

    ACCELERATED("Accelerated");

    private final String value;

}
