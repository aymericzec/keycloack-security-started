package fr.zecchini.keycloack.resources;

import fr.zecchini.keycloack.models.user.SigninUser;
import fr.zecchini.keycloack.models.user.SignupResponse;
import fr.zecchini.keycloack.models.user.SignupUser;
import fr.zecchini.keycloack.models.token.Token;
import fr.zecchini.keycloack.resources.io.user.input.SigninUserInput;
import fr.zecchini.keycloack.resources.io.token.input.RefreshTokenInput;
import fr.zecchini.keycloack.resources.io.user.input.SignupUserInput;
import fr.zecchini.keycloack.resources.io.token.output.TokenOutput;
import fr.zecchini.keycloack.service.KeycloakService;
import org.keycloak.representations.idm.UserRepresentation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/keycloack")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KeycloackResource {
    @Inject
    KeycloakService keycloackService;

    @POST
    @Path("signup")
    public SignupResponse signup(SignupUserInput signupUserInput) throws WebApplicationException {
        SignupUser signupUser = SignupUser.create(signupUserInput);
        return keycloackService.signup(signupUser);
    }

    @POST
    @Path("signin")
    public TokenOutput signin(SigninUserInput signinUserInput) throws WebApplicationException {
        Token token = keycloackService.signin(SigninUser.create(signinUserInput));
        return TokenOutput.create(token);
    }

    @POST
    @Path("refresh-token")
    public Token refreshToken(RefreshTokenInput refreshTokenInput) throws WebApplicationException {
        return keycloackService.refreshToken(refreshTokenInput.refreshToken);
    }

    @POST
    @Path("logout")
    @RolesAllowed("user")
    public void logout(RefreshTokenInput refreshTokenInput) throws WebApplicationException {
        keycloackService.logout(refreshTokenInput.refreshToken);
    }

    @GET
    @Path("user")
    @RolesAllowed("user")
    public UserRepresentation user() throws WebApplicationException {
        return keycloackService.user();
    }

    @DELETE
    @Path("user")
    @RolesAllowed("user")
    public Response deleteUser() throws WebApplicationException {
        keycloackService.deleteUser();
        return Response.status(200).build();
    }
}