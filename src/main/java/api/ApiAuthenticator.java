package api;

import api.models.ApiUser;
import io.dropwizard.auth.Authenticator;

import java.util.Optional;

/**
 * Author: Ashish Gogna
 */

public class ApiAuthenticator implements Authenticator<String, ApiUser> {

    @Override
    public Optional<ApiUser> authenticate(String authToken) {
        ApiUser apiUser = new ApiUser();
        return Optional.ofNullable(apiUser);
    }
}
