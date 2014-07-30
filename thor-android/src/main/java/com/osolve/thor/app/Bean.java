package com.osolve.thor.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.osolve.thor.client.ApiClient;
import com.squareup.otto.Bus;

/**
 * Created by Kros on 7/22/14.
 */
public class Bean {
    final Application context;
    private final VolleyEnv volleyEnv;
    private final Handler mainHandler;

    public final Bus bus;
    public final CoffeeShopDaemon coffeeShopDaemon;
    public final AccountDaemon accountDaemon;

    public Bean(final Application application) {
        bus = new Bus();
        mainHandler = new Handler(Looper.getMainLooper());

        context = application;
        volleyEnv = new VolleyEnv(application);

        ApiClient apiClient = new ApiClient(volleyEnv.getRequestQueue());
        Pref pref = new Pref(application);

        coffeeShopDaemon = new CoffeeShopDaemon(this, apiClient);
        accountDaemon = new AccountDaemon(this, apiClient, pref);
    }

    public void postBusEvent(final Object event) {
        // 1. event bus must run in main looper
        // 2. always lag a event prevent side effect
        // 3. some activity already rely on such behavior, don't alter it!
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                bus.post(event);
            }
        });
    }

    public void registerInEventBus(final Object host) {
        bus.register(host);
    }

    public void unregisterFromEventBus(final Object host) {
        bus.unregister(host);
    }

    public void terminate() {
        coffeeShopDaemon.terminate();
        accountDaemon.terminate();
    }
}
