package com.github.lostizalith.adcreator.domain.adwords.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BudgetDeliveryMethod {

    STANDARD("Standard"),

    ACCELERATED("Accelerated");

    private final String name;

}
