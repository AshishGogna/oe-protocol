import api.models.VoteRequestPayload;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.*;

import java.io.Console;

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
            if (args.length < 2) {
                LOGGER.error("Invalid init args. Exiting.");
                return;
            }
            String pwd = getPassword();
            boolean isAuthority = Node.isAuthority(pwd);
            if (!isAuthority) pwd = null;

            Node.initialize(args[0], Integer.parseInt(args[1]), pwd);
            LOGGER.info("***********************************");
            LOGGER.info("Node Initialized.");
            LOGGER.info("Node type: {}", (!isAuthority) ? "Basic": "Authority");
            if (isAuthority) {
                LOGGER.info("Node Public Key: {}", DataStore.readAuth().getPublicKey());
            }
            LOGGER.info("***********************************");
            Node.getInstance().getRegistery().shareSelf();

            //Test
            try {
//                VoteRequestPayload vrp = new VoteRequestPayload("GE2024", "dbb006bb7b71d59d927b6671557fe1c978f0dd44a1eb909658c0d1c2d556ea0a", null, "AP1BJPML", "BJP");
//                Gson gson = new Gson();
//                String vrpd = gson.toJson(vrp);
//                String pvk = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUWl+mCOLfjSp5DawqUps1W5EdzYM=";
//                vrp.setSignature(Crypto.createSignature(pvk, vrpd));
//                Node.getInstance().vote(vrp);
//
//                String s = "MCwCFBzJR7FFEjNrbqzv67cTs0RY2gthAhQlvW4nDMoRD2wxWNb6kW2HC5DRsg==";
//                String d = "{\"votes\":[{\"electionId\":\"GE2024\",\"candidateId\":\"BJP\",\"digink\":\"8440ed8343479f347c9ce50c165d6290d4a768df91b65684a640412b620840f6\"}],\"timestamp\":1605015845168,\"previousHash\":\"67778288dc33ddcc9d79cc5826fa1393cbf7108dac6c2d4b08281b21eb636976\"}";
//                String a = "MIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBAPJy7/+SPPurVzuYRVqZqClpC40MkKa7oYHB5ONNcdkwmrOCGU8I+ZiFHBvKmiqJVgWrhCzoGssqEFOUrrCyT0rSUArHTz3H1wWoWoJ4dM4LPu4F6qQ/KxzXVbci+a9b3OibD5lIW+qN4ddTYa+EqIFMsS8Z43jWlhuP16/TBiPp";
//                LOGGER.info("{}",  Crypto.verifySignature(s, a, d));
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            LOGGER.error("Terminating due to exception while trying to initialize node: {}", e);
        }
    }

    /** Private functions */
    private static String getPassword() {
        String pwd = null;
        Console con = System.console();
        if(con != null) {
            try {
                DataStore.readAuth().getNodePublicKey();
                System.out.println("Enter private key password: ");
                char[] ch = con.readPassword();
                pwd = String.valueOf(ch);
            } catch (Exception ignored) { }
        }
        if (pwd.length() == 0) pwd = null;
        return pwd;
    }
}
