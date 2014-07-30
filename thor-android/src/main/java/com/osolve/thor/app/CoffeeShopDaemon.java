package com.osolve.thor.app;

import android.location.Location;

import com.osolve.thor.client.ApiClient;
import com.osolve.thor.model.CoffeeShop;
import com.osolve.thor.model.ShopDto;
import com.osolve.thor.model.SubmitInformation;
import com.osolve.thor.model.UserLoginInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import bolts.Task;

/**
 * Created by Kros on 7/22/14.
 */
public class CoffeeShopDaemon extends BaseDaemon {
    private final ApiClient apiClient;

    public CoffeeShopDaemon(final Bean bean, final ApiClient apiClient) {
        super(bean);
        this.apiClient = apiClient;
    }

    public Task<List<CoffeeShop>> listCoffeeShopsWithLocation(final Location location) {
        return apiClient.listCoffeeShopWithLocation(location.getLatitude(), location.getLongitude(), 30, 50, 0);
    }

    public Task<UserLoginInfo> login(final String email, final String password) {
        return apiClient.login(email, password);
    }

    public Task<ShopDto> shopDetailWithId(final String shopId) {
        return apiClient.fetchShopDetailWithId(shopId);
    }

    public Task<ShopDto> submitNewShop(final SubmitInformation submitInformation) {
        Map<String, String> params = new TreeMap<>();

        params.put("name", submitInformation.getShopName());
        params.put("phone", submitInformation.getPhone());
        params.put("website_url", submitInformation.getWebsite());
        params.put("is_wifi_free", String.valueOf(submitInformation.isWifiFree()));
        params.put("power_outlets", String.valueOf(submitInformation.isHasPowerOutlet()));
        params.put("hours", submitInformation.getHours());
        params.put("lat", String.valueOf(submitInformation.getLat()));
        params.put("lng", String.valueOf(submitInformation.getLng()));
        params.put("address", submitInformation.getAddress());
        params.put("authentication_token", "FrTzaE2YG2DibzZkioYU");

        return apiClient.postNewShop(params);
    }

    @Override
    public void terminate() {

    }
}
