package com.github.lostizalith.common.adwords.domain.ad;

import com.github.lostizalith.common.adwords.domain.AbstractAdWordsItemCreator;
import com.github.lostizalith.common.adwords.domain.batch.BatchManager;
import com.github.lostizalith.common.adwords.domain.model.ExpandedTextAdItem;
import com.github.lostizalith.common.adwords.domain.model.enumeration.Status;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupAd;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupAdOperation;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupAdReturnValue;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupAdServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupAdStatus;
import com.google.api.ads.adwords.axis.v201710.cm.ExpandedTextAd;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.AD_WORDS_SERVICES;
import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.MAX_BATCH_SIZE;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExpandedTextAdCreator extends AbstractAdWordsItemCreator<ExpandedTextAdItem> {

    @Override
    public List<ExpandedTextAdItem> create(final AdWordsSession session, final List<ExpandedTextAdItem> expandedTextAdItems) {

        final List<AdGroupAdOperation> adGroupAdOperations = expandedTextAdItems.stream()
                .map(ad -> {
                    final ExpandedTextAd expandedTextAd = new ExpandedTextAd();
                    expandedTextAd.setHeadlinePart1(ad.getHeadline1());
                    expandedTextAd.setHeadlinePart2(ad.getHeadline2());
                    expandedTextAd.setDescription(ad.getDescription());
                    expandedTextAd.setFinalUrls(new String[]{ad.getFinalUrl()});

                    final AdGroupAd expandedTextAdGroupAd = new AdGroupAd();
                    expandedTextAdGroupAd.setAdGroupId(ad.getAdGroupId());
                    expandedTextAdGroupAd.setAd(expandedTextAd);
                    expandedTextAdGroupAd.setStatus(AdGroupAdStatus.PAUSED);

                    final AdGroupAdOperation adGroupAdOperation = new AdGroupAdOperation();
                    adGroupAdOperation.setOperand(expandedTextAdGroupAd);
                    adGroupAdOperation.setOperator(Operator.ADD);

                    return adGroupAdOperation;
                }).collect(Collectors.toList());


        final AdGroupAdServiceInterface adGroupAdService =
                AD_WORDS_SERVICES.get(session, AdGroupAdServiceInterface.class);
        final List<List<AdGroupAdOperation>> batches = BatchManager.slitRequest(adGroupAdOperations);
        IntStream.range(0, batches.size())
                .forEach(i -> {
                    try {
                        final AdGroupAdReturnValue createdAds =
                                adGroupAdService.mutate(batches.get(i).toArray(new AdGroupAdOperation[batches.get(i).size()]));
                        IntStream.range(MAX_BATCH_SIZE * i, getIndex(i, createdAds.getValue().length))
                                .forEach(index -> {
                                    expandedTextAdItems.get(index).setStatus(Status.SUCCESS);
                                    expandedTextAdItems.get(index).setId(createdAds.getValue()[index].getAd().getId());
                                });

                    } catch (RemoteException e) {
                        IntStream.range(MAX_BATCH_SIZE * i, getIndex(i, batches.get(i).size()))
                                .forEach(index -> {
                                    expandedTextAdItems.get(index).setStatus(Status.SUCCESS);
                                    expandedTextAdItems.get(index).setErrorMessage(e.getMessage());
                                });
                    }
                });


        return expandedTextAdItems;
    }

    private static int getIndex(final int iterator, final int adsSize) {
        return MAX_BATCH_SIZE * iterator + (adsSize < MAX_BATCH_SIZE ? adsSize : MAX_BATCH_SIZE) - 1;
    }

}
