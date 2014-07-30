package com.osolve.thor.client.state;

import com.osolve.thor.app.BaseState;
import com.squareup.otto.Subscribe;

/**
 * Created by Kros on 7/29/14.
 */
public class CoffeeClientIdleState extends BaseState {

    public CoffeeClientIdleState() {
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    @Subscribe
    public void onTrigger(CoffeeShopClientTrigger trigger) {

        if (trigger.loggedIn) {
            transitTo(new CoffeeClientLoggedInState());
        }
    }
}
