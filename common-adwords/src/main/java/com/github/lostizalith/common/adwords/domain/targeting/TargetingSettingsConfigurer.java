package com.github.lostizalith.common.adwords.domain.targeting;

import com.google.api.ads.adwords.axis.v201710.cm.CampaignCriterion;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignCriterionOperation;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;
import java.util.List;

import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.AD_WORDS_SERVICES;

@Slf4j
public class TargetingSettingsConfigurer {

    protected void mutate(final AdWordsSession session, final Long campaignId, final List<Criterion> criteria) {

        final CampaignCriterionServiceInterface campaignCriterionService =
                AD_WORDS_SERVICES.get(session, CampaignCriterionServiceInterface.class);

        try {
            campaignCriterionService.mutate(criteria.stream()
                    .map(c -> {
                        final CampaignCriterion campaignCriterion = new CampaignCriterion();
                        campaignCriterion.setCampaignId(campaignId);
                        campaignCriterion.setCriterion(c);

                        final CampaignCriterionOperation operation = new CampaignCriterionOperation();
                        operation.setOperand(campaignCriterion);
                        operation.setOperator(Operator.ADD);

                        return operation;
                    }).toArray(CampaignCriterionOperation[]::new));
        } catch (RemoteException e) {
            log.error(e.getMessage(), e);
        }
    }
}
