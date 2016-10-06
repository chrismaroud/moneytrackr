package com.bitsfromspace.moneytracker.services;

/**
 * @author chris
 * @since 29/09/2016.
 */
public interface CurrencyService {

    double getExchangeRate(Currency from, Currency to);
}
