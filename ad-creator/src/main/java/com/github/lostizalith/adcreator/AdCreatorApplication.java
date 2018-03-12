package com.github.lostizalith.adcreator;

import com.github.lostizalith.adcreator.domain.utils.generator.CampaignsGenerator;
import com.github.lostizalith.common.adwords.domain.campaign.CampaignCreator;
import com.github.lostizalith.common.adwords.domain.model.CampaignItem;
import com.github.lostizalith.common.adwords.domain.model.enumeration.MatchType;
import com.github.lostizalith.common.adwords.domain.session.AdWordsSessionManager;
import com.google.api.ads.adwords.lib.client.AdWordsSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@ComponentScan({"com.github.lostizalith"})
@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdCreatorApplication {

    private static final String CUSTOMER_ID = "502-297-9129";

    private final CampaignsGenerator campaignsGenerator;

    private final AdWordsSessionManager adWordsSessionManager;
    private final CampaignCreator campaignCreator;

    public static void main(String[] args) {
        SpringApplication.run(AdCreatorApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(final ApplicationContext context) {
        return args -> {

            final List<CampaignItem> campaignItems = campaignsGenerator.generate(MatchType.EXACT, 10);
            final AdWordsSession session = adWordsSessionManager.createSession(CUSTOMER_ID);
            campaignCreator.create(session, campaignItems);

        };
    }
}
