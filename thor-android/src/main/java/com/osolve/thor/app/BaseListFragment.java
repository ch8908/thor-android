package com.osolve.thor.app;

import android.support.v4.app.ListFragment;

/**
 * Created by Kros on 7/28/14.
 */
public class BaseListFragment extends ListFragment {
    protected final Bean bean() {
        return ThorApplication.getBean();
    }

    public BaseListFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        bean().registerInEventBus(this);
    }

    @Override
    public void onPause() {
        bean().unregisterFromEventBus(this);
        super.onPause();
    }
}
