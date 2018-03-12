package com.github.lostizalith.common.adwords.domain.session;

import com.google.api.ads.adwords.lib.client.AdWordsSession;
import com.google.api.ads.common.lib.exception.ValidationException;
import com.google.api.client.auth.oauth2.Credential;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdWordsSessionManager implements SessionManager {

    private final Credential credential;

    @Value("${api.adwords.developerToken}")
    private String developerToken;

    @Override
    public AdWordsSession createSession(final String customerId) {
        log.info("Create session with customer id {}", customerId);

        try {
            return new AdWordsSession.Builder()
                    .withDeveloperToken(developerToken)
                    .withClientCustomerId(customerId)
                    .withOAuth2Credential(credential)
                    .build();
        } catch (ValidationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
