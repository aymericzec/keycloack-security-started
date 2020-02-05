package fr.zecchini.keycloack.middleware;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HttpExeptionMapper implements ExceptionMapper<Exception> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Response toResponse(Exception exception) {
        int code = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        if (exception instanceof WebApplicationException) {
            code = ((WebApplicationException) exception).getResponse().getStatus();
        }

        String jsonError = mapper.createObjectNode()
                .put("error", exception.getMessage())
                .put("status", code)
                .toString();

        return Response
                .status(code)
                .type(MediaType.APPLICATION_JSON)
                .entity(jsonError)
                .build();
    }
}
