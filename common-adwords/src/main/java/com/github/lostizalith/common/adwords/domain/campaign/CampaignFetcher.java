package com.github.lostizalith.common.adwords.domain.campaign;

import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.Campaign;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.CampaignField;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.AD_WORDS_SERVICES;
import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.PAGE_SIZE;

@Service
public class CampaignFetcher {

    public List<Campaign> fetch(final AdWordsSession session, final List<String> campaignNames) {

        validateArguments(session, campaignNames);

        final CampaignServiceInterface campaignService = AD_WORDS_SERVICES.get(session, CampaignServiceInterface.class);

        int offset = 0;

        final SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(CampaignField.Id, CampaignField.Name)
                .in(CampaignField.Name, campaignNames.toArray(new String[campaignNames.size()]))
                .offset(offset)
                .limit(PAGE_SIZE)
                .build();

        final List<Campaign> campaigns = new ArrayList<>();
        CampaignPage page;
        do {
            try {
                page = campaignService.get(selector);
            } catch (RemoteException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            if (page.getEntries() != null) {
                campaigns.addAll(Arrays.asList(page.getEntries()));
            }

            offset += PAGE_SIZE;
            selector = builder.increaseOffsetBy(PAGE_SIZE).build();
        } while (offset < page.getTotalNumEntries());

        return campaigns;
    }

    private static void validateArguments(final AdWordsSession session, final List<String> campaignNames) {
        if (session == null) {
            throw new IllegalArgumentException("AdWords session can't be null");
        }

        if (CollectionUtils.isEmpty(campaignNames)) {
            throw new IllegalArgumentException("Campaign items can't be empty");
        }
    }
}
