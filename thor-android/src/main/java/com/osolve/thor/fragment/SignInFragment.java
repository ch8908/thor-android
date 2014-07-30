package com.osolve.thor.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.osolve.json.ClientJsonMapper;
import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragment;
import com.osolve.thor.fragment.event.SignUpEvent;
import com.osolve.thor.model.UserLoginInfo;

import java.io.IOException;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Kros on 7/30/14.
 */
public class SignInFragment extends BaseFragment {

    private static final String TAG = SignInFragment.class.getSimpleName();

    private View rootView;
    private TextView errorMessageTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        errorMessageTextView = (TextView) rootView.findViewById(R.id.errorMessageTextView);

        rootView.findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean().postBusEvent(new SignUpEvent());
            }
        });

        rootView.findViewById(R.id.signInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return rootView;
    }

    private void signIn() {
        EditText emailEditText = (EditText) rootView.findViewById(R.id.emailEditText);
        EditText passwordEditText = (EditText) rootView.findViewById(R.id.passwordEditText);
        final String password = passwordEditText.getText().toString();
        final String email = emailEditText.getText().toString();

        if (TextUtils.isEmpty(email.trim())) {
            errorMessageTextView.setText(getActivity().getResources().getText(R.string.signIn_email_is_empty));
            return;
        }

        if (TextUtils.isEmpty(password.trim())) {
            errorMessageTextView.setText(getActivity().getResources().getText(R.string.signIn_password_is_empty));
            return;
        }

        bean().accountDaemon.signIn(email, password).continueWith(new Continuation<UserLoginInfo, Object>() {
            @Override
            public Object then(Task<UserLoginInfo> task) throws Exception {

                if (task.isFaulted()) {
                    VolleyError error = (VolleyError) task.getError();
                    if (error.networkResponse == null || error.networkResponse.data == null) {
                        errorMessageTextView.setText(getActivity().getResources().getText(R.string.signIn_sign_in_failed));
                    }
                    try {
                        ClientJsonMapper mapper = ClientJsonMapper.getInstance();
                        SignInError signUpError = mapper.readValue(error.networkResponse.data, SignInError.class);
                        StringBuilder errorMessage = new StringBuilder();
                        if (signUpError.getErrorMessage() != null) {
                            errorMessageTextView.setText(errorMessage.toString());
                        }
                        Log.d(TAG, "Sign In error:" + signUpError + " json:" + new String(error.networkResponse.data));
                    } catch (IOException e) {
                        Log.d(TAG, "Sign In Json decode error:" + e + " json:" + new String(error.networkResponse.data));
                        e.printStackTrace();
                    }
                    return null;
                }

                Log.d(TAG, "Sign In Successfully");

                return null;
            }
        });
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SignInError {
        private final String errorMessage;

        @JsonCreator
        private SignInError(@JsonProperty("error") final String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        @Override
        public String toString() {
            return "SignInError{" +
                    "errorMessage='" + errorMessage + '\'' +
                    '}';
        }
    }
}
