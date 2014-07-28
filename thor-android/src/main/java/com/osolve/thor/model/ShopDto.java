package com.osolve.thor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kros on 7/28/14.
 */
public class ShopDto {
    private final String shopId;
    private final String name;
    private final double lat;
    private final double lng;
    private final String address;
    private final String shopDescription;
    private final boolean isWifiFree;
    private final boolean hasPowerOutlet;
    private final String hours;
    private final String webSiteUrl;
    private final double rating;

    @JsonCreator
    public ShopDto(@JsonProperty("id") final String shopId,
                   @JsonProperty("name") final String name,
                   @JsonProperty("lat") final double lat,
                   @JsonProperty("lng") final double lng,
                   @JsonProperty("address") final String address,
                   @JsonProperty("description") final String shopDescription,
                   @JsonProperty("is_wifi_free") final boolean isWifiFree,
                   @JsonProperty("power_outlets") final boolean hasPowerOutlet,
                   @JsonProperty("hours") final String hours,
                   @JsonProperty("website_url") final String webSiteUrl,
                   @JsonProperty("avg_rating") final double rating) {

        this.shopId = shopId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.shopDescription = shopDescription;
        this.isWifiFree = isWifiFree;
        this.hasPowerOutlet = hasPowerOutlet;
        this.hours = hours;
        this.webSiteUrl = webSiteUrl;
        this.rating = rating;
    }

    public String getShopId() {
        return shopId;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAddress() {
        return address;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public boolean isWifiFree() {
        return isWifiFree;
    }

    public boolean isHasPowerOutlet() {
        return hasPowerOutlet;
    }

    public String getHours() {
        return hours;
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public double getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShopDto)) return false;

        ShopDto that = (ShopDto) o;

        if (!shopId.equals(that.shopId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return shopId.hashCode();
    }

    @Override
    public String toString() {
        return "ShopDto{" +
                "shopId='" + shopId + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", address='" + address + '\'' +
                ", shopDescription='" + shopDescription + '\'' +
                ", isWifiFree=" + isWifiFree +
                ", hasPowerOutlet=" + hasPowerOutlet +
                ", hours='" + hours + '\'' +
                ", webSiteUrl='" + webSiteUrl + '\'' +
                ", rating=" + rating +
                '}';
    }
}
