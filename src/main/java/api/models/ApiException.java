package api.models;

import api.models.ApiStatus;

/**
 * Author: Ashish Gogna
 */

public class ApiException extends Exception {

    private ApiStatus status;
    private String message;

    public ApiException(ApiStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public ApiStatus getApiStatus() { return status; }

    @Override
    public String getMessage() {
        return String.format("%s %s", status, super.getMessage());
    }
}
