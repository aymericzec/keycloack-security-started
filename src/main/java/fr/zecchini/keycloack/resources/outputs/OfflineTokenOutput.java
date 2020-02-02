package fr.zecchini.keycloack.resources.outputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.zecchini.keycloack.models.OfflineToken;
import fr.zecchini.keycloack.models.Token;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OfflineTokenOutput {
    @JsonProperty("id_token")
    public String id;

    @JsonProperty("access_token")
    public String accessToken;

    @JsonProperty("refresh_token")
    public String refreshToken;

    @JsonProperty("expires_in")
    public int expire;

    @JsonProperty("refresh_expires_in")
    public int refreshExpire;

    public OfflineTokenOutput(String accessToken, String refreshToken, int expire, int refreshExpire) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expire = expire;
        this.refreshExpire = refreshExpire;
    }

    public static OfflineTokenOutput create (OfflineToken token) {
        return new OfflineTokenOutput(token.accessToken, token.refreshToken, token.expire, token.refreshExpire);
    }
}
