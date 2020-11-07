package protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.Block;
import protocol.models.Summary;
import protocol.models.Vote;

/**
 * Author: Ashish Gogna
 */
public class Blockchain {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(Blockchain.class);
    private static Blockchain instance;

    private Summary summary;

    /** Public functions */
    public Blockchain() throws Exception {

        summary = Summary.refresh();

        //Genesis block
        //try { addBlock(new Block(System.currentTimeMillis(), "0", "0", null)); } catch (Exception e) { LOGGER.error("{}", e); }

        LOGGER.info("Blockchain summary: {}", summary.toString());
    }

    public static void initialize() throws Exception{
        if (instance != null) return;
        instance = new Blockchain();
    }

    public static Blockchain getInstance() {
        return instance;
    }

    /** Private functions */
    private void addBlock(Block block) throws Exception {
        FileOps.writeBlock(block.getHash(), block.toString());
        summary.setLastBlock(block.getHash());
    }
}
