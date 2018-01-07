package com.github.lostizalith.adcreator.domain.adwords.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Budget {

    private final long amount;

    private final String deliveryMethod;

    private final boolean isShare;
}
