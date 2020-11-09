package protocol.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Ashish Gogna
 */

@Getter
@Setter
public class Auth {

    /** Private declarations */
    private String issuer;
    private String publicKey;
    private String signature;
    private String nodePublicKey;
    private String nodeEncryptedPrivateKey;
}
