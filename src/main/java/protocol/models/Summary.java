package protocol.models;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import protocol.FileOps;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Author: Ashish Gogna
 */

public class Summary {

    /** Private declarations */
    private String lastBlock;

    /** Public functions */
    public static Summary refresh() throws FileNotFoundException {
        Summary s = FileOps.readSummary();
        return s == null ? new Summary() : s;
    }

    public void update() throws IOException {
        FileOps.writeSummary(this.toString());
    }

    public String getLastBlock() { return lastBlock; }
    public void setLastBlock(String lastBlock) throws IOException {
        this.lastBlock = lastBlock;
        update();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
