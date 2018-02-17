package com.github.lostizalith.adcreator.domain.adwords.keyword;

import com.github.lostizalith.adcreator.domain.adwords.AbstractAdWordsItemCreator;
import com.github.lostizalith.adcreator.domain.adwords.batch.BatchManager;
import com.github.lostizalith.adcreator.domain.adwords.model.AdWordsItem;
import com.github.lostizalith.adcreator.domain.adwords.model.KeywordItem;
import com.github.lostizalith.adcreator.domain.adwords.model.MatchType;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupCriterionOperation;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupCriterionReturnValue;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.BiddableAdGroupCriterion;
import com.google.api.ads.adwords.axis.v201710.cm.BiddingStrategyConfiguration;
import com.google.api.ads.adwords.axis.v201710.cm.Bids;
import com.google.api.ads.adwords.axis.v201710.cm.CpcBid;
import com.google.api.ads.adwords.axis.v201710.cm.Keyword;
import com.google.api.ads.adwords.axis.v201710.cm.KeywordMatchType;
import com.google.api.ads.adwords.axis.v201710.cm.Money;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.axis.v201710.cm.UrlList;
import com.google.api.ads.adwords.axis.v201710.cm.UserStatus;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.AD_WORDS_SERVICES;
import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.AMOUNT_FACTOR;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeywordCreator extends AbstractAdWordsItemCreator<KeywordItem> {

    @Override
    public List<KeywordItem> create(final AdWordsSession session, final List<KeywordItem> keywordItems) {

        final List<BiddableAdGroupCriterion> biddableAdGroupCriteria = keywordItems.stream()
                .map(this::createCriterion)
                .collect(toList());

        final List<AdGroupCriterionOperation> operations = biddableAdGroupCriteria.stream()
                .map(c -> {
                    final AdGroupCriterionOperation operation = new AdGroupCriterionOperation();
                    operation.setOperand(c);
                    operation.setOperator(Operator.ADD);

                    return operation;
                }).collect(toList());

        final AdGroupCriterionServiceInterface adGroupCriterionService =
                AD_WORDS_SERVICES.get(session, AdGroupCriterionServiceInterface.class);
        final List<List<AdGroupCriterionOperation>> batches = BatchManager.slitRequest(operations);

        final Map<String, AdWordsItem> createdAdGroups = batches.stream()
                .map(b -> mutate(adGroupCriterionService, b.toArray(new AdGroupCriterionOperation[b.size()])))
                .flatMap(c -> c.entrySet().stream())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        keywordItems.forEach(g -> setCreationResult(g, createdAdGroups.get(g.getText())));

        return keywordItems;
    }

    private BiddableAdGroupCriterion createCriterion(final KeywordItem keywordItem) {
        final Keyword keyword = new Keyword();
        keyword.setText(keywordItem.getText());
        keyword.setMatchType(fetchKeywordMatchType(keywordItem.getMatchType()));

        final BiddableAdGroupCriterion keywordBiddableAdGroupCriterion = new BiddableAdGroupCriterion();
        keywordBiddableAdGroupCriterion.setAdGroupId(keywordItem.getAdGroupId());
        keywordBiddableAdGroupCriterion.setCriterion(keyword);
        keywordBiddableAdGroupCriterion.setUserStatus(UserStatus.PAUSED);
        keywordBiddableAdGroupCriterion.setFinalUrls(new UrlList(new String[]{keywordItem.getFinalUrl()}));

        if (keywordItem.getCpcBid() != null) {
            final BiddingStrategyConfiguration biddingStrategyConfiguration = new BiddingStrategyConfiguration();
            final CpcBid bid = new CpcBid();
            bid.setBid(new Money(null, keywordItem.getCpcBid() * AMOUNT_FACTOR));
            biddingStrategyConfiguration.setBids(new Bids[]{bid});
            keywordBiddableAdGroupCriterion.setBiddingStrategyConfiguration(biddingStrategyConfiguration);
        }

        return keywordBiddableAdGroupCriterion;
    }

    private Map<String, AdWordsItem> mutate(final AdGroupCriterionServiceInterface campaignService, final AdGroupCriterionOperation[] operations) {
        try {
            final AdGroupCriterionReturnValue result = campaignService.mutate(operations);
            return Arrays.stream(result.getValue())
                    .collect(toMap(x -> ((Keyword) x.getCriterion()).getText(), x -> fetchSuccessItem(x.getCriterion().getId())));
        } catch (RemoteException e) {
            return Stream.of(operations)
                    .map(o -> ((Keyword) o.getOperand().getCriterion()).getText())
                    .collect(toMap(x -> x, x -> fetchErrorItem(e.getMessage())));
        }
    }

    private static KeywordMatchType fetchKeywordMatchType(final MatchType matchType) {
        return KeywordMatchType.fromValue(matchType.name());
    }
}
