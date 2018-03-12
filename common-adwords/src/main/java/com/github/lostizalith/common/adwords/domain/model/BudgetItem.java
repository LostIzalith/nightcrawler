package com.github.lostizalith.common.adwords.domain.model;

import com.github.lostizalith.common.adwords.domain.model.enumeration.BudgetDeliveryMethod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BudgetItem extends AdWordsItem {

    private final String name;

    private final Long amount;

    private BudgetDeliveryMethod deliveryMethod;

    private boolean isShare;

    public static BudgetItem aBudgetItem(final String name,
                                         final Long amount) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Budget name can't be empty");
        }

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Budget amount must be greater then 0");
        }

        final BudgetItem budgetItem = new BudgetItem(name, amount);
        budgetItem.setDeliveryMethod(BudgetDeliveryMethod.STANDARD);
        budgetItem.setShare(false);

        return budgetItem;
    }
}
