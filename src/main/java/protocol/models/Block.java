package protocol.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;
import protocol.Crypto;

/**
 * Author: Ashish Gogna
 */

@Getter
public class Block {

    /** Private declarations */
    private Vote[] votes;
    private long timestamp;
    private String mRoot;
    private String previousHash;
    private String hash;

    /** Public functions */
    public Block(long timestamp, String previousHash, String mRoot, Vote[] votes) throws Exception {
        this.timestamp = timestamp;
        this.previousHash = previousHash;
        this.mRoot = mRoot;
        this.votes = votes;
        this.hash = hash();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /** Private functions */
    private String hash() throws Exception {
        return Crypto.sha256(this.toString());
    }
}
