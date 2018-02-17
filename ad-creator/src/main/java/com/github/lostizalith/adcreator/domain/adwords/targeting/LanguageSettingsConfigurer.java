package com.github.lostizalith.adcreator.domain.adwords.targeting;

import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Language;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LanguageSettingsConfigurer extends TargetingSettingsConfigurer {

    public void setLanguage(final AdWordsSession session, final Long campaignId, final List<Long> languageIds) {

        final List<Criterion> criteria = languageIds.stream()
                .map(id -> {
                    final Language language = new Language();
                    language.setId(id);
                    return language;
                }).collect(toList());

        mutate(session, campaignId, criteria);
    }
}
