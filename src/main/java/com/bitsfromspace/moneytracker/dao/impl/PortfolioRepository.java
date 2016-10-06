package com.bitsfromspace.moneytracker.dao.impl;

import com.bitsfromspace.moneytracker.model.Portfolio;
import org.springframework.data.repository.CrudRepository;

/**
 * @author chris
 * @since 29/09/2016.
 */
interface PortfolioRepository extends CrudRepository<Portfolio, String> {
}
