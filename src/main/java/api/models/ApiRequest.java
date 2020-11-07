package api.models;

import com.google.gson.Gson;

/**
 * Author: Ashish Gogna
 */

public class ApiRequest {

    /** Protected functions */
    public String jsonify() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
