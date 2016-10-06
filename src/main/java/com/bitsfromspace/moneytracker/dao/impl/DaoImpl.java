package com.bitsfromspace.moneytracker.dao.impl;

import com.bitsfromspace.moneytracker.dao.Dao;
import com.bitsfromspace.moneytracker.model.AssetValue;
import com.bitsfromspace.moneytracker.model.Portfolio;
import com.bitsfromspace.moneytracker.model.PortfolioValue;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author chris
 * @since 29/09/2016.
 */
@Named
@Singleton
public class DaoImpl implements Dao {
    private final AssetValueRepository assetValueRepo;
    private final PortfolioRepository portfolioRepo;
    private final PortfolioValueRepository portfolioValueRepo;

    @Inject
    public DaoImpl(AssetValueRepository assetValueRepo, PortfolioRepository portfolioRepo, PortfolioValueRepository portfolioValueRepo) {
        this.assetValueRepo = assetValueRepo;
        this.portfolioRepo = portfolioRepo;
        this.portfolioValueRepo = portfolioValueRepo;
    }

    @Override
    public Portfolio getPortfolio(String email) {
        return portfolioRepo.findOne(email);
    }

    @Override
    public Stream<Portfolio> getAllPortfolios() {
        return StreamSupport.stream(portfolioRepo.findAll().spliterator(), false);
    }

    @Override
    public Portfolio save(@NotNull Portfolio portfolio) {
        return portfolioRepo.save(portfolio);
    }

    @Override
    public Stream<PortfolioValue> getPortfolioValues(@NotNull String portfolioId, @NotNull LocalDate from, @NotNull LocalDate to) {
        return StreamSupport.stream(portfolioValueRepo.findByPortfolioEmailAndDateBetweenOrderByDate(portfolioId, from, to).spliterator(), false);
    }

    @Override
    public void save(@NotNull PortfolioValue portfolioValue) {
        portfolioValueRepo.save(portfolioValue);
    }

    @Override
    public Stream<AssetValue> getAssetValues(@NotNull String assetId, @NotNull LocalDate from, @NotNull LocalDate to) {
        return StreamSupport.stream(assetValueRepo.findByAssetIdAndDateGreaterThanEqualAndDateLessThanEqualOrderByDate(assetId, from, to).spliterator(), false);
    }

    @Override
    public void save(@NotNull AssetValue assetValue) {
        assetValueRepo.save(assetValue);
    }
}
