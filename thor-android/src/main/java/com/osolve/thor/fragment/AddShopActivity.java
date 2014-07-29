package com.osolve.thor.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragmentActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kros on 7/28/14.
 */
public class AddShopActivity extends BaseFragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = AddShopActivity.class.getSimpleName();
    private GoogleMap mMap;
    private LocationClient mLocationClient;
    private Geocoder geocoder;
    private EditText addressEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shop);
        setUpMapIfNeeded();

        mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();

        geocoder = new Geocoder(this, Locale.getDefault());

        addressEditText = (EditText) findViewById(R.id.addressEditText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment shopMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.addShopMap);
            mMap = shopMapFragment.getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                try {
                    List<Address> fromLocation = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
                    Address address = fromLocation.get(0);
                    int maxLines = address.getMaxAddressLineIndex();
                    StringBuilder addressBuilder = new StringBuilder();
                    for (int i = 0; i <= maxLines; i++) {
                        addressBuilder.append(address.getAddressLine(i)).append(" ");
                    }
                    addressEditText.setText(addressBuilder.toString());

                } catch (IOException e) {
                    Log.e(TAG, "geocoder decode failed", e);
                }

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = mLocationClient.getLastLocation();
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
