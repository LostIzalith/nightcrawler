package com.github.lostizalith.common.adwords.domain.targeting;

import com.google.api.ads.adwords.axis.v201710.cm.Criterion;
import com.google.api.ads.adwords.axis.v201710.cm.Language;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class LanguageSettingsConfigurer extends TargetingSettingsConfigurer {

    public void setLanguage(final AdWordsSession session, final Map<Long, List<Long>> languageIds) {

        final Map<Long, List<Criterion>> languages = languageIds.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream()
                        .map(id -> {
                            final Language language = new Language();
                            language.setId(id);
                            return (Criterion) language;
                        }).collect(toList())));

        mutate(session, languages);
    }
}
