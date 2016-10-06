package com.bitsfromspace.moneytracker.dao.impl;

import com.bitsfromspace.moneytracker.model.PortfolioValue;
import com.bitsfromspace.moneytracker.model.PortfolioValueId;
import org.springframework.data.repository.CrudRepository;

import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */

interface PortfolioValueRepository extends CrudRepository<PortfolioValue, PortfolioValueId>{

    @NotNull
    Iterable<PortfolioValue> findByPortfolioEmailAndDateBetweenOrderByDate(@NotNull String portfolioEmail, LocalDate startDate, LocalDate endDate);
}
