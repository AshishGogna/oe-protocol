package protocol;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.Block;
import protocol.models.NodeException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Author: Ashish Gogna
 */

public class Crypto {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(Crypto.class);

    /** Public functions */
    public static String sha256(String input) throws NodeException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new NodeException(NodeException.Reason.CryptoFailure, e);
        }
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

    public static String calculateMerkleRoot(List<String> diginks) throws NodeException {
        List<String> merkleRoot = generateMerkleTree(diginks);
        return merkleRoot.get(0);
    }

    public static String encrypt(String data, String secret) throws Exception {
        SecretKeySpec secretKey;
        byte[] key;
        MessageDigest sha = null;
        key = secret.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
    }

    public static String decrypt(String encryptedData, String secret) throws Exception {
        SecretKeySpec secretKey;
        byte[] key;
        MessageDigest sha = null;
        key = secret.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)));
    }

    /** Private functions */
    private static List<String> generateMerkleTree(List<String> diginks) throws NodeException {
        if (diginks.size() == 1) return diginks;

        List<String> parentHashList = new ArrayList();
        for(int i=0; i<diginks.size(); i+=2){
            String hashedString = sha256(diginks.get(i).concat(diginks.get(i+1)));
            parentHashList.add(hashedString);
        }
        if(diginks.size() % 2 == 1){
            String lastHash = diginks.get(diginks.size()-1);
            String hashedString = sha256(lastHash.concat(lastHash));
            parentHashList.add(hashedString);
        }

        return generateMerkleTree(parentHashList);
    }
}
