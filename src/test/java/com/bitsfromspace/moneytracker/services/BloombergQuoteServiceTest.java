package com.bitsfromspace.moneytracker.services;

import com.bitsfromspace.moneytracker.services.impl.BloombergQuoteService;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNull;

/**
 * @author chris
 * @since 29/09/2016.
 */
public class BloombergQuoteServiceTest {

    private BloombergQuoteService bloombergQuoteService;

    @Before
    public void setUp() throws Exception {
        bloombergQuoteService = new BloombergQuoteService(Clock.systemDefaultZone(), 10);
    }

    @Test
    public void testQuote() throws Exception {

        final Quote quote = bloombergQuoteService.getQuote("INFO:US");
        assertThat(quote, is(not(nullValue())));
        assertThat(quote.getSymbol(), is("INFO:US"));
        assertThat(quote.getClose(), is(closeTo(40, 10)));

    }

    @Test
    public void testNoSuchQuote() throws Exception {
        final Quote quote = bloombergQuoteService.getQuote("CHRIS:NL");
        assertThat(quote, is(isNull()));
    }

    @Test
    public void testRetiredQuote() throws Exception {

        final Quote quote = bloombergQuoteService.getQuote("MRKT:US");
        assertThat(quote, is(isNull()));
    }
}