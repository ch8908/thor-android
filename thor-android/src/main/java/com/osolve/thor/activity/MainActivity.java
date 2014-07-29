package com.osolve.thor.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
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
import com.osolve.thor.fragment.AddShopActivity;
import com.osolve.thor.fragment.ShopDetailFragment;
import com.osolve.thor.fragment.event.AddShopEvent;
import com.osolve.thor.fragment.event.SignInEvent;
import com.osolve.thor.model.CoffeeShop;
import com.osolve.thor.model.ShopClusterItem;
import com.osolve.thor.util.ViewHelper;
import com.osolve.thor.view.CoffeeShopRender;
import com.osolve.thor.view.ShopListAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends BaseFragmentActivity
        implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    enum MainRequestCode {
        ADD_SHOP_REQUEST_CODE
    }

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
        layoutParams.topMargin = ViewHelper.getScreenHeight(this) / 2;
        layoutParams.height = ViewHelper.getScreenHeight(this) - layoutParams.topMargin - ViewHelper.getNavigationBarHeight(this) - ViewHelper.getStatusBarHeight(this);
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
        setUpMapIfNeeded();
        mLocationClient.connect();
    }

    @Override
    protected void onPause() {
        mLocationClient.disconnect();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                boolean popped = getSupportFragmentManager().popBackStackImmediate();
                if (!popped) {
                    toggle();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (MainRequestCode.ADD_SHOP_REQUEST_CODE.ordinal() == requestCode) {
            if (resultCode == RESULT_OK) {
                CoffeeShop newShop = AddShopActivity.getNewShop(data);
                LatLng latLng = new LatLng(newShop.getLatitude(), newShop.getLongitude());

                shopListAdapter.add(newShop);
                shopListView.setSelection(shopListAdapter.getCount());

                ShopClusterItem newClusterItem = new ShopClusterItem(newShop, latLng);
                clusterManager.addItem(newClusterItem);
                clusterItems.add(newClusterItem);
                clusterManager.cluster();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            }
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment shopMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = shopMapFragment.getMap();
            if (mMap != null) {
                setUpClusterManager();
                setUpMap();
            }
        }
    }

    private void setUpClusterManager() {
        clusterManager = new ClusterManager<ShopClusterItem>(MainActivity.this, mMap);
        renderer = new CoffeeShopRender(MainActivity.this, mMap, clusterManager);
        clusterManager.setRenderer(renderer);
        mMap.setOnCameraChangeListener(clusterManager);
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setPadding(0, 0, 0, ViewHelper.getScreenHeight(this) / 2);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!coffeeShops.isEmpty()) {
                    return false;
                }

                Location lastLocation = mLocationClient.getLastLocation();
                if (lastLocation == null) {
                    Log.e(TAG, "lastLocation is null, probably GPS is disable!");
                    return false;
                }
                listCoffeeShops(lastLocation);
                return false;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ShopClusterItem item = renderer.getItemByMarker(marker);
                changeToDetailFragment(item);
                return false;
            }
        });
    }

    private void changeToDetailFragment(ShopClusterItem item) {
        CoffeeShop shop = item.getShop();
        ShopDetailFragment shopDetailFragment = ShopDetailFragment.newInstance(shop.getShopId());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, shopDetailFragment, ShopDetailFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(MainActivity.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = mLocationClient.getLastLocation();
        if (lastLocation == null) {
            Log.e(TAG, "lastLocation is null, probably GPS is disable!");
            return;
        }

        if (!coffeeShops.isEmpty()) {
            return;
        }

        listCoffeeShops(lastLocation);

        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
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
                Log.d(TAG, "listCoffeeShop success:" + coffeeShops);

                coffeeShops.clear();
                coffeeShops.addAll(task.getResult());

                shopListAdapter.reload(coffeeShops);

                clusterItems.clear();
                for (CoffeeShop shop : coffeeShops) {
                    ShopClusterItem shopItem = new ShopClusterItem(shop, new LatLng(shop.getLatitude(), shop.getLongitude()));
                    clusterItems.add(shopItem);
                }

                clusterManager.addItems(clusterItems);
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

    @Subscribe
    public void onAddShopEvent(final AddShopEvent event) {
        openAddShopActivity();
    }

    private void openAddShopActivity() {
        Intent intent = new Intent(this, AddShopActivity.class);
        startActivityForResult(intent, MainRequestCode.ADD_SHOP_REQUEST_CODE.ordinal());
        toggle();
    }

    @Subscribe
    public void onSignInEvent(final SignInEvent event) {
        openToSignInActivity();
    }

    private void openToSignInActivity() {

    }

}
