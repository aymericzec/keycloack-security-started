package fr.zecchini.keycloack.utils.environment;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "quarkus.oidc")
public class KeycloackConfigurations {
    @ConfigProperty(name = "auth-server-url")
    public String url;

    @ConfigProperty(name = "client-id")
    public String clientId;

    @ConfigProperty(name = "credentials.secret")
    public String credentialSecret;
}
