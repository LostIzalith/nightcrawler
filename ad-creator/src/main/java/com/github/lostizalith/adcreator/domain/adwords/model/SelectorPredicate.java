package com.github.lostizalith.adcreator.domain.adwords.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class SelectorPredicate {

    private final String[] fields;

    private final Map<String, String[]> inPredicate;
}
