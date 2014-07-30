package com.osolve.thor.app;

import com.osolve.thor.client.ApiClient;
import com.osolve.thor.client.state.CoffeeClientIdleState;
import com.osolve.thor.client.state.CoffeeShopClientTrigger;
import com.osolve.thor.model.SignUpResult;
import com.osolve.thor.model.UserLoginInfo;
import com.osolve.thor.state.AbstractState;
import com.osolve.thor.state.StateMachine;

import bolts.Task;

/**
 * Created by Kros on 7/29/14.
 */
public class AccountDaemon extends BaseDaemon {

    private final StateMachine<AbstractState> stateMachine;
    private ApiClient apiClient;
    private final Pref pref;

    @Override
    public void terminate() {
        stateMachine.terminate();
    }

    public AccountDaemon(final Bean bean,
                         final ApiClient apiClient,
                         final Pref pref) {
        super(bean);
        this.apiClient = apiClient;
        this.pref = pref;

        stateMachine = new StateMachine<AbstractState>(new CoffeeClientIdleState());
        stateMachine.initialize();

        if (pref.findAuthToken() != null) {
            triggerLoggedIn();
        }
    }

    private void triggerLoggedIn() {
        CoffeeShopClientTrigger trigger = new CoffeeShopClientTrigger();
        trigger.loggedIn = true;
        stateMachine.trigger(trigger);
    }

    public Task<UserLoginInfo> signIn(final String email, final String password) {
        return apiClient.login(email, password);
    }

    public Task<SignUpResult> signUp(final String email, final String password) {
        return apiClient.postSignUp(email, password, password);
    }

}
