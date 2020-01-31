package org.acme;

import com.google.gson.Gson;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.acme.service.KeycloackService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.jws.WebParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/keycloack")
public class ExampleResource {
    @Inject
    KeycloackService keycloackService;

    @GET
    @Path("no-security")
    @Produces(MediaType.TEXT_PLAIN)
    public String noSecure() {
        //System.out.println("role" + identity.getRoles());
        //System.out.println(identity.getPrincipal().getName());
        return "Anybody can call me";
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Token login(@WebParam String login, String password) throws WebApplicationException {
        return keycloackService.login(login, password);
    }

    @GET
    @Path("user-security")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    public String userSecurity() {
        return "User role can call me";
    }

    @GET
    @Path("admin-security")
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String adminSecurity() {
        return "Admin role can call me";
    }
}