package com.osolve.thor.app;

import android.content.Context;

/**
 * Created by Kros on 7/22/14.
 */
public abstract class BaseDaemon implements IDaemon {
    private final Bean bean;

    private final Context context;

    public BaseDaemon(final Bean bean) {
        this.bean = bean;
        context = bean.context;
    }

    protected Bean bean() {
        return bean;
    }

    public Context context() {
        return context;
    }
}
