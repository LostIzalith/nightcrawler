package com.github.lostizalith.adcreator.domain.adwords.model;

import com.github.lostizalith.adcreator.domain.adwords.model.enumeration.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AdWordsItem implements Serializable {

    private Long id;

    private Status status;

    private String errorMessage;

}
