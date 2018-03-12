package com.github.lostizalith.common.adwords.domain;

import com.google.api.ads.adwords.axis.factory.AdWordsServices;
import com.google.api.ads.adwords.lib.factory.AdWordsServicesInterface;

public class AdWordsUtils {

    public static final AdWordsServicesInterface AD_WORDS_SERVICES = AdWordsServices.getInstance();

    public static final int PAGE_SIZE = 100;

    public static final int MAX_BATCH_SIZE = 2000;

    public static final long AMOUNT_FACTOR = 1000_000;

}
