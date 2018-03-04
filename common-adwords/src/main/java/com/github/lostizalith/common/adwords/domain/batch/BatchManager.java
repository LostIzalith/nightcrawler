package com.github.lostizalith.common.adwords.domain.batch;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static com.github.lostizalith.common.adwords.domain.AdWordsUtils.MAX_BATCH_SIZE;

public class BatchManager {

    public static <T> List<List<T>> slitRequest(List<T> items) {

        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }

        return Lists.partition(items, MAX_BATCH_SIZE);
    }
}
