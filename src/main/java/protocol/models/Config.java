package protocol.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Ashish Gogna
 */

@Getter
@Setter
public class Config {

    /** Private declarations */
    private String endpoint;
    private int cleanerInterval;
    private String secret;
}
