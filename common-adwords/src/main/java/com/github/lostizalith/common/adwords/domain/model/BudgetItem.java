package com.github.lostizalith.common.adwords.domain.model;

import com.github.lostizalith.common.adwords.domain.model.enumeration.BudgetDeliveryMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BudgetItem extends AdWordsItem {

    private final String name;

    private final long amount;

    private final BudgetDeliveryMethod deliveryMethod;

    private final boolean isShare;
}
