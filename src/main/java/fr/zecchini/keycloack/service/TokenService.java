package fr.zecchini.keycloack.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.quarkus.oidc.AccessTokenCredential;
import io.quarkus.security.identity.SecurityIdentity;

import javax.inject.Singleton;

@Singleton
public class TokenService {
    public DecodedJWT deserializeToken (String token) {
        return JWT.decode(token);
    }

    public String getUserId (String accessToken) {
        DecodedJWT jwt = this.deserializeToken(accessToken);
        return jwt.getClaim("sub").asString();
    }
    public String getUserId (SecurityIdentity identity) {
        DecodedJWT jwt = this.deserializeToken(getTokenWithIdentity(identity));
        return jwt.getClaim("sub").asString();
    }

    public boolean isSameUser (String token1, String token2) {
        return this.getUserId(token1).equals(this.getUserId(token2));
    }

    public String getTokenWithIdentity (SecurityIdentity identity) {
        return identity.getCredential(AccessTokenCredential.class).getToken();
    }
}
