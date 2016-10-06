package com.bitsfromspace.moneytracker.dao.impl;

import com.bitsfromspace.moneytracker.model.*;
import com.bitsfromspace.moneytracker.services.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * @author chris
 * @since 29/09/2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("/application-test.properties")
public class DaoImplTest {

    private DaoImpl dao;
    private MongoTemplate mongoTemplate;

    @Inject
    public void setDao(DaoImpl dao) {
        this.dao = dao;
    }

    @Inject
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Before
    public void setup() {
        mongoTemplate.getDb().dropDatabase();
    }

    @Test
    public void testPortfolio() throws Exception {
        final String email = "chris@oudeman.com";
        final Currency currency = Currency.EUR;
        final LocalDate createDate = LocalDate.of(2016, 9, 16);

        assertThat(dao.getPortfolio(email), is(nullValue()));
        Portfolio portfolio = new Portfolio(email, currency, createDate);
        dao.save(portfolio);
        portfolio = dao.getPortfolio(email);
        assertThat(portfolio, is(not(nullValue())));
        assertThat(portfolio.getEmail(), is(email));
        assertThat(portfolio.getCurrency(), is(currency));
        assertThat(portfolio.getCreateDate(), is(createDate));

        List<Asset> assets = new ArrayList<>();
        assets.add(new Cash("asset-1", LocalDate.of(2016, 1, 1), Currency.EUR, 1000.1000).setInterestPercentage(0.054555));
        assets.add(new Cash("asset-2", LocalDate.of(2016, 1, 2), Currency.USD, 2000.2000).setInterestPercentage(0.000000000000012));
        assets.add(new Share("asset-3", LocalDate.of(2016, 1, 3), Currency.EUR, "symbol1").setNumberOfShares(3000.123));
        assets.add(new Share("asset-4", LocalDate.of(2016, 1, 4), Currency.USD, "symbol2").setNumberOfShares(4000.123));
        assets.add(new Option("asset-5", LocalDate.of(2016, 1, 5), Currency.EUR, "symbol3", 12.123).setNumberOfShares(5000.123));
        assets.add(new Option("asset-6", LocalDate.of(2016, 1, 6), Currency.USD, "symbol4", 0.12).setNumberOfShares(6000.123));

        portfolio.setAssets(assets);
        dao.save(portfolio);

        portfolio = dao.getPortfolio(email);
        assertThat(portfolio, is(not(nullValue())));
        assertThat(portfolio.getEmail(), is(email));
        assertThat(portfolio.getCurrency(), is(currency));
        assertThat(portfolio.getCreateDate(), is(createDate));
        assertThat(assets.size(), is(6));

        assertCash(assets.get(0), "asset-1", LocalDate.of(2016,1,1), Currency.EUR, 1000.1000, 0.054555);
        assertCash(assets.get(1), "asset-2", LocalDate.of(2016,1,2), Currency.USD, 2000.2000, 0.000000000000012);
        assertShare(assets.get(2), "asset-3", LocalDate.of(2016,1,3), Currency.EUR, "symbol1", 3000.123);
        assertShare(assets.get(3), "asset-4", LocalDate.of(2016,1,4), Currency.USD, "symbol2", 4000.123);
        assertOption(assets.get(4), "asset-5", LocalDate.of(2016,1,5), Currency.EUR, "symbol3", 5000.123, 12.123);
        assertOption(assets.get(5), "asset-6", LocalDate.of(2016,1,6), Currency.USD, "symbol4", 6000.123, 0.12);

        portfolio.getAssets().get(0).setHighestValue(new AssetValue("asset-1", LocalDate.of(2016,10,1), 99.99, 0.03333));
        portfolio.getAssets().get(0).setLowestValue(new AssetValue("asset-1", LocalDate.of(2016,10,25), 9.99, -0.13));
        portfolio.getAssets().get(0).setLatestValue(new AssetValue("asset-1", LocalDate.of(2016,10,30), 12, 0.035));
        dao.save(portfolio);

        portfolio = dao.getPortfolio(email);
        assertNull(portfolio.getAssets().get(1).getLowestValue());
        assertNull(portfolio.getAssets().get(2).getLowestValue());
        assertNull(portfolio.getAssets().get(3).getLowestValue());
        assertNull(portfolio.getAssets().get(4).getLowestValue());
        assertNull(portfolio.getAssets().get(5).getLowestValue());
        assertNull(portfolio.getAssets().get(1).getHighestValue());
        assertNull(portfolio.getAssets().get(2).getHighestValue());
        assertNull(portfolio.getAssets().get(3).getHighestValue());
        assertNull(portfolio.getAssets().get(4).getHighestValue());
        assertNull(portfolio.getAssets().get(5).getHighestValue());
        assertNull(portfolio.getAssets().get(1).getLatestValue());
        assertNull(portfolio.getAssets().get(2).getLatestValue());
        assertNull(portfolio.getAssets().get(3).getLatestValue());
        assertNull(portfolio.getAssets().get(4).getLatestValue());
        assertNull(portfolio.getAssets().get(5).getLatestValue());
        assertAssetValue(portfolio.getAssets().get(0).getHighestValue(), "asset-1", LocalDate.of(2016, 10,1), 99.99, 0.03333);
        assertAssetValue(portfolio.getAssets().get(0).getLowestValue(), "asset-1", LocalDate.of(2016, 10,25), 9.99, -0.13);
        assertAssetValue(portfolio.getAssets().get(0).getLatestValue(), "asset-1", LocalDate.of(2016, 10,30), 12, 0.035);

        assertNull(portfolio.getLatestValue());
        assertNull(portfolio.getLowestValue());
        assertNull(portfolio.getHighestValue());

        portfolio.setLatestValue(new PortfolioValue(email, LocalDate.of(2016,11,1), 23725.23423, 124124.23, 0.123123))
                .setLowestValue(new PortfolioValue(email, LocalDate.of(2017,2,1), 98298424.124, 221412.244, 21412124.124412421))
                .setHighestValue(new PortfolioValue(email, LocalDate.of(2019,12,30), 3928432.432, 834.234, 23.22332));
        dao.save(portfolio);

        portfolio = dao.getPortfolio(email);
        assertPortfolioValue(portfolio.getLatestValue(), email, LocalDate.of(2016,11,1),23725.23423, 124124.23, 0.123123);
        assertPortfolioValue(portfolio.getLowestValue(), email, LocalDate.of(2017,2,1), 98298424.124, 221412.244, 21412124.124412421);
        assertPortfolioValue(portfolio.getHighestValue(), email, LocalDate.of(2019,12,30), 3928432.432, 834.234, 23.22332);

    }

    @Test
    public void testPortfolioValue() throws Exception {
        final String id = "portfolioId";
        assertThat(dao.getPortfolioValues(id, LocalDate.of(1970,1,1), LocalDate.of(3000,12,31)).count(), is(0L));

        dao.save(new PortfolioValue("id1", LocalDate.of(2016, 1, 1), 123.123, 456.456, 789.789));
        dao.save(new PortfolioValue("id1", LocalDate.of(2016, 1, 2), 321.321, 654.654, 987.987));
        dao.save(new PortfolioValue("id1", LocalDate.of(2016, 1, 1), 132.132, 465.465, 798.798));
        assertThat(dao.getPortfolioValues("id1", LocalDate.of(1970,1,1), LocalDate.of(3000,12,31)).count(), is(2L)); //2016,1,1 overrides first
        assertThat(dao.getPortfolioValues("id1", LocalDate.of(1970,1,1), LocalDate.of(3000,12,31)).collect(Collectors.toList()).get(0).getValue(), is(132.132));


        dao.save(new PortfolioValue("id2", LocalDate.of(2016, 1, 1), 1.1, 11.11, 111.111));
        dao.save(new PortfolioValue("id2", LocalDate.of(2016, 1, 2), 2.2, 22.22, 222.222));
        dao.save(new PortfolioValue("id2", LocalDate.of(2016, 1, 3), 3.3, 33.33, 333.333));
        dao.save(new PortfolioValue("id2", LocalDate.of(2016, 1, 4), 4.3, 43.33, 433.333));
        dao.save(new PortfolioValue("id2", LocalDate.of(2016, 1, 5), 5.3, 53.33, 533.333));
        dao.save(new PortfolioValue("id2", LocalDate.of(2016, 1, 6), 6.3, 63.33, 633.333));
        assertThat(dao.getPortfolioValues("id2", LocalDate.of(1970,1,1), LocalDate.of(3000,12,31)).count(), is(6L));
        assertThat(dao.getPortfolioValues("id2", LocalDate.of(2016,1,1), LocalDate.of(2016,1,6)).count(), is(4L));
        assertThat(dao.getPortfolioValues("id2", LocalDate.of(2016,1,1), LocalDate.of(2016,1,4)).count(), is(2L));
        assertThat(dao.getPortfolioValues("id2", LocalDate.of(2016,1,2), LocalDate.of(2016,1,3)).count(), is(0L));
        assertThat(dao.getPortfolioValues("id2", LocalDate.of(2016,1,3), LocalDate.of(2099,1,5)).count(), is(3L));

        final List<PortfolioValue> values = dao.getPortfolioValues("id2", LocalDate.of(1970,1,1), LocalDate.of(3000,12,31)).collect(Collectors.toList());
        assertThat(values.size(), is(6));
        assertPortfolioValue(values.get(0), "id2", LocalDate.of(2016,1,1), 1.1, 11.11, 111.111);
        assertPortfolioValue(values.get(1), "id2", LocalDate.of(2016, 1, 2), 2.2, 22.22, 222.222);
        assertPortfolioValue(values.get(2), "id2", LocalDate.of(2016, 1, 3), 3.3, 33.33, 333.333);
        assertPortfolioValue(values.get(3), "id2", LocalDate.of(2016, 1, 4), 4.3, 43.33, 433.333);
        assertPortfolioValue(values.get(4), "id2", LocalDate.of(2016, 1, 5), 5.3, 53.33, 533.333);
        assertPortfolioValue(values.get(5), "id2", LocalDate.of(2016, 1, 6), 6.3, 63.33, 633.333);




    }

    private void assertPortfolioValue(PortfolioValue portfolioValue, String portfolioId, LocalDate date, double value, double change, double changePercentage){
        assertNotNull(portfolioValue);
        assertThat(portfolioValue.getPortfolioEmail(), is(portfolioId));
        assertThat(portfolioValue.getDate(), is(date));
        assertThat(portfolioValue.getId().getEmail(), is(portfolioId));
        assertThat(portfolioValue.getId().getDate(), is(date));
        assertThat(portfolioValue.getValue(), is(value));
        assertThat(portfolioValue.getChange(), is(change));
        assertThat(portfolioValue.getChangePercentage(), is(changePercentage));

    }
    private void assertAssetValue(AssetValue assetValue, String assetId, LocalDate date, double value, double relativeValueChange){
        assertNotNull(assetValue);
        assertThat(assetValue.getAssetId(), is(assetId));
        assertThat(assetValue.getDate(), is(date));
        assertThat(assetValue.getId().getAssetId(), is(assetId));
        assertThat(assetValue.getId().getDate(), is(date));
        assertThat(assetValue.getValue(), is(value));
        assertThat(assetValue.getRelativeChangePercentage(), is(relativeValueChange));
    }

    private void assertCash(Asset asset, String id, LocalDate date, Currency currency, double initialAmount, double interestPercentage){
        assertNotNull(asset);
        assertThat((Cash)asset, isA(Cash.class));
        assertThat(asset.getId(), is(id));
        assertThat(asset.getCreateDate(), is(date));
        assertThat(asset.getCurrency(), is(currency));
        assertThat(((Cash) asset).getInitialAmount(), is(initialAmount));
        assertThat(((Cash) asset).getInterestPercentage(), is(interestPercentage));

    }

    private void assertOption(Asset asset, String id, LocalDate date, Currency currency, String symbol, double numberOfShares, double strikePrice){
        assertShare(asset, id, date, currency, symbol, numberOfShares);
        assertThat((Option)asset, isA(Option.class));
        assertThat(((Option)asset).getStrikePrice(), is(strikePrice));
    }
    private void assertShare(Asset asset, String id, LocalDate date, Currency currency, String symbol, double numberOfShares){
        assertNotNull(asset);
        assertThat((Share)asset, isA(Share.class));
        assertThat(asset.getId(), is(id));
        assertThat(asset.getCreateDate(), is(date));
        assertThat(asset.getCurrency(), is(currency));
        assertThat(((Share) asset).getBbSymbol(), is(symbol));
        assertThat(((Share) asset).getNumberOfShares(), is (numberOfShares));
    }

}