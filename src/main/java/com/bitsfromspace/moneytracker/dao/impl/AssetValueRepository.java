package com.bitsfromspace.moneytracker.dao.impl;

import com.bitsfromspace.moneytracker.model.AssetValue;
import com.bitsfromspace.moneytracker.model.AssetValueId;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
interface AssetValueRepository extends CrudRepository<AssetValue, AssetValueId> {
    @NotNull
    Iterable<AssetValue> findByAssetIdAndDateGreaterThanEqualAndDateLessThanEqualOrderByDate(@NotNull String assetId, LocalDate startDate, LocalDate endDate);
}
