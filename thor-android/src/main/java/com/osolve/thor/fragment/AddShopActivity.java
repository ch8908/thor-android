package com.osolve.thor.fragment;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
import com.osolve.thor.model.CoffeeShop;
import com.osolve.thor.model.ShopDto;
import com.osolve.thor.model.SubmitInformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Kros on 7/28/14.
 */
public class AddShopActivity extends BaseFragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String TAG = AddShopActivity.class.getSimpleName();
    private static final String NEW_SHOP_BUNDLE_KEY = "NEW_SHOP_BUNDLE_KEY";
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_add_shop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submitNewShop:
                submitNewShop();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitNewShop() {
        SubmitInformation submitInformation = createSubmitInfo();
        bean().coffeeShopDaemon.submitNewShop(submitInformation).continueWith(new Continuation<ShopDto, Object>() {
            @Override
            public Object then(Task<ShopDto> task) throws Exception {
                if (task.isFaulted()) {
                    Log.e(TAG, "submit new shop failed", task.getError());
                    return null;
                }
                Log.d(TAG, "submit new shop successfully");
                ShopDto shopDto = task.getResult();

                Intent resultIntent = new Intent();
                resultIntent.putExtra(NEW_SHOP_BUNDLE_KEY, new CoffeeShop(shopDto.getShopId(), shopDto.getName(), shopDto.getLat(), shopDto.getLng(), shopDto.isWifiFree(), shopDto.isHasPowerOutlet()));

                setResult(RESULT_OK, resultIntent);
                finish();
                return null;
            }
        });
    }

    private SubmitInformation createSubmitInfo() {

        EditText hoursEditText = (EditText) findViewById(R.id.hoursEditText);
        EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        EditText websiteEditText = (EditText) findViewById(R.id.websiteEditText);
        EditText shopNameEditText = (EditText) findViewById(R.id.shopNameEditText);
        EditText phoneEditText = (EditText) findViewById(R.id.phoneEditText);

        CameraPosition cameraPosition = mMap.getCameraPosition();

        return new SubmitInformation(shopNameEditText.getText().toString(),
                phoneEditText.getText().toString(),
                addressEditText.getText().toString(),
                cameraPosition.target.latitude,
                cameraPosition.target.longitude,
                hoursEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                websiteEditText.getText().toString(),
                false,
                false);
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static CoffeeShop getNewShop(Intent data) {
        return (CoffeeShop) data.getSerializableExtra(NEW_SHOP_BUNDLE_KEY);
    }
}
