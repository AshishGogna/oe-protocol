package api.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Ashish Gogna
 */

@Getter
public class VoteResponsePayload {

    /** Private declarations */
    private boolean submittedForConsensus;
    private String voteHash;

    /** Public functions */
    public VoteResponsePayload(boolean submittedForConsensus, String voteHash) {
        this.submittedForConsensus = submittedForConsensus;
        this.voteHash = voteHash;
    }
}
