package com.github.lostizalith.adcreator.domain.adwords.model;

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
