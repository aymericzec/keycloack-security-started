package org.acme.service;

import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.acme.Token;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloackService {

    private final Gson gson;

    public KeycloackService(){
        this.gson = new Gson();
    }

    public Token login (String login, String password) {
        HttpResponse<JsonNode> response =  Unirest.post("http://192.168.0.49:8080/auth/realms/feng-realm/protocol/openid-connect/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .field("username", login)
                .field("password", password)
                .field("grant_type", "password")
                .field("client_id", "backend-service")
                .asJson();

        return this.gson.fromJson(response.getBody().toString(), Token.class);
    }
}
