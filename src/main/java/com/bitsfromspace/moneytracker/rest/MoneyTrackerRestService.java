package com.bitsfromspace.moneytracker.rest;

import com.bitsfromspace.moneytracker.dao.Dao;
import com.bitsfromspace.moneytracker.model.*;
import com.bitsfromspace.moneytracker.services.AssetValueCalculationService;
import com.bitsfromspace.moneytracker.services.Currency;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

import static com.bitsfromspace.moneytracker.rest.CredentialsHelper.getEmail;
import static com.bitsfromspace.utils.Conditions.notNull;

/**
 * @author chris
 * @since 02/10/2016.
 */
@RestController
@Singleton
public class MoneyTrackerRestService {

    private final Dao dao;
    private final Clock clock;
    private final AssetValueCalculationService assetValueCalculationService;

    @Inject
    public MoneyTrackerRestService(Dao dao, Clock clock, AssetValueCalculationService assetValueCalculationService) {
        this.dao = dao;
        this.clock = clock;
        this.assetValueCalculationService = assetValueCalculationService;
    }

    @RequestMapping(value = "/portfolio", method = RequestMethod.GET)
    public Portfolio getPortfolio(Principal user){
        final String email = getEmail(user);
        return email == null ? null : dao.getPortfolio(email);
    }
    @RequestMapping(value = "/portfolio", method = RequestMethod.POST)
    public void savePortfolio(Principal user, @RequestParam("currency") Currency defaultCurrency){
        final String email = getEmail(user);
        final LocalDate createDate = LocalDate.now(clock);
        final Portfolio portfolio = new Portfolio(email, defaultCurrency, createDate);
        dao.save(portfolio);
    }

    @RequestMapping(value="/asset", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON)
    public Portfolio addAsset(Principal user, AssetType assetType, Currency currency,
                              @RequestParam(required = false) Double initialAmount,
                              @RequestParam(required = false) Double interestPercentage,
                              @RequestParam(required = false) String bbSymbol,
                              @RequestParam(required = false) Integer numberOfShares,
                              @RequestParam(required = false) Double strikePrice) {

        notNull(assetType, "assetType");
        notNull(currency, "currency");
        notNull(user, ()-> "Unable to retrieve user authentication context. Please try reloading the page.");

        final Portfolio portfolio = getPortfolio(user);
        notNull(portfolio, () -> "Unable to find portfolio for user");

        final Asset asset = createAsset(assetType, currency, initialAmount, interestPercentage, bbSymbol, numberOfShares, strikePrice);
        setInitialValue(asset, portfolio.getCurrency());
        portfolio.addAsset(asset);
        return dao.save(portfolio);
    }

    private void setInitialValue(final Asset asset, Currency targetCurrency) {
        final AssetValue initialValue = assetValueCalculationService.calculate(asset, targetCurrency);
        asset.setCreateValue(initialValue)
             .setLatestValue(initialValue)
             .setLowestValue(initialValue)
             .setHighestValue(initialValue);
    }

    private Asset createAsset(AssetType assetType, Currency currency, Double initialAmount, Double interestPercentage, String bbSymbol, Integer numberOfShares, Double strikePrice) {
        final String id = UUID.randomUUID().toString();
        final LocalDate createDate = LocalDate.now(clock);

        switch (assetType){
            case CASH: {
                notNull(initialAmount, "initialAmount");
                notNull(interestPercentage, "interestPercentage");
                return new Cash(id, createDate, currency, initialAmount).setInterestPercentage(interestPercentage);
            }
            case SHARE: {
                notNull(numberOfShares, "numberOfShares");
                return new Share(id, createDate, currency, bbSymbol).setNumberOfShares(numberOfShares);
            }
            case OPTION:{
                notNull(numberOfShares, "numberOfShares");
                notNull(strikePrice, "strikePrice");
                return new Option(id, createDate, currency, bbSymbol, strikePrice).setNumberOfShares(numberOfShares);
            }
            default: throw new IllegalArgumentException("Unknown asset type: " + assetType);
        }
    }


}
