package fr.zecchini.keycloack.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fr.zecchini.keycloack.models.OfflineToken;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import fr.zecchini.keycloack.models.Token;
import fr.zecchini.keycloack.resources.inputs.SignupUserInput;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class KeycloackService {

    private final ObjectMapper mapper;

    public KeycloackService(){
        this.mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL.NON_NULL);
    }

    public Token login (String username, String password) throws WebApplicationException {
        HttpResponse<JsonNode> response =  Unirest.post("http://192.168.1.42:8180/auth/realms/equido/protocol/openid-connect/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("username", username)
                .field("password", password)
                .field("grant_type", "password")
                .field("client_id", "backend-service")
                .field("client_secret", "abd4dbf7-9a80-40bd-9c9e-1ecc4eaca7f6")
                .asJson();
        if (response.getStatus() != 200)
            throw new WebApplicationException(response.getStatus());

        try {
            Token token = this.mapper.readValue(response.getBody().toString(), Token.class);
            return  token;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WebApplicationException(response.getStatus());
        }
    }

    public OfflineToken loginOffline (String username, String password) throws WebApplicationException {
        HttpResponse<JsonNode> response =  Unirest.post("http://192.168.1.42:8180/auth/realms/equido/protocol/openid-connect/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("username", username)
                .field("password", password)
                .field("grant_type", "password")
                .field("client_id", "backend-service")
                .field("scope", "openid info offline_access")
                .field("client_secret", "abd4dbf7-9a80-40bd-9c9e-1ecc4eaca7f6")
                .asJson();

        if (response.getStatus() != 200)
            throw new WebApplicationException(response.getStatus());

        try {
            OfflineToken offlineToken = this.mapper.readValue(response.getBody().toString(), OfflineToken.class);
            return  offlineToken;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WebApplicationException(response.getStatus());
        }
    }

    public Token refreshToken (String refreshToken) throws WebApplicationException {
        HttpResponse<JsonNode> response =  Unirest.post("http://192.168.1.42:8180/auth/realms/equido/protocol/openid-connect/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("grant_type", "refresh_token")
                .field("refresh_token", refreshToken)
                .field("client_id", "backend-service")
                .field("client_secret", "abd4dbf7-9a80-40bd-9c9e-1ecc4eaca7f6")
                .asJson();
        if (response.getStatus() != 200)
            throw new WebApplicationException(response.getStatus());

        try {
            Token token = this.mapper.readValue(response.getBody().toString(), Token.class);
            return  token;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WebApplicationException(response.getStatus());
        }
    }


    private UserRepresentation createUserRepresentation (SignupUserInput signupUserInput) {
        List<CredentialRepresentation> credentialRepresentations = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(signupUserInput.password);
        credentialRepresentation.setType("password");
        credentialRepresentations.add(credentialRepresentation);

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(signupUserInput.username);
        userRepresentation.setEmail(signupUserInput.email);
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(credentialRepresentations);
        return userRepresentation;
    }

    public OfflineToken signup (SignupUserInput signupInput) throws WebApplicationException {
        UserRepresentation userRepresentation = createUserRepresentation(signupInput);
        String token = login("user_manager", "manager").accessToken;
        try {
            String o = this.mapper.writeValueAsString(userRepresentation);
            String test = new Gson().toJson(userRepresentation);
            System.out.println(o);
            System.out.println(test);
            HttpResponse<JsonNode> response =  Unirest.post("http://192.168.1.42:8180/auth/admin/realms/equido/users")
                    .header("content-type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .body(o)
                    .asJson();
            if (!response.isSuccess())
                throw new WebApplicationException(response.getStatus() + " " + response.getParsingError().get());

            return this.loginOffline(signupInput.username, signupInput.password);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WebApplicationException();
        }
    }

    public void logout(String refreshToken) {
        HttpResponse<JsonNode> response = Unirest
                .post("http://192.168.1.42:8180/auth/realms/equido/protocol/openid-connect/logout")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("grant_type", "refresh_token")
                .field("refresh_token", refreshToken)
                //.field("token", refreshToken)
                .field("client_id", "backend-service")
                .field("client_secret", "abd4dbf7-9a80-40bd-9c9e-1ecc4eaca7f6")
                .asJson();
        if (!response.isSuccess())
            throw new WebApplicationException(response.getStatus());
    }

    public UserRepresentation user (String id) {
        String token = login("user_manager", "manager").accessToken;
        HttpResponse<JsonNode> response = Unirest
                .get("http://192.168.1.42:8180/auth/admin/realms/equido/users/" + id)
                .header("Authorization", "Bearer " + token)
                .asJson();
        if (!response.isSuccess())
            throw new WebApplicationException(response.getStatus());

        try {
            UserRepresentation userRepresentation = this.mapper.readValue(response.getBody().toString(), UserRepresentation.class);
            return  userRepresentation;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new WebApplicationException(response.getStatus());
        }
    }
}
