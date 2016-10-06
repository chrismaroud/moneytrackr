package com.bitsfromspace.moneytracker.model;

import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
@Document
public class AssetValue {

    @Id
    private final AssetValueId id;
    @Indexed
    private final String assetId;
    @Indexed
    private final LocalDate date;
    private final double value;
    private final double relativeChangePercentage;

    public AssetValue(@NotNull String assetId, @NotNull LocalDate date, double value, double relativeChangePercentage) {
        this.id = new AssetValueId(assetId, date);
        this.assetId = assetId;
        this.date = date;
        this.value = value;
        this.relativeChangePercentage = relativeChangePercentage;
    }

    public @NotNull AssetValueId getId() {
        return id;
    }

    public @NotNull String getAssetId() {
        return assetId;
    }

    public @NotNull LocalDate getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    public double getRelativeChangePercentage() {
        return relativeChangePercentage;
    }
}
