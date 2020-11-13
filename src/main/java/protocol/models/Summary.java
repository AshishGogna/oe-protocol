package protocol.models;

import com.google.gson.Gson;
import lombok.Getter;
import protocol.DataStore;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Author: Ashish Gogna
 */

@Getter
public class Summary {

    /** Private declarations */
    private String lastBlock;
    private int blocks;

    /** Public functions */
    public static Summary refresh() throws FileNotFoundException {
        Summary s = DataStore.readSummary();
        return s == null ? new Summary() : s;
    }

    public void update() throws NodeException {
        DataStore.writeSummary(this.toString());
    }

    public void blockAdded(String lastBlock) throws NodeException {
        this.lastBlock = lastBlock;
        this.blocks++;
        update();
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
