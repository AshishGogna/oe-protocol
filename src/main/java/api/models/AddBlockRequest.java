package api.models;

import lombok.Getter;
import lombok.Setter;
import protocol.models.Block;

/**
 * Author: Ashish Gogna
 */

@Getter
@Setter
public class AddBlockRequest {

    /** Private declarations */
    private Block block;

    /** Public functions */
    public AddBlockRequest() { }

    public AddBlockRequest(Block block) {
        this.block = block;
    }
}
