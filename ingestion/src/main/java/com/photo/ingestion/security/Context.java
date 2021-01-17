package com.photo.ingestion.security;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.HashMap;
import java.util.Map;

public class Context extends DefaultOAuth2ClientContext {
    private static final long serialVersionUID = 914967629530462926L;
    private AccessToken accessToken;
    private AccessTokenRequest accessTokenRequest;
    private Map<String, Object> state;

    public Context() {
        this((AccessTokenRequest)(new DefaultAccessTokenRequest()));
    }

    public Context(AccessTokenRequest accessTokenRequest) {
        this.state = new HashMap();
        this.accessTokenRequest = accessTokenRequest;
    }

    public Context(AccessToken accessToken) {
        this.state = new HashMap();
        this.accessToken = accessToken;
        this.accessTokenRequest = new DefaultAccessTokenRequest();
    }

    public AccessToken getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = (AccessToken) accessToken;
        this.accessTokenRequest.setExistingToken(accessToken);
    }

    public AccessTokenRequest getAccessTokenRequest() {
        return this.accessTokenRequest;
    }

    public void setPreservedState(String stateKey, Object preservedState) {
        this.state.put(stateKey, preservedState);
    }

    public Object removePreservedState(String stateKey) {
        return this.state.remove(stateKey);
    }
}
