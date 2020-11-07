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
    private String voterPublicKey;
    private String voterSignature;
    private String candidateId;
}
