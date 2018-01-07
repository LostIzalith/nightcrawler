package com.github.lostizalith.adcreator.domain.adwords.campaign;

import com.google.api.ads.adwords.axis.v201710.cm.AdvertisingChannelType;
import com.google.api.ads.adwords.axis.v201710.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201710.cm.BiddingStrategyType;
import com.google.api.ads.adwords.axis.v201710.cm.Budget;
import com.google.api.ads.adwords.axis.v201710.cm.BudgetBudgetDeliveryMethod;
import com.google.api.ads.adwords.axis.v201710.cm.BudgetOperation;
import com.google.api.ads.adwords.axis.v201710.cm.BudgetServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Campaign;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignOperation;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignReturnValue;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignStatus;
import com.google.api.ads.adwords.axis.v201710.cm.FrequencyCap;
import com.google.api.ads.adwords.axis.v201710.cm.GeoTargetTypeSetting;
import com.google.api.ads.adwords.axis.v201710.cm.GeoTargetTypeSettingPositiveGeoTargetType;
import com.google.api.ads.adwords.axis.v201710.cm.Level;
import com.google.api.ads.adwords.axis.v201710.cm.ManualCpcBiddingScheme;
import com.google.api.ads.adwords.axis.v201710.cm.Money;
import com.google.api.ads.adwords.axis.v201710.cm.NetworkSetting;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.axis.v201710.cm.Setting;
import com.google.api.ads.adwords.axis.v201710.cm.TimeUnit;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

@Component
public class CampaignCreator {

    public void create(final AdWordsSession session, final AdWordsServicesInterface adWordsServices) {

        BudgetServiceInterface budgetService =
                adWordsServices.get(session, BudgetServiceInterface.class);

        // Create a budget, which can be shared by multiple campaigns.
        Budget sharedBudget = new Budget();
        sharedBudget.setName("Interplanetary Cruise #" + System.currentTimeMillis());
        Money budgetAmount = new Money();
        budgetAmount.setMicroAmount(50000000L);
        sharedBudget.setAmount(budgetAmount);
        sharedBudget.setDeliveryMethod(BudgetBudgetDeliveryMethod.STANDARD);

        BudgetOperation budgetOperation = new BudgetOperation();
        budgetOperation.setOperand(sharedBudget);
        budgetOperation.setOperator(Operator.ADD);

        // Add the budget
        Long budgetId;
        try {
            budgetId = budgetService.mutate(new BudgetOperation[]{budgetOperation}).getValue(0).getBudgetId();
        } catch (RemoteException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        // Get the CampaignService.
        CampaignServiceInterface campaignService =
                adWordsServices.get(session, CampaignServiceInterface.class);

        // Create campaign.
        Campaign campaign = new Campaign();
        campaign.setName("Interplanetary Cruise #" + System.currentTimeMillis());

        // Recommendation: Set the campaign to PAUSED when creating it to prevent
        // the ads from immediately serving. Set to ENABLED once you've added
        // targeting and the ads are ready to serve.
        campaign.setStatus(CampaignStatus.PAUSED);

        BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
        biddingStrategyConfiguration.setBiddingStrategyType(BiddingStrategyType.MANUAL_CPC);

        // You can optionally provide a bidding scheme in place of the type.
        ManualCpcBiddingScheme cpcBiddingScheme = new ManualCpcBiddingScheme();
        cpcBiddingScheme.setEnhancedCpcEnabled(false);
        biddingStrategyConfiguration.setBiddingScheme(cpcBiddingScheme);

        campaign.setBiddingStrategyConfiguration(biddingStrategyConfiguration);

        // You can optionally provide these field(s).
        campaign.setStartDate(new DateTime().plusDays(1).toString("yyyyMMdd"));
        campaign.setEndDate(new DateTime().plusDays(30).toString("yyyyMMdd"));
        campaign.setFrequencyCap(new FrequencyCap(5L, TimeUnit.DAY, Level.ADGROUP));

        // Only the budgetId should be sent, all other fields will be ignored by CampaignService.
        Budget budget = new Budget();
        budget.setBudgetId(budgetId);
        campaign.setBudget(budget);

        campaign.setAdvertisingChannelType(AdvertisingChannelType.SEARCH);

        // Set the campaign network options to Search and Search Network.
        NetworkSetting networkSetting = new NetworkSetting();
        networkSetting.setTargetGoogleSearch(true);
        networkSetting.setTargetSearchNetwork(true);
        networkSetting.setTargetContentNetwork(false);
        networkSetting.setTargetPartnerSearchNetwork(false);
        campaign.setNetworkSetting(networkSetting);

        // Set options that are not required.
        GeoTargetTypeSetting geoTarget = new GeoTargetTypeSetting();
        geoTarget.setPositiveGeoTargetType(GeoTargetTypeSettingPositiveGeoTargetType.DONT_CARE);
        campaign.setSettings(new Setting[]{geoTarget});

        // You can create multiple campaigns in a single request.
        Campaign campaign2 = new Campaign();
        campaign2.setName("Interplanetary Cruise banner #" + System.currentTimeMillis());
        campaign2.setStatus(CampaignStatus.PAUSED);
        BiddingStrategyConfiguration biddingStrategyConfiguration2 = new BiddingStrategyConfiguration();
        biddingStrategyConfiguration2.setBiddingStrategyType(BiddingStrategyType.MANUAL_CPC);
        campaign2.setBiddingStrategyConfiguration(biddingStrategyConfiguration2);

        Budget budget2 = new Budget();
        budget2.setBudgetId(budgetId);
        campaign2.setBudget(budget2);

        campaign2.setAdvertisingChannelType(AdvertisingChannelType.DISPLAY);

        // Create operations.
        CampaignOperation operation = new CampaignOperation();
        operation.setOperand(campaign);
        operation.setOperator(Operator.ADD);
        CampaignOperation operation2 = new CampaignOperation();
        operation2.setOperand(campaign2);
        operation2.setOperator(Operator.ADD);

        CampaignOperation[] operations = new CampaignOperation[]{operation, operation2};

        // Add campaigns.
        CampaignReturnValue result;
        try {
            result = campaignService.mutate(operations);
        } catch (RemoteException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        // Display campaigns.
        for (Campaign campaignResult : result.getValue()) {
            System.out.printf("Campaign with name '%s' and ID %d was added.%n", campaignResult.getName(),
                    campaignResult.getId());
        }
    }
}
