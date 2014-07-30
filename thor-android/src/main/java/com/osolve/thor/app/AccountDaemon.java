package com.osolve.thor.app;

import com.osolve.thor.client.ApiClient;
import com.osolve.thor.client.state.CoffeeClientIdleState;
import com.osolve.thor.client.state.CoffeeShopClientTrigger;
import com.osolve.thor.model.SignUpResult;
import com.osolve.thor.model.UserLoginInfo;
import com.osolve.thor.state.AbstractState;
import com.osolve.thor.state.StateMachine;

import bolts.Continuation;
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

    public Task<UserLoginInfo> signIn(final String email, final String password) {
        return apiClient.login(email, password).onSuccess(new Continuation<UserLoginInfo, UserLoginInfo>() {
            @Override
            public UserLoginInfo then(Task<UserLoginInfo> task) throws Exception {
                UserLoginInfo result = task.getResult();
                loggedIn(result.getAuthToken(), result.getName());
                return task.getResult();
            }
        });
    }

    private void loggedIn(final String authToken, final String name) {
        pref.saveAuthToken(authToken);
        pref.userName.set(name);
        triggerLoggedIn();
    }

    public Task<SignUpResult> signUp(final String email, final String password) {
        return apiClient.postSignUp(email, password, password);
    }

    private void triggerLoggedIn() {
        CoffeeShopClientTrigger trigger = new CoffeeShopClientTrigger();
        trigger.loggedIn = true;
        stateMachine.trigger(trigger);
    }
}
