package com.osolve.thor.state;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class StateMachine<T extends AbstractState> {

    private final static String TAG = StateMachine.class.getSimpleName();
    final Bus bus;
    T currentState;

    public StateMachine(final T currentState) {
        bus = new Bus();
        bus.register(this);
        this.currentState = currentState;
    }

    private void changeState(final T nextState) {
        bus.unregister(currentState);
        currentState.setBus(null);
        currentState.onExit();

        currentState = nextState;
        currentState.setBus(bus);
        bus.register(currentState);
        currentState.onEnter();
        Log.d(TAG, "enter state:" + currentState.getClass().getSimpleName());
    }

    public T getCurrentState() {
        return currentState;
    }

    public void initialize() {
        currentState.setBus(bus);
        bus.register(currentState);
        currentState.onEnter();
        Log.d(TAG, "enter state:" + currentState.getClass().getSimpleName());
    }

    @Subscribe
    public void onTransit(final T nextState) {
        changeState(nextState);
    }

    public void terminate() {
        bus.unregister(currentState);
        currentState.setBus(null);
        currentState.onExit();
        bus.unregister(this);
    }

    public void trigger(final IStateMachineTrigger event) {
        bus.post(event);
    }

}
