package protocol.models;

import api.models.VoteRequestPayload;
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
    private String constituencyId;
    private String candidateId;
    private String digink;

    /** Public functions */
    public Vote(String electionId, String voterFingerprint, String constituencyId, String candidateId) throws Exception {
        this.electionId = electionId;
        this.constituencyId = constituencyId;
        this.candidateId = candidateId;
        this.digink = digink(voterFingerprint);
    }

    /** Private functions */
    private String digink(String voterFingerprint) throws Exception {
        return Crypto.sha256(electionId + voterFingerprint);
    }
}
