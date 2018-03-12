package com.github.lostizalith.common.adwords.domain.targeting;

import com.github.lostizalith.common.adwords.domain.batch.BatchManager;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignCriterion;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignCriterionOperation;
import com.google.api.ads.adwords.axis.v201710.cm.CampaignCriterionServiceInterface;
import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Operator;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.AD_WORDS_SERVICES;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class TargetingSettingsConfigurer {

    protected void mutate(final AdWordsSession session, final Map<Long, List<Criterion>> criteria) {

        final CampaignCriterionServiceInterface campaignCriterionService =
                AD_WORDS_SERVICES.get(session, CampaignCriterionServiceInterface.class);

        final List<CampaignCriterionOperation> operations = criteria.entrySet().stream()
                .map(e -> e.getValue().stream()
                        .map(c -> {
                            final CampaignCriterion campaignCriterion = new CampaignCriterion();
                            campaignCriterion.setCampaignId(e.getKey());
                            campaignCriterion.setCriterion(c);

                            final CampaignCriterionOperation operation = new CampaignCriterionOperation();
                            operation.setOperand(campaignCriterion);
                            operation.setOperator(Operator.ADD);

                            return operation;
                        }).collect(toList()))
                .flatMap(Collection::stream)
                .collect(toList());

        final List<List<CampaignCriterionOperation>> batches = BatchManager.slitRequest(operations);
        batches.forEach(b -> {
            try {
                campaignCriterionService.mutate(b.toArray(new CampaignCriterionOperation[b.size()]));
            } catch (RemoteException e) {
                log.error(e.getMessage(), e);
            }
        });
    }
}
