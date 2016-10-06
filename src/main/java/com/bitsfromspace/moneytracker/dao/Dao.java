package com.bitsfromspace.moneytracker.dao;

import com.bitsfromspace.moneytracker.model.AssetValue;
import com.bitsfromspace.moneytracker.model.Portfolio;
import com.bitsfromspace.moneytracker.model.PortfolioValue;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * @author chris
 * @since 29/09/2016.
 */
public interface Dao {

    @Null
    Portfolio getPortfolio(String email);
    Portfolio save(@NotNull Portfolio portfolio);
    Stream<Portfolio> getAllPortfolios();

    @NotNull
    Stream<PortfolioValue> getPortfolioValues(@NotNull String portfolioId, @NotNull LocalDate from, @NotNull LocalDate to);
    void save(@NotNull PortfolioValue portfolioValue);

    @NotNull
    Stream<AssetValue> getAssetValues(@NotNull String assetId, @NotNull LocalDate from, @NotNull LocalDate to);
    void save(@NotNull AssetValue assetValue);


}
