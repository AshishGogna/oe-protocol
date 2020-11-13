package protocol;

import api.models.VoteRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.NodeException;
import protocol.models.Vote;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Author: Ashish Gogna
 */

public class UnconfirmedPool {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnconfirmedPool.class);
    private Map<String, Vote> voteMap;

    /** Public functions */
    public UnconfirmedPool(int poolCleanerDelay) {
        voteMap = new HashMap();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(new UnconfirmedPoolCleaner(), poolCleanerDelay, poolCleanerDelay, TimeUnit.SECONDS);

        try {
//        Test
//            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
//            keyGen.initialize(1024, new SecureRandom());
//            KeyPair pair = keyGen.generateKeyPair();
//            String pubK = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
//            String pvtK = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
//            LOGGER.info("Pbk = " + pubK);
//            LOGGER.info("Pvk = " + pvtK);
//            String pubK = "MIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBAIeZ143wGcBITdJz7+G290BzV5qikB5e7obEanJMtcVt/0NUm9CmIcbNTwcIizxT5wVcvLQbUi6J6SqiWhKDrlS39ozz9XjeIzPKDIutw8P1u93UPOF/pgxhwLdbigsoMHqaupywv3gMdT+zL+iz6L88Ybf/QgEn5lbm2Jbb4oBf";
//            String pvtK = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUW5LIybiiYh4FCeQcFcmigZFu5FY=";
//            String data = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAU7sM8Uhvzy7gF9MMocGKki52QLyco6tB0ClK7VMizK0BmaCXZdNfC2ryeLNYs26DypkU7UJZYdKd+sTNjRF4fUFt9fcZPt8HcfcBfr+/nkfGBHEa99NcfMdi/MLdW51hOjNTMz6T6Ub5QQIZnzMRdMxMMWrwFr6UDBuKUB1rYCY=";
//            String sign = Crypto.createSignature(pvtK, data);
//            LOGGER.info("SG = " + sign);
//            boolean solid = Crypto.verifySignature(sign, pubK, data);
//            LOGGER.info("VF = " + solid);

//            String e = Crypto.encrypt(pvtK, "");
//            LOGGER.info("E = {}", e);
//            String d = Crypto.decrypt(e, "");
//            LOGGER.info("D = {}", d);
        } catch (Exception e) {
            LOGGER.error("{}", e);
        }
    }

    public String addVote(VoteRequestPayload vrp) throws NodeException {

        //Verify the LWC signature.
        //Bypassed for testing LWC.
//        boolean lwcSolid = false;
//        try {
//            List<String> lwcs = DataStore.readLWCs();
//            for (String lwc : lwcs) {
//                VoteRequestPayload copy = new VoteRequestPayload(vrp);
//                lwcSolid = Crypto.verifySignature(vrp.getSignature(), lwc, copy.jsonify());
//                if (lwcSolid) break;
//            }
//        } catch (Exception ignored) { }
//        if (!lwcSolid) throw new NodeException(NodeException.Reason.VoteTampered);

        //Add to the unconfirmed pool
        try {
            Vote v = new Vote(vrp.getElectionId(), vrp.getVoterFingerprint(), vrp.getConstituencyId(), vrp.getCandidateId());

            if (voteMap.containsKey(v.getDigink())) throw new NodeException(NodeException.Reason.VoteAlradyExistsInUnconfirmedPool);
            if (Node.getInstance().getBlockchain().voteExists(v.getDigink())) throw new NodeException(NodeException.Reason.VoteAlradyExistsInBlockchain);

            LOGGER.info("Vote added to unconfirmed pool: {}", v.getDigink());
            voteMap.put(v.getDigink(), v);
            return v.getDigink();
        } catch (Exception e) {
            throw new NodeException(NodeException.Reason.SystemFailure, e);
        }
    }

    /** Private classes */
    private class UnconfirmedPoolCleaner implements Runnable {

        /** Private classes: Public functions */
        public void run() {
            try {
                LOGGER.info("UnconfirmedPool cleaner run.");

                //Generate block
                if (voteMap.size() > 0) {
                    Node.getInstance().getBlockchain().generateBlock(voteMap.values());
                    voteMap.clear();
                }

                //Validate blockchain
                Node.getInstance().getBlockchain().validate();
            } catch (Exception e) {
                if (e instanceof NodeException) {
                    NodeException ne = (NodeException) e;
                    if (ne.getReason().equals(NodeException.Reason.NotAnAuthority)) voteMap.clear();
                }
                LOGGER.info("Couldn't clear unconfirmed pool: {}", e);
            }
        }
    }
}
