package fr.zecchini.keycloack.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.zecchini.keycloack.models.user.SigninUser;
import fr.zecchini.keycloack.models.user.SignupResponse;
import fr.zecchini.keycloack.models.user.SignupUser;
import fr.zecchini.keycloack.models.user.User;
import fr.zecchini.keycloack.utils.configurations.AppConfigurations;
import fr.zecchini.keycloack.utils.configurations.KeycloakConfigurations;
import io.quarkus.security.identity.SecurityIdentity;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import fr.zecchini.keycloack.models.token.Token;
import org.keycloak.representations.idm.UserRepresentation;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

@ApplicationScoped
public class KeycloakService {

    private ObjectMapper mapper;
    private Token tokenManager;
    private KeycloakConfigurations keycloakConfigurations;

    @Inject
    SecurityIdentity identity;
    @Inject
    TokenService tokenService;

    public KeycloakService(AppConfigurations appConfigurations) {
        this.keycloakConfigurations = appConfigurations.getKeycloackConfigurations();
    }

    @PostConstruct
    void init() {
        this.mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.tokenManager = this.refreshToken(this.keycloakConfigurations.getTokenManager());
    }

    public SignupResponse signup (SignupUser signupUser) {
        UserRepresentation userRepresentation = User.create(signupUser).userRepresentation();
        try {
            String o = this.mapper.writeValueAsString(userRepresentation);
            String accessToken = this.tokenManager.isValid() ? this.tokenManager.accessToken : this.refreshToken(this.tokenManager.refreshToken).accessToken;
            HttpResponse<JsonNode> response =  Unirest.post(this.keycloakConfigurations.getAuthAdminUrl() + "/users")
                    .header("content-type", "application/json")
                    .body(o)
                    .header("Authorization", "Bearer " + accessToken)
                    .asJson();

            if (!response.isSuccess())
                throw new WebApplicationException(response.getBody().toPrettyString(), response.getStatus());

            return new SignupResponse(response.getBody().toPrettyString());
        } catch (JsonProcessingException e) {
            throw new WebApplicationException(e);
        }
    }

    public Token signin (SigninUser signinUser) {
        HttpResponse<JsonNode> response =  Unirest.post( this.keycloakConfigurations.getAuthUrl() + "/protocol/openid-connect/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("grant_type", "password")
                .field("username", signinUser.getUsername())
                .field("password",signinUser.getPassword())
                .field("client_id", keycloakConfigurations.getClientId())
                .field("client_secret", keycloakConfigurations.getCredentialSecret())
                .field("scope", signinUser.isOffline() ? "openid info offline_access" : "")
                .asJson();
        if (!response.isSuccess())
            throw new WebApplicationException(response.getBody().toPrettyString(), response.getStatus());

        try {
            return  this.mapper.readValue(response.getBody().toString(), Token.class);
        } catch (JsonProcessingException e) {
            throw new WebApplicationException(e, response.getStatus());
        }
    }

    public void logout(String refreshToken) {
        String token = this.tokenService.getTokenWithIdentity(this.identity);
        if (!this.tokenService.isSameUser(token, refreshToken))
            throw new WebApplicationException(401);

        HttpResponse<JsonNode> response = Unirest
                .post(this.keycloakConfigurations.getAuthUrl() + "/protocol/openid-connect/logout")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("grant_type", "refresh_token")
                .field("refresh_token", refreshToken)
                .field("client_id", this.keycloakConfigurations.getClientId())
                .field("client_secret", this.keycloakConfigurations.getCredentialSecret())
                .asJson();
        if (!response.isSuccess())
            throw new WebApplicationException(response.getBody().toPrettyString(), response.getStatus());
    }

    public UserRepresentation user () {
        String accessToken = this.tokenManager.isValid() ? this.tokenManager.accessToken : this.refreshToken(this.tokenManager.refreshToken).accessToken;
        HttpResponse<JsonNode> response = Unirest
                .get(this.keycloakConfigurations.getAuthAdminUrl() + "/users/" + this.tokenService.getUserId(identity))
                .header("Authorization", "Bearer " + accessToken)
                .asJson();
        if (!response.isSuccess())
            throw new WebApplicationException(response.getBody().toPrettyString(), response.getStatus());

        try {
            return  this.mapper.readValue(response.getBody().toString(), UserRepresentation.class);
        } catch (JsonProcessingException e) {
            throw new WebApplicationException(e, response.getStatus());
        }
    }

    public Token refreshToken (String refreshToken) {
        HttpResponse<JsonNode> response =  Unirest.post(this.keycloakConfigurations.getAuthUrl() + "/protocol/openid-connect/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("grant_type", "refresh_token")
                .field("refresh_token", refreshToken)
                .field("client_id", this.keycloakConfigurations.getClientId())
                .field("client_secret", this.keycloakConfigurations.getCredentialSecret())
                .asJson();

        if (!response.isSuccess())
            throw new WebApplicationException(response.getBody().toPrettyString(), response.getStatus());

        try {
            return  this.mapper.readValue(response.getBody().toString(), Token.class);
        } catch (JsonProcessingException e) {
            throw new WebApplicationException(e, response.getStatus());
        }
    }

    public void deleteUser() {
        String accessToken = this.tokenManager.isValid() ? this.tokenManager.accessToken : this.refreshToken(this.tokenManager.refreshToken).accessToken;
        HttpResponse<JsonNode> response = Unirest.delete(this.keycloakConfigurations.getAuthAdminUrl() + "/users/" + this.tokenService.getUserId(this.identity))
                .header("Authorization", "Bearer " + accessToken)
               .asJson();

    }
}
