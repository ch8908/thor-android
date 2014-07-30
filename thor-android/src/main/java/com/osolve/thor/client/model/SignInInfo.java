package com.osolve.thor.client.model;

/**
 * Created by Kros on 7/29/14.
 */
public class SignInInfo {
    private final String email;
    private final String password;

    public SignInInfo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SignInInfo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
