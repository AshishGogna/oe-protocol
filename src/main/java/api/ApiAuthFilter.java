package api;

import io.dropwizard.auth.AuthFilter;

import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.Principal;

/**
 * Author: Ashish Gogna
 */

public class ApiAuthFilter<P extends Principal> extends AuthFilter<String, P> {

    /** Public declarations */
    public static class Builder<P extends Principal> extends AuthFilterBuilder<String, P, ApiAuthFilter<P>> {
        @Override
        protected ApiAuthFilter<P> newInstance() {
            return new ApiAuthFilter<>();
        }
    }

    /** Public functions */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException { }
}
