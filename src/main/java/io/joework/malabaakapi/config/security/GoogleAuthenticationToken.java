package io.joework.malabaakapi.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;


public class GoogleAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;

    public GoogleAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        this.setAuthenticated(true);
    }
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
