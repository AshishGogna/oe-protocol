import api.NodeApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.Blockchain;
import protocol.UnconfirmedPool;

/**
 * Author: Ashish Gogna
 */

public class MainClass {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainClass.class);

    /** Public functions */
    public static void main(String[] args) {
        try {
//            new NodeApplication().run(args);
            UnconfirmedPool.initialize();
            Blockchain.initialize();
            LOGGER.info("*** Open Elections Node Initialized ***");
        } catch (Exception e) {
            LOGGER.error("Terminating due to exception while trying to initialize node: {}", e);
        }
    }
}
