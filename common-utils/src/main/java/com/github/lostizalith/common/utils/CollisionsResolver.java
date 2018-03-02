package com.github.lostizalith.common.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CollisionsResolver {

    private CollisionsResolver() {
        // Util class
    }

    public static <T> List<T> concat(final List<T> e1, final List<T> e2) {
        return Stream.concat(e1.stream(), e2.stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
