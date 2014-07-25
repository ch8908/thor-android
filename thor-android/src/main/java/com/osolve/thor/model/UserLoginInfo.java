package com.osolve.thor.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Kros on 7/22/14.
 */
public class UserLoginInfo {
    private final long userId;
    private final String name;
    private final String authToken;

    @JsonCreator
    public UserLoginInfo(@JsonProperty("id") final long userId,
                         @JsonProperty("name") final String name,
                         @JsonProperty("authentication_token") final String authToken) {
        this.userId = userId;
        this.name = name;
        this.authToken = authToken;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String toString() {
        return "UserLoginInfo{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
