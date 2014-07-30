package com.osolve.thor.client.state;

import com.osolve.thor.client.model.RegisterInfo;
import com.osolve.thor.client.model.SignInInfo;
import com.osolve.thor.state.IStateMachineTrigger;

/**
 * Created by Kros on 7/29/14.
 */
public class CoffeeShopClientTrigger implements IStateMachineTrigger {
    public boolean loggedIn;
    public boolean logout;
    public SignInInfo signInInfo;
    public RegisterInfo registerInfo;
}
