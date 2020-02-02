package fr.zecchini.keycloack.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("expires_in")
    public int expire;

    @JsonProperty("refresh_expires_in")
    public int refreshExpire;

    @JsonProperty("token_type")
    public String type;

    @JsonProperty("not-before-policy")
    public int notBeforePolicy;

    @JsonProperty("session_state")
    public String session;

    @JsonProperty("scope")
    public String scope;
}
