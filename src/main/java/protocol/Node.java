package protocol;

import api.models.VoteRequestPayload;
import com.google.gson.Gson;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.Block;
import protocol.models.NodeException;
import protocol.models.Vote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Ashish Gogna
 */

public class Node {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(Node.class);
    private static Node instance;

    private String endpoint;
    private String secret;
    private UnconfirmedPool unconfirmedPool;
    private Blockchain blockchain;
    private Registery registery;

    /** Public functions */
    public Node(String endpoint, int poolCleanerDelay, String secret) throws Exception {
        this.endpoint = endpoint;
        this.secret = secret;
        unconfirmedPool = new UnconfirmedPool(poolCleanerDelay);
        blockchain = new Blockchain();
        registery = new Registery();
    }

    public static void initialize(String endpoint, int poolCleanerDelay, String secret) throws Exception {
        if (instance != null) return;
        instance = new Node(endpoint, poolCleanerDelay, secret);
    }

    public static Node getInstance() {
        return instance;
    }

    public static boolean isAuthority(String pwd) {
        try {
            Crypto.decrypt(DataStore.readAuth().getNodeEncryptedPrivateKey(), pwd);
            return true;
        } catch (Exception e) { }
        return false;
    }

    public String vote(VoteRequestPayload vrp) throws NodeException {
        return unconfirmedPool.addVote(vrp);
    }

    public boolean isThisAuthority() {
        return isAuthority(secret);
    }

    public String getEndpoint() { return endpoint; }
    public String getSecret() { return secret; }
    public UnconfirmedPool getUnconfirmedPool() { return unconfirmedPool; }
    public Blockchain getBlockchain() { return blockchain; }
    public Registery getRegistery() { return registery; }
}
