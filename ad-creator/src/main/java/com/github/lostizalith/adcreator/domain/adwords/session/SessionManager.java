package com.github.lostizalith.adcreator.domain.adwords.session;

import com.google.api.ads.adwords.lib.client.AdWordsSession;

/**
 * Interface for session managers.
 */
public interface SessionManager {

    /**
     * Create session.
     *
     * @param customerId is an account id
     * @return session
     */
    AdWordsSession createSession(String customerId);
}
