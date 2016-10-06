package com.bitsfromspace.moneytracker.services;

import com.bitsfromspace.moneytracker.model.Asset;
import com.bitsfromspace.moneytracker.model.AssetValue;

import javax.validation.constraints.NotNull;

/**
 * @author chris
 * @since 30/09/2016.
 */
public interface AssetValueCalculationService {
    @NotNull
    AssetValue calculate(@NotNull Asset asset, @NotNull Currency targetCurrency);
}
