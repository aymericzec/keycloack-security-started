package fr.zecchini.keycloack.models.token;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {
    @JsonProperty("id_token")
    public String id;

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    public int expire;

    @JsonIgnoreProperties
    public LocalDateTime localExpire;

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

    public Token ( @JsonProperty("expires_in") int expire) {
        this.expire = expire;
        this.localExpire = LocalDateTime.now().plusSeconds(this.expire);
    }

    public boolean isValid () {
        return LocalDateTime.now().isBefore(localExpire);
    }
}
