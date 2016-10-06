package com.bitsfromspace.moneytracker.services.impl;

import com.bitsfromspace.moneytracker.services.Currency;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author chris
 * @since 29/09/2016.
 */
public class YahooCurrencyServiceTest {

    private YahooCurrencyService yahooCurrencyService;

    @Before
    public void setUp() throws Exception {
        yahooCurrencyService = new YahooCurrencyService(Clock.systemDefaultZone(), 1);
    }

    @Test
    public void testRealLookup() throws Exception {
        assertThat(yahooCurrencyService.getExchangeRate(Currency.USD, Currency.EUR), is(closeTo(0.5, 0.49)));
        assertThat(yahooCurrencyService.getExchangeRate(Currency.EUR, Currency.USD), is(closeTo(1.5, 0.49)));
    }

    @Test
    public void testSameCurrency() throws Exception {
        assertThat(yahooCurrencyService.getExchangeRate(Currency.USD, Currency.USD), is(1.0));
        assertThat(yahooCurrencyService.getExchangeRate(Currency.EUR, Currency.EUR), is(1.0));
    }
}