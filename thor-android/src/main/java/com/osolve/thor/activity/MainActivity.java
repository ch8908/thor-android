package com.osolve.thor.activity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragmentActivity;
import com.osolve.thor.model.CoffeeShop;
import com.osolve.thor.model.ShopClusterItem;
import com.osolve.thor.util.ViewHelper;
import com.osolve.thor.view.CoffeeShopRender;
import com.osolve.thor.view.ShopListAdapter;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends BaseFragmentActivity
        implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationClient mLocationClient;

    private static final String TAG = MainActivity.class.getSimpleName();
    private final List<CoffeeShop> coffeeShops = new ArrayList<>();
    private ListView shopListView;
    private ShopListAdapter shopListAdapter;
    private final List<ShopClusterItem> clusterItems = new ArrayList<>();
    private ClusterManager<ShopClusterItem> clusterManager;
    private CoffeeShopRender renderer;

    public MainActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();

        mLocationClient = new LocationClient(this, this, this);

        shopListView = (ListView) findViewById(R.id.coffeeShopListView);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) shopListView.getLayoutParams();
        layoutParams.height = ViewHelper.getScreenHeight(this) / 2;
        layoutParams.topMargin = ViewHelper.getScreenHeight(this) / 2;
        shopListView.setLayoutParams(layoutParams);

        shopListAdapter = new ShopListAdapter();
        shopListView.setAdapter(shopListAdapter);

        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                CoffeeShop shop = shopListAdapter.getItem(position);
                for (ShopClusterItem clusterItem : clusterItems) {
                    if (clusterItem.getShop().equals(shop)) {
                        Marker marker = renderer.getMarkerByItem(clusterItem);
                        if (marker != null) {
                            marker.showInfoWindow();
                            break;
                        }
                    }
                }
                moveCameraToSelectedShops(shop);
            }
        });
    }

    private void moveCameraToSelectedShops(CoffeeShop shop) {
        LatLng latLng = new LatLng(shop.getLatitude(), shop.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f), 300, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setUpMapIfNeeded();
        mLocationClient.connect();
    }

    @Override
    protected void onPause() {
        mLocationClient.disconnect();
        super.onPause();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment shopMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = shopMapFragment.getMap();
            if (mMap != null) {
                clusterManager = new ClusterManager<ShopClusterItem>(MainActivity.this, mMap);
                renderer = new CoffeeShopRender(MainActivity.this, mMap, clusterManager);
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        System.out.println("set up map");
        mMap.setMyLocationEnabled(true);
        mMap.setPadding(0, 0, 0, ViewHelper.getScreenHeight(this) / 2);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = mLocationClient.getLastLocation();
        if (lastLocation == null) {
            Log.e(TAG, "lastLocation is null, probably GPS is disable!");
            return;
        }

        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

        listCoffeeShops(lastLocation);
    }

    private void listCoffeeShops(Location lastLocation) {
        bean().coffeeShopDaemon.listCoffeeShopsWithLocation(lastLocation).continueWith(new Continuation<List<CoffeeShop>, Object>() {
            @Override
            public Object then(Task<List<CoffeeShop>> task) throws Exception {
                if (task.isFaulted()) {
                    VolleyError error = (VolleyError) task.getError();
                    Log.d(TAG, "listCoffeeShop error:" + new String(error.networkResponse.data));
                    return null;
                }

                coffeeShops.clear();
                coffeeShops.addAll(task.getResult());

                clusterManager.setRenderer(renderer);

                mMap.setOnCameraChangeListener(clusterManager);

                shopListAdapter.reload(coffeeShops);

                clusterItems.clear();
                for (int i = 0; i < coffeeShops.size(); i++) {
                    CoffeeShop shop = coffeeShops.get(i);
                    ShopClusterItem shopItem = new ShopClusterItem(shop, new LatLng(shop.getLatitude(), shop.getLongitude()));
                    clusterItems.add(shopItem);
                }

                clusterManager.addItems(clusterItems);

                Log.d(TAG, "listCoffeeShop success:" + coffeeShops);

                return null;
            }
        });
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
