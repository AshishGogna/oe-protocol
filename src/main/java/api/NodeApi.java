package api;

import api.models.ApiResponse;
import api.models.ApiStatus;
import api.models.VoteRequestPayload;
import api.models.VoteResponsePayload;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.Node;
import protocol.UnconfirmedPool;
import protocol.models.NodeException;
import protocol.models.Vote;

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
            String voteDigink = Node.getInstance().vote(request);
            return new ApiResponse(ApiStatus.OK, new VoteResponsePayload(voteDigink));
        } catch (NodeException e) {
            LOGGER.error("Exception caught at /vote: {}", e);
            return processException(e);
        }
    }
}
