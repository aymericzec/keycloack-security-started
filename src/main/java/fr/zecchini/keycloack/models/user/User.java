package fr.zecchini.keycloack.models.user;

import fr.zecchini.keycloack.resources.io.user.input.SignupUserInput;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private final String email;
    private final String password;
    private final String realm;
    private final boolean enable;

    public User(String username, String email, String password, String realm, boolean enable) {
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

    public UserRepresentation userRepresentation () {
        List<CredentialRepresentation> credentialRepresentations = new ArrayList<>();

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(this.password);
        credentialRepresentation.setType("password");
        credentialRepresentations.add(credentialRepresentation);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(this.username);
        userRepresentation.setEmail(this.email);
        userRepresentation.setEnabled(this.enable);
        userRepresentation.setCredentials(credentialRepresentations);

        return userRepresentation;
    }

    public static User create(SignupUser signupUser) {
        return new User(signupUser.getUsername(), signupUser.getEmail(), signupUser.getPassword(), signupUser.getRealm(), signupUser.isEnable());
    }
}
