import api.NodeApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.Blockchain;
import protocol.Node;
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
            Node.initialize(args[0]);
            LOGGER.info("*** Open Elections Node Initialized ***");
        } catch (Exception e) {
            LOGGER.error("Terminating due to exception while trying to initialize node: {}", e);
        }
    }
}
