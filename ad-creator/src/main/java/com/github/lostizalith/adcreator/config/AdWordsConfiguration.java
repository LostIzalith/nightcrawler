package com.github.lostizalith.adcreator.config;

import com.google.api.ads.common.lib.auth.OfflineCredentials;
import com.google.api.ads.common.lib.exception.OAuthException;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdWordsConfiguration {

    @Value("${api.adwords.clientId}")
    private String clientId;

    @Value("${api.adwords.clientSecret}")
    private String clientSecret;

    @Value("${api.adwords.refreshToken}")
    private String refreshToken;

    @Bean
    private Credential credential() {
        try {
            return new OfflineCredentials.Builder()
                    .forApi(OfflineCredentials.Api.ADWORDS)
                    .withClientSecrets(clientId, clientSecret)
                    .withRefreshToken(refreshToken)
                    .build()
                    .generateCredential();
        } catch (OAuthException | ValidationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
