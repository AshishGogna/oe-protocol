package api;

import api.models.ApiException;
import api.models.ApiResponse;
import api.models.ApiStatus;

import javax.validation.Validator;

/**
 * Author: Ashish Gogna
 */

public class Api {

    private Validator validator;

    public Api(Validator validator) {
        this.validator = validator;
    }

    protected ApiResponse processException(Exception e) {
        if (e instanceof ApiException) {
            return new ApiResponse(((ApiException) e).getApiStatus());
        }
        return new ApiResponse<>(ApiStatus.INTERNAL_ERROR);
    }
}
