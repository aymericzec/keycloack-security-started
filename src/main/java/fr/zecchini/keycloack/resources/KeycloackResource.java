package fr.zecchini.keycloack.resources;

import fr.zecchini.keycloack.models.OfflineToken;
import fr.zecchini.keycloack.models.Token;
import fr.zecchini.keycloack.resources.inputs.LoginUserInput;
import fr.zecchini.keycloack.resources.inputs.RefreshTokenInput;
import fr.zecchini.keycloack.resources.inputs.SignupUserInput;
import fr.zecchini.keycloack.resources.outputs.OfflineTokenOutput;
import fr.zecchini.keycloack.resources.outputs.TokenOutput;
import fr.zecchini.keycloack.service.KeycloackService;
import org.keycloak.representations.idm.UserRepresentation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/keycloack")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KeycloackResource {
    @Inject
    KeycloackService keycloackService;

    @POST
    @Path("login")
    public TokenOutput login(LoginUserInput login) throws WebApplicationException {
        Token token = keycloackService.login(login.username, login.password);
        return TokenOutput.create(token);
    }

    @POST
    @Path("login-offline")
    public OfflineTokenOutput loginOffline(LoginUserInput login) throws WebApplicationException {
        OfflineToken offlineToken = keycloackService.loginOffline(login.username, login.password);
        return OfflineTokenOutput.create(offlineToken);
    }

    @POST
    @Path("refresh-token")
    public Token refreshToken(RefreshTokenInput refreshTokenInput) throws WebApplicationException {
        return keycloackService.refreshToken(refreshTokenInput.refreshToken);
    }

    @POST
    @Path("signup")
    public OfflineTokenOutput signup(SignupUserInput signupUserInput) throws WebApplicationException {
        OfflineToken offlineToken = keycloackService.signup(signupUserInput);
        return OfflineTokenOutput.create(offlineToken);
    }

    @POST
    @Path("logout")
    public void logout(RefreshTokenInput refreshTokenInput) throws WebApplicationException {
        keycloackService.logout(refreshTokenInput.refreshToken);
    }

    @GET
    @Path("user")
    @RolesAllowed("user")
    public UserRepresentation user() throws WebApplicationException {
        return keycloackService.user("b926ca4e-dd07-4014-8d94-5f0074c6cbdd");
    }
}