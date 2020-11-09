package protocol;

import api.models.VoteRequestPayload;
import lombok.Getter;
import protocol.models.Block;
import protocol.models.NodeException;
import protocol.models.Vote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Ashish Gogna
 */

public class Node {

    /** Private declarations */
    private static Node instance;

    private String secret;
    private UnconfirmedPool unconfirmedPool;
    private Blockchain blockchain;

    /** Public functions */
    public Node(String secret) throws Exception {
        this.secret = secret;
        unconfirmedPool = new UnconfirmedPool();
        blockchain = new Blockchain();
    }

    public static void initialize(String secret) throws Exception {
        if (instance != null) return;
        instance = new Node(secret);
    }

    public static Node getInstance() {
        return instance;
    }

    public boolean isAuthorized() {
        //TODO: Cryptographic validation pls.
        return true;
    }

    public String vote(VoteRequestPayload vrp) throws NodeException {
        return unconfirmedPool.addVote(vrp);
    }

    public void submitVotesForConsensus(Collection<Vote> voteCollection) throws Exception {

        //Generate merkle root
        List<Vote> votes = new ArrayList<>();
        List<String> diginks = new ArrayList<>();
        for (Vote vote : voteCollection) {
            votes.add(vote);
            diginks.add(vote.getDigink());
        }
        String merkleRoot = Crypto.calculateMerkleRoot(diginks);

        //Create block
        Block block = new Block(System.currentTimeMillis(), blockchain.getSummary().getLastBlock(), merkleRoot, votes);

        //Add into blockchain, if this node is authority.
        blockchain.addBlock(block);
    }

    public String getSecret() { return secret; }
}
