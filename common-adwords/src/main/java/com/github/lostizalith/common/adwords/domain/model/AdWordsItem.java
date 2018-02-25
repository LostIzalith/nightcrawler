package com.github.lostizalith.common.adwords.domain.model;

import com.github.lostizalith.common.adwords.domain.model.enumeration.Status;
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
