package com.osolve.thor.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Kros on 7/22/14.
 */
public class VolleyEnv {
    private static int calculateConservativeImageCacheByteSize(final Application application) {
        final int memClass = ((ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        return 1024 * 1024 * memClass * 1 / 9;
    }

    private final RequestQueue requestQueue;

    private final ImageLoader imageLoader;

    public VolleyEnv(final Application application) {
        this(application, calculateConservativeImageCacheByteSize(application));
    }

    public VolleyEnv(final Application application, int maxImageCacheByteSize) {
        requestQueue = Volley.newRequestQueue(application);
        imageLoader = new ImageLoader(requestQueue, new ImageLruCache(maxImageCacheByteSize));
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
