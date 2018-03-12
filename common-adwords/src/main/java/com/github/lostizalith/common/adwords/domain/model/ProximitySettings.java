package com.github.lostizalith.common.adwords.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProximitySettings {

    private String countryCode;

    private String postalCode;

    private String cityName;

    private String streetAddress;

    private Double radius;
}
