package api.models;

/**
 * Author: Ashish Gogna
 */

public class ApiStatus {
    public static final ApiStatus OK             = new ApiStatus(200, "OK");
    public static final ApiStatus BAD_REQUEST    = new ApiStatus(400, "Bad Request");
    public static final ApiStatus UNAUTHORIZED   = new ApiStatus(401, "Unauthorized");
    public static final ApiStatus UNPROCESSABLE_ENTITY = new ApiStatus(422, "Unprocessable Entity");
    public static final ApiStatus NOT_FOUND      = new ApiStatus(404, "Not found");
    public static final ApiStatus INTERNAL_ERROR = new ApiStatus(500, "Internal server error");

    private int code;
    private String message;

    public ApiStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
