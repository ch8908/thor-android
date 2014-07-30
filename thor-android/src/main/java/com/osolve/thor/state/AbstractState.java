package com.osolve.thor.state;

import com.squareup.otto.Bus;

public abstract class AbstractState implements IState {

    private Bus bus;

    protected void setBus(final Bus bus) {
        this.bus = bus;
    }

    protected void transitTo(final AbstractState state) {
        bus.post(state);
    }
}
