package com.github.lostizalith.adcreator.domain.adwords.campaign;

import com.github.lostizalith.adcreator.domain.adwords.budget.BudgetCreator;
import com.github.lostizalith.adcreator.domain.adwords.model.AdChannelType;
import com.github.lostizalith.adcreator.domain.adwords.model.BudgetItem;
import com.github.lostizalith.adcreator.domain.adwords.model.CampaignItem;
import com.github.lostizalith.adcreator.domain.adwords.model.StrategyType;
import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.axis.v201710.cm.AdvertisingChannelType;
import com.google.api.ads.adwords.axis.v201710.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201710.cm.BiddingStrategyType;
import com.google.api.ads.adwords.axis.v201710.cm.Budget;
import com.google.api.ads.adwords.axis.v201710.cm.Campaign;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignOperation;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignReturnValue;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignStatus;
import com.google.api.ads.adwords.axis.v201710.cm.ManualCpcBiddingScheme;
import com.google.api.ads.adwords.axis.v201710.cm.NetworkSetting;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CampaignCreator {

    private final BudgetCreator budgetCreator;

    private final AdWordsServicesInterface adWordsServices = AdWordsServices.getInstance();

    public CampaignItem create(final AdWordsSession session, final CampaignItem campaignItem) {

        final CampaignServiceInterface campaignService = adWordsServices.get(session, CampaignServiceInterface.class);

        final Campaign campaign = new Campaign();
        campaign.setName(campaignItem.getName());
        campaign.setStatus(CampaignStatus.PAUSED);

        final BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
        biddingStrategyConfiguration.setBiddingStrategyType(getBiddingStrategyType(campaignItem.getStrategyType()));

        final ManualCpcBiddingScheme cpcBiddingScheme = new ManualCpcBiddingScheme();
        cpcBiddingScheme.setEnhancedCpcEnabled(false);
        biddingStrategyConfiguration.setBiddingScheme(cpcBiddingScheme);

        campaign.setBiddingStrategyConfiguration(biddingStrategyConfiguration);

        final BudgetItem budgetItem = budgetCreator.create(session, campaignItem.getBudgetItem());
        final Budget budget = new Budget();
        budget.setBudgetId(budgetItem.getId());
        campaign.setBudget(budget);

        campaign.setAdvertisingChannelType(getAdvertisingChannelType(campaignItem.getAdChannelType()));

        // Set the campaign network options to Search and Search Network.
        final NetworkSetting networkSetting = new NetworkSetting();
        networkSetting.setTargetGoogleSearch(true);
        networkSetting.setTargetSearchNetwork(true);
        networkSetting.setTargetContentNetwork(false);
        networkSetting.setTargetPartnerSearchNetwork(false);
        campaign.setNetworkSetting(networkSetting);

        // Create operations.
        final CampaignOperation operation = new CampaignOperation();
        operation.setOperand(campaign);
        operation.setOperator(Operator.ADD);

        final CampaignOperation[] operations = new CampaignOperation[]{operation};

        CampaignReturnValue result;
        try {
            result = campaignService.mutate(operations);
            campaignItem.setId(result.getValue(0).getId());
        } catch (RemoteException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        return campaignItem;
    }

    private static BiddingStrategyType getBiddingStrategyType(final StrategyType strategyType) {
        return BiddingStrategyType.fromValue(strategyType.name());
    }

    private static AdvertisingChannelType getAdvertisingChannelType(final AdChannelType adChannelType) {
        return AdvertisingChannelType.fromValue(adChannelType.name());
    }
}
