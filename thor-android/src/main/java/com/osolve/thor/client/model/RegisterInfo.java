package com.osolve.thor.client.model;

/**
 * Created by Kros on 7/29/14.
 */
public class RegisterInfo {
    private final String email;
    private final String password;
    private final String confirmPassword;

    public RegisterInfo(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }

}
