package api;

import api.models.ApiResponse;
import api.models.ApiStatus;
import io.dropwizard.auth.UnauthorizedHandler;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Author: Ashish Gogna
 */
public class ResponseHandler401 implements UnauthorizedHandler {

    /** Public functions */
    @Override
    public Response buildResponse(String s, String s1) {
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(new ApiResponse<>(ApiStatus.UNAUTHORIZED))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
