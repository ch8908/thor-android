package com.osolve.thor.model;

/**
 * Created by Kros on 7/29/14.
 */
public class SubmitInformation {

    private final String shopName;
    private final String phone;
    private final String address;
    private final double lat;
    private final double lng;
    private final String hours;
    private final String shopDescription;
    private final String website;
    private final boolean isWifiFree;
    private final boolean hasPowerOutlet;

    public SubmitInformation(final String shopName,
                             final String phone,
                             final String address,
                             final double lat,
                             final double lng,
                             final String hours,
                             final String shopDescription,
                             final String website,
                             final boolean isWifiFree,
                             final boolean powerOutlet) {
        this.shopName = shopName;
        this.phone = phone;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.hours = hours;
        this.shopDescription = shopDescription;
        this.website = website;
        this.isWifiFree = isWifiFree;
        this.hasPowerOutlet = powerOutlet;
    }

    public String getShopName() {
        return shopName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getHours() {
        return hours;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public String getWebsite() {
        return website;
    }

    public boolean isWifiFree() {
        return isWifiFree;
    }

    public boolean isHasPowerOutlet() {
        return hasPowerOutlet;
    }

    @Override
    public String toString() {
        return "SubmitInformation{" +
                "shopName='" + shopName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", hours='" + hours + '\'' +
                ", shopDescription='" + shopDescription + '\'' +
                ", website='" + website + '\'' +
                ", isWifiFree=" + isWifiFree +
                ", hasPowerOutlet=" + hasPowerOutlet +
                '}';
    }
}
