package fr.zecchini.keycloack.models.user;

import fr.zecchini.keycloack.resources.io.user.input.SignupUserInput;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.List;

public class SignupUser {
    private final String username;
    private final String email;
    private final String password;
    private final String realm;
    private final boolean enable;

    public SignupUser(String username, String email, String password, String realm, boolean enable) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.realm = realm;
        this.enable = enable;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRealm() {
        return realm;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEnable() {
        return enable;
    }

    public static SignupUser create(SignupUserInput signupUserInput) {
        return new SignupUser(signupUserInput.username, signupUserInput.email, signupUserInput.password, signupUserInput.realm, signupUserInput.enable);
    }
}
