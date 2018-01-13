package com.github.lostizalith.adcreator.domain.adwords.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdWordsItem {

    private Long id;

    private Status status;

    private String errorMessage;

}
