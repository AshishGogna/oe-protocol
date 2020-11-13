package protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
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
    private static final String PATH_NODES = "./registry/nodes";
    private static final Logger LOGGER = LoggerFactory.getLogger(DataStore.class);

    /** Public functions */
    public static Summary readSummary() throws FileNotFoundException {
        return readFromFile(PATH_SUMMARY, Summary.class);
    }

    public static void writeSummary(String data) throws NodeException {
        try { writeToFile(PATH_SUMMARY, data); } catch (Exception e) { throw new NodeException(NodeException.Reason.DataStoreFailure, e); }
    }

    public static void writeBlock(String fileName, String data) throws NodeException {
        String path = PATH_BLOCKS + fileName;

        File f = new File(path);
        if (f.exists()) throw new NodeException(NodeException.Reason.BlockAlreadyExists);

        try { writeToFile(path, data); } catch (Exception e) { throw new NodeException(NodeException.Reason.DataStoreFailure, e); }
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

        File f = new File(PATH_LWCS);
        if (!f.exists()) return new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode data = mapper.readValue(readFromFile(PATH_LWCS), ArrayNode.class);
        List<String> lwcs = new ArrayList<>();
        for (int i=0; i<data.size(); i++) lwcs.add(data.get(i).asText());
        return lwcs;
    }

    public static List<String> readAuths() {

        File f = new File(PATH_AUTHS);
        if (!f.exists()) return new ArrayList<>();

        List<String> auths = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode data = mapper.readValue(readFromFile(PATH_AUTHS), ArrayNode.class);
            for (int i = 0; i < data.size(); i++) auths.add(data.get(i).asText());
        } catch (Exception ignored) { }
        return auths;
    }

    public static List<String> readNodes() throws Exception {

        File f = new File(PATH_NODES);
        if (!f.exists()) return new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode data = mapper.readValue(readFromFile(PATH_NODES), ArrayNode.class);
        List<String> nodes = new ArrayList<>();
        for (int i=0; i<data.size(); i++) nodes.add(data.get(i).asText());
        return nodes;
    }

    public static void addNode(String endpoint) throws Exception {
        List<String> nodes = readNodes();
        if (nodes.contains(endpoint)) {
            LOGGER.info("Node already exists in registry: {}", endpoint);
            return;
        }
        nodes.add(endpoint);
        Gson gson = new Gson();
        writeToFile(PATH_NODES, gson.toJson(nodes));
        LOGGER.info("Node added in registry: {}", endpoint);
    }

    public static void removeNode(String endpoint) throws Exception {
        List<String> nodes = readNodes();
        if (!nodes.contains(endpoint)) {
            LOGGER.info("Node doesn't exist in registry: {}", endpoint);
            return;
        }
        nodes.remove(endpoint);
        Gson gson = new Gson();
        writeToFile(PATH_NODES, gson.toJson(nodes));
        LOGGER.info("Node removed from registry: {}", endpoint);
    }

    public static void addLWC(String publicKey) throws Exception {
        List<String> lwcs = readLWCs();
        if (lwcs.contains(publicKey)) {
            LOGGER.info("LWC already exists in registry: {}", publicKey);
            return;
        }
        lwcs.add(publicKey);
        Gson gson = new Gson();
        writeToFile(PATH_LWCS, gson.toJson(lwcs));
        LOGGER.info("LWC added in registry: {}", publicKey);
    }

    public static void removeLWC(String publicKey) throws Exception {
        List<String> lwcs = readLWCs();
        if (!lwcs.contains(publicKey)) {
            LOGGER.info("LWC doesn't exist in registry: {}", publicKey);
            return;
        }
        lwcs.remove(publicKey);
        Gson gson = new Gson();
        writeToFile(PATH_LWCS, gson.toJson(lwcs));
        LOGGER.info("LWC removed from registry: {}", publicKey);
    }

    public static void addAuth(String publicKey) throws Exception {
        List<String> auths = readAuths();
        if (auths.contains(publicKey)) {
            LOGGER.info("Auth already exists in registry: {}", publicKey);
            return;
        }
        auths.add(publicKey);
        Gson gson = new Gson();
        writeToFile(PATH_AUTHS, gson.toJson(auths));
        LOGGER.info("Auth added in registry: {}", publicKey);
    }

    public static void removeAuth(String publicKey) throws Exception {
        List<String> auths = readAuths();
        if (!auths.contains(publicKey)) {
            LOGGER.info("Auth doesn't exist in registry: {}", publicKey);
            return;
        }
        auths.remove(publicKey);
        Gson gson = new Gson();
        writeToFile(PATH_AUTHS, gson.toJson(auths));
        LOGGER.info("Auth removed from registry: {}", publicKey);
    }

    /** Private functions */
    private static void writeToFile(String path, String data) throws IOException {
        FileWriter w = new FileWriter(path);
        w.write(data);
        w.close();
    }

    private static <T> T readFromFile(String path, Class<T> t) throws FileNotFoundException {
        File f = new File(path);
        Scanner r = new Scanner(f);
        String json = "";
        while (r.hasNextLine()) {
            String data = r.nextLine();
            json += data;
        }
        r.close();
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
        r.close();
        return json;
    }
}
