package protocol.models;

import com.google.gson.Gson;
import lombok.Getter;
import protocol.Crypto;
import protocol.DataStore;
import protocol.Node;

import java.util.List;

/**
 * Author: Ashish Gogna
 */

@Getter
public class Block {

    /** Private declarations */
    private List<Vote> votes;
    private long timestamp;
    private String merkleRoot;
    private String previousHash;
    private String signature;
    private String hash;

    /** Public functions */
    public Block(long timestamp, String previousHash, String merkleRoot, List<Vote> votes) throws Exception {
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.merkleRoot = merkleRoot;
        this.votes = votes;
        this.signature = sign();
        this.hash = hash();
    }

    public Block(Block block) throws Exception {
        this.timestamp = block.timestamp;
        this.previousHash = block.previousHash;
        this.merkleRoot = block.merkleRoot;
        this.votes = block.votes;
    }

    public boolean containsVote(String digink) {
        for (Vote v : votes) {
            if (v.getDigink().equals(digink)) return true;
        }
        return false;
    }

    public void setSignature(String signature) { this.signature = signature; }
    public void setHash(String hash) { this.hash = hash; }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /** Private functions */
    private String sign() throws Exception {
        String ePvtK = DataStore.readAuth().getNodeEncryptedPrivateKey();
        String pvtK = Crypto.decrypt(ePvtK, Node.getInstance().getSecret());
        return Crypto.createSignature(pvtK, this.toString());
    }

    private String hash() throws Exception {
        return Crypto.sha256(this.toString());
    }
}
