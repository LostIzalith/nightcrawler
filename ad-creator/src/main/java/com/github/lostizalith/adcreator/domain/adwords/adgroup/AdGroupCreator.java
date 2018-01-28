package com.github.lostizalith.adcreator.domain.adwords.adgroup;

import com.github.lostizalith.adcreator.domain.adwords.AbstractAdWordsItemCreator;
import com.github.lostizalith.adcreator.domain.adwords.batch.BatchManager;
import com.github.lostizalith.adcreator.domain.adwords.model.AdGroupItem;
import com.github.lostizalith.adcreator.domain.adwords.model.AdWordsItem;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupAdRotationMode;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupOperation;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupReturnValue;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupStatus;
import com.google.api.ads.adwords.axis.v201710.cm.AdRotationMode;
import com.google.api.ads.adwords.axis.v201710.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201710.cm.Bids;
import com.google.api.ads.adwords.axis.v201710.cm.CpcBid;
import com.google.api.ads.adwords.axis.v201710.cm.CriterionTypeGroup;
import com.google.api.ads.adwords.axis.v201710.cm.Money;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.axis.v201710.cm.Setting;
import com.google.api.ads.adwords.axis.v201710.cm.TargetingSetting;
import com.google.api.ads.adwords.axis.v201710.cm.TargetingSettingDetail;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.AD_WORDS_SERVICES;
import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.AMOUNT_FACTOR;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdGroupCreator extends AbstractAdWordsItemCreator<AdGroupItem> {

    private final AdGroupFetcher adGroupFetcher;

    @Override
    public List<AdGroupItem> create(final AdWordsSession session, final List<AdGroupItem> adGroupItems) {

        validateArguments(session, adGroupItems);

        final List<AdGroup> adGroupsToCreating = fetchAdGroupsToCreating(session, adGroupItems);

        final List<AdGroupOperation> operations = adGroupsToCreating.stream()
                .map(g -> {
                    final AdGroupOperation operation = new AdGroupOperation();
                    operation.setOperand(g);
                    operation.setOperator(Operator.ADD);
                    return operation;
                }).collect(toList());

        final AdGroupServiceInterface adGroupService = AD_WORDS_SERVICES.get(session, AdGroupServiceInterface.class);
        final List<List<AdGroupOperation>> batches = BatchManager.slitRequest(operations);
        final Map<String, AdWordsItem> createdAdGroups = batches.stream()
                .map(b -> mutate(adGroupService, b.toArray(new AdGroupOperation[b.size()])))
                .flatMap(c -> c.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        adGroupItems.forEach(g -> setCreationResult(g, createdAdGroups.get(g.getName())));

        return adGroupItems;
    }

    private List<AdGroup> fetchAdGroupsToCreating(final AdWordsSession session, final List<AdGroupItem> adGroupItems) {

        final Map<Long, List<String>> adGroupsByCampaignId = adGroupItems.stream()
                .collect(toMap(AdGroupItem::getCampaignId, g -> asList(g.getName()), AdGroupCreator::resolveCollisions));

        final List<AdGroup> createdAdGroups = adGroupsByCampaignId.entrySet().stream()
                .map(e -> adGroupFetcher.fetch(session, e.getValue(), e.getKey().toString()))
                .flatMap(Collection::stream)
                .collect(toList());

        final Map<String, Long> groupNameToId = createdAdGroups.stream()
                .collect(toMap(AdGroup::getName, AdGroup::getId));

        adGroupItems.forEach(g -> {
            if (groupNameToId.containsKey(g.getName())) {
                setCreationResult(g, fetchSuccessItem(groupNameToId.get(g.getName())));
            }
        });

        return adGroupItems.stream()
                .filter(g -> g.getId() == null)
                .map(this::createAdGroup)
                .collect(toList());

    }

    private static List<String> resolveCollisions(final List<String> e1, final List<String> e2) {
        return Stream.concat(e1.stream(), e2.stream())
                .distinct()
                .collect(toList());
    }

    private AdGroup createAdGroup(final AdGroupItem adGroupItem) {

        final AdGroup adGroup = new AdGroup();
        adGroup.setName(adGroupItem.getName());
        adGroup.setStatus(AdGroupStatus.ENABLED);
        adGroup.setCampaignId(adGroupItem.getCampaignId());

        final TargetingSetting targeting = new TargetingSetting();

        final TargetingSettingDetail placements = new TargetingSettingDetail();
        placements.setCriterionTypeGroup(CriterionTypeGroup.PLACEMENT);
        placements.setTargetAll(Boolean.FALSE);

        final TargetingSettingDetail verticals = new TargetingSettingDetail();
        verticals.setCriterionTypeGroup(CriterionTypeGroup.VERTICAL);
        verticals.setTargetAll(Boolean.TRUE);

        targeting.setDetails(new TargetingSettingDetail[]{placements, verticals});
        adGroup.setSettings(new Setting[]{targeting});

        final AdGroupAdRotationMode rotationMode = new AdGroupAdRotationMode(AdRotationMode.OPTIMIZE);
        adGroup.setAdGroupAdRotationMode(rotationMode);

        final BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
        final CpcBid bid = new CpcBid();
        bid.setBid(new Money(null, adGroupItem.getAmount() * AMOUNT_FACTOR));
        biddingStrategyConfiguration.setBids(new Bids[]{bid});
        adGroup.setBiddingStrategyConfiguration(biddingStrategyConfiguration);

        return adGroup;
    }

    private Map<String, AdWordsItem> mutate(final AdGroupServiceInterface adGroupService,
                                            final AdGroupOperation[] operations) {
        try {
            final AdGroupReturnValue result = adGroupService.mutate(operations);
            return Arrays.stream(result.getValue())
                    .collect(toMap(AdGroup::getName, x -> fetchSuccessItem(x.getId())));
        } catch (RemoteException e) {
            return Stream.of(operations)
                    .map(o -> o.getOperand().getName())
                    .collect(toMap(x -> x, x -> fetchErrorItem(e.getMessage())));
        }
    }

    private static void validateArguments(final AdWordsSession session, final List<AdGroupItem> adGroupItems) {
        if (session == null) {
            throw new IllegalArgumentException("AdWords session can't be null");
        }

        if (CollectionUtils.isEmpty(adGroupItems)) {
            throw new IllegalArgumentException("AdGroup items can't be empty");
        }
    }
}
