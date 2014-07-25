package com.osolve.thor.client;

/**
 * Created by Kros on 7/22/14.
 */
public enum ServerType {
    STAGE("geekcoffee-staging.roachking.net:80/api"), PRODUCTION("");
    private String apiNode;

    private ServerType(final String apiNode) {
        this.apiNode = apiNode;
    }

    public String getFullUrlPrefix() {
        switch (this) {
            case STAGE:
                return "http://" + apiNode;
            case PRODUCTION:
                return "https://" + apiNode;
            default:
                return "";
        }
    }
}
