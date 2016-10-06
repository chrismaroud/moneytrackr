package com.bitsfromspace.moneytracker.services;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author chris
 * @since 28/09/2016.
 */
public interface QuoteService {

    @Null Quote getQuote(@NotNull String symbol);
}
