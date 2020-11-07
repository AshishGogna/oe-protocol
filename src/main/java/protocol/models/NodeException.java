package protocol.models;

/**
 * Author: Ashish Gogna
 */
public class NodeException extends Exception {

    /** Public declarations */
    public enum Reason {
        FileAlreadyExists,
        VoteTampered,
        SystemFailure,
        VoteAlradyExists
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

    @Override
    public String getMessage() {
        return String.format("%s %s", reason, super.getMessage());
    }
}
