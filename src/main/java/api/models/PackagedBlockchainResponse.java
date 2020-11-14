package api.models;

import java.util.List;

import api.models.ApiResponse;
import lombok.Getter;
import protocol.models.Block;

/**
 * Author: Ashish Gogna
 */

@Getter
public class PackagedBlockchainResponse {

    /** Private declarations */
    private String lastBock;
    private List<Block> blocks;

    /** Public functions */
    public PackagedBlockchainResponse(String lastBock, List<Block> blocks) {
        this.lastBock = lastBock;
        this.blocks = blocks;
    }
}
