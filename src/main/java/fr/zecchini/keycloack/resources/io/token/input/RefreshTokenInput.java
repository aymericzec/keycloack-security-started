package fr.zecchini.keycloack.resources.io.token.input;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RefreshTokenInput {
    @JsonProperty("refresh_token")
    public String refreshToken;
}
