package api.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Ashish Gogna
 */

@Getter
@Setter
public class AddRegistryEntityRequest {

    /** Private declarations */
    private String signature;
    private String entity;

    /** Public functions */
    public AddRegistryEntityRequest(String signature, String entity) {
        this.signature = signature;
        this.entity = entity;
    }
}
