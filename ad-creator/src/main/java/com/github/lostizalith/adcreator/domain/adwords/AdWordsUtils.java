package com.github.lostizalith.adcreator.domain.adwords;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;

public class AdWordsUtils {

    public static final AdWordsServicesInterface AD_WORDS_SERVICES = AdWordsServices.getInstance();

    public static final int PAGE_SIZE = 100;

}
