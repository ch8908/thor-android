package com.osolve.thor.app;

import android.support.v4.app.Fragment;

/**
 * Created by Kros on 7/25/14.
 */
public class BaseFragment extends Fragment {
    protected final Bean bean() {
        return ThorApplication.getBean();
    }

    public BaseFragment() {
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
