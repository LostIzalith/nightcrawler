package com.github.lostizalith.adcreator.domain.adwords.budget;

import com.github.lostizalith.adcreator.domain.adwords.model.enumeration.BudgetDeliveryMethod;
import com.github.lostizalith.adcreator.domain.adwords.model.BudgetItem;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201710.cm.Budget;
import com.google.api.ads.adwords.axis.v201710.cm.BudgetBudgetDeliveryMethod;
import com.google.api.ads.adwords.axis.v201710.cm.BudgetOperation;
import com.google.api.ads.adwords.axis.v201710.cm.BudgetServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Money;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

@Component
public class BudgetCreator {

    private final AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

    public BudgetItem create(final AdWordsSession session, final BudgetItem budgetItem) {
        final BudgetServiceInterface budgetService = adWordsServices.get(session, BudgetServiceInterface.class);

        final Money budgetAmount = new Money();
        budgetAmount.setMicroAmount(budgetItem.getAmount());

        final Budget budget = new Budget();
        budget.setIsExplicitlyShared(false);
        budget.setName(budgetItem.getName());
        budget.setAmount(budgetAmount);
        budget.setDeliveryMethod(getDeliveryMethod(budgetItem.getDeliveryMethod()));

        final BudgetOperation budgetOperation = new BudgetOperation();
        budgetOperation.setOperand(budget);
        budgetOperation.setOperator(Operator.ADD);

        Long budgetId;
        try {
            budgetId = budgetService.mutate(new BudgetOperation[]{budgetOperation}).getValue(0).getBudgetId();
        } catch (RemoteException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        budgetItem.setId(budgetId);

        return budgetItem;
    }

    private static BudgetBudgetDeliveryMethod getDeliveryMethod(final BudgetDeliveryMethod deliveryMethod) {
        return BudgetBudgetDeliveryMethod.fromValue(deliveryMethod.name());
    }
}
