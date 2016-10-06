package com.bitsfromspace.moneytracker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
@Document (collection = "portfoliovalue123")
public class PortfolioValue {
    @Id
    private final PortfolioValueId id;
    @Indexed
    private final String portfolioEmail;
    @Indexed
    private final LocalDate date;

    private final double value;
    private final double change;
    private final double changePercentage;

    public PortfolioValue(@NotNull String portfolioEmail, @NotNull LocalDate date, double value, double change, double changePercentage) {
        this.id = new PortfolioValueId(portfolioEmail, date);
        this.portfolioEmail = portfolioEmail;
        this.date = date;
        this.value = value;
        this.change = change;
        this.changePercentage = changePercentage;
    }

    public @NotNull String getPortfolioEmail() {
        return portfolioEmail;
    }

    public @NotNull LocalDate getDate(){
        return date;
    }

    public @NotNull PortfolioValueId getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public double getChange() {
        return change;
    }

    public double getChangePercentage() {
        return changePercentage;
    }
}
