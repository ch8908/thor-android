package com.osolve.thor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kros on 7/21/14.
 */
public class CoffeeShop {
    private final String shopId;
    private final String name;
    private final double latitude;
    private final double longitude;
    private final boolean wifiFree;
    private final boolean powerOutlet;

    @JsonCreator
    public CoffeeShop(@JsonProperty("id") final String shopId,
                      @JsonProperty("name") final String name,
                      @JsonProperty("lat") final double latitude,
                      @JsonProperty("lng") final double longitude,
                      @JsonProperty("is_wifi_free") final boolean wifiFree,
                      @JsonProperty("power_outlets") final boolean powerOutlet) {

        this.shopId = shopId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wifiFree = wifiFree;
        this.powerOutlet = powerOutlet;
    }

    public String getShopId() {
        return shopId;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isWifiFree() {
        return wifiFree;
    }

    public boolean isPowerOutlet() {
        return powerOutlet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoffeeShop that = (CoffeeShop) o;

        if (!shopId.equals(that.shopId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return shopId.hashCode();
    }

    @Override
    public String toString() {
        return "CoffeeShop{" +
                "shopId='" + shopId + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", wifiFree=" + wifiFree +
                ", powerOutlet=" + powerOutlet +
                '}';
    }
}
