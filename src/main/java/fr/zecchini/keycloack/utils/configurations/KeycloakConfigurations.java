package fr.zecchini.keycloack.utils.configurations;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloakConfigurations {
    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    String authUrl;

    @ConfigProperty(name = "auth-admin-url")
    String authAdminUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String credentialSecret;

    @ConfigProperty(name = "username-manager")
    String usernameManager;

    @ConfigProperty(name = "password-manager")
    String passwordManager;

    @ConfigProperty(name = "token-manager")
    String tokenManager;

    public String getAuthUrl() {
        return authUrl;
    }

    public String getUsernameManager() {
        return usernameManager;
    }

    public String getPasswordManager() {
        return passwordManager;
    }

    public String getAuthAdminUrl() {
        return authAdminUrl;
    }

    public String getCredentialSecret() {
        return credentialSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getTokenManager() {
        return tokenManager;
    }
}

