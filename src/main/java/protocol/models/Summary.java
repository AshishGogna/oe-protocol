package protocol.models;

import com.google.gson.Gson;
import protocol.DataStore;

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
        Summary s = DataStore.readSummary();
        return s == null ? new Summary() : s;
    }

    public void update() throws NodeException {
        DataStore.writeSummary(this.toString());
    }

    public String getLastBlock() { return lastBlock; }
    public void setLastBlock(String lastBlock) throws NodeException {
        this.lastBlock = lastBlock;
        update();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
