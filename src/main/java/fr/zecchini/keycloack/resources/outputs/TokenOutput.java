package fr.zecchini.keycloack.resources.outputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.zecchini.keycloack.models.Token;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenOutput {
    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("expires_in")
    public int expire;

    @JsonProperty("refresh_expires_in")
    public int refreshExpire;

    public TokenOutput(String accessToken, String refreshToken, int expire, int refreshExpire) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expire = expire;
        this.refreshExpire = refreshExpire;
    }

    public static TokenOutput create (Token token) {
        return new TokenOutput(token.accessToken, token.refreshToken, token.expire, token.refreshExpire);
    }
}
