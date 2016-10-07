package com.bitsfromspace.moneytracker.services.impl;

import com.bitsfromspace.moneytracker.services.Currency;
import com.bitsfromspace.moneytracker.services.CurrencyService;
import com.bitsfromspace.utils.Cache;
import com.eclipsesource.json.Json;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

import static com.bitsfromspace.utils.IoUtils.httpGet;

/**
 * @author chris
 * @since 29/09/2016.
 */
@Singleton
@Named
public class YahooCurrencyService implements CurrencyService {

    private static final String YAHOO_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20%3D%20%22$CURRENCY_PAIR%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

    private final Cache<String, Double> currencyPairExchargeRateCache;

    @Inject
    public YahooCurrencyService(Clock clock, @Value("${moneytrackr.yahooCurrencyService.cacheTimeoutMinutes}") int cacheTimeoutMinutes){
        currencyPairExchargeRateCache = new Cache<>(clock, cacheTimeoutMinutes, TimeUnit.MINUTES);
    }

    @Override
    public double getExchangeRate(Currency from, Currency to) {
        if (from == to) {
            return 1;
        }
        final String currencyPair = createCurrencyPair(from, to);
        return currencyPairExchargeRateCache.getOrSet(currencyPair, this::lookup);
    }

    private double lookup(String currencyPair){
        final String url = YAHOO_URL.replace("$CURRENCY_PAIR", currencyPair);
        final String response = httpGet(url);
        return Double.valueOf(
                Json.parse(response).asObject()
                    .get("query").asObject()
                    .get("results").asObject()
                    .get("rate").asObject()
                    .get("Rate").asString()
        );
    }

    private String createCurrencyPair(Currency from, Currency to) {
        return from.toString() + to.toString();
    }
}
