package com.osolve.thor.app;

import android.app.Application;

/**
 * Created by Kros on 7/29/14.
 */
public class Pref extends AbstractPref {

    protected Pref(Application application) {
        super(application);
    }

    private final StringPref authToken = new StringPref("AUTH_TOKEN");

    public String findAuthToken() {
        return authToken.get();
    }

    public void removeAuthToken() {
        authToken.remove();
    }
}
