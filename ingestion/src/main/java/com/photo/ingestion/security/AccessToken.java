package com.photo.ingestion.security;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.Serializable;
import java.util.*;

public class AccessToken implements Serializable, OAuth2AccessToken {
    private static final long serialVersionUID = 1L;
    private String value;
    private Date expiration;
    private String tokenType;
    private OAuth2RefreshToken refreshToken;
    private Set<String> scope;
    private Map<String, Object> additionalInformation;

    public AccessToken(String value) {
        this.tokenType = "Bearer".toLowerCase();
        this.additionalInformation = Collections.emptyMap();
        this.value = value;
    }

    private AccessToken() {
        this((String)null);
    }

    public AccessToken(OAuth2AccessToken accessToken) {
        this(accessToken.getValue());
        this.setAdditionalInformation(accessToken.getAdditionalInformation());
        this.setRefreshToken(accessToken.getRefreshToken());
        this.setExpiration(accessToken.getExpiration());
        this.setScope(accessToken.getScope());
        this.setTokenType(accessToken.getTokenType());
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public int getExpiresIn() {
        return this.expiration != null ? Long.valueOf((this.expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue() : 0;
    }

    protected void setExpiresIn(int delta) {
        this.setExpiration(new Date(System.currentTimeMillis() + (long)delta));
    }

    public Date getExpiration() {
        return this.expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean isExpired() {
        return this.expiration != null && this.expiration.before(new Date());
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public OAuth2RefreshToken getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(OAuth2RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Set<String> getScope() {
        return this.scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    public boolean equals(Object obj) {
        return obj != null && this.toString().equals(obj.toString());
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        return String.valueOf(this.getValue());
    }

    public static OAuth2AccessToken valueOf(Map<String, String> tokenParams) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken((String)tokenParams.get("token"));
        if (tokenParams.containsKey("expires_in")) {
            long expiration = 0L;

            try {
                expiration = Long.parseLong(String.valueOf(tokenParams.get("expires_in")));
            } catch (NumberFormatException var5) {
                ;
            }

            token.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L));
        }

        if (tokenParams.containsKey("refresh_token")) {
            String refresh = (String)tokenParams.get("refresh_token");
            DefaultOAuth2RefreshToken refreshToken = new DefaultOAuth2RefreshToken(refresh);
            token.setRefreshToken(refreshToken);
        }

        if (tokenParams.containsKey("scope")) {
            Set<String> scope = new TreeSet();
            StringTokenizer tokenizer = new StringTokenizer((String)tokenParams.get("scope"), " ,");

            while(tokenizer.hasMoreTokens()) {
                scope.add(tokenizer.nextToken());
            }

            token.setScope(scope);
        }

        if (tokenParams.containsKey("token_type")) {
            token.setTokenType((String)tokenParams.get("token_type"));
        }

        return token;
    }

    public Map<String, Object> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = new LinkedHashMap(additionalInformation);
    }
}
