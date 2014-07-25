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
    public CoffeeShopRender(final Context context, final GoogleMap map, final ClusterManager<ShopClusterItem> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(final ShopClusterItem item, final MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.title(item.getShop().getName());
        markerOptions.flat(true);
    }

    public Marker getMarkerByItem(final ShopClusterItem item) {
        return getMarker(item);
    }

    public ShopClusterItem getItemByMarker(final Marker marker) {
        return getClusterItem(marker);
    }
}
