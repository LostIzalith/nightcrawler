package com.github.lostizalith.common.adwords.domain.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChannelType {

    SEARCH("Search"),

    DISPLAY("Display"),

    SHOPPING("Shopping"),

    MULTI_CHANNEL("Multi channel");

    private final String value;
}
