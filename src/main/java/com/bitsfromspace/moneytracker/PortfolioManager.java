package com.bitsfromspace.moneytracker;

import com.bitsfromspace.moneytracker.dao.Dao;
import com.bitsfromspace.moneytracker.model.Asset;
import com.bitsfromspace.moneytracker.model.AssetValue;
import com.bitsfromspace.moneytracker.model.Portfolio;
import com.bitsfromspace.moneytracker.model.PortfolioValue;
import com.bitsfromspace.moneytracker.services.AssetValueCalculationService;
import com.bitsfromspace.moneytracker.services.Currency;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Clock;
import java.time.LocalDate;

import static com.bitsfromspace.utils.ExceptionUtils.tryLog;

/**
 * @author chris
 * @since 01/10/2016.
 */
@Named
@Singleton
public class PortfolioManager {

    private final Dao dao;
    private final AssetValueCalculationService assetValueCalculationService;
    private final Clock clock;

    public PortfolioManager(Dao dao, AssetValueCalculationService assetValueCalculationService, Clock clock) {
        this.dao = dao;
        this.assetValueCalculationService = assetValueCalculationService;
        this.clock = clock;
    }

    @Scheduled(cron="0 15 9,12,15,19,22 ? * MON-FRI")//mon-fri 9:15, 12:15, 15:15, 19:15 & 22:15
    public synchronized void updatePortfolios(){
        dao.getAllPortfolios().forEach(p-> tryLog(getClass(), ()->updatePortfolio(p), ex -> "Error updating portfolio"));
    }

    private void updatePortfolio(Portfolio portfolio){
        portfolio.getAssets().forEach(asset -> updateAsset(asset, portfolio.getCurrency()));

        final LocalDate today = LocalDate.now(clock);
        final double newValue = portfolio.getAssets().stream().mapToDouble(a->a.getLatestValue().getValue()).sum();
        final double oldValue = portfolio.getLatestValue() == null ? newValue : portfolio.getLatestValue().getValue();
        final double change = newValue - oldValue;
        final double changePercentage = (newValue / oldValue) -1;

        final PortfolioValue portfolioValue = new PortfolioValue(portfolio.getEmail(), today, newValue, change, changePercentage);
        if (portfolio.getLowestValue() == null || portfolio.getLowestValue().getValue() >= newValue){
            portfolio.setLowestValue(portfolioValue);
        }
        if (portfolio.getHighestValue() == null || portfolio.getHighestValue().getValue() <= newValue){
            portfolio.setHighestValue(portfolioValue);
        }
        portfolio.setLatestValue(portfolioValue);
        dao.save(portfolio);
        dao.save(portfolioValue);
    }

    private void updateAsset(Asset asset, Currency targetCurrency){
        final AssetValue assetValue = assetValueCalculationService.calculate(asset, targetCurrency);
        if (asset.getLowestValue() == null || asset.getLowestValue().getValue() >= assetValue.getValue()){
            asset.setLowestValue(assetValue);
        }
        if (asset.getHighestValue() == null || asset.getHighestValue().getValue() <= assetValue.getValue()){
            asset.setHighestValue(assetValue);
        }
        asset.setLatestValue(assetValue);
        dao.save(assetValue);
    }
}
