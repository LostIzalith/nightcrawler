package com.github.lostizalith.adcreator.domain.adwords.campaign;

import com.github.lostizalith.adcreator.domain.adwords.AbstractAdWordsItemCreator;
import com.github.lostizalith.adcreator.domain.adwords.batch.BatchManager;
import com.github.lostizalith.adcreator.domain.adwords.budget.BudgetCreator;
import com.github.lostizalith.adcreator.domain.adwords.model.AdChannelType;
import com.github.lostizalith.adcreator.domain.adwords.model.AdWordsItem;
import com.github.lostizalith.adcreator.domain.adwords.model.BudgetItem;
import com.github.lostizalith.adcreator.domain.adwords.model.CampaignItem;
import com.github.lostizalith.adcreator.domain.adwords.model.StrategyType;
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
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.AD_WORDS_SERVICES;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CampaignCreator extends AbstractAdWordsItemCreator<CampaignItem> {

    private final CampaignFetcher campaignFetcher;
    private final BudgetCreator budgetCreator;

    @Override
    public List<CampaignItem> create(final AdWordsSession session, final List<CampaignItem> campaignItems) {

        validateArguments(session, campaignItems);

        final List<Campaign> campaigns = fetchCampaignToCreating(session, campaignItems);

        final List<CampaignOperation> campaignOperations = campaigns.stream()
                .map(c -> {
                    final CampaignOperation operation = new CampaignOperation();
                    operation.setOperand(c);
                    operation.setOperator(Operator.ADD);
                    return operation;
                }).collect(toList());

        final CampaignServiceInterface campaignService = AD_WORDS_SERVICES.get(session, CampaignServiceInterface.class);
        final List<List<CampaignOperation>> batches = BatchManager.slitRequest(campaignOperations);
        final Map<String, AdWordsItem> createdCampaigns = batches.stream()
                .map(b -> mutate(campaignService, b.toArray(new CampaignOperation[b.size()])))
                .flatMap(c -> c.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        campaignItems.forEach(c -> {
            final AdWordsItem adWordsItem = createdCampaigns.get(c.getName());
            c.setId(adWordsItem.getId());
            c.setStatus(adWordsItem.getStatus());
            c.setErrorMessage(adWordsItem.getErrorMessage());
        });

        return campaignItems;
    }

    private List<Campaign> fetchCampaignToCreating(final AdWordsSession session, final List<CampaignItem> campaignItems) {

        final List<String> campaignNames = campaignItems.stream()
                .map(CampaignItem::getName)
                .collect(toList());
        final List<Campaign> adWordsCampaigns = campaignFetcher.fetch(session, campaignNames);
        final Map<String, Long> name2CampaignId = adWordsCampaigns.stream()
                .collect(toMap(Campaign::getName, Campaign::getId));

        final List<CampaignItem> campaigns = campaignItems.stream()
                .filter(c -> name2CampaignId.containsKey(c.getName()))
                .peek(c -> {
                    final AdWordsItem adWordsItem = fetchSuccessItem(name2CampaignId.get(c.getName()));
                    c.setId(adWordsItem.getId());
                    c.setStatus(adWordsItem.getStatus());
                }).collect(toList());

        return campaigns.stream()
                .filter(c -> c.getId() == null)
                .map(c -> createCampaign(session, c))
                .collect(toList());
    }

    private Campaign createCampaign(final AdWordsSession session, final CampaignItem campaignItem) {

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

        return campaign;
    }

    private Map<String, AdWordsItem> mutate(final CampaignServiceInterface campaignService, final CampaignOperation[] operations) {
        try {
            final CampaignReturnValue result = campaignService.mutate(operations);
            return Arrays.stream(result.getValue())
                    .collect(toMap(Campaign::getName, x -> fetchSuccessItem(x.getId())));
        } catch (RemoteException e) {
            return Stream.of(operations)
                    .map(o -> o.getOperand().getName())
                    .collect(toMap(x -> x, x -> fetchErrorItem(e.getMessage())));
        }
    }

    private static BiddingStrategyType getBiddingStrategyType(final StrategyType strategyType) {
        return BiddingStrategyType.fromValue(strategyType.name());
    }

    private static AdvertisingChannelType getAdvertisingChannelType(final AdChannelType adChannelType) {
        return AdvertisingChannelType.fromValue(adChannelType.name());
    }

    private static void validateArguments(final AdWordsSession session, final List<CampaignItem> campaignItems) {
        if (session == null) {
            throw new IllegalArgumentException("AdWords session can't be null");
        }

        if (CollectionUtils.isEmpty(campaignItems)) {
            throw new IllegalArgumentException("Campaign items can't be empty");
        }
    }
}
