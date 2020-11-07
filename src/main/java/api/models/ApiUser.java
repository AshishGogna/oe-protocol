package api.models;

import java.security.Principal;

/**
 * Author: Ashish Gogna
 */

public class ApiUser implements Principal {

    public ApiUser() { }

    @Override
    public String getName() {
        return "user";
    }
}
