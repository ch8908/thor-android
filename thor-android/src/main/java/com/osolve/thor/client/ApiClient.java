package com.osolve.thor.client;

import com.android.volley.RequestQueue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.osolve.thor.model.CoffeeShop;
import com.osolve.thor.model.ShopDto;
import com.osolve.thor.model.UserLoginInfo;

import java.util.List;
import java.util.Map;

import bolts.Task;

/**
 * Created by Kros on 7/22/14.
 */
public class ApiClient {

    private final TypeReference<List<CoffeeShop>> coffeeShopListType = new TypeReference<List<CoffeeShop>>() {
    };

    private final RequestQueue queue;

    public ApiClient(final RequestQueue queue) {
        this.queue = queue;
    }

    private RequestBuilder builder() {
        return new RequestBuilder(queue).withUrlPrefix(ThorConstant.SERVER_TYPE.getFullUrlPrefix());
    }

    public Task<List<CoffeeShop>> listCoffeeShopWithLocation(double latitude, double longitude, int distance, int size, int page) {

        return builder().withHttpGetAllowCache()
                .withPath("/v1/shops/near")
                .withAddParam("lat", latitude)
                .withAddParam("lng", longitude)
                .withAddParam("distance", distance)
                .withAddParam("per_page", size)
                .withAddParam("page", page)
                .build(coffeeShopListType)
                .request();
    }

    public Task<UserLoginInfo> login(final String email, final String password) {
        return builder().withHttpPost()
                .withPath("/v1/users/tokens/create")
                .withAddParam("email", email)
                .withAddParam("password", password)
                .build(UserLoginInfo.class)
                .request();
    }

    public Task<ShopDto> fetchShopDetailWithId(String shopId) {
        return builder().withHttpGetAllowCache()
                .withPath("/v1/shops")
                .withPath("/" + shopId)
                .build(ShopDto.class)
                .request();
    }

    public Task<ShopDto> postNewShop(Map<String, String> params) {
        return builder().withHttpPost()
                .withPath("/v1/shops")
                .withAddParams(params)
                .build(ShopDto.class)
                .request();
    }
}
