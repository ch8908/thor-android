package com.osolve.thor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragment;
import com.osolve.thor.fragment.event.SignUpEvent;

/**
 * Created by Kros on 7/30/14.
 */
public class SignInFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        rootView.findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean().postBusEvent(new SignUpEvent());
            }
        });

        return rootView;
    }
}
