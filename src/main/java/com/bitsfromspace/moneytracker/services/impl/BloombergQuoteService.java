package com.bitsfromspace.moneytracker.services.impl;

import com.bitsfromspace.moneytracker.services.Quote;
import com.bitsfromspace.moneytracker.services.QuoteService;
import com.bitsfromspace.utils.Cache;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

import static com.bitsfromspace.utils.ExceptionUtils.swallow;
import static com.bitsfromspace.utils.ExceptionUtils.unchecked;

/**
 * @author chris
 * @since 28/09/2016.
 */
@Singleton
@Named
public class BloombergQuoteService implements QuoteService {

    private final Cache<String, Quote> quoteCache;

    @Inject
    public BloombergQuoteService(Clock clock, @Value("${moneytrackr.bloombergQuoteService.cacheTimoutMinutes}") int cacheTimeoutMinutes){
        quoteCache = new Cache<>(clock, cacheTimeoutMinutes, TimeUnit.MINUTES);
    }

    @Override
    public @Null Quote getQuote(@NotNull  String symbol) {
        return quoteCache.getOrSet(symbol, this::lookup);
    }

    private @Null Quote lookup(String symbol) {

        Element priceElement =
                unchecked( () -> Jsoup.connect(String.format("http://www.bloomberg.com/quote/%s", symbol))
                        .get()
                        .select(".price")
                        .first());

        return priceElement == null ? null : swallow(()->new Quote(symbol, Double.valueOf(priceElement.text())), t -> null);

    }
}
