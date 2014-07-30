package com.osolve.thor.app;

import android.app.Application;

/**
 * Created by Kros on 7/22/14.
 */
public class ThorApplication extends Application {
    private static Bean bean;

    static Bean getBean() {
        return bean;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bean = new Bean(this);
    }

    @Override
    public void onTerminate() {
        bean.terminate();
        super.onTerminate();
    }
}
