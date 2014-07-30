package com.osolve.thor.app;

import android.os.Handler;

import com.osolve.thor.state.AbstractState;

/**
 * Created by Kros on 7/29/14.
 */
public abstract class BaseState extends AbstractState {
    protected final static String TAG = BaseState.class.getSimpleName();

    protected final Bean bean() {
        return ThorApplication.getBean();
    }

    private Handler mainHandler;

    protected BaseState() {
        this.mainHandler = new Handler();
    }

}
