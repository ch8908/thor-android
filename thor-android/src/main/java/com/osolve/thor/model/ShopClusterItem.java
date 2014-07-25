package com.osolve.thor.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Kros on 7/24/14.
 */
public class ShopClusterItem implements ClusterItem {

    private final CoffeeShop shop;
    private final LatLng position;

    public ShopClusterItem(CoffeeShop shop, LatLng positions) {
        this.shop = shop;
        this.position = positions;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public CoffeeShop getShop() {
        return shop;
    }
}
