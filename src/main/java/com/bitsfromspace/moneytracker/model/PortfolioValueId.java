package com.bitsfromspace.moneytracker.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author chris
 * @since 02/10/2016.
 */
public class PortfolioValueId implements Serializable {

    private final String email;
    private final LocalDate date;

    public PortfolioValueId(@NotNull String email, @NotNull LocalDate date) {
        this.email = email;
        this.date = date;
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }
}


