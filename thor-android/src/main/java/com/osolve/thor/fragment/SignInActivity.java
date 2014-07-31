package com.osolve.thor.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragmentActivity;
import com.osolve.thor.fragment.event.SignInSuccessEvent;
import com.osolve.thor.fragment.event.SignUpEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by Kros on 7/28/14.
 */
public class SignInActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, new SignInFragment());
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onSignUpEvent(final SignUpEvent event) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, new SignUpFragment(), SignUpFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(SignInActivity.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Subscribe
    public void onSignInSuccess(final SignInSuccessEvent event) {
        setResult(RESULT_OK);
        finish();
    }
}

