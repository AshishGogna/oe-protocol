package api.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Ashish Gogna
 */

@Getter
@Setter
public class VoteRequestPayload extends ApiRequest {

    /** Private declarations */
    private String electionId;
    private String candidateId;
    private String voterFingerprint;
    private String signature;

    /** Public functions */
    public VoteRequestPayload(String electionId, String voterFingerprint, String signature, String candidateId) {
        this.electionId = electionId;
        this.voterFingerprint = voterFingerprint;
        this.signature = signature;
        this.candidateId = candidateId;
    }
}
