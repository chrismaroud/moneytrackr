package com.bitsfromspace.moneytracker.services.impl;

import com.bitsfromspace.moneytracker.model.*;
import com.bitsfromspace.moneytracker.services.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author chris
 * @since 30/09/2016.
 */
@Named
@Singleton
public class AssetValueCalculationServiceImpl implements AssetValueCalculationService {

    private final Clock clock;
    private final QuoteService quoteService;
    private final CurrencyService currencyService;

    @Inject
    public AssetValueCalculationServiceImpl(Clock clock, QuoteService quoteService, CurrencyService currencyService) {
        this.clock = clock;
        this.quoteService = quoteService;
        this.currencyService = currencyService;
    }

    @Override
    public AssetValue calculate(@NotNull Asset asset, @NotNull Currency targetCurrency) {
        switch (asset.getType()) {
            case CASH:
                return calculate((Cash) asset, targetCurrency);
            case SHARE:
                return calculate((Share) asset, targetCurrency);
            case OPTION:
                return calculate((Option) asset, targetCurrency);
            default:
                throw new IllegalArgumentException("No such asset type: " + asset.getType());
        }
    }

    private AssetValue calculate(Cash cash, Currency targetCurrency) {
        final LocalDate today = LocalDate.now(clock);
        final double lastValue = cash.getLatestValue() == null ? cash.getInitialAmount() : cash.getLatestValue().getValue();
        final int numDays = (int) DAYS.between(today, cash.getLatestValue() == null ? cash.getCreateDate() : cash.getLatestValue().getDate());
        final double interest = 365d / numDays * cash.getInterestPercentage();
        final double newValue = lastValue + interest;
        final double changePercentage = (newValue / lastValue) - 1;

        return new AssetValue(cash.getId(), today, newValue, changePercentage);

    }

    private AssetValue calculate(Share share, Currency targetCurrency) {
        final LocalDate today = LocalDate.now(clock);
        final Quote quote = quoteService.getQuote(share.getBbSymbol());
        final double newValue = convert(quote.getClose() * share.getNumberOfShares(), share.getCurrency(), targetCurrency);
        final double lastValue = share.getLatestValue() == null ? newValue : share.getLatestValue().getValue();
        final double changePercentage = (newValue / lastValue) - 1;
        return new AssetValue(share.getId(), today, newValue, changePercentage);
    }

    private AssetValue calculate(Option option, Currency targetCurrency) {
        final LocalDate today = LocalDate.now(clock);
        final Quote quote = quoteService.getQuote(option.getBbSymbol());
        final double newValue = convert(Math.max(0, quote.getClose() - option.getStrikePrice()) * option.getNumberOfShares(), option.getCurrency(), targetCurrency);
        final double lastValue = option.getLatestValue() == null ? newValue : option.getLatestValue().getValue();
        final double changePercentage = (newValue / lastValue) - 1;
        return new AssetValue(option.getId(), today, newValue, changePercentage);
    }

    private double convert(double amount, Currency from, Currency to) {
        if (from == to || amount == 0) {
            return amount;
        }
        return amount * currencyService.getExchangeRate(from, to);

    }
}

