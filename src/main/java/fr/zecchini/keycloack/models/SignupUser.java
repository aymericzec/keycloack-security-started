package fr.zecchini.keycloack.models;

public class SignupUser {
    private final String username;
    private final String email;
    private final String password;
    private boolean enable;

    public SignupUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
