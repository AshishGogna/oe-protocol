package protocol;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.Block;
import protocol.models.NodeException;
import protocol.models.Summary;
import protocol.models.Vote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Ashish Gogna
 */

@Getter
public class Blockchain {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(Blockchain.class);

    private Summary summary;

    /** Public functions */
    public Blockchain() throws Exception {

        summary = Summary.refresh();

        //Genesis block
        //try { addBlock(new Block(System.currentTimeMillis(), "0", "0", null)); } catch (Exception e) { LOGGER.error("{}", e); }

        LOGGER.info("Blockchain summary: {}", summary.toString());
    }

    public void generateBlock(Collection<Vote> voteCollection) throws Exception {

        //Generate merkle root
        List<Vote> votes = new ArrayList<>();
        List<String> diginks = new ArrayList<>();
        for (Vote vote : voteCollection) {
            votes.add(vote);
            diginks.add(vote.getDigink());
        }
        String merkleRoot = Crypto.calculateMerkleRoot(diginks);

        //Create block
        Block block = new Block(System.currentTimeMillis(), getSummary().getLastBlock(), merkleRoot, votes);
        LOGGER.info("Generated new block: {}", block);

        //Add to the blockchain
        addBlock(block);
    }

    public void addBlock(Block block) throws Exception {

        //Validate previous hash
        if (summary.getLastBlock() != block.getPreviousHash()) throw new NodeException(NodeException.Reason.PreviousHashMismatch);

        Block copy = new Block(block);

        //Validate signature
        boolean authSolid = false;
        List<String> auths = DataStore.readAuths();
        for (String auth : auths) {
            try { authSolid = Crypto.verifySignature(block.getSignature(), auth, copy.toString()); } catch (Exception ignored) { }
            if (authSolid) break;
        }
        if (!authSolid) throw new NodeException(NodeException.Reason.BlockTampered);
        copy.setSignature(block.getSignature());

        //Validate hash
        copy.calculateHash();
        if (!block.getHash().equals(copy.getHash())) throw new NodeException(NodeException.Reason.HashMismatch);
        copy.setHash(block.getHash());

        //Validate merkle root
        List<Vote> votes = new ArrayList<>();
        List<String> diginks = new ArrayList<>();
        for (Vote vote : block.getVotes()) {
            votes.add(vote);
            diginks.add(vote.getDigink());
        }
        String merkleRoot = Crypto.calculateMerkleRoot(diginks);
        if (!block.getMerkleRoot().equals(merkleRoot)) throw new NodeException(NodeException.Reason.MerkleRootMismatch);

        //Validate digink
        String bHash = summary.getLastBlock();
        while (DataStore.hasBlock(bHash)) {
            Block b = DataStore.readBlock(bHash);
            if (b.getVotes() != null) {
                for (Vote v : b.getVotes()) {
                    if (block.containsVote(v.getDigink()))
                        throw new NodeException(NodeException.Reason.DiginkDuplicate);
                }
            }
            bHash = b.getPreviousHash();
        }

        //Valid, add to blockchain
        DataStore.writeBlock(block.getHash(), block.toString());
        summary.setLastBlock(block.getHash());
        LOGGER.info("Added new block: {}", block);
    }

    public boolean voteExists(String digink) {
        //TODO: Scan through indexed blockchain

        //Scan through the block chain
        String bHash = summary.getLastBlock();
        while (DataStore.hasBlock(bHash)) {
            Block b = DataStore.readBlock(bHash);
            if (b.getVotes() != null) {
                for (Vote v : b.getVotes()) {
                    if (v.getDigink().equals(digink)) return true;
                }
            }
            bHash = b.getPreviousHash();
        }
        return false;
    }

    /** Private functions */
}
