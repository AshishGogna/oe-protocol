package protocol;

import api.models.VoteRequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.NodeException;
import protocol.models.Vote;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Ashish Gogna
 */

public class UnconfirmedPool {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnconfirmedPool.class);
    private static UnconfirmedPool instance;
    private Map<String, Vote> voteMap;

    /** Public functions */
    public UnconfirmedPool() {
        voteMap = new HashMap();

        //Test
//        try {
////            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
////            keyGen.initialize(1024, new SecureRandom());
////            KeyPair pair = keyGen.generateKeyPair();
////            String pubK = Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
////            String pvtK = Base64.getEncoder().encodeToString(pair.getPrivate().getEncoded());
////            LOGGER.info("Pbk = " + pubK);
////            LOGGER.info("Pvk = " + pvtK);
//            String pubK = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAU7sM8Uhvzy7gF9MMocGKki52QLyco6tB0ClK7VMizK0BmaCXZdNfC2ryeLNYs26DypkU7UJZYdKd+sTNjRF4fUFt9fcZPt8HcfcBfr+/nkfGBHEa99NcfMdi/MLdW51hOjNTMz6T6Ub5QQIZnzMRdMxMMWrwFr6UDBuKUB1rYCY=";
//            String pvtK = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUCLc4tyXeKPwt01YBX02bkQ2xohQ=";
//            String data = "{\"electionId\":\"GE2024\",\"voterPublicKey\":\" MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAU7sM8Uhvzy7gF9MMocGKki52QLyco6tB0ClK7VMizK0BmaCXZdNfC2ryeLNYs26DypkU7UJZYdKd+sTNjRF4fUFt9fcZPt8HcfcBfr+/nkfGBHEa99NcfMdi/MLdW51hOjNTMz6T6Ub5QQIZnzMRdMxMMWrwFr6UDBuKUB1rYCY=\",\"candidateId\":\"BJP\"}";
//            String sign = Crypto.createSignature(pvtK, data);
//            LOGGER.info("SG = " + sign);
//            boolean solid = Crypto.verifySignature(sign, pubK, data);
//            LOGGER.info("VF = " + solid);
//        } catch (Exception e) {
//            LOGGER.error("{}", e);
//        }
    }

    public static void initialize() {
        if (instance != null) return;
        instance = new UnconfirmedPool();
    }

    public static UnconfirmedPool getInstance() {
        return instance;
    }

    public String addVote(VoteRequestPayload vrp) throws NodeException {

        //Verify the vote's signature.
        boolean solid = false;
        try {
            solid = Crypto.verifySignature(vrp.getVoterSignature(), vrp.getVoterPublicKey(), vrp.jsonify());
        } catch (Exception ignored) { }
        if (!solid) throw new NodeException(NodeException.Reason.VoteTampered);

        try {
            Vote v = new Vote(vrp.getElectionId(), vrp.getVoterPublicKey(), vrp.getVoterSignature(), vrp.getCandidateId());
            if (voteMap.containsKey(v.getDigink())) throw new NodeException(NodeException.Reason.VoteAlradyExists);
            //TODO: check if vote exxists in blockchain

            voteMap.put(v.getDigink(), v);
            return v.getDigink();
        } catch (Exception e) {
            throw new NodeException(NodeException.Reason.SystemFailure);
        }
    }
}
