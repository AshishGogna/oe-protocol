package protocol;


import api.models.AddBlockRequest;
import api.models.AddRegistryEntityRequest;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.Block;
import protocol.models.NodeException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Author: Ashish Gogna
 */

public class Registery {

    /** Private declarations */
    private static final String NODE_PATH_ADD_BLOCK = "/add-block";
    private static final String NODE_PATH_ADD_NODE = "/add-node";
    private static final String NODE_PATH_ADD_AUTH= "/add-auth";
    private static final String NODE_PATH_ADD_LWC = "/add-lwc";
    private static final String NODE_PATH_REM_NODE = "/rem-node";
    private static final String NODE_PATH_REM_AUTH= "/rem-auth";
    private static final String NODE_PATH_REM_LWC = "/rem-lwc";

    private static final Logger LOGGER = LoggerFactory.getLogger(Blockchain.class);

    /** Public functions */
    public Registery() { }

    public void shareSelf() {
        shareEntity(NODE_PATH_ADD_NODE, "", Node.getInstance().getEndpoint());
        try { if (Node.getInstance().isThisAuthority()) shareEntity(NODE_PATH_ADD_AUTH, "", DataStore.readAuth().getPublicKey()); } catch (Exception e) { }
    }

    public void addNode(AddRegistryEntityRequest request) {
        if (request.getEntity().equals(Node.getInstance().getEndpoint())) {
            LOGGER.info("I am the node, no need to add in registry.");
            return;
        }

        try {
            DataStore.addNode(request.getEntity());
            shareEntity(NODE_PATH_ADD_NODE, "", request.getEntity());
        } catch (Exception e) {
            LOGGER.error("Couldn't add node in registry: ", e);
        }
    }

    public void addAuth(AddRegistryEntityRequest request) throws NodeException {
        if (!isRequestSolid(request)) throw new NodeException(NodeException.Reason.SignatureVerificationFailed);
        try {
            DataStore.addAuth(request.getEntity());
            shareEntity(NODE_PATH_ADD_AUTH, request.getSignature(), request.getEntity());
        } catch (Exception e) {
            LOGGER.error("Couldn't add Auth in registry: ", e);
        }
    }

    public void addLwc(AddRegistryEntityRequest request) throws NodeException {
        if (!isRequestSolid(request)) throw new NodeException(NodeException.Reason.SignatureVerificationFailed);
        try {
            DataStore.addLWC(request.getEntity());
            shareEntity(NODE_PATH_ADD_LWC, request.getSignature(), request.getEntity());
        } catch (Exception e) {
            LOGGER.error("Couldn't add LWC in registry: ", e);
        }
    }

    public void remNode(AddRegistryEntityRequest request) {
        try {
            DataStore.removeNode(request.getEntity());
            shareEntity(NODE_PATH_REM_NODE, "", request.getEntity());
        } catch (Exception e) {
            LOGGER.error("Couldn't remove node from registry: ", e);
        }
    }

    public void remAuth(AddRegistryEntityRequest request) throws NodeException {
        if (!isRequestSolid(request)) throw new NodeException(NodeException.Reason.SignatureVerificationFailed);
        try {
            DataStore.removeAuth(request.getEntity());
            shareEntity(NODE_PATH_REM_AUTH, request.getSignature(), request.getEntity());
        } catch (Exception e) {
            LOGGER.error("Couldn't remove Auth from registry: ", e);
        }
    }

    public void remLwc(AddRegistryEntityRequest request) throws NodeException {
        if (!isRequestSolid(request)) throw new NodeException(NodeException.Reason.SignatureVerificationFailed);
        try {
            DataStore.removeLWC(request.getEntity());
            shareEntity(NODE_PATH_REM_LWC, request.getSignature(), request.getEntity());
        } catch (Exception e) {
            LOGGER.error("Couldn't remove LWC from registry: ", e);
        }
    }

    public void shareNewBlock(Block block) {
        try {
            List<String> nodes = DataStore.readNodes();
            for (String node : nodes) {
                AddBlockRequest addBlockRequest = new AddBlockRequest(block);
                Gson gson = new Gson();
                String json = gson.toJson(addBlockRequest);
                postCall(node + NODE_PATH_ADD_BLOCK, json);
            }
        } catch (Exception e) {
            LOGGER.error("Couldn't share new block with the network: ", e);
        }
    }

    public void shareEntity(String path, String signature, String entity) {
        try {
            List<String> nodes = DataStore.readNodes();
            for (String node : nodes) {
                AddRegistryEntityRequest request = new AddRegistryEntityRequest(signature, entity);
                Gson gson = new Gson();
                String json = gson.toJson(request);
                postCall(node + path, json);
            }
        } catch (Exception e) {
            LOGGER.error("Couldn't share this node with the network: ", e);
        }
    }

    /** Private functions */
    private boolean isRequestSolid(AddRegistryEntityRequest request) {
        boolean solid = false;
        try {
            List<String> auths = DataStore.readAuths();
            for (String auth : auths) {
                try { solid = Crypto.verifySignature(request.getSignature(), auth, request.getEntity()); } catch (Exception ignored) { }
                if (solid) break;
            }
        } catch (Exception ignored) { }
        return solid;
    }

    private void postCall(String url, String jsonBody) {
        OutputStream os = null;
        InputStream is = null;
        try {
            HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
            c.addRequestProperty("Content-Type", "application/json");
            c.setRequestMethod("POST");
            c.setConnectTimeout(2000);
            c.setReadTimeout(2000);
            c.setDoOutput(true);
            c.setDoInput(true);

            os = c.getOutputStream();
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            os.close();

            is = c.getInputStream();
            is.close();
        } catch (Exception e) {
            LOGGER.error("Couldn't call {}", url);
        } finally {
            if(os != null) try { os.close(); } catch (Exception ignored) { }
            if(is != null) try { is.close(); } catch (Exception ignored) { }
        }
    }
}
