package com.osolve.thor.app;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Kros on 7/22/14.
 */
public class ImageLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public ImageLruCache(final int maxByteSize) {
        super(maxByteSize);
    }

    @Override
    public Bitmap getBitmap(final String url) {
        return get(url);
    }

    @Override
    public void putBitmap(final String url, final Bitmap bitmap) {
        put(url, bitmap);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }
}