package api.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Ashish Gogna
 */

@Getter
public class VoteResponsePayload {

    /** Private declarations */
    private String voteHash;

    /** Public functions */
    public VoteResponsePayload(String voteHash) {
        this.voteHash = voteHash;
    }
}
