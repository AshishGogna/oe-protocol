package protocol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.omg.CORBA.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.models.Auth;
import protocol.models.Block;
import protocol.models.NodeException;
import protocol.models.Summary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

/**
 * Author: Ashish Gogna
 */
public class DataStore {

    /** Private declarations */
    private static final String PATH_SUMMARY = "./data/sum";
    private static final String PATH_BLOCKS = "./data/blocks/";
    private static final String PATH_AUTH = "./auth";
    private static final String PATH_LWCS = "./registry/lwcs";
    private static final String PATH_AUTHS = "./registry/auths";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStore.class);

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

    public static boolean hasBlock(String hash) {
        String path = PATH_BLOCKS + hash;
        File f = new File(path);
        return f.exists();
    }

    public static Block readBlock(String hash) {
        String path = PATH_BLOCKS + hash;
        File f = new File(path);
        if (!f.exists()) return null;
        try {
            return readFromFile(path, Block.class);
        } catch (FileNotFoundException e) {
            LOGGER.error("Exception while readBlock: {}", e);
            return null;
        }
    }

    public static Auth readAuth() throws FileNotFoundException {
        return readFromFile(PATH_AUTH, Auth.class);
    }

    public static List<String> readLWCs() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode data = mapper.readValue(readFromFile(PATH_LWCS), ArrayNode.class);
        List<String> lwcs = new ArrayList<>();
        for (int i=0; i<data.size(); i++) lwcs.add(data.get(i).asText());
        return lwcs;
    }

    public static List<String> readAuths() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode data = mapper.readValue(readFromFile(PATH_AUTHS), ArrayNode.class);
        List<String> auths = new ArrayList<>();
        for (int i=0; i<data.size(); i++) auths.add(data.get(i).asText());
        return auths;
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

    private static String readFromFile(String path) throws FileNotFoundException {
        File f = new File(path);
        Scanner r = new Scanner(f);
        String json = "";
        while (r.hasNextLine()) {
            String data = r.nextLine();
            json += data;
        }
        return json;
    }
}
