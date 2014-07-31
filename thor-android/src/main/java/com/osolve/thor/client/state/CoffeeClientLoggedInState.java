package com.osolve.thor.client.state;

import com.osolve.thor.app.BaseState;
import com.osolve.thor.fragment.event.SignInSuccessEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by Kros on 7/29/14.
 */
public class CoffeeClientLoggedInState extends BaseState {

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    @Subscribe
    public void onTrigger(CoffeeShopClientTrigger trigger) {
        if (trigger.isLoggedIn) {
            bean().postBusEvent(new SignInSuccessEvent());
        } else if (trigger.logOut) {
            transitTo(new CoffeeClientIdleState());
        }
    }
}
