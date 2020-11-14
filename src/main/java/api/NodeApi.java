package api;

import api.models.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.Node;
import protocol.models.NodeException;
import api.models.PackagedBlockchainResponse;

import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Author: Ashish Gogna
 */

@Slf4j
@Path("/node")
@Produces(MediaType.APPLICATION_JSON)
public class NodeApi extends Api {

    /** Private declarations */
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeApi.class);

    /** Public functions */
    public NodeApi(Validator validator) {
        super(validator);
    }

    @GET
    @Path("/status")
    public ApiResponse getConfig() {
        try {
            return new ApiResponse(ApiStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Exception caught at /status: {}", e);
            return processException(e);
        }
    }

    @POST
    @Path("/vote")
    public ApiResponse<VoteResponsePayload> vote(VoteRequestPayload request) {
        try {
            if (!Node.getInstance().isThisAuthority()) throw new NodeException(NodeException.Reason.NotAnAuthority);
            String voteDigink = Node.getInstance().vote(request);
            return new ApiResponse(ApiStatus.OK, new VoteResponsePayload(voteDigink));
        } catch (NodeException e) {
            LOGGER.error("Exception caught at /vote: {}", e);
            return processException(e);
        }
    }

    @POST
    @Path("/add-block")
    public ApiResponse addBlock(AddBlockRequest request) {
        try {
            Node.getInstance().getBlockchain().addBlock(request.getBlock());
            return new ApiResponse(ApiStatus.OK);
        } catch (NodeException e) {
            LOGGER.error("Exception caught at /add-block: {}", e);
            return processException(e);
        }
    }

    @POST
    @Path("/add-auth")
    public ApiResponse addAuth(AddRegistryEntityRequest request) {
        try {
            Node.getInstance().getRegistery().addAuth(request);
            return new ApiResponse(ApiStatus.OK);
        } catch (NodeException e) {
            LOGGER.error("Exception caught at /add-auth: {}", e);
            return processException(e);
        }
    }

    @POST
    @Path("/add-lwc")
    public ApiResponse addLwc(AddRegistryEntityRequest request) {
        try {
            Node.getInstance().getRegistery().addLwc(request);
            return new ApiResponse(ApiStatus.OK);
        } catch (NodeException e) {
            LOGGER.error("Exception caught at /add-lwc: {}", e);
            return processException(e);
        }
    }

    @POST
    @Path("/add-node")
    public ApiResponse addNode(AddRegistryEntityRequest request) {
        Node.getInstance().getRegistery().addNode(request);
        return new ApiResponse(ApiStatus.OK);
    }

    @POST
    @Path("/rem-auth")
    public ApiResponse remAuth(AddRegistryEntityRequest request) {
        try {
            Node.getInstance().getRegistery().remAuth(request);
            return new ApiResponse(ApiStatus.OK);
        } catch (NodeException e) {
            LOGGER.error("Exception caught at /rem-auth: {}", e);
            return processException(e);
        }
    }

    @POST
    @Path("/rem-lwc")
    public ApiResponse remLwc(AddRegistryEntityRequest request) {
        try {
            Node.getInstance().getRegistery().remLwc(request);
            return new ApiResponse(ApiStatus.OK);
        } catch (NodeException e) {
            LOGGER.error("Exception caught at /rem-lwc: {}", e);
            return processException(e);
        }
    }

    @POST
    @Path("/rem-node")
    public ApiResponse remNode(AddRegistryEntityRequest request) {
        Node.getInstance().getRegistery().remNode(request);
        return new ApiResponse(ApiStatus.OK);
    }

    @POST
    @Path("/get-blockchain")
    public ApiResponse<PackagedBlockchainResponse> getBlockchain() {
        return new ApiResponse(ApiStatus.OK, Node.getInstance().getBlockchain().getPackaged());
    }
}
