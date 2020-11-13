import api.NodeApplication;
import api.models.VoteRequestPayload;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.Blockchain;
import protocol.Crypto;
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

            //Test
//            try {
//                VoteRequestPayload vrp = new VoteRequestPayload("GE2024", "dbb006bb7b71d59d927a6671557fe1c978f0dd44a1eb909658c0d1c2d556ea0a", null, "AP1BJPML", "BJP");
//                Gson gson = new Gson();
//                String vrpd = gson.toJson(vrp);
//                String pvk = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUWl+mCOLfjSp5DawqUps1W5EdzYM=";
//                vrp.setSignature(Crypto.createSignature(pvk, vrpd));
//                Node.getInstance().vote(vrp);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


        } catch (Exception e) {
            LOGGER.error("Terminating due to exception while trying to initialize node: {}", e);
        }
    }
}
