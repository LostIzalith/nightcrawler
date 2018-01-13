package com.github.lostizalith.adcreator.domain.adwords.campaign;

import com.google.api.ads.adwords.axis.utils.v201710.SelectorBuilder;
import com.google.api.ads.adwords.axis.v201710.cm.Campaign;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Selector;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.CampaignField;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.List;

import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.AD_WORDS_SERVICES;
import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.fetchItems;

@Service
public class CampaignFetcher {

    private static final int PAGE_SIZE = 100;

    public List<Campaign> fetch(final AdWordsSession session, final List<String> campaignNames) {

        final SelectorBuilder builder = new SelectorBuilder();
        Selector selector = builder
                .fields(CampaignField.Id, CampaignField.Name)
                .in(CampaignField.Name, campaignNames.toArray(new String[campaignNames.size()]))
                .offset(0)
                .limit(PAGE_SIZE)
                .build();

        final CampaignServiceInterface campaignService = AD_WORDS_SERVICES.get(session, CampaignServiceInterface.class);
        return fetchItems(s -> {
                    try {
                        return campaignService.get(s);
                    } catch (RemoteException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                },
                CampaignPage::getEntries,
                builder,
                selector);
    }
}
