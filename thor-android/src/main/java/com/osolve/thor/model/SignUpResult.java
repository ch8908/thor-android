package com.osolve.thor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kros on 7/30/14.
 */
public class SignUpResult {
    private final String userId;
    private final String name;
    private final String autoToken;

    @JsonCreator
    public SignUpResult(@JsonProperty("id") final String userId,
                        @JsonProperty("name") final String name,
                        @JsonProperty("authentication_token") final String autoToken) {
        
        this.userId = userId;
        this.name = name;
        this.autoToken = autoToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAutoToken() {
        return autoToken;
    }

    @Override
    public String toString() {
        return "SignUpResult{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", autoToken='" + autoToken + '\'' +
                '}';
    }
}
