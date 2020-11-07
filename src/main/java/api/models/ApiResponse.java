package api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Author: Ashish Gogna
 */
public class ApiResponse<T> {

    private ApiStatus status;
    private T payload;

    public ApiResponse() { }

    public ApiResponse(ApiStatus status) {
        this(status, null);
    }

    public ApiResponse(ApiStatus status, T payload) {
        this.status = status;
        this.payload = payload;
    }

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        try { return new ObjectMapper().writeValueAsString(this); }
        catch (JsonProcessingException e) { return "ApiResponse serialization failed. M: " + e.getMessage(); }
    }
}
