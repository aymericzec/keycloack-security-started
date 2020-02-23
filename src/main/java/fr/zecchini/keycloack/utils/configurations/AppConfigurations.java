package fr.zecchini.keycloack.utils.configurations;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AppConfigurations {
    @Inject
    KeycloakConfigurations keycloackConfigurations;

    public KeycloakConfigurations getKeycloackConfigurations() {
        return keycloackConfigurations;
    }
}



