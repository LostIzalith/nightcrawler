package com.github.lostizalith.adcreator.domain.adwords;

import com.github.lostizalith.adcreator.domain.adwords.model.AdWordsItem;
import com.github.lostizalith.adcreator.domain.adwords.model.Status;
import com.google.api.ads.adwords.lib.client.AdWordsSession;

import java.util.List;

public abstract class AbstractAdWordsItemCreator<T extends AdWordsItem> {

    public abstract List<T> create(AdWordsSession session, final List<T> items);

    protected static AdWordsItem fetchSuccessItem(final Long id) {
        final AdWordsItem item = new AdWordsItem();
        item.setId(id);
        item.setStatus(Status.SUCCESS);
        return item;
    }

    protected static AdWordsItem fetchErrorItem(final String errorMessage) {
        final AdWordsItem item = new AdWordsItem();
        item.setStatus(Status.FAILED);
        item.setErrorMessage(errorMessage);
        return item;
    }

    protected static void setCreationResult(final AdWordsItem target, final AdWordsItem source) {
        target.setId(source.getId());
        target.setStatus(source.getStatus());
        target.setErrorMessage(source.getErrorMessage());
    }
}
