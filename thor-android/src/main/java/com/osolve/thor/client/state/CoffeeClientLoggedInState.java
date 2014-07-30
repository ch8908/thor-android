package com.osolve.thor.client.state;

import com.osolve.thor.state.AbstractState;
import com.squareup.otto.Subscribe;

/**
 * Created by Kros on 7/29/14.
 */
public class CoffeeClientLoggedInState extends AbstractState {
    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    @Subscribe
    public void onTrigger(CoffeeShopClientTrigger trigger) {

    }
}
