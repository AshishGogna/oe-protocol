package protocol;

import api.models.PackagedBlockchainResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Author: Ashish Gogna
 */

@Getter
public class Blockchain {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(Blockchain.class);

    private Summary summary;
    private boolean syncedOnStartup;

    /** Public functions */
    public Blockchain() throws Exception {

        summary = Summary.refresh();
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

        //Share with the network.
        Node.getInstance().getRegistery().shareNewBlock(block);
    }

    public void addBlock(Block block) throws NodeException {

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
        if (!block.getMRoot().equals(merkleRoot)) throw new NodeException(NodeException.Reason.MerkleRootMismatch);

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
        summary.blockAdded(block.getHash());
        LOGGER.info("Added new block: {}", block);
    }

    public boolean voteExists(String digink) {
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

    public void validate() {

        LOGGER.info("Blockchain validation started: {}", summary);

        boolean solid = true;
        int blocksValidated = 0;
        String bHash = summary.getLastBlock();
        while (DataStore.hasBlock(bHash)) {
            try {
                Block block = DataStore.readBlock(bHash);
                Block copy = new Block(block);

                //Validate previous hash
                if (DataStore.hasBlock(block.getPreviousHash())) {
                    Block previousBlock = DataStore.readBlock(block.getPreviousHash());
                    Block previousBlockCopy = new Block(previousBlock);

                    if (previousBlock.getSignature() !=null ) {
                        previousBlockCopy.setSignature(previousBlock.getSignature());
                    }
                    previousBlockCopy.calculateHash();
                    if (!block.getPreviousHash().equals(previousBlockCopy.getHash())) throw new NodeException(NodeException.Reason.HashMismatch);
                    previousBlockCopy.setHash(block.getHash());
                }

                //Validate signature
                if (!block.getPreviousHash().equals("0")) {
                    boolean signSolid = false;
                    List<String> auths = DataStore.readAuths();
                    for (String auth : auths) {
                        try {
                            signSolid = Crypto.verifySignature(block.getSignature(), auth, copy.toString());
                        } catch (Exception ignored) {
                        }
                        if (signSolid) break;
                    }
                    if (!signSolid) throw new NodeException(NodeException.Reason.BlockTampered);
                    copy.setSignature(block.getSignature());
                }

                //Validate hash
                copy.calculateHash();
                if (!block.getHash().equals(copy.getHash())) throw new NodeException(NodeException.Reason.HashMismatch);
                copy.setHash(block.getHash());

                //Validate merkle root
                if (!block.getPreviousHash().equals("0")) {
                    List<Vote> votes = new ArrayList<>();
                    List<String> diginks = new ArrayList<>();
                    for (Vote vote : block.getVotes()) {
                        votes.add(vote);
                        diginks.add(vote.getDigink());
                    }
                    String mRoot = Crypto.calculateMerkleRoot(diginks);
                    if (!block.getMRoot().equals(mRoot)) throw new NodeException(NodeException.Reason.MerkleRootMismatch);
                }

                blocksValidated++;
                bHash = block.getPreviousHash();
            } catch (Exception e) {
                LOGGER.error("Blockchain validation failed: {}", e);
                solid = false;
                break;
            }
        }
        if (blocksValidated != summary.getBlocks()) solid = false;

        LOGGER.info("Blockchain validation finished : {}", solid);
        if (!solid) resync();
    }

    public PackagedBlockchainResponse getPackaged() {
        List<Block> blocks = new ArrayList<>();
        String bHash = summary.getLastBlock();
        while (DataStore.hasBlock(bHash)) {
            Block b = DataStore.readBlock(bHash);
            blocks.add(b);
            bHash = b.getPreviousHash();
        }
        return new PackagedBlockchainResponse(summary.getLastBlock(), blocks);
    }

    public void resync() {
        LOGGER.info("Blockchain resync started.");

        //Fetch the Blockchain from the network.
        try {
            PackagedBlockchainResponse pbcr = Node.getInstance().getRegistery().getBlockchain(0);
            if (pbcr == null) {
                LOGGER.info("Couldn't fetch blockchain from any node.");
                return;
            };

            //Remove all blocks
            DataStore.removeBlocks();

            for (Block b : pbcr.getBlocks()) {
                DataStore.writeBlock(b.getHash(), b.toString());
            }
            summary.setLastBlock(pbcr.getLastBock());
            summary.setBlocks(pbcr.getBlocks().size());
            summary.update();
            LOGGER.info("Blockchain synced.");
        } catch (NodeException e) {
            LOGGER.error("Couldn't sync blockchain: {}", e);
        }
    }
}
