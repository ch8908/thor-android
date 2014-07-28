package com.osolve.thor.app;

import android.location.Location;

import com.osolve.thor.client.ApiClient;
import com.osolve.thor.model.CoffeeShop;
import com.osolve.thor.model.ShopDto;
import com.osolve.thor.model.UserLoginInfo;

import java.util.List;

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

    public Task<List<CoffeeShop>> listCoffeeShopsWithLocation(Location location) {
        return apiClient.listCoffeeShopWithLocation(location.getLatitude(), location.getLongitude(), 30, 50, 0);
    }

    public Task<UserLoginInfo> login(String email, String password) {
        return apiClient.login(email, password);
    }

    public Task<ShopDto> shopDetailWithId(String shopId) {
        return apiClient.fetchShopDetailWithId(shopId);
    }
}
