package protocol.models;

/**
 * Author: Ashish Gogna
 */
public class NodeException extends Exception {

    /** Public declarations */
    public enum Reason {
        BlockAlreadyExists,
        VoteTampered,
        SystemFailure,
        VoteAlradyExistsInUnconfirmedPool,
        VoteAlradyExistsInBlockchain,
        PreviousHashMismatch,
        HashMismatch,
        BlockTampered,
        MerkleRootMismatch,
        DiginkDuplicate,
        SignatureVerificationFailed,
        CryptoFailure,
        DataStoreFailure,
        NotAnAuthority
    }

    /** Private declarations */
    private Reason reason;

    /** Public functions */
    public NodeException(Reason reason) {
        super(reason.toString());
        this.reason = reason;
    }

    public NodeException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public NodeException(Reason reason, Throwable cause) {
        super(reason.toString(), cause);
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return String.format("%s %s", reason, super.getMessage());
    }
}
