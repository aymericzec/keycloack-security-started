package fr.zecchini.keycloack.models.user;

import fr.zecchini.keycloack.resources.io.user.input.SigninUserInput;

public class SigninUser {
    private final String username;
    private final String  password;
    private final boolean offline;

    public SigninUser(String username, String password, boolean offline) {
        this.username = username;
        this.password = password;
        this.offline = offline;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOffline() {
        return this.offline;
    }

    public static SigninUser create(SigninUserInput signinUserInput) {
        return new SigninUser(signinUserInput.username, signinUserInput.password, signinUserInput.offline);
    }
}
