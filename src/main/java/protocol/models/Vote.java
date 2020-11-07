package protocol.models;

import com.google.gson.Gson;
import lombok.Getter;
import protocol.Crypto;

/**
 * Author: Ashish Gogna
 */

@Getter
public class Vote {

    /** Private declarations */
    private String electionId;
    private String voterPublicKey;
    private String voterSignature;
    private String candidateId;
    private String digink;

    /** Public functions */
    public Vote(String electionId, String voterPublicKey, String voterSignature, String candidateId) throws Exception {
        this.electionId = electionId;
        this.voterPublicKey = voterPublicKey;
        this.voterSignature = voterSignature;
        this.candidateId = candidateId;
        this.digink = digink();
    }

    /** Private functions */
    private String digink() throws Exception {
        return Crypto.sha256(electionId + voterPublicKey);
    }
}
