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
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.osolve.json.ClientJsonMapper;
import com.osolve.thor.R;
import com.osolve.thor.app.BaseFragment;
import com.osolve.thor.model.SignUpResult;

import java.io.IOException;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Kros on 7/30/14.
 */
public class SignUpFragment extends BaseFragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private View rootView;
    private TextView errorMesssageTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        errorMesssageTextView = (TextView) rootView.findViewById(R.id.signUpErrorMessageTextView);

        rootView.findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        return rootView;
    }

    private void signUp() {
        final EditText emailEditText = (EditText) rootView.findViewById(R.id.emailEditText);
        EditText passwordEditText = (EditText) rootView.findViewById(R.id.passwordEditText);
        EditText confirmPasswordEditText = (EditText) rootView.findViewById(R.id.confirmPasswordEditText);
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!password.equals(confirmPassword)) {
            errorMesssageTextView.setText(R.string.signUp_password_is_not_the_same);
            return;
        }

        bean().accountDaemon.signUp(emailEditText.getText().toString(), password).continueWith(new Continuation<SignUpResult, Object>() {
            @Override
            public Object then(Task<SignUpResult> task) throws Exception {
                if (task.isFaulted()) {
                    VolleyError error = (VolleyError) task.getError();
                    try {
                        ClientJsonMapper mapper = ClientJsonMapper.getInstance();
                        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
                        mapper.enable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS);
                        SignUpError signUpError = mapper.readValue(error.networkResponse.data, SignUpError.class);
                        StringBuilder errorMessage = new StringBuilder();
                        if (signUpError.email != null) {
                            errorMessage.append(TextUtils.join("\n", signUpError.email)).append("\n");
                        }
                        if (signUpError.password != null) {
                            errorMessage.append(TextUtils.join("\n", signUpError.password));
                        }
                        errorMesssageTextView.setText(errorMessage.toString());
                        Log.d(TAG, "Sign Up error:" + signUpError + " json:" + new String(error.networkResponse.data));
                    } catch (IOException e) {
                        Log.d(TAG, "Sign Up error mapped failed");
                    }
                    return null;
                }
                Log.d(TAG, "Sign up successfully");
                return null;
            }
        });
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonRootName("error")
    public static class SignUpError {
        private final List<String> email;
        private final List<String> password;

        @JsonCreator
        private SignUpError(@JsonProperty("email") @JsonSerialize final List<String> email,
                            @JsonProperty("password") @JsonSerialize final List<String> password) {
            this.email = email;
            this.password = password;
        }

        public List<String> getEmail() {
            return email;
        }

        public List<String> getPassword() {
            return password;
        }

        @Override
        public String toString() {
            return "SignUpError{" +
                    "email=" + email +
                    ", password=" + password +
                    '}';
        }
    }
}
