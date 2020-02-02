package fr.zecchini.keycloack.resources.inputs;

import com.fasterxml.jackson.annotation.JsonProperty;


public class RefreshTokenInput {
    @JsonProperty("refresh_token")
    public String refreshToken;
}
