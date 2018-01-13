package com.github.lostizalith.adcreator.domain.adwords.campaign;

import com.github.lostizalith.adcreator.domain.adwords.model.SelectorPredicate;
import com.google.api.ads.adwords.axis.v201710.cm.Campaign;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignPage;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignServiceInterface;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.adwords.lib.selectorfields.v201710.cm.CampaignField;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.AD_WORDS_SERVICES;
import static com.github.lostizalith.adcreator.domain.adwords.AdWordsUtils.fetchItems;
import static java.util.Collections.singletonMap;

@Service
public class CampaignFetcher {

    public List<Campaign> fetch(final AdWordsSession session, final List<String> campaignNames) {

        validateArguments(session, campaignNames);

        final String[] fields = new String[]{CampaignField.Id.name(), CampaignField.Name.name()};
        final Map<String, String[]> in = singletonMap(CampaignField.Name.name(), campaignNames.toArray(new String[campaignNames.size()]));
        final SelectorPredicate selectorPredicate = new SelectorPredicate(fields, in);

        final CampaignServiceInterface campaignService = AD_WORDS_SERVICES.get(session, CampaignServiceInterface.class);
        return fetchItems(s -> {
                    try {
                        return campaignService.get(s);
                    } catch (RemoteException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                },
                CampaignPage::getEntries,
                selectorPredicate);
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
