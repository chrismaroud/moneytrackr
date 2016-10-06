package com.bitsfromspace.moneytracker.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
public class AssetValueId implements Serializable{

    private final String assetId;
    private final LocalDate date;

    public AssetValueId(@NotNull String assetId, @NotNull LocalDate date) {
        this.assetId = assetId;
        this.date = date;
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public @NotNull String getAssetId() {
        return assetId;
    }
}
