package com.github.lostizalith.common.adwords.domain.adgroup;

import com.github.lostizalith.common.adwords.domain.AdWordsUtils;
import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroup;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupPage;
import com.google.api.ads.adwords.axis.v201710.cm.AdGroupServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.AdGroupField;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AdGroupFetcher {

    public List<AdGroup> fetch(final AdWordsSession session, final List<String> groupNames, final String campaignId) {

        AdGroupServiceInterface adGroupService =
                AdWordsUtils.AD_WORDS_SERVICES.get(session, AdGroupServiceInterface.class);

        int offset = 0;
        boolean morePages = true;

        final SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(AdGroupField.Id, AdGroupField.Name)
                .in(AdGroupField.Name, groupNames.toArray(new String[groupNames.size()]))
                .equals(AdGroupField.CampaignId, campaignId)
                .offset(offset)
                .limit(AdWordsUtils.PAGE_SIZE)
                .build();

        final List<AdGroup> adGroups = new ArrayList<>();
        while (morePages) {
            AdGroupPage page;
            try {
                page = adGroupService.get(selector);
            } catch (RemoteException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            if (page.getEntries() != null) {
                adGroups.addAll(Arrays.asList(page.getEntries()));
            }

            offset += AdWordsUtils.PAGE_SIZE;
            selector = builder.increaseOffsetBy(AdWordsUtils.PAGE_SIZE).build();
            morePages = offset < page.getTotalNumEntries();
        }

        return adGroups;
    }
}
