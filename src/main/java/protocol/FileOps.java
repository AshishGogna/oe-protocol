package protocol;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.NodeException;
import protocol.models.Summary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Author: Ashish Gogna
 */
public class FileOps {

    /** Private declarations */
    private static final String PATH_SUMMARY = "./data/sum";
    private static final String PATH_BLOCKS = "./data/blocks/";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileOps.class);

    /** Public functions */
    public static Summary readSummary() throws FileNotFoundException {
        return readFromFile(PATH_SUMMARY, Summary.class);
    }

    public static void writeSummary(String data) throws IOException {
        writeToFile(PATH_SUMMARY, data);
    }

    public static void writeBlock(String fileName, String data) throws Exception {
        String path = PATH_BLOCKS + fileName;

        File f = new File(path);
        if (f.exists()) throw new NodeException(NodeException.Reason.FileAlreadyExists);

        writeToFile(path, data);
    }

    /** Private functions */
    private static void writeToFile(String path, String data) throws IOException {
        FileWriter myWriter = new FileWriter(PATH_SUMMARY);
        myWriter.write(data);
        myWriter.close();
    }

    private static <T> T readFromFile(String path, Class<T> t) throws FileNotFoundException {
        File f = new File(path);
        Scanner r = new Scanner(f);
        String json = "";
        while (r.hasNextLine()) {
            String data = r.nextLine();
            json += data;
        }
        Gson gson = new Gson();
        return gson.fromJson(json, t);
    }
}
