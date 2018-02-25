package com.github.lostizalith.common.adwords.domain.batch;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class BatchManager {

    private static final int MAX_BATCH_SIZE = 2000;

    public static <T> List<List<T>> slitRequest(List<T> operations) {

        if (CollectionUtils.isEmpty(operations)) {
            return Collections.emptyList();
        }

        return Lists.partition(operations, MAX_BATCH_SIZE);
    }
}
