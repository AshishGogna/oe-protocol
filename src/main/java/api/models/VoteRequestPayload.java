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
    private String constituencyId;
    private String candidateId;
    private String voterFingerprint;
    private String signature;

    /** Public functions */
    public VoteRequestPayload(String electionId, String voterFingerprint, String signature, String constituencyId, String candidateId) {
        this.electionId = electionId;
        this.voterFingerprint = voterFingerprint;
        this.signature = signature;
        this.constituencyId = constituencyId;
        this.candidateId = candidateId;
    }

    public VoteRequestPayload(VoteRequestPayload vrp) {
        this.electionId = vrp.getElectionId();
        this.voterFingerprint = vrp.getVoterFingerprint();
        this.constituencyId = vrp.getConstituencyId();
        this.candidateId = vrp.getCandidateId();
    }
}
