package com.osolve.thor.view;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.osolve.thor.model.ShopClusterItem;

/**
 * Created by Kros on 7/25/14.
 */
public class CoffeeShopRender extends DefaultClusterRenderer<ShopClusterItem> {
    public CoffeeShopRender(Context context, GoogleMap map, ClusterManager<ShopClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(ShopClusterItem item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.title(item.getShop().getName());
        markerOptions.flat(true);
    }

    public Marker getMarkerByItem(ShopClusterItem item) {
        return getMarker(item);
    }
}
