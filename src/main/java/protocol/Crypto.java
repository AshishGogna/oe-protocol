package protocol;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.Block;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Author: Ashish Gogna
 */

public class Crypto {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(Crypto.class);

    /** Public functions */
    public static String sha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String createSignature(String privateKey, String data) throws Exception {
        Signature dsa = Signature.getInstance("SHA/DSA");

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey pk = KeyFactory.getInstance("DSA").generatePrivate(keySpec);

        dsa.initSign(pk);
        dsa.update(data.getBytes());
        return Base64.getEncoder().encodeToString(dsa.sign());
    }

    public static boolean verifySignature(String signature, String publicKey, String data) throws Exception {
        Signature dsa = Signature.getInstance("DSA");

        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        PublicKey pk = keyFactory.generatePublic(keySpec);

        dsa.initVerify(pk);
        dsa.update(data.getBytes());
        return dsa.verify(Base64.getDecoder().decode(signature));
    }
}
